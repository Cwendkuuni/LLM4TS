import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ChecksumCalculatingInputStreamTest {
  
  @Test
  public void testRead() throws IOException {
    Checksum checksum = new CRC32();
    byte[] data = {1,2,3,4,5};
    ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
    ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, inputStream);
    assertEquals(1, stream.read());
    assertEquals(2, stream.read());
    assertEquals(3, stream.read());
    assertEquals(4, stream.read());
    assertEquals(5, stream.read());
    assertEquals(15, stream.getValue());
  }
  
  @Test
  public void testReadByteArray() throws IOException {
    Checksum checksum = new CRC32();
    byte[] data = {1,2,3,4,5};
    byte[] buffer = new byte[5];
    ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
    ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, inputStream);
    assertEquals(5, stream.read(buffer));
    assertEquals(15, stream.getValue());
  }
  
  @Test
  public void testReadByteArrayWithOffset() throws IOException {
    Checksum checksum = new CRC32();
    byte[] data = {1,2,3,4,5};
    byte[] buffer = new byte[5];
    ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
    ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, inputStream);
    assertEquals(3, stream.read(buffer, 1, 3));
    assertEquals(15, stream.getValue());
  }
  
  @Test
  public void testSkip() throws IOException {
    Checksum checksum = new CRC32();
    byte[] data = {1,2,3,4,5};
    ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
    ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, inputStream);
    assertEquals(1, stream.skip(1));
    assertEquals(10, stream.getValue());
  }
  
  @Test
  public void testGetValue() {
    Checksum checksum = new CRC32();
    byte[] data = {1,2,3,4,5};
    ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
    ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, inputStream);
    assertEquals(0, stream.getValue());
  }
}