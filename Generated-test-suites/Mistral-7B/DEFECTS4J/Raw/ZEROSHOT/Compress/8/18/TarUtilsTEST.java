import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TarUtilsTest {

 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', '0', '0'};
 long expected = 0175500L;
 long result = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(expected, result);
 }

 @Test
 public void testParseOctalInvalidByte() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', 'x', '0'};
 assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, 0, buffer.length));
 }

 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'x', 't'};
 String expected = "tar.txt";
 String result = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatNameBytes() {
 String name = "test.tar.gz";
 byte[] buffer = new byte[32];
 int length = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 byte[] expected = new byte[]{'t', 'e', 's', 't', '.','t', 'a', 'r', '.', 'g', 'z', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
 assertArrayEquals(expected, buffer, 0, length);
 }

 @Test
 public void testFormatUnsignedOctalString() {
 long value = 12345;
 byte[] buffer = new byte[10];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', ' ', '0', ' '};
 assertArrayEquals(expected, buffer, 0, buffer.length);
 }

 @Test
 public void testFormatOctalBytes() {
 long value = 12345;
 byte[] buffer = new byte[10];
 TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', ' ', '0', '\u0000'};
 assertArrayEquals(expected, buffer, 0, buffer.length);
 }

 @Test
 public void testFormatLongOctalBytes() {
 long value = 12345678912345L;
 byte[] buffer = new byte[11];
 TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
 byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '\u0001'};
 assertArrayEquals(expected, buffer, 0, buffer.length);
 }

 @Test
 public void testFormatCheckSumOctalBytes() {
 long value = 12345678912345L;
 byte[] buffer = new byte[12];
 TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
 byte[] expected = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '1', '2', ' '};
 assertArrayEquals(expected, buffer, 0, buffer.length);
 }

 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r'};
 long expected = 0x12C14E3F85B42093L;
 long result = TarUtils.computeCheckSum(buffer);
 assertEquals(expected, result);
 }
}