import static org.junit.Assert.*;
import org.junit.*;
import java.io.File;
import java.io.FileNotFoundException;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;

public class ARFFHandlerTest {
    
    @Test
    public void testLoadARFF_noClassIndex() throws FileNotFoundException {
        File file = new File("path/to/file.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        
        // Assert statements
        assertNotNull(dataset);
        // Add more assertions
    }
    
    @Test
    public void testLoadARFF_withClassIndex() throws FileNotFoundException {
        File file = new File("path/to/file.arff");
        int classIndex = 1;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        
        // Assert statements
        assertNotNull(dataset);
        // Add more assertions
    }
    
    // Add more test methods to cover all cases
    
}