import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF() {
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            assertNotNull(dataset);
            assertEquals(0, dataset.size()); // Empty dataset
        } catch (FileNotFoundException e) {
            fail("File not found exception");
        }
    }

    @Test
    public void testLoadARFFWithClassIndex() {
        File file = new File("test_arff_with_class_index.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, 1);
            assertNotNull(dataset);
            assertEquals(0, dataset.size()); // Empty dataset
        } catch (FileNotFoundException e) {
            fail("File not found exception");
        }
    }

    @Test
    public void testLoadARFFWithInvalidFile() {
        File file = new File("invalid_file.arff");
        try {
            ARFFHandler.loadARFF(file);
            fail("Expected FileNotFoundException");
        } catch (FileNotFoundException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testLoadARFFWithInvalidClassIndex() {
        File file = new File("test.arff");
        try {
            ARFFHandler.loadARFF(file, 10); // Index out of range
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            assertTrue(true);
        } catch (FileNotFoundException e) {
            fail("Unexpected FileNotFoundException");
        }
    }

    // Add more test cases for edge cases and error scenarios if needed

}