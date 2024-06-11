package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.Checksums;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ChecksumCalculatingInputStreamTest {

 @Test
 public void testChecksumCalculatingInputStream() throws Exception {
 Checksum checksum = Checksums.adler32();
 InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x1, (byte) 0x2, (byte) 0x3, (byte) 0x4});
 ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

 // Test reading a single byte
 int readByte = checksumCalculatingInputStream.read();
 assertEquals((int) 0x1, readByte);
 assertEquals(0x1, checksum.getValue());

 // Test reading a byte array
 byte[] byteArray = new byte[4];
 int length = checksumCalculatingInputStream.read(byteArray);
 assertEquals(4, length);
 assertEquals((byte) 0x1, byteArray[0]);
 assertEquals((byte) 0x2, byteArray[1]);
 assertEquals((byte) 0x3, byteArray[2]);
 assertEquals((byte) 0x4, byteArray[3]);
 assertEquals(0x18020314L, checksum.getValue()); // Adler32 checksum for the given input

 // Test reading a negative length
 int negativeLength = -1;
 try {
 checksumCalculatingInputStream.read(new byte[negativeLength]);
 } catch (Exception e) {
 // expected exception
 }

 // Test checking the checksum
 long expectedChecksum = 0x18020314L;
 assertEquals(expectedChecksum, checksumCalculatingInputStream.getValue());
 }
}