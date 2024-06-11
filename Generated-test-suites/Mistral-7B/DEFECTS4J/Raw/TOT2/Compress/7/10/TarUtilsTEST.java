import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class TarUtilsTest {

 // Test case for parseOctal method
 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7'};
 long expected = 01234567L;
 long result = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(expected, result);
 }

 // Test case for parseOctal with invalid input
 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalInvalidInput() {
 byte[] buffer = new byte[]{(byte) '0', (byte) '1', (byte) '8', (byte) '2', (byte) '3'};
 TarUtils.parseOctal(buffer, 0, buffer.length);
 }

 // Test case for parseOctal with leading spaces
 @Test
 public void testParseOctalLeadingSpaces() {
 byte[] buffer = new byte[]{(byte) ' ', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) ' '};
 long expected = 01234567L;
 long result = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(expected, result);
 }

 // Test case for parseOctal with trailing spaces
 @Test
 public void testParseOctalTrailingSpaces() {
 byte[] buffer = new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) ' ', (byte) ' '};
 long expected = 01234567L;
 long result = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(expected, result);
 }

 // Test case for parseOctal with null input
 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalNullInput() {
 TarUtils.parseOctal(null, 0, 0);
 }

 // Test case for parseOctal with zero length input
 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalZeroLengthInput() {
 TarUtils.parseOctal(new byte[0], 0, 0);
 }

 // Test case for parseName method
 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{(byte) 'm', (byte) 'y', (byte) 'file', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 'p', (byte) 'o', (byte) 'i', (byte) 'n', (byte) 't', (byte) 0};
 String expected = "myfile.tar.gz.zip.tar.gz.zip.tar.gz.point";
 String result = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals(expected, result);
 }

 // Test case for formatNameBytes method
 @Test
 public void testFormatNameBytes() {
 String name = "myfile.tar.gz.zip.tar.gz.zip.tar.gz.point";
 byte[] buffer = new byte[32];
 int length = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 byte[] expected = new byte[]{(byte) 'm', (byte) 'y', (byte) 'f', (byte) 'i', (byte) 'l', (byte) 'e', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) '.', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) '.', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 'p', (byte) 'o', (byte) 'i', (byte) 'n', (byte) 't', (byte) 0};
 Assert.assertArrayEquals(expected, buffer, 0, length);
 }

 // Test case for formatUnsignedOctalString method
 @Test
 public void testFormatUnsignedOctalString() {
 long value = 1234567L;
 byte[] buffer = new byte[10];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 byte[] expected = new byte[]{(byte) '0', (byte) '0', (byte) '01', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7'};
 Assert.assertArrayEquals(expected, buffer, 0, buffer.length);
 }

 // Test case for formatOctalBytes method
 @Test
 public void testFormatOctalBytes() {
 long value = 1234567L;
 byte[] buffer = new byte[12];
 TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 byte[] expected = new byte[]{(byte) '0', (byte) '0', (byte) '01', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) ' ', (byte) ' '};
 Assert.assertArrayEquals(expected, buffer, 0, buffer.length);
 }

 // Test case for computeCheckSum method
 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{(byte) 0x75, (byte) 0x72, (byte) 0x63, (byte) 0x61, (byte) 0x74, (byte) 0x65, (byte) 0x54, (byte) 0x68, (byte) 0x69, (byte) 0x61, (byte) 0x20, (byte) 0x74, (byte) 0x65, (byte) 0x73, (byte) 0x74, (byte) 0x53, (byte) 0x74, (byte) 0x61, (byte) 0x72, (byte) 0x74, (byte) 0x65, (byte) 0x31, (byte) 0x36, (byte) 0x38, (byte) 0x37, (byte) 0x32, (byte) 0x31};
 long checksum = TarUtils.computeCheckSum(buffer);
 long expected = 0x634752741689612074657341241724534172453724131;
 assertEquals(expected, checksum);
 }

 // Test case for computeCheckSum with empty buffer
 @Test
 public void testComputeCheckSumEmptyBuffer() {
 long checksum = TarUtils.computeCheckSum(new byte[0]);
 assertEquals(0, checksum);
 }
}