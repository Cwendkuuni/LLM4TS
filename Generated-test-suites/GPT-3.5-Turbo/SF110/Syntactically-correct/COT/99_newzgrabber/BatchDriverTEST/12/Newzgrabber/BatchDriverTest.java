
import org.junit.*;
import static org.junit.Assert.*;

public class BatchDriverTest {
    
    @Test
    public void testConstructor() throws NullPointerException, IOException {
        BatchDriver batchDriver = new BatchDriver("newz.ini", "batchfile.ini");
        assertNotNull(batchDriver);
    }
    
    @Test
    public void testGetGroups() throws ArrayIndexOutOfBoundsException {
        BatchDriver batchDriver = new BatchDriver("newz.ini", "batchfile.ini");
        String[] groups = batchDriver.getGroups();
        assertNotNull(groups);
    }
    
    @Test
    public void testGetBatchJob() {
        BatchDriver batchDriver = new BatchDriver("newz.ini", "batchfile.ini");
        BatchJob batchJob = batchDriver.getBatchJob("groupname");
        assertNotNull(batchJob);
    }
    
    @Test
    public void testGetSubjectLines() {
        BatchDriver batchDriver = new BatchDriver("newz.ini", "batchfile.ini");
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNotNull(subjectLines);
    }
}
