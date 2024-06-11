package org.apache.commons.compress.archivers;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.*;
import org.junit.Test;

public class ArchiveStreamFactoryTest {
  
  @Test
  public void testCreateArchiveInputStream() {
    try {
      ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
      ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream("zip", null);
      assertNull(archiveInputStream);
    } catch (ArchiveException e) {
      fail("Should not throw exception");
    }
  }
 
  @Test
  public void testCreateArchiveOutputStream() {
    try {
      ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
      ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream("tar", null);
      assertNull(archiveOutputStream);
    } catch (ArchiveException e) {
      fail("Should not throw exception");
    }
  }

  @Test(expected=IllegalArgumentException.class)
  public void testCreateArchiveInputStreamWithNullName() throws ArchiveException {
    ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
    archiveStreamFactory.createArchiveInputStream(null, null);
  }

  @Test(expected=IllegalArgumentException.class)
  public void testCreateArchiveOutputStreamWithNullName() throws ArchiveException {
    ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
    archiveStreamFactory.createArchiveOutputStream(null, null);
  }

  @Test(expected=IllegalArgumentException.class)
  public void testCreateArchiveInputStreamWithNullStream() throws ArchiveException {
    ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
    archiveStreamFactory.createArchiveInputStream("zip", null);
  }

  @Test(expected=IllegalArgumentException.class)
  public void testCreateArchiveOutputStreamWithNullStream() throws ArchiveException {
    ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
    archiveStreamFactory.createArchiveOutputStream("tar", null);
  }

  @Test
  public void testCreateArchiveInputStreamWithUnknownArchiver() {
    try {
      ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
      archiveStreamFactory.createArchiveInputStream("unknown", null);
      fail("Should throw ArchiveException");
    } catch (ArchiveException e) {
      assertEquals("Archiver: unknown not found.", e.getMessage());
    }
  }

  @Test
  public void testCreateArchiveOutputStreamWithUnknownArchiver() {
    try {
      ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
      archiveStreamFactory.createArchiveOutputStream("unknown", null);
      fail("Should throw ArchiveException");
    } catch (ArchiveException e) {
      assertEquals("Archiver: unknown not found.", e.getMessage());
    }
  }

  @Test
  public void testCreateArchiveInputStreamWithUnsupportedFormat() {
    try {
      ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
      archiveStreamFactory.createArchiveInputStream("7z", null);
      fail("Should throw StreamingNotSupportedException");
    } catch (StreamingNotSupportedException e) {
      assertEquals("7z", e.getFormat());
    } catch (ArchiveException e) {
      fail("Should throw StreamingNotSupportedException");
    }
  }

}

