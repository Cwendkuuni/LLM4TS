
package Newzgrabber;

import org.junit.Test;
import static org.junit.Assert.*;

public class BatchDriverTest {

    @Test
    public void testConstructor() {
        try {
            BatchDriver batchDriver = new BatchDriver(null, null);
            fail("Should throw a NullPointerException");
        } catch (NullPointerException e) {
            // Test passed
        } catch (IOException e) {
            fail("Should throw a NullPointerException first");
        }
        
        try {
            BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
        } catch (NullPointerException e) {
            fail("Should not throw a NullPointerException");
        } catch (IOException e) {
            fail("Should not throw an IOException");
        }
    }
    
    @Test
    public void testGetGroups() {
        BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
        
        try {
            String[] groups = batchDriver.getGroups();
            assertEquals(0, groups.length);
        } catch (ArrayIndexOutOfBoundsException e) {
            fail("Should not throw an ArrayIndexOutOfBoundsException");
        }
        
        batchDriver.BatchgroupsVector.add(new String[]{"group1"});
        batchDriver.BatchgroupsVector.add(new String[]{"group2"});
        
        try {
            String[] groups = batchDriver.getGroups();
            assertEquals(2, groups.length);
            assertEquals("group1", groups[0]);
            assertEquals("group2", groups[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            fail("Should not throw an ArrayIndexOutOfBoundsException");
        }
    }
    
    @Test
    public void testGetBatchJob() {
        BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
        
        BatchJob batchJob = batchDriver.getBatchJob("group1");
        assertNull(batchJob);
        
        batchDriver.BatchgroupsHash.put("group1", "directory1");
        
        batchDriver.OptionsHash.put("server", "server1");
        batchJob = batchDriver.getBatchJob("group1");
        assertNull(batchJob);
        
        batchDriver.OptionsHash.put("username", "username1");
        batchJob = batchDriver.getBatchJob("group1");
        assertNull(batchJob);
        
        batchDriver.OptionsHash.put("password", "password1");
        batchJob = batchDriver.getBatchJob("group1");
        assertNull(batchJob);
        
        batchDriver.OptionsHash.put("threads", "2");
        batchJob = batchDriver.getBatchJob("group1");
        assertNull(batchJob);
        
        batchDriver.OptionsHash.put("timeout", "1000");
        batchJob = batchDriver.getBatchJob("group1");
        assertNull(batchJob);
        
        batchDriver.NewsgroupsHash.put("group1", "1");
        batchJob = batchDriver.getBatchJob("group1");
        assertNotNull(batchJob);
        
    }
    
    @Test
    public void testGetSubjectLines() {
        BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
        
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNull(subjectLines);
        
        batchDriver.subjectlines = new String[] {"subject1", "subject2"};
        
        subjectLines = batchDriver.getSubjectLines();
        assertNotNull(subjectLines);
        assertEquals(2, subjectLines.length);
        assertEquals("subject1", subjectLines[0]);
        assertEquals("subject2", subjectLines[1]);
    }
}

