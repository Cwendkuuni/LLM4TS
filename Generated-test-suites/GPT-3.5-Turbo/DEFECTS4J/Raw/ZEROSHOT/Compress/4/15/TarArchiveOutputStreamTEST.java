import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {
  
  private TarArchiveOutputStream tarOutput;
  private ByteArrayOutputStream output;
  
  @Before
  public void setUp() {
    output = new ByteArrayOutputStream();
    tarOutput = new TarArchiveOutputStream(output);
  }
  
  @After
  public void tearDown() {
    try {
      tarOutput.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  @Test
  public void testConstructor1() {
    assertNotNull(tarOutput);
  }
  
  @Test
  public void testConstructor2() {
    TarArchiveOutputStream tarOutput2 = new TarArchiveOutputStream(output, 512);
    assertNotNull(tarOutput2);
  }
  
  @Test
  public void testConstructor3() {
    TarArchiveOutputStream tarOutput3 = new TarArchiveOutputStream(output, 512, 1024);
    assertNotNull(tarOutput3);
  }
  
  @Test
  public void testSetLongFileMode() {
    tarOutput.setLongFileMode(1);
    assertEquals(1, tarOutput.getLongFileMode());
  }
  
  @Test
  public void testGetRecordSize() {
    assertEquals(512, tarOutput.getRecordSize());
  }
  
  @Test
  public void testPutArchiveEntry() throws IOException {
    File file = new File("test.txt");
    TarArchiveEntry entry = new TarArchiveEntry(file, "test.txt");
    tarOutput.putArchiveEntry(entry);
    tarOutput.closeArchiveEntry();
    tarOutput.finish();
    assertEquals(4, output.toByteArray().length);
  }
  
  @Test
  public void testCloseArchiveEntry() throws IOException {
    TarArchiveEntry entry = new TarArchiveEntry("test.txt");
    tarOutput.putArchiveEntry(entry);
    tarOutput.write(new byte[]{1, 2, 3});
    tarOutput.closeArchiveEntry();
    assertEquals(2, output.toByteArray().length);
  }
  
  @Test(expected=IOException.class)
  public void testCloseArchiveEntryThrowsIOException() throws IOException {
    tarOutput.closeArchiveEntry();
  }
  
  @Test
  public void testWrite() throws IOException {
    tarOutput.putArchiveEntry(new TarArchiveEntry("test.txt"));
    tarOutput.write(new byte[]{1, 2, 3});
    tarOutput.closeArchiveEntry();
    assertEquals(512, output.toByteArray().length);
  }
  
  @Test(expected=IOException.class)
  public void testWriteThrowsIOException() throws IOException {
    tarOutput.putArchiveEntry(new TarArchiveEntry("test.txt"));
    tarOutput.closeArchiveEntry();
    tarOutput.write(new byte[]{1, 2, 3});
  }
  
  @Test
  public void testWriteEmpty() throws IOException {
    tarOutput.putArchiveEntry(new TarArchiveEntry("test.txt"));
    tarOutput.write(new byte[0]);
    tarOutput.closeArchiveEntry();
    assertEquals(512, output.toByteArray().length);
  }
  
  @Test
  public void testFinish() throws IOException {
    tarOutput.putArchiveEntry(new TarArchiveEntry("test.txt"));
    tarOutput.write(new byte[]{1, 2, 3});
    tarOutput.closeArchiveEntry();
    tarOutput.finish();
    assertEquals(1024, output.toByteArray().length);
  }
  
  @Test(expected=IOException.class)
  public void testFinishThrowsIOException() throws IOException {
    tarOutput.putArchiveEntry(new TarArchiveEntry("test.txt"));
    tarOutput.write(new byte[]{1, 2, 3});
    tarOutput.closeArchiveEntry();
    tarOutput.finish();
    tarOutput.finish();
  }
  
  @Test
  public void testClose() throws IOException {
    tarOutput.putArchiveEntry(new TarArchiveEntry("test.txt"));
    tarOutput.write(new byte[]{1, 2, 3});
    tarOutput.closeArchiveEntry();
    tarOutput.close();
    assertEquals(1024, output.toByteArray().length);
  }
  
  @Test(expected=IOException.class)
  public void testCloseThrowsIOException() throws IOException {
    tarOutput.close();
  }
  
  @Test
  public void testCreateArchiveEntry() throws IOException {
    File file = new File("test.txt");
    ArchiveEntry entry = tarOutput.createArchiveEntry(file, "test.txt");
    assertNotNull(entry);
  }
}