import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {
  
  private TarArchiveOutputStream tarArchiveOutputStream;
  
  @Before
  public void setup() {
    tarArchiveOutputStream = new TarArchiveOutputStream(null, 512, 512);
  }
  
  @Test
  public void testGetRecordSize() {
    assertEquals(512, tarArchiveOutputStream.getRecordSize());
  }
  
  @Test
  public void testSetLongFileMode() {
    tarArchiveOutputStream.setLongFileMode(1);
    assertEquals(1, tarArchiveOutputStream.longFileMode);
  }
  
  @Test
  public void testFinish() {
    try {
      tarArchiveOutputStream.finish();
      assertFalse(tarArchiveOutputStream.haveUnclosedEntry);
    } catch (Exception e) {
      fail("Unexpected exception in testFinish: " + e.getMessage());
    }
  }
  
  @Test(expected = IOException.class)
  public void testFinishWithUnclosedEntry() throws IOException {
    tarArchiveOutputStream.haveUnclosedEntry = true;
    tarArchiveOutputStream.finish();
  }
  
  @Test
  public void testClose() {
    try {
      tarArchiveOutputStream.close();
      assertTrue(tarArchiveOutputStream.closed);
    } catch (Exception e) {
      fail("Unexpected exception in testClose: " + e.getMessage());
    }
  }
  
  @Test
  public void testCloseWhenAlreadyClosed() {
    tarArchiveOutputStream.closed = true;
    try {
      tarArchiveOutputStream.close();
      assertTrue(tarArchiveOutputStream.closed);
    } catch (Exception e) {
      fail("Unexpected exception in testCloseWhenAlreadyClosed: " + e.getMessage());
    }
  }
  
  @Test
  public void testPutArchiveEntry() {
    TarArchiveEntry archiveEntry = new TarArchiveEntry("entry1");
    try {
      tarArchiveOutputStream.putArchiveEntry(archiveEntry);
      assertTrue(tarArchiveOutputStream.haveUnclosedEntry);
      assertEquals(0, tarArchiveOutputStream.currBytes);
      assertEquals("entry1", tarArchiveOutputStream.currName);
    } catch (Exception e) {
      fail("Unexpected exception in testPutArchiveEntry: " + e.getMessage());
    }
  }
  
  @Test(expected = RuntimeException.class)
  public void testPutArchiveEntryWithLongFileNameError() {
    TarArchiveEntry archiveEntry = new TarArchiveEntry("entry1");
    archiveEntry.setName("this_is_a_long_file_name_that_exceeds");
    tarArchiveOutputStream.putArchiveEntry(archiveEntry);
  }
  
  @Test
  public void testPutArchiveEntryWithLongFileNameTruncate() {
    TarArchiveEntry archiveEntry = new TarArchiveEntry("entry1");
    archiveEntry.setName("this_is_a_long_file_name_that_exceeds");
    tarArchiveOutputStream.setLongFileMode(1);
    try {
      tarArchiveOutputStream.putArchiveEntry(archiveEntry);
      assertTrue(tarArchiveOutputStream.haveUnclosedEntry);
      assertEquals(0, tarArchiveOutputStream.currBytes);
      assertEquals("this_is_a_long_file_name_that_e", tarArchiveOutputStream.currName);
    } catch (Exception e) {
      fail("Unexpected exception in testPutArchiveEntryWithLongFileNameTruncate: " + e.getMessage());
    }
  }
  
  @Test
  public void testPutArchiveEntryWithLongFileNameGnu() {
    TarArchiveEntry archiveEntry = new TarArchiveEntry("entry1");
    archiveEntry.setName("this_is_a_long_file_name_that_exceeds");
    tarArchiveOutputStream.setLongFileMode(2);
    try {
      tarArchiveOutputStream.putArchiveEntry(archiveEntry);
      assertTrue(tarArchiveOutputStream.haveUnclosedEntry);
      assertEquals(0, tarArchiveOutputStream.currBytes);
      assertNotEquals("entry1", tarArchiveOutputStream.currName);
    } catch (Exception e) {
      fail("Unexpected exception in testPutArchiveEntryWithLongFileNameGnu: " + e.getMessage());
    }
  }
  
  @Test
  public void testCloseArchiveEntry() {
    try {
      tarArchiveOutputStream.assemLen = 1;
      tarArchiveOutputStream.assemBuf = new byte[512];
      tarArchiveOutputStream.currSize = 512;
      tarArchiveOutputStream.currName = "entry1";
      tarArchiveOutputStream.haveUnclosedEntry = true;
      tarArchiveOutputStream.closeArchiveEntry();
      assertFalse(tarArchiveOutputStream.haveUnclosedEntry);
      assertEquals(0, tarArchiveOutputStream.assemLen);
    } catch (Exception e) {
      fail("Unexpected exception in testCloseArchiveEntry: " + e.getMessage());
    }
  }
  
  @Test(expected = IOException.class)
  public void testCloseArchiveEntryWithoutCompleteWrite() throws IOException {
    tarArchiveOutputStream.currBytes = 0;
    tarArchiveOutputStream.currSize = 512;
    tarArchiveOutputStream.currName = "entry1";
    tarArchiveOutputStream.haveUnclosedEntry = true;
    tarArchiveOutputStream.closeArchiveEntry();
  }
  
  @Test
  public void testWrite() {
    byte[] wBuf = new byte[512];
    try {
      tarArchiveOutputStream.currBytes = 0;
      tarArchiveOutputStream.currSize = 512;
      tarArchiveOutputStream.currName = "entry1";
      tarArchiveOutputStream.write(wBuf, 0, 512);
      assertEquals(512, tarArchiveOutputStream.currBytes);
      assertEquals(0, tarArchiveOutputStream.assemLen);
    } catch (Exception e) {
      fail("Unexpected exception in testWrite: " + e.getMessage());
    }
  }
  
  @Test(expected = IOException.class)
  public void testWriteWithExceedEntrySize() throws IOException {
    byte[] wBuf = new byte[512];
    tarArchiveOutputStream.currBytes = 0;
    tarArchiveOutputStream.currSize = 512;
    tarArchiveOutputStream.currName = "entry1";
    tarArchiveOutputStream.write(wBuf, 0, 600);
  }
  
  @Test
  public void testWriteWithIncompleteRecord() {
    byte[] wBuf = new byte[256];
    try {
      tarArchiveOutputStream.currBytes = 0;
      tarArchiveOutputStream.currSize = 512;
      tarArchiveOutputStream.currName = "entry1";
      tarArchiveOutputStream.write(wBuf, 0, 256);
      assertEquals(512, tarArchiveOutputStream.currBytes);
      assertEquals(256, tarArchiveOutputStream.assemLen);
    } catch (Exception e) {
      fail("Unexpected exception in testWriteWithIncompleteRecord: " + e.getMessage());
    }
  }
  
  @Test
  public void testWriteEOFRecord() {
    try {
      tarArchiveOutputStream.writeEOFRecord();
    } catch (Exception e) {
      fail("Unexpected exception in testWriteEOFRecord: " + e.getMessage());
    }
  }
  
  @Test
  public void testCreateArchiveEntry() {
    File inputFile = new File("input.txt");
    String entryName = "entry1";
    try {
      ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(inputFile, entryName);
      assertEquals(TarArchiveEntry.class, archiveEntry.getClass());
      assertEquals(inputFile, ((TarArchiveEntry)archiveEntry).getFile());
      assertEquals(entryName, ((TarArchiveEntry)archiveEntry).getName());
    } catch (Exception e) {
      fail("Unexpected exception in testCreateArchiveEntry: " + e.getMessage());
    }
  }
}