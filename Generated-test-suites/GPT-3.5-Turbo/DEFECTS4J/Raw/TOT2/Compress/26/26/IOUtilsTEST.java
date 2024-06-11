import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.apache.commons.compress.utils.IOUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

@RunWith(JUnit4.class)
public class IOUtilsTest {
  
  @Test
  public void testCopy() throws IOException {
    byte[] data = {1, 2, 3, 4, 5};
    InputStream inputStream = new ByteArrayInputStream(data);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    
    long bytesCopied = IOUtils.copy(inputStream, outputStream);
    assertEquals(5, bytesCopied);
    assertArrayEquals(data, outputStream.toByteArray());
  }
  
  @Test
  public void testSkip() throws IOException {
    byte[] data = {1, 2, 3, 4, 5};
    InputStream inputStream = new ByteArrayInputStream(data);
    
    long bytesSkipped = IOUtils.skip(inputStream, 3);
    assertEquals(3, bytesSkipped);
  }
  
  @Test
  public void testReadFully() throws IOException {
    byte[] data = {1, 2, 3, 4, 5};
    InputStream inputStream = new ByteArrayInputStream(data);
    byte[] buffer = new byte[5];
    
    int bytesRead = IOUtils.readFully(inputStream, buffer);
    assertEquals(5, bytesRead);
    assertArrayEquals(data, buffer);
  }
  
  @Test
  public void testToByteArray() throws IOException {
    byte[] data = {1, 2, 3, 4, 5};
    InputStream inputStream = new ByteArrayInputStream(data);
    
    byte[] byteArray = IOUtils.toByteArray(inputStream);
    assertArrayEquals(data, byteArray);
  }
  
  @Test
  public void testCloseQuietly() {
    CloseableStub closeable = new CloseableStub();
    
    IOUtils.closeQuietly(closeable);
    assertTrue(closeable.isClosed());
  }
  
  // Edge case for copy method
  @Test
  public void testCopyEmptyInputStream() throws IOException {
    InputStream inputStream = new ByteArrayInputStream(new byte[]{});
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    
    long bytesCopied = IOUtils.copy(inputStream, outputStream);
    assertEquals(0, bytesCopied);
  }
  
  // Edge case for readFully method
  @Test
  public void testReadFullyPartialDataRead() throws IOException {
    byte[] data = {1, 2, 3};
    InputStream inputStream = new ByteArrayInputStream(data);
    byte[] buffer = new byte[5];
    
    int bytesRead = IOUtils.readFully(inputStream, buffer);
    assertEquals(3, bytesRead);
    assertArrayEquals(new byte[]{1, 2, 3, 0, 0}, buffer);
  }
  
  // Error scenario for skip method
  @Test
  public void testSkipIOException() {
    InputStream inputStream = new InputStream() {
        @Override
        public int read() throws IOException {
            throw new IOException("Mock IOException");
        }
        
        @Override
        public long skip(long n) throws IOException {
            throw new IOException("Mock IOException");
        }
    };
    
    IOException exception = assertThrows(IOException.class, () -> IOUtils.skip(inputStream, 3));
    assertEquals("Mock IOException", exception.getMessage());
  }
}

class CloseableStub implements Closeable {
  private boolean closed = false;
  
  @Override
  public void close() throws IOException {
    closed = true;
  }
  
  public boolean isClosed() {
    return closed;
  }
}