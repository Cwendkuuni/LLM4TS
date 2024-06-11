import org.junit.*;
import static org.junit.Assert.*;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF() {
        // Test case for loadARFF(File file)
        // Functionality: Loads ARFF file into Dataset object
        File file = new File("test.arff"); // replace with actual file path
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            
            // Assert that dataset is not null
            assertNotNull(dataset);
            
            // Assert that dataset is of type DefaultDataset
            assertTrue(dataset instanceof DefaultDataset);
            
            // Additional test cases for specific edge cases and exception handling scenarios:            
        } catch (FileNotFoundException e) {
            // Handle file not found exception
            fail("File not found: " + file.getAbsolutePath());
        }
    }
    
    @Test
    public void testLoadARFFWithClassIndex() {
        // Test case for loadARFF(File file, int classIndex)
        // Functionality: Loads ARFF file into Dataset object with specified class index
        File file = new File("test.arff"); // replace with actual file path
        int classIndex = 2; // replace with actual class index
        
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
            
            // Assert that dataset is not null
            assertNotNull(dataset);
            
            // Assert that dataset is of type DefaultDataset
            assertTrue(dataset instanceof DefaultDataset);
            
            // Additional test cases for specific edge cases and exception handling scenarios:            
        } catch (FileNotFoundException e) {
            // Handle file not found exception
            fail("File not found: " + file.getAbsolutePath());
        }
    }
}