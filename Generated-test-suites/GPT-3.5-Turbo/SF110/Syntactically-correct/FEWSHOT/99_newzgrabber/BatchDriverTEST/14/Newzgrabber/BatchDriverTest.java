package Newzgrabber;

import static org.junit.Assert.*;
import java.io.IOException;
import org.junit.Test;

public class BatchDriverTest {
    
    @Test(expected = NullPointerException.class)
    public void testBatchDriverConstructorWithNullNewzini() throws NullPointerException, IOException {
        String newzini = null;
        String batchfile = "path/to/batchfile";
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
    }
    
    @Test(expected = IOException.class)
    public void testBatchDriverConstructorWithNullBatchfile() throws NullPointerException, IOException {
        String newzini = "path/to/newzini";
        String batchfile = null;
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
    }
    
    @Test
    public void testBatchDriverConstructor() throws NullPointerException, IOException {
        String newzini = "path/to/newzini";
        String batchfile = "path/to/batchfile";
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        
        assertNotNull(batchDriver);
    }
    
    @Test
    public void testGetGroups() throws ArrayIndexOutOfBoundsException {
        BatchDriver batchDriver = new BatchDriver("path/to/newzini", "path/to/batchfile");
        
        String[] groups = batchDriver.getGroups();
        
        assertEquals(0, groups.length);
    }
    
    @Test
    public void testGetBatchJobWithValidGroup() {
        BatchDriver batchDriver = new BatchDriver("path/to/newzini", "path/to/batchfile");
        
        BatchJob batchJob = batchDriver.getBatchJob("groupname");
        
        assertNotNull(batchJob);
    }
    
    @Test
    public void testGetBatchJobWithInvalidGroup() {
        BatchDriver batchDriver = new BatchDriver("path/to/newzini", "path/to/batchfile");
        
        BatchJob batchJob = batchDriver.getBatchJob("invalidgroup");
        
        assertNull(batchJob);
    }
    
    @Test
    public void testGetSubjectLines() {
        BatchDriver batchDriver = new BatchDriver("path/to/newzini", "path/to/batchfile");
        
        String[] subjectLines = batchDriver.getSubjectLines();
        
        assertNull(subjectLines);
    }
}