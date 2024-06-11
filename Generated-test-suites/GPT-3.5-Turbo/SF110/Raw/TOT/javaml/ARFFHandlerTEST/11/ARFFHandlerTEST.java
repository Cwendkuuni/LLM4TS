import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;

public class ARFFHandlerTest {

    // Test case for loadARFF method with valid file argument
    @Test
    public void testLoadARFF_ValidFile() throws FileNotFoundException {
        File file = new File("valid_data.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        
        assertNotNull(dataset);
        // Assert additional conditions as per requirements
    }
    
    // Test case for loadARFF method with valid file and class index argument
    @Test
    public void testLoadARFF_ValidFileAndClassIndex() throws FileNotFoundException {
        File file = new File("valid_data.arff");
        int classIndex = 1; // Assuming a valid class index
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        
        assertNotNull(dataset);
        // Assert additional conditions as per requirements
    }
    
    // Test case for loadARFF method with invalid file argument
    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_InvalidFile() throws FileNotFoundException {
        File file = new File("non_existent_file.arff");
        ARFFHandler.loadARFF(file);
    }
    
    // Test case for loadARFF method with invalid class index argument
    @Test(expected = IndexOutOfBoundsException.class)
    public void testLoadARFF_InvalidClassIndex() throws FileNotFoundException {
        File file = new File("valid_data.arff");
        int classIndex = 10; // Assuming an invalid class index
        ARFFHandler.loadARFF(file, classIndex);
    }
    
    // Additional test cases can be added to cover more scenarios
    
}