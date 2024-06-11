import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class DeflateCompressorInputStreamTest {

 private InputStream inputStream;
 private DeflateCompressorInputStream deflateCompressorInputStream;
 private static final byte[] zlibHeader = {(byte) 0x7F, 0x45, 0x4C, 0x4B, 0x42, 0x0A, 0x1A, 0x00, 0x00, 0x00, 0x01, 0x00, 0x0F, 0x40};

 @Before
 public void setUp() {
 inputStream = new ByteArrayInputStream(new byte[]{'A', 'B', 'C'});
 deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
 }

 @Test
 public void testConstructorWithInputStream() {
 DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
 assertNotNull(deflateCompressorInputStream);
 }

 @Test
 public void testConstructorWithInputStreamAndParameters() {
 DeflateParameters deflateParameters = new DeflateParameters();
 DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, deflateParameters);
 assertNotNull(deflateCompressorInputStream);
 }

 @Test
 public void testRead() throws IOException {
 byte[] buffer = new byte[1];
 int read = deflateCompressorInputStream.read();
 assertEquals(-1, read);
 read = deflateCompressorInputStream.read();
 assertEquals('A', read);
 read = deflateCompressorInputStream.read();
 assertEquals('B', read);
 read = deflateCompressorInputStream.read();
 assertEquals('C', read);
 read = deflateCompressorInputStream.read();
 assertEquals(-1, read);
 }

 @Test
 public void testReadByteArray() throws IOException {
 byte[] buffer = new byte[3];
 int length = deflateCompressorInputStream.read(buffer, 0, buffer.length);
 assertEquals(3, length);
 for (int i = 0; i < buffer.length; i++) {
 assertEquals(i + 'A', buffer[i]);
 }
 }

 @Test
 public void testSkip() throws IOException {
 deflateCompressorInputStream.skip(1);
 byte[] buffer = new byte[1];
 int length = deflateCompressorInputStream.read(buffer, 0, buffer.length);
 assertEquals('B', buffer[0]);
 deflateCompressorInputStream.skip(2);
 length = deflateCompressorInputStream.read(buffer, 0, buffer.length);
 assertEquals('C', buffer[0]);
 }

 @Test
 public void testAvailable() throws IOException {
 assertEquals(3, deflateCompressorInputStream.available());
 }

 @Test
 public void testClose() throws IOException {
 deflateCompressorInputStream.close();
 assertFalse(deflateCompressorInputStream.isOpen());
 }

 @Test(expected = IOException.class)
 public void testCheckZlibSignatureWithZlibHeader() throws IOException {
 byte[] buffer = new byte[zlibHeader.length];
 int length = deflateCompressorInputStream.read(buffer, 0, zlibHeader.length);
 assertEquals(zlibHeader.length, length);
 assertSame(zlibHeader, buffer);
 }

 @Test(expected = IOException.class)
 public void testCheckZlibSignatureWithoutZlibHeader() throws IOException {
 byte[] buffer = new byte[1];
 int length = deflateCompressorInputStream.read(buffer, 0, 1);
 assertEquals(1, length);
 deflateCompressorInputStream.skip(2);
 length = deflateCompressorInputStream.read(buffer, 0, 1);
 assertEquals(1, length);
 deflateCompressorInputStream.skip(1);
 length = deflateCompressorInputStream.read(buffer, 0, 1);
 assertEquals(-1, length);
 deflateCompressorInputStream.close();
 deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
 deflateCompressorInputStream.skip(1);
 length = deflateCompressorInputStream.read(buffer, 0, 1);
 assertEquals('A', buffer[0]);
 }
}