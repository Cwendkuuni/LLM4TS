import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

public class BatchDriverTest {
    
    @Test
    public void testConstructor() throws IOException {
        BatchDriver batchDriver = new BatchDriver("groups.ini", "batch.ini");
        assertNotNull(batchDriver);
    }
    
    @Test
    public void testGetGroups() {
        BatchDriver batchDriver = new BatchDriver("groups.ini", "batch.ini");
        String[] groups = batchDriver.getGroups();
        assertNotNull(groups);
    }
    
    @Test
    public void testGetBatchJob() {
        BatchDriver batchDriver = new BatchDriver("groups.ini", "batch.ini");
        BatchJob batchJob = batchDriver.getBatchJob("group1");
        assertNotNull(batchJob);
    }
    
    @Test
    public void testGetSubjectLines() {
        BatchDriver batchDriver = new BatchDriver("groups.ini", "batch.ini");
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNotNull(subjectLines);
    }
}