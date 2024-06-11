import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TarArchiveOutputStreamTest {
  
  private TarArchiveOutputStream tarArchiveOutputStream;
  private ByteArrayOutputStream outputStream;
  
  @Before
  public void setUp() {
    outputStream = new ByteArrayOutputStream();
    tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
  }
  
  @After
  public void tearDown() throws IOException {
    tarArchiveOutputStream.close();
  }
  
  @Test
  public void testConstructor() {
    assertEquals(outputStream, tarArchiveOutputStream.out);
    assertEquals(0, tarArchiveOutputStream.currSize);
    assertEquals(null, tarArchiveOutputStream.currName);
    assertEquals(0, tarArchiveOutputStream.currBytes);
    assertEquals(new byte[TarBuffer.DEFAULT_RCDSIZE], tarArchiveOutputStream.recordBuf);
    assertEquals(0, tarArchiveOutputStream.assemLen);
    assertEquals(new byte[TarBuffer.DEFAULT_RCDSIZE], tarArchiveOutputStream.assemBuf);
    assertTrue(tarArchiveOutputStream.buffer instanceof TarBuffer);
    assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarArchiveOutputStream.longFileMode);
    assertFalse(tarArchiveOutputStream.closed);
    assertFalse(tarArchiveOutputStream.haveUnclosedEntry);
  }
  
  @Test
  public void testSetLongFileMode() {
    tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
    assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarArchiveOutputStream.longFileMode);
  }
  
  @Test
  public void testFinish() throws IOException {
    tarArchiveOutputStream.finish();
    tarArchiveOutputStream.finish();
    assertEquals(1024, outputStream.toByteArray().length);
  }
  
  @Test(expected = IOException.class)
  public void testFinishWithUnclosedEntry() throws IOException {
    tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("Test Entry"));
    tarArchiveOutputStream.finish();
  }
  
  @Test
  public void testClose() throws IOException {
    tarArchiveOutputStream.close();
    assertTrue(tarArchiveOutputStream.closed);
  }
  
  @Test(expected = IOException.class)
  public void testStartClose() throws IOException {
    tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("Test Entry"));
    tarArchiveOutputStream.close();
  }
  
  @Test
  public void testGetRecordSize() {
    assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarArchiveOutputStream.getRecordSize());
  }
  
  @Test
  public void testPutArchiveEntry() throws IOException {
    ArchiveEntry archiveEntry = new TarArchiveEntry("Test Entry");
    tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    assertEquals(archiveEntry, tarArchiveOutputStream.buffer.currentEntry);
    assertTrue(tarArchiveOutputStream.haveUnclosedEntry);
  }
  
  @Test(expected = ClassCastException.class)
  public void testPutArchiveEntryWithWrongType() throws IOException {
    ArchiveEntry archiveEntry = new ArchiveEntry() {
      @Override
      public long getSize() {
        return 0;
      }
    };
    tarArchiveOutputStream.putArchiveEntry(archiveEntry);
  }
  
  @Test
  public void testCloseArchiveEntry() throws IOException {
    tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("Test Entry"));
    tarArchiveOutputStream.closeArchiveEntry();
    assertEquals(0, tarArchiveOutputStream.assemLen);
    assertFalse(tarArchiveOutputStream.haveUnclosedEntry);
  }
  
  @Test(expected = IOException.class)
  public void testCloseArchiveEntryWithIncompleteData() throws IOException {
    tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("Test Entry", 100));
    tarArchiveOutputStream.write(new byte[50]);
    tarArchiveOutputStream.closeArchiveEntry();
  }
  
  @Test
  public void testWrite() throws IOException {
    tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("Test Entry", 50));
    tarArchiveOutputStream.write(new byte[50]);
    tarArchiveOutputStream.closeArchiveEntry();
    
    assertEquals(1024, outputStream.toByteArray().length);
  }
  
  @Test(expected = IOException.class)
  public void testWriteWithExceedingBytes() throws IOException {
    tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("Test Entry", 50));
    tarArchiveOutputStream.write(new byte[100]);
  }
  
  @Test
  public void testWriteEOFRecord() throws IOException {
    tarArchiveOutputStream.writeEOFRecord();
    assertEquals(1024, outputStream.toByteArray().length);
  }
  
  @Test
  public void testCreateArchiveEntry() throws IOException {
    File inputFile = new File("test.txt");
    ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(inputFile, "/test.txt");
    assertEquals(inputFile, ((TarArchiveEntry) archiveEntry).getFile());
    assertEquals("/test.txt", archiveEntry.getName());
  }
}