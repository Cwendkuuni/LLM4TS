import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.ByteArrayOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class IOUtilsTest {

 private InputStream input;
 private OutputStream output;
 private ByteArrayOutputStream byteArrayOutput;

 @Before
 public void setUp() throws Exception {
 input = IOUtils.class.getResourceAsStream("/testFile.txt");
 output = new ByteArrayOutputStream();
 byteArrayOutput = new ByteArrayOutputStream();
 }

 @After
 public void tearDown() throws Exception {
 IOUtils.closeQuietly(input);
 IOUtils.closeQuietly(output);
 IOUtils.closeQuietly(byteArrayOutput);
 }

 @Test
 public void testCopy() throws Exception {
 byte[] expected = {"Hello World".getBytes()};
 long actual = IOUtils.copy(input, output);
 assertEquals(expected.length, actual);
 assertEquals(expected, output.toByteArray());
 }

 @Test
 public void testCopyBufferSize() throws Exception {
 byte[] expected = {"Hello World".getBytes()};
 long actual = IOUtils.copy(input, output, 50);
 assertEquals(expected.length, actual);
 assertEquals(expected, output.toByteArray());
 }

 @Test(expected = NullPointerException.class)
 public void testCopyNullInputStream() {
 long actual = IOUtils.copy(null, output);
 fail("Expected NullPointerException but got " + actual);
 }

 @Test(expected = NullPointerException.class)
 public void testCopyNullOutputStream() {
 long actual = IOUtils.copy(input, null);
 fail("Expected NullPointerException but got " + actual);
 }

 @Test
 public void testSkip() throws Exception {
 long numToSkip = 5;
 long actual = IOUtils.skip(input, numToSkip);
 assertEquals(numToSkip, actual);
 }

 @Test
 public void testSkipFail() throws Exception {
 InputStream inputNull = null;
 long numToSkip = 5;
 long actual = IOUtils.skip(inputNull, numToSkip);
 assertEquals(0, actual);
 }

 @Test
 public void testReadFully() throws Exception {
 byte[] expected = {"Hello World".getBytes()};
 byte[] actual = new byte[expected.length];
 IOUtils.readFully(input, actual);
 assertEquals(expected, actual);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testReadFullyNegativeOffset() throws Exception {
 byte[] expected = {"Hello World".getBytes()};
 byte[] actual = new byte[expected.length];
 IOUtils.readFully(input, actual, -1, expected.length);
 fail("Expected IndexOutOfBoundsException but got " + actual);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testReadFullyNegativeLength() throws Exception {
 byte[] expected = {"Hello World".getBytes()};
 byte[] actual = new byte[expected.length];
 IOUtils.readFully(input, actual, 0, -1);
 fail("Expected IndexOutOfBoundsException but got " + actual);
 }

 @Test
 public void testReadFullyPartialFill() throws Exception {
 byte[] expected = {"Hello World".getBytes()};
 byte[] actual = new byte[expected.length + 1];
 IOUtils.readFully(input, actual, 0, expected.length);
 assertArrayEquals(expected, Arrays.copyOfRange(actual, 0, expected.length));
 }

 @Test
 public void testReadFullyNullInputStream() throws Exception {
 byte[] expected = {"Hello World".getBytes()};
 byte[] actual = new byte[expected.length];
 IOUtils.readFully(null, actual);
 fail("Expected NullPointerException but got " + actual);
 }

 @Test
 public void testReadFullyNullOutput() throws Exception {
 byte[] expected = {"Hello World".getBytes()};
 byte[] actual = new byte[expected.length];
 IOUtils.readFully(input, null);
 fail("Expected NullPointerException but got " + actual);
 }

 @Test
 public void testToByteArray() throws Exception {
 byte[] expected = {"Hello World".getBytes()};
 byte[] actual = IOUtils.toByteArray(input);
 assertArrayEquals(expected, actual);
 }

 @Test(expected = NullPointerException.class)
 public void testToByteArrayNullInputStream() throws Exception {
 byte[] actual = IOUtils.toByteArray(null);
 fail("Expected NullPointerException but got " + actual);
 }

 @Test
 public void testCloseQuietly() throws Exception {
 InputStream inputTest = new ByteArrayInputStream("Test".getBytes());
 try {
 IOUtils.copy(inputTest, byteArrayOutput);
 IOUtils.closeQuietly(inputTest);
 byte[] actual = byteArrayOutput.toByteArray();
 assertEquals("Test", new String(actual));
 } finally {
 IOUtils.closeQuietly(byteArrayOutput);
 }
 }

 @Test(expected = IOException.class)
 public void testCloseQuietlyIOException() throws Exception {
 InputStream inputTest = new ByteArrayInputStream(new byte[2]) {
 @Override
 public void close() throws IOException {
 throw new IOException("Test");
 }
 };
 IOUtils.closeQuietly(inputTest);
 fail("Expected IOException but didn't get one.");
 }
}