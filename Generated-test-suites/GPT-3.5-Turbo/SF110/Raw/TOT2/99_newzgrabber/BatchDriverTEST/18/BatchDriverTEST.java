import static org.junit.Assert.*;
import org.junit.Test;

public class BatchDriverTest {

    @Test
    public void testConstructor() {
        try {
            BatchDriver driver = new BatchDriver("newzini.txt", "batchfile.txt");
            assertNotNull(driver);
        } catch (Exception e) {
            fail("Constructor should not throw any exceptions");
        }
    }

    @Test
    public void testGetGroups() {
        BatchDriver driver = new BatchDriver("newzini.txt", "batchfile.txt");
        try {
            String[] groups = driver.getGroups();
            assertNotNull(groups);
        } catch (Exception e) {
            fail("getGroups should not throw any exceptions");
        }
    }

    @Test
    public void testGetBatchJob() {
        BatchDriver driver = new BatchDriver("newzini.txt", "batchfile.txt");
        try {
            BatchJob batchJob = driver.getBatchJob("testgroup");
            assertNotNull(batchJob);
        } catch (Exception e) {
            fail("getBatchJob should not throw any exceptions");
        }
    }

    @Test
    public void testGetSubjectLines() {
        BatchDriver driver = new BatchDriver("newzini.txt", "batchfile.txt");
        try {
            String[] subjectLines = driver.getSubjectLines();
            assertNotNull(subjectLines);
        } catch (Exception e) {
            fail("getSubjectLines should not throw any exceptions");
        }
    }
}