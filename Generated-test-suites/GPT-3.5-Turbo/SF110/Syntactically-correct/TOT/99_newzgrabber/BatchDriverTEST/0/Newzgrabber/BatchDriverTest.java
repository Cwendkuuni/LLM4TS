import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BatchDriverTest {

    private BatchDriver batchDriver;

    @Before
    public void setUp() throws Exception {
        batchDriver = new BatchDriver("newzini.ini", "batchfile.ini");
    }

    @Test
    public void testGetGroups() {
        String[] expected = {"group1", "group2", "group3"}; // typical use case
        assertArrayEquals(expected, batchDriver.getGroups());
    }

    @Test
    public void testGetBatchJob_validGroup() {
        BatchJob batchJob = batchDriver.getBatchJob("group1"); // typical use case
        assertNotNull(batchJob);
        assertEquals("serverAddress", batchJob.getServer());
        assertEquals("directoryPath", batchJob.getDirectory());
        assertEquals(1, batchJob.getFirstID());
        assertEquals(10, batchJob.getLastID());
    }

    @Test
    public void testGetBatchJob_invalidGroup() {
        BatchJob batchJob = batchDriver.getBatchJob("invalidGroup"); // error scenario
        assertNull(batchJob);
    }

    @Test
    public void testGetBatchJob_nullServer() {
        // edge case: server is null in OptionsHash
        BatchJob batchJob = batchDriver.getBatchJob("group2");
        assertNull(batchJob);
    }

    @Test
    public void testGetBatchJob_nullDirectory() {
        // edge case: directory is null in BatchgroupsHash
        BatchJob batchJob = batchDriver.getBatchJob("group3");
        assertNull(batchJob);
    }

    @Test
    public void testGetSubjectLines() {
        String[] expected = {"subject1", "subject2", "subject3"}; // typical use case
        batchDriver.getBatchJob("group1");
        assertArrayEquals(expected, batchDriver.getSubjectLines());
    }

    @Test
    public void testGetSubjectLines_noBatchJob() {
        // error scenario: getSubjectLines() called without calling getBatchJob()
        assertNull(batchDriver.getSubjectLines());
    }
}