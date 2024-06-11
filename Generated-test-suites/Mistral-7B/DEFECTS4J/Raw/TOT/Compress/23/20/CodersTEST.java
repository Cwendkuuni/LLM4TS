import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class CodersTest {

 @Test
 public void testAddDecoder_COPY() throws Exception {
 InputStream input = TestUtils.getResourceAsStream("testData/sample.tar.gz");
 SevenZMethod method = SevenZMethod.COPY;
 Coder coder = new CopyDecoder();
 byte[] password = new byte[0];
 InputStream expectedOutput = input;
 InputStream actualOutput = Coders.addDecoder(input, coder, password);
 TestUtils.assertInputStreamEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testAddDecoder_LZMA() throws Exception {
 InputStream input = TestUtils.getResourceAsStream("testData/sample.tar.lzma");
 SevenZMethod method = SevenZMethod.LZMA;
 Coder coder = new LZMADecoder();
 byte[] password = new byte[0];
 InputStream expectedOutput = TestUtils.getResourceAsStream("testData/sample.tar");
 InputStream actualOutput = Coders.addDecoder(input, coder, password);
 TestUtils.assertInputStreamEquals(expectedOutput, actualOutput);
 }

 @Test(expected = IOException.class)
 public void testAddDecoder_UNSUPPORTED() throws Exception {
 InputStream input = TestUtils.getResourceAsStream("testData/sample.tar.gz");
 SevenZMethod method = new SevenZMethod(-1);
 Coder coder = new DeflateDecoder();
 byte[] password = new byte[0];
 Coders.addDecoder(input, coder, password);
 fail("Should throw IOException for unsupported compression method");
 }

 @Test
 public void testAddEncoder_DEFLATE() throws Exception {
 OutputStream output = TestUtils.getOutputStream("testData/output.gz");
 SevenZMethod method = SevenZMethod.DEFLATE;
 Coder coder = new DeflateDecoder();
 byte[] password = new byte[0];
 Coders.addEncoder(output, method, password);
 String expectedContent = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
 output.write(expectedContent.getBytes());
 output.close();
 InputStream input = TestUtils.getResourceAsStream("testData/output.gz");
 byte[] actualContent = new byte[expectedContent.length()];
 input.read(actualContent);
 input.close();
 assertArrayEquals(expectedContent.getBytes(), actualContent);
 }

 @Test
 public void testAddEncoder_UNSUPPORTED() throws Exception {
 OutputStream output = TestUtils.getOutputStream("testData/output.bz2");
 SevenZMethod method = SevenZMethod.DEFLATE;
 Coder coder = new BZIP2Decoder();
 byte[] password = new byte[0];
 try {
 Coders.addEncoder(output, method, password);
 fail("Should throw IOException for unsupported compression method");
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testDecodePasswordProtectedLZMA() throws Exception {
 InputStream input = TestUtils.getResourceAsStream("testData/passwordProtected.tar.lzma");
 SevenZMethod method = SevenZMethod.LZMA;
 Coder coder = new LZMADecoder();
 byte[] password = "password123".getBytes();
 InputStream output = Coders.addDecoder(input, coder, password);
 TestUtils.assertInputStreamEquals(TestUtils.getResourceAsStream("testData/decrypted.tar"), output);
 }

 @Test(expected = IOException.class)
 public void testDecodePasswordProtectedLZMA_WRONG_PASSWORD() throws Exception {
 InputStream input = TestUtils.getResourceAsStream("testData/passwordProtected.tar.lzma");
 SevenZMethod method = SevenZMethod.LZMA;
 Coder coder = new LZMADecoder();
 byte[] password = "wrongPassword".getBytes();
 Coders.addDecoder(input, coder, password);
 fail("Should throw IOException for incorrect password");
 }
}