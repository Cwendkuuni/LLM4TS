package org.apache.commons.compress.archivers.tar;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TarUtilsTest {

 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{(byte) '0', (byte) '7', (byte) '5', (byte) '5', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '0'};
 int offset = 0;
 int length = 9;

 long result = TarUtils.parseOctal(buffer, offset, length);
 assertEquals(01755770L, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalInvalidInput() {
 byte[] buffer = new byte[]{(byte) '0', (byte) '8', (byte) '5', (byte) '5', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '0'};
 int offset = 0;
 int length = 9;

 TarUtils.parseOctal(buffer, offset, length);
 }

 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) ' ', (byte) 'f', (byte) 'i', (byte) 'l', (byte) 'e', (byte) '.', (byte) 't', (byte) 'x', (byte) 't', (byte) ' ', (byte) 'e', (byte) 'x', (byte) 'a', (byte) 'm', (byte) 'p', (byte) 'l', (byte) 'e', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p'};
 int offset = 0;
 int length = 32;

 String result = TarUtils.parseName(buffer, offset, length);
 assertEquals("file.example.tar.gz", result);
 }

 @Test
 public void testFormatNameBytes() {
 String name = "testName.tar.gz";
 byte[] buf = new byte[10];
 int offset = 0;
 int length = 10;

 int result = TarUtils.formatNameBytes(name, buf, offset, length);
 assertEquals(10, result);

 for (int i = 0; i < length; ++i) {
 assertEquals(name.charAt(i), (char) buf[i]);
 }
 }

 @Test
 public void testFormatUnsignedOctalString() {
 long value = 12345678L;
 byte[] buffer = new byte[10];
 int offset = 0;
 int length = 10;

 TarUtils.formatUnsignedOctalString(value, buffer, offset, length);

 for (int i = 0; i < 8; ++i) {
 assertEquals(i < 7 ? (byte) (i + '0') : (byte) ' ', buffer[i]);
 }
 assertEquals((byte) ' ', buffer[8]);
 assertEquals((byte) '\u0000', buffer[9]);
 }

 @Test
 public void testFormatOctalBytes() {
 long value = 12345678L;
 byte[] buffer = new byte[12];
 int offset = 0;
 int length = 12;

 int result = TarUtils.formatOctalBytes(value, buffer, offset, length);
 assertEquals(12, result);

 for (int i = 0; i < 8; ++i) {
 assertEquals(i < 7 ? (byte) (i + '0') : (byte) ' ', buffer[i]);
 }
 assertEquals((byte) ' ', buffer[8]);
 assertEquals((byte) '\u0020', buffer[9]);
 assertEquals((byte) '\u0000', buffer[10]);
 assertEquals((byte) '\u0000', buffer[11]);
 }

 @Test
 public void testComputeCheckSum() {
 byte[] buf = new byte[]{(byte) 0x75, (byte) 0x78, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00};
 long checksum = TarUtils.computeCheckSum(buf);
 long expectedChecksum = 0x78757801 ^ 0x7f;
 assertEquals(expectedChecksum, checksum);
 }
}