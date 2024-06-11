
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF() {
        // Arrange
        File file = new File("test.arff");

        // Act
        Dataset dataset = ARFFHandler.loadARFF(file);

        // Assert
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }

    @Test
    public void testLoadARFFWithClassIndex() {
        // Arrange
        File file = new File("test.arff");
        int classIndex = 2;

        // Act
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);

        // Assert
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFFileNotFound() throws FileNotFoundException {
        // Arrange
        File file = new File("nonexistent.arff");

        // Act
        ARFFHandler.loadARFF(file);

        // Assert (expected exception)
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFFileNotFoundWithClassIndex() throws FileNotFoundException {
        // Arrange
        File file = new File("nonexistent.arff");
        int classIndex = 2;

        // Act
        ARFFHandler.loadARFF(file, classIndex);

        // Assert (expected exception)
    }
}
