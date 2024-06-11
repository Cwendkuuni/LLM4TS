package org.apache.commons.compress.archivers.tar;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

 // Test case for parseOctal method
 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{'0', '7', '5', '5', '6', '7', '7'};
 long expectedResult = 0175567L;
 long result = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(expectedResult, result);
 }

 // Test case for parseOctal with invalid input
 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalInvalidInput() {
 byte[] buffer = new byte[]{'1', '2', '3'};
 TarUtils.parseOctal(buffer, 0, buffer.length);
 }

 // Test case for parseOctal with null input
 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalNullInput() {
 TarUtils.parseOctal(null, 0, 0);
 }

 // Test case for parseName method
 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{'t', 'a', 'r', '.', 'g', 'z', 'i', 'p', '.', 't', 'x', 't', 0};
 String expectedResult = "tar.gz.txT";
 String result = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals(expectedResult, result);
 }

 // Test case for formatNameBytes method
 @Test
 public void testFormatNameBytes() {
 String name = "testName.txt";
 byte[] buffer = new byte[10];
 int length = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 String result = new String(buffer, 0, length);
 assertEquals("testName.txt", result);
 }

 // Test case for formatNameBytes with null input
 @Test(expected = IllegalArgumentException.class)
 public void testFormatNameBytesNullInput() {
 String name = null;
 byte[] buffer = new byte[10];
 TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 }

 // Test case for formatNameBytes with name longer than buffer
 @Test(expected = IllegalArgumentException.class)
 public void testFormatNameBytesLongerThanBuffer() {
 String name = "testNameLongerThanBuffer.txt";
 byte[] buffer = new byte[5];
 TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 }

 // Test case for formatUnsignedOctalString method
 @Test
 public void testFormatUnsignedOctalString() {
 long value = 1234567L;
 byte[] buffer = new byte[10];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 String result = new String(buffer, 0, buffer.length);
 assertEquals("0125617", result);
 }

 // Test case for formatUnsignedOctalString with value longer than buffer
 @Test(expected = IllegalArgumentException.class)
 public void testFormatUnsignedOctalStringLongerThanBuffer() {
 long value = 123456789012345L;
 byte[] buffer = new byte[5];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 }

 // Test case for formatOctalBytes method
 @Test
 public void testFormatOctalBytes() {
 long value = 1234567L;
 byte[] buffer = new byte[10];
 TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 String result = new String(buffer, 0, buffer.length);
 assertEquals("0125617  ", result);
 }

 // Test case for formatOctalBytes with null input
 @Test(expected = IllegalArgumentException.class)
 public void testFormatOctalBytesNullInput() {
 long value = 1234567L;
 byte[] buffer = null;
 TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 }

 // Test case for formatOctalBytes with value longer than buffer
 @Test(expected = IllegalArgumentException.class)
 public void testFormatOctalBytesLongerThanBuffer() {
 long value = 123456789012345L;
 byte[] buffer = new byte[5];
 TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 }

 // Test case for computeCheckSum method
 @Test
 public void testComputeCheckSum() {
 byte[] buffer = new byte[]{0, 1, 2, 3, 4, 5};
 long expectedResult = 199;
 long result = TarUtils.computeCheckSum(buffer);
 assertEquals(expectedResult, result);
 }

 // Test case for computeCheckSum with null input
 @Test(expected = IllegalArgumentException.class)
 public void testComputeCheckSumNullInput() {
 TarUtils.computeCheckSum(null);
 }
}