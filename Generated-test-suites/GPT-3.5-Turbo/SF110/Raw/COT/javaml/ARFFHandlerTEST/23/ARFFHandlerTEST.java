import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.DenseInstance;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF_defaultClassIndex() {
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            // Add assertions to test the functionality of the method
            // ...
        } catch (FileNotFoundException e) {
            fail("File not found");
        }
    }
    
    @Test
    public void testLoadARFF_customClassIndex() {
        File file = new File("test.arff");
        int classIndex = 1;
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
            // Add assertions to test the functionality of the method
            // ...
        } catch (FileNotFoundException e) {
            fail("File not found");
        }
    }
    
    // Add additional test cases for potential edge cases and exceptions
    // ...

}