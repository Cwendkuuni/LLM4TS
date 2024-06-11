package org.apache.commons.compress.compressors.deflate;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DeflateCompressorInputStreamTest {

 private InputStream inputStream;
 private DeflateCompressorInputStream deflateCompressorInputStream;

 @Before
 public void setUp() throws Exception {
 inputStream = getClass().getResourceAsStream("/test_files/deflate_test.bin");
 deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
 }

 @After
 public void tearDown() throws Exception {
 IOUtils.closeQuietly(deflateCompressorInputStream);
 IOUtils.closeQuietly(inputStream);
 }

 @Test
 public void testDeflateCompressorInputStreamCreation() {
 // Test default constructor
 DeflateCompressorInputStream deflateCompressorInputStreamDefault = new DeflateCompressorInputStream(null);
 assertNull(deflateCompressorInputStreamDefault);

 // Test constructor with null input stream
 DeflateCompressorInputStream deflateCompressorInputStreamNull = new DeflateCompressorInputStream(null, new DeflateParameters());
 assertNull(deflateCompressorInputStreamNull);

 // Test constructor with valid input stream
 assertNotNull(deflateCompressorInputStream);
 }

 @Test
 public void testDeflateCompressorInputStreamReadMethod() throws IOException {
 byte[] buffer = new byte[5];
 int read;

 do {
 read = deflateCompressorInputStream.read(buffer, 0, buffer.length);
 } while (read > 0);

 assertEquals(-1, deflateCompressorInputStream.read());
 }

 @Test
 public void testDeflateCompressorInputStreamReadByteMethod() throws IOException {
 byte[] buffer = new byte[5];
 int read;

 do {
 read = deflateCompressorInputStream.read();
 } while (read != -1);

 assertEquals(-1, deflateCompressorInputStream.read());
 }

 @Test
 public void testDeflateCompressorInputStreamReadBytesMethod() throws IOException {
 byte[] buffer = new byte[5];
 int read = deflateCompressorInputStream.read(buffer, 0, buffer.length);
 assertEquals(read, deflateCompressorInputStream.read(buffer, 0, read));
 }

 @Test(expected = IOException.class)
 public void testDeflateCompressorInputStreamThrowsIOExceptionWhenNegativeLengthIsPassed() throws IOException {
 deflateCompressorInputStream.read(-1);
 }

 @Test
 public void testDeflateCompressorInputStreamAvailableMethod() throws IOException {
 assertEquals(deflateCompressorInputStream.available(), deflateCompressorInputStream.skip(deflateCompressorInputStream.available()));
 }

 @Test
 public void testDeflateCompressorInputStreamClosesInputstream() throws IOException {
 deflateCompressorInputStream.close();
 assertFalse(inputStream.isOpen());
 }

 @Test
 public void testDeflateCompressorInputStreamDoesNotAcceptZlibHeaders() throws IOException {
 DeflateCompressorInputStream deflateCompressorInputStreamZlib = new DeflateCompressorInputStream(new InflaterInputStream(inputStream, new Inflater(true)));
 assertTrue(deflateCompressorInputStreamZlib.hasZlibHeader());
 assertFalse(deflateCompressorInputStream.hasZlibHeader());
 }

}