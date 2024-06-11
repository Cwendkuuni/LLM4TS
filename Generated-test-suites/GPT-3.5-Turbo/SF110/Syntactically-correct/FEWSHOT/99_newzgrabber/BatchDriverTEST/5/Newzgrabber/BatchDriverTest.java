package Newzgrabber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class BatchDriverTest {
    private BatchDriver batchDriver;
    
    @Before
    public void setUp() {
        // Instantiate BatchDriver object with necessary parameters
        batchDriver = new BatchDriver("newz.ini", "batchfile.ini");
    }
    
    @Test
    public void testGetGroups() {
        // Verify if the groups returned by getGroups() method are as expected
        String[] expectedGroups = {"group1", "group2", "group3"};
        assertArrayEquals(expectedGroups, batchDriver.getGroups());
    }
    
    @Test
    public void testGetBatchJob() {
        // Test case when valid group is provided
        String group = "group1";
        BatchJob batchJob = batchDriver.getBatchJob(group);
        
        // Verify if the returned BatchJob object is not null
        assertNotNull(batchJob);
        
        // Verify if the server and directory values in BatchJob object are correct
        assertEquals("expected server", batchJob.getServer());
        assertEquals("expected directory", batchJob.getDirectory());
        
        // Verify if the username and password values in BatchJob object are correct
        assertEquals("expected username", batchJob.getUsername());
        assertEquals("expected password", batchJob.getPassword());
        
        // Verify if the number of threads in BatchJob object is correct
        assertEquals(3, batchJob.getThreads());
        
        // Verify if the timeout value in BatchJob object is correct
        assertEquals(10, batchJob.getTimeout());
        
        // Verify if the first and last id values in BatchJob object are correct
        assertEquals(100, batchJob.getFirstId());
        assertEquals(200, batchJob.getLastId());
        
        // Test case when invalid group is provided
        String invalidGroup = "invalidGroup";
        BatchJob invalidBatchJob = batchDriver.getBatchJob(invalidGroup);
        
        // Verify if the returned BatchJob object is null
        assertNull(invalidBatchJob);
    }
    
    @Test
    public void testGetSubjectLines() {
        // Verify if the subject lines returned by getSubjectLines() method are not null
        assertNotNull(batchDriver.getSubjectLines());
        
        // Verify if the number of subject lines returned by getSubjectLines() method is correct
        assertEquals(10, batchDriver.getSubjectLines().length);
    }
}