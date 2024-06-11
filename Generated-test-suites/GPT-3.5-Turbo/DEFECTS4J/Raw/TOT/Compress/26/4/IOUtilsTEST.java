import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class IOUtilsTest {
  
  private InputStream inputStream;
  private OutputStream outputStream;
  
  @Before
  public void setUp() {
    // Set up input and output streams for each test case
    inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
    outputStream = new ByteArrayOutputStream();
  }
  
  @After
  public void tearDown() {
    // Close input and output streams after each test case
    try {
      inputStream.close();
    } catch (IOException ignored) {
    }
    try {
      outputStream.close();
    } catch (IOException ignored) {
    }
  }
  
  @Test
  public void testCopyWithDefaultBufferSize() throws IOException {
    long count = IOUtils.copy(inputStream, outputStream);
    
    // Verify that the correct number of bytes are copied
    assertEquals(5, count);
    
    // Verify that the content is copied correctly
    assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, ((ByteArrayOutputStream) outputStream).toByteArray());
  }
  
  @Test
  public void testCopyWithCustomBufferSize() throws IOException {
    long count = IOUtils.copy(inputStream, outputStream, 2);
    
    // Verify that the correct number of bytes are copied
    assertEquals(5, count);
    
    // Verify that the content is copied correctly
    assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, ((ByteArrayOutputStream) outputStream).toByteArray());
  }
  
  @Test
  public void testCopyWithEmptyInputStream() throws IOException {
    // Set up an empty input stream
    inputStream = new ByteArrayInputStream(new byte[]{});
    
    long count = IOUtils.copy(inputStream, outputStream);
    
    // Verify that no bytes are copied
    assertEquals(0, count);
    
    // Verify that the output stream is empty
    assertArrayEquals(new byte[]{}, ((ByteArrayOutputStream) outputStream).toByteArray());
  }
  
  @Test(expected = IOException.class)
  public void testCopyWithIOException() throws IOException {
    // Set up an input stream that throws IOException on read()
    inputStream = new InputStream() {
      @Override
      public int read() throws IOException {
        throw new IOException("Test IOException");
      }
    };
    
    IOUtils.copy(inputStream, outputStream);
    
    // Expect an IOException to be thrown
  }
  
  @Test
  public void testSkipWithPositiveNumToSkip() throws IOException {
    long skipped = IOUtils.skip(inputStream, 2);
    
    // Verify that the correct number of bytes are skipped
    assertEquals(2, skipped);
    
    // Verify that the remaining content is correct after skipping
    assertArrayEquals(new byte[]{3, 4, 5}, ((ByteArrayInputStream) inputStream).readAllBytes());
  }
  
  @Test
  public void testSkipWithZeroNumToSkip() throws IOException {
    long skipped = IOUtils.skip(inputStream, 0);
    
    // Verify that no bytes are skipped
    assertEquals(0, skipped);
    
    // Verify that the content is unchanged
    assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, ((ByteArrayInputStream) inputStream).readAllBytes());
  }
  
  @Test
  public void testSkipWithNegativeNumToSkip() throws IOException {
    long skipped = IOUtils.skip(inputStream, -2);
    
    // Verify that no bytes are skipped
    assertEquals(0, skipped);
    
    // Verify that the content is unchanged
    assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, ((ByteArrayInputStream) inputStream).readAllBytes());
  }
  
  @Test
  public void testSkipWithNumToSkipGreaterThanAvailable() throws IOException {
    long skipped = IOUtils.skip(inputStream, 6);
    
    // Verify that only the available number of bytes are skipped
    assertEquals(5, skipped);
    
    // Verify that the input stream is empty after skipping
    assertEquals(-1, inputStream.read());
  }
  
  @Test(expected = IOException.class)
  public void testSkipWithIOException() throws IOException {
    // Set up an input stream that throws IOException on skip()
    inputStream = new InputStream() {
      @Override
      public long skip(long n) throws IOException {
        throw new IOException("Test IOException");
      }
    };
    
    IOUtils.skip(inputStream, 3);
    
    // Expect an IOException to be thrown
  }
  
  @Test
  public void testReadFullyWithFullArray() throws IOException {
    byte[] buffer = new byte[5];
    int bytesRead = IOUtils.readFully(inputStream, buffer);
    
    // Verify that the correct number of bytes are read
    assertEquals(5, bytesRead);
    
    // Verify that the content is read correctly
    assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, buffer);
  }
  
  @Test
  public void testReadFullyWithPartialArray() throws IOException {
    byte[] buffer = new byte[3];
    int bytesRead = IOUtils.readFully(inputStream, buffer);
    
    // Verify that the correct number of bytes are read
    assertEquals(3, bytesRead);
    
    // Verify that the content is read correctly
    assertArrayEquals(new byte[]{1, 2, 3}, buffer);
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testReadFullyWithInvalidArguments() throws IOException {
    byte[] buffer = new byte[5];
    
    // Invalid offset - should throw IndexOutOfBoundsException
    IOUtils.readFully(inputStream, buffer, -1, 5);
  }
  
  @Test(expected = IOException.class)
  public void testReadFullyWithIOException() throws IOException {
    byte[] buffer = new byte[5];
    
    // Set up an input stream that throws IOException on read()
    inputStream = new InputStream() {
      @Override
      public int read(byte[] b, int off, int len) throws IOException {
        throw new IOException("Test IOException");
      }
    };
    
    IOUtils.readFully(inputStream, buffer);
    
    // Expect an IOException to be thrown
  }
  
  @Test
  public void testToByteArray() throws IOException {
    byte[] expected = new byte[]{1, 2, 3, 4, 5};
    byte[] actual = IOUtils.toByteArray(inputStream);
    
    // Verify that the content is read correctly
    assertArrayEquals(expected, actual);
  }
  
  @Test
  public void testToByteArrayWithEmptyInputStream() throws IOException {
    // Set up an empty input stream
    inputStream = new ByteArrayInputStream(new byte[]{});
    
    byte[] expected = new byte[]{};
    byte[] actual = IOUtils.toByteArray(inputStream);
    
    // Verify that the content is read correctly
    assertArrayEquals(expected, actual);
  }
  
  @Test(expected = IOException.class)
  public void testToByteArrayWithIOException() throws IOException {
    // Set up an input stream that throws IOException on read()
    inputStream = new InputStream() {
      @Override
      public int read() throws IOException {
        throw new IOException("Test IOException");
      }
    };
    
    IOUtils.toByteArray(inputStream);
    
    // Expect an IOException to be thrown
  }
  
  @Test
  public void testCloseQuietlyWithNullCloseable() {
    // Verify that no exception is thrown
    IOUtils.closeQuietly(null);
  }
  
  @Test
  public void testCloseQuietlyWithValidCloseable() throws IOException {
    // Create a valid closeable for testing
    Closeable closeable = new Closeable() {
      @Override
      public void close() throws IOException {
        // Do nothing
      }
    };
    
    // Verify that no exception is thrown
    IOUtils.closeQuietly(closeable);
  }
  
  @Test(expected = IOException.class)
  public void testCloseQuietlyWithIOException() throws IOException {
    // Create a closeable that throws IOException on close()
    Closeable closeable = new Closeable() {
      @Override
      public void close() throws IOException {
        throw new IOException("Test IOException");
      }
    };
    
    IOUtils.closeQuietly(closeable);
    
    // Expect an IOException to be thrown
  }
}