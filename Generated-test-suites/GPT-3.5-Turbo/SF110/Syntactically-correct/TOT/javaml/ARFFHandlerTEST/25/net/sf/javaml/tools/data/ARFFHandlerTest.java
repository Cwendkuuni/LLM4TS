import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;

public class ARFFHandlerTest {

    // Test case for loadARFF(File file) method
    @Test
    public void testLoadARFFFile() throws FileNotFoundException {
        // Arrange
        File file = new File("test.arff");

        // Act
        Dataset dataset = ARFFHandler.loadARFF(file);

        // Assert
        assertNotNull(dataset);
    }

    // Test case for loadARFF(File file, int classIndex) method
    @Test
    public void testLoadARFFFileWithClassIndex() throws FileNotFoundException {
        // Arrange
        File file = new File("test.arff");
        int classIndex = 1;

        // Act
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);

        // Assert
        assertNotNull(dataset);
    }

    // Test case for loadARFF(File file) method with empty file
    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFFileEmpty() throws FileNotFoundException {
        // Arrange
        File file = new File("empty.arff");

        // Act
        ARFFHandler.loadARFF(file);
    }

    // Test case for loadARFF(File file) method with invalid file format
    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFFInvalidFormat() throws FileNotFoundException {
        // Arrange
        File file = new File("invalid.arff");

        // Act
        ARFFHandler.loadARFF(file);
    }

    // Test case for loadARFF(File file, int classIndex) method with negative classIndex
    @Test(expected = IndexOutOfBoundsException.class)
    public void testLoadARFFFileNegativeClassIndex() throws FileNotFoundException {
        // Arrange
        File file = new File("test.arff");
        int classIndex = -1;

        // Act
        ARFFHandler.loadARFF(file, classIndex);
    }

    // Test case for loadARFF(File file, int classIndex) method with classIndex greater than number of attributes
    @Test(expected = IndexOutOfBoundsException.class)
    public void testLoadARFFFileInvalidClassIndex() throws FileNotFoundException {
        // Arrange
        File file = new File("test.arff");
        int classIndex = 10;

        // Act
        ARFFHandler.loadARFF(file, classIndex);
    }

    // Test case for loadARFF(File file) method with missing attribute values
    @Test
    public void testLoadARFFFileMissingAttributeValues() throws FileNotFoundException {
        // Arrange
        File file = new File("test_missing_values.arff");

        // Act
        Dataset dataset = ARFFHandler.loadARFF(file);

        // Assert
        assertNotNull(dataset);
        assertEquals(3, dataset.size());
    }

    // Test case for loadARFF(File file) method with non-numeric attribute values
    @Test
    public void testLoadARFFFileNonNumericAttributeValues() throws FileNotFoundException {
        // Arrange
        File file = new File("test_non_numeric_values.arff");

        // Act
        Dataset dataset = ARFFHandler.loadARFF(file);

        // Assert
        assertNotNull(dataset);
        assertEquals(3, dataset.size());
        for (Instance instance : dataset) {
            assertFalse(Double.isNaN(instance.value(0)));
        }
    }
}