import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF_noClassIndex() {
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            assertEquals(0, dataset.size()); // Empty file case
        } catch (FileNotFoundException e) {
            fail("File not found exception thrown");
        }
    }

    @Test
    public void testLoadARFF_withClassIndex() {
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, 1);
            assertEquals(0, dataset.size()); // Empty file case
        } catch (FileNotFoundException e) {
            fail("File not found exception thrown");
        }
    }

    @Test
    public void testLoadARFF_edgeCases() {
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, -1);
            // Test case for handling data with missing or NaN values
            // Test case for handling empty file
            // Test case for handling data not starting with @data
        } catch (FileNotFoundException e) {
            fail("File not found exception thrown");
        }
    }

    @Test
    public void testLoadARFF_exceptions() {
        File file = new File("nonexistent.arff");
        try {
            ARFFHandler.loadARFF(file);
            fail("Expected FileNotFoundException not thrown");
        } catch (FileNotFoundException e) {
            // Expected exception case
        }
    }
}