import org.junit.*;
import static org.junit.Assert.*;

public class BatchDriverTest {

    private BatchDriver batchDriver;

    @Before
    public void setUp() {
        batchDriver = new BatchDriver("newzini.txt", "batchfile.txt");
    }

    @Test
    public void testGetGroups_typicalCase() {
        String[] groups = batchDriver.getGroups();
        assertEquals(2, groups.length);
        assertEquals("Group1", groups[0]);
        assertEquals("Group2", groups[1]);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetGroups_emptyBatchgroupsVector() {
        batchDriver.BatchgroupsVector.clear();
        batchDriver.getGroups();
    }

    @Test
    public void testGetBatchJob_typicalCase() {
        BatchJob batchJob = batchDriver.getBatchJob("Group1");
        assertNotNull(batchJob);
        assertEquals("server1", batchJob.getServer());
        assertEquals("directory1", batchJob.getDirectory());
        assertEquals("username1", batchJob.getUsername());
        assertEquals("password1", batchJob.getPassword());
        assertEquals(3, batchJob.getNumberOfThreads());
        assertFalse(batchJob.isTimeoutEnabled());
        assertEquals(100, batchJob.getTimeoutMax());
        assertEquals(0, batchJob.getFirstId());
        assertEquals(1000, batchJob.getLastId());
    }

    @Test
    public void testGetBatchJob_groupNotFound() {
        BatchJob batchJob = batchDriver.getBatchJob("NonexistentGroup");
        assertNull(batchJob);
    }

    @Test
    public void testGetBatchJob_usernameAndPasswordNotPresent() {
        BatchJob batchJob = batchDriver.getBatchJob("Group2");
        assertNotNull(batchJob);
        assertNull(batchJob.getUsername());
        assertNull(batchJob.getPassword());
    }

    @Test
    public void testGetBatchJob_invalidThreadsValue() {
        batchDriver.OptionsHash.put("threads", "invalid");
        BatchJob batchJob = batchDriver.getBatchJob("Group1");
        assertNotNull(batchJob);
        assertEquals(1, batchJob.getNumberOfThreads());
    }

    @Test
    public void testGetBatchJob_invalidTimeoutValue() {
        batchDriver.OptionsHash.put("timeout", "invalid");
        BatchJob batchJob = batchDriver.getBatchJob("Group1");
        assertNotNull(batchJob);
        assertEquals(-1, batchJob.getTimeoutMax());
    }

    @Test
    public void testGetBatchJob_newsSocketIsNull() {
        Newzgrabber.nf = null;
        BatchJob batchJob = batchDriver.getBatchJob("Group1");
        assertNull(batchJob);
    }

    @Test
    public void testGetBatchJob_statusNotOK() {
        NewsFactory newsFactory = mock(NewsFactory.class);
        when(mockNewsFactory.getNewsSocket(anyString())).thenReturn(mock(NNTP.class));
        when(nf.getNewsSocket(anyString()).genericCommand(anyString())).thenReturn(new String[]{"status not OK"});

        BatchJob batchJob = batchDriver.getBatchJob("Group1");
        assertNull(batchJob);
    }

    @Test
    public void testGetBatchJob_invalidNewsgroupsHashValue() {
        batchDriver.NewsgroupsHash.put("Group1", "invalid");
        BatchJob batchJob = batchDriver.getBatchJob("Group1");
        assertNotNull(batchJob);
        assertEquals(1, batchJob.getFirstId());
    }

    @Test
    public void testGetBatchJob_tmpFirstGreaterThanFirstId() {
        BatchJob batchJob = batchDriver.getBatchJob("Group1");
        assertNotNull(batchJob);
        assertEquals(90, batchJob.getFirstId());
    }

    @Test
    public void testGetSubjectLines_typicalCase() {
        String[] subjects = batchDriver.getSubjectLines();
        assertNotNull(subjects);
        assertEquals(2, subjects.length);
        assertEquals("Subject1", subjects[0]);
        assertEquals("Subject2", subjects[1]);
    }

    @After
    public void tearDown() {
        batchDriver = null;
    }
}