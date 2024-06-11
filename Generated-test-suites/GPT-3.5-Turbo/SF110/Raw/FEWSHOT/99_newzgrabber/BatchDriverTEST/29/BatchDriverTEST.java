package Newzgrabber;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BatchDriverTest {
    private BatchDriver batchDriver;

    @Before
    public void setUp() throws Exception {
        batchDriver = new BatchDriver("newzini", "batchfile");
    }

    @Test
    public void testGetGroups() {
        String[] expected = {};
        assertArrayEquals(expected, batchDriver.getGroups());
    }

    @Test
    public void testGetBatchJob() {
        assertNull(batchDriver.getBatchJob("group"));
    }

    @Test
    public void testGetSubjectLines() {
        assertNull(batchDriver.getSubjectLines());
    }
}