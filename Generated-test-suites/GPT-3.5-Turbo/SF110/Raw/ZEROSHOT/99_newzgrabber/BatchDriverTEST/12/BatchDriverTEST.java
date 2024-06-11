import org.junit.Before;
import org.junit.Test;
import java.util.Hashtable;
import java.util.Vector;
import static org.junit.Assert.*;

public class BatchDriverTest {

    private BatchDriver batchDriver;
    
    @Before
    public void setUp() throws Exception {
        batchDriver = new BatchDriver("/path/to/newzini", "/path/to/batchfile");
    }
    
    @Test
    public void testGetGroups() {
        String[] groups = batchDriver.getGroups();
        assertNotNull(groups);
        assertEquals(Vector.class, batchDriver.getBatchgroupsVector().getClass());
    }
    
    @Test
    public void testGetBatchJob() {
        BatchJob batchJob = batchDriver.getBatchJob("group");
        assertNotNull(batchJob);
        assertEquals(String.class, batchJob.getServer().getClass());
        assertEquals(String.class, batchJob.getGroup().getClass());
        assertEquals(String.class, batchJob.getDirectory().getClass());
        assertEquals(int.class, batchJob.getFirstid().getClass());
        assertEquals(int.class, batchJob.getLastid().getClass());
        assertEquals(String.class, batchJob.getUsername().getClass());
        assertEquals(String.class, batchJob.getPassword().getClass());
    }
    
    @Test
    public void testGetSubjectLines() {
        String[] subjectlines = batchDriver.getSubjectLines();
        assertNotNull(subjectlines);
    }
}