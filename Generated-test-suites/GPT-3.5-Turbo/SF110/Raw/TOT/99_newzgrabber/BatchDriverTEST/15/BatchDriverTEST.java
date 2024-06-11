import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BatchDriverTest {

    private BatchDriver batchDriver;

    @Before
    public void setUp() throws Exception {
        batchDriver = new BatchDriver("newzini.txt", "batchfile.txt");
    }

    @Test
    public void testGetGroups() {
        String[] expected = {"group1", "group2"};
        String[] groups = batchDriver.getGroups();
        assertArrayEquals(expected, groups);
    }

    @Test
    public void testGetBatchJob() {
        BatchJob batchJob = batchDriver.getBatchJob("group1");
        assertNotNull(batchJob);
    }

    @Test
    public void testGetBatchJob_InvalidGroup() {
        BatchJob batchJob = batchDriver.getBatchJob("invalidGroup");
        assertNull(batchJob);
    }

    @Test
    public void testGetSubjectLines() {
        String[] expected = {"subject1", "subject2"};
        String[] subjectLines = batchDriver.getSubjectLines();
        assertArrayEquals(expected, subjectLines);
    }

    @Test
    public void testGetSubjectLines_NoSubjects() {
        batchDriver.getBatchJob("emptyGroup");
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNull(subjectLines);
    }
}