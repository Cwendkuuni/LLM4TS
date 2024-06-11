package Newzgrabber;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class BatchDriverTest {

    private BatchDriver batchDriver;

    @Before
    public void setUp() throws Exception {
        batchDriver = new BatchDriver("newzini", "batchfile");
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullPointer() throws NullPointerException, IOException {
        BatchDriver batchDriver = new BatchDriver(null, null);
    }

    @Test(expected = IOException.class)
    public void testConstructorIOException() throws NullPointerException, IOException {
        BatchDriver batchDriver = new BatchDriver("newzini", "nonexistentfile");
    }

    @Test
    public void testGetGroups() throws ArrayIndexOutOfBoundsException {
        String[] groups = batchDriver.getGroups();
        assertNotNull(groups);
        assertTrue(groups.length > 0);
    }

    @Test
    public void testGetBatchJob() {
        BatchJob batchJob = batchDriver.getBatchJob("group");
        assertNotNull(batchJob);
        assertNotNull(batchJob.getServer());
        assertNotNull(batchJob.getDirectory());
        assertEquals("group", batchJob.getGroup());
        assertTrue(batchJob.getFirstId() > 0);
        assertTrue(batchJob.getLastId() > 0);
    }

    @Test
    public void testGetSubjectLines() {
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNotNull(subjectLines);
        assertTrue(subjectLines.length > 0);
    }
}