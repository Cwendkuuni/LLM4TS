package Newzgrabber;

import org.junit.Test;
import static org.junit.Assert.*;

public class BatchDriverTest {

    @Test
    public void testConstructor() throws Exception {
        // Test with valid input
        BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
        assertNotNull(batchDriver);
        
        // Test with null newzini
        try {
            batchDriver = new BatchDriver(null, "batchfile");
            fail("NullPointerException should have been thrown");
        } catch (NullPointerException e) {
            assertNotNull(e.getMessage());
        }
        
        // Test with null batchfile
        try {
            batchDriver = new BatchDriver("newzini", null);
            fail("NullPointerException should have been thrown");
        } catch (NullPointerException e) {
            assertNotNull(e.getMessage());
        }
        
        // Test with invalid newzini
        try {
            batchDriver = new BatchDriver("invalid_newzini", "batchfile");
            fail("IOException should have been thrown");
        } catch (IOException e) {
            assertNotNull(e.getMessage());
        }
        
        // Test with invalid batchfile
        try {
            batchDriver = new BatchDriver("newzini", "invalid_batchfile");
            fail("IOException should have been thrown");
        } catch (IOException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testGetGroups() throws Exception {
        BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
        
        // Test with populated BatchgroupsVector
        String[] groups = batchDriver.getGroups();
        assertEquals(0, groups.length);
        
        // Test with empty BatchgroupsVector
        batchDriver.BatchgroupsVector = new Vector();
        groups = batchDriver.getGroups();
        assertEquals(0, groups.length);
    }
    
    @Test
    public void testGetBatchJob() throws Exception {
        BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
        
        // Test with valid group
        BatchJob batchJob = batchDriver.getBatchJob("valid_group");
        assertNotNull(batchJob);
        
        // Test with invalid group
        batchJob = batchDriver.getBatchJob("invalid_group");
        assertNull(batchJob);
    }

    @Test
    public void testGetSubjectLines() throws Exception {
        BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
        
        // Test without setting subjectlines
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNull(subjectLines);
        
        // Test with setting subjectlines
        batchDriver.subjectlines = new String[] {"subject1", "subject2"};
        subjectLines = batchDriver.getSubjectLines();
        assertNotNull(subjectLines);
        assertEquals(2, subjectLines.length);
        assertEquals("subject1", subjectLines[0]);
        assertEquals("subject2", subjectLines[1]);
    }
}