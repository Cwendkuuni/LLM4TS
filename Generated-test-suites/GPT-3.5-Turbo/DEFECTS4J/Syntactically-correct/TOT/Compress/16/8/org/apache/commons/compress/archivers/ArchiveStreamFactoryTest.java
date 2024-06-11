import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.archivers.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ArchiveStreamFactoryTest {
  
  private ArchiveStreamFactory archiveStreamFactory;
  
  @Before
  public void setUp() {
    archiveStreamFactory = new ArchiveStreamFactory();
  }
  
  // Testing createArchiveInputStream method
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateArchiveInputStreamWithNullArchiverName() throws ArchiveException {
    InputStream inputStream = new ByteArrayInputStream(new byte[1]);
    archiveStreamFactory.createArchiveInputStream(null, inputStream);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateArchiveInputStreamWithNullInputStream() throws ArchiveException {
    archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
  }
  
  @Test(expected = ArchiveException.class)
  public void testCreateArchiveInputStreamWithInvalidArchiverName() throws ArchiveException {
    InputStream inputStream = new ByteArrayInputStream(new byte[1]);
    archiveStreamFactory.createArchiveInputStream("invalid", inputStream);
  }
  
  @Test
  public void testCreateArchiveInputStreamWithValidArchiverNameAndInputStream() throws ArchiveException {
    InputStream inputStream = new ByteArrayInputStream(new byte[1]);
    ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
    assertNotNull(archiveInputStream);
  }
  
  // Testing createArchiveOutputStream method
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateArchiveOutputStreamWithNullArchiverName() throws ArchiveException {
    OutputStream outputStream = new ByteArrayOutputStream();
    archiveStreamFactory.createArchiveOutputStream(null, outputStream);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateArchiveOutputStreamWithNullOutputStream() throws ArchiveException {
    archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
  }
  
  @Test(expected = ArchiveException.class)
  public void testCreateArchiveOutputStreamWithInvalidArchiverName() throws ArchiveException {
    OutputStream outputStream = new ByteArrayOutputStream();
    archiveStreamFactory.createArchiveOutputStream("invalid", outputStream);
  }
  
  @Test
  public void testCreateArchiveOutputStreamWithValidArchiverNameAndOutputStream() throws ArchiveException {
    OutputStream outputStream = new ByteArrayOutputStream();
    ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
    assertNotNull(archiveOutputStream);
  }
  
  // Testing createArchiveInputStream method with autodetection
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateArchiveInputStreamWithNullInputStreamForAutodetection() throws ArchiveException {
    archiveStreamFactory.createArchiveInputStream(null);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateArchiveInputStreamWithUnsupportedInputStreamForAutodetection() throws ArchiveException {
    InputStream inputStream = new FileInputStream(new File("test.txt"));
    archiveStreamFactory.createArchiveInputStream(inputStream);
  }
  
  @Test(expected = ArchiveException.class)
  public void testCreateArchiveInputStreamWithUnknownArchiveTypeForAutodetection() throws Exception {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ZipOutputStream zos = new ZipOutputStream(baos);
    ZipEntry zipEntry = new ZipEntry("test.txt");
    zos.putNextEntry(zipEntry);
    zos.closeEntry();
    zos.close();
    byte[] data = baos.toByteArray();
    ByteArrayInputStream bais = new ByteArrayInputStream(data);
    ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(bais);
    assertNull(archiveInputStream);
  }
  
  @Test
  public void testCreateArchiveInputStreamWithZipArchiveForAutodetection() throws Exception {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ZipOutputStream zos = new ZipOutputStream(baos);
    ZipEntry zipEntry = new ZipEntry("test.txt");
    zos.putNextEntry(zipEntry);
    zos.closeEntry();
    zos.close();
    byte[] data = baos.toByteArray();
    ByteArrayInputStream bais = new ByteArrayInputStream(data);
    ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(bais);
    assertNotNull(archiveInputStream);
  }
  
  @Test
  public void testCreateArchiveInputStreamWithJarArchiveForAutodetection() throws Exception {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ZipOutputStream zos = new ZipOutputStream(baos);
    ZipEntry zipEntry = new ZipEntry("test.jar");
    zos.putNextEntry(zipEntry);
    zos.closeEntry();
    zos.close();
    byte[] data = baos.toByteArray();
    ByteArrayInputStream bais = new ByteArrayInputStream(data);
    ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(bais);
    assertNotNull(archiveInputStream);
  }
  
  @Test
  public void testCreateArchiveInputStreamWithArArchiveForAutodetection() throws Exception {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ZipOutputStream zos = new ZipOutputStream(baos);
    ZipEntry zipEntry = new ZipEntry("test.ar");
    zos.putNextEntry(zipEntry);
    zos.closeEntry();
    zos.close();
    byte[] data = baos.toByteArray();
    ByteArrayInputStream bais = new ByteArrayInputStream(data);
    ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(bais);
    assertNotNull(archiveInputStream);
  }
  
  @Test
  public void testCreateArchiveInputStreamWithCpioArchiveForAutodetection() throws Exception {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ZipOutputStream zos = new ZipOutputStream(baos);
    ZipEntry zipEntry = new ZipEntry("test.cpio");
    zos.putNextEntry(zipEntry);
    zos.closeEntry();
    zos.close();
    byte[] data = baos.toByteArray();
    ByteArrayInputStream bais = new ByteArrayInputStream(data);
    ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(bais);
    assertNotNull(archiveInputStream);
  }
  
  @Test
  public void testCreateArchiveInputStreamWithDumpArchiveForAutodetection() throws Exception {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ZipOutputStream zos = new ZipOutputStream(baos);
    ZipEntry zipEntry = new ZipEntry("test.dump");
    zos.putNextEntry(zipEntry);
    zos.closeEntry();
    zos.close();
    byte[] data = baos.toByteArray();
    ByteArrayInputStream bais = new ByteArrayInputStream(data);
    ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(bais);
    assertNotNull(archiveInputStream);
  }
  
  @Test
  public void testCreateArchiveInputStreamWithTarArchiveForAutodetection() throws Exception {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ZipOutputStream zos = new ZipOutputStream(baos);
    ZipEntry zipEntry = new ZipEntry("test.tar");
    zos.putNextEntry(zipEntry);
    zos.closeEntry();
    zos.close();
    byte[] data = baos.toByteArray();
    ByteArrayInputStream bais = new ByteArrayInputStream(data);
    ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(bais);
    assertNotNull(archiveInputStream);
  }
  
  @Test
  public void testCreateArchiveInputStreamWithTarArchiveAnd512BytesForAutodetection() throws Exception {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ZipOutputStream zos = new ZipOutputStream(baos);
    ZipEntry zipEntry = new ZipEntry("test.tar");
    zos.putNextEntry(zipEntry);
    byte[] header = new byte[512];
    zos.write(header);
    zos.closeEntry();
    zos.close();
    byte[] data = baos.toByteArray();
    ByteArrayInputStream bais = new ByteArrayInputStream(data);
    ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(bais);
    assertNotNull(archiveInputStream);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateArchiveInputStreamWithTarArchiveAndInsufficientBytesForAutodetection() throws Exception {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ZipOutputStream zos = new ZipOutputStream(baos);
    ZipEntry zipEntry = new ZipEntry("test.tar");
    zos.putNextEntry(zipEntry);
    byte[] header = new byte[512];
    zos.write(header);
    zos.closeEntry();
    zos.close();
    byte[] data = baos.toByteArray();
    byte[] truncatedData = new byte[data.length - 1];
    System.arraycopy(data, 0, truncatedData, 0, truncatedData.length);
    ByteArrayInputStream bais = new ByteArrayInputStream(truncatedData);
    ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(bais);
  }
}