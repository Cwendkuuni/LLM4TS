import java.io.File;
import java.io.FileNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ARFFHandlerTest {
    
    private ARFFHandler arffHandler;
    private File file;
    
    @Before
    public void setUp() {
        arffHandler = new ARFFHandler();
        file = new File("test.arff"); // Replace with the actual file path for testing
    }
    
    @After
    public void tearDown() {
        arffHandler = null;
        file = null;
    }
    
    @Test
    public void testLoadARFF_withFileParameter_returnsDataset() throws FileNotFoundException {
        Dataset result = ARFFHandler.loadARFF(file);
        
        assertNotNull(result);
    }
    
    @Test
    public void testLoadARFF_withFileAndClassIndexParameters_returnsDataset() throws FileNotFoundException {
        int classIndex = 0; // Replace with the desired class index for testing
        Dataset result = ARFFHandler.loadARFF(file, classIndex);
        
        assertNotNull(result);
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_withNonExistingFile_throwsFileNotFoundException() throws FileNotFoundException {
        File nonExistingFile = new File("non_existing.arff");
        
        ARFFHandler.loadARFF(nonExistingFile);
    }
    
    // Add additional test cases for edge cases and exception handling scenarios
    
}