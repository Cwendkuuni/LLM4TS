package Newzgrabber;

import static org.junit.Assert.*;
import org.junit.Test;

public class BatchDriverTest {

    @Test
    public void testBatchDriverConstructor() {
        try {
            BatchDriver batchDriver = new BatchDriver("newzini.ini", "batchfile.ini");
            assertNotNull(batchDriver);
        } catch (Exception e) {
            fail("Constructor should not throw an exception");
        }
    }
    
    @Test
    public void testGetGroups() {
        BatchDriver batchDriver = new BatchDriver("newzini.ini", "batchfile.ini");
        String[] groups = batchDriver.getGroups();
        assertNotNull(groups);
    }
    
    @Test
    public void testGetBatchJob() {
        BatchDriver batchDriver = new BatchDriver("newzini.ini", "batchfile.ini");
        BatchJob batchJob = batchDriver.getBatchJob("group1");
        assertNotNull(batchJob);
    }
    
    @Test
    public void testGetSubjectLines() {
        BatchDriver batchDriver = new BatchDriver("newzini.ini", "batchfile.ini");
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNotNull(subjectLines);
    }

}