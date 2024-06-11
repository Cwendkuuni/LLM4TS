import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
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
import org.junit.Before;
import org.junit.Test;

public class ArchiveStreamFactoryTest {
  
  private ArchiveStreamFactory factory;
  
  @Before
  public void setUp() {
    factory = new ArchiveStreamFactory();
  }
  
  // Test createArchiveInputStream(String archiverName, InputStream in)
  @Test
  public void testCreateArchiveInputStream() throws ArchiveException, IOException {
    // Test for AR archive format
    String archiverName = ArchiveStreamFactory.AR;
    InputStream in = new ByteArrayInputStream(new byte[1]);
    ArchiveInputStream ais = factory.createArchiveInputStream(archiverName, in);
    assertTrue(ais instanceof ArArchiveInputStream);
    
    // Test for ZIP archive format
    archiverName = ArchiveStreamFactory.ZIP;
    in = new ByteArrayInputStream(new byte[1]);
    ais = factory.createArchiveInputStream(archiverName, in);
    assertTrue(ais instanceof ZipArchiveInputStream);
    
    // Test for TAR archive format
    archiverName = ArchiveStreamFactory.TAR;
    in = new ByteArrayInputStream(new byte[1]);
    ais = factory.createArchiveInputStream(archiverName, in);
    assertTrue(ais instanceof TarArchiveInputStream);
    
    // Test for JAR archive format
    archiverName = ArchiveStreamFactory.JAR;
    in = new ByteArrayInputStream(new byte[1]);
    ais = factory.createArchiveInputStream(archiverName, in);
    assertTrue(ais instanceof JarArchiveInputStream);
    
    // Test for CPIO archive format
    archiverName = ArchiveStreamFactory.CPIO;
    in = new ByteArrayInputStream(new byte[1]);
    ais = factory.createArchiveInputStream(archiverName, in);
    assertTrue(ais instanceof CpioArchiveInputStream);
    
    // Test for DUMP archive format
    archiverName = ArchiveStreamFactory.DUMP;
    in = new ByteArrayInputStream(new byte[1]);
    ais = factory.createArchiveInputStream(archiverName, in);
    assertTrue(ais instanceof DumpArchiveInputStream);
    
    // Test for unknown archiver name
    archiverName = "unknown";
    in = new ByteArrayInputStream(new byte[1]);
    try {
      factory.createArchiveInputStream(archiverName, in);
      fail("ArchiveException expected");
    } catch (ArchiveException e) {
      // expected
    }
  }
  
  // Test createArchiveOutputStream(String archiverName, OutputStream out)
  @Test
  public void testCreateArchiveOutputStream() throws ArchiveException, IOException {
    // Test for AR archive format
    String archiverName = ArchiveStreamFactory.AR;
    OutputStream out = new ByteArrayOutputStream();
    ArchiveOutputStream aos = factory.createArchiveOutputStream(archiverName, out);
    assertTrue(aos instanceof ArArchiveOutputStream);
    
    // Test for ZIP archive format
    archiverName = ArchiveStreamFactory.ZIP;
    out = new ByteArrayOutputStream();
    aos = factory.createArchiveOutputStream(archiverName, out);
    assertTrue(aos instanceof ZipArchiveOutputStream);
    
    // Test for TAR archive format
    archiverName = ArchiveStreamFactory.TAR;
    out = new ByteArrayOutputStream();
    aos = factory.createArchiveOutputStream(archiverName, out);
    assertTrue(aos instanceof TarArchiveOutputStream);
    
    // Test for JAR archive format
    archiverName = ArchiveStreamFactory.JAR;
    out = new ByteArrayOutputStream();
    aos = factory.createArchiveOutputStream(archiverName, out);
    assertTrue(aos instanceof JarArchiveOutputStream);
    
    // Test for CPIO archive format
    archiverName = ArchiveStreamFactory.CPIO;
    out = new ByteArrayOutputStream();
    aos = factory.createArchiveOutputStream(archiverName, out);
    assertTrue(aos instanceof CpioArchiveOutputStream);
    
    // Test for unknown archiver name
    archiverName = "unknown";
    out = new ByteArrayOutputStream();
    try {
      factory.createArchiveOutputStream(archiverName, out);
      fail("ArchiveException expected");
    } catch (ArchiveException e) {
      // expected
    }
  }
  
  // Test createArchiveInputStream(InputStream in)
  @Test
  public void testCreateArchiveInputStream_withAutoDetection() throws ArchiveException, IOException {
    // Test for ZIP archive format
    byte[] zipSignature = { 0x50, 0x4b, 0x03, 0x04 };
    InputStream in = new ByteArrayInputStream(zipSignature);
    ArchiveInputStream ais = factory.createArchiveInputStream(in);
    assertTrue(ais instanceof ZipArchiveInputStream);
    
    // Test for JAR archive format
    byte[] jarSignature = { 0x50, 0x4b, 0x03, 0x04 };
    in = new ByteArrayInputStream(jarSignature);
    ais = factory.createArchiveInputStream(in);
    assertTrue(ais instanceof JarArchiveInputStream);
    
    // Test for AR archive format
    byte[] arSignature = { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a };
    in = new ByteArrayInputStream(arSignature);
    ais = factory.createArchiveInputStream(in);
    assertTrue(ais instanceof ArArchiveInputStream);
    
    // Test for CPIO archive format
    byte[] cpioSignature = { 0x30, 0x37, 0x30, 0x37 };
    in = new ByteArrayInputStream(cpioSignature);
    ais = factory.createArchiveInputStream(in);
    assertTrue(ais instanceof CpioArchiveInputStream);
    
    // Test for DUMP archive format
    byte[] dumpSignature = { 0x75, 0x73, 0x74, 0x61, 0x72 };
    in = new ByteArrayInputStream(dumpSignature);
    ais = factory.createArchiveInputStream(in);
    assertTrue(ais instanceof DumpArchiveInputStream);
    
    // Test for TAR archive format
    byte[] tarSignature = { 0x75, 0x73, 0x74, 0x61, 0x72 };
    in = new ByteArrayInputStream(tarSignature);
    ais = factory.createArchiveInputStream(in);
    assertTrue(ais instanceof TarArchiveInputStream);
    
    // Test for unknown archive format
    byte[] unknownSignature = { 0x00 };
    in = new ByteArrayInputStream(unknownSignature);
    try {
      factory.createArchiveInputStream(in);
      fail("ArchiveException expected");
    } catch (ArchiveException e) {
      // expected
    }
  }

}