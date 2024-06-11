import static org.junit.Assert.*;
import org.junit.Test;
import net.sf.javaml.*;
import java.io.*;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF_singleFile() {
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            assertNotNull(dataset);
            // Add more assertions to test the loaded dataset
        } catch (FileNotFoundException e) {
            fail("FileNotFoundException should not be thrown");
        }
    }

    @Test
    public void testLoadARFF_withClassIndex() {
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, 1);
            assertNotNull(dataset);
            // Add more assertions to test the loaded dataset
        } catch (FileNotFoundException e) {
            fail("FileNotFoundException should not be thrown");
        }
    }

    // Edge cases and exception handling scenarios
    @Test
    public void testLoadARFF_invalidClassIndex() {
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, 5);
            // ClassIndex out of bounds should throw ArrayIndexOutOfBoundsException
            fail("ArrayIndexOutOfBoundsException should be thrown for invalid classIndex");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Exception is expected
        } catch (FileNotFoundException e) {
            fail("FileNotFoundException should not be thrown");
        }
    }

    @Test
    public void testLoadARFF_invalidDataFormat() {
        File file = new File("invalidData.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            // Data with incorrect format should throw NumberFormatException
            fail("NumberFormatException should be thrown for invalid data format");
        } catch (NumberFormatException e) {
            // Exception is expected
        } catch (FileNotFoundException e) {
            fail("FileNotFoundException should not be thrown");
        }
    }
}