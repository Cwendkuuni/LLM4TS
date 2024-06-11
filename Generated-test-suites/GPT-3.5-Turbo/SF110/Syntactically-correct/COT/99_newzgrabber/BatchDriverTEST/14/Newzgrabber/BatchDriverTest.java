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
        assertArrayEquals(expected, batchDriver.getGroups());
    }
    
    @Test
    public void testGetBatchJob() {
        BatchJob batchJob = batchDriver.getBatchJob("group1");
        assertNotNull(batchJob);
        assertEquals("server1", batchJob.getServer());
        assertEquals("/directory1", batchJob.getDirectory());
    }
    
    @Test
    public void testGetSubjectLines() {
        String[] expected = {"subject1", "subject2", "subject3"};
        assertArrayEquals(expected, batchDriver.getSubjectLines());
    }
}