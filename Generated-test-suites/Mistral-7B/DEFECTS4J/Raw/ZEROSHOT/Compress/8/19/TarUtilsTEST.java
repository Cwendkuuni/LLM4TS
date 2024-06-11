import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{'0', '7', '7', '7', '5', '0', '0', '0', '0', '0'};
 long expected = 0177754L;
 long result = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalInvalidByte() {
 byte[] buffer = new byte[]{'0', '7', '7', '7', '5', 'x', '0', '0', '0', '0'};
 TarUtils.parseOctal(buffer, 0, buffer.length);
 }

 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{'h', 'e', 'l', 'l', 'o', ' ', 'T', 'a', 'r', ' ', 'G', 'z', 'i', 'p', '.', 't', 'x', 't', ' ', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
 String expected = "hello Tar gzip.txt";
 String result = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatNameBytes() {
 String name = "hello Tar gzip.txt";
 byte[] buffer = new byte[32];
 int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 String parsedName = TarUtils.parseName(buffer, 0, offset);
 assertEquals(name, parsedName);
 }

 @Test
 public void testFormatUnsignedOctalString() {
 long value = 123456789L;
 byte[] buffer = new byte[10];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 String expected = "01234567541";
 String result = new String(buffer, 0, 10);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatOctalBytes() {
 long value = 123456789L;
 byte[] buffer = new byte[12];
 TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 String expected = "01234567541 ";
 String result = new String(buffer, 0, 12);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatLongOctalBytes() {
 long value = 1234567890123456789L;
 byte[] buffer = new byte[13];
 TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
 String expected = "012345675410123456789 ";
 String result = new String(buffer, 0, 13);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatCheckSumOctalBytes() {
 long value = 1234567890123456789L;
 byte[] buffer = new byte[13];
 TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
 String expected = "012345675410123456789  ";
 String result = new String(buffer, 0, 13);
 assertEquals(expected, result);
 }

 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{'h', 'e', 'l', 'l', 'o', ' ', 'T', 'a', 'r', ' ', 'G', 'z', 'i', 'p', '.', 't', 'x', 't', ' ', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
 long expected = 0x967342239218C5B9L;
 long result = TarUtils.computeCheckSum(buffer);
 assertEquals(expected, result);
 }
}