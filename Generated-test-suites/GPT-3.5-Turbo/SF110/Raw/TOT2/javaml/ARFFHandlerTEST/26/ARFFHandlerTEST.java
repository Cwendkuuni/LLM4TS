import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import net.sf.javaml.core.Dataset;

public class ARFFHandlerTest {
    
    @Test
    public void testLoadARFF() {
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            assertNotNull(dataset);
        } catch (FileNotFoundException e) {
            fail("Exception thrown when loading ARFF file");
        }
    }
    
    @Test
    public void testLoadARFF_ClassIndex() {
        File file = new File("test.arff");
        int classIndex = 1;
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
            assertNotNull(dataset);
        } catch (FileNotFoundException e) {
            fail("Exception thrown when loading ARFF file with classIndex");
        }
    }
    
    @Test
    public void testLoadARFF_InvalidFile() {
        File file = new File("invalid.arff");
        try {
            ARFFHandler.loadARFF(file);
            fail("No exception thrown for invalid ARFF file");
        } catch (FileNotFoundException e) {
            // This is expected
        }
    }
    
    @Test
    public void testLoadARFF_InvalidClassIndex() {
        File file = new File("test.arff");
        int classIndex = 10; // Assuming there are not that many attributes
        try {
            ARFFHandler.loadARFF(file, classIndex);
            fail("No exception thrown for invalid class index");
        } catch (ArrayIndexOutOfBoundsException e) {
            // This is expected
        } catch (FileNotFoundException e) {
            fail("Unexpected exception thrown");
        }
    }
}