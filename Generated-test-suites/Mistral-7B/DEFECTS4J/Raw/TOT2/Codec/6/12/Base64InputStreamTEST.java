import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class Base64InputStreamTest {

 private static final byte[] TEST_DATA = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};

 @Test
 public void testDefaultConstruct() {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA));
 assertEquals(TEST_DATA.length, inputStream.available());
 }

 @Test
 public void testConstructWithDecode() {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA), false);
 byte[] output = new byte[TEST_DATA.length];
 int read = inputStream.read(output);
 assertEquals(TEST_DATA.length, read);
 assertArrayEquals(TEST_DATA, output);
 }

 @Test
 public void testConstructWithEncode() {
 Base64OutputStream outputStream = new Base64OutputStream(new ByteArrayOutputStream());
 outputStream.write(TEST_DATA);
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(outputStream.toByteArray()), true);
 byte[] output = new byte[TEST_DATA.length];
 int read = inputStream.read(output);
 assertEquals(TEST_DATA.length, read);
 assertArrayEquals(TEST_DATA, output);
 }

 @Test
 public void testNegativeConstruct() {
 try {
 Base64InputStream inputStream = new Base64InputStream(null, false);
 fail("Expected IllegalArgumentException as inputStream cannot be null.");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }

 try {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA), false, -1, null);
 fail("Expected IllegalArgumentException as lineLength cannot be negative.");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }

 try {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA), false, 0, null);
 fail("Expected IllegalArgumentException as lineSeparator cannot be null.");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testReadMethods() throws IOException {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA), false);
 byte[] output = new byte[TEST_DATA.length];
 int read = inputStream.read(output);
 assertEquals(TEST_DATA.length, read);
 assertArrayEquals(TEST_DATA, output);

 byte singleByte[] = new byte[1];
 int readSingleByte = inputStream.read(singleByte);
 assertEquals(TEST_DATA[0], singleByte[0]);
 assertFalse(inputStream.markSupported());
 assertNull(inputStream.mark(0));
 }

 @Test
 public void testEdgeCases() throws IOException {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA), false);
 byte[] output = new byte[TEST_DATA.length];

 // Test reading boundary values
 int readBoundary = inputStream.read(output);
 assertEquals(TEST_DATA.length, readBoundary);
 int remaining = inputStream.available();
 assertEquals(0, remaining);

 // Test reading empty stream
 ByteArrayInputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
 Base64InputStream emptyBase64InputStream = new Base64InputStream(emptyInputStream, false);
 int readEmpty = emptyBase64InputStream.read(output);
 assertEquals(-1, readEmpty);
 int remainingEmpty = emptyBase64InputStream.available();
 assertEquals(0, remainingEmpty);
 }
}