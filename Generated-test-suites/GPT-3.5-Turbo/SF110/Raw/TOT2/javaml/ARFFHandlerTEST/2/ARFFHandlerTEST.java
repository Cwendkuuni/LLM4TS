import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF_basic() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }

    @Test
    public void testLoadARFF_classIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file, 1);
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }

    @Test
    public void testLoadARFF_edgeCases() throws FileNotFoundException {
        // Test when classIndex is out of bounds
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file, 5);
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
        
        // Test when data format is incorrect
        file = new File("invalid.arff");
        dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_fileNotFound() throws FileNotFoundException {
        File file = new File("nonexistent.arff");
        ARFFHandler.loadARFF(file);
    }
}