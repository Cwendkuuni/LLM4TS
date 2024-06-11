package Newzgrabber;

import org.junit.*;
import static org.junit.Assert.*;
import java.io.IOException;

public class BatchDriverTest {

    @Test(expected = NullPointerException.class)
    public void testConstructorNullPointer() throws IOException {
        BatchDriver batchDriver = new BatchDriver(null, null);
    }
    
    @Test(expected = IOException.class)
    public void testConstructorIOException() throws IOException {
        BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
    }

    @Test
    public void testGetGroups() {
        BatchDriver batchDriver = null;
        try {
            batchDriver = new BatchDriver("newzini", "batchfile");
        } catch (IOException e) {
            fail("Constructor threw an exception");
        }
        
        String[] groups = batchDriver.getGroups();
        
        assertNotNull(groups);
        assertEquals(3, groups.length);
        assertEquals("group1", groups[0]);
        assertEquals("group2", groups[1]);
        assertEquals("group3", groups[2]);
    }

    @Test
    public void testGetBatchJob() {
        BatchDriver batchDriver = null;
        try {
            batchDriver = new BatchDriver("newzini", "batchfile");
        } catch (IOException e) {
            fail("Constructor threw an exception");
        }
        
        BatchJob batchJob = batchDriver.getBatchJob("group1");
        
        assertNotNull(batchJob);
        assertEquals("server", batchJob.getServer());
        assertEquals("directory1", batchJob.getDirectory());
        assertEquals(1, batchJob.getFirstId());
        assertEquals(100, batchJob.getLastId());
    }

    @Test
    public void testGetSubjectLines() {
        BatchDriver batchDriver = null;
        try {
            batchDriver = new BatchDriver("newzini", "batchfile");
        } catch (IOException e) {
            fail("Constructor threw an exception");
        }
        
        batchDriver.getBatchJob("group1");
        String[] subjectLines = batchDriver.getSubjectLines();
        
        assertNotNull(subjectLines);
        assertEquals(5, subjectLines.length);
        assertEquals("subject1", subjectLines[0]);
        assertEquals("subject2", subjectLines[1]);
        assertEquals("subject3", subjectLines[2]);
        assertEquals("subject4", subjectLines[3]);
        assertEquals("subject5", subjectLines[4]);
    }

}