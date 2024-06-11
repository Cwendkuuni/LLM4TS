import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

public class BaseNCodecInputStreamTest {

 private BaseNCodecInputStream inputStream;
 private BaseNCodec baseNCodec;
 private ByteArrayOutputStream outputStream;
 private ByteArrayInputStream inputStreamForTesting;

 @Before
 public void setUp() {
 baseNCodec = new BaseNCodec();
 outputStream = new ByteArrayOutputStream();
 inputStreamForTesting = new ByteArrayInputStream(outputStream.toByteArray());
 inputStream = new BaseNCodecInputStream(inputStreamForTesting, baseNCodec, false);
 }

 @Test
 public void testReadMethod() throws IOException {
 byte[] expectedBytes = new byte[]{9};
 outputStream.write(expectedBytes);
 outputStream.flush();

 int actualByte = inputStream.read();
 byte[] actualByteAsArray = new byte[]{actualByte};

 assertEquals(expectedBytes[0], actualByteAsArray[0]);
 assertFalse(inputStream.hasReachedEndOfStream());
 }

 @Test
 public void testReadMethod_ReachesEndOfStream() throws IOException {
 baseNCodec.setHasData(false);

 int actualByte = inputStream.read();
 assertEquals(-1, actualByte);
 assertTrue(inputStream.hasReachedEndOfStream());
 }

 @Test(expected = NullPointerException.class)
 public void testReadMethod_NullByteArray() {
 inputStream.read(null, 0, 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testReadMethod_NegativeOffset() {
 inputStream.read(new byte[1], -1, 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testReadMethod_NegativeLength() {
 inputStream.read(new byte[1], 0, -1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testReadMethod_NegativeOffsetAndLength() {
 inputStream.read(new byte[1], -1, -1);
 }

 @Test(expected = IOException.class)
 public void testReadMethod_IOException() throws IOException {
 byte[] expectedBytes = new byte[]{9};
 outputStream.write(expectedBytes);
 outputStream.flush();
 inputStream.read(new byte[1], 0, 1);
 inputStream.mark(512);
 assertThrows(IOException.class, () -> inputStream.read(new byte[1], 0, 1));
 assertFalse(inputStream.markSupported());
 }

 @Test
 public void testReadMethod_ReadMultipleBytes() throws IOException {
 byte[] expectedBytes = new byte[]{1, 2, 3};
 outputStream.write(expectedBytes);
 outputStream.flush();

 byte[] actualBytes = new byte[3];
 int actualNumberOfBytesRead = inputStream.read(actualBytes, 0, 3);

 assertEquals(expectedBytes[0], actualBytes[0]);
 assertEquals(expectedBytes[1], actualBytes[1]);
 assertEquals(expectedBytes[2], actualBytes[2]);
 assertEquals(3, actualNumberOfBytesRead);
 assertFalse(inputStream.hasReachedEndOfStream());
 }

 @Test
 public void testReadMethod_ReadLessThanExpectedBytes() throws IOException {
 byte[] expectedBytes = new byte[]{1, 2, 3, 4, 5};
 outputStream.write(expectedBytes, 0, expectedBytes.length);
 outputStream.flush();

 byte[] actualBytes = new byte[4];
 int actualNumberOfBytesRead = inputStream.read(actualBytes, 0, 4);

 assertEquals(expectedBytes[0], actualBytes[0]);
 assertEquals(expectedBytes[1], actualBytes[1]);
 assertEquals(expectedBytes[2], actualBytes[2]);
 assertEquals(expectedBytes[3], actualBytes[3]);
 assertEquals(4, actualNumberOfBytesRead);
 assertFalse(inputStream.hasReachedEndOfStream());
 }

 @Test
 public void testReadMethod_ReadOneByteLessThanExpectedBytes() throws IOException {
 byte[] expectedBytes = new byte[]{1, 2, 3, 4, 5};
 outputStream.write(expectedBytes, 0, expectedBytes.length);
 outputStream.flush();

 byte[] actualBytes = new byte[5];
 int actualNumberOfBytesRead = inputStream.read(actualBytes, 0, 5);

 assertEquals(expectedBytes[0], actualBytes[0]);
 assertEquals(expectedBytes[1], actualBytes[1]);
 assertEquals(expectedBytes[2], actualBytes[2]);
 assertEquals(expectedBytes[3], actualBytes[3]);
 assertEquals(expectedBytes.length - 1, actualNumberOfBytesRead);
 assertFalse(inputStream.hasReachedEndOfStream());
 }

 @Test
 public void testReadMethod_ReadOneByteMoreThanExpectedBytes() throws IOException {
 byte[] expectedBytes = new byte[]{1, 2, 3, 4, 5};
 outputStream.write(expectedBytes, 0, expectedBytes.length);
 outputStream.flush();

 byte[] actualBytes = new byte[6];
 int actualNumberOfBytesRead = inputStream.read(actualBytes, 0, 5);

 assertEquals(expectedBytes[0], actualBytes[0]);
 assertEquals(expectedBytes[1], actualBytes[1]);
 assertEquals(expectedBytes[2], actualBytes[2]);
 assertEquals(expectedBytes[3], actualBytes[3]);
 assertEquals(expectedBytes[4], actualBytes[4]);
 assertEquals(5, actualNumberOfBytesRead);
 assertFalse(inputStream.hasReachedEndOfStream());
 }

 @Test
 public void testReadAllBytesMethod() throws IOException {
 byte[] expectedBytes = new byte[]{1, 2, 3, 4, 5};
 outputStream.write(expectedBytes, 0, expectedBytes.length);
 outputStream.flush();

 byte[] actualBytes = inputStream.readAllBytes();

 assertEquals(expectedBytes.length, actualBytes.length);
 assertArrayEquals(expectedBytes, actualBytes);
 assertFalse(inputStream.hasReachedEndOfStream());
 }

 @Test
 public void testReadAllBytesMethod_EndOfStream() throws IOException {
 baseNCodec.setHasData(false);

 byte[] actualBytes = inputStream.readAllBytes();
 assertEquals(0, actualBytes.length);
 assertTrue(inputStream.hasReachedEndOfStream());
 }

 @Test
 public void testSkipMethod() throws IOException {
 long numberOfBytesToSkip = 5;
 int numberOfBytesSkipped = inputStream.skip(numberOfBytesToSkip);

 assertEquals(numberOfBytesToSkip, numberOfBytesSkipped);
 assertFalse(inputStream.hasReachedEndOfStream());
 }

 @Test
 public void testSkipMethod_EndOfStream() throws IOException {
 long numberOfBytesToSkip = 5;
 inputStream.mark(512);
 baseNCodec.setHasData(false);

 int numberOfBytesSkipped = inputStream.skip(numberOfBytesToSkip);
 assertEquals(numberOfBytesToSkip, numberOfBytesSkipped);
 assertTrue(inputStream.hasReachedEndOfStream());
 }

 @Test(expected = NullPointerException.class)
 public void testSkipMethod_NullByteArray() {
 inputStream.skip(new byte[1], 0, 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSkipMethod_NegativeLength() {
 inputStream.skip(1, -1, 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSkipMethod_NegativeOffset() {
 inputStream.skip(-1, 1, 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSkipMethod_NegativeOffsetAndLength() {
 inputStream.skip(-1, -1, 1);
 }

 @Test(expected = IOException.class)
 public void testSkipMethod_IOException() throws IOException {
 inputStream.mark(512);
 assertThrows(IOException.class, () -> inputStream.skip(1));
 assertFalse(inputStream.markSupported());
 }

 @Test
 public void testAvailableMethod() throws IOException {
 byte[] expectedBytes = new byte[]{1, 2, 3, 4, 5};
 outputStream.write(expectedBytes);
 outputStream.flush();

 int actualNumberOfAvailableBytes = inputStream.available();

 assertEquals(expectedBytes.length - (inputStream.read(new byte[expectedBytes.length], 0, expectedBytes.length)
 .length), actualNumberOfAvailableBytes);
 }

 @Test
 public void testAvailableMethod_EndOfStream() throws IOException {
 baseNCodec.setHasData(false);

 int actualNumberOfAvailableBytes = inputStream.available();
 assertEquals(0, actualNumberOfAvailableBytes);
 assertTrue(inputStream.hasReachedEndOfStream());
 }
}