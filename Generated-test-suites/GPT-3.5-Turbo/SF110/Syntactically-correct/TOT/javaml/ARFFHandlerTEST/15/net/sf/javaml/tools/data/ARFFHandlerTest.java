import org.junit.Test;
import static org.junit.Assert.*;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF_withFile_shouldLoadDataset() {
        // Arrange
        File testFile = new File("test.arff");
        
        // Act
        Dataset result = ARFFHandler.loadARFF(testFile);
        
        // Assert
        assertNotNull(result);
        // Additional assertions to verify the loaded dataset
    }
    
    @Test
    public void testLoadARFF_withNonExistingFile_shouldThrowFileNotFoundException() {
        // Arrange
        File testFile = new File("nonExistingFile.arff");
        
        // Act and Assert
        assertThrows(FileNotFoundException.class, () -> ARFFHandler.loadARFF(testFile));
    }
    
    @Test
    public void testLoadARFF_withFileAndClassIndex_shouldLoadDataset() {
        // Arrange
        File testFile = new File("test.arff");
        int classIndex = 1;
        
        // Act
        Dataset result = ARFFHandler.loadARFF(testFile, classIndex);
        
        // Assert
        assertNotNull(result);
        // Additional assertions to verify the loaded dataset with class index
    }
    
    @Test
    public void testLoadARFF_withFileAndInvalidClassIndex_shouldLoadDatasetWithNaNValuesForClassIndex() {
        // Arrange
        File testFile = new File("test.arff");
        int invalidClassIndex = 10;
        
        // Act
        Dataset result = ARFFHandler.loadARFF(testFile, invalidClassIndex);
        
        // Assert
        assertNotNull(result);
        // Additional assertions to verify the loaded dataset with NaN values for invalid class index
    }
    
    @Test
    public void testLoadARFF_withInvalidFileAndClassIndex_shouldThrowFileNotFoundException() {
        // Arrange
        File testFile = new File("nonExistingFile.arff");
        int classIndex = 1;
        
        // Act and Assert
        assertThrows(FileNotFoundException.class, () -> ARFFHandler.loadARFF(testFile, classIndex));
    }
    
    // Add more test methods to cover other scenarios and edge cases
    
}