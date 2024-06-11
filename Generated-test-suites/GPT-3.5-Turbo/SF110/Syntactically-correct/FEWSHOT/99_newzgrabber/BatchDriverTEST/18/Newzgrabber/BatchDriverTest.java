package Newzgrabber;

import static org.junit.Assert.*;
import org.junit.Test;

public class BatchDriverTest {
    @Test
    public void testConstructor() {
        try {
            BatchDriver driver = new BatchDriver("newzini", "batchfile");
            assertNotNull(driver.getGroups());
            assertNotNull(driver.getBatchJob("group"));
            assertNotNull(driver.getSubjectLines());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetGroups() {
        try {
            BatchDriver driver = new BatchDriver("newzini", "batchfile");
            String[] groups = driver.getGroups();
            assertNotNull(groups);
            assertEquals(0, groups.length);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetBatchJob() {
        try {
            BatchDriver driver = new BatchDriver("newzini", "batchfile");
            BatchJob job = driver.getBatchJob("group");
            assertNull(job);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetSubjectLines() {
        try {
            BatchDriver driver = new BatchDriver("newzini", "batchfile");
            String[] subjectLines = driver.getSubjectLines();
            assertNotNull(subjectLines);
            assertEquals(0, subjectLines.length);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}