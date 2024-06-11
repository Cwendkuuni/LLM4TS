import static org.junit.Assert.*;
import org.junit.Test;

public class BatchDriverTest {

    @Test
    public void testBatchDriverConstructor() {
        try {
            BatchDriver batchDriver = new BatchDriver("testNewzini.ini", "testBatchfile.ini");
            assertNotNull(batchDriver);
        } catch (Exception e) {
            fail("Failed to initialize BatchDriver");
        }
    }

    @Test
    public void testGetGroups() {
        BatchDriver batchDriver = new BatchDriver("testNewzini.ini", "testBatchfile.ini");
        String[] groups = batchDriver.getGroups();
        assertNotNull(groups);
    }

    @Test
    public void testGetBatchJob() {
        BatchDriver batchDriver = new BatchDriver("testNewzini.ini", "testBatchfile.ini");
        BatchJob batchJob = batchDriver.getBatchJob("testgroup");
        assertNotNull(batchJob);
    }

    @Test
    public void testGetSubjectLines() {
        BatchDriver batchDriver = new BatchDriver("testNewzini.ini", "testBatchfile.ini");
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNotNull(subjectLines);
    }

}