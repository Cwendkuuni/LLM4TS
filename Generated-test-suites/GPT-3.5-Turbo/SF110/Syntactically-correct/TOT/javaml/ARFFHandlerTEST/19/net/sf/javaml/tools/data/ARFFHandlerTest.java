import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;
import net.sf.javaml.core.Dataset;

public class ARFFHandlerTest {
    
    private File file;
    
    @Before
    public void setUp() {
        file = new File("test.arff"); // Replace with path to a test ARFF file
    }
    
    @Test
    public void testLoadARFF_withValidFile_returnsDataset() throws FileNotFoundException {
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_withInvalidFile_throwsFileNotFoundException() throws FileNotFoundException {
        File invalidFile = new File("invalid.arff"); // Replace with path to a non-existent ARFF file
        ARFFHandler.loadARFF(invalidFile);
    }
    
    @Test
    public void testLoadARFF_withValidFileAndClassIndex_returnsDatasetWithCorrectClassIndex() throws FileNotFoundException {
        int classIndex = 2; // Replace with a valid class index for the test ARFF file
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        assertEquals(classIndex, dataset.classIndex());
    }
    
    @Test
    public void testLoadARFF_withValidFileAndNegativeClassIndex_returnsDatasetWithNoClassIndex() throws FileNotFoundException {
        int classIndex = -1;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        assertEquals(classIndex, dataset.classIndex());
    }
    
    @Test
    public void testLoadARFF_withInvalidClassIndex_returnsDatasetWithNoClassIndex() throws FileNotFoundException {
        int classIndex = 100; // Assuming classIndex is out of bounds for the test ARFF file
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        assertEquals(-1, dataset.classIndex());
    }
    
    @Test
    public void testLoadARFF_withValidFileAndMissingClassValues_returnsDatasetWithNaNValues() throws FileNotFoundException {
        int classIndex = 2; // Replace with a valid class index for the test ARFF file
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        for (int i = 0; i < dataset.size(); i++) {
            double classValue = dataset.instance(i).classValue();
            assertTrue(Double.isNaN(classValue));
        }
    }
    
    @Test
    public void testLoadARFF_withValidFileAndInvalidNumericValues_returnsDatasetWithNaNValues() throws FileNotFoundException {
        int classIndex = 2; // Replace with a valid class index for the test ARFF file
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        for (int i = 0; i < dataset.size(); i++) {
            double[] values = dataset.instance(i).toDoubleArray();
            for (double value : values) {
                assertTrue(Double.isNaN(value));
            }
        }
    }
}