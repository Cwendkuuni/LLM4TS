import static org.junit.Assert.*;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF_withNoClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        
        assertNotNull(dataset);
        // Add more assertions here for validation
    }

    @Test
    public void testLoadARFF_withClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file, 0);
        
        assertNotNull(dataset);
        // Add more assertions here for validation
    }

    @Test
    public void testLoadARFF_withInvalidFile() {
        try {
            File file = new File("invalid.arff");
            ARFFHandler.loadARFF(file);
            fail("Expected FileNotFoundException was not thrown");
        } catch (FileNotFoundException e) {
            // Expected exception
        }
    }

    // Add more test cases for edge cases and error scenarios

}