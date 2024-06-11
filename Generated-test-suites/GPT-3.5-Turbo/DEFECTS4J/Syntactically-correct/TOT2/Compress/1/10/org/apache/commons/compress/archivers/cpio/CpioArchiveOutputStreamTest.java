import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CpioArchiveOutputStreamTest {

  private CpioArchiveOutputStream cpioArchiveOutputStream;
  private ByteArrayOutputStream outputStream;

  @Before
  public void setUp() {
    outputStream = new ByteArrayOutputStream();
    cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);
  }

  @After
  public void tearDown() {
    try {
      cpioArchiveOutputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testPutNextEntry() throws IOException {
    CpioArchiveEntry entry = new CpioArchiveEntry();
    entry.setName("testfile");
    entry.setFileSize(5);

    cpioArchiveOutputStream.putNextEntry(entry);
    cpioArchiveOutputStream.write("12345".getBytes());
    cpioArchiveOutputStream.closeArchiveEntry();

    assertEquals(0, cpioArchiveOutputStream.crc);
  }

  @Test
  public void testWrite() throws IOException {
    CpioArchiveEntry entry = new CpioArchiveEntry();
    entry.setName("testfile");
    entry.setFileSize(5);

    cpioArchiveOutputStream.putNextEntry(entry);
    cpioArchiveOutputStream.write("12345".getBytes());
    cpioArchiveOutputStream.closeArchiveEntry();

    assertEquals(5, cpioArchiveOutputStream.written);
  }

  @Test
  public void testPutArchiveEntry() throws IOException {
    CpioArchiveEntry entry = new CpioArchiveEntry();
    entry.setName("testfile");
    entry.setFileSize(5);

    cpioArchiveOutputStream.putArchiveEntry(entry);
    cpioArchiveOutputStream.write("12345".getBytes());
    cpioArchiveOutputStream.closeArchiveEntry();

    assertEquals(5, cpioArchiveOutputStream.written);
  }

  @Test(expected = IOException.class)
  public void testInvalidEntrySize() throws IOException {
    CpioArchiveEntry entry = new CpioArchiveEntry();
    entry.setName("testfile");
    entry.setFileSize(5);

    cpioArchiveOutputStream.putNextEntry(entry);
    cpioArchiveOutputStream.write("123456".getBytes());
    cpioArchiveOutputStream.closeArchiveEntry();
  }

}