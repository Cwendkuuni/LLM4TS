import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.ByteArrayInputStream;
import org.apache.commons.io.ByteArrayOutputStream;
import org.junit.Test;
import static org.junit.Assert.*;

public class IOUtilsTest {

 @Test
 public void testCopy() throws IOException {
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 InputStream input = new ByteArrayInputStream(new byte[]{"A"});
 long count = IOUtils.copy(input, output);
 byte[] expected = new byte[]{"A"};
 byte[] actual = output.toByteArray();
 assertArrayEquals(expected, actual);
 assertEquals(1, count);
 }

 @Test
 public void testCopyWithBufferSize() throws IOException {
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 InputStream input = new ByteArrayInputStream(new byte[]{"A", "B", "C"});
 long count = IOUtils.copy(input, output, 1);
 byte[] expected = new byte[]{"A", "B", "C"};
 byte[] actual = output.toByteArray();
 assertArrayEquals(expected, actual);
 assertEquals(3, count);
 }

 @Test
 public void testSkip() throws IOException {
 InputStream input = new ByteArrayInputStream(new byte[]{"A", "B", "C", "D"});
 long numToSkip = 3;
 long skipped = IOUtils.skip(input, numToSkip);
 assertEquals(1, skipped);
 byte[] expected = new byte[]{"D"};
 byte[] actual = new ByteArrayOutputStream().toByteArray(IOUtils.toByteArray(input));
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testSkipLessThanRequested() throws IOException {
 InputStream input = new ByteArrayInputStream(new byte[]{"A", "B", "C", "D"});
 long numToSkip = 4;
 long skipped = IOUtils.skip(input, numToSkip);
 assertEquals(3, skipped);
 byte[] expected = new byte[]{"D"};
 byte[] actual = new ByteArrayOutputStream().toByteArray(IOUtils.toByteArray(input));
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testReadFully() throws IOException {
 InputStream input = new ByteArrayInputStream(new byte[]{"A", "B", "C"});
 byte[] expected = new byte[]{"A", "B", "C"};
 byte[] result = new byte[3];
 IOUtils.readFully(input, result);
 assertArrayEquals(expected, result);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testReadFullyNegativeLength() throws IOException {
 InputStream input = new ByteArrayInputStream(new byte[]{"A", "B", "C"});
 byte[] b = new byte[2];
 IOUtils.readFully(input, b, -1, 2);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testReadFullyNegativeOffset() throws IOException {
 InputStream input = new ByteArrayInputStream(new byte[]{"A", "B", "C"});
 byte[] b = new byte[3];
 IOUtils.readFully(input, b, 0, -1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testReadFullyTooLongArray() throws IOException {
 InputStream input = new ByteArrayInputStream(new byte[]{"A", "B", "C"});
 byte[] b = new byte[4];
 IOUtils.readFully(input, b);
 }

 @Test
 public void testToByteArray() throws IOException {
 InputStream input = new ByteArrayInputStream(new byte[]{"A", "B", "C"});
 byte[] expected = new byte[]{"A", "B", "C"};
 byte[] actual = IOUtils.toByteArray(input);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testToByteArrayEmptyStream() throws IOException {
 InputStream input = new ByteArrayInputStream(new byte[0]);
 byte[] expected = new byte[0];
 byte[] actual = IOUtils.toByteArray(input);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testCloseQuietly() {
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 try {
 IOUtils.copy(new ByteArrayInputStream(new byte[0]), output);
 IOUtils.closeQuietly(output);
 } catch (IOException e) {
 fail("IOException was not swallowed: " + e.getMessage());
 }
 }
}