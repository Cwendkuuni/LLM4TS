package Newzgrabber;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class BatchDriverTest {
    
    private BatchDriver batchDriver;
    
    @Before
    public void setUp() throws Exception {
        batchDriver = new BatchDriver("newzini", "batchfile");
    }
    
    @Test
    public void testGetGroups() throws ArrayIndexOutOfBoundsException {
        String[] groups = batchDriver.getGroups();
        
        assertNotNull(groups);
        assertEquals(0, groups.length);
    }
    
    @Test
    public void testGetBatchJob() {
        BatchJob batchJob = batchDriver.getBatchJob("group");
        
        assertNotNull(batchJob);
        assertEquals("server", batchJob.getServer());
        assertEquals("directory", batchJob.getDirectory());
        assertNotNull(batchJob.getUsername());
        assertNotNull(batchJob.getPassword());
        assertEquals(1, batchJob.getThreads());
        assertEquals(-1, batchJob.getTimeout());
    }
    
    @Test
    public void testGetSubjectLines() {
        String[] subjectLines = batchDriver.getSubjectLines();
        
        assertNotNull(subjectLines);
        assertEquals(0, subjectLines.length);
    }
}