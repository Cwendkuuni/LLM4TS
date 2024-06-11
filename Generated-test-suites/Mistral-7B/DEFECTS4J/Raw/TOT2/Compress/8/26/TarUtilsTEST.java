import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

 @Test
 public void testParseOctal_ValidInput() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', '6', '4'};
 long expected = 0175564L;
 long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctal_InvalidInput_MissingTrailingNull() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', '6', '4'};
 int offset = 0;
 int length = 5;
 TarUtils.parseOctal(buffer, offset, length);
 fail("Expected IllegalArgumentException not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctal_InvalidInput_InvalidByte() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', '6', '8'};
 int offset = 0;
 int length = 6;
 String exceptionMessage = TarUtils.exceptionMessage(buffer, offset, length, 5, (byte) 8);
 String expectedMessage = "Invalid byte 8 at offset 5 in '...";
 assertEquals(expectedMessage, exceptionMessage);
 }

 @Test
 public void testParseName_ValidInput() {
 byte[] buffer = new byte[]{(byte) 0x54, (byte) 0x65, (byte) 0x73, (byte) 0x74, (byte) 0x61, (byte) 0x72, (byte) 0x74, 0};
 String expected = "Data";
 String actual = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatNameBytes_ValidInput() {
 String name = "MyFile.txt";
 byte[] buffer = new byte[name.length() + 1];
 int offset = 0;
 int length = name.length() + 1;
 TarUtils.formatNameBytes(name, buffer, offset, length);
 String actual = new String(buffer, 0, length);
 String expected = "MyFile.txt";
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatUnsignedOctalString_ValidInput() {
 long value = 01234567L;
 byte[] buffer = new byte[9];
 int offset = 0;
 int length = 8;
 TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
 String expected = "0123456";
 for (int i = 0; i < 8; ++i) {
 assertEquals((byte) (expected.charAt(i) & 0xFF), buffer[i]);
 }
 }

 @Test
 public void testFormatOctalBytes_ValidInput() {
 long value = 01234567L;
 byte[] buffer = new byte[10];
 int offset = 0;
 int length = 9;
 TarUtils.formatOctalBytes(value, buffer, offset, length);
 String expected = "0123456\t\0";
 for (int i = 0; i < 10; ++i) {
 assertEquals(expected.charAt(i), (char) (buffer[i] & 0xFF));
 }
 }

 @Test
 public void testComputeCheckSum_ValidInput() {
 byte[] buffer = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05};
 long checkSum = TarUtils.computeCheckSum(buffer);
 long expected = 0x0b9d3f24L;
 assertEquals(expected, checkSum);
 }
}