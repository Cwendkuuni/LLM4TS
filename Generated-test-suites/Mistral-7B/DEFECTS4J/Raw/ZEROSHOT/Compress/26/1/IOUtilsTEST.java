import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class IOUtilsTest {

 @Test
 public void testCopy() throws IOException {
 InputStream input = IOUtils.toInputStream("TestInputData.bin");
 OutputStream output = new ByteArrayOutputStream();
 long bytesCopied = IOUtils.copy(input, output);
 byte[] expectedOutput = IOUtils.toByteArray(input);
 byte[] actualOutput = output.toByteArray();
 assertEquals("Incorrect number of bytes copied", expectedOutput.length, bytesCopied);
 assertArrayEquals("Copied data does not match expected data", expectedOutput, actualOutput);
 }

 @Test(expected = IOException.class)
 public void testCopyWithNullInput() {
 OutputStream output = new ByteArrayOutputStream();
 IOUtils.copy(null, output);
 }

 @Test(expected = IOException.class)
 public void testCopyWithNullOutput() {
 InputStream input = IOUtils.toInputStream("TestInputData.bin");
 IOUtils.copy(input, null);
 }

 @Test
 public void testSkip() throws IOException {
 InputStream input = IOUtils.toInputStream("TestInputData.bin");
 long numToSkip = 5;
 long skipped = IOUtils.skip(input, numToSkip);
 assertEquals("Incorrect number of bytes skipped", numToSkip, skipped);
 }

 @Test
 public void testSkipWithNegativeNumToSkip() {
 InputStream input = IOUtils.toInputStream("TestInputData.bin");
 long numToSkip = -5;
 assertThrows("Expected IOException when skipping a negative number of bytes", IOException.class, () -> IOUtils.skip(input, numToSkip));
 }

 @Test
 public void testSkipWithInputClosed() {
 InputStream input = IOUtils.toInputStream("TestInputData.bin");
 IOUtils.closeQuietly(input);
 long numToSkip = 5;
 assertThrows("Expected IOException when skipping bytes from a closed input stream", IOException.class, () -> IOUtils.skip(input, numToSkip));
 }

 @Test
 public void testReadFully() throws IOException {
 InputStream input = IOUtils.toInputStream("TestInputData.bin");
 byte[] expectedOutput = new byte[5];
 int bytesRead = IOUtils.readFully(input, expectedOutput);
 byte[] actualOutput = new byte[bytesRead];
 System.arraycopy(expectedOutput, 0, actualOutput, 0, bytesRead);
 assertArrayEquals("Read data does not match expected data", expectedOutput, actualOutput);
 }

 @Test(expected = IOException.class)
 public void testReadFullyWithNullInput() {
 byte[] output = new byte[5];
 IOUtils.readFully(null, output);
 }

 @Test(expected = NullPointerException.class)
 public void testReadFullyWithNullOutput() {
 InputStream input = IOUtils.toInputStream("TestInputData.bin");
 IOUtils.readFully(input, null);
 }

 @Test(expected = IOException.class)
 public void testReadFullyWithInputClosed() {
 InputStream input = IOUtils.toInputStream("TestInputData.bin");
 IOUtils.closeQuietly(input);
 byte[] output = new byte[5];
 assertThrows("Expected IOException when reading from a closed input stream", IOException.class, () -> IOUtils.readFully(input, output));
 }

 @Test
 public void testToByteArray() throws IOException {
 InputStream input = IOUtils.toInputStream("TestInputData.bin");
 byte[] expectedOutput = IOUtils.toByteArray(input);
 byte[] actualOutput = IOUtils.toByteArray(input);
 assertArrayEquals("Converted data does not match expected data", expectedOutput, actualOutput);
 }

 @Test(expected = IOException.class)
 public void testToByteArrayWithNullInput() {
 IOUtils.toByteArray(null);
 }

 @Test
 public void testCloseQuietly() {
 InputStream input = IOUtils.toInputStream("TestInputData.bin");
 IOUtils.closeQuietly(input);
 assertNull("Expected InputStream to be closed and null", input);
 }

 @Test
 public void testCloseQuietlyWithNullCloseable() {
 IOUtils.closeQuietly(null);
 }
}