import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', '0', '0', '0'};
 long result = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(07550L, result);

 buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6'};
 try {
 result = TarUtils.parseOctal(buffer, 0, buffer.length);
 fail("Should have thrown an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 buffer = new byte[]{'1'};
 try {
 result = TarUtils.parseOctal(buffer, 0, buffer.length);
 fail("Should have thrown an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{'t', 'a', 'r', '.', 'o', 'b', 'j', 'e', 'c', 't', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p'};
 assertEquals("obj.tar.gz", TarUtils.parseName(buffer, 0, buffer.length));

 buffer = new byte[]{'t', 'a', 'r', '.', 'o', 'b', 'j', 'e', 'c', 't', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p', '.'};
 assertEquals("obj.tar.gz.", TarUtils.parseName(buffer, 0, buffer.length));

 buffer = new byte[0];
 assertEquals("", TarUtils.parseName(buffer, 0, buffer.length));
 }

 @Test
 public void testFormatNameBytes() {
 String name = "name.tar.gz";
 byte[] buffer = new byte[20];
 int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 assertEquals("name.tar.gz", new String(buffer, 0, offset));
 }

 @Test
 public void testFormatUnsignedOctalString() {
 long value = 12345;
 byte[] buffer = new byte[10];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 assertEquals("012345", new String(buffer, 0, 6));
 }

 @Test
 public void testFormatOctalBytes() {
 long value = 12345;
 byte[] buffer = new byte[10];
 TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 assertEquals("012345  ", new String(buffer, 0, 7));
 }

 @Test
 public void testFormatLongOctalBytes() {
 long value = 1234567890123L;
 byte[] buffer = new byte[10];
 TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
 assertEquals("01234567890", new String(buffer, 0, 9));
 }

 @Test
 public void testFormatCheckSumOctalBytes() {
 long value = 123456789012345L;
 byte[] buffer = new byte[11];
 TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
 assertEquals("01234567890  ", new String(buffer, 0, 10));
 }

 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{'t', 'a', 'r', '.', 'o', 'b', 'j', 'e', 'c', 't', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p'};
 long checksum = TarUtils.computeChecksum(buffer);
 long computedChecksum = TarUtils.computeChecksum(new byte[]{(byte) (checksum & 0xFF), (byte) ((checksum >>> 8) & 0xFF),
 (byte) ((checksum >>> 16) & 0xFF), (byte) ((checksum >>> 24) & 0xFF),
 (byte) ((checksum >>> 32) & 0xFF), (byte) ((checksum >>> 40) & 0xFF),
 (byte) ((checksum >>> 48) & 0xFF), (byte) ((checksum >>> 56) & 0xFF)});
 assertEquals(checksum, computedChecksum);
 }

}