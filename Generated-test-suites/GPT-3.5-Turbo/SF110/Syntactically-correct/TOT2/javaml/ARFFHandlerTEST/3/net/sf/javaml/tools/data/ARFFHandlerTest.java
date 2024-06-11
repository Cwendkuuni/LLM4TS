package net.sf.javaml.tools.data;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;
import net.sf.javaml.core.Dataset;

public class ARFFHandlerTest {
    
    @Test
    public void testLoadARFF_withDefaultClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        // Add more assertions to test the loaded dataset
    }
    
    @Test
    public void testLoadARFF_withCustomClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file, 1);
        assertNotNull(dataset);
        // Add more assertions to test the loaded dataset with custom class index
    }
    
    @Test
    public void testLoadARFF_withInvalidFile() {
        try {
            File file = new File("invalid.arff");
            ARFFHandler.loadARFF(file);
            fail("FileNotFoundException not thrown");
        } catch (FileNotFoundException e) {
            // Exception caught, test passed
        }
    }
    
    @Test
    public void testLoadARFF_withInvalidClassIndex() {
        try {
            File file = new File("test.arff");
            ARFFHandler.loadARFF(file, 10);
            fail("IndexOutOfBoundsException not thrown");
        } catch (IndexOutOfBoundsException e) {
            // Exception caught, test passed
        } catch (FileNotFoundException e) {
            fail("Unexpected FileNotFoundException");
        }
    }
    
    // Add more test methods to cover edge cases and error scenarios
    
}