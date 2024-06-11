package org.apache.commons.compress.archivers.sevenz;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.lzma.LZMAOutputStream;
import org.tukaani.xz.LZMAInputStream;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecretKey;
import java.security.spec.SecretKeySpec;

public class CodersTest {

 @Test
 public void testAddDecoderWithSupportedCoder() throws IOException, NoSuchAlgorithmException {
 byte[] testData = "This is a test data".getBytes(StandardCharsets.UTF_8);
 byte[] password = "password".getBytes(StandardCharsets.UTF_8);
 InputStream inputStream = new ByteArrayInputStream(testData);
 SevenZMethod sevenZMethod = SevenZMethod.DEFLATE;
 Coder coder = sevenZMethod == SevenZMethod.DEFLATE ? new DeflateDecoder() : new BZIP2Decoder();
 InputStream expectedOutput = sevenZMethod == SevenZMethod.DEFLATE ? new InflaterInputStream(new ByteArrayInputStream(testData.withByte(0)), new Inflater(true)) : new BZip2CompressorInputStream(new ByteArrayInputStream(testData));
 InputStream output = Coders.addDecoder(inputStream, coder, password);
 byte[] outputByteArray = new byte[testData.length];
 int bytesRead = -1;
 while ((bytesRead = output.read(outputByteArray, 0, outputByteArray.length)) != -1) {
 assertArrayEquals(expectedOutput.readAllBytes(), Arrays.copyOfRange(outputByteArray, 0, bytesRead));
 }
 assertEquals(expectedOutput.available(), output.available());
 output.close();
 expectedOutput.close();
 }

 @Test(expected = IOException.class)
 public void testAddDecoderWithUnsupportedCoder() throws IOException {
 byte[] testData = "This is a test data".getBytes(StandardCharsets.UTF_8);
 byte[] password = "password".getBytes(StandardCharsets.UTF_8);
 InputStream inputStream = new ByteArrayInputStream(testData);
 SevenZMethod sevenZMethod = SevenZMethod.LZMA;
 Coder coder = new LZMADecoder();
 Coders.addDecoder(inputStream, coder, password);
 }

 @Test
 public void testAddEncoderWithSupportedCoder() throws IOException, NoSuchAlgorithmException {
 byte[] testData = "This is a test data".getBytes(StandardCharsets.UTF_8);
 byte[] password = "password".getBytes(StandardCharsets.UTF_8);
 OutputStream outputStream = new ByteArrayOutputStream();
 SevenZMethod sevenZMethod = SevenZMethod.DEFLATE;
 Coder coder = sevenZMethod == SevenZMethod.DEFLATE ? new DeflateDecoder() : new BZIP2Decoder();
 OutputStream expectedOutput = sevenZMethod == SevenZMethod.DEFLATE ? new DeflaterOutputStream(new ByteArrayOutputStream(), new Deflater(9, true)) : new BZip2CompressorOutputStream(new ByteArrayOutputStream());
 Coders.addEncoder(outputStream, sevenZMethod, password);
 byte[] outputByteArray = outputStream.toByteArray();
 OutputStream newOutput = new ByteArrayOutputStream();
 Coders.addEncoder(newOutput, sevenZMethod, password);
 InputStream inputStream = sevenZMethod == SevenZMethod.DEFLATE ? new InflaterInputStream(new ByteArrayInputStream(outputByteArray), new Inflater(true)) : new BZip2CompressorInputStream(new ByteArrayInputStream(outputByteArray));
 byte[] expectedOutputByteArray = new byte[testData.length];
 int bytesRead = -1;
 while ((bytesRead = inputStream.read(expectedOutputByteArray, 0, expectedOutputByteArray.length)) != -1) {
 assertArrayEquals(testData, Arrays.copyOfRange(expectedOutputByteArray, 0, bytesRead));
 }
 assertArrayEquals(outputByteArray, newOutput.toByteArray());
 inputStream.close();
 newOutput.close();
 }

 @Test(expected = IOException.class)
 public void testAddEncoderWithUnsupportedCoder() throws IOException {
 byte[] testData = "This is a test data".getBytes(StandardCharsets.UTF_8);
 byte[] password = "password".getBytes(StandardCharsets.UTF_8);
 OutputStream outputStream = new ByteArrayOutputStream();
 SevenZMethod sevenZMethod = SevenZMethod.LZMA;
 Coder coder = new DeflateDecoder();
 Coders.addEncoder(outputStream, sevenZMethod, password);
 }

 @Test
 public void testCoderIdClass() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
 byte[] testData = "This is a test data".getBytes(StandardCharsets.UTF_8);
 byte[] password = "password".getBytes(StandardCharsets.UTF_8);
 SevenZMethod sevenZMethod = SevenZMethod.AES256SHA256;
 InputStream inputStream = new ByteArrayInputStream(testData);
 OutputStream outputStream = new ByteArrayOutputStream();
 Coder coder = new AES256SHA256Decoder();
 CoderId coderId = new CoderId(sevenZMethod, coder);
 assertEquals(sevenZMethod, coderId.method);
 assertEquals(coder, coderId.coder);
 InputStream decodedStream = Coders.addDecoder(inputStream, coderId, password);
 byte[] outputByteArray = new byte[testData.length];
 int bytesRead = -1;
 while ((bytesRead = decodedStream.read(outputByteArray, 0, outputByteArray.length)) != -1) {
 assertArrayEquals(testData, Arrays.copyOfRange(outputByteArray, 0, bytesRead));
 }
 decodedStream.close();
 OutputStream encodedStream = Coders.addEncoder(outputStream, sevenZMethod, password);
 byte[] encodedByteArray = outputStream.toByteArray();
 InputStream encodedInputStream = new ByteArrayInputStream(encodedByteArray);
 byte[] expectedOutputByteArray = new byte[testData.length];
 int bytesReadFromEncodedStream = -1;
 while ((bytesReadFromEncodedStream = encodedInputStream.read(expectedOutputByteArray, 0, expectedOutputByteArray.length)) != -1) {
 assertArrayEquals(testData, Arrays.copyOfRange(expectedOutputByteArray, 0, bytesReadFromEncodedStream));
 }
 encodedInputStream.close();
 assertEquals(outputByteArray, encodedByteArray);
 encodedStream.close();
 }
}