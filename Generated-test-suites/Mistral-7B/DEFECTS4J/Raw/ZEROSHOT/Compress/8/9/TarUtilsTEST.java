import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{(byte) '0', (byte) '7', (byte) '5', (byte) '5', (byte) ' ', (byte) '7', (byte) '5', (byte) '5', (byte) ' ', (byte) '0'};
 long expected = 0175575L; // Octal representation of 5755
 long result = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalInvalidByte() {
 byte[] buffer = new byte[]{(byte) '0', (byte) '7', (byte) 'a', (byte) '5', (byte) '5', (byte) ' ', (byte) '7', (byte) '5', (byte) '5', (byte) ' ', (byte) '0'};
 TarUtils.parseOctal(buffer, 0, buffer.length);
 }

 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{(byte) 'm', (byte) 'y', (byte) ' ', (byte) 'f', (byte) 'i', (byte) 'l', (byte) 'e', (byte) 's', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 'p', (byte) 'd', (byte) 'f', (byte) ' ', (byte) '0', (byte) '1', (byte) '2'};
 String expected = "my file.tar.gz.pd";
 String result = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatNameBytes() {
 String name = "my file.tar.gz.pd.pdf";
 byte[] buffer = new byte[17];
 int result = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 String parsedName = TarUtils.parseName(buffer, 0, result);
 assertEquals(name, parsedName);
 }

 @Test
 public void testFormatUnsignedOctalString() {
 long value = 35545L; // 0177775 in octal
 byte[] buffer = new byte[10];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 String expected = "0177775 ";
 String result = new String(buffer, 0, 10);
 assertEquals(expected, result);
 }

 @Test
 public void testFormatOctalBytes() {
 long value = 35545L; // 0177775 in octal
 byte[] buffer = new byte[12];
 int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 String expected = "0177775 \0";
 String result = new String(buffer, 0, offset);
 assertEquals(expected, result);
 }

 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{(byte) 0x75, (byte) 0x50, (byte) 0x55, (byte) 0x75, (byte) 0x55, (byte) 0x75};
 long expected = TarUtils.computeCheckSum(buffer);
 long result = TarUtils.computeCheckSum(buffer);
 assertEquals(expected, result);
 }
}