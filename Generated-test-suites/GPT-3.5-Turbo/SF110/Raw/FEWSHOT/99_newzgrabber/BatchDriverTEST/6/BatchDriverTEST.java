package Newzgrabber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

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
        String[] expected = {"group1", "group2", "group3"};
        String[] actual = batchDriver.getGroups();
        assertNotNull(actual);
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
    
    @Test
    public void testGetBatchJob() {
        String group = "group1";
        BatchJob batchJob = batchDriver.getBatchJob(group);
        assertNotNull(batchJob);
        assertEquals(group, batchJob.getGroup());
        // Add more assertions as needed for BatchJob properties
    }
    
    @Test
    public void testGetSubjectLines() {
        String[] subjectlines = batchDriver.getSubjectLines();
        assertNotNull(subjectlines);
        // Add more assertions as needed for subjectlines
    }
    
    // Add more test cases for all methods in BatchDriver
    
}