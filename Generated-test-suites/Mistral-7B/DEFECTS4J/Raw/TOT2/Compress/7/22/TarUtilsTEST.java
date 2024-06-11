package org.apache.commons.compress.archivers.tar;

import org.apache.commons.compress.utils.ByteStreams;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

 private byte[] testBuffer;

 @Before
 public void setup() {
 testBuffer = new byte[1024];
 }

 @Test
 public void testParseOctal() {
 // Basic test case
 long result = TarUtils.parseOctal(testBuffer, 0, 10);
 assertEquals(0x1234567, result);

 // Edge case: empty input
 result = TarUtils.parseOctal(testBuffer, 0, 0);
 assertEquals(0, result);

 // Edge case: input longer than necessary
 result = TarUtils.parseOctal(testBuffer, 0, 11);
 assertEquals(0x123456, result);

 // Exception case: invalid octal digit
 testBuffer[0] = (byte) 0x80;
 try {
 TarUtils.parseOctal(testBuffer, 0, 1);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testParseName() {
 // Basic test case
 String name = TarUtils.parseName(testBuffer, 0, 10);
 assertEquals("testName", name);

 // Edge case: empty input
 name = TarUtils.parseName(testBuffer, 0, 0);
 assertEquals("", name);

 // Edge case: input longer than necessary
 name = TarUtils.parseName(testBuffer, 0, 11);
 assertEquals("testName...", name);
 }

 @Test
 public void testFormatNameBytes() {
 // Basic test case
 String name = "testName";
 byte[] result = new byte[20];
 int offset = TarUtils.formatNameBytes(name, result, 0, result.length);
 assertEquals(10, offset);
 assertArrayEquals(new byte[]{"t", "e", "s", "t", "N", "a", "m", "e", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, result, 0, 18);

 // Edge case: name longer than buffer
 name = "testName testName testName testName testName testName testName testName testName testName testName testName testName testName";
 try {
 TarUtils.formatNameBytes(name, result, 0, result.length);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testFormatUnsignedOctalString() {
 // Basic test case
 long value = 0x1234567;
 byte[] result = new byte[10];
 TarUtils.formatUnsignedOctalString(value, result, 0, result.length);
 assertArrayEquals(new byte[]{"0", "0", "0", "075", "1", "1", "2", "054", "065", "067"}, result, 0, 10);

 // Edge case: value longer than buffer
 value = 0x12345678901234567890123456789L;
 try {
 TarUtils.formatUnsignedOctalString(value, result, 0, result.length);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testFormatOctalBytes() {
 // Basic test case
 long value = 0x1234567;
 byte[] result = new byte[12];
 int offset = TarUtils.formatOctalBytes(value, result, 0, result.length);
 assertArrayEquals(new byte[]{"0", "0", "075", "1", "1", "2", "054", "065", "067", " ", "0"}, result, 0, offset);

 // Edge case: value longer than buffer (truncated)
 value = 0x12345678901234567890123456789L;
 offset = TarUtils.formatOctalBytes(value, result, 0, result.length);
 assertArrayEquals(new byte[]{"0", "0", "077", "7", "1", "1", "2", "055", "056", "066", "067", " "}, result, 0, offset);

 // Edge case: result buffer length shorter than necessary
 result = new byte[11];
 try {
 TarUtils.formatOctalBytes(value, result, 0, result.length);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testComputeCheckSum() {
 // Basic test case
 byte[] buffer = new byte[]{"T", "a", "r", " ", "H", "e", "a", "d", "e", "r", 0x07, " ", "v", "1.0", "\r\n"};
 long checksum = TarUtils.computeCheckSum(buffer);
 long computedChecksum = TarUtils.formatCheckSumOctalBytes(checksum, new byte[12], 0, buffer.length + 2);
 assertArrayEquals(new byte[]{"075", "7", "6", "5", "5", "6", "3", "4", "2", "2", "2", "1"}, computedChecksum, 0, computedChecksum.length());
 }
}