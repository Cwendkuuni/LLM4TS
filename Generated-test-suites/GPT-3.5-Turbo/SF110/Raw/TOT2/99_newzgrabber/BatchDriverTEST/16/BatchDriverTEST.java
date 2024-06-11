package Newzgrabber;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

public class BatchDriverTest {
    
    private BatchDriver batchDriver;
    
    @Test
    public void testConstructor() {
        try {
            batchDriver = new BatchDriver("newzini", "batchfile");
        } catch (NullPointerException | IOException e) {
            fail("Constructor should not throw any exceptions");
        }
    }
    
    @Test
    public void testGetGroups() {
        String[] groups = batchDriver.getGroups();
        assertNotNull(groups);
    }
    
    @Test
    public void testGetBatchJob() {
        BatchJob batchJob = batchDriver.getBatchJob("group1");
        assertNotNull(batchJob);
    }
    
    @Test
    public void testGetSubjectLines() {
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNotNull(subjectLines);
    }
    
    // Edge Cases and Exception Handling
    
    @Test
    public void testConstructorWithNullParameters() {
        assertThrows(NullPointerException.class, () -> {
            new BatchDriver(null, null);
        });
    }
    
    @Test
    public void testGetBatchJobWithInvalidGroup() {
        BatchJob batchJob = batchDriver.getBatchJob("invalidGroup");
        assertNull(batchJob);
    }
    
    @Test
    public void testGetSubjectLinesWithNoData() {
        batchDriver = new BatchDriver("newzini", "batchfile");
        String[] subjectLines = batchDriver.getSubjectLines();
        assertEquals(0, subjectLines.length);
    }
}