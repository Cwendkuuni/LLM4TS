import static org.junit.Assert.*;
import org.junit.Test;

public class BatchDriverTest {
    
    @Test
    public void testConstructor() {
        try {
            BatchDriver batchDriver = new BatchDriver("newzini.txt", "batchfile.txt");
            assertNotNull(batchDriver);
        } catch (Exception e) {
            fail("Constructor should not throw any exceptions");
        }
    }
    
    @Test
    public void testGetGroups() {
        BatchDriver batchDriver = new BatchDriver("newzini.txt", "batchfile.txt");
        String[] groups = batchDriver.getGroups();
        assertNotNull(groups);
    }
    
    @Test
    public void testGetBatchJob() {
        BatchDriver batchDriver = new BatchDriver("newzini.txt", "batchfile.txt");
        BatchJob batchJob = batchDriver.getBatchJob("group1");
        assertNotNull(batchJob);
    }
    
    @Test
    public void testGetSubjectLines() {
        BatchDriver batchDriver = new BatchDriver("newzini.txt", "batchfile.txt");
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNull(subjectLines);
    }
}