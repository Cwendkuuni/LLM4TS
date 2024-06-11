import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;
import static org.junit.Assert.*;

public class ARFFHandlerTest {

    private static final String TEST_FILE_PATH = "test/testData.arff";
    private static final int TEST_CLASS_INDEX = 0;

    @Test
    public void testLoadARFFWithDefaultClassIndex() throws FileNotFoundException {
        File file = new File(TEST_FILE_PATH);
        Dataset dataset = ARFFHandler.loadARFF(file);

        assertNotNull(dataset);
        assertEquals(6, dataset.size());
    }

    @Test
    public void testLoadARFFWithSpecificClassIndex() throws FileNotFoundException {
        File file = new File(TEST_FILE_PATH);
        Dataset dataset = ARFFHandler.loadARFF(file, TEST_CLASS_INDEX);

        assertNotNull(dataset);
        assertEquals(6, dataset.size());
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFWithNonExistingFile() throws FileNotFoundException {
        File file = new File("nonExistingFile.arff");
        ARFFHandler.loadARFF(file);
    }

    @Test
    public void testLoadARFFWithEmptyFile() throws FileNotFoundException {
        File file = new File(TEST_FILE_PATH);
        Dataset dataset = ARFFHandler.loadARFF(file);

        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }

    @Test
    public void testLoadARFFWithInvalidNumericValue() throws FileNotFoundException {
        File file = new File(TEST_FILE_PATH);
        Dataset dataset = ARFFHandler.loadARFF(file);

        assertNotNull(dataset);
        assertEquals(Double.NaN, dataset.get(2).value(1), 0.001);
    }

    @Test
    public void testLoadARFFWithMissingClassValue() throws FileNotFoundException {
        File file = new File(TEST_FILE_PATH);
        Dataset dataset = ARFFHandler.loadARFF(file);

        assertNotNull(dataset);
        assertEquals(null, dataset.get(5).classValue());
    }

}