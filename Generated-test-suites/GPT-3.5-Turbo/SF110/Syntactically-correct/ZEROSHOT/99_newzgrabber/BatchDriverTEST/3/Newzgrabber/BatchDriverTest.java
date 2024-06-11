import org.junit.*;
import static org.junit.Assert.*;

public class BatchDriverTest {

    @Test
    public void testGetGroups() {
        BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
        String[] expected = new String[] {"group1", "group2", "group3"};
        assertArrayEquals(expected, batchDriver.getGroups());
    }

    @Test
    public void testGetBatchJob() {
        BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
        BatchJob batchJob = batchDriver.getBatchJob("group1");
        
        assertNotNull(batchJob);
        assertEquals("server", batchJob.getServer());
        assertEquals("directory1", batchJob.getDirectory());
        assertEquals("username", batchJob.getUsername());
        assertEquals("password", batchJob.getPassword());
        assertEquals(2, batchJob.getThreads());
        assertEquals(1000, batchJob.getTimeout());
        assertEquals("group1", batchJob.getGroup());
        assertEquals(0, batchJob.getFirstId());
        assertEquals(100, batchJob.getLastId());
    }

    @Test
    public void testGetSubjectLines() {
        BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
        String[] expected = new String[] {"subject1", "subject2", "subject3"};
        
        assertNull(batchDriver.getSubjectLines());
        
        batchDriver.getBatchJob("group1");
        
        assertArrayEquals(expected, batchDriver.getSubjectLines());
    }

}