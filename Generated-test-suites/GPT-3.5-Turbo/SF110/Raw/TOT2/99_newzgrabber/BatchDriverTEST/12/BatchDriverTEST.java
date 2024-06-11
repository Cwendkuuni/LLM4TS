import static org.junit.Assert.*;
import org.junit.Test;

public class BatchDriverTest {

    @Test
    public void testConstructor() {
        try {
            BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
            assertNotNull(batchDriver);
        } catch (Exception e) {
            fail("Failed to create BatchDriver object");
        }
    }

    @Test
    public void testGetGroups() {
        BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
        String[] groups = batchDriver.getGroups();
        assertNotNull(groups);
    }

    @Test
    public void testGetBatchJob() {
        BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
        BatchJob batchJob = batchDriver.getBatchJob("group");
        assertNotNull(batchJob);
    }

    @Test
    public void testGetSubjectLines() {
        BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNotNull(subjectLines);
    }
}