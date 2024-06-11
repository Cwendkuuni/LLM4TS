import org.junit.Test;
import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {
  
  @Test
  public void testConstructor() {
    TarArchiveOutputStream t = new TarArchiveOutputStream(new OutputStream() {
      public void write(int b) throws IOException {}
    });
    assertNotNull(t);
  }
  
  @Test
  public void testSetLongFileMode() {
    TarArchiveOutputStream t = new TarArchiveOutputStream(new OutputStream() {
      public void write(int b) throws IOException {}
    });
    t.setLongFileMode(1);
    assertEquals(1, t.longFileMode);
  }
  
  @Test
  public void testFinish() throws IOException {
    TarArchiveOutputStream t = new TarArchiveOutputStream(new OutputStream() {
      public void write(int b) throws IOException {}
    });
    t.finish();
    // Check if finish method executed without throwing any exception
  }
  
  @Test
  public void testClose() throws IOException {
    TarArchiveOutputStream t = new TarArchiveOutputStream(new OutputStream() {
      public void write(int b) throws IOException {}
    });
    t.close();
    // Check if close method executed without throwing any exception
  }
  
  @Test
  public void testGetRecordSize() {
    TarArchiveOutputStream t = new TarArchiveOutputStream(new OutputStream() {
      public void write(int b) throws IOException {}
    });
    int size = t.getRecordSize();
    assertEquals(size, TarBuffer.DEFAULT_RCDSIZE);
  }
  
  @Test
  public void testPutArchiveEntry() throws IOException {
    TarArchiveOutputStream t = new TarArchiveOutputStream(new OutputStream() {
      public void write(int b) throws IOException {}
    });
    TarArchiveEntry entry = new TarArchiveEntry("test.txt");
    t.putArchiveEntry(entry);
    t.closeArchiveEntry();
  }
  
  @Test
  public void testCloseArchiveEntry() throws IOException {
    TarArchiveOutputStream t = new TarArchiveOutputStream(new OutputStream() {
      public void write(int b) throws IOException {}
    });
    TarArchiveEntry entry = new TarArchiveEntry("test.txt");
    t.putArchiveEntry(entry);
    t.closeArchiveEntry();
  }
  
  @Test
  public void testWrite() throws IOException {
    TarArchiveOutputStream t = new TarArchiveOutputStream(new OutputStream() {
      public void write(int b) throws IOException {}
    });
    byte[] buf = "test".getBytes();
    t.write(buf, 0, buf.length);
  }
  
  @Test
  public void testWriteEOFRecord() throws IOException {
    TarArchiveOutputStream t = new TarArchiveOutputStream(new OutputStream() {
      public void write(int b) throws IOException {}
    });
    t.writeEOFRecord();
    // Check if writeEOFRecord method executed without throwing any exception
  }
  
  @Test
  public void testFlush() throws IOException {
    TarArchiveOutputStream t = new TarArchiveOutputStream(new OutputStream() {
      public void write(int b) throws IOException {}
    });
    t.flush();
    // Check if flush method executed without throwing any exception
  }
  
  @Test
  public void testCreateArchiveEntry() throws IOException {
    TarArchiveOutputStream t = new TarArchiveOutputStream(new OutputStream() {
      public void write(int b) throws IOException {}
    });
    File file = new File("test.txt");
    String entryName = "test.txt";
    TarArchiveEntry entry = (TarArchiveEntry) t.createArchiveEntry(file, entryName);
    assertNotNull(entry);
    assertEquals(file, entry.getFile());
    assertEquals(entryName, entry.getName());
  }
}