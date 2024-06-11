import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

@RunWith(JUnit4.class)
public class BatchDriverTest {

    private static final String newzini = "C:/path/to/newz.ini";
    private static final String batchfile = "C:/path/to/batchfile.ini";
    private BatchDriver batchDriver;

    @Before
    public void setUp() throws IOException {
        batchDriver = new BatchDriver(newzini, batchfile);
    }

    @Test
    public void testGetGroups() {
        String[] expectedGroups = {"group1", "group2", "group3"};
        String[] groups = batchDriver.getGroups();
        assertArrayEquals(expectedGroups, groups);
    }

    @Test
    public void testGetBatchJob_whenValidGroup() {
        String group = "group1";
        BatchJob batchJob = batchDriver.getBatchJob(group);
        assertNotNull(batchJob);
        assertEquals(group, batchJob.getGroup());
    }

    @Test
    public void testGetBatchJob_whenInvalidGroup() {
        String group = "invalidGroup";
        BatchJob batchJob = batchDriver.getBatchJob(group);
        assertNull(batchJob);
    }

    @Test
    public void testGetSubjectLines_whenBatchJobNotNull() {
        String group = "group1";
        BatchJob batchJob = batchDriver.getBatchJob(group);
        assertNotNull(batchJob);

        String[] subjectLines = batchDriver.getSubjectLines();
        assertNotNull(subjectLines);
        assertEquals(batchJob.getSubjectLines().length, subjectLines.length);
    }

    @Test
    public void testGetSubjectLines_whenBatchJobNull() {
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNull(subjectLines);
    }

}