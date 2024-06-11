import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

public class BatchDriverTest {

    private BatchDriver batchDriver;

    @Before
    public void setUp() {
        String newzini = "test.ini";
        String batchfile = "batch.ini";
        batchDriver = new BatchDriver(newzini, batchfile);
    }

    @Test
    public void testGetGroups() {
        String[] groups = batchDriver.getGroups();
        assertNotNull(groups);
        assertEquals(0, groups.length);
    }

    @Test
    public void testGetBatchJob_ValidGroup() {
        String group = "group1";
        BatchJob batchJob = batchDriver.getBatchJob(group);
        assertNotNull(batchJob);
    }
    
    @Test
    public void testGetBatchJob_InvalidGroup() {
        String group = "invalidGroup";
        BatchJob batchJob = batchDriver.getBatchJob(group);
        assertNull(batchJob);
    }

    @Test
    public void testGetSubjectLines() {
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNull(subjectLines);
    }
}