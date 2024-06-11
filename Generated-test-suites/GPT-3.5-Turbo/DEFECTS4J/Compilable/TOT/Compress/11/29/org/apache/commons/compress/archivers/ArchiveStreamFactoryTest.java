package org.apache.commons.compress.archivers;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;

public class ArchiveStreamFactoryTest {

  @Test
  public void testCreateArchiveInputStream() throws IOException, ArchiveException {
    
    // Test case 1: Valid archiver name and input stream
    String archiverName = "zip";
    byte[] inputBytes = "Test Input".getBytes();
    InputStream inputStream = new ByteArrayInputStream(inputBytes);
    
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(archiverName, inputStream);
    
    assertNotNull(archiveInputStream);
    // Add assertion for reading from archiveInputStream

    // Test case 2: Null archiver name
    try {
        archiverName = null;
        archiveInputStream = factory.createArchiveInputStream(archiverName, inputStream);
        fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
        // Assertion for exception
    }

    // Test case 3: Null input stream
    try {
        archiverName = "zip";
        inputStream = null;
        archiveInputStream = factory.createArchiveInputStream(archiverName, inputStream);
        fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
        // Assertion for exception
    }

    // Test case 4: Invalid archiver name
    try {
        archiverName = "rar";
        inputStream = new ByteArrayInputStream(inputBytes);
        archiveInputStream = factory.createArchiveInputStream(archiverName, inputStream);
        fail("Should throw ArchiveException");
    } catch (ArchiveException e) {
        // Assertion for exception
    }
  }

  @Test
  public void testCreateArchiveOutputStream() throws IOException, ArchiveException {
    
    // Test case 1: Valid archiver name and output stream
    String archiverName = "zip";
    byte[] outputBytes;
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(archiverName, outputStream);
    
    assertNotNull(archiveOutputStream);
    // Add assertion for writing to archiveOutputStream

    // Test case 2: Null archiver name
    try {
        archiverName = null;
        archiveOutputStream = factory.createArchiveOutputStream(archiverName, outputStream);
        fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
        // Assertion for exception
    }

    // Test case 3: Null output stream
    try {
        archiverName = "zip";
        outputStream = null;
        archiveOutputStream = factory.createArchiveOutputStream(archiverName, outputStream);
        fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
        // Assertion for exception
    }

    // Test case 4: Invalid archiver name
    try {
        archiverName = "rar";
        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = factory.createArchiveOutputStream(archiverName, outputStream);
        fail("Should throw ArchiveException");
    } catch (ArchiveException e) {
        // Assertion for exception
    }
  }

  @Test
  public void testCreateArchiveInputStream_autoDetect() throws IOException, ArchiveException {
    
    // Test case 1: Valid input stream with zip archive type
    byte[] inputBytes = "Test Input".getBytes();
    InputStream inputStream = new ByteArrayInputStream(inputBytes);
    
    ArchiveStreamFactory factory = new ArchiveStreamFactory();
    ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStream);
    
    assertNotNull(archiveInputStream);
    // Add assertion for reading from archiveInputStream

    // Test case 2: Null input stream
    try {
        inputStream = null;
        archiveInputStream = factory.createArchiveInputStream(inputStream);
        fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
        // Assertion for exception
    }

    // Test case 3: Input stream not supporting marks
    try {
        byte[] nonMarkableInputBytes = "Non-Markable Input".getBytes();
        InputStream nonMarkableInputStream = new ByteArrayInputStream(nonMarkableInputBytes);
        archiveInputStream = factory.createArchiveInputStream(nonMarkableInputStream);
        fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
        // Assertion for exception
    }

    // Test case 4: Invalid input stream signature
    try {
        byte[] invalidInputBytes = "Invalid Input".getBytes();
        InputStream invalidInputStream = new ByteArrayInputStream(invalidInputBytes);
        archiveInputStream = factory.createArchiveInputStream(invalidInputStream);
        fail("Should throw ArchiveException");
    } catch (ArchiveException e) {
        // Assertion for exception
    }
  }
}