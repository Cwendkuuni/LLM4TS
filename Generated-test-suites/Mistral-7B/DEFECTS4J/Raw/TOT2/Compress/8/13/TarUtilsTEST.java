import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

 // Test case for parseOctal method
 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', '3', '4', '1', '0'};
 long expected = 017553410L;
 long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 // Test case for parseOctal with invalid byte
 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalInvalidByte() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', '3', 'A', '1', '0'}; // 'A' is not a valid octal digit
 long offset = 0;
 int length = buffer.length;
 TarUtils.parseOctal(buffer, offset, length);
 }

 // Test case for parseOctal with missing trailing space or NUL
 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalMissingTrailing() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', '3', '4', '1'};
 int offset = 0;
 int length = buffer.length;
 TarUtils.parseOctal(buffer, offset, length);
 }

 // Test case for parseName method
 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{'.' , 'a', '.' , 'b', '.' , 'c', '/' , 0};
 String expected = "a.b.c";
 String actual = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 // Test case for parseName with invalid byte
 @Test(expected = IllegalArgumentException.class)
 public void testParseNameInvalidByte() {
 byte[] buffer = new byte[]{'.' , 'a', '.' , 'b', '.' , 'c', 'x', 0}; // 'x' is not a valid name character
 int offset = 0;
 int length = buffer.length;
 TarUtils.parseName(buffer, offset, length);
 }

 // Test case for formatNameBytes method
 @Test
 public void testFormatNameBytes() {
 String name = "a.b.c";
 byte[] buffer = new byte[20];
 int offset = 0;
 int length = name.length() > buffer.length ? buffer.length : name.length();
 TarUtils.formatNameBytes(name, buffer, offset, length);
 String actual = new String(buffer, 0, length);
 String expected = "a.b.c";
 assertEquals(expected, actual);
 }

 // Test case for formatUnsignedOctalString method
 @Test
 public void testFormatUnsignedOctalString() {
 long value = 1234567890L;
 byte[] buffer = new byte[10];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 String expected = "01234567541";
 String actual = new String(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 // Test case for formatOctalBytes method
 @Test
 public void testFormatOctalBytes() {
 long value = 1234567890L;
 byte[] buffer = new byte[12];
 int offset = 0;
 int length = buffer.length;
 TarUtils.formatOctalBytes(value, buffer, offset, length);
 String actual = new String(buffer, 0, length);
 String expected = "01234567541 ";
 assertEquals(expected, actual);
 }

 // Test case for formatCheckSumOctalBytes method
 @Test
 public void testFormatCheckSumOctalBytes() {
 long value = 1234567890L;
 byte[] buffer = new byte[12];
 int offset = 0;
 int length = buffer.length;
 TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
 String actual = new String(buffer, 0, length);
 String expected = "01234567541  ";
 assertEquals(expected, actual);
 }

 // Test case for computeCheckSum method
 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{0x75, 0x72, 0x70, 0x65, 0x6e, 0x63, 0x6f, 0x6d, 0x65, 0x6e, 0x74, 0x73, 0x74, 0x50, 0x61, 0x73, 0x70, 0x5c, 0x61, 0x72, 0x72, 0x65, 0x64, 0x69, 0x73, 0x74, 0x31, 0x2e, 0x31, 0x2e, 0x32, 0x0};
 long expected = 32178164415L;
 long actual = TarUtils.computeCheckSum(buffer);
 assertEquals(expected, actual);
 }

}