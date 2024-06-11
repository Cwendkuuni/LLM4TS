import org.junit.Test;
import static org.junit.Assert.*;

public class BatchDriverTest {

    @Test
    public void testConstructor() {
        try {
            BatchDriver batchDriver = new BatchDriver("newzini.txt", "batchfile.txt");
            assertNotNull(batchDriver);
        } catch (Exception e) {
            fail("Exception thrown during constructor test");
        }
    }

    @Test
    public void testGetGroups() {
        BatchDriver batchDriver = new BatchDriver("newzini.txt", "batchfile.txt");
        String[] groups = batchDriver.getGroups();
        assertNotNull(groups);
    }

    @Test
    public void testGetBatchJob() {
        BatchDriver batchDriver = new BatchDriver("newzini.txt", "batchfile.txt");
        String[] groups = batchDriver.getGroups();
        BatchJob batchJob = batchDriver.getBatchJob(groups[0]);
        assertNotNull(batchJob);
    }

    @Test
    public void testGetSubjectLines() {
        BatchDriver batchDriver = new BatchDriver("newzini.txt", "batchfile.txt");
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNotNull(subjectLines);
    }
}