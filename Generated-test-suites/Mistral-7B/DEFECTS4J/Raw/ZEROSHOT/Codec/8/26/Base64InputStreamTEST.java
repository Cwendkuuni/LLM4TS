package org.apache.commons.codec.binary;

import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.encoding.Base64;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * JUnit test file for Base64InputStream class.
 */
public class Base64InputStreamTest {

 private Base64InputStream testStream;
 private InputStream inputStream;
 private ByteArrayOutputStream outputStream;
 private ByteArrayInputStream inputTestData;
 private byte[] testData;
 private static final int LINE_LENGTH = 76;
 private static final int LINE_SEPARATOR_LENGTH = 2;
 private static final byte[] LINE_SEPARATOR = new byte[]{0x0D, 0x0A};

 @Before
 public void setUp() throws Exception {
 inputTestData = new ByteArrayInputStream(testData);
 testStream = new Base64InputStream(inputTestData, false, LINE_LENGTH, LINE_SEPARATOR);
 outputStream = new ByteArrayOutputStream();
 }

 @After
 public void tearDown() throws Exception {
 testStream.close();
 outputStream.close();
 }

 @Test
 public void testBase64InputStreamWithDefaultConstructor() throws Exception {
 testStream = new Base64InputStream(inputTestData);
 byte[] decodedData = new byte[testData.length];
 int decodedLength = testStream.read(decodedData, 0, decodedData.length);
 assertEquals(testData.length, decodedLength);
 assertArrayEquals(testData, decodedData);
 testStream.close();

 inputStream = new ByteArrayInputStream(testData);
 byte[] encodedData = new byte[testData.length * 4 / 3 + 4];
 int encodedLength = testStream.read(encodedData, 0, encodedData.length);
 assertNotEquals(testData.length, encodedLength);

 ByteArrayInputStream encodedTestStream = new ByteArrayInputStream(encodedData);
 BinaryDecoder decoder = new Base64Decoder();
 byte[] decodedTestData = new byte[testData.length];
 int decodedTestLength = decoder.decode(encodedTestStream, decodedTestData, 0, testData.length);
 assertEquals(testData.length, decodedTestLength);
 assertArrayEquals(testData, decodedTestData);
 encodedTestStream.close();
 }

 @Test
 public void testBase64InputStreamWithEncodeFlagFalse() throws Exception {
 testStream = new Base64InputStream(inputTestData, false);
 byte[] decodedData = new byte[testData.length];
 int decodedLength = testStream.read(decodedData, 0, decodedData.length);
 assertEquals(testData.length, decodedLength);
 assertArrayEquals(testData, decodedData);
 testStream.close();
 }

 @Test(expected = IOException.class)
 public void testBase64InputStreamWithEncodeFlagTrueAndInputDoesNotContainAnyBase64Data() throws Exception {
 testStream = new Base64InputStream(inputTestData, true);
 }

 @Test
 public void testBase64InputStreamWithEncodeFlagTrue() throws Exception {
 testStream = new Base64InputStream(inputTestData, true, LINE_LENGTH, LINE_SEPARATOR);
 outputStream.reset();
 int read;
 byte[] buffer = new byte[LINE_LENGTH - LINE_SEPARATOR_LENGTH];
 while ((read = testStream.read(buffer, 0, buffer.length)) > 0) {
 outputStream.write(buffer, 0, read);
 }
 byte[] encodedData = outputStream.toByteArray();

 ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedData);
 BinaryEncoder encoder = new Base64Encoder();
 byte[] decodedData = new byte[encodedData.length];
 int decodedLength = encoder.encode(inputStream, decodedData, 0, decodedData.length);
 assertEquals(encodedData.length, decodedLength);
 assertArrayEquals(encodedData, decodedData);
 inputStream.close();
 }

 @Test(expected = DecoderException.class)
 public void testBase64InputStreamWithInvalidBase64Data() throws Exception {
 inputTestData = new ByteArrayInputStream(new byte[]{0x00, 0x01, 0x02, 0x03, (byte) 0x80});
 testStream = new Base64InputStream(inputTestData, false);
 }

 @Test(expected = EncoderException.class)
 public void testBase64InputStreamWithInvalidBase64DataWhenEncoding() throws Exception {
 inputTestData = new ByteArrayInputStream(new byte[]{0x00, 0x01, 0x02, 0x03, (byte) 0xC0});
 testStream = new Base64InputStream(inputTestData, true, LINE_LENGTH, LINE_SEPARATOR);
 }
}