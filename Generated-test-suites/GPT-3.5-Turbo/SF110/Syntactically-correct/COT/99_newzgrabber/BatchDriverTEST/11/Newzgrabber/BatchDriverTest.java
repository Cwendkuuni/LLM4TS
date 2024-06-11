import org.junit.Test;
import static org.junit.Assert.*;

public class BatchDriverTest {

    @Test
    public void testConstructor() {
        try {
            BatchDriver driver = new BatchDriver("newzini", "batchfile");
            assertNotNull(driver);
        } catch (NullPointerException e) {
            fail("NullPointerException thrown");
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testGetGroups() {
        BatchDriver driver = new BatchDriver("newzini", "batchfile");
        try {
            String[] groups = driver.getGroups();
            assertNotNull(groups);
        } catch (ArrayIndexOutOfBoundsException e) {
            fail("ArrayIndexOutOfBoundsException thrown");
        }
    }

    @Test
    public void testGetBatchJob() {
        BatchDriver driver = new BatchDriver("newzini", "batchfile");
        String group = "testGroup";
        BatchJob job = driver.getBatchJob(group);
        assertNotNull(job);
    }

    @Test
    public void testGetSubjectLines() {
        BatchDriver driver = new BatchDriver("newzini", "batchfile");
        String[] subjectLines = driver.getSubjectLines();
        assertNotNull(subjectLines);
    }
}