import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import net.sf.javaml.tools.data.*;
import net.sf.javaml.core.*;

public class ARFFHandlerTest {
    
    // Test loadARFF method
    
    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_FileNotFound() throws FileNotFoundException {
        File file = new File("non_existent_file.arff");
        ARFFHandler.loadARFF(file);
    }
    
    @Test
    public void testLoadARFF_DefaultClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        // Add more assertions here for testing the loaded dataset
    }
    
    @Test
    public void testLoadARFF_SpecifiedClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = 2;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        assertNotNull(dataset);
        // Add more assertions here for testing the loaded dataset
    }
    
    // Add more test methods for other methods in ARFFHandler class
    
}