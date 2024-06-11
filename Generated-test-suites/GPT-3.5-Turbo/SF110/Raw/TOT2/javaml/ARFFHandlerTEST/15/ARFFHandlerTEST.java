import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFFNoClassIndex() {
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            assertNotNull(dataset);
            // Add more assertions for expected results
        } catch (FileNotFoundException e) {
            fail("Unexpected FileNotFoundException");
        }
    }

    @Test
    public void testLoadARFFWithClassIndex() {
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, 0);
            assertNotNull(dataset);
            // Add more assertions for expected results
        } catch (FileNotFoundException e) {
            fail("Unexpected FileNotFoundException");
        }
    }

    // Add more test cases for edge cases and exception handling scenarios

}