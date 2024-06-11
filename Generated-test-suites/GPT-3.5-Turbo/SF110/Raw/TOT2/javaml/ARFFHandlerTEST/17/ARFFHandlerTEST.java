import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF_NoClassIndex() {
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            assertNotNull(dataset);
            // Add more assertions here if needed
        } catch (FileNotFoundException e) {
            fail("File not found");
        }
    }
    
    @Test
    public void testLoadARFF_WithClassIndex() {
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, 1);
            assertNotNull(dataset);
            // Add more assertions here if needed
        } catch (FileNotFoundException e) {
            fail("File not found");
        }
    }

    // Add more test cases for edge cases and error scenarios here

}