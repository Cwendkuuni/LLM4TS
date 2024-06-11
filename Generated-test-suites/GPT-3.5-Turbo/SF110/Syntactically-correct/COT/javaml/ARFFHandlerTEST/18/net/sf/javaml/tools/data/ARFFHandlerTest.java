import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;

public class ARFFHandlerTest {
    
    @Test
    public void testLoadARFF() throws FileNotFoundException {
        // Test loading ARFF file with default class index
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        
        // Test loading ARFF file with specified class index
        int classIndex = 0;
        dataset = ARFFHandler.loadARFF(file, classIndex);
        assertNotNull(dataset);
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFFileNotFound() throws FileNotFoundException {
        // Test handling FileNotFoundException
        File file = new File("nonexistent.arff");
        ARFFHandler.loadARFF(file);
    }
    
    @Test
    public void testLoadARFFInvalidData() throws FileNotFoundException {
        // Test loading ARFF file with invalid data
        File file = new File("invalid.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
    }
    
    @Test
    public void testLoadARFFNoData() throws FileNotFoundException {
        // Test loading ARFF file with no data
        File file = new File("nodata.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
    }
    
}