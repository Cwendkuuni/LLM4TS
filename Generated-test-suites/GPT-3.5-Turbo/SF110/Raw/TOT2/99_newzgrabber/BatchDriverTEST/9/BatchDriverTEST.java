import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import org.ini4j.Ini;

public class BatchDriverTest {
    
    private BatchDriver batchDriver;
    
    @Test
    public void testConstructor() {
        try {
            batchDriver = new BatchDriver("test.ini", "test.batch");
            assertNotNull(batchDriver);
        } catch (Exception e) {
            fail("Exception thrown in constructor");
        }
    }
    
    @Test
    public void testGetGroups() {
        String[] groups = batchDriver.getGroups();
        assertNotNull(groups);
        assertEquals(0, groups.length);
    }
    
    @Test
    public void testGetBatchJob() {
        String group = "Test";
        BatchJob batchJob = batchDriver.getBatchJob(group);
        assertNotNull(batchJob);
    }
    
    @Test
    public void testGetSubjectLines() {
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNull(subjectLines);
    }
}