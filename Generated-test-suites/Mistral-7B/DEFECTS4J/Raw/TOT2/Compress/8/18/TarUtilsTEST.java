import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TarUtilsTest {

 // Test case for parseOctal method
 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{(byte) '7', (byte) '5', (byte) '5', (byte) '5', (byte) '4', (byte) '1', (byte) '5', (byte) '7', (byte) '0'};
 long expected = 01755541570L;
 long result = TarUtils.parseOctal(buffer, 0, buffer.length);
 Assertions.assertEquals(expected, result);
 }

 // Test case for parseOctal with invalid byte
 @Test
 public void testParseOctalInvalidByte() {
 byte[] buffer = new byte[]{(byte) '7', (byte) '5', (byte) '5', (byte) '5', (byte) 'A', (byte) '4', (byte) '1', (byte) '5', (byte) '7', (byte) '0'};
 Assertions.assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, 0, buffer.length));
 }

 // Test case for parseOctal with empty buffer
 @Test
 public void testParseOctalEmptyBuffer() {
 byte[] buffer = new byte[0];
 Assertions.assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, 0, buffer.length));
 }

 // Test case for parseName method
 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{(byte) 'm', (byte) 'y', (byte) 'file', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p'};
 String expected = "myfile.tar.gz";
 String result = TarUtils.parseName(buffer, 0, buffer.length);
 Assertions.assertEquals(expected, result);
 }

 // Test case for parseName with invalid byte
 @Test
 public void testParseNameInvalidByte() {
 byte[] buffer = new byte[]{(byte) 'm', (byte) 'y', (byte) 'file', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) 'x'};
 Assertions.assertThrows(IllegalArgumentException.class, () -> TarUtils.parseName(buffer, 0, buffer.length));
 }

 // Test case for formatNameBytes method
 @Test
 public void testFormatNameBytes() {
 String name = "myfile.tar.gz";
 byte[] buffer = new byte[22];
 int result = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 byte[] expected = new byte[]{(byte) 'm', (byte) 'y', (byte) 'f', (byte) 'i', (byte) 'l', (byte) 'e', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p'};
 Assertions.assertArrayEquals(expected, Arrays.copyOfRange(buffer, 0, result));
 }

 // Test case for formatUnsignedOctalString method
 @Test
 public void testFormatUnsignedOctalString() {
 long value = 755;
 byte[] buffer = new byte[8];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 byte[] expected = new byte[]{(byte) '01', (byte) '175', (byte) '5'};
 Assertions.assertArrayEquals(expected, Arrays.copyOfRange(buffer, 0, 3));
 }

 // Test case for formatOctalBytes method
 @Test
 public void testFormatOctalBytes() {
 long value = 755;
 byte[] buffer = new byte[10];
 TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 byte[] expected = new byte[]{(byte) '01', (byte) '175', (byte) '5', (byte) ' '};
 Assertions.assertArrayEquals(expected, Arrays.copyOfRange(buffer, 0, 4));
 }

 // Test case for computeCheckSum method
 @Test
 public void testComputeCheckSum() {
 byte[] tarHeader = new byte[]{(byte) 0x50, (byte) 0x4b, (byte) 0x05, (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
 long checksum = TarUtils.computeCheckSum(tarHeader);
 long expected = 0x15022027;
 Assertions.assertEquals(expected, checksum);
 }

 // Test case for parseOctal with null buffer
 @Test
 public void testParseOctalNullBuffer() {
 Assertions.assertThrows(NullPointerException.class, () -> TarUtils.parseOctal(null, 0, 0));
 }

 // Test case for parseName with null buffer
 @Test
 public void testParseNameNullBuffer() {
 Assertions.assertThrows(NullPointerException.class, () -> TarUtils.parseName(null, 0, 0));
 }

 // Test case for formatNameBytes with null name
 @Test
 public void testFormatNameBytesNullName() {
 Assertions.assertThrows(NullPointerException.class, () -> TarUtils.formatNameBytes(null, new byte[10], 0, 10));
 }
}