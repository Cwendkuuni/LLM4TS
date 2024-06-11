package Newzgrabber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class BatchDriverTest {
	
	private BatchDriver batchDriver;
	
	@Before
	public void setup() throws NullPointerException, IOException {
		this.batchDriver = new BatchDriver("newz.ini", "batchfile.ini");
	}
	
	@Test
	public void testGetGroups() {
		String[] expectedGroups = {"group1", "group2", "group3"};
		String[] actualGroups = this.batchDriver.getGroups();
		assertEquals(expectedGroups.length, actualGroups.length);
		for(int i=0; i<expectedGroups.length; i++) {
			assertEquals(expectedGroups[i], actualGroups[i]);
		}
	}
	
	@Test
	public void testGetBatchJob() {
		String groupName = "group1";
		BatchJob actualBatchJob = this.batchDriver.getBatchJob(groupName);
		assertNull(actualBatchJob);
	}
	
	@Test
	public void testGetSubjectLines() {
		String[] expectedSubjectLines = {"subject1", "subject2", "subject3"};
		String[] actualSubjectLines = this.batchDriver.getSubjectLines();
		assertEquals(expectedSubjectLines.length, actualSubjectLines.length);
		for(int i=0; i<expectedSubjectLines.length; i++) {
			assertEquals(expectedSubjectLines[i], actualSubjectLines[i]);
		}
	}
}