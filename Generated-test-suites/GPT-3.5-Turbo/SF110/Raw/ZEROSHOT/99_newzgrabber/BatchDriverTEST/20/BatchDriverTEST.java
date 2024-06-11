import org.junit.*;
import static org.junit.Assert.*;

public class BatchDriverTest {

    @Test(expected = NullPointerException.class)
    public void testConstructorNullPointer() throws IOException {
        BatchDriver batchDriver = new BatchDriver(null, null);
    }
    
    @Test(expected = IOException.class)
    public void testConstructorIOException() throws IOException {
        BatchDriver batchDriver = new BatchDriver("newzini.txt", "batchfile.txt");
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
        assertNotNull(subjectLines);
    }
}