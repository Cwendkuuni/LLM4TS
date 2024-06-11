package org.apache.commons.compress.archivers;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {
  
  @Test
  public void testCreateArchiveInputStream() {
    try {
      ArchiveStreamFactory factory = new ArchiveStreamFactory();
      assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[0])));
      assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.ARJ, new ByteArrayInputStream(new byte[0])));
      assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(new byte[0])));
      assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(new byte[0])));
      assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(new byte[0])));
      assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(new byte[0])));
      assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[0])));
      try {
        factory.createArchiveInputStream("unknown", new ByteArrayInputStream(new byte[0]));
        fail("Should have thrown an ArchiveException");
      } catch (ArchiveException e) {
        // expected
      }
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }
  
  @Test
  public void testCreateArchiveOutputStream() {
    try {
      ArchiveStreamFactory factory = new ArchiveStreamFactory();
      assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.AR, new ByteArrayOutputStream()));
      assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, new ByteArrayOutputStream()));
      assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, new ByteArrayOutputStream()));
      assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, new ByteArrayOutputStream()));
      assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, new ByteArrayOutputStream()));
      try {
        factory.createArchiveOutputStream("unknown", new ByteArrayOutputStream());
        fail("Should have thrown an ArchiveException");
      } catch (ArchiveException e) {
        // expected
      }
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }
  
  @Test
  public void testCreateArchiveInputStreamAutoDetect() {
    try {
      ArchiveStreamFactory factory = new ArchiveStreamFactory();
      assertNotNull(factory.createArchiveInputStream(new ByteArrayInputStream(new byte[0])));
    } catch (Exception e) {
      fail("Exception occurred: " + e.getMessage());
    }
  }
  
}