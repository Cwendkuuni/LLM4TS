import static org.junit.Assert.*;
import org.junit.Test;
import net.sf.javaml.core.*;

public class ARFFHandlerTest {
    
    @Test
    public void testLoadARFF_NoClassIndex() {
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            assertNotNull(dataset);
            assertEquals(0, dataset.size());
        } catch (FileNotFoundException e) {
            fail("File not found exception");
        }
    }
    
    @Test
    public void testLoadARFF_WithClassIndex() {
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, 3);
            assertNotNull(dataset);
            assertEquals(0, dataset.size());
        } catch (FileNotFoundException | ArrayIndexOutOfBoundsException e) {
            fail("File not found or invalid index");
        }
    }
    
    @Test
    public void testLoadARFF_InvalidDataFormat() {
        File file = new File("invalid.arff");
        try {
            ARFFHandler.loadARFF(file, 2);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            assertTrue(true);
        } catch (FileNotFoundException e) {
            fail("File not found exception");
        }
    }
    
    // Add more test cases for edge cases and error scenarios
    
}