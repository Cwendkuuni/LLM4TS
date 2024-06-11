package Newzgrabber;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BatchDriverTest {
	
	private BatchDriver batchDriver;
	private BatchJob batchJob;
	
	@Before
	public void setUp() throws NullPointerException, IOException {
		String newzini = "path_to_newzini";
		String batchfile = "path_to_batchfile";
		batchDriver = new BatchDriver(newzini, batchfile);
		batchJob = batchDriver.getBatchJob("testGroup");
	}
	
	@Test
	public void testGetGroups() {
		String[] groups = batchDriver.getGroups();
		assertNotNull(groups);
		assertEquals(1, groups.length);
		assertEquals("testGroup", groups[0]);
	}
	
	@Test
	public void testGetBatchJob() {
		assertNotNull(batchJob);
		assertEquals("testGroup", batchJob.getGroup());
	}
	
	@Test
	public void testGetSubjectLines() {
		String[] subjectLines = batchDriver.getSubjectLines();
		assertNull(subjectLines);
	}
}