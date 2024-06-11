
import org.junit.Test;
import net.sf.javaml.core.Dataset;
import java.io.File;
import java.io.FileNotFoundException;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF_withFile() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset result = ARFFHandler.loadARFF(file);
        // Add assertions here to verify the functionality of the method
    }
    
    @Test
    public void testLoadARFF_withFileAndClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = 0; // Set the class index to the desired value
        Dataset result = ARFFHandler.loadARFF(file, classIndex);
        // Add assertions here to verify the functionality of the method
    }

    // Add additional test cases here for potential edge cases and exception handling scenarios

}
