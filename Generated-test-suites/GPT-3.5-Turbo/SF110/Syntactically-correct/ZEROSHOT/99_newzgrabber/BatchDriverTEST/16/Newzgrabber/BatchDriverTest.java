package Newzgrabber;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;

public class BatchDriverTest {
    
    private BatchDriver batchDriver;

    @Before
    public void setUp() throws IOException {
        batchDriver = new BatchDriver("newzini", "batchfile");
    }

    @Test
    public void testGetGroups() {
        String[] expected = {"group1", "group2", "group3"};
        String[] actual = batchDriver.getGroups();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetBatchJob() {
        String group = "group1";
        BatchJob expected = batchDriver.getBatchJob(group);
        assertNotNull(expected);
    }

    @Test
    public void testGetSubjectLines() {
        String[] expected = {};
        String[] actual = batchDriver.getSubjectLines();
        assertArrayEquals(expected, actual);
    }
}