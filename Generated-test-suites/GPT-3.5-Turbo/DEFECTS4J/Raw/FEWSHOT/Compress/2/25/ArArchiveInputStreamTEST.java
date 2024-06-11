import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class ArArchiveInputStreamTest {

  private ArArchiveInputStream arArchiveInputStream;
  
  @Before
  public void setUp() throws IOException {
    byte[] bytes = new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x00, 0x00};
    InputStream inputStream = new ByteArrayInputStream(bytes);
    arArchiveInputStream = new ArArchiveInputStream(inputStream);
  }
  
  @After
  public void tearDown() throws IOException {
    arArchiveInputStream.close();
  }
  
  @Test
  public void testGetNextArEntry() throws IOException {
    ArArchiveEntry entry = arArchiveInputStream.getNextArEntry();
    assertEquals(ArArchiveEntry.HEADER, new String(Arrays.copyOf(entry.getHeader(), ArArchiveEntry.HEADER.getBytes().length)));
    assertEquals(0, entry.getLength());
  }

  @Test
  public void testGetNextEntry() throws IOException {
    ArArchiveEntry entry = (ArArchiveEntry)arArchiveInputStream.getNextEntry();
    assertEquals(ArArchiveEntry.HEADER, new String(Arrays.copyOf(entry.getHeader(), ArArchiveEntry.HEADER.getBytes().length)));
    assertEquals(0, entry.getLength());
  }

  @Test
  public void testClose() throws IOException {
    InputStream inputStream = mock(InputStream.class);
    ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
    arArchiveInputStream.close();
    verify(inputStream, times(1)).close();
  }

  @Test
  public void testRead() throws IOException {
    int read = arArchiveInputStream.read();
    assertEquals(33, read);
  }

  @Test
  public void testReadByteArray() throws IOException {
    byte[] buffer = new byte[10];
    int read = arArchiveInputStream.read(buffer);
    assertEquals(10, read);
  }

  @Test
  public void testReadByteArrayWithOffsetAndLength() throws IOException {
    byte[] buffer = new byte[5];
    int read = arArchiveInputStream.read(buffer, 1, 3);
    assertEquals(3, read);
  }

  @Test
  public void testMatches() {
    byte[] signature = new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
    boolean match = ArArchiveInputStream.matches(signature, signature.length);
    assertEquals(true, match);
  }

}