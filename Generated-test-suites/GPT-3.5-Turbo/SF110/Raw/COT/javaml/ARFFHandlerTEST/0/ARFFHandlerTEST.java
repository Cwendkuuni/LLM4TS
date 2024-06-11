
import org.junit.Test;
import org.junit.Assert;
import java.io.FileNotFoundException;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF() {
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            // Test the functionality of loadARFF() here
        } catch (FileNotFoundException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testLoadARFFWithClassIndex() {
        File file = new File("test.arff");
        int classIndex = 2;
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
            // Test the functionality of loadARFF() with classIndex here
        } catch (FileNotFoundException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    // Additional test cases for edge cases and exception handling scenarios

    @Test
    public void testLoadARFFFileNotFound() {
        File file = new File("nonexistent.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            Assert.fail("Expected FileNotFoundException not thrown");
        } catch (FileNotFoundException e) {
            // Exception caught, test passed
        }
    }

    @Test
    public void testLoadARFFWithClassIndexOutOfBounds() {
        File file = new File("test.arff");
        int classIndex = 10;
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
            Assert.fail("Expected IndexOutOfBoundsException not thrown");
        } catch (FileNotFoundException e) {
            Assert.fail("Unexpected exception thrown: " + e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            // Exception caught, test passed
        }
    }

}

