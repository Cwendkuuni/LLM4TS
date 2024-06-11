import static org.junit.Assert.*;
import org.junit.Test;

public class BatchDriverTest {

    @Test
    public void testConstructor() {
        try {
            BatchDriver driver = new BatchDriver("newzini.txt", "batchfile.txt");
            assertNotNull(driver);
        } catch (Exception e) {
            fail("Constructor test failed: " + e.getMessage());
        }
    }

    @Test
    public void testGetGroups() {
        BatchDriver driver = new BatchDriver("newzini.txt", "batchfile.txt");
        String[] groups = driver.getGroups();
        assertNotNull(groups);
        assertTrue(groups.length > 0);
    }

    @Test
    public void testGetBatchJob() {
        BatchDriver driver = new BatchDriver("newzini.txt", "batchfile.txt");
        String group = "exampleGroup";
        BatchJob batchJob = driver.getBatchJob(group);
        assertNotNull(batchJob);
    }

    @Test
    public void testGetSubjectLines() {
        BatchDriver driver = new BatchDriver("newzini.txt", "batchfile.txt");
        String[] subjectLines = driver.getSubjectLines();
        assertNull(subjectLines);
    }
}