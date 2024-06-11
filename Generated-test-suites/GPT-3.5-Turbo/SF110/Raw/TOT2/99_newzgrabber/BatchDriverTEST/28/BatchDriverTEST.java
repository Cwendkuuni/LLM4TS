import static org.junit.Assert.*;
import org.junit.Test;

public class BatchDriverTest {

    @Test
    public void testBatchDriverConstructor() {
        String newzini = "test.ini";
        String batchfile = "test.batch";
        try {
            BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
            assertNotNull(batchDriver);
        } catch (Exception e) {
            fail("Exception thrown during BatchDriver constructor test: " + e.getMessage());
        }
    }

    @Test
    public void testGetGroups() {
        String[] expectedGroups = {"Group1", "Group2", "Group3"};
        try {
            BatchDriver batchDriver = new BatchDriver("test.ini", "test.batch");
            String[] actualGroups = batchDriver.getGroups();
            assertArrayEquals(expectedGroups, actualGroups);
        } catch (Exception e) {
            fail("Exception thrown during getGroups test: " + e.getMessage());
        }
    }

    @Test
    public void testGetBatchJob() {
        String group = "Group1";
        try {
            BatchDriver batchDriver = new BatchDriver("test.ini", "test.batch");
            BatchJob batchJob = batchDriver.getBatchJob(group);
            assertNotNull(batchJob);
        } catch (Exception e) {
            fail("Exception thrown during getBatchJob test: " + e.getMessage());
        }
    }

}