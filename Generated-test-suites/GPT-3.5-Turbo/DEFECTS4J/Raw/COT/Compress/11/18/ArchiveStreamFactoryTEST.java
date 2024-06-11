import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
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
  public void testCreateArchiveInputStream() {
    try {
      InputStream inputStream = new ByteArrayInputStream(new byte[0]);
      ArchiveStreamFactory factory = new ArchiveStreamFactory();
      // Test AR
      ArchiveInputStream arInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, inputStream);
      assertTrue(arInputStream instanceof ArArchiveInputStream);
      // Test ZIP
      ArchiveInputStream zipInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
      assertTrue(zipInputStream instanceof ZipArchiveInputStream);
      // Test TAR
      ArchiveInputStream tarInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, inputStream);
      assertTrue(tarInputStream instanceof TarArchiveInputStream);
      // Test JAR
      ArchiveInputStream jarInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, inputStream);
      assertTrue(jarInputStream instanceof JarArchiveInputStream);
      // Test CPIO
      ArchiveInputStream cpioInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, inputStream);
      assertTrue(cpioInputStream instanceof CpioArchiveInputStream);
      // Test DUMP
      ArchiveInputStream dumpInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, inputStream);
      assertTrue(dumpInputStream instanceof DumpArchiveInputStream);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testCreateArchiveOutputStream() {
    try {
      OutputStream outputStream = new ByteArrayOutputStream();
      ArchiveStreamFactory factory = new ArchiveStreamFactory();
      // Test AR
      ArchiveOutputStream arOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
      assertTrue(arOutputStream instanceof ArArchiveOutputStream);
      // Test ZIP
      ArchiveOutputStream zipOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
      assertTrue(zipOutputStream instanceof ZipArchiveOutputStream);
      // Test TAR
      ArchiveOutputStream tarOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
      assertTrue(tarOutputStream instanceof TarArchiveOutputStream);
      // Test JAR
      ArchiveOutputStream jarOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
      assertTrue(jarOutputStream instanceof JarArchiveOutputStream);
      // Test CPIO
      ArchiveOutputStream cpioOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, outputStream);
      assertTrue(cpioOutputStream instanceof CpioArchiveOutputStream);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testCreateArchiveInputStreamAutodetect() {
    try {
      InputStream inputStream = new ByteArrayInputStream(new byte[0]);
      ArchiveStreamFactory factory = new ArchiveStreamFactory();
      // Test ZIP
      ArchiveInputStream zipInputStream = factory.createArchiveInputStream(inputStream);
      assertTrue(zipInputStream instanceof ZipArchiveInputStream);
      // Test JAR
      ArchiveInputStream jarInputStream = factory.createArchiveInputStream(inputStream);
      assertTrue(jarInputStream instanceof JarArchiveInputStream);
      // Test AR
      ArchiveInputStream arInputStream = factory.createArchiveInputStream(inputStream);
      assertTrue(arInputStream instanceof ArArchiveInputStream);
      // Test CPIO
      ArchiveInputStream cpioInputStream = factory.createArchiveInputStream(inputStream);
      assertTrue(cpioInputStream instanceof CpioArchiveInputStream);
      // Test DUMP
      ArchiveInputStream dumpInputStream = factory.createArchiveInputStream(inputStream);
      assertTrue(dumpInputStream instanceof DumpArchiveInputStream);
      // Test TAR
      ArchiveInputStream tarInputStream = factory.createArchiveInputStream(inputStream);
      assertTrue(tarInputStream instanceof TarArchiveInputStream);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
}