package org.apache.commons.compress.archivers.tar;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

 // Test for parseOctal method
 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[] { (byte) '0', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) ' ' };
 long expected = 01777777L;
 long result = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalInvalidInput() {
 byte[] buffer = new byte[] { (byte) '0', (byte) '8' }; // Invalid octal digit
 int offset = 0;
 int length = buffer.length;
 try {
 TarUtils.parseOctal(buffer, offset, length);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalLengthExceeded() {
 byte[] buffer = new byte[] { (byte) '0' };
 int offset = 0;
 int length = Integer.MAX_VALUE + 1; // length exceeded
 try {
 TarUtils.parseOctal(buffer, offset, length);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 // Test for parseName method
 @Test
 public void testParseName() {
 byte[] buffer = new byte[] { (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 't', (byte) 'e', (byte) 's', (byte) 't', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 'p', (byte) 'l', (byte) 'a', (byte) 't', (byte) 'f', (byte) 'o', (byte) 'r', (byte) 'm', (byte) 'a', (byte) 't', (byte) 'e', (byte) 'd', (byte) '.', (byte) 'd', (byte) 'a', (byte) 't', (byte) 'a', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 'p', (byte) 'l', (byte) 'a', (byte) 't', (byte) 'f', (byte) 'o', (byte) 'r', (byte) 'm', (byte) 'a', (byte) 't', (byte) 'e', (byte) 'd' };
 String expected = "tar.tester.gz.platform.data.tar.gz.platform.data.test.tar.gz";
 String result = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals(expected, result);
 }

 // Test for formatNameBytes method
 @Test
 public void testFormatNameBytes() {
 String name = "tar.tester.gz.platform.data.tar.gz.platform.data.test.tar.gz";
 byte[] expected = new byte[] { (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 't', (byte) 'e', (byte) 's', (byte) 't', (byte) 'e', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 'p', (byte) 'l', (byte) 'a', (byte) 't', (byte) 'f', (byte) 'o', (byte) 'r', (byte) 'm', (byte) 'a', (byte) 't', (byte) 'e', (byte) 'd', (byte) '.', (byte) 'd', (byte) 'a', (byte) 't', (byte) 'a', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 'p', (byte) 'l', (byte) 'a', (byte) 't', (byte) 'f', (byte) 'o', (byte) 'r', (byte) 'm', (byte) 'a', (byte) 't', (byte) 'e', (byte) 'd' };
 byte[] buffer = new byte[expected.length];
 int offset = 0;
 int length = expected.length;
 TarUtils.formatNameBytes(name, buffer, offset, length);
 assertArrayEquals(expected, buffer);
 }

 // Test for formatUnsignedOctalString method
 @Test
 public void testFormatUnsignedOctalString() {
 long value = 1234567890L;
 byte[] expected = new byte[] { (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9' };
 byte[] buffer = new byte[expected.length];
 int offset = 0;
 int length = expected.length;
 TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
 assertArrayEquals(expected, buffer);
 }

 // Test for formatOctalBytes method
 @Test
 public void testFormatOctalBytes() {
 long value = 1234567890L;
 byte[] expected = new byte[] { (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9', (byte) ' ', (byte) '0' };
 byte[] buffer = new byte[expected.length];
 int offset = 0;
 int length = expected.length;
 TarUtils.formatOctalBytes(value, buffer, offset, length);
 assertArrayEquals(expected, buffer);
 }

 // Test for computeCheckSum method
 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[] { (byte) 0x75, (byte) 0x72, (byte) 0x63, (byte) 0x61, (byte) 0x74, (byte) 0x65, (byte) 0x54, (byte) 0x79, (byte) 0x70, (byte) 0x65, (byte) 0x32, (byte) 0x5C, (byte) 0x75, (byte) 0x74, (byte) 0x66, (byte) 0x2D, (byte) 0x31, (byte) 0x0D, (byte) 0x0A };
 long expected = 110193248212721L;
 long result = TarUtils.computeCheckSum(buffer);
 assertEquals(expected, result);
 }
}