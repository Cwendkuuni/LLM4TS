package Newzgrabber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

public class BatchDriverTest {
    
    @Test
    public void testConstructor() throws NullPointerException, IOException {
        BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
        assertNotNull(batchDriver.getGroups());
        assertNotNull(batchDriver.getBatchJob("group"));
        assertNotNull(batchDriver.getSubjectLines());
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructor_NullNewzini() throws NullPointerException, IOException {
        BatchDriver batchDriver = new BatchDriver(null, "batchfile");
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructor_NullBatchfile() throws NullPointerException, IOException {
        BatchDriver batchDriver = new BatchDriver("newzini", null);
    }
    
    @Test(expected = IOException.class)
    public void testConstructor_InvalidNewzini() throws NullPointerException, IOException {
        BatchDriver batchDriver = new BatchDriver("invalid", "batchfile");
    }
    
    @Test(expected = IOException.class)
    public void testConstructor_InvalidBatchfile() throws NullPointerException, IOException {
        BatchDriver batchDriver = new BatchDriver("newzini", "invalid");
    }
    
    @Test
    public void testGetGroups() throws ArrayIndexOutOfBoundsException {
        BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
        assertNotNull(batchDriver.getGroups());
        assertEquals(0, batchDriver.getGroups().length);
    }
    
    @Test
    public void testGetBatchJob() {
        BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
        assertNotNull(batchDriver.getBatchJob("group"));
        assertNull(batchDriver.getBatchJob("invalidGroup"));
    }
    
    @Test
    public void testGetSubjectLines() {
        BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
        assertNotNull(batchDriver.getSubjectLines());
        assertEquals(0, batchDriver.getSubjectLines().length);
    }
}