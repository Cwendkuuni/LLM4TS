import org.junit.Test;
import org.junit.Assert;

public class DumpArchiveInputStreamTest {

  @Test
  public void testGetCount() {
    InputStream is = new InputStream();
    DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(is);
    int count = dumpArchiveInputStream.getCount();
    Assert.assertEquals(0, count);
  }

  @Test
  public void testGetBytesRead() {
    InputStream is = new InputStream();
    DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(is);
    long bytesRead = dumpArchiveInputStream.getBytesRead();
    Assert.assertEquals(0, bytesRead);
  }

  @Test
  public void testGetSummary() {
    InputStream is = new InputStream();
    DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(is);
    DumpArchiveSummary summary = dumpArchiveInputStream.getSummary();
    Assert.assertNotNull(summary);
  }

  @Test
  public void testGetNextDumpEntry() {
    InputStream is = new InputStream();
    DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(is);
    DumpArchiveEntry entry = dumpArchiveInputStream.getNextDumpEntry();
    Assert.assertNull(entry);
  }

  @Test
  public void testGetNextEntry() {
    InputStream is = new InputStream();
    DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(is);
    DumpArchiveEntry entry = dumpArchiveInputStream.getNextEntry();
    Assert.assertNull(entry);
  }

  @Test
  public void testRead() {
    InputStream is = new InputStream();
    DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(is);
    byte[] buf = new byte[10];
    int off = 0;
    int len = 10;
    int bytesRead = dumpArchiveInputStream.read(buf, off, len);
    Assert.assertEquals(-1, bytesRead);
  }

  @Test
  public void testClose() {
    InputStream is = new InputStream();
    DumpArchiveInputStream dumpArchiveInputStream = new DumpArchiveInputStream(is);
    try {
      dumpArchiveInputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}