import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

public class BatchDriverTest {

    private BatchDriver batchDriver;

    @Before
    public void setUp() {
        batchDriver = new BatchDriver("test.ini", "batchfile.ini");
    }

    @After
    public void tearDown() {
        // Clean up any resources used
    }

    @Test
    public void testGetGroups() {
        String[] groups = batchDriver.getGroups();
        assertNotNull(groups);
        assertEquals(2, groups.length);
    }

    @Test
    public void testGetBatchJob_validGroup() {
        BatchJob batchJob = batchDriver.getBatchJob("group1");
        assertNotNull(batchJob);
    }

    @Test
    public void testGetBatchJob_invalidGroup() {
        BatchJob batchJob = batchDriver.getBatchJob("invalidGroup");
        assertNull(batchJob);
    }

    @Test
    public void testGetSubjectLines() {
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNotNull(subjectLines);
        assertEquals(10, subjectLines.length);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_nullArguments() {
        BatchDriver nullBatchDriver = new BatchDriver(null, null);
    }

    @Test(expected = IOException.class)
    public void testConstructor_invalidFile() {
        BatchDriver invalidBatchDriver = new BatchDriver("invalid.ini", "invalid.ini");
    }

}