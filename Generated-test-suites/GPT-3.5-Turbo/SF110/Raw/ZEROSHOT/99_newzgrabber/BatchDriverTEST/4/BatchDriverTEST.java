import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BatchDriverTest {
    private BatchDriver batchDriver;
    
    @Before
    public void setUp() throws Exception {
        batchDriver = new BatchDriver("newzini", "batchfile");
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructorNullPointerException() throws NullPointerException, IOException {
        BatchDriver nullBatchDriver = new BatchDriver(null, null);
    }
    
    @Test(expected = IOException.class)
    public void testConstructorIOException() throws NullPointerException, IOException {
        BatchDriver invalidBatchDriver = new BatchDriver("invalidNewzini", "invalidBatchfile");
    }
    
    @Test
    public void testGetGroups() throws ArrayIndexOutOfBoundsException {
        String[] groups = batchDriver.getGroups();
        assertNotNull(groups);
    }
    
    @Test
    public void testGetBatchJob() {
        BatchJob batchJob = batchDriver.getBatchJob("group");
        assertNotNull(batchJob);
    }
    
    @Test
    public void testGetSubjectLines() {
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNotNull(subjectLines);
    }
}