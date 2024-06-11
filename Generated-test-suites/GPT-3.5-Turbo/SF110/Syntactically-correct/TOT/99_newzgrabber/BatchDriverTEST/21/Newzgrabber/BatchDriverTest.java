import org.junit.*;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

public class BatchDriverTest {
    
    BatchDriver driver;
    
    @Before
    public void setUp() throws NullPointerException, IOException {
        driver = new BatchDriver("newz.ini", "batchfile.ini");
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(driver.groupsini);
        assertNotNull(driver.batchini);
        assertNotNull(driver.NewsgroupsHash);
        assertNotNull(driver.BatchgroupsHash);
        assertNotNull(driver.BatchretrieveHash);
        assertNotNull(driver.OptionsHash);
        assertNotNull(driver.BatchgroupsVector);
        assertNull(driver.subjectlines);
    }
    
    @Test
    public void testGetGroups() {
        String[] groups = driver.getGroups();
        assertNotNull(groups);
        assertEquals(0, groups.length);
        
        Vector<String[]> batchgroupsVector = new Vector<String[]>();
        batchgroupsVector.add(new String[]{"group1"});
        batchgroupsVector.add(new String[]{"group2"});
        driver.BatchgroupsVector = batchgroupsVector;
        
        groups = driver.getGroups();
        assertNotNull(groups);
        assertEquals(2, groups.length);
        assertEquals("group1", groups[0]);
        assertEquals("group2", groups[1]);
    }
    
    @Test
    public void testGetBatchJob() {
        BatchJob batchJob = driver.getBatchJob("group1");
        assertNull(batchJob);
        
        driver.OptionsHash.put("server", "localhost");
        driver.BatchgroupsHash.put("group1", "directory1");
        BatchJob batchJob1 = driver.getBatchJob("group1");
        assertNotNull(batchJob1);
        assertEquals("localhost", batchJob1.getServer());
        assertEquals("directory1", batchJob1.getDirectory());
        
        driver.OptionsHash.put("username", "user1");
        driver.OptionsHash.put("password", "pass1");
        BatchJob batchJob2 = driver.getBatchJob("group2");
        assertNotNull(batchJob2);
        assertEquals("localhost", batchJob2.getServer());
        assertEquals("directory2", batchJob2.getDirectory());
        assertEquals("user1", batchJob2.getUsername());
        assertEquals("pass1", batchJob2.getPassword());
        
        driver.OptionsHash.put("threads", "2");
        BatchJob batchJob3 = driver.getBatchJob("group3");
        assertNotNull(batchJob3);
        assertEquals("localhost", batchJob3.getServer());
        assertEquals("directory3", batchJob3.getDirectory());
        assertEquals("user1", batchJob3.getUsername());
        assertEquals("pass1", batchJob3.getPassword());
        assertEquals(2, batchJob3.getNumberOfThreads());
        
        driver.OptionsHash.put("timeout", "60");
        BatchJob batchJob4 = driver.getBatchJob("group4");
        assertNotNull(batchJob4);
        assertEquals("localhost", batchJob4.getServer());
        assertEquals("directory4", batchJob4.getDirectory());
        assertEquals("user1", batchJob4.getUsername());
        assertEquals("pass1", batchJob4.getPassword());
        assertEquals(2, batchJob4.getNumberOfThreads());
        assertTrue(batchJob4.isTimeoutEnabled());
        assertEquals(60, batchJob4.getTimeoutMax());
        
        driver.OptionsHash.put("timeout", "invalid");
        BatchJob batchJob5 = driver.getBatchJob("group5");
        assertNotNull(batchJob5);
        assertEquals("localhost", batchJob5.getServer());
        assertEquals("directory5", batchJob5.getDirectory());
        assertEquals("user1", batchJob5.getUsername());
        assertEquals("pass1", batchJob5.getPassword());
        assertEquals(2, batchJob5.getNumberOfThreads());
        assertFalse(batchJob5.isTimeoutEnabled());
        assertEquals(-1, batchJob5.getTimeoutMax());
    }
    
    @Test
    public void testGetSubjectLines() {
        assertNull(driver.getSubjectLines());
        
        driver.subjectlines = new String[]{"subject1", "subject2"};
        String[] subjectLines = driver.getSubjectLines();
        assertNotNull(subjectLines);
        assertEquals(2, subjectLines.length);
        assertEquals("subject1", subjectLines[0]);
        assertEquals("subject2", subjectLines[1]);
    }
}