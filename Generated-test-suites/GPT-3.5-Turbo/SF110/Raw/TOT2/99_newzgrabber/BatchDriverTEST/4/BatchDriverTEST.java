import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

public class BatchDriverTest {

    @Test
    public void testBatchDriverConstructor() {
        try {
            BatchDriver driver = new BatchDriver("newzini.ini", "batchfile.ini");
            assertNotNull(driver);
        } catch (NullPointerException | IOException e) {
            fail("Constructor should not throw NullPointerException or IOException");
        }
    }

    @Test
    public void testGetGroups() {
        BatchDriver driver = new BatchDriver("newzini.ini", "batchfile.ini");
        String[] groups = driver.getGroups();
        assertNotNull(groups);
    }

    @Test
    public void testGetBatchJob() {
        BatchDriver driver = new BatchDriver("newzini.ini", "batchfile.ini");
        BatchJob job = driver.getBatchJob("testGroup");
        assertNotNull(job);
    }

    @Test
    public void testGetSubjectLines() {
        BatchDriver driver = new BatchDriver("newzini.ini", "batchfile.ini");
        String[] subjectLines = driver.getSubjectLines();
        assertNotNull(subjectLines);
    }

}