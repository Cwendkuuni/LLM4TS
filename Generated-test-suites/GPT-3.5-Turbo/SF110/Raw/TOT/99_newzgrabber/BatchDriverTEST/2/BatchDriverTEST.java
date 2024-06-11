import org.junit.*;
import static org.junit.Assert.*;
import java.io.IOException;

public class BatchDriverTest {
    
    @Test
    public void testConstructor() {
        try {
            BatchDriver bd = new BatchDriver("newzini.txt", "batchfile.txt");
            assertNotNull(bd.getGroups());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructorNullNewzini() throws IOException {
        BatchDriver bd = new BatchDriver(null, "batchfile.txt");
    }
    
    @Test(expected = IOException.class)
    public void testConstructorNonexistentNewzini() throws IOException {
        BatchDriver bd = new BatchDriver("nonexistent.ini", "batchfile.txt");
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructorNullBatchfile() throws IOException {
        BatchDriver bd = new BatchDriver("newzini.txt", null);
    }
    
    @Test(expected = IOException.class)
    public void testConstructorNonexistentBatchfile() throws IOException {
        BatchDriver bd = new BatchDriver("newzini.txt", "nonexistent.ini");
    }
    
    @Test
    public void testGetGroups() {
        try {
            BatchDriver bd = new BatchDriver("newzini.txt", "batchfile.txt");
            String[] groups = bd.getGroups();
            assertEquals(3, groups.length);
            assertEquals("group1", groups[0]);
            assertEquals("group2", groups[1]);
            assertEquals("group3", groups[2]);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetGroupsEmptyBatchgroupsVector() {
        try {
            BatchDriver bd = new BatchDriver("newzini.txt", "empty_batchfile.txt");
            bd.getGroups();
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testGetBatchJob() {
        try {
            BatchDriver bd = new BatchDriver("newzini.txt", "batchfile.txt");
            BatchJob bj = bd.getBatchJob("group1");
            assertNotNull(bj);
            assertEquals("server1", bj.getServer());
            assertEquals("directory1", bj.getDirectory());
            assertEquals(1, bj.getFirstId());
            assertEquals(10, bj.getLastId());
            assertNull(bj.getUsername());
            assertNull(bj.getPassword());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testGetBatchJobMissingServerAndDirectory() {
        try {
            BatchDriver bd = new BatchDriver("newzini.txt", "batchfile.txt");
            BatchJob bj = bd.getBatchJob("errorGroup");
            assertNull(bj);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testGetBatchJobWithUsernameAndPassword() {
        try {
            BatchDriver bd = new BatchDriver("newzini.txt", "batchfile.txt");
            BatchJob bj = bd.getBatchJob("group2");
            assertNotNull(bj);
            assertEquals("server2", bj.getServer());
            assertEquals("directory2", bj.getDirectory());
            assertEquals("username2", bj.getUsername());
            assertEquals("password2", bj.getPassword());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testGetBatchJobWithInvalidThreadsAndTimeout() {
        try {
            BatchDriver bd = new BatchDriver("newzini.txt", "batchfile.txt");
            BatchJob bj = bd.getBatchJob("group3");
            assertNotNull(bj);
            assertEquals("server3", bj.getServer());
            assertEquals("directory3", bj.getDirectory());
            assertEquals(1, bj.getnThreads());
            assertEquals(-1, bj.getTimeout());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testGetSubjectLines() {
        try {
            BatchDriver bd = new BatchDriver("newzini.txt", "batchfile.txt");
            bd.getBatchJob("group1");
            assertNotNull(bd.getSubjectLines());
            assertEquals(3, bd.getSubjectLines().length);
            assertEquals("subject1", bd.getSubjectLines()[0]);
            assertEquals("subject2", bd.getSubjectLines()[1]);
            assertEquals("subject3", bd.getSubjectLines()[2]);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
}