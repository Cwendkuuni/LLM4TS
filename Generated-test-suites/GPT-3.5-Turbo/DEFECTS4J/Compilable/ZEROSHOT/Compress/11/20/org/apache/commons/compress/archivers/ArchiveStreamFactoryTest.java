package org.apache.commons.compress.archivers;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
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

public class ArchiveStreamFactoryTest {
  
  private ArchiveStreamFactory factory;
  private InputStream file1;
  private InputStream file2;
  private ByteArrayOutputStream outputStream;
  
  @Before
  public void setup() {
    factory = new ArchiveStreamFactory();
    file1 = new ByteArrayInputStream("test file 1".getBytes());
    file2 = new ByteArrayInputStream("test file 2".getBytes());
    outputStream = new ByteArrayOutputStream();
  }
  
  @Test
  public void testCreateArchiveInputStreamAR() throws ArchiveException, IOException {
    ArchiveInputStream in = factory.createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[0]));
    assertTrue(in instanceof ArArchiveInputStream);
    in.close();
  }
  
  @Test
  public void testCreateArchiveInputStreamZIP() throws ArchiveException, IOException {
    ArchiveInputStream in = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[0]));
    assertTrue(in instanceof ZipArchiveInputStream);
    in.close();
  }
  
  @Test
  public void testCreateArchiveInputStreamTAR() throws ArchiveException, IOException {
    ArchiveInputStream in = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(new byte[0]));
    assertTrue(in instanceof TarArchiveInputStream);
    in.close();
  }
  
  @Test
  public void testCreateArchiveInputStreamJAR() throws ArchiveException, IOException {
    ArchiveInputStream in = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(new byte[0]));
    assertTrue(in instanceof JarArchiveInputStream);
    in.close();
  }
  
  @Test
  public void testCreateArchiveInputStreamCPIO() throws ArchiveException, IOException {
    ArchiveInputStream in = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(new byte[0]));
    assertTrue(in instanceof CpioArchiveInputStream);
    in.close();
  }
  
  @Test
  public void testCreateArchiveInputStreamDUMP() throws ArchiveException, IOException {
    ArchiveInputStream in = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(new byte[0]));
    assertTrue(in instanceof DumpArchiveInputStream);
    in.close();
  }
  
  @Test(expected = ArchiveException.class)
  public void testCreateArchiveInputStreamUnknown() throws ArchiveException, IOException {
    factory.createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[0]));
  }
  
  @Test
  public void testCreateArchiveOutputStreamAR() throws ArchiveException, IOException {
    ArchiveOutputStream out = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, outputStream);
    assertTrue(out instanceof ArArchiveOutputStream);
    out.close();
  }
  
  @Test
  public void testCreateArchiveOutputStreamZIP() throws ArchiveException, IOException {
    ArchiveOutputStream out = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
    assertTrue(out instanceof ZipArchiveOutputStream);
    out.close();
  }
  
  @Test
  public void testCreateArchiveOutputStreamTAR() throws ArchiveException, IOException {
    ArchiveOutputStream out = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
    assertTrue(out instanceof TarArchiveOutputStream);
    out.close();
  }
  
  @Test
  public void testCreateArchiveOutputStreamJAR() throws ArchiveException, IOException {
    ArchiveOutputStream out = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, outputStream);
    assertTrue(out instanceof JarArchiveOutputStream);
    out.close();
  }
  
  @Test
  public void testCreateArchiveOutputStreamCPIO() throws ArchiveException, IOException {
    ArchiveOutputStream out = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, outputStream);
    assertTrue(out instanceof CpioArchiveOutputStream);
    out.close();
  }
  
  @Test(expected = ArchiveException.class)
  public void testCreateArchiveOutputStreamUnknown() throws ArchiveException, IOException {
    factory.createArchiveOutputStream("unknown", outputStream);
  }
  
  @Test
  public void testCreateArchiveInputStreamSignatureZIP() throws ArchiveException, IOException {
    byte[] signature = new byte[] { 80, 75, 3, 4, 10, 0, 2, 0, 0, 0, 0, 0 };
    ArchiveInputStream in = factory.createArchiveInputStream(new ByteArrayInputStream(signature));
    assertTrue(in instanceof ZipArchiveInputStream);
    in.close();
  }
  
  @Test
  public void testCreateArchiveInputStreamSignatureJAR() throws ArchiveException, IOException {
    byte[] signature = new byte[] { 80, 75, 3, 4, 10, 0, 2, 0, 0, 0, 0, 0 };
    ArchiveInputStream in = factory.createArchiveInputStream(new ByteArrayInputStream(signature));
    assertTrue(in instanceof JarArchiveInputStream);
    in.close();
  }
  
  @Test
  public void testCreateArchiveInputStreamSignatureAR() throws ArchiveException, IOException {
    byte[] signature = new byte[] { 33, 60, 97, 114, 99, 104, 62, 13, 10, 26, 0, 0 };
    ArchiveInputStream in = factory.createArchiveInputStream(new ByteArrayInputStream(signature));
    assertTrue(in instanceof ArArchiveInputStream);
    in.close();
  }
  
  @Test
  public void testCreateArchiveInputStreamSignatureCPIO() throws ArchiveException, IOException {
    byte[] signature = new byte[] { 070, 117, 115, 116, 97, 114, 32, 48, 48, 48, 48, 0 };
    ArchiveInputStream in = factory.createArchiveInputStream(new ByteArrayInputStream(signature));
    assertTrue(in instanceof CpioArchiveInputStream);
    in.close();
  }
  
  @Test
  public void testCreateArchiveInputStreamSignatureDUMP() throws ArchiveException, IOException {
    byte[] signature = new byte[] { 126, 98, 105, 110, 47, 115, 104, 13, 10, 0, 0, 0 };
    ArchiveInputStream in = factory.createArchiveInputStream(new ByteArrayInputStream(signature));
    assertTrue(in instanceof DumpArchiveInputStream);
    in.close();
  }
  
  @Test
  public void testCreateArchiveInputStreamSignatureTAR() throws ArchiveException, IOException {
    byte[] signature = new byte[] { 116, 114, 117, 110, 99, 97, 116, 0, 0, 0, 0, 0 };
    ArchiveInputStream in = factory.createArchiveInputStream(new ByteArrayInputStream(signature));
    assertTrue(in instanceof TarArchiveInputStream);
    in.close();
  }
  
  @Test(expected = ArchiveException.class)
  public void testCreateArchiveInputStreamSignatureUnknown() throws ArchiveException, IOException {
    byte[] signature = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
    factory.createArchiveInputStream(new ByteArrayInputStream(signature));
  }

  @Test
  public void testCreateArchiveInputStreamSignatureCustom() throws ArchiveException, IOException {
    byte[] signature = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
    ArchiveInputStream in = new ArchiveStreamFactory() {
      @Override
      public ArchiveInputStream createArchiveInputStream(String archiverName, InputStream in) throws ArchiveException {
        return new ArchiveInputStream() {
          @Override
          public ArchiveEntry getNextEntry() throws IOException {
            return null;
          }
          
          @Override
          public int read() throws IOException {
            return -1;
          }
          
          @Override
          public int read(byte[] b, int off, int len) throws IOException {
            return -1;
          }
          
          @Override
          public void close() throws IOException {
          }
        };
      }
    }.createArchiveInputStream(new ByteArrayInputStream(signature));
    assertNotNull(in);
    in.close();
  }
  
}