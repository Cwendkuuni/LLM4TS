import static org.junit.Assert.*;
import org.junit.*;

public class BatchDriverTest {

    private BatchDriver batchDriver;

    @Before
    public void setUp() throws NullPointerException, IOException {
        batchDriver = new BatchDriver("newzini_file.ini", "batchfile.txt");
    }

    @Test
    public void testGetGroups() {
        String[] expected = { "group1", "group2", "group3" };
        assertArrayEquals(expected, batchDriver.getGroups());
    }

    @Test
    public void testGetGroupsEmpty() {
        batchDriver = new BatchDriver("newzini_file.ini", "empty_batchfile.txt");
        String[] expected = {};
        assertArrayEquals(expected, batchDriver.getGroups());
    }

    @Test
    public void testGetGroupsOutOfBounds() {
        batchDriver = new BatchDriver("newzini_file.ini", "batchfile.txt");
        try {
            String[] groups = batchDriver.getGroups();
            groups[3] = "group4";
            fail("ArrayIndexOutOfBoundsException not thrown");
        } catch (ArrayIndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetBatchJob() {
        String groupName = "group1";
        BatchJob expectedBatchJob = new BatchJob("server", groupName, "directory", 0, 10);
        BatchJob actualBatchJob = batchDriver.getBatchJob(groupName);
        assertNotNull(actualBatchJob);
        assertEquals(expectedBatchJob.getServer(), actualBatchJob.getServer());
        assertEquals(expectedBatchJob.getGroup(), actualBatchJob.getGroup());
        assertEquals(expectedBatchJob.getDirectory(), actualBatchJob.getDirectory());
        assertEquals(expectedBatchJob.getFirstId(), actualBatchJob.getFirstId());
        assertEquals(expectedBatchJob.getLastId(), actualBatchJob.getLastId());
    }

    @Test
    public void testGetBatchJobNonExistingGroup() {
        String groupName = "nonExistingGroup";
        assertNull(batchDriver.getBatchJob(groupName));
    }

    @Test
    public void testGetBatchJobMissingServerAndDirectory() {
        String groupName = "group2";
        assertNull(batchDriver.getBatchJob(groupName));
    }

    @Test
    public void testGetBatchJobWithUsernameAndPassword() {
        String groupName = "group3";
        BatchJob expectedBatchJob = new BatchJob("server", groupName, "directory", 0, 10);
        expectedBatchJob.setUsername("username");
        expectedBatchJob.setPassword("password");
        BatchJob actualBatchJob = batchDriver.getBatchJob(groupName);
        assertNotNull(actualBatchJob);
        assertEquals(expectedBatchJob.getServer(), actualBatchJob.getServer());
        assertEquals(expectedBatchJob.getGroup(), actualBatchJob.getGroup());
        assertEquals(expectedBatchJob.getDirectory(), actualBatchJob.getDirectory());
        assertEquals(expectedBatchJob.getFirstId(), actualBatchJob.getFirstId());
        assertEquals(expectedBatchJob.getLastId(), actualBatchJob.getLastId());
        assertEquals(expectedBatchJob.getUsername(), actualBatchJob.getUsername());
        assertEquals(expectedBatchJob.getPassword(), actualBatchJob.getPassword());
    }

    @Test
    public void testGetBatchJobWithInvalidThreads() {
        String groupName = "group3";
        BatchJob expectedBatchJob = new BatchJob("server", groupName, "directory", 0, 10);
        expectedBatchJob.setThreadLimit(1);
        BatchJob actualBatchJob = batchDriver.getBatchJob(groupName);
        assertNotNull(actualBatchJob);
        assertEquals(expectedBatchJob.getThreadLimit(), actualBatchJob.getThreadLimit());
    }

    @Test
    public void testGetBatchJobWithInvalidTimeout() {
        String groupName = "group3";
        BatchJob expectedBatchJob = new BatchJob("server", groupName, "directory", 0, 10);
        BatchJob actualBatchJob = batchDriver.getBatchJob(groupName);
        assertNotNull(actualBatchJob);
        assertEquals(expectedBatchJob.isTimeout(), actualBatchJob.isTimeout());
        assertEquals(expectedBatchJob.getTimeoutMax(), actualBatchJob.getTimeoutMax());
    }

    @Test
    public void testGetBatchJobWithInvalidIniFile() {
        batchDriver = new BatchDriver("invalid_newzini_file.ini", "batchfile.txt");
        String groupName = "group1";
        assertNull(batchDriver.getBatchJob(groupName));
    }

    @Test
    public void testGetBatchJobException() {
        batchDriver = new BatchDriver("newzini_file.ini", "batchfile.txt");
        String groupName = null;
        assertNull(batchDriver.getBatchJob(groupName));
    }

    @Test
    public void testGetSubjectLines() {
        assertNotNull(batchDriver.getSubjectLines());
    }

    @Test
    public void testGetSubjectLinesEmpty() {
        batchDriver = new BatchDriver("newzini_file.ini", "empty_batchfile.txt");
        assertNull(batchDriver.getSubjectLines());
    }

}