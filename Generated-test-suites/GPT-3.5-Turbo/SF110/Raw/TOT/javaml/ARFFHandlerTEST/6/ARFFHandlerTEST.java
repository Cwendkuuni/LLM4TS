import org.junit.*;
import static org.junit.Assert.*;
import net.sf.javaml.core.*;

public class ARFFHandlerTest {
    
    @Test
    public void testLoadARFF_WithValidFile_ReturnsDataset() {
        try {
            Dataset dataset = ARFFHandler.loadARFF(new File("validFile.arff"));
            assertTrue(dataset instanceof Dataset);
        } catch (FileNotFoundException e) {
            fail("Valid file not found");
        }
    }
    
    @Test
    public void testLoadARFF_WithInvalidFile_ThrowsFileNotFoundException() {
        try {
            ARFFHandler.loadARFF(new File("invalidFile.arff"));
            fail("FileNotFoundException not thrown");
        } catch (FileNotFoundException e) {
            assertEquals("File not found", e.getMessage());
        }
    }
    
    @Test
    public void testLoadARFF_WithClassIndex_ReturnsCorrectDataset() {
        try {
            Dataset dataset = ARFFHandler.loadARFF(new File("validFile.arff"), 2);
            assertTrue(dataset instanceof Dataset);
            Instance instance = dataset.get(0);
            assertEquals(2.0, instance.classValue(), 0.001);
        } catch (FileNotFoundException e) {
            fail("Valid file not found");
        }
    }
    
    @Test
    public void testLoadARFF_WithNegativeClassIndex_ReturnsDatasetWithoutClass() {
        try {
            Dataset dataset = ARFFHandler.loadARFF(new File("validFile.arff"), -1);
            assertTrue(dataset instanceof Dataset);
            Instance instance = dataset.get(0);
            assertTrue(Double.isNaN(instance.classValue()));
        } catch (FileNotFoundException e) {
            fail("Valid file not found");
        }
    }
    
    @Test
    public void testLoadARFF_WithEmptyFile_ReturnsEmptyDataset() {
        try {
            Dataset dataset = ARFFHandler.loadARFF(new File("emptyFile.arff"));
            assertTrue(dataset instanceof Dataset);
            assertEquals(0, dataset.size());
        } catch (FileNotFoundException e) {
            fail("Empty file not found");
        }
    }
    
    @Test
    public void testLoadARFF_WithInvalidData_ReturnsDatasetWithNaNValues() {
        try {
            Dataset dataset = ARFFHandler.loadARFF(new File("invalidData.arff"));
            assertTrue(dataset instanceof Dataset);
            Instance instance = dataset.get(0);
            double[] values = instance.values();
            
            for (double value : values) {
                assertTrue(Double.isNaN(value));
            }
        } catch (FileNotFoundException e) {
            fail("Invalid data file not found");
        }
    }
}