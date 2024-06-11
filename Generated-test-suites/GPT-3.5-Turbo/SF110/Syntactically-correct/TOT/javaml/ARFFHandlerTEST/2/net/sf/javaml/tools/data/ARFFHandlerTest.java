import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.tools.data.ARFFHandler;

public class ARFFHandlerTest {
    
    private File validFile;
    private File invalidFile;
    
    @Before
    public void setUp() {
        validFile = new File("valid.arff"); // Assumes a valid ARFF file exists with name valid.arff
        invalidFile = new File("invalid.arff"); // Assumes an invalid ARFF file exists with name invalid.arff
    }
    
    @Test
    public void testLoadARFF_WithValidFile_ShouldReturnDataset() throws FileNotFoundException {
        Dataset dataset = ARFFHandler.loadARFF(validFile);
        assertNotNull(dataset);
        // Add assertions to verify dataset content, e.g. number of instances, attributes, etc.
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_WithMissingFile_ShouldThrowFileNotFoundException() throws FileNotFoundException {
        ARFFHandler.loadARFF(new File("nonexistent.arff"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testLoadARFF_WithNegativeClassIndex_ShouldThrowIllegalArgumentException() throws FileNotFoundException {
        ARFFHandler.loadARFF(validFile, -2);
    }
    
    @Test
    public void testLoadARFF_WithClassIndex1_ShouldReturnDataset() throws FileNotFoundException {
        Dataset dataset = ARFFHandler.loadARFF(validFile, 1);
        assertNotNull(dataset);
        // Add assertions to verify dataset content when class index is specified
    }
    
    // Additional test cases for edge cases, typical use cases, and error scenarios can be added here
    
}