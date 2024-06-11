package org.apache.commons.codec.binary;

 import static org.apache.commons.codec.binary.BaseNCodec.EOF;
 import static org.junit.Assert.assertEquals;
 import static org.junit.Assert.assertFalse;
 import static org.junit.Assert.assertThrows;
 import static org.junit.Assert.assertTrue;

 import java.io.ByteArrayInputStream;
 import java.io.ByteArrayOutputStream;
 import java.io.IOException;
 import java.io.InputStream;

 import org.apache.commons.codec.binary.BaseNCodec;
 import org.apache.commons.codec.binary.BaseNCodecInputStream;

 /**
 * Test class for BaseNCodecInputStream
 */
 public class BaseNCodecInputStreamTest {

 private static final ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
 private BaseNCodec baseNCodec;
 private InputStream inputStream;
 private BaseNCodecInputStream baseNCodecInputStream;

 @Before
 public void setUp() throws IOException {
 baseNCodec = new BaseNCodec();
 byteArrayOut.write(new byte[] { (byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x44 });
 byteArrayOut.close();
 inputStream = new ByteArrayInputStream(byteArrayOut.toByteArray());
 }

 @Test
 public void testReadMethod() throws IOException {
 baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
 int read = 0;
 byte testByte = new byte[1];
 while (read != EOF) {
 read = baseNCodecInputStream.read();
 testByte[0] = (byte) read;
 assertEquals((int) testByte[0], read);
 }
 }

 @Test
 public void testReadMethodWithEncode() throws IOException {
 baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);
 int read = 0;
 byte testByte = new byte[1];
 while (read != EOF) {
 read = baseNCodecInputStream.read();
 testByte[0] = (byte) read;
 assertEquals(baseNCodec.encode(testByte[0]), read);
 }
 }

 @Test
 public void testMarkSupportedMethod() {
 baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
 assertFalse(baseNCodecInputStream.markSupported());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMarkSupportedMethodWithNegativeValue() {
 baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
 assertThrows(IllegalArgumentException.class, () -> baseNCodecInputStream.mark((long) -1));
 }

 @Test
 public void testSkipMethod() throws IOException {
 baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
 int skip = 5;
 baseNCodecInputStream.skip(skip);
 assertEquals(skip, baseNCodecInputStream.available());
 }

 @Test
 public void testAvailableMethod() throws IOException {
 baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
 int available = baseNCodecInputStream.available();
 assertEquals(available, 0);
 }

 @Test
 public void testCloseMethod() throws IOException {
 baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
 baseNCodecInputStream.close();
 assertEquals(inputStream.available(), 0);
 }
 }