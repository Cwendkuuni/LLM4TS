
import static org.junit.Assert.*;
import org.junit.*;

public class BatchDriverTest {
    
    @Test
    public void testConstructor() throws NullPointerException, IOException {
        BatchDriver driver = new BatchDriver("newzini", "batchfile");
        
        assertNotNull(driver);
    }
    
    @Test
    public void testGetGroups() throws ArrayIndexOutOfBoundsException {
        BatchDriver driver = new BatchDriver("newzini", "batchfile");
        
        String[] groups = driver.getGroups();
        
        assertNotNull(groups);
    }
    
    @Test
    public void testGetBatchJob() {
        BatchDriver driver = new BatchDriver("newzini", "batchfile");
        
        BatchJob job = driver.getBatchJob("group");
        
        assertNull(job);
    }
    
    @Test
    public void testGetSubjectLines() {
        BatchDriver driver = new BatchDriver("newzini", "batchfile");
        
        String[] subjectlines = driver.getSubjectLines();
        
        assertNull(subjectlines);
    }
    
}
