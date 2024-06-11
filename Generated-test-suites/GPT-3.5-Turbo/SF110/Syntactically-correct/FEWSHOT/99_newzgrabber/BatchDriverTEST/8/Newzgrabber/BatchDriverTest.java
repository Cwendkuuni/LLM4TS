package Newzgrabber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class BatchDriverTest {
    @Test
    public void testGetGroups() {
        BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
        String[] expected = {};
        assertEquals(expected, batchDriver.getGroups());
    }

    @Test
    public void testGetBatchJob() {
        BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
        BatchJob expected = null;
        assertEquals(expected, batchDriver.getBatchJob(null));
    }

    @Test
    public void testGetSubjectLines() {
        BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
        String[] expected = null;
        assertEquals(expected, batchDriver.getSubjectLines());
    }
}