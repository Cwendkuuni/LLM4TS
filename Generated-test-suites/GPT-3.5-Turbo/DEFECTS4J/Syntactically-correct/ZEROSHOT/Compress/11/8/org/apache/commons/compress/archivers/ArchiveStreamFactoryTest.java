import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.archivers.*;

public class ArchiveStreamFactoryTest {

  private ArchiveStreamFactory archiveStreamFactory;

  @Before
  public void setUp() {
    archiveStreamFactory = new ArchiveStreamFactory();
  }

  @Test
  public void testCreateArchiveInputStream_AR() throws ArchiveException {
    String archiverName = "ar";
    InputStream in = new ByteArrayInputStream(new byte[0]);
    ArchiveInputStream result = archiveStreamFactory.createArchiveInputStream(archiverName, in);
    assertNotNull(result);
    assertTrue(result instanceof ArArchiveInputStream);
  }

  @Test
  public void testCreateArchiveInputStream_ZIP() throws ArchiveException {
    String archiverName = "zip";
    InputStream in = new ByteArrayInputStream(new byte[0]);
    ArchiveInputStream result = archiveStreamFactory.createArchiveInputStream(archiverName, in);
    assertNotNull(result);
    assertTrue(result instanceof ZipArchiveInputStream);
  }

  @Test
  public void testCreateArchiveInputStream_TAR() throws ArchiveException {
    String archiverName = "tar";
    InputStream in = new ByteArrayInputStream(new byte[0]);
    ArchiveInputStream result = archiveStreamFactory.createArchiveInputStream(archiverName, in);
    assertNotNull(result);
    assertTrue(result instanceof TarArchiveInputStream);
  }

  @Test
  public void testCreateArchiveInputStream_JAR() throws ArchiveException {
    String archiverName = "jar";
    InputStream in = new ByteArrayInputStream(new byte[0]);
    ArchiveInputStream result = archiveStreamFactory.createArchiveInputStream(archiverName, in);
    assertNotNull(result);
    assertTrue(result instanceof JarArchiveInputStream);
  }

  @Test
  public void testCreateArchiveInputStream_CPIO() throws ArchiveException {
    String archiverName = "cpio";
    InputStream in = new ByteArrayInputStream(new byte[0]);
    ArchiveInputStream result = archiveStreamFactory.createArchiveInputStream(archiverName, in);
    assertNotNull(result);
    assertTrue(result instanceof CpioArchiveInputStream);
  }

  @Test
  public void testCreateArchiveOutputStream_AR() throws ArchiveException {
    String archiverName = "ar";
    OutputStream out = new ByteArrayOutputStream();
    ArchiveOutputStream result = archiveStreamFactory.createArchiveOutputStream(archiverName, out);
    assertNotNull(result);
    assertTrue(result instanceof ArArchiveOutputStream);
  }

  @Test
  public void testCreateArchiveOutputStream_ZIP() throws ArchiveException {
    String archiverName = "zip";
    OutputStream out = new ByteArrayOutputStream();
    ArchiveOutputStream result = archiveStreamFactory.createArchiveOutputStream(archiverName, out);
    assertNotNull(result);
    assertTrue(result instanceof ZipArchiveOutputStream);
  }

  @Test
  public void testCreateArchiveOutputStream_TAR() throws ArchiveException {
    String archiverName = "tar";
    OutputStream out = new ByteArrayOutputStream();
    ArchiveOutputStream result = archiveStreamFactory.createArchiveOutputStream(archiverName, out);
    assertNotNull(result);
    assertTrue(result instanceof TarArchiveOutputStream);
  }

  @Test
  public void testCreateArchiveOutputStream_JAR() throws ArchiveException {
    String archiverName = "jar";
    OutputStream out = new ByteArrayOutputStream();
    ArchiveOutputStream result = archiveStreamFactory.createArchiveOutputStream(archiverName, out);
    assertNotNull(result);
    assertTrue(result instanceof JarArchiveOutputStream);
  }

  @Test
  public void testCreateArchiveOutputStream_CPIO() throws ArchiveException {
    String archiverName = "cpio";
    OutputStream out = new ByteArrayOutputStream();
    ArchiveOutputStream result = archiveStreamFactory.createArchiveOutputStream(archiverName, out);
    assertNotNull(result);
    assertTrue(result instanceof CpioArchiveOutputStream);
  }

  @Test
  public void testCreateArchiveInputStream_AutoDetect() throws ArchiveException {
    InputStream in = new ByteArrayInputStream(new byte[0]);
    ArchiveInputStream result = archiveStreamFactory.createArchiveInputStream(in);
    assertNotNull(result);
    assertTrue(result instanceof ZipArchiveInputStream || result instanceof JarArchiveInputStream ||
        result instanceof ArArchiveInputStream || result instanceof CpioArchiveInputStream ||
        result instanceof DumpArchiveInputStream || result instanceof TarArchiveInputStream);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateArchiveInputStream_NullArchiverName() throws ArchiveException {
    String archiverName = null;
    InputStream in = new ByteArrayInputStream(new byte[0]);
    archiveStreamFactory.createArchiveInputStream(archiverName, in);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateArchiveInputStream_NullInputStream() throws ArchiveException {
    String archiverName = "ar";
    InputStream in = null;
    archiveStreamFactory.createArchiveInputStream(archiverName, in);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateArchiveOutputStream_NullArchiverName() throws ArchiveException {
    String archiverName = null;
    OutputStream out = new ByteArrayOutputStream();
    archiveStreamFactory.createArchiveOutputStream(archiverName, out);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateArchiveOutputStream_NullOutputStream() throws ArchiveException {
    String archiverName = "ar";
    OutputStream out = null;
    archiveStreamFactory.createArchiveOutputStream(archiverName, out);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateArchiveInputStream_NullStream() throws ArchiveException {
    InputStream in = null;
    archiveStreamFactory.createArchiveInputStream(in);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateArchiveInputStream_StreamWithoutMark() throws ArchiveException {
    InputStream in = new ByteArrayInputStream(new byte[0]);
    archiveStreamFactory.createArchiveInputStream(in);
  }
}