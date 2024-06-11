package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert. assertNull;

import org.junit.Test;

public class TarUtilsTest {

 @Test
 public void testParseOctal() {
 byte[] buffer = {(byte) 075, (byte) 064, (byte) 064, (byte) 075};
 assertEquals(0175644, TarUtils.parseOctal(buffer, 0, buffer.length));

 buffer = new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0};
 assertEquals(0, TarUtils.parseOctal(buffer, 0, buffer.length));

 buffer = new byte[]{(byte) 0, (byte) 0, (byte) 0xFF, (byte) 0};
 assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, 0, buffer.length));

 buffer = new byte[]{(byte) 0, (byte) 0x80, (byte) 0x80, (byte) 0xFF};
 assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, 0, buffer.length));
 }

 @Test
 public void testParseOctalOrBinary() {
 byte[] buffer1 = {(byte) 0x01, (byte) 0x2a, (byte) 0x3f, (byte) 0x5e};
 assertEquals(270026L, TarUtils.parseOctalOrBinary(buffer1, 0, buffer1.length));

 byte[] buffer2 = {(byte) 0x81, (byte) 0x01, (byte) 0x2a, (byte) 0x3f, (byte) 0x5e};
 assertEquals(53289623295L, TarUtils.parseOctalOrBinary(buffer2, 0, buffer2.length));

 byte[] buffer3 = {(byte) 0x01, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80};
 assertEquals(1073741824L, TarUtils.parseOctalOrBinary(buffer3, 0, buffer3.length));

 byte[] buffer4 = new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0};
 assertEquals(0, TarUtils.parseOctalOrBinary(buffer4, 0, buffer4.length));

 byte[] buffer5 = {(byte) 0, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
 assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctalOrBinary(buffer5, 0, buffer5.length));
 }

 @Test
 public void testParseBoolean() {
 byte[] buffer1 = {(byte) 0};
 assertEquals(true, TarUtils.parseBoolean(buffer1, 0));

 byte[] buffer2 = {(byte) 1};
 assertEquals(true, TarUtils.parseBoolean(buffer2, 0));

 byte[] buffer3 = {(byte) 0x80};
 assertEquals(false, TarUtils.parseBoolean(buffer3, 0));
 }

 @Test
 public void testParseName() {
 byte[] buffer1 = new byte[]{(byte) 'a', (byte) 'b', (byte) 'c', (byte) 0};
 assertEquals("abc", TarUtils.parseName(buffer1, 0, buffer1.length));

 byte[] buffer2 = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
 assertNull(TarUtils.parseName(buffer2, 0, buffer2.length));
 }

 @Test
 public void testFormatNameBytes() {
 String name = "myname";
 byte[] buffer = new byte[20];
 int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 assertEquals(name.length(), offset);
 byte[] result = new byte[offset];
 System.arraycopy(buffer, 0, result, 0, offset);
 assertArrayEquals(name.getBytes(), result);
 }

 @Test
 public void testFormatUnsignedOctalString() {
 long value = 1234;
 byte[] buffer = new byte[10];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 assertEquals("01234", new String(buffer));
 }

 @Test
 public void testFormatOctalBytes() {
 long value = 1234;
 byte[] buffer = new byte[10];
 TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 assertEquals("01234 \0", new String(buffer));
 }

 @Test
 public void testFormatLongOctalBytes() {
 long value = 1234;
 byte[] buffer = new byte[10];
 TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
 assertEquals("01234", new String(buffer));
 }

 @Test
 public void testFormatLongOctalOrBinaryBytes() {
 long value1 = 1234;
 long value2 = 2761234;
 byte[] buffer1 = new byte[10];
 byte[] buffer2 = new byte[10];
 TarUtils.formatLongOctalOrBinaryBytes(value1, buffer1, 0, buffer1.length);
 assertEquals("01234", new String(buffer1));
 TarUtils.formatLongOctalOrBinaryBytes(value2, buffer2, 0, buffer2.length);
 assertEquals("\u0001\u0176\u0163\u0172\u0165", new String(buffer2));
 }

 @Test
 public void testFormatCheckSumOctalBytes() {
 long value = 123456789012345L;
 byte[] buffer = new byte[10];
 TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
 assertEquals("0123456764321", new String(buffer));
 }
}