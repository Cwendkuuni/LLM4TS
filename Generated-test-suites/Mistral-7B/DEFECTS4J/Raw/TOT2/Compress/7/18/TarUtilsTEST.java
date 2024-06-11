import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

 // Test case for parseOctal method
 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', '7', '7', '5', '5', '0'};
 int offset = 0;
 int length = buffer.length;
 long expectedResult = 0175577550L;
 long result = TarUtils.parseOctal(buffer, offset, length);
 assertEquals(expectedResult, result);
 }

 // Test case for parseOctal with invalid input
 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalInvalidInput() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', '7', 'A', '5', '5', '0'};
 int offset = 0;
 int length = buffer.length;
 TarUtils.parseOctal(buffer, offset, length);
 }

 // Test case for parseOctal with null input
 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalNullInput() {
 byte[] buffer = null;
 int offset = 0;
 int length = 10;
 TarUtils.parseOctal(buffer, offset, length);
 }

 // Test case for parseName method
 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd', '.', 't', 'a', 'r', '.', 'g', 'z', 0};
 int offset = 0;
 int length = buffer.length;
 String expectedResult = "hello.tar.gz";
 String result = TarUtils.parseName(buffer, offset, length);
 assertEquals(expectedResult, result);
 }

 // Test case for parseName with null input
 @Test(expected = IllegalArgumentException.class)
 public void testParseNameNullInput() {
 byte[] buffer = null;
 int offset = 0;
 int length = 10;
 TarUtils.parseName(buffer, offset, length);
 }

 // Test case for formatNameBytes method
 @Test
 public void testFormatNameBytes() {
 String name = "hello.tar.gz";
 byte[] buf = new byte[25];
 int offset = 0;
 int length = name.length();
 int result = TarUtils.formatNameBytes(name, buf, offset, length);
 byte[] expectedResult = new byte[]{'h', 'e', 'l', 'l', 'o', '.', 't', 'a', 'r', '.', 'g', 'z', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
 assertArrayEquals(expectedResult, buf, 0, 22);
 }

 // Test case for formatNameBytes with null input
 @Test(expected = IllegalArgumentException.class)
 public void testFormatNameBytesNullInput() {
 String name = null;
 byte[] buf = new byte[25];
 int offset = 0;
 int length = 10;
 TarUtils.formatNameBytes(name, buf, offset, length);
 }

 // Test case for formatUnsignedOctalString method
 @Test
 public void testFormatUnsignedOctalString() {
 long value = 1234567890L;
 byte[] buffer = new byte[10];
 int offset = 0;
 int length = 10;
 TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
 byte[] expectedResult = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
 assertArrayEquals(expectedResult, buffer, 0, 10);
 }

 // Test case for formatUnsignedOctalString with invalid value
 @Test(expected = IllegalArgumentException.class)
 public void testFormatUnsignedOctalStringInvalidValue() {
 long value = Long.MAX_VALUE+1;
 byte[] buffer = new byte[10];
 int offset = 0;
 int length = 10;
 TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
 }

 // Test case for formatOctalBytes method
 @Test
 public void testFormatOctalBytes() {
 long value = 1234567890L;
 byte[] buf = new byte[13];
 int offset = 0;
 int length = 13;
 TarUtils.formatOctalBytes(value, buf, offset, length);
 byte[] expectedResult = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '7', '5', '5', '6', '0'};
 assertArrayEquals(expectedResult, buf, 0, 13);
 }

 // Test case for formatOctalBytes with invalid value
 @Test(expected = IllegalArgumentException.class)
 public void testFormatOctalBytesInvalidValue() {
 long value = Long.MAX_VALUE+1;
 byte[] buf = new byte[13];
 int offset = 0;
 int length = 13;
 TarUtils.formatOctalBytes(value, buf, offset, length);
 }

 // Test case for computeCheckSum method
 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{'h', 'e', 'l', 'l', 'o', 0x0a, 0x2b, 0x0e, 0x06, 0x00, 0x14, 0x00, 0x00, 0x00};
 long expectedResult = 0x7c2b0e14L;
 long result = TarUtils.computeCheckSum(buffer);
 assertEquals(expectedResult, result);
 }
}