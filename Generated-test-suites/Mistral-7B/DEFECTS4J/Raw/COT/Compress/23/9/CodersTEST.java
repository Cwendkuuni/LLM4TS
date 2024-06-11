import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.Test;
import static org.junit.Assert.*;

public class CodersTest {

 @Test
 public void testAddDecoderWithSupportedMethod() throws Exception {
 InputStream is = new ByteArrayInputStream(new byte[0]);
 SevenZMethod method = SevenZMethod.DEFLATE;
 Coder coder = new DeflateDecoder();
 byte[] password = new byte[0];
 InputStream expectedOutput = is; // No change for supported method
 InputStream actualOutput = Coders.addDecoder(is, coder, password);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = IOException.class)
 public void testAddDecoderWithUnsupportedMethod() throws Exception {
 InputStream is = new ByteArrayInputStream(new byte[0]);
 SevenZMethod method = new SevenZMethod(0x12345678, 0);
 Coder coder = new CopyDecoder();
 byte[] password = new byte[0];
 Coders.addDecoder(is, coder, password);
 fail("Should throw IOException for unsupported method");
 }

 @Test
 public void testAddEncoderWithSupportedMethod() throws Exception {
 OutputStream out = new ByteArrayOutputStream();
 SevenZMethod method = SevenZMethod.DEFLATE;
 Coder coder = new DeflateEncoder();
 byte[] password = new byte[0];
 OutputStream expectedOutput = out; // No change for supported method
 OutputStream actualOutput = Coders.addEncoder(out, method, password);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = IOException.class)
 public void testAddEncoderWithUnsupportedMethod() throws Exception {
 OutputStream out = new ByteArrayOutputStream();
 SevenZMethod method = new SevenZMethod(0x12345678, 0);
 Coder coder = new CopyEncoder();
 byte[] password = new byte[0];
 Coders.addEncoder(out, method, password);
 fail("Should throw IOException for unsupported method");
 }

 @Test
 public void testLZMADecoderWithSmallDictionary() throws Exception {
 byte[] input = new byte[] {0x00, 0x01, 0x02, 0x03, 0x00, 0x01};
 InputStream is = new ByteArrayInputStream(input);
 Coder coder = new LZMADecoder();
 byte[] password = new byte[0];
 InputStream expectedOutput = new ByteArrayInputStream(new byte[] {0x00, 0x01, 0x02, 0x03, 0x03, 0x01, 0x02, 0x00});
 InputStream actualOutput = Coders.addDecoder(is, coder, password);
 byte[] outputBytes = new byte[8];
 int length = actualOutput.read(outputBytes, 0, outputBytes.length);
 assertEquals(expectedOutput, actualOutput);
 assertArrayEquals(Arrays.copyOfRange(outputBytes, 0, length), new byte[] {0x03, 0x01, 0x02, 0x00});
 }

 @Test(expected = IOException.class)
 public void testLZMADecoderWithTooLargeDictionary() throws Exception {
 byte[] input = new byte[LZMAInputStream.DICT_SIZE_MAX + 1];
 InputStream is = new ByteArrayInputStream(input);
 Coder coder = new LZMADecoder();
 byte[] password = new byte[0];
 try {
 Coders.addDecoder(is, coder, password);
 fail("Should throw IOException for dictionary larger than 4GiB maximum size");
 } catch (IOException e) {
 // Expected
 }
 }

}