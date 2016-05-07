/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.openmrs.Program;
import org.openmrs.api.db.ProgramWorkflowDAO;
import org.openmrs.api.impl.ProgramWorkflowServiceImpl;
import org.openmrs.test.Verifies;

import org.junit.Ignore;
import org.openmrs.ConceptStateConversion;
import org.openmrs.Concept;
import org.openmrs.ProgramWorkflow;
import org.openmrs.ProgramWorkflowState;
import org.openmrs.Program;
import org.openmrs.api.context.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * This class unit tests methods in the ProgramWorkflowService class.
 * Unlike ProgramWorkflowServiceTest, this class does not extend
 * BaseContextSensitiveTest so as not to auto-wire the dependencies
 * of PatientService, hence implementing true unit (and not integration) tests
 */
public class ProgramWorkflowServiceUnitTest {
	
	private ProgramWorkflowService pws;
	
	@Before
	public void setup() {
		pws = new ProgramWorkflowServiceImpl();
	}
	
	@Test
	@Verifies(value = "should call the DAO method getProgramsByName", method = "getProgramByName")
	public void getProgramByName_shouldCallDaoGetProgramsByName() {
		ProgramWorkflowDAO mockDao = Mockito.mock(ProgramWorkflowDAO.class);
		pws.setProgramWorkflowDAO(mockDao);
		pws.getProgramByName("A name");
		Mockito.verify(mockDao).getProgramsByName("A name", false);
		Mockito.verify(mockDao).getProgramsByName("A name", true);
	}
	
	@Test
	@Verifies(value = "should return null when DAO returns an empty list", method = "getProgramByName")
	public void getProgramByName_shouldReturnNullWhenThereIsNoProgramForGivenName() {
		ProgramWorkflowDAO mockDao = Mockito.mock(ProgramWorkflowDAO.class);
		List<Program> noProgramWithGivenName = new ArrayList<Program>();
		Mockito.stub(mockDao.getProgramsByName("A name", false)).toReturn(noProgramWithGivenName);
		Mockito.stub(mockDao.getProgramsByName("A name", true)).toReturn(noProgramWithGivenName);
		pws.setProgramWorkflowDAO(mockDao);
		Assert.assertNull(pws.getProgramByName("A name"));
	}
	
	@Test(expected = org.openmrs.api.ProgramNameDuplicatedException.class)
	@Verifies(value = "should fail when two programs found with same name", method = "getProgramByName()")
	public void getProgramByName_shouldFailWhenTwoProgramsFoundWithSameName() {
		ProgramWorkflowDAO mockDao = Mockito.mock(ProgramWorkflowDAO.class);
		List<Program> programsWithGivenName = new ArrayList<Program>();
		Program program1 = new Program("A name");
		Program program2 = new Program("A name");
		programsWithGivenName.add(program1);
		programsWithGivenName.add(program2);
		Mockito.stub(mockDao.getProgramsByName("A name", false)).toReturn(programsWithGivenName);
		Mockito.stub(mockDao.getProgramsByName("A name", true)).toReturn(programsWithGivenName);
		pws.setProgramWorkflowDAO(mockDao);
		pws.getProgramByName("A name");
	}

	@Test(expected = org.openmrs.api.APIException.class)
	@Verifies(value = "should fail when ProgramWorkflowState is null", method = "saveConceptStateConversion()")
	public void saveConceptStateConversion_shouldFailWhenProgramWorkflowStateIsNull() throws Exception {
		ProgramWorkflowDAO mockDao = Mockito.mock(ProgramWorkflowDAO.class);
		pws.setProgramWorkflowDAO(mockDao);

		ConceptStateConversion csc = new ConceptStateConversion();
		csc.setConcept(new Concept());
		csc.setProgramWorkflow(new ProgramWorkflow());
		csc.setProgramWorkflowState(null);

		pws.saveConceptStateConversion(csc);
	}

	@Test(expected = org.openmrs.api.APIException.class)
	@Verifies(value = "should fail when ProgramWorkflow is null", method = "saveConceptStateConversion()")
	public void saveConceptStateConversion_shouldFailWhenProgramWorkflowIsNull() throws Exception {
		ProgramWorkflowDAO mockDao = Mockito.mock(ProgramWorkflowDAO.class);
		pws.setProgramWorkflowDAO(mockDao);

		ConceptStateConversion csc = new ConceptStateConversion();
		csc.setConcept(new Concept());
		csc.setProgramWorkflow(null);
		csc.setProgramWorkflowState(new ProgramWorkflowState());

		pws.saveConceptStateConversion(csc);
	}

	@Test(expected = org.openmrs.api.APIException.class)
	@Verifies(value = "should fail when Concept is null", method = "saveConceptStateConversion()")
	public void saveConceptStateConversion_shouldFailWhenConceptIsNull() throws Exception {
		ProgramWorkflowDAO mockDao = Mockito.mock(ProgramWorkflowDAO.class);
		pws.setProgramWorkflowDAO(mockDao);

		ConceptStateConversion csc = new ConceptStateConversion();
		csc.setConcept(null);
		csc.setProgramWorkflow(new ProgramWorkflow());
		csc.setProgramWorkflowState(new ProgramWorkflowState());

		pws.saveConceptStateConversion(csc);
	}

	@Test
	@Verifies(value = "should call the DAO method saveConceptStateConversion", method = "saveConceptStateConversion()")
	public void saveConceptStateConversion_shouldCallDaoSaveConceptStateConversion() {
		ProgramWorkflowDAO mockDao = Mockito.mock(ProgramWorkflowDAO.class);
		pws.setProgramWorkflowDAO(mockDao);

		ConceptStateConversion csc = new ConceptStateConversion();
		csc.setConcept(new Concept());
		csc.setProgramWorkflow(new ProgramWorkflow());
		csc.setProgramWorkflowState(new ProgramWorkflowState());

		pws.saveConceptStateConversion(csc);
		Mockito.verify(mockDao).saveConceptStateConversion(csc);
	}

		
	@Test(expected = org.openmrs.api.APIException.class)
	@Verifies(value = "saveProgram should fail when program concept is null", method = "saveProgram()")
	public void saveProgram_shouldFailWhenProgramConceptIsNull() throws Exception{

		ProgramWorkflowDAO mockDao = Mockito.mock(ProgramWorkflowDAO.class);
		List<Program> programsWithGivenName = new ArrayList<Program>();
		Program program1 = new Program("A name");
		programsWithGivenName.add(program1);
		pws.setProgramWorkflowDAO(mockDao);

		pws.saveProgram(program1);
	}


	@Test
	@Verifies(value = "saveProgram should pass when program concept is not null and has no ProgramWorkflow", method = "saveProgram()")
	public void saveProgram_shouldPassWhenProgramConceptIsNotNullAndHasNoProgramWorkflow() {

		ProgramWorkflowDAO mockDao = Mockito.mock(ProgramWorkflowDAO.class);
		List<Program> programsWithGivenName = new ArrayList<Program>();
		Program program1 = new Program("A name");
		programsWithGivenName.add(program1);
		pws.setProgramWorkflowDAO(mockDao);

		program1.setConcept(new Concept());
		pws.saveProgram(program1);
	}

	@Test(expected = org.openmrs.api.APIException.class)
	@Verifies(value = "saveProgram should fail when program concept is not null and ProgramWorkflow concept is null", method = "saveProgram()")
	public void saveProgram_shouldFailWhenProgramConceptIsNotNullAndProgramWorkflowConceptIsNull() throws Exception{

		ProgramWorkflowDAO mockDao = Mockito.mock(ProgramWorkflowDAO.class);
		List<Program> programsWithGivenName = new ArrayList<Program>();
		Program program1 = new Program("A name");
		programsWithGivenName.add(program1);
		pws.setProgramWorkflowDAO(mockDao);

		program1.setConcept(new Concept());

		ProgramWorkflow workflow = new ProgramWorkflow(1);
		program1.addWorkflow(workflow);

		pws.saveProgram(program1);
	}

	@Test
	@Verifies(value = "saveProgram should pass when program concept is not null and ProgramWorkflow concept is not null", method = "saveProgram()")
	public void saveProgram_shouldFailWhenProgramConceptIsNotNullAndProgramWorkflowConceptIsNotNull() {

		ProgramWorkflowDAO mockDao = Mockito.mock(ProgramWorkflowDAO.class);
		List<Program> programsWithGivenName = new ArrayList<Program>();
		Program program1 = new Program("A name");
		programsWithGivenName.add(program1);
		pws.setProgramWorkflowDAO(mockDao);

		program1.setConcept(new Concept());

		ProgramWorkflow workflow = new ProgramWorkflow(1);
		workflow.setConcept(new Concept());
		program1.addWorkflow(workflow);

		pws.saveProgram(program1);
	}

}
