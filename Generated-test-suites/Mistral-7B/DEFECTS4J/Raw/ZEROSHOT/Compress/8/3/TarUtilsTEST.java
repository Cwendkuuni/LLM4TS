import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', '7', '7', '7', '7'};
 long expected = 01755664L; // Octal number representation of 55664
 long result = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalInvalidByte() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', '7', '7', '7', '8'}; // Invalid byte '8'
 TarUtils.parseOctal(buffer, 0, buffer.length);
 }

 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{(byte) 0x63, (byte) 0x61, (byte) 0x6d, (byte) 0x65, (byte) 0x6d, (byte) 0x70, (byte) 0x6c, (byte) 0x69, (byte) 0x63, (byte) 0x61, (byte) 0x74, (byte) 0x65, (byte) 0x73, (byte) 0x74, (byte) 0x20, (byte) 0x74, (byte) 0x68, (byte) 0x65, (byte) 0x72, (byte) 0x65, (byte) 0x20, (byte) 0x63, (byte) 0x61, (byte) 0x72, (byte) 0x65, (byte) 0x64, (byte) 0x20, (byte) 0x61, (byte) 0x73, (byte) 0x73, (byte) 0x77, (byte) 0x65, (byte) 0x6e, (byte) 0x74, (byte) 0x20, (byte) 0x74, (byte) 0x6f, (byte) 0x20, (byte) 0x63, (byte) 0x61, (byte) 0x62, (byte) 0x65, (byte) 0x66, (byte) 0x66, (byte) 0x65, (byte) 0x20, (byte) 0x73, (byte) 0x61, (byte) 0x6d, (byte) 0x20, (byte) 0x72, (byte) 0x75, (byte) 0x6e, (byte) 0x64, (byte) 0x65, (byte) 0x64, (byte) 0x20, (byte) 0x63, (byte) 0x61, (byte) 0x6e, (byte) 0x64, (byte) 0x65, (byte) 0x64, (byte) 0x65, (byte) 0x20, (byte) 0x74, (byte) 0x65, (byte) 0x73, (byte) 0x74, (byte) 0x20, (byte) 0x68, (byte) 0x65, (byte) 0x61, (byte) 0x64, (byte) 0x20, (byte) 0x63, (byte) 0x68, (byte) 0x61, (byte) 0x6d, (byte) 0x70, (byte) 0x6c, (byte) 0x65, (byte) 0x20, (byte) 0x74, (byte) 0x68, (byte) 0x69, (byte) 0x73, (byte) 0x20, (byte) 0x62, (byte) 0x65, (byte) 0x20, (byte) 0x73, (byte) 0x6f, (byte) 0x6e, (byte) 0x6e, (byte) 0x65, (byte) 0x67, (byte) 0x68, (byte) 0x61, (byte) 0x73, (byte) 0x73, (byte) 0x20, (byte) 0x6f, (byte) 0x66, (byte) 0x20, (byte) 0x69, (byte) 0x6e, (byte) 0x74, (byte) 0x65, (byte) 0x6e, (byte) 0x74};
 String expected = "name1.extension1.name2.extension2.name3.extension3.name4.extension4";
 String result = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatNameBytes() {
 String name = "name1.extension1";
 byte[] buffer = new byte[32];
 int length = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 byte[] expected = new byte[]{(byte) 0x6e, (byte) 0x69, (byte) 0x6d, (byte) 0x65, (byte) 0x78, (byte) 0x61, (byte) 0x73, (byte) 0x70, (byte) 0x65, (byte) 0x6e, (byte) 0x64, (byte) 0x2e, (byte) 0x65, (byte) 0x78, (byte) 0x68, (byte) 0x61, (byte) 0x73, (byte) 0x70, (byte) 0x65, (byte) 0x6e, (byte) 0x64};
 assertArrayEquals(expected, buffer, 0, length);
 }

 @Test
 public void testFormatUnsignedOctalString() {
 long value = 3210;
 byte[] buffer = new byte[10];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 byte[] expected = new byte[]{(byte) 0x3, (byte) 0x2, (byte) 0x1, (byte) 0x0, (byte) ' ', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '8'};
 assertArrayEquals(expected, buffer, 0, buffer.length);
 }

 @Test
 public void testFormatOctalBytes() {
 long value = 3210;
 byte[] buffer = new byte[12];
 TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 byte[] expected = new byte[]{(byte) '0', (byte) '1', (byte) '1', (byte) '0', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) ' ', (byte) ' '};
 assertArrayEquals(expected, buffer, 0, buffer.length);
 }

 @Test
 public void testFormatLongOctalBytes() {
 long value = 3210;
 byte[] buffer = new byte[11];
 TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
 byte[] expected = new byte[]{(byte) '0', (byte) '1', (byte) '1', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7'};
 assertArrayEquals(expected, buffer, 0, buffer.length);
 }

 @Test
 public void testFormatCheckSumOctalBytes() {
 long value = 3210;
 byte[] buffer = new byte[12];
 TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
 byte[] expected = new byte[]{(byte) '0', (byte) '1', (byte) '1', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) ' ', (byte) ' '};
 assertArrayEquals(expected, buffer, 0, buffer.length);
 }

 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{0x65, 0x78, 0x61, 0x6d, 0x65, 0x78, 0x61, 0x6d, 0x65, 0x78, 0x61, 0x6d, 0x65, 0x78, 0x61, 0x6d, 0x65, 0x78, 0x61, 0x6d, 0x65, 0x73, 0x74, 0x20, 0x62, 0x75, 0x6e, 0x64, 0x65, 0x64, 0x20, 0x63, 0x61, 0x6e, 0x64, 0x65, 0x64, 0x65, 0x20, 0x74, 0x65, 0x73, 0x74, 0x20, 0x68, 0x65, 0x61, 0x64, 0x20, 0x63, 0x68, 0x61, 0x6d, 0x70, 0x6c, 0x65, 0x20, 0x74, 0x68, 0x69, 0x73, 0x20, 0x62, 0x53, 0x20, 0x6f, 0x66, 0x20, 0x69, 0x6e, 0x74, 0x65, 0x6e, 0x74};
 long expected = 2942273703L;
 long result = TarUtils.computeCheckSum(buffer);
 assertEquals(expected, result);
 }
}