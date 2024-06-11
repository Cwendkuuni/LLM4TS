import static org.junit.Assert.*;
import org.junit.Test;

public class BatchDriverTest {

    @Test
    public void testGetGroups() {
        BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
        String[] expected = {"group1", "group2", "group3"};
        assertArrayEquals(expected, batchDriver.getGroups());
    }

    @Test
    public void testGetBatchJob() {
        BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
        BatchJob batchJob = batchDriver.getBatchJob("group1");
        assertNotNull(batchJob);
        assertEquals("server", batchJob.getServer());
        assertEquals("directory1", batchJob.getDirectory());
        assertEquals(0, batchJob.getFirstid());
        assertEquals(100, batchJob.getLastid());
        assertEquals("username", batchJob.getUsername());
        assertEquals("password", batchJob.getPassword());
    }

    @Test
    public void testGetSubjectLines() {
        BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
        String[] expected = {"subject1", "subject2", "subject3"};
        assertArrayEquals(expected, batchDriver.getSubjectLines());
    }

}