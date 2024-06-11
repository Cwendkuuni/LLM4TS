package Newzgrabber;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BatchDriverTest {

    private BatchDriver batchDriver;
    private String newzini = "path/to/newzini";
    private String batchfile = "path/to/batchfile";

    @Before
    public void setUp() throws Exception {
        batchDriver = new BatchDriver(newzini, batchfile);
    }

    @After
    public void tearDown() {
        batchDriver = null;
    }

    @Test
    public void testConstructor() throws Exception {
        assertNotNull(batchDriver.getGroups());
    }

    @Test
    public void testGetGroups() {
        String[] groups = batchDriver.getGroups();
        assertNotNull(groups);
        assertEquals(2, groups.length);
        assertEquals("group1", groups[0]);
        assertEquals("group2", groups[1]);
    }

    @Test
    public void testGetBatchJob() {
        String group = "group1";
        BatchJob job = batchDriver.getBatchJob(group);
        assertNotNull(job);
        assertEquals("server", job.getServer());
        assertEquals("directory1", job.getDirectory());
        assertEquals(1, job.getThreads());
        assertEquals(-1, job.getTimeout());
    }

    @Test
    public void testGetSubjectLines() {
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNotNull(subjectLines);
    }
}