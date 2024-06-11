import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BatchDriverTest {

    private BatchDriver batchDriver;

    @Before
    public void setUp() throws Exception {
        batchDriver = new BatchDriver("newz.ini", "batch.ini");
    }

    @Test
    public void testGetGroups() {
        String[] expectedGroups = {"group1", "group2", "group3"};

        String[] actualGroups = batchDriver.getGroups();

        assertArrayEquals(expectedGroups, actualGroups);
    }

    @Test
    public void testGetBatchJob_ValidGroup_ReturnsBatchJobObject() {
        String group = "group1";

        BatchJob batchJob = batchDriver.getBatchJob(group);

        assertNotNull(batchJob);
    }

    @Test
    public void testGetBatchJob_InvalidGroup_ReturnsNull() {
        String group = "invalidGroup";

        BatchJob batchJob = batchDriver.getBatchJob(group);

        assertNull(batchJob);
    }

    @Test
    public void testGetSubjectLines() {
        String[] expectedSubjectLines = {"subject1", "subject2", "subject3"};

        String[] actualSubjectLines = batchDriver.getSubjectLines();

        assertArrayEquals(expectedSubjectLines, actualSubjectLines);
    }
}