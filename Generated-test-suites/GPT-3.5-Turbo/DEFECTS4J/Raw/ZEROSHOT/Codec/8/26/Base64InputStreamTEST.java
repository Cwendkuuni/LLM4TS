import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

  @Test
  public void testConstructor() {
    InputStream mockStream = new InputStream() {
      public int read() {
        return 0;
      }
    };

    Base64InputStream base64InputStream = new Base64InputStream(mockStream);
    assertNotNull(base64InputStream);
    assertFalse(base64InputStream.markSupported());
  }

  @Test
  public void testRead() throws IOException {
    // Create a byte array with base64 encoded data
    byte[] encodedData = "SGVsbG8gV29ybGQ=".getBytes();

    // Decode the data
    byte[] decodedData = Base64.decodeBase64(encodedData);

    // Create an input stream with the decoded data
    InputStream inputStream = new ByteArrayInputStream(decodedData);

    // Create a Base64InputStream
    Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

    // Read the decoded data from the Base64InputStream
    int readData = base64InputStream.read();

    // Check that the read data matches the decoded data
    assertEquals(decodedData[0], readData);
  }

  @Test
  public void testReadWithBuffer() throws IOException {
    // Create a byte array with base64 encoded data
    byte[] encodedData = "SGVsbG8gV29ybGQ=".getBytes();

    // Decode the data
    byte[] decodedData = Base64.decodeBase64(encodedData);

    // Create an input stream with the decoded data
    InputStream inputStream = new ByteArrayInputStream(decodedData);

    // Create a Base64InputStream
    Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

    // Create a buffer to read the data into
    byte[] buffer = new byte[decodedData.length];

    // Read the decoded data from the Base64InputStream into the buffer
    int readBytes = base64InputStream.read(buffer, 0, buffer.length);

    // Check that the number of read bytes matches the decoded data length
    assertEquals(decodedData.length, readBytes);

    // Check that the read data matches the decoded data
    assertArrayEquals(decodedData, buffer);
  }

  @Test
  public void testReadWithInvalidArguments() throws IOException {
    InputStream mockStream = new InputStream() {
      public int read() {
        return 0;
      }
    };

    Base64InputStream base64InputStream = new Base64InputStream(mockStream, true);

    byte[] buffer = new byte[10];

    // Check when b is null
    try {
      base64InputStream.read(null, 0, buffer.length);
      fail("IllegalArgumentException should have been thrown");
    } catch (IllegalArgumentException ex) {
      assertEquals("Byte array must not be null", ex.getMessage());
    }

    // Check when offset is negative
    try {
      base64InputStream.read(buffer, -1, buffer.length);
      fail("IndexOutOfBoundsException should have been thrown");
    } catch (IndexOutOfBoundsException ex) {
      assertEquals("Index out of bounds", ex.getMessage());
    }

    // Check when len is negative
    try {
      base64InputStream.read(buffer, 0, -1);
      fail("IndexOutOfBoundsException should have been thrown");
    } catch (IndexOutOfBoundsException ex) {
      assertEquals("Index out of bounds", ex.getMessage());
    }

    // Check when offset is greater than length of b
    try {
      base64InputStream.read(buffer, buffer.length + 1, buffer.length);
      fail("IndexOutOfBoundsException should have been thrown");
    } catch (IndexOutOfBoundsException ex) {
      assertEquals("Index out of bounds", ex.getMessage());
    }

    // Check when offset plus len is greater than length of b
    try {
      base64InputStream.read(buffer, 0, buffer.length + 1);
      fail("IndexOutOfBoundsException should have been thrown");
    } catch (IndexOutOfBoundsException ex) {
      assertEquals("Index out of bounds", ex.getMessage());
    }
  }
}