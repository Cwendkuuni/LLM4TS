import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TarArchiveOutputStreamTest {
  
  private TarArchiveOutputStream tarOutputStream;
  
  @Before
  public void setUp() {
    tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
  }
  
  @After
  public void tearDown() throws IOException {
    tarOutputStream.close();
  }
  
  @Test
  public void testPutArchiveEntryWithValidEntry() throws IOException {
    TarArchiveEntry entry = new TarArchiveEntry("file.txt");
    tarOutputStream.putArchiveEntry(entry);
    
    assertEquals("TarArchiveOutputStream should set the current name to the entry's name", "file.txt", tarOutputStream.currName);
  }
  
  @Test(expected = RuntimeException.class)
  public void testPutArchiveEntryWithLongFilenameAndModeError() throws IOException {
    TarArchiveEntry entry = new TarArchiveEntry("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz");
    tarOutputStream.putArchiveEntry(entry);
  }
  
  @Test
  public void testPutArchiveEntryWithLongFilenameAndModeTruncate() throws IOException {
    tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    TarArchiveEntry entry = new TarArchiveEntry("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz");
    tarOutputStream.putArchiveEntry(entry);
    
    assertEquals("TarArchiveOutputStream should truncate the long file name", "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdef", tarOutputStream.currName);
  }
  
  @Test
  public void testPutArchiveEntryWithLongFilenameAndModeGnu() throws IOException {
    tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
    TarArchiveEntry entry = new TarArchiveEntry("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz");
    tarOutputStream.putArchiveEntry(entry);
    
    assertEquals("TarArchiveOutputStream should set the current name to the entry's name", "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz", tarOutputStream.currName);
  }
  
  @Test
  public void testCloseArchiveEntryWhenAssemLenIsZero() throws IOException {
    TarArchiveEntry entry = new TarArchiveEntry("file.txt");
    tarOutputStream.putArchiveEntry(entry);
    tarOutputStream.closeArchiveEntry();
  }
  
  @Test(expected = IOException.class)
  public void testCloseArchiveEntryWhenAssemLenIsNotZero() throws IOException {
    TarArchiveEntry entry = new TarArchiveEntry("file.txt");
    tarOutputStream.putArchiveEntry(entry);
    tarOutputStream.assemLen = 10;
    tarOutputStream.closeArchiveEntry();
  }
  
  @Test(expected = IOException.class)
  public void testCloseArchiveEntryWhenCurrBytesIsLessThanCurrSize() throws IOException {
    TarArchiveEntry entry = new TarArchiveEntry("file.txt");
    entry.setSize(10);
    tarOutputStream.putArchiveEntry(entry);
    tarOutputStream.write(new byte[5]);
    tarOutputStream.closeArchiveEntry();
  }
  
  @Test
  public void testWriteWhenAssemLenIsZero() throws IOException {
    TarArchiveEntry entry = new TarArchiveEntry("file.txt");
    entry.setSize(10);
    tarOutputStream.putArchiveEntry(entry);
    tarOutputStream.write(new byte[5]);
    
    assertEquals("TarArchiveOutputStream should update the current bytes count correctly", 5, tarOutputStream.currBytes);
  }
  
  @Test
  public void testWriteWhenAssemLenIsNotZeroAndNumToWriteIsLessThanRecordBufLength() throws IOException {
    TarArchiveEntry entry = new TarArchiveEntry("file.txt");
    entry.setSize(10);
    tarOutputStream.putArchiveEntry(entry);
    tarOutputStream.assemLen = 10;
    tarOutputStream.write(new byte[5]);
    
    assertEquals("TarArchiveOutputStream should update the current bytes count correctly", 15, tarOutputStream.currBytes);
  }
  
  @Test
  public void testWriteWhenAssemLenIsNotZeroAndNumToWriteIsGreaterThanRecordBufLength() throws IOException {
    TarArchiveEntry entry = new TarArchiveEntry("file.txt");
    entry.setSize(10);
    tarOutputStream.putArchiveEntry(entry);
    tarOutputStream.assemLen = 10;
    tarOutputStream.write(new byte[10]);
    
    assertEquals("TarArchiveOutputStream should update the current bytes count correctly", 20, tarOutputStream.currBytes);
  }
  
  @Test
  public void testWriteEOFRecord() throws IOException {
    tarOutputStream.writeEOFRecord();
    byte[] recordBuf = new byte[TarArchiveOutputStream.TarBuffer.DEFAULT_RCDSIZE];
    ByteArrayInputStream in = new ByteArrayInputStream(tarOutputStream.getArchiveStream().toByteArray());
    in.read(recordBuf, 0, recordBuf.length);
    for (byte b : recordBuf) {
      assertEquals("TarArchiveOutputStream should write an EOF record of all zeros", 0, b);
    }
  }
  
  @Test
  public void testFinish() throws IOException {
    tarOutputStream.finish();
    byte[] recordBuf = new byte[TarArchiveOutputStream.TarBuffer.DEFAULT_RCDSIZE];
    ByteArrayInputStream in = new ByteArrayInputStream(tarOutputStream.getArchiveStream().toByteArray());
    in.read(recordBuf, 0, recordBuf.length);
    for (byte b : recordBuf) {
      assertEquals("TarArchiveOutputStream should write an EOF record of all zeros", 0, b);
    }
  }
  
  @Test
  public void testCloseWhenOutputStreamNotClosed() throws IOException {
    tarOutputStream.close();
    assertTrue("TarArchiveOutputStream should close the underlying output stream", tarOutputStream.isClosed());
  }
  
  @Test
  public void testGetRecordSize() {
    assertEquals("TarArchiveOutputStream should return the correct record size", TarArchiveOutputStream.TarBuffer.DEFAULT_RCDSIZE, tarOutputStream.getRecordSize());
  }
  
  @Test
  public void testCreateArchiveEntry() throws IOException {
    File inputFile = new File("file.txt");
    ArchiveEntry entry = tarOutputStream.createArchiveEntry(inputFile, "file.txt");
    
    assertEquals("TarArchiveOutputStream should create a TarArchiveEntry", TarArchiveEntry.class, entry.getClass());
  }
}