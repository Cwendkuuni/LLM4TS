import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TarUtilsTest {

 // Test case for parseOctal method
 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6'};
 int offset = 0;
 int length = buffer.length;
 long expected = 0123456L;
 long result = TarUtils.parseOctal(buffer, offset, length);
 assertEquals(expected, result);
 }

 // Test case for parseOctal with invalid input
 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalInvalidInput() {
 byte[] buffer = new byte[]{'0', '1', '2', 'A'}; // 'A' is an invalid octal digit
 int offset = 0;
 int length = buffer.length;
 TarUtils.parseOctal(buffer, offset, length);
 }

 // Test case for parseOctal with buffer length exceeded
 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalBufferLengthExceeded() {
 byte[] buffer = new byte[]{'0'};
 int offset = 0;
 int length = 1;
 TarUtils.parseOctal(buffer, offset, length);
 }

 // Test case for parseName method
 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{'t','a','r','/','.','.'};
 int offset = 0;
 int length = buffer.length;
 String expected = "tar/.";
 String result = TarUtils.parseName(buffer, offset, length);
 assertEquals(expected, result);
 }

 // Test case for parseName with buffer length exceeded
 @Test(expected = IllegalArgumentException.class)
 public void testParseNameBufferLengthExceeded() {
 byte[] buffer = new byte[]{'t','a','r'};
 int offset = 0;
 int length = buffer.length;
 TarUtils.parseName(buffer, offset, length);
 }

 // Test case for formatNameBytes method
 @Test
 public void testFormatNameBytes() {
 String name = "tar/.";
 byte[] buffer = new byte[name.length() + 1];
 int offset = 0;
 int length = buffer.length;
 int actual = TarUtils.formatNameBytes(name, buffer, offset, length);
 assertEquals(offset + length, actual);
 String result = new String(buffer, 0, offset + length);
 assertEquals(name, result);
 }

 // Test case for formatNameBytes with name longer than buffer
 @Test(expected = IllegalArgumentException.class)
 public void testFormatNameBytesNameLongerThanBuffer() {
 String name = "testnamewithlengthlongerthanbuffer";
 byte[] buffer = new byte[name.length()];
 int offset = 0;
 int length = buffer.length;
 TarUtils.formatNameBytes(name, buffer, offset, length);
 }

 // Test case for formatUnsignedOctalString method
 @Test
 public void testFormatUnsignedOctalString() {
 long value = 123456;
 byte[] buffer = new byte[7];
 int offset = 0;
 int length = buffer.length;
 TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
 String expected = "0123456";
 String result = new String(buffer, 0, offset + length);
 assertEquals(expected, result);
 }

 // Test case for formatUnsignedOctalString with value too large for buffer
 @Test(expected = IllegalArgumentException.class)
 public void testFormatUnsignedOctalStringValueTooLarge() {
 long value = 12345678901234567890L;
 byte[] buffer = new byte[10];
 int offset = 0;
 int length = buffer.length;
 TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
 }

 // Test case for formatOctalBytes method
 @Test
 public void testFormatOctalBytes() {
 long value = 123456;
 byte[] buffer = new byte[10];
 int offset = 0;
 int length = buffer.length;
 int actual = TarUtils.formatOctalBytes(value, buffer, offset, length);
 assertEquals(offset + length, actual);
 String result = new String(buffer, 0, offset + length);
 assertEquals("0123456 ", result);
 }

 // Test case for formatOctalBytes with value too large for buffer including trailer
 @Test(expected = IllegalArgumentException.class)
 public void testFormatOctalBytesValueTooLarge() {
 long value = 12345678901234567890L;
 byte[] buffer = new byte[12];
 int offset = 0;
 int length = buffer.length;
 TarUtils.formatOctalBytes(value, buffer, offset, length);
 }

 // Test case for computeCheckSum method
 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{0x75, 0x72, 0x62, 0x61, 0x73, 0x74, 0x65, 0x31, 0x20, 0x63, 0x61, 0x6d, 0x65, 0x64, 0x20, 0x63, 0x72, 0x65, 0x61, 0x74, 0x65, 0x20, 0x68, 0x65, 0x61, 0x64, 0x20, 0x63, 0x6f, 0x6d, 0x70, 0x65, 0x64, 0x69, 0x61, 0x6c, 0x21, 0x00, 0x00};
 long expected = 0x4e9529a5b3497c22L;
 long result = TarUtils.computeCheckSum(buffer);
 assertEquals(expected, result);
 }
}