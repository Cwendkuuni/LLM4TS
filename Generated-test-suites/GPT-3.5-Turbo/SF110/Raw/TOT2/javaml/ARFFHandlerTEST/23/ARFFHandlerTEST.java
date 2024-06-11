import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.FileNotFoundException;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF_file() {
        try {
            File file = new File("test.arff");
            Dataset dataset = ARFFHandler.loadARFF(file);
            assertNotNull(dataset);
            // Add more assertions based on typical use cases
        } catch (FileNotFoundException e) {
            fail("File not found exception");
        }
    }

    @Test
    public void testLoadARFF_file_classIndex() {
        try {
            File file = new File("test.arff");
            int classIndex = 0;
            Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
            assertNotNull(dataset);
            // Add more assertions based on typical use cases
        } catch (FileNotFoundException e) {
            fail("File not found exception");
        }
    }

    @Test
    public void testLoadARFF_file_invalidFile() {
        try {
            File file = new File("non_existent_file.arff");
            Dataset dataset = ARFFHandler.loadARFF(file);
            fail("Expected FileNotFoundException");
        } catch (FileNotFoundException e) {
            // Exception expected
        }
    }

    // Add more test cases for edge cases and error scenarios

}