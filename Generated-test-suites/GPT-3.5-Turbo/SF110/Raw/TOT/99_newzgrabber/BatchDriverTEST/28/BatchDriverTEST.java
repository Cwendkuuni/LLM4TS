import org.junit.*;
import static org.junit.Assert.*;

public class BatchDriverTest {

    @Test
    public void testConstructor() {
        try {
            BatchDriver batchDriver = new BatchDriver("newz.ini", "batchfile.ini");
            assertNotNull(batchDriver);
        } catch (Exception e) {
            fail("Constructor test failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetGroups() {
        try {
            BatchDriver batchDriver = new BatchDriver("newz.ini", "batchfile.ini");
            String[] groups = batchDriver.getGroups();
            assertNotNull(groups);
            assertEquals(2, groups.length);
        } catch (Exception e) {
            fail("getGroups test failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetBatchJob() {
        try {
            BatchDriver batchDriver = new BatchDriver("newz.ini", "batchfile.ini");
            BatchJob batchJob = batchDriver.getBatchJob("Group1");
            assertNotNull(batchJob);
            assertEquals("server1", batchJob.getServer());
            assertEquals("directory1", batchJob.getDirectory());
            assertEquals(1, batchJob.getFirstId());
            assertEquals(10, batchJob.getLastId());
            assertNull(batchJob.getUsername());
            assertNull(batchJob.getPassword());
        } catch (Exception e) {
            fail("getBatchJob test failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetSubjectLines() {
        try {
            BatchDriver batchDriver = new BatchDriver("newz.ini", "batchfile.ini");
            BatchJob batchJob = batchDriver.getBatchJob("Group1");
            String[] subjectLines = batchDriver.getSubjectLines();
            assertNotNull(subjectLines);
            assertEquals(5, subjectLines.length);
        } catch (Exception e) {
            fail("getSubjectLines test failed: " + e.getMessage());
        }
    }
}