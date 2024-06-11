import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{'0', '7', '7', '5', '0', '0', '0'};
 long result = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(07755L, result);

 buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6'};
 try {
 result = TarUtils.parseOctal(buffer, 0, buffer.length);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 buffer = new byte[]{'1', '2', '3'};
 try {
 result = TarUtils.parseOctal(buffer, 0, buffer.length);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{(byte) 0x54, (byte) 0x68, (byte) 0x69, (byte) 0x73, (byte) 0x73, (byte) 0x20, (byte) 0x61, (byte) 0x72, (byte) 0x72, (byte) 0x65, (byte) 0x74, (byte) 0x65, (byte) 0x64, (byte) 0x20, (byte) 0x74, (byte) 0x65, (byte) 0x73, (byte) 0x74, (byte) 0x20, (byte) 0x75, (byte) 0x73, (byte) 0x68, (byte) 0x6F, (byte) 0x72, (byte) 0x73, (byte) 0x20, (byte) 0x65, (byte) 0x78, (byte) 0x65, (byte) 0x78, (byte) 0x20, (byte) 0x69, (byte) 0x6E, (byte) 0x66, (byte) 0x6F, (byte) 0x20, (byte) 0x63, (byte) 0x61, (byte) 0x6D, (byte) 0x65, (byte) 0x20, (byte) 0x61, (byte) 0x73, (byte) 0x63, (byte) 0x61, (byte) 0x6E, (byte) 0x6E, (byte) 0x6F, (byte) 0x74, (byte) 0x20, (byte) 0x74, (byte) 0x68, (byte) 0x69, (byte) 0x73, (byte) 0x73, (byte) 0x61, (byte) 0x72, (byte) 0x72, (byte) 0x65, (byte) 0x20, (byte) 0x63, (byte) 0x72, (byte) 0x65, (byte) 0x61, (byte) 0x74, (byte) 0x65, (byte) 0x5C, (byte) 0x68, (byte) 0x65, (byte) 0x61, (byte) 0x64, (byte) 0x20, (byte) 0x70, (byte) 0x61, (byte) 0x72, (byte) 0x73, (byte) 0x20, (byte) 0x74, (byte) 0x68, (byte) 0x69, (byte) 0x73, (byte) 0x73, (byte) 0x61, (byte) 0x72, (byte) 0x72, (byte) 0x65, (byte) 0x20, (byte) 0x6D, (byte) 0x79, (byte) 0x20, (byte) 0x63, (byte) 0x61, (byte) 0x6E, (byte) 0x6E, (byte) 0x6F, (byte) 0x6D, (byte) 0x65, (byte) 0x20, (byte) 0x66, (byte) 0x6F, (byte) 0x6F, (byte) 0x2E};
 String expected = "This is a tar file.";
 String result = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatNameBytes() {
 String name = "This is a tar file.";
 byte[] buffer = new byte[512];
 int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 byte[] actual = new byte[offset];
 System.arraycopy(buffer, 0, actual, 0, offset);
 String result = new String(actual, 0, offset);
 assertEquals("This is a tar file.", result);
 }

 @Test
 public void testFormatUnsignedOctalString() {
 long value = 0x12345678L;
 byte[] buffer = new byte[13];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 assertEquals("017\u000776543", new String(buffer, 0, buffer.length));
 }

 @Test
 public void testFormatOctalBytes() {
 long value = 0x12345678L;
 byte[] buffer = new byte[13];
 TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 assertEquals("017\u000776543 ", new String(buffer, 0, buffer.length));
 }

 @Test
 public void testFormatLongOctalBytes() {
 long value = 0x1234567890123456L;
 byte[] buffer = new byte[13];
 TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
 assertEquals("017\u000776543 01553424556", new String(buffer, 0, buffer.length));
 }

 @Test
 public void testFormatCheckSumOctalBytes() {
 long value = 0x1234567890123456L;
 byte[] buffer = new byte[13];
 TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
 assertEquals("017\u000776543 000", new String(buffer, 0, buffer.length));
 }

 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{'H', 'e', 'a', 'd', 'e', 'r', 0, 0, 0, 'T', 'a', 'r'};
 long computed = TarUtils.computeCheckSum(buffer);
 long expected = 0x68656C6C6F63616D65L;
 assertEquals(expected, computed);
 }
}