package org.apache.commons.compress.archivers;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {
  
  // Variables for testing
  private ArchiveStreamFactory factory;
  private OutputStream outputStream;
  private InputStream inputStream;
  
  @Before
  public void setUp() {
    factory = new ArchiveStreamFactory();
    outputStream = new ByteArrayOutputStream();
  }
  
  @After
  public void tearDown() throws IOException {
    if (outputStream != null) {
      outputStream.close();
    }
    if (inputStream != null) {
      inputStream.close();
    }
  }
  
  @Test
  public void testCreateArchiveInputStream_WithValidArchiverNameAndInputStream_ReturnsArchiveInputStream() throws Exception {
    // Arrange
    String validArchiverName = ArchiveStreamFactory.ZIP;
    inputStream = new ByteArrayInputStream(new byte[0]);
    
    // Act
    ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(validArchiverName, inputStream);
    
    // Assert
    assertNotNull(archiveInputStream);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateArchiveInputStream_WithNullArchiverName_ThrowsIllegalArgumentException() throws Exception {
    // Arrange
    String nullArchiverName = null;
    inputStream = new ByteArrayInputStream(new byte[0]);
    
    // Act
    factory.createArchiveInputStream(nullArchiverName, inputStream);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateArchiveInputStream_WithNullInputStream_ThrowsIllegalArgumentException() throws Exception {
    // Arrange
    String validArchiverName = ArchiveStreamFactory.ZIP;
    inputStream = null;
    
    // Act
    factory.createArchiveInputStream(validArchiverName, inputStream);
  }
  
  @Test(expected = ArchiveException.class)
  public void testCreateArchiveInputStream_WithInvalidArchiverName_ThrowsArchiveException() throws Exception {
    // Arrange
    String invalidArchiverName = "invalid";
    inputStream = new ByteArrayInputStream(new byte[0]);
    
    // Act
    factory.createArchiveInputStream(invalidArchiverName, inputStream);
  }
  
  @Test
  public void testCreateArchiveOutputStream_WithValidArchiverNameAndOutputStream_ReturnsArchiveOutputStream() throws Exception {
    // Arrange
    String validArchiverName = ArchiveStreamFactory.ZIP;
    
    // Act
    ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(validArchiverName, outputStream);
    
    // Assert
    assertNotNull(archiveOutputStream);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateArchiveOutputStream_WithNullArchiverName_ThrowsIllegalArgumentException() throws Exception {
    // Arrange
    String nullArchiverName = null;
    
    // Act
    factory.createArchiveOutputStream(nullArchiverName, outputStream);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateArchiveOutputStream_WithNullOutputStream_ThrowsIllegalArgumentException() throws Exception {
    // Arrange
    outputStream = null;
    String validArchiverName = ArchiveStreamFactory.ZIP;
    
    // Act
    factory.createArchiveOutputStream(validArchiverName, outputStream);
  }
  
  @Test(expected = ArchiveException.class)
  public void testCreateArchiveOutputStream_WithInvalidArchiverName_ThrowsArchiveException() throws Exception {
    // Arrange
    String invalidArchiverName = "invalid";
    
    // Act
    factory.createArchiveOutputStream(invalidArchiverName, outputStream);
  }
  
  @Test
  public void testCreateArchiveInputStream_AutoDetectingArchiveTypeFromInputStream_ReturnsArchiveInputStream() throws Exception {
    // Arrange
    inputStream = new ByteArrayInputStream(new byte[0]);
    
    // Act
    ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStream);
    
    // Assert
    assertNotNull(archiveInputStream);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateArchiveInputStream_AutoDetectingArchiveTypeFromNullInputStream_ThrowsIllegalArgumentException() throws Exception {
    // Arrange
    inputStream = null;
    
    // Act
    factory.createArchiveInputStream(inputStream);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateArchiveInputStream_AutoDetectingArchiveTypeFromInputStreamWithoutMarkSupport_ThrowsIllegalArgumentException() throws Exception {
    // Arrange
    inputStream = new InputStream() {
      @Override
      public int read() throws IOException {
        return -1;
      }
    };
    
    // Act
    factory.createArchiveInputStream(inputStream);
  }
  
  @Test(expected = ArchiveException.class)
  public void testCreateArchiveInputStream_AutoDetectingArchiveTypeFromInputStreamWithInvalidSignature_ThrowsArchiveException() throws Exception {
    // Arrange
    byte[] invalidSignature = new byte[12];
    inputStream = new ByteArrayInputStream(invalidSignature);
    
    // Act
    factory.createArchiveInputStream(inputStream);
  }
  
  // Add more test cases for other methods if needed
  
}