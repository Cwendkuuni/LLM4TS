package Newzgrabber;

import org.junit.*;
import static org.junit.Assert.*;
import java.io.IOException;

public class BatchDriverTest {
    
    @Test(expected = NullPointerException.class)
    public void testConstructorNullParameters() throws IOException {
        BatchDriver driver = new BatchDriver(null, null);
    }
    
    @Test
    public void testConstructorValidParameters() throws IOException {
        BatchDriver driver = new BatchDriver("newz.ini", "batchfile.ini");
        assertNotNull(driver);
    }
    
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetGroupsEmptyBatchGroupsVector() throws ArrayIndexOutOfBoundsException {
        BatchDriver driver = new BatchDriver("newz.ini", "batchfile.ini");
        driver.getGroups();
    }
    
    @Test
    public void testGetGroupsNonEmptyBatchGroupsVector() throws IOException {
        BatchDriver driver = new BatchDriver("newz.ini", "batchfile.ini");
        String[] groups = driver.getGroups();
        assertNotNull(groups);
        assertTrue(groups.length > 0);
    }
    
    @Test
    public void testGetBatchJobValidGroup() throws IOException {
        BatchDriver driver = new BatchDriver("newz.ini", "batchfile.ini");
        BatchJob job = driver.getBatchJob("group1");
        assertNotNull(job);
    }
    
    @Test
    public void testGetBatchJobInvalidGroup() throws IOException {
        BatchDriver driver = new BatchDriver("newz.ini", "batchfile.ini");
        BatchJob job = driver.getBatchJob("invalid_group");
        assertNull(job);
    }
    
    @Test
    public void testGetSubjectLinesValidGroup() throws IOException {
        BatchDriver driver = new BatchDriver("newz.ini", "batchfile.ini");
        BatchJob job = driver.getBatchJob("group1");
        String[] subjectLines = driver.getSubjectLines();
        assertNotNull(subjectLines);
    }
    
    @Test
    public void testGetSubjectLinesInvalidGroup() throws IOException {
        BatchDriver driver = new BatchDriver("newz.ini", "batchfile.ini");
        BatchJob job = driver.getBatchJob("invalid_group");
        String[] subjectLines = driver.getSubjectLines();
        assertNull(subjectLines);
    }
}