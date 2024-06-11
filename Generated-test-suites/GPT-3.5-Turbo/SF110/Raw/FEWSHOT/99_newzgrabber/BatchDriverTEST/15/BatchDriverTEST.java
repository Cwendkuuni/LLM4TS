package Newzgrabber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class BatchDriverTest {
    @Test
    public void testGetGroups() {
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        String[] expectedGroups = {"group1", "group2", "group3"};
        assertEquals(expectedGroups, batchDriver.getGroups());
    }
    
    @Test
    public void testGetBatchJob() {
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        BatchJob expectedBatchJob1 = new BatchJob("server1", "group1", "directory1", 0, 100);
        assertEquals(expectedBatchJob1, batchDriver.getBatchJob("group1"));
        
        BatchJob expectedBatchJob2 = new BatchJob("server2", "group2", "directory2", 0, 200);
        assertEquals(expectedBatchJob2, batchDriver.getBatchJob("group2"));
        
        assertNull(batchDriver.getBatchJob("group3"));
    }
    
    @Test
    public void testGetSubjectLines() {
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        String[] expectedSubjectLines = {"subject1", "subject2", "subject3"};
        assertEquals(expectedSubjectLines, batchDriver.getSubjectLines());
    }
}