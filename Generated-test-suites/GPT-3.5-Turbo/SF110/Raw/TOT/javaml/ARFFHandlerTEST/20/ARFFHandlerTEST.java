import org.junit.Before;
import org.junit.Test;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.tools.data.ARFFHandler;
import java.io.File;
import java.io.FileNotFoundException;

public class ARFFHandlerTest {

    private Dataset dataset;

    @Before
    public void setUp() {
        dataset = null;
    }

    @Test
    public void testLoadARFF_ValidFile_ReturnsDataset() throws FileNotFoundException {
        // Arrange
        File file = new File("validFile.arff");

        // Act
        dataset = ARFFHandler.loadARFF(file);

        // Assert
        assertNotNull(dataset);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_InvalidFile_ThrowsFileNotFoundException() throws FileNotFoundException {
        // Arrange
        File file = new File("invalidFile.arff");

        // Act
        dataset = ARFFHandler.loadARFF(file);

        // Assert
        // Expects FileNotFoundException
    }

    @Test
    public void testLoadARFF_ValidFileWithClassIndex_ReturnsDataset() throws FileNotFoundException {
        // Arrange
        File file = new File("validFile.arff");
        int classIndex = 1;

        // Act
        dataset = ARFFHandler.loadARFF(file, classIndex);

        // Assert
        assertNotNull(dataset);
    }

    @Test
    public void testLoadARFF_ValidFileWithNegativeClassIndex_ReturnsDataset() throws FileNotFoundException {
        // Arrange
        File file = new File("validFile.arff");
        int classIndex = -1;

        // Act
        dataset = ARFFHandler.loadARFF(file, classIndex);

        // Assert
        assertNotNull(dataset);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_InvalidFileWithClassIndex_ThrowsFileNotFoundException() throws FileNotFoundException {
        // Arrange
        File file = new File("invalidFile.arff");
        int classIndex = 1;

        // Act
        dataset = ARFFHandler.loadARFF(file, classIndex);

        // Assert
        // Expects FileNotFoundException
    }

    @Test
    public void testLoadARFF_ValidFileWithInvalidClassIndex_ReturnsDataset() throws FileNotFoundException {
        // Arrange
        File file = new File("validFile.arff");
        int classIndex = 10;

        // Act
        dataset = ARFFHandler.loadARFF(file, classIndex);

        // Assert
        assertNotNull(dataset);
    }

    @Test
    public void testLoadARFF_ValidFileWithNegativeInvalidClassIndex_ReturnsDataset() throws FileNotFoundException {
        // Arrange
        File file = new File("validFile.arff");
        int classIndex = -10;

        // Act
        dataset = ARFFHandler.loadARFF(file, classIndex);

        // Assert
        assertNotNull(dataset);
    }
}