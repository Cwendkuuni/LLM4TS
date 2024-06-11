import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class BatchDriverTest {
    @Test(expected = NullPointerException.class)
    public void testConstructor() throws NullPointerException, IOException {
        BatchDriver batchDriver = new BatchDriver(null, null);
    }
    
    @Test(expected = IOException.class)
    public void testConstructorWithInvalidFile() throws NullPointerException, IOException {
        BatchDriver batchDriver = new BatchDriver("invalidFile.ini", "invalidFile.ini");
    }
    
    @Test
    public void testGetGroups() {
        BatchDriver batchDriver = new BatchDriver("newzini.ini", "batchfile.ini");
        String[] expected = {"group1", "group2", "group3"};
        assertEquals(expected, batchDriver.getGroups());
    }
    
    @Test
    public void testGetBatchJob() {
        BatchDriver batchDriver = new BatchDriver("newzini.ini", "batchfile.ini");
        BatchJob batchJob = batchDriver.getBatchJob("group1");
        assertNotNull(batchJob);
        assertEquals("server1", batchJob.getServer());
        assertEquals("directory1", batchJob.getDirectory());
        assertEquals(1, batchJob.getFirstId());
        assertEquals(10, batchJob.getLastId());
    }
    
    @Test
    public void testGetBatchJobWithNoDirectory() {
        BatchDriver batchDriver = new BatchDriver("newzini.ini", "batchfile.ini");
        BatchJob batchJob = batchDriver.getBatchJob("group2");
        assertNull(batchJob);
    }
    
    @Test
    public void testGetBatchJobWithNoService() {
        BatchDriver batchDriver = new BatchDriver("newzini.ini", "batchfile.ini");
        BatchJob batchJob = batchDriver.getBatchJob("group3");
        assertNull(batchJob);
    }
    
    @Test
    public void testGetSubjectLines() {
        BatchDriver batchDriver = new BatchDriver("newzini.ini", "batchfile.ini");
        BatchJob batchJob = batchDriver.getBatchJob("group1");
        String[] expected = {"subject1", "subject2", "subject3"};
        batchJob.setSubjectLines(expected);
        assertEquals(expected, batchDriver.getSubjectLines());
    }
}