import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.Checksum;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ChecksumCalculatingInputStreamTest {
  
  @Mock
  Checksum checksum;
  
  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }
  
  @Test
  public void testRead() throws IOException {
    byte[] data = new byte[] {1,2,3,4,5};
    ByteArrayInputStream input = new ByteArrayInputStream(data);
    ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, input);
    
    int result1 = stream.read();
    assertEquals(1, result1);
    assertEquals(1, getValueFromChecksum());
    
    int result2 = stream.read();
    assertEquals(2, result2);
    assertEquals(3, getValueFromChecksum());
    
    int result3 = stream.read();
    assertEquals(3, result3);
    assertEquals(6, getValueFromChecksum());
    
    int result4 = stream.read();
    assertEquals(4, result4);
    assertEquals(10, getValueFromChecksum());
    
    int result5 = stream.read();
    assertEquals(5, result5);
    assertEquals(15, getValueFromChecksum());
    
    int result6 = stream.read();
    assertEquals(-1, result6);
    assertEquals(15, getValueFromChecksum());
    
    stream.close();
  }
  
  @Test
  public void testReadArray() throws IOException {
    byte[] data = new byte[] {1,2,3,4,5};
    ByteArrayInputStream input = new ByteArrayInputStream(data);
    ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, input);
    byte[] buffer = new byte[3];
    
    int result = stream.read(buffer);
    assertArrayEquals(new byte[] {1,2,3}, buffer);
    assertEquals(6, getValueFromChecksum());
    assertEquals(3, result);
    
    stream.close();
  }
  
  @Test
  public void testReadArrayWithOffsetAndLength() throws IOException {
    byte[] data = new byte[] {1,2,3,4,5};
    ByteArrayInputStream input = new ByteArrayInputStream(data);
    ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, input);
    byte[] buffer = new byte[5];
    
    int result = stream.read(buffer, 1, 3);
    assertArrayEquals(new byte[] {0,1,2,3,0}, buffer);
    assertEquals(6, getValueFromChecksum());
    assertEquals(3, result);
    
    int result2 = stream.read(buffer, 2, 4);
    assertArrayEquals(new byte[] {0,1,3,4,5}, buffer);
    assertEquals(15, getValueFromChecksum());
    assertEquals(2, result2);
    
    stream.close();
  }
  
  @Test
  public void testSkip() throws IOException {
    byte[] data = new byte[] {1,2,3,4,5};
    ByteArrayInputStream input = new ByteArrayInputStream(data);
    ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, input);
    
    long result1 = stream.skip(2);
    assertEquals(2, result1);
    assertEquals(3, getValueFromChecksum());
    
    long result2 = stream.skip(3);
    assertEquals(0, result2);
    assertEquals(3, getValueFromChecksum());
    
    stream.close();
  }
  
  @Test
  public void testGetValue() {
    ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, null);
    assertEquals(0, stream.getValue());
    stream.close();
  }
  
  private long getValueFromChecksum() {
    return checksum.getValue();
  }
}