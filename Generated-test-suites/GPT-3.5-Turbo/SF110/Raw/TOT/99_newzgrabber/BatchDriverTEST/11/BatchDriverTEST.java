import static org.junit.Assert.*;
import org.junit.*;
import java.io.*;
import java.util.*;

public class BatchDriverTest {

    private static final String newzini = "Newzgrabber.ini";
    private static final String batchfile = "Batchfile.ini";
    private BatchDriver batchDriver;
    
    @BeforeClass
    public static void setUpClass() throws IOException {
        // Creating necessary test files
        BufferedWriter newziniWriter = new BufferedWriter(new FileWriter(newzini));
        newziniWriter.write("[Messageids]\n");
        newziniWriter.close();
        
        BufferedWriter batchfileWriter = new BufferedWriter(new FileWriter(batchfile));
        batchfileWriter.write("Newsgroups\nNewsgroup1\nRetrieve\nOption1\n");
        batchfileWriter.close();
    }
    
    @Before
    public void setUp() throws IOException {
        batchDriver = new BatchDriver(newzini, batchfile);
    }
    
    @Test
    public void testGetGroups_EmptyBatchGroupsVector_ReturnsEmptyArray() {
        // Arrange
        batchDriver.BatchgroupsVector.clear();
        
        // Act
        String[] groups = batchDriver.getGroups();
        
        // Assert
        assertEquals(0, groups.length);
    }
    
    @Test
    public void testGetGroups_NonEmptyBatchGroupsVector_ReturnsCorrectArray() {
        // Arrange
        int size = batchDriver.BatchgroupsVector.size();
        
        // Act
        String[] groups = batchDriver.getGroups();
        
        // Assert
        assertEquals(size, groups.length);
        for (int i = 0; i < size; i++) {
            String expectedGroup = batchDriver.BatchgroupsVector.elementAt(i)[0];
            assertEquals(expectedGroup, groups[i]);
        }
    }
    
    @Test
    public void testGetGroups_ExceptionThrown_ReturnsEmptyArray() {
        // Arrange
        batchDriver.BatchgroupsVector = null;
        
        // Act
        String[] groups = batchDriver.getGroups();
        
        // Assert
        assertEquals(0, groups.length);
    }
    
    @Test
    public void testGetBatchJob_ValidGroup_ReturnsNonNullBatchJob() {
        // Arrange
        String validGroup = "Newsgroup1";
        
        // Act
        BatchJob batchJob = batchDriver.getBatchJob(validGroup);
        
        // Assert
        assertNotNull(batchJob);
    }
    
    @Test
    public void testGetBatchJob_InvalidGroup_ReturnsNull() {
        // Arrange
        String invalidGroup = "InvalidGroup";
        
        // Act
        BatchJob batchJob = batchDriver.getBatchJob(invalidGroup);
        
        // Assert
        assertNull(batchJob);
    }
    
    @Test
    public void testGetSubjectLines_ValidGroup_ReturnsNonNullArray() {
        // Arrange
        String validGroup = "Newsgroup1";
        
        // Act
        batchDriver.getBatchJob(validGroup);
        String[] subjectLines = batchDriver.getSubjectLines();
        
        // Assert
        assertNotNull(subjectLines);
    }
    
    @Test
    public void testGetSubjectLines_InvalidGroup_ReturnsNull() {
        // Arrange
        String invalidGroup = "InvalidGroup";
        
        // Act
        batchDriver.getBatchJob(invalidGroup);
        String[] subjectLines = batchDriver.getSubjectLines();
        
        // Assert
        assertNull(subjectLines);
    }
    
    @AfterClass
    public static void tearDownClass() {
        // Deleting test files
        File newziniFile = new File(newzini);
        newziniFile.delete();
        
        File batchfileFile = new File(batchfile);
        batchfileFile.delete();
    }
}