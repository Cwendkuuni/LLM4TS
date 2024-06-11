package Newzgrabber;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class BatchDriverTest {

	private BatchDriver batchDriver;

	@Before
	public void setUp() throws NullPointerException, IOException {
		String newzini = "path/to/newzini";
		String batchfile = "path/to/batchfile";
		
		batchDriver = new BatchDriver(newzini, batchfile);
	}

	@Test
	public void testGetGroups() {
		String[] expectedGroups = {"group1", "group2", "group3"};
		assertArrayEquals(expectedGroups, batchDriver.getGroups());
	}

	@Test
	public void testGetBatchJob() {
		String[] expectedSubjectLines = {"subject1", "subject2", "subject3"};
		
		BatchJob batchJob1 = batchDriver.getBatchJob("group1");
		BatchJob batchJob2 = batchDriver.getBatchJob("group2");
		BatchJob batchJob3 = batchDriver.getBatchJob("group3");
		
		assertEquals("server1", batchJob1.getServer());
		assertEquals("directory1", batchJob1.getDirectory());
		assertEquals("username1", batchJob1.getUsername());
		assertEquals("password1", batchJob1.getPassword());
		assertEquals(2, batchJob1.getThreads());
		assertEquals(1000, batchJob1.getTimeout());
		assertArrayEquals(expectedSubjectLines, batchJob1.getSubjectLines());
		
		assertEquals("server2", batchJob2.getServer());
		assertEquals("directory2", batchJob2.getDirectory());
		assertEquals("username2", batchJob2.getUsername());
		assertEquals("password2", batchJob2.getPassword());
		assertEquals(3, batchJob2.getThreads());
		assertEquals(-1, batchJob2.getTimeout());
		assertArrayEquals(expectedSubjectLines, batchJob2.getSubjectLines());
		
		assertEquals("server3", batchJob3.getServer());
		assertEquals("directory3", batchJob3.getDirectory());
		assertEquals("username3", batchJob3.getUsername());
		assertEquals("password3", batchJob3.getPassword());
		assertEquals(1, batchJob3.getThreads());
		assertEquals(-1, batchJob3.getTimeout());
		assertArrayEquals(expectedSubjectLines, batchJob3.getSubjectLines());
	}

	@Test
	public void testGetSubjectLines() {
		String[] expectedSubjectLines = {"subject1", "subject2", "subject3"};
		assertArrayEquals(expectedSubjectLines, batchDriver.getSubjectLines());
	}

}