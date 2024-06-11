import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

public class ArchiveStreamFactoryTest {

  @Test
  public void testCreateArchiveInputStream_AR() throws Exception {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    InputStream in = new ByteArrayInputStream(new byte[1024]);
    ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.AR, in);
    assertTrue(ais instanceof ArArchiveInputStream);
  }

  @Test
  public void testCreateArchiveInputStream_ZIP() throws Exception {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    InputStream in = new ByteArrayInputStream(new byte[1024]);
    ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
    assertTrue(ais instanceof ZipArchiveInputStream);
  }

  @Test
  public void testCreateArchiveInputStream_TAR() throws Exception {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    InputStream in = new ByteArrayInputStream(new byte[1024]);
    ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, in);
    assertTrue(ais instanceof TarArchiveInputStream);
  }

  @Test
  public void testCreateArchiveInputStream_JAR() throws Exception {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    InputStream in = new ByteArrayInputStream(new byte[1024]);
    ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, in);
    assertTrue(ais instanceof JarArchiveInputStream);
  }

  @Test
  public void testCreateArchiveInputStream_CPIO() throws Exception {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    InputStream in = new ByteArrayInputStream(new byte[1024]);
    ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, in);
    assertTrue(ais instanceof CpioArchiveInputStream);
  }

  @Test
  public void testCreateArchiveInputStream_DUMP() throws Exception {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    InputStream in = new ByteArrayInputStream(new byte[1024]);
    ArchiveInputStream ais = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, in);
    assertTrue(ais instanceof DumpArchiveInputStream);
  }

  @Test
  public void testCreateArchiveOutputStream_AR() throws Exception {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    OutputStream out = new ByteArrayOutputStream();
    ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, out);
    assertTrue(aos instanceof ArArchiveOutputStream);
  }

  @Test
  public void testCreateArchiveOutputStream_ZIP() throws Exception {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    OutputStream out = new ByteArrayOutputStream();
    ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
    assertTrue(aos instanceof ZipArchiveOutputStream);
  }

  @Test
  public void testCreateArchiveOutputStream_TAR() throws Exception {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    OutputStream out = new ByteArrayOutputStream();
    ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
    assertTrue(aos instanceof TarArchiveOutputStream);
  }

  @Test
  public void testCreateArchiveOutputStream_JAR() throws Exception {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    OutputStream out = new ByteArrayOutputStream();
    ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, out);
    assertTrue(aos instanceof JarArchiveOutputStream);
  }

  @Test
  public void testCreateArchiveOutputStream_CPIO() throws Exception {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    OutputStream out = new ByteArrayOutputStream();
    ArchiveOutputStream aos = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, out);
    assertTrue(aos instanceof CpioArchiveOutputStream);
  }

  @Test
  public void testCreateArchiveInputStream() throws Exception {
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    InputStream in = new ByteArrayInputStream(new byte[1024]);
    ArchiveInputStream ais = factory.createArchiveInputStream(in);
    assertNotNull(ais);
  }

}