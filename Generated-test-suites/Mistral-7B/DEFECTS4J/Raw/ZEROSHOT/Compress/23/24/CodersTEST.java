package org.apache.commons.compress.archivers.sevenz;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.tukaani.xz.LZMAInputStream;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

public class CodersTest {

 @Test
 public void testAddDecoder() throws Exception {
 Coders coders = new Coders();

 // Test with BZIP2 compression method
 byte[] bzip2Password = "password123".getBytes();
 ByteArrayInputStream bzip2InputStream = new ByteArrayInputStream(
 "BZGold\u0001\u0002BZip2".getBytes());
 SevenZMethod bzip2Method = SevenZMethod.BZIP2;
 InputStream expectedBzip2InputStream = new BZip2CompressorInputStream(bzip2InputStream);
 InputStream actualBzip2InputStream = coders.addDecoder(bzip2InputStream, new Coder(bzip2Method, null), bzip2Password);
 assertTrue(actualBzip2InputStream instanceof BZip2CompressorInputStream);
 assertEquals(expectedBzip2InputStream, actualBzip2InputStream);

 // Test with LZMA compression method
 byte[] lzmaPassword = "password456".getBytes();
 ByteArrayInputStream lzmaInputStream = new ByteArrayInputStream(
 "\u000F\u0001\u0002\u000A\u0000\u0000\u0000\u0000\u0000PROPS\u0000\u0000\u0000\u0000\u0000\u0000\u0000".getBytes());
 SevenZMethod lzmaMethod = SevenZMethod.LZMA;
 InputStream expectedLzmaInputStream = new LZMAInputStream(bzip2InputStream);
 InputStream actualLzmaInputStream = coders.addDecoder(lzmaInputStream, new Coder(lzmaMethod, null), lzmaPassword);
 assertTrue(actualLzmaInputStream instanceof LZMAInputStream);
 assertEquals(expectedLzmaInputStream, actualLzmaInputStream);

 // Test with unsupported compression method
 SevenZMethod unsupportedMethod = new SevenZMethod(-1);
 InputStream inputStream = new ByteArrayInputStream(new byte[0]);
 try {
 InputStream actualUnsupportedInputStream = coders.addDecoder(inputStream, new Coder(unsupportedMethod, null), null);
 fail();
 } catch (IOException e) {
 String expectedMessage = "Unsupported compression method " + unsupportedMethod;
 String actualMessage = e.getMessage();
 assertTrue(actualMessage.startsWith(expectedMessage));
 }
 }

 @Test
 public void testAddEncoder() throws Exception {
 Coders coders = new Coders();

 // Test with BZIP2 compression method
 ByteArrayOutputStream bzip2OutputStream = new ByteArrayOutputStream();
 OutputStream expectedBzip2OutputStream = new BZip2CompressorOutputStream(bzip2OutputStream);
 OutputStream actualBzip2OutputStream = coders.addEncoder(expectedBzip2OutputStream, SevenZMethod.BZIP2, null);
 assertTrue(actualBzip2OutputStream instanceof BZip2CompressorOutputStream);
 assertEquals(expectedBzip2OutputStream, actualBzip2OutputStream);

 // Test with LZMA compression method
 ByteArrayOutputStream lzmaOutputStream = new ByteArrayOutputStream();
 OutputStream expectedLzmaOutputStream = new LZMAOutputStream(lzmaOutputStream);
 OutputStream actualLzmaOutputStream = coders.addEncoder(expectedLzmaOutputStream, SevenZMethod.LZMA, null);
 assertTrue(actualLzmaOutputStream instanceof LZMAOutputStream);
 assertEquals(expectedLzmaOutputStream, actualLzmaOutputStream);

 // Test with unsupported compression method
 SevenZMethod unsupportedMethod = new SevenZMethod(-1);
 OutputStream outputStream = new ByteArrayOutputStream();
 try {
 OutputStream actualUnsupportedOutputStream = coders.addEncoder(outputStream, unsupportedMethod, null);
 fail();
 } catch (IOException e) {
 String expectedMessage = "Unsupported compression method " + unsupportedMethod;
 String actualMessage = e.getMessage();
 assertTrue(actualMessage.startsWith(expectedMessage));
 }
 }
}

class Coder {
 Coder(SevenZMethod method, CoderBase coder) {
 this.method = method;
 this.coder = coder;
 }
 SevenZMethod method;
 CoderBase coder;
}