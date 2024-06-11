import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import net.sf.javaml.core.Dataset;
import java.io.File;
import java.io.FileNotFoundException;


public class ARFFHandlerTest {
    
    private File file;
    private File invalidFile;
    
    @Before
    public void setUp() {
        file = new File("test.arff");
        invalidFile = new File("invalid.arff");
    }
    
    @Test
    public void testLoadARFF_withValidFile_returnsDataset() throws FileNotFoundException {
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertFalse(dataset.isEmpty());
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_withInvalidFile_throwsFileNotFoundException() throws FileNotFoundException {
        Dataset dataset = ARFFHandler.loadARFF(invalidFile);
    }
    
    @Test
    public void testLoadARFF_withValidFileAndClassIndex_returnsDatasetWithClassIndex() throws FileNotFoundException {
        Dataset dataset = ARFFHandler.loadARFF(file, 1);
        assertFalse(dataset.isEmpty());
        assertEquals(1, dataset.get(0).classValue(), 0.001);
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_withInvalidFileAndClassIndex_throwsFileNotFoundException() throws FileNotFoundException {
        Dataset dataset = ARFFHandler.loadARFF(invalidFile, 2);
    }

    @Test
    public void testLoadARFF_withValidFileAndNegativeClassIndex_returnsDatasetWithoutClassIndex() throws FileNotFoundException {
        Dataset dataset = ARFFHandler.loadARFF(file, -1);
        assertFalse(dataset.isEmpty());
        assertTrue(dataset.get(0).classIsMissing());
    }
    
    @Test
    public void testLoadARFF_withFileContainingMissingValue_returnsDatasetWithMissingValues() throws FileNotFoundException {
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertFalse(dataset.isEmpty());
        
        assertEquals(Double.NaN, dataset.get(0).value(0), 0.001);
    }
    
    @Test
    public void testLoadARFF_withFileContainingInvalidNumericValue_returnsDatasetWithInvalidValues() throws FileNotFoundException {
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertFalse(dataset.isEmpty());
        
        assertEquals(0, dataset.get(0).value(1), 0.001);
    }
}