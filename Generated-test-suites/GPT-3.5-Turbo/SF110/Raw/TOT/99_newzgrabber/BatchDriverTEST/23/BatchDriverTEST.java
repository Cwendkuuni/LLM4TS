import org.junit.*;
import java.io.IOException;
import static org.junit.Assert.*;

public class BatchDriverTest {
    
    private BatchDriver batchDriver;
    
    @Before
    public void setUp() throws NullPointerException, IOException {
        batchDriver = new BatchDriver("newzini.ini", "batchfile.ini");
    }
    
    @After
    public void tearDown() {
        batchDriver = null;
    }
    
    @Test
    public void testGetGroups() {
        String[] expectedGroups = {"group1", "group2"};
        
        String[] actualGroups = batchDriver.getGroups();
        
        assertArrayEquals(expectedGroups, actualGroups);
    }
    
    @Test
    public void testGetBatchJob_ValidGroup_ReturnsBatchJob() {
        String group = "group1";
        
        BatchJob batchJob = batchDriver.getBatchJob(group);
        
        assertNotNull(batchJob);
        assertEquals("server", batchJob.getServer());
        assertEquals("directory1", batchJob.getDirectory());
        assertEquals(1, batchJob.getThreads());
        assertFalse(batchJob.isTimeoutEnabled());
        assertNull(batchJob.getUsername());
        assertNull(batchJob.getPassword());
    }
    
    @Test
    public void testGetBatchJob_InvalidGroup_ReturnsNull() {
        String group = "invalidGroup";
        
        BatchJob batchJob = batchDriver.getBatchJob(group);
        
        assertNull(batchJob);
    }
    
    @Test
    public void testGetSubjectLines_ReturnsSubjectLines() {
        String[] expectedSubjectLines = {"subject1", "subject2"};
        
        String[] actualSubjectLines = batchDriver.getSubjectLines();
        
        assertArrayEquals(expectedSubjectLines, actualSubjectLines);
    }
}