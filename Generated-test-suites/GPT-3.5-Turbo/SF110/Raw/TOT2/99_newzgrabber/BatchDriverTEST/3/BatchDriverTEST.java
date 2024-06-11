import static org.junit.Assert.*;
import org.junit.Test;

public class BatchDriverTest {

    @Test
    public void testConstructor() {
        BatchDriver batchDriver = new BatchDriver("example.ini", "example.batch");
        assertNotNull(batchDriver);
    }

    @Test
    public void testGetGroups() {
        BatchDriver batchDriver = new BatchDriver("example.ini", "example.batch");
        String[] groups = batchDriver.getGroups();
        assertNotNull(groups);
    }

    @Test
    public void testGetBatchJob() {
        BatchDriver batchDriver = new BatchDriver("example.ini", "example.batch");
        BatchJob batchJob = batchDriver.getBatchJob("group1");
        assertNull(batchJob);
    }

    @Test
    public void testGetSubjectLines() {
        BatchDriver batchDriver = new BatchDriver("example.ini", "example.batch");
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNotNull(subjectLines);
    }
}