import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {
  
  @Test
  public void testLoad_File_ValidInput() {
    // Arrange
    File file = new File("validFile.txt");
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    
    // Act
    Document result = DataUtil.load(file, charsetName, baseUri);
    
    // Assert
    assertNotNull(result);
    // additional assertions for valid input
  }
  
  @Test(expected = IOException.class)
  public void testLoad_File_InvalidFile() throws IOException {
    // Arrange
    File file = new File("nonExistentFile.txt");
    String charsetName = "UTF-8";
    String baseUri = "http://example.com";
    
    // Act
    Document result = DataUtil.load(file, charsetName, baseUri);
    
    // Assert
    // IOException should be thrown
  }
  
  // Additional test cases for other methods in DataUtil class
  
}