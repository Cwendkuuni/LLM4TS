import org.junit.*;
import static org.junit.Assert.*;

public class BatchDriverTest {

    @Test
    public void testConstructor() {
        try {
            BatchDriver driver = new BatchDriver("newzini", "batchfile");
            assertNotNull(driver);
        } catch (Exception e) {
            fail("Constructor threw exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetGroups() {
        BatchDriver driver = new BatchDriver("newzini", "batchfile");
        String[] groups = driver.getGroups();
        assertNotNull(groups);
    }

    @Test
    public void testGetBatchJob() {
        BatchDriver driver = new BatchDriver("newzini", "batchfile");
        BatchJob job = driver.getBatchJob("group");
        assertNotNull(job);
    }

    @Test
    public void testGetSubjectLines() {
        BatchDriver driver = new BatchDriver("newzini", "batchfile");
        String[] subjectLines = driver.getSubjectLines();
        assertNotNull(subjectLines);
    }
}