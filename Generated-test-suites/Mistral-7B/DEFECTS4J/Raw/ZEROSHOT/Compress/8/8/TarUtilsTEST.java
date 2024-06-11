import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{'0', '0', '7', '7', '7', '7', '7', '7', '0'};
 long result = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(01777770L, result);

 buffer = new byte[]{'0', '0', '7', '7', '7', '7', '7', '7', '1'};
 result = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(01777771L, result);

 buffer = new byte[]{'0', '0'};
 result = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(0L, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalInvalidByte() {
 byte[] buffer = new byte[]{'0', '0', '8'};
 TarUtils.parseOctal(buffer, 0, buffer.length);
 }

 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{'a', 'b', 'c', 'd', 'e'};
 String name = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals("abcde", name);
 }

 @Test
 public void testFormatNameBytes() {
 String name = "abcde";
 byte[] buffer = new byte[10];
 int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 assertArrayEquals(new byte[]{'a', 'b', 'c', 'd', 'e', 0, 0, 0, 0, 0}, buffer);
 assertEquals(10, offset);
 }

 @Test
 public void testFormatUnsignedOctalString() {
 long value = 12345;
 byte[] buffer = new byte[10];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 assertArrayEquals(new byte[]{'0', '0', '1', '2', '3', '4', '5', '0', '0', '0'}, buffer);
 }

 @Test
 public void testFormatOctalBytes() {
 long value = 12345;
 byte[] buffer = new byte[12];
 TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', ' ', '0', '0', '0'}, buffer);
 }

 @Test
 public void testFormatLongOctalBytes() {
 long value = 1234567890L;
 byte[] buffer = new byte[11];
 TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
 assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' '}, buffer);
 }

 @Test
 public void testFormatCheckSumOctalBytes() {
 long value = 1234567890L;
 byte[] buffer = new byte[12];
 TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
 assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', ' '}, buffer);
 }

 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{'a', 'b', 'c'};
 long checksum = TarUtils.computeCheckSum(buffer);
 assertEquals(315 * BYTE_MASK + 172, checksum);
 }
}