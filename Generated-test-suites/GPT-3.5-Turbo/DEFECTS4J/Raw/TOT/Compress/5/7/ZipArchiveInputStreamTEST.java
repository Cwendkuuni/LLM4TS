import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.junit.Test;

public class ZipArchiveInputStreamTest {

  @Test
  public void testGetNextZipEntry() throws IOException {
    // Create a sample zip file
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ZipOutputStream zipOut = new ZipOutputStream(outputStream);
    zipOut.putNextEntry(new ZipEntry("file1.txt"));
    zipOut.closeEntry();
    zipOut.putNextEntry(new ZipEntry("folder/"));
    zipOut.closeEntry();
    zipOut.close();

    // Create a ZipArchiveInputStream for the sample zip file
    ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
    ZipArchiveInputStream zipIn = new ZipArchiveInputStream(inputStream);

    // Test getNextZipEntry method
    ZipArchiveEntry entry1 = zipIn.getNextZipEntry();
    assertEquals("file1.txt", entry1.getName());

    ZipArchiveEntry entry2 = zipIn.getNextZipEntry();
    assertEquals("folder/", entry2.getName());

    ZipArchiveEntry entry3 = zipIn.getNextZipEntry();
    assertNull(entry3);
    
    zipIn.close();
  }

  @Test
  public void testRead() throws IOException {
    // Create a sample zip file
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ZipOutputStream zipOut = new ZipOutputStream(outputStream);
    zipOut.putNextEntry(new ZipEntry("file.txt"));
    zipOut.write("This is a test file.".getBytes());
    zipOut.closeEntry();
    zipOut.close();

    // Create a ZipArchiveInputStream for the sample zip file
    ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
    ZipArchiveInputStream zipIn = new ZipArchiveInputStream(inputStream);

    // Test read method
    byte[] buffer = new byte[10];
    int bytesRead = zipIn.read(buffer, 0, 10);
    assertEquals("This is a", new String(buffer, 0, bytesRead));

    bytesRead = zipIn.read(buffer, 0, 10);
    assertEquals(" test file", new String(buffer, 0, bytesRead));

    bytesRead = zipIn.read(buffer, 0, 10);
    assertEquals(-1, bytesRead);

    zipIn.close();
  }

  @Test
  public void testSkip() throws IOException {
    // Create a sample zip file
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ZipOutputStream zipOut = new ZipOutputStream(outputStream);
    zipOut.putNextEntry(new ZipEntry("file.txt"));
    zipOut.write("This is a test file.".getBytes());
    zipOut.closeEntry();
    zipOut.close();

    // Create a ZipArchiveInputStream for the sample zip file
    ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
    ZipArchiveInputStream zipIn = new ZipArchiveInputStream(inputStream);

    // Test skip method
    long skipped = zipIn.skip(10);
    assertEquals(10, skipped);

    byte[] buffer = new byte[10];
    int bytesRead = zipIn.read(buffer, 0, 10);
    assertEquals("st file.", new String(buffer, 0, bytesRead));

    skipped = zipIn.skip(100);
    assertEquals(6, skipped);

    bytesRead = zipIn.read(buffer, 0, 10);
    assertEquals(-1, bytesRead);

    zipIn.close();
  }

}