package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test class for {@link Base64InputStream}
 */
public class Base64InputStreamTest {

 // Test constructor with default settings
 @Test
 public void testBase64InputStreamDefault() throws IOException {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43 }));
 byte[] expectedBytes = new byte[] { 0x41, 0x42, 0x43 };
 byte[] resultBytes = new byte[expectedBytes.length];
 int bytesRead = inputStream.read(resultBytes);
 assertEquals(expectedBytes.length, bytesRead);
 assertArrayEquals(expectedBytes, resultBytes);
 inputStream.close();
 }

 // Test constructor with encoding enabled
 @Test
 public void testBase64InputStreamEncoding() throws IOException {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43 }), true);
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 byte[] resultBytes = new byte[4];
 int bytesRead;
 do {
 bytesRead = inputStream.read(resultBytes);
 outputStream.write(resultBytes, 0, bytesRead);
 } while (bytesRead > 0);
 String decodedResult = new String(outputStream.toByteArray(), "US-ASCII");
 String expectedResult = new String(new byte[] { 0x41, 0x42, 0x43 }, "US-ASCII");
 assertEquals(expectedResult, decodedResult);
 inputStream.close();
 }

 // Test constructor with custom line length and character encoding
 @Test
 public void testBase64InputStreamCustom() throws IOException {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43 }), true, 50, new byte[] { 0x0D, 0x0A });
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 byte[] resultBytes = new byte[4];
 int bytesRead;
 do {
 bytesRead = inputStream.read(resultBytes);
 outputStream.write(resultBytes, 0, bytesRead);
 } while (bytesRead > 0);
 String decodedResult = new String(outputStream.toByteArray(), "US-ASCII");
 String expectedResult = new String(new byte[] { 0x41, 0x42, 0x43 }, "US-ASCII");
 assertEquals(expectedResult, decodedResult);
 inputStream.close();
 }

 // Test reading past EOF
 @Test(expected = IOException.class)
 public void testBase64InputStreamEOF() throws IOException {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43 }));
 byte[] resultBytes = new byte[1];
 while (inputStream.read(resultBytes) > 0) {}
 inputStream.read(); // should throw exception
 inputStream.close();
 }

 // Test reading null input stream
 @Test(expected = NullPointerException.class)
 public void testBase64InputStreamNullInputStream() {
 Base64InputStream inputStream = new Base64InputStream(null);
 }
}