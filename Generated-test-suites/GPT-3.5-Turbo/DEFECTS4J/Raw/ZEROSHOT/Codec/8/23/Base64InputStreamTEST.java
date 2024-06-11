import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class Base64InputStreamTest {
  
  private byte[] testData;
  private InputStream inputStream;
  
  @Before
  public void setUp() {
    testData = "Test Data".getBytes();
    inputStream = new ByteArrayInputStream(testData);
  }

  @Test
  public void testRead() throws IOException {
    Base64InputStream base64InputStream = new Base64InputStream(inputStream);
    int data = base64InputStream.read();
    
    assertEquals(testData[0], data);
  }
  
  @Test
  public void testReadWithOffsetAndLength() throws IOException {
    Base64InputStream base64InputStream = new Base64InputStream(inputStream);
    byte[] buffer = new byte[5];
    int bytesRead = base64InputStream.read(buffer, 0, 5);
    
    assertEquals(5, bytesRead);
    assertArrayEquals(testData, buffer);
  }
  
  @Test
  public void testMarkSupported() {
    Base64InputStream base64InputStream = new Base64InputStream(inputStream);
    boolean isMarkSupported = base64InputStream.markSupported();
    
    assertFalse(isMarkSupported);
  }
}