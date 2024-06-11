package Newzgrabber;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BatchDriverTest {

    private BatchDriver batchDriver;

    @Before
    public void setUp() throws Exception {
        batchDriver = new BatchDriver("newzini", "batchfile");
    }

    @Test
    public void testGetGroups() {
        String[] expected = {"group1", "group2", "group3"};
        assertArrayEquals(expected, batchDriver.getGroups());
    }

    @Test
    public void testGetBatchJob() {
        BatchJob batchJob = batchDriver.getBatchJob("group1");
        assertNotNull(batchJob);
    }

    @Test
    public void testGetSubjectLines() {
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNotNull(subjectLines);
    }
}