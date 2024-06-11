import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {

  @Test
  public void testConstructor() {
    OutputStream os = new FileOutputStream("test.tar");
    int blockSize = 512;
    int recordSize = 1024;
    TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os, blockSize, recordSize);

    Assert.assertNotNull(tarOs);
    Assert.assertEquals(blockSize, tarOs.getRecordSize());
  }

  @Test
  public void testSetLongFileMode() {
    OutputStream os = new FileOutputStream("test.tar");
    int blockSize = 512;
    int recordSize = 1024;
    TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os, blockSize, recordSize);

    tarOs.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
    Assert.assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOs.getLongFileMode());

    tarOs.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    Assert.assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOs.getLongFileMode());

    tarOs.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
    Assert.assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOs.getLongFileMode());
  }

  @Test
  public void testFinish() throws IOException {
    OutputStream os = new FileOutputStream("test.tar");
    int blockSize = 512;
    int recordSize = 1024;
    TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os, blockSize, recordSize);

    tarOs.finish();

    Assert.assertTrue(tarOs.isFinished());
  }

  @Test
  public void testClose() throws IOException {
    OutputStream os = new FileOutputStream("test.tar");
    int blockSize = 512;
    int recordSize = 1024;
    TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os, blockSize, recordSize);

    tarOs.close();

    Assert.assertTrue(tarOs.isClosed());
  }

  @Test
  public void testPutArchiveEntry() throws IOException {
    OutputStream os = new FileOutputStream("test.tar");
    int blockSize = 512;
    int recordSize = 1024;
    TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os, blockSize, recordSize);

    File file = new File("test.txt");
    TarArchiveEntry entry = new TarArchiveEntry(file, file.getName());

    tarOs.putArchiveEntry(entry);

    Assert.assertTrue(tarOs.hasUnclosedEntry());
  }

  @Test(expected = IOException.class)
  public void testCloseArchiveEntryWithoutPutArchiveEntry() throws IOException {
    OutputStream os = new FileOutputStream("test.tar");
    int blockSize = 512;
    int recordSize = 1024;
    TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os, blockSize, recordSize);

    tarOs.closeArchiveEntry();
  }

  @Test
  public void testCloseArchiveEntry() throws IOException {
    OutputStream os = new FileOutputStream("test.tar");
    int blockSize = 512;
    int recordSize = 1024;
    TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os, blockSize, recordSize);

    File file = new File("test.txt");
    TarArchiveEntry entry = new TarArchiveEntry(file, file.getName());

    tarOs.putArchiveEntry(entry);
    tarOs.closeArchiveEntry();

    Assert.assertFalse(tarOs.hasUnclosedEntry());
  }

  @Test(expected = IOException.class)
  public void testWriteBytesExceedingSizeInHeader() throws IOException {
    OutputStream os = new FileOutputStream("test.tar");
    int blockSize = 512;
    int recordSize = 1024;
    TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os, blockSize, recordSize);

    File file = new File("test.txt");
    TarArchiveEntry entry = new TarArchiveEntry(file, file.getName(), 100);
    tarOs.putArchiveEntry(entry);

    byte[] buffer = new byte[101];
    tarOs.write(buffer, 0, buffer.length);

    tarOs.closeArchiveEntry();
  }

  @Test
  public void testWriteBytesWithinSizeInHeader() throws IOException {
    OutputStream os = new FileOutputStream("test.tar");
    int blockSize = 512;
    int recordSize = 1024;
    TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os, blockSize, recordSize);

    File file = new File("test.txt");
    TarArchiveEntry entry = new TarArchiveEntry(file, file.getName(), 100);
    tarOs.putArchiveEntry(entry);

    byte[] buffer = new byte[50];
    tarOs.write(buffer, 0, buffer.length);

    tarOs.closeArchiveEntry();

    Assert.assertFalse(tarOs.hasUnclosedEntry());
  }

  @Test
  public void testWriteBytesWithMultipleRecords() throws IOException {
    OutputStream os = new FileOutputStream("test.tar");
    int blockSize = 512;
    int recordSize = 1024;
    TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os, blockSize, recordSize);

    File file = new File("test.txt");
    TarArchiveEntry entry = new TarArchiveEntry(file, file.getName(), 1500);
    tarOs.putArchiveEntry(entry);

    byte[] buffer = new byte[300];
    for (int i = 0; i < 5; i++) {
      tarOs.write(buffer, 0, buffer.length);
    }

    tarOs.closeArchiveEntry();

    Assert.assertFalse(tarOs.hasUnclosedEntry());
  }

  @Test
  public void testWriteBytesWithAssembleBuffer() throws IOException {
    OutputStream os = new FileOutputStream("test.tar");
    int blockSize = 512;
    int recordSize = 1024;
    TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os, blockSize, recordSize);

    File file = new File("test.txt");
    TarArchiveEntry entry = new TarArchiveEntry(file, file.getName(), 1500);
    tarOs.putArchiveEntry(entry);

    byte[] buffer = new byte[300];
    for (int i = 0; i < 4; i++) {
      tarOs.write(buffer, 0, buffer.length);
    }

    tarOs.write(buffer, 0, 150);

    tarOs.closeArchiveEntry();

    Assert.assertFalse(tarOs.hasUnclosedEntry());
  }
}