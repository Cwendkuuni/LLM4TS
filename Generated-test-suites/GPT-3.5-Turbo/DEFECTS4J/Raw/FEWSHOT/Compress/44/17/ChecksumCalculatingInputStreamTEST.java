
package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {
  
  private static final int BUFFER_SIZE = 1024;

  @Test
  public void testRead() throws IOException {
    byte[] testData = generateTestData(BUFFER_SIZE);
    InputStream input = new ByteArrayInputStream(testData);
    Checksum checksum = new CRC32();
    ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, input);
    
    int value;
    int checksumResult = 0;
    while ((value = stream.read()) != -1) {
      checksumResult = updateChecksum(checksum, checksumResult, value);
    }
    stream.close();
    
    assertEquals(testData.length, checksumResult);
    assertEquals(checksum.getValue(), checksumResult);
  }
  
  @Test
  public void testReadWithBufferSize() throws IOException {
    byte[] testData = generateTestData(BUFFER_SIZE * 3);
    InputStream input = new ByteArrayInputStream(testData);
    Checksum checksum = new CRC32();
    ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, input);
    
    byte[] buffer = new byte[BUFFER_SIZE];
    int bytesRead;
    int checksumResult = 0;
    while ((bytesRead = stream.read(buffer)) != -1) {
      checksumResult = updateChecksum(checksum, checksumResult, buffer, 0, bytesRead);
    }
    stream.close();
    
    assertEquals(testData.length, checksumResult);
    assertEquals(checksum.getValue(), checksumResult);
  }
  
  @Test
  public void testReadWithOffsetAndLength() throws IOException {
    byte[] testData = generateTestData(BUFFER_SIZE * 2);
    InputStream input = new ByteArrayInputStream(testData);
    Checksum checksum = new CRC32();
    ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, input);
    
    byte[] buffer = new byte[BUFFER_SIZE];
    int offset = BUFFER_SIZE / 2;
    int length = BUFFER_SIZE;
    int bytesRead = stream.read(buffer, offset, length);
    int checksumResult = updateChecksum(checksum, 0, buffer, offset, bytesRead);
    stream.close();
    
    assertEquals(length, bytesRead);
    assertEquals(testData.length, bytesRead);
    assertEquals(checksum.getValue(), checksumResult);
  }
  
  @Test
  public void testSkip() throws IOException {
    byte[] testData = generateTestData(BUFFER_SIZE * 2);
    InputStream input = new ByteArrayInputStream(testData);
    Checksum checksum = new CRC32();
    ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, input);
    
    long skipBytes = BUFFER_SIZE / 2;
    long bytesSkipped = stream.skip(skipBytes);
    int value;
    int checksumResult = 0;
    while ((value = stream.read()) != -1) {
      checksumResult = updateChecksum(checksum, checksumResult, value);
    }
    stream.close();
    
    assertEquals(skipBytes, bytesSkipped);
    assertEquals(testData.length - skipBytes, checksumResult);
    assertEquals(checksum.getValue(), checksumResult);
  }
  
  @Test
  public void testGetValue() throws IOException {
    byte[] testData = generateTestData(BUFFER_SIZE * 2);
    InputStream input = new ByteArrayInputStream(testData);
    Checksum checksum = new CRC32();
    ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, input);
    
    int value;
    while ((value = stream.read()) != -1) {
      updateChecksum(checksum, 0, value);
    }
    long checksumValueBeforeClose = stream.getValue();
    stream.close();
    long checksumValueAfterClose = checksum.getValue();
    
    assertEquals(checksumValueBeforeClose, checksumValueAfterClose);
  }
  
  @Test
  public void testChecksumMatches() throws IOException {
    byte[] testData = generateTestData(BUFFER_SIZE * 2);
    InputStream input = new ByteArrayInputStream(testData);
    Checksum checksum = new CRC32();
    ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, input);
    
    int value;
    while ((value = stream.read()) != -1) {
      updateChecksum(checksum, 0, value);
    }
    stream.close();
    
    assertEquals(testData.length, checksum.getValue());
    assertTrue(stream.getValue() == checksum.getValue());
  }
  
  private byte[] generateTestData(int length) {
    byte[] testData = new byte[length];
    for (int i = 0; i < length; i++) {
      testData[i] = (byte) (i % 256); // fill with incrementing byte values
    }
    return testData;
  }
  
  private int updateChecksum(Checksum checksum, int checksumResult, int value) {
    checksumResult = (int) (checksumResult + (value & 0xFF));
    checksum.update(value);
    return checksumResult;
  }
  
  private int updateChecksum(Checksum checksum, int checksumResult, byte[] buffer, int offset, int length) {
    for (int i = offset; i < offset + length; i++) {
      checksumResult = (int) (checksumResult + (buffer[i] & 0xFF));
      checksum.update(buffer[i]);
    }
    return checksumResult;
  }
}
