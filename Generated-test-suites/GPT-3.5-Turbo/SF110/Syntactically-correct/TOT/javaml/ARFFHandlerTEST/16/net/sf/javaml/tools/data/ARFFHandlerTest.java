import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;

public class ARFFHandlerTest {
    
    @Test
    public void testLoadARFF_withValidFile_shouldReturnDataset() throws FileNotFoundException {
        // Arrange
        File file = new File("test.arff");
        
        // Act
        Dataset dataset = ARFFHandler.loadARFF(file);
        
        // Assert
        assertNotNull(dataset);
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_withInvalidFile_shouldThrowFileNotFoundException() throws FileNotFoundException {
        // Arrange
        File file = new File("invalid.arff");
        
        // Act
        ARFFHandler.loadARFF(file);
        
        // Assert (Exception expected)
    }
    
    @Test
    public void testLoadARFF_withValidFileAndClassIndex_shouldReturnDatasetWithClassIndex() throws FileNotFoundException {
        // Arrange
        File file = new File("test.arff");
        int classIndex = 2;
        
        // Act
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        
        // Assert
        assertNotNull(dataset);
        for (Instance instance : dataset) {
            assertEquals(classIndex, instance.classIndex());
        }
    }
    
    @Test
    public void testLoadARFF_withValidFileAndNegativeClassIndex_shouldReturnDatasetWithoutClassIndex() throws FileNotFoundException {
        // Arrange
        File file = new File("test.arff");
        int classIndex = -1;
        
        // Act
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        
        // Assert
        assertNotNull(dataset);
        for (Instance instance : dataset) {
            assertEquals(-1, instance.classIndex());
        }
    }
    
    @Test
    public void testLoadARFF_withEmptyFile_shouldReturnEmptyDataset() throws FileNotFoundException {
        // Arrange
        File file = new File("empty.arff");
        
        // Act
        Dataset dataset = ARFFHandler.loadARFF(file);
        
        // Assert
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }
    
    @Test
    public void testLoadARFF_withFileWithoutDataSection_shouldReturnEmptyDataset() throws FileNotFoundException {
        // Arrange
        File file = new File("nodata.arff");
        
        // Act
        Dataset dataset = ARFFHandler.loadARFF(file);
        
        // Assert
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }
    
    @Test
    public void testLoadARFF_withInvalidNumericData_shouldHandleNumberFormatException() throws FileNotFoundException {
        // Arrange
        File file = new File("numericdata.arff");
        
        // Act
        Dataset dataset = ARFFHandler.loadARFF(file);
        
        // Assert
        assertNotNull(dataset);
        assertEquals(Double.NaN, dataset.get(0).doubleValue(0), 0.001);
    }
}