import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TarUtilsTest {

 @Test
 public void testParseOctal_ValidInput() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', '7', '7'};
 int offset = 0;
 int length = 6;
 long expectedResult = 0175577L;
 long actualResult = TarUtils.parseOctal(buffer, offset, length);
 Assertions.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testParseOctal_TrailingSpace() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', '7', '7', ' '};
 int offset = 0;
 int length = 7;
 long expectedResult = 0175577L;
 long actualResult = TarUtils.parseOctal(buffer, offset, length);
 Assertions.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testParseOctal_LeadingSpace() {
 byte[] buffer = new byte[]{' ', '0', '7', '5', '5', '7', '7'};
 int offset = 0;
 int length = 7;
 long expectedResult = 0175577L;
 long actualResult = TarUtils.parseOctal(buffer, offset, length);
 Assertions.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testParseOctal_InvalidInput() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', '7', '8'}; // invalid byte 8
 int offset = 0;
 int length = 6;
 Assertions.assertThrows(IllegalArgumentException.class, () -> {
 TarUtils.parseOctal(buffer, offset, length);
 });
 }

 @Test
 public void testParseOctal_EmptyInput() {
 byte[] buffer = new byte[0];
 int offset = 0;
 int length = 0;
 long expectedResult = 0L;
 long actualResult = TarUtils.parseOctal(buffer, offset, length);
 Assertions.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testParseName_ValidInput() {
 byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'x', 't'};
 int offset = 0;
 int length = 12;
 String expectedResult = "tar.txt";
 String actualResult = TarUtils.parseName(buffer, offset, length);
 Assertions.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testParseName_TrailingNull() {
 byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'x', 't', '\0'};
 int offset = 0;
 int length = 13;
 String expectedResult = "tar.txt";
 String actualResult = TarUtils.parseName(buffer, offset, length);
 Assertions.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testParseName_EmptyInput() {
 byte[] buffer = new byte[0];
 int offset = 0;
 int length = 0;
 String expectedResult = "";
 String actualResult = TarUtils.parseName(buffer, offset, length);
 Assertions.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFormatNameBytes_ValidInput() {
 String name = "tar.gz";
 byte[] buf = new byte[11];
 int offset = 0;
 int length = 10;
 int expectedResult = formatNameBytes(name, buf, offset, length);
 Assertions.assertEquals(expectedResult, offset + length);
 String actualResult = new String(buf, 0, length);
 Assertions.assertEquals(name, actualResult);
 }

 @Test
 public void testFormatNameBytes_InputLongerThanBuffer() {
 String name = "tar.gz.backup.2021-12-31";
 byte[] buf = new byte[10];
 int offset = 0;
 int length = 10;
 int expectedResult = formatNameBytes(name, buf, offset, length);
 Assertions.assertEquals(expectedResult, offset + length);
 String actualResult = new String(buf, 0, length);
 Assertions.assertEquals("tar.gz...", actualResult);
 }

 @Test
 public void testFormatUnsignedOctalString_ValidInput() {
 long value = 1234567L;
 byte[] buffer = new byte[10];
 int offset = 0;
 int length = 7;
 TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
 String expectedResult = "0126367";
 String actualResult = new String(buffer, 0, length);
 Assertions.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testFormatUnsignedOctalString_ValueTooLong() {
 long value = 1234567890L;
 byte[] buffer = new byte[10];
 int offset = 0;
 int length = 7;
 Assertions.assertThrows(IllegalArgumentException.class, () -> {
 TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
 });
 }

 @Test
 public void testComputeCheckSum() {
 byte[] buf = new byte[]{'T', 'a', 'r', ' ', 'h', 'e', 'a', 'd', 'e', 'r', ' ', 'C', 'h', 'e', 'c', 'k', 's', 'u', 'm'};
 long expectedResult = 1157749437L;
 long actualResult = TarUtils.computeCheckSum(buf);
 Assertions.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testComputeCheckSum_EmptyInput() {
 byte[] buf = new byte[0];
 long expectedResult = 0L;
 long actualResult = TarUtils.computeCheckSum(buf);
 Assertions.assertEquals(expectedResult, actualResult);
 }
}