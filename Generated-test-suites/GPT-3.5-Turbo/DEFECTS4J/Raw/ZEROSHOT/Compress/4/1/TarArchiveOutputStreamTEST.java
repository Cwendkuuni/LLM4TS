import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TarArchiveOutputStreamTest {
  
  private TarArchiveOutputStream tarOutputStream;

  @Before
  public void setUp() {
    tarOutputStream = new TarArchiveOutputStream(new OutputStream());
  }

  @Test
  public void testSetLongFileMode() {
    tarOutputStream.setLongFileMode(0);
    assertEquals(0, tarOutputStream.getLongFileMode());
  }

  @Test
  public void testFinish() {
    try {
      tarOutputStream.finish();
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testClose() {
    try {
      tarOutputStream.close();
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testGetRecordSize() {
    assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarOutputStream.getRecordSize());
  }

  @Test
  public void testPutArchiveEntry() {
    TarArchiveEntry entry = new TarArchiveEntry("Test");
    try {
      tarOutputStream.putArchiveEntry(entry);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testCloseArchiveEntry() {
    try {
      tarOutputStream.closeArchiveEntry();
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testWriteByte() {
    try {
      tarOutputStream.write(0);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testWriteByteArray() {
    byte[] bytes = new byte[]{0, 1, 2};
    try {
      tarOutputStream.write(bytes);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testWriteByteArrayWithOffsetAndLength() {
    byte[] bytes = new byte[]{0, 1, 2};
    try {
      tarOutputStream.write(bytes, 0, 2);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testFlush() {
    try {
      tarOutputStream.flush();
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testCreateArchiveEntry() {
    File inputFile = new File("Test");
    String entryName = "TestEntry";
    try {
      ArchiveEntry entry = tarOutputStream.createArchiveEntry(inputFile, entryName);
      assertNotNull(entry);
      assertTrue(entry instanceof TarArchiveEntry);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
}