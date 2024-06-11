import static org.junit.Assert.*;
import java.io.File;
import org.junit.Test;
import net.sf.javaml.core.Dataset;
import java.util.NoSuchElementException;
import java.io.FileNotFoundException;

public class ARFFHandlerTest {

    // Test case for loadARFF(File file)
    @Test
    public void testLoadARFFFile() throws FileNotFoundException {
        File file = new File("data.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);

        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }

    // Test case for loadARFF(File file, int classIndex)
    @Test
    public void testLoadARFFFileWithClassIndex() throws FileNotFoundException {
        File file = new File("data.arff");
        int classIndex = 5;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);

        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }

    // Edge case: Empty file input
    @Test(expected = NoSuchElementException.class)
    public void testLoadARFFEmptyFile() throws FileNotFoundException {
        File file = new File("empty.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
    }

    // Edge case: Non-existent file input
    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFNonExistentFile() throws FileNotFoundException {
        File file = new File("nonexistent.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
    }

    // Error scenario: Invalid data format (non-numeric value)
    @Test
    public void testLoadARFFInvalidDataFormat() throws FileNotFoundException {
        File file = new File("invalid.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);

        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }

    // Error scenario: Class index out of range
    @Test
    public void testLoadARFFInvalidClassIndex() throws FileNotFoundException {
        File file = new File("data.arff");
        int classIndex = 10; // Assuming there are only 9 attributes
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);

        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }

}