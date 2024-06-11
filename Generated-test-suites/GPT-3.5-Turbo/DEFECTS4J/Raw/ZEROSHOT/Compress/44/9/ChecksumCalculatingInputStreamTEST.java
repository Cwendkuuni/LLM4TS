import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.InputStream;
import java.util.zip.Checksum;
import java.io.IOException;

public class ChecksumCalculatingInputStreamTest {
  private ChecksumCalculatingInputStream checksumCalculatingInputStream;
  private Checksum checksum;
  private InputStream inputStream;

  @Before
  public void setUp() {
    checksum = new Checksum();
    inputStream = new InputStream();
    checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
  }

  @Test
  public void testRead() {
    try {
      int result = checksumCalculatingInputStream.read();
      assertEquals(inputStream.read(), result);
      assertEquals(checksum.getValue(), result);
    } catch(IOException e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testReadByteArray() {
    byte[] byteArray = new byte[10];
    try {
      int result = checksumCalculatingInputStream.read(byteArray);
      assertEquals(inputStream.read(byteArray), result);
      assertEquals(checksum.getValue(), result);
    } catch(IOException e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testReadByteArrayWithOffset() {
    byte[] byteArray = new byte[10];
    int offset = 1;
    try {
      int result = checksumCalculatingInputStream.read(byteArray, offset, byteArray.length - offset);
      assertEquals(inputStream.read(byteArray, offset, byteArray.length - offset), result);
      assertEquals(checksum.getValue(), result);
    } catch(IOException e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testSkip() {
    long n = 10;
    try {
      long result = checksumCalculatingInputStream.skip(n);
      long expected = inputStream.skip(n) > 0 ? 1 : 0;
      assertEquals(expected, result);
      assertEquals(checksum.getValue(), result);
    } catch(IOException e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testGetValue() {
    long expected = checksum.getValue();
    long result = checksumCalculatingInputStream.getValue();
    assertEquals(expected, result);
  }
}