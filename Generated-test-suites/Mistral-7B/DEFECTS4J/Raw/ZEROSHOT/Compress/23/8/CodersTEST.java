package org.apache.commons.compress.archivers.sevenz;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.tukaani.xz.LZMAInputStream;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CodersTest {

 @Test
 public void testAddDecoderWithCopyCoder() throws Exception {
 InputStream inputStream = mock(InputStream.class);
 SevenZMethod copyMethod = SevenZMethod.COPY;
 Coder copyCoder = new CopyDecoder();
 byte[] password = new byte[0];
 InputStream expectedResult = inputStream;
 InputStream result = Coders.addDecoder(inputStream, copyCoder, password);
 assertEquals(expectedResult, result);
 }

 @Test(expected = IOException.class)
 public void testAddDecoderWithUnsupportedCoder() throws Exception {
 InputStream inputStream = mock(InputStream.class);
 SevenZMethod unsupportedMethod = new SevenZMethod("UnsupportedMethodID");
 Coder unsupportedCoder = mock(Coder.class);
 byte[] password = new byte[0];
 Coders.addDecoder(inputStream, unsupportedCoder, password);
 }

 @Test
 public void testAddEncoderWithLZMAMethod() throws Exception {
 OutputStream outputStream = mock(OutputStream.class);
 SevenZMethod lzmaMethod = SevenZMethod.LZMA;
 byte[] password = new byte[0];
 OutputStream expectedResult = outputStream;
 OutputStream result = Coders.addEncoder(outputStream, lzmaMethod, password);
 assertEquals(expectedResult, result);
 }

 @Test(expected = IOException.class)
 public void testAddEncoderWithUnsupportedMethod() throws Exception {
 OutputStream outputStream = mock(OutputStream.class);
 SevenZMethod unsupportedMethod = new SevenZMethod("UnsupportedMethodID");
 byte[] password = new byte[0];
 Coders.addEncoder(outputStream, unsupportedMethod, password);
 }

 @Test
 public void testLZMADecoder() throws Exception {
 byte[] testData = new byte[]{(byte) 0x1, (byte) 0x2, (byte) 0x3};
 InputStream inputStream = new ByteArrayInputStream(testData);
 SevenZMethod lzmaMethod = SevenZMethod.LZMA;
 Coder lzmaCoder = new LZMADecoder();
 byte[] password = new byte[0];
 InputStream result = Coders.addDecoder(inputStream, lzmaCoder, password);
 byte[] expectedResult = new byte[]{(byte) 0x1, (byte) 0x2, (byte) 0x3};
 byte[] readData = new byte[3];
 result.read(readData, 0, 3);
 assertArrayEquals(expectedResult, readData);
 }

 @Test(expected = IOException.class)
 public void testDeflateDecoderWithNullPassword() throws Exception {
 InputStream inputStream = mock(InputStream.class);
 SevenZMethod deflateMethod = SevenZMethod.DEFLATE;
 Coder deflateCoder = new DeflateDecoder();
 byte[] password = null;
 Coders.addDecoder(inputStream, deflateCoder, password);
 }

 @Test
 public void testAES256SHA256Decoder() throws Exception {
 byte[] testPassword = new byte[]{(byte) 0x1, (byte) 0x2, (byte) 0x3, (byte) 0x4};
 byte[] testSalt = new byte[]{(byte) 0x5, (byte) 0x6, (byte) 0x7, (byte) 0x8};
 byte[] testIV = new byte[]{(byte) 0x9, (byte) 0xa, (byte) 0xb, (byte) 0xc, (byte) 0xd, (byte) 0xe, (byte) 0xf, (byte) 0x10};
 byte[] testData = new byte[]{(byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x14};
 InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x01, 0x02, 0x03, // Byte 0
                                                               (byte) 0x04, 0x11, 0x12, 0x13, 0x14, // Byte 1
                                                               (byte) 0x05, 0x06, 0x07, 0x08, 0x11, 0x12, 0x13, 0x14, // Byte 2
                                                               (byte) 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, // Byte 3
                                                               (byte) 0x11, 0x12, 0x13, 0x14}); // Byte 4
 SevenZMethod aes256SHA256Method = SevenZMethod.AES256SHA256;
 Coder aes256SHA256Coder = new AES256SHA256Decoder();
 byte[] expectedResult = new byte[]{(byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x14};
 InputStream result = Coders.addDecoder(inputStream, aes256SHA256Coder, testPassword);
 byte[] readData = new byte[3];
 result.read(readData, 0, 3);
 assertArrayEquals(expectedResult, readData);
 }

 @Test(expected = IOException.class)
 public void testAES256SHA256DecoderWithNullPassword() throws Exception {
 InputStream inputStream = mock(InputStream.class);
 SevenZMethod aes256SHA256Method = SevenZMethod.AES256SHA256;
 Coder aes256SHA256Coder = new AES256SHA256Decoder();
 byte[] password = null;
 Coders.addDecoder(inputStream, aes256SHA256Coder, password);
 }

 @Test(expected = GeneralSecurityException.class)
 public void testAES256SHA256DecoderWithInvalidPasswordLength() throws Exception {
 InputStream inputStream = mock(InputStream.class);
 SevenZMethod aes256SHA256Method = SevenZMethod.AES256SHA256;
 Coder aes256SHA256Coder = new AES256SHA256Decoder();
 byte[] password = new byte[1];
 Coders.addDecoder(inputStream, aes256SHA256Coder, password);
 }
}