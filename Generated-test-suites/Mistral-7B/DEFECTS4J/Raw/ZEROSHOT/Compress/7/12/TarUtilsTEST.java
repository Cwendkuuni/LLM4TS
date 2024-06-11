import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{(byte) '7', (byte) '5', (byte) '5', (byte) '4', (byte) '5', (byte) '1'};
 long expected = 01755451L;
 long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalInvalidByte() {
 byte[] buffer = new byte[]{(byte) '7', (byte) '5', (byte) '5', (byte) 'X', (byte) '4', (byte) '5', (byte) '1'};
 TarUtils.parseOctal(buffer, 0, buffer.length);
 }

 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'o', (byte) 'n', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p'};
 String expected = "tar.on.tar.gzip";
 String actual = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatNameBytes() {
 String name = "testName.txt";
 byte[] buffer = new byte[256];
 int length = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 assertEquals(name.length(), length);
 byte[] expected = name.getBytes();
 assertArrayEquals(expected, buffer, 0, name.length());
 }

 @Test
 public void testFormatUnsignedOctalString() {
 long value = 12345;
 byte[] buffer = new byte[10];
 int offset = 0;
 int length = 10;
 TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
 String expected = "012345";
 String actual = new String(buffer, 0, length);
 assertEquals(expected, actual);
 }

 @Test
 public void testFormatOctalBytes() {
 long value = 12345;
 byte[] buffer = new byte[10];
 int offset = 0;
 int length = 10;
 TarUtils.formatOctalBytes(value, buffer, offset, length);
 String expected = "012345 ";
 String actual = new String(buffer, 0, length);
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'o', (byte) 'n', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5'};
 long expected = TarUtils.computeCheckSum(buffer);
 long actual = TarUtils.computeCheckSum(buffer);
 assertEquals(expected, actual);
 }
}