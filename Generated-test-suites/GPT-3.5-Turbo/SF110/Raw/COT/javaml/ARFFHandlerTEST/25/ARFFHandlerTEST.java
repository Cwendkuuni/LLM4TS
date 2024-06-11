import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;

public class ARFFHandlerTest {
    
    @Test
    public void testLoadARFF() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        
        // Assert that the returned dataset is not null
        assertNotNull(dataset);
        
        // Add additional assertions to check the functionality of the method
        // ...
    }
    
    @Test
    public void testLoadARFFWithClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = 2;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        
        // Assert that the returned dataset is not null
        assertNotNull(dataset);
        
        // Add additional assertions to check the functionality of the method
        // ...
    }
    
    // Add more basic test cases for other public methods in the ARFFHandler class
    
    @Test
    public void testLoadARFFWithInvalidFile() {
        File file = new File("invalid.arff");
        
        // Assert that a FileNotFoundException is thrown
        assertThrows(FileNotFoundException.class, () -> {
            ARFFHandler.loadARFF(file);
        });
    }
    
    // Add test cases to handle other exceptions and edge cases
    
}



// Merge all the individual test cases here

