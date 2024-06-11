package Newzgrabber;

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

    @Test
    public void testGetGroups() {
        String[] expectedGroups = {"group1", "group2", "group3"};
        assertArrayEquals(expectedGroups, batchDriver.getGroups());
    }

    @Test
    public void testGetBatchJob() {
        BatchJob batchJob = batchDriver.getBatchJob("group1");
        assertNotNull(batchJob);
        assertEquals("server1", batchJob.getServer());
        assertEquals("directory1", batchJob.getDirectory());
        assertNull(batchDriver.getBatchJob("invalidGroup"));
    }

    @Test
    public void testGetSubjectLines() {
        String[] expectedSubjectLines = {"subject1", "subject2", "subject3"};
        assertArrayEquals(expectedSubjectLines, batchDriver.getSubjectLines());
    }
}