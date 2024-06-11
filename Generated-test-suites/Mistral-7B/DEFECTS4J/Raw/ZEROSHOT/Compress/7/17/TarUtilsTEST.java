import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{'0', '1', '2'};
 long result = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(012L, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalInvalid() {
 byte[] buffer = new byte[]{'0', '1', 'x'};
 TarUtils.parseOctal(buffer, 0, buffer.length);
 }

 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'e', 'n', 't', 'r', 'y', '.', 't', 'x', 't'};
 String result = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals("entry.txt", result);
 }

 @Test
 public void testFormatNameBytes() {
 String name = "test.txt";
 byte[] buffer = new byte[25];
 int length = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 assertArrayEquals(new byte[]{'t', 'e', 's', 't', '.', 't', 'x', 't'}, buffer, 0, length);
 }

 @Test
 public void testFormatUnsignedOctalString() {
 long value = 12345;
 byte[] buffer = new byte[10];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', ' ', '0', ' ', (byte) 0}, buffer, 0, buffer.length);
 }

 @Test
 public void testFormatOctalBytes() {
 long value = 12345;
 byte[] buffer = new byte[10];
 int length = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', ' ', ' ', (byte) 0, (byte) 0}, buffer, 0, length);
 }

 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'e', 'n', 't', 'r', 'y', '.', 't', 'x', 't'};
 long checksum = TarUtils.computeCheckSum(buffer);
 long computedChecksum = 0;
 for (int i = 0; i < buffer.length; ++i) {
 computedChecksum += TarUtils.BYTE_MASK & buffer[i];
 }
 assertEquals(checksum, computedChecksum);
 }

}