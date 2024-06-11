package Newzgrabber;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BatchDriverTest {

    private BatchDriver batchDriver;

    @Before
    public void setUp() {
        try {
            batchDriver = new BatchDriver("newzini", "batchfile");
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        batchDriver = null;
    }

    @Test
    public void testGetGroups() {
        try {
            String[] groups = batchDriver.getGroups();
            assertNotNull(groups);
            assertEquals(0, groups.length);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetBatchJob() {
        BatchJob batchJob = batchDriver.getBatchJob("group");
        assertNotNull(batchJob);
        assertNotNull(batchJob.getServer());
        assertNotNull(batchJob.getGroup());
        assertNotNull(batchJob.getDirectory());
        assertTrue(batchJob.getFirstId() >= 0);
        assertTrue(batchJob.getLastId() >= 0);
    }

    @Test
    public void testGetSubjectLines() {
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNull(subjectLines);
    }
}