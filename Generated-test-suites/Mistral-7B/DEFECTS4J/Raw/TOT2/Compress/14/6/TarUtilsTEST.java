package org.apache.commons.compress.archivers.tar;

import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

 @Test
 public void testParseOctal_ValidInput() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', '7', '5', '5', '5', '0'};
 long expectedResult = 0175575557L;
 long result = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctal_InvalidInputLength() {
 byte[] buffer = new byte[]{'0'};
 long result;
 try {
 result = TarUtils.parseOctal(buffer, 0, buffer.length);
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctal_InvalidInput() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', '7', 'a', '5', '5', '0'};
 long result;
 try {
 result = TarUtils.parseOctal(buffer, 0, buffer.length);
 } catch (IllegalArgumentException e) {
 // Expected exception
 String expectedMessage = "Invalid byte a at offset 5 in '...len=10'";
 assertEquals(expectedMessage, e.getMessage());
 }
 }

 @Test
 public void testParseOctalOrBinary_ValidInput_Octal() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', '7', '5', '5', '5', '0'};
 long expectedResult = 0175575557L;
 long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testParseOctalOrBinary_ValidInput_Binary() {
 byte[] buffer = new byte[]{(byte) 0x80, 0x01, 0x2A, 0x2B, 0x03, 0x46, 0xCD, 0xEE, 0x05};
 long expectedResult = 128 + 1 * 256 + 41 * 256 * 256 + 42 * 256 * 256 * 256 + 3 * 256 * 256 * 256 * 256 + 13 * 256 * 256 * 256 * 256 * 256 + 60 * 256 * 256 * 256 * 256 * 256 * 256 + 205 * 256 * 256 * 256 * 256 * 256 * 256 * 256;
 long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalOrBinary_InvalidInputLength() {
 byte[] buffer = new byte[]{};
 long result;
 try {
 result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalOrBinary_InvalidInputOctal() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', '7', 'a', '5', '5', '0'};
 long result;
 try {
 result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
 } catch (IllegalArgumentException e) {
 // Expected exception
 String expectedMessage = "Invalid byte a at offset 5 in '...len=10'";
 assertEquals(expectedMessage, e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalOrBinary_InvalidInputBinary() {
 byte[] buffer = new byte[]{(byte) 0x80, 0x01, 0x2A, 0x2B, 0x03, 0x46, 0xFF, 0xEE, 0x05};
 long result;
 try {
 result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
 } catch (IllegalArgumentException e) {
 // Expected exception
 String expectedMessage = "At offset 7, 255 (0xff) exceeds maximum signed long value";
 assertEquals(expectedMessage, e.getMessage());
 }
 }

 @Test
 public void testParseBoolean_ValidInput() {
 byte[] buffer = new byte[]{1};
 boolean expectedResult = true;
 boolean result = TarUtils.parseBoolean(buffer, 0);
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseBoolean_InvalidInput() {
 byte[] buffer = new byte[]{0};
 boolean result;
 try {
 result = TarUtils.parseBoolean(buffer, 0);
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testParseName_ValidInput() {
 byte[] buffer = new byte[]{(byte) 0x21, (byte) 0x35, (byte) 0x62, 0x63, 0x6F, 0x6D, 0x00};
 String expectedResult = "!5barfoo";
 String result = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testFormatNameBytes_ValidInput() {
 String name = "!5barfoo";
 byte[] buffer = new byte[name.length() + 1];
 int result = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 String expectedResult = name;
 assertEquals(expectedResult, new String(buffer, 0, result));
 }

 @Test
 public void testFormatNameBytes_InvalidInput() {
 String name = "!5barfoo\0bar";
 byte[] buffer = new byte[name.length() + 1];
 int result = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 assertEquals(name.length(), result);
 byte[] expectedBytes = new byte[name.length() + 1];
 System.arraycopy(name.getBytes(), 0, expectedBytes, 0, name.length());
 expectedBytes[name.length()] = 0;
 assertArrayEquals(expectedBytes, buffer);
 }

 @Test
 public void testFormatUnsignedOctalString_ValidInput() {
 long value = 0175575557L;
 byte[] buffer = new byte[TarConstants.UIDLEN + 1];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 String expectedResult = "0175575557";
 assertEquals(expectedResult, new String(buffer, 0, TarConstants.UIDLEN+1));
 }

 @Test
 public void testFormatOctalBytes_ValidInput() {
 long value = 0175575557L;
 byte[] buffer = new byte[TarConstants.UIDLEN + 1];
 TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 String expectedResult = "0175575557 ";
 assertEquals(expectedResult, new String(buffer, 0, TarConstants.UIDLEN+1));
 }

 @Test
 public void testFormatLongOctalOrBinaryBytes_ValidInput_Octal() {
 long value = 0175575557L;
 byte[] buffer = new byte[TarConstants.UIDLEN + 1];
 TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
 String expectedResult = "0175575557 ";
 assertEquals(expectedResult, new String(buffer, 0, TarConstants.UIDLEN+1));
 }

 @Test
 public void testFormatLongOctalOrBinaryBytes_ValidInput_Binary() {
 long value = 128 + 1 * 256 + 41 * 256 * 256 + 42 * 256 * 256 * 256 + 3 * 256 * 256 * 256 * 256 * 256 + 13 * 256 * 256 * 256 * 256 * 256 * 256 + 60 * 256 * 256 * 256 * 256 * 256 * 256 * 256;
 byte[] buffer = new byte[TarConstants.UIDLEN + 1];
 TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
 String expectedResult = "0\u0080\u0001\u002a\u002b\u0003\u0046\u00cd\u00ee\u0005 ";
 assertEquals(expectedResult, new String(buffer, 0, TarConstants.UIDLEN+1));
 }

 @Test
 public void testFormatLongOctalOrBinaryBytes_InvalidInput() {
 long value = 128 + 1 * 256 + 41 * 256 * 256 + 42 * 256 * 256 * 256 + 3 * 256 * 256 * 256 * 256 * 256 + 13 * 256 * 256 * 256 * 256 * 256 * 256 + 60 * 256 * 256 * 256 * 256 * 256 * 256 * 256 + 257 * 256 * 256 * 256 * 256 * 256 * 256 * 256;
 byte[] buffer = new byte[TarConstants.UIDLEN + 1];
 TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
 // Should throw exception
 }

 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{0x1F, 0x21, 0x22, 0x23};
 long expectedResult = (0x1F & 0xFF) + ((0x21 & 0xFF) << 8) + ((0x22 & 0xFF) << 16) + ((0x23 & 0xFF) << 24);
 long result = TarUtils.computeCheckSum(buffer);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testComputeCheckSum_AllNuls() {
 byte[] buffer = new byte[]{0};
 long expectedResult = 0;
 long result = TarUtils.computeCheckSum(buffer);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testComputeCheckSum_AllTrailingNuls() {
 byte[] buffer = new byte[]{0x01, 0, 0, 0, 0, 0, 0, 0, 0};
 long expectedResult = 0x01;
 long result = TarUtils.computeCheckSum(buffer);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testComputeCheckSum_AllTrailingSpaces() {
 byte[] buffer = new byte[]{0x01, ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
 long expectedResult = 0x01;
 long result = TarUtils.computeCheckSum(buffer);
 assertEquals(expectedResult, result);
 }
}