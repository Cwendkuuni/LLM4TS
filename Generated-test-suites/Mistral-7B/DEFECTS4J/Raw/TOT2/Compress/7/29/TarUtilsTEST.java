import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

 // Test case for parseOctal method
 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{(byte) '0', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7'};
 long expected = 0177777L;
 long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 // Test case for parseOctal method with invalid input
 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalInvalidInput() {
 byte[] buffer = new byte[]{(byte) '0', (byte) '8', (byte) '7', (byte) '7', (byte) '7', (byte) '7'};
 TarUtils.parseOctal(buffer, 0, buffer.length);
 }

 // Test case for parseName method
 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 't', (byte) 'x', (byte) 't', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p'};
 String expected = "tar.txt.tar.gz";
 String actual = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals(expected, actual);
 }

 // Test case for parseName method with invalid input
 @Test(expected = IllegalArgumentException.class)
 public void testParseNameInvalidInput() {
 byte[] buffer = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 't', (byte) 'x', (byte) 't', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.'};
 TarUtils.parseName(buffer, 0, buffer.length);
 }

 // Test case for formatNameBytes method
 @Test
 public void testFormatNameBytes() {
 String name = "tar.txt.tar.gz";
 byte[] expected = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 't', (byte) 'x', (byte) 't', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) 0, (byte) 0, (byte) 0};
 byte[] buffer = new byte[expected.length];
 int actual = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 assertArrayEquals(expected, buffer);
 assertEquals(expected.length, actual);
 }

 // Test case for formatNameBytes method with invalid input
 @Test(expected = IllegalArgumentException.class)
 public void testFormatNameBytesInvalidInput() {
 String name = "tar.txt.tar.gz";
 byte[] buffer = new byte[name.length()+1];
 TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 }

 // Test case for formatUnsignedOctalString method
 @Test
 public void testFormatUnsignedOctalString() {
 long value = 1234567L;
 byte[] expected = new byte[]{(byte) '0', (byte) '1', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7'};
 byte[] buffer = new byte[expected.length];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 assertArrayEquals(expected, buffer);
 }

 // Test case for formatUnsignedOctalString method with invalid input
 @Test(expected = IllegalArgumentException.class)
 public void testFormatUnsignedOctalStringInvalidInput() {
 long value = 123456789123456789123456789123456789123456789123456789L;
 byte[] buffer = new byte[13];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 }

 // Test case for formatOctalBytes method
 @Test
 public void testFormatOctalBytes() {
 long value = 1234567L;
 byte[] expected = new byte[]{(byte) '0', (byte) '1', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) ' ', (byte) '0'};
 byte[] buffer = new byte[expected.length];
 int actual = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 assertArrayEquals(expected, buffer);
 assertEquals(expected.length, actual);
 }

 // Test case for formatOctalBytes method with invalid input
 @Test(expected = IllegalArgumentException.class)
 public void testFormatOctalBytesInvalidInput() {
 long value = 123456789123456789123456789123456789123456789123456789L;
 byte[] buffer = new byte[13];
 TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 }

 // Test case for computeCheckSum method
 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{(byte) 75, (byte) 78, (byte) 61, (byte) 74, (byte) 70, (byte) 75, (byte) 73, (byte) 68, (byte) 6F, (byte) 6D, (byte) 65, (byte) 0};
 long expected = TarUtils.computeCheckSum(buffer);
 long actual = TarUtils.computeCheckSum(buffer);
 assertEquals(expected, actual);
 }

 // Test case for computeCheckSum method with empty buffer
 @Test
 public void testComputeCheckSumEmptyBuffer() {
 long expected = 0;
 long actual = TarUtils.computeCheckSum(new byte[0]);
 assertEquals(expected, actual);
 }

 // Test case for computeCheckSum method with invalid buffer
 @Test(expected = IllegalArgumentException.class)
 public void testComputeCheckSumInvalidBuffer() {
 byte[] buffer = new byte[1];
 TarUtils.computeCheckSum(buffer);
 }

}