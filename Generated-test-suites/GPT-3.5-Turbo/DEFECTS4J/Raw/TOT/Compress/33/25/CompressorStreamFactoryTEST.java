import org.junit.Test;
import static org.junit.Assert.*;

public class CompressorStreamFactoryTest {

  //Test case for createCompressorInputStream(InputStream in)
  @Test
  public void testCreateCompressorInputStream() throws Exception {
    // Arrange
    CompressorStreamFactory factory = new CompressorStreamFactory();
    
    // Act
    CompressorInputStream result = factory.createCompressorInputStream(null);
    // Assert
    assertEquals(null, result);
  }

  //Test case for createCompressorInputStream(String name, InputStream in)
  @Test
  public void testCreateCompressorInputStreamWithNameAndInputStream() throws Exception {
    // Arrange
    CompressorStreamFactory factory = new CompressorStreamFactory();
    String name = "unknown";
    
    // Act and Assert
    try {
      CompressorInputStream result = factory.createCompressorInputStream(name, null);
      fail("Should have thrown CompressorException");
    } catch(CompressorException e) {
      assertEquals("Compressor: unknown not found.", e.getMessage());
    }
  }

  //Test case for createCompressorOutputStream(String name, OutputStream out)
  @Test
  public void testCreateCompressorOutputStream() throws Exception {
    // Arrange
    CompressorStreamFactory factory = new CompressorStreamFactory();
    String name = "unknown";
    
    // Act and Assert
    try {
      CompressorOutputStream result = factory.createCompressorOutputStream(name, null);
      fail("Should have thrown CompressorException");
    } catch(CompressorException e) {
      assertEquals("Compressor: unknown not found.", e.getMessage());
    }
  }

  //Additional test case for createCompressorInputStream(InputStream in) with valid input
  @Test
  public void testCreateCompressorInputStreamWithValidInputStream() throws Exception {
    // Arrange
    CompressorStreamFactory factory = new CompressorStreamFactory();
    InputStream input = new FileInputStream(new File("test.txt"));
    
    // Act
    CompressorInputStream result = factory.createCompressorInputStream(input);
    
    // Assert
    assertNotNull(result);
  }
  
  // Additional test case for createCompressorInputStream(String name, InputStream in) with valid input
  @Test
  public void testCreateCompressorInputStreamWithNameAndValidInputStream() throws Exception {
    // Arrange
    CompressorStreamFactory factory = new CompressorStreamFactory();
    String name = "gzip";
    InputStream input = new FileInputStream(new File("test.txt"));
    
    // Act
    CompressorInputStream result = factory.createCompressorInputStream(name, input);
    
    // Assert
    assertNotNull(result);
  }
  
  // Additional test case for createCompressorOutputStream(String name, OutputStream out) with valid input
  @Test
  public void testCreateCompressorOutputStreamWithValidOutputStream() throws Exception {
    // Arrange
    CompressorStreamFactory factory = new CompressorStreamFactory();
    String name = "gzip";
    OutputStream output = new FileOutputStream(new File("test.txt"));
    
    // Act
    CompressorOutputStream result = factory.createCompressorOutputStream(name, output);
    
    // Assert
    assertNotNull(result);
  }
}