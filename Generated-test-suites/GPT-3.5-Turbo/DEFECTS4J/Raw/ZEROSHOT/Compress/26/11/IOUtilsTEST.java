import static org.junit.Assert.*;
import org.junit.Test;

public class IOUtilsTest {
  
  @Test
  public void testCopyWithDefaultBufferSize() throws Exception {
    // Create input stream
    String inputString = "This is a test";
    byte[] inputData = inputString.getBytes();
    InputStream input = new ByteArrayInputStream(inputData);
    
    // Create output stream
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    
    // Call method under test
    long count = IOUtils.copy(input, output);
    
    // Check result
    assertEquals(inputData.length, count);
    assertArrayEquals(inputData, output.toByteArray());
    
    // Close streams
    input.close();
    output.close();
  }
  
  @Test
  public void testCopyWithCustomBufferSize() throws Exception {
    // Create input stream
    String inputString = "This is a test";
    byte[] inputData = inputString.getBytes();
    InputStream input = new ByteArrayInputStream(inputData);
    
    // Create output stream
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    
    // Call method under test
    int bufferSize = 100;
    long count = IOUtils.copy(input, output, bufferSize);
    
    // Check result
    assertEquals(inputData.length, count);
    assertArrayEquals(inputData, output.toByteArray());
    
    // Close streams
    input.close();
    output.close();
  }
  
  @Test
  public void testSkip() throws Exception {
    // Create input stream
    String inputString = "This is a test";
    byte[] inputData = inputString.getBytes();
    InputStream input = new ByteArrayInputStream(inputData);
    
    // Call method under test
    long numToSkip = 5;
    long skipped = IOUtils.skip(input, numToSkip);
    
    // Check result
    assertEquals(numToSkip, skipped);
    
    // Close stream
    input.close();
  }
  
  @Test
  public void testReadFully() throws Exception {
    // Create input stream
    String inputString = "This is a test";
    byte[] inputData = inputString.getBytes();
    InputStream input = new ByteArrayInputStream(inputData);
    
    // Create buffer
    byte[] b = new byte[10];
    
    // Call method under test
    int bytesRead = IOUtils.readFully(input, b);
    
    // Check result
    assertEquals(10, bytesRead);
    assertArrayEquals("This is a ".getBytes(), b);
    
    // Close stream
    input.close();
  }
  
  @Test
  public void testReadFullyWithOffsetAndLength() throws Exception {
    // Create input stream
    String inputString = "This is a test";
    byte[] inputData = inputString.getBytes();
    InputStream input = new ByteArrayInputStream(inputData);
    
    // Create buffer
    byte[] b = new byte[10];
    
    // Call method under test
    int offset = 3;
    int length = 5;
    int bytesRead = IOUtils.readFully(input, b, offset, length);
    
    // Check result
    assertEquals(length, bytesRead);
    assertArrayEquals("s is ".getBytes(), b);
    
    // Close stream
    input.close();
  }
  
  @Test
  public void testToByteArray() throws Exception {
    // Create input stream
    String inputString = "This is a test";
    byte[] inputData = inputString.getBytes();
    InputStream input = new ByteArrayInputStream(inputData);
    
    // Call method under test
    byte[] result = IOUtils.toByteArray(input);
    
    // Check result
    assertArrayEquals(inputData, result);
    
    // Close stream
    input.close();
  }
  
  @Test
  public void testCloseQuietly() throws Exception {
    // Create closeable object
    Closeable c = new Closeable() {
        @Override
        public void close() throws IOException {
          // Do nothing
        }
    };
    
    // Call method under test
    IOUtils.closeQuietly(c);
    
    // No exception should be thrown
  }
}