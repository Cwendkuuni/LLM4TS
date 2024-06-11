import org.junit.Test;
import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {
  
  @Test
  public void testSetLongFileMode() {
    TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(null);
    tarOutputStream.setLongFileMode(2);
    assertEquals(2, tarOutputStream.longFileMode);
  }
  
  @Test
  public void testFinish() {
    TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(null);
    assertThrows(IOException.class, () -> tarOutputStream.finish());
  }
  
  @Test
  public void testClose() {
    TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(null);
    assertThrows(IOException.class, () -> tarOutputStream.close());
  }
  
  @Test
  public void testGetRecordSize() {
    TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(null);
    assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarOutputStream.getRecordSize());
  }
  
  @Test
  public void testPutArchiveEntry() {
    TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(null);
    assertThrows(ClassCastException.class, () -> tarOutputStream.putArchiveEntry(null));
  }
  
  @Test
  public void testCloseArchiveEntry() {
    TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(null);
    assertThrows(IOException.class, () -> tarOutputStream.closeArchiveEntry());
  }
  
  @Test
  public void testWrite1() {
    TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(null);
    assertThrows(IOException.class, () -> tarOutputStream.write(null, 0, 0));
  }
  
  @Test
  public void testWrite2() {
    TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(null);
    assertThrows(IOException.class, () -> tarOutputStream.write(null, 0, 0));
  }
  
  @Test
  public void testWriteEOFRecord() {
    TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(null);
    assertThrows(IOException.class, () -> tarOutputStream.writeEOFRecord());
  }
  
  @Test
  public void testCreateArchiveEntry() {
    TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(null);
    assertThrows(IOException.class, () -> tarOutputStream.createArchiveEntry(null, null));
  }
}