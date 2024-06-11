import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DumpArchiveInputStreamTest {

  private DumpArchiveInputStream stream;

  @Before
  public void setUp() throws Exception {
    stream = new DumpArchiveInputStream(null, null);
  }

  @Test
  public void testGetCount() {
    assertEquals(0, stream.getCount());
  }

  @Test
  public void testGetBytesRead() {
    assertEquals(0, stream.getBytesRead());
  }

  @Test
  public void testGetSummary() {
    assertNull(stream.getSummary());
  }

  @Test
  public void testGetNextDumpEntry() {
    assertNull(stream.getNextDumpEntry());
  }

  @Test
  public void testGetNextEntry() {
    assertNull(stream.getNextEntry());
  }

  @Test
  public void testRead() throws Exception {
    byte[] buf = new byte[10];
    assertEquals(-1, stream.read(buf, 0, 10));
  }

  @Test
  public void testClose() throws Exception {
    stream.close();
    assertTrue(stream.isClosed());
  }

}