import org.junit.*;
import static org.junit.Assert.*;

public class BatchDriverTest {

    private BatchDriver batchDriver;

    @Before
    public void setUp() throws Exception {
        batchDriver = new BatchDriver("newzini.txt", "batchfile.txt");
    }

    @Test
    public void testGetGroups() {
        String[] groups = batchDriver.getGroups();
        assertNotNull(groups);
        assertEquals(3, groups.length);
    }

    @Test
    public void testGetBatchJob() {
        BatchJob batchJob = batchDriver.getBatchJob("group1");
        assertNotNull(batchJob);
        assertEquals("server1", batchJob.getServer());
        assertEquals("directory1", batchJob.getDirectory());
    }

    @Test
    public void testGetSubjectLines() {
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNull(subjectLines);
    }

}