package org.apache.commons.compress.archivers.tar;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

 // Test for parseOctal method
 @Test
 public void testParseOctal() {
 byte[] buffer = new byte[]{(byte) '0', (byte) '7', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5'};
 long expectedValue = 0666666666666L;
 long actualValue = TarUtils.parseOctal(buffer, 0, buffer.length);
 assertEquals(expectedValue, actualValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOctalInvalidByte() {
 byte[] buffer = new byte[]{(byte) '0', (byte) '7', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) 'x'}; // 'x' is an invalid byte
 long actualValue = TarUtils.parseOctal(buffer, 0, buffer.length);
 // The test should throw an IllegalArgumentException
 }

 // Test for parseName method
 @Test
 public void testParseName() {
 byte[] buffer = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'm', (byte) 'y', (byte) ' ', (byte) 'f', (byte) 'i', (byte) 'l', (byte) 'e', (byte) '.', (byte) 't', (byte) 'x', (byte) 't', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 'p', (byte) 'o', (byte) 'd', (byte) '.', (byte) 'd', (byte) 'a', (byte) 't', (byte) '.', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'j', (byte) 'a', (byte) 'v', (byte) 'a', (byte) '.', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'j', (byte) 'a', (byte) 'v', (byte) 'a', (byte) '.', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'j', (byte) 'a', (byte) 'v', (byte) 'a'};
 String expectedValue = "tar.my.file.tar.gz.pod.dar.ar.jar.ar.jar.ar.jar.jar.ar.";
 String actualValue = TarUtils.parseName(buffer, 0, buffer.length);
 assertEquals(expectedValue, actualValue);
 }

 // Test for formatNameBytes method
 @Test
 public void testFormatNameBytes() {
 String name = "tar.my.file.tar.gz.pod.dar.ar.jar.ar.jar.ar.jar.jar.ar.";
 byte[] buffer = new byte[name.length() + 1];
 int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
 String actualValue = new String(buffer, 0, offset);
 assertEquals(name, actualValue);
 }

 // Test for formatUnsignedOctalString method
 @Test
 public void testFormatUnsignedOctalString() {
 long value = 0666666666666L;
 byte[] buffer = new byte[10];
 TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
 String expectedValue = "066666666666";
 String actualValue = new String(buffer, 0, 10);
 assertEquals(expectedValue, actualValue);
 }

 // Test for formatOctalBytes method
 @Test
 public void testFormatOctalBytes() {
 long value = 0666666666666L;
 byte[] buffer = new byte[12];
 TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
 String expectedValue = "066666666666  ";
 String actualValue = new String(buffer, 0, 12);
 assertEquals(expectedValue, actualValue);
 }

 // Test for computeCheckSum method
 @Test
 public void testComputeCheckSum() {
 byte[] buf = new byte[]{(byte) 0x75, (byte) 0x72, (byte) 0x69, (byte) 0x63, (byte) 0x73, (byte) 0x61, (byte) 0x63, (byte) 0x68, (byte) 0x65, (byte) 0x78, (byte) 0x70, (byte) 0x65, (byte) 0x73, (byte) 0x61, (byte) 0x6D, (byte) 0x65, (byte) 0x64, (byte) 0x69, (byte) 0x6F, (byte) 0x6E, (byte) 0x61, (byte) 0x6D, (byte) 0x70, (byte) 0x6C, (byte) 0x65, (byte) 0x61, (byte) 0x72, (byte) 0x65, (byte) 0x73, (byte) 0x74, (byte) 0x20, (byte) 0x66, (byte) 0x6F, (byte) 0x72, (byte) 0x20, (byte) 0x63, (byte) 0x61, (byte) 0x74, (byte) 0x65, (byte) 0x5C, (byte) 0x61, (byte) 0x74, (byte) 0x65, (byte) 0x72, (byte) 0x2E, (byte) 0x63, (byte) 0x61, (byte) 0x72, (byte) 0x74, (byte) 0x69, (byte) 0x6E, (byte) 0x67, (byte) 0x2E, (byte) 0x62, (byte) 0x61, (byte) 0x73, (byte) 0x65, (byte) 0x2E, (byte) 0x68, (byte) 0x65, (byte) 0x61, (byte) 0x70, (byte) 0x70, (byte) 0x6C, (byte) 0x65, (byte) 0x2E, (byte) 0x64, (byte) 0x61, (byte) 0x74, (byte) 0x61, (byte) 0x2E, (byte) 0x61, (byte) 0x6D, (byte) 0x65, (byte) 0x73, (byte) 0x74, (byte) 0x2E, (byte) 0x61, (byte) 0x6D, (byte) 0x70, (byte) 0x6C, (byte) 0x65, (byte) 0x2E, (byte) 0x6A, (byte) 0x61, (byte) 0x76, (byte) 0x61, (byte) 0x73, (byte) 0x63, (byte) 0x72, (byte) 0x69, (byte) 0x7A, (byte) 0x65, (byte) 0x64, (byte) 0x2E, (byte) 0x61, (byte) 0x74, (byte) 0x61, (byte) 0x2E, (byte) 0x69, (byte) 0x6E, (byte) 0x76, (byte) 0x65, (byte) 0x72, (byte) 0x2E, (byte) 0x63, (byte) 0x72, (byte) 0x79, (byte) 0x65, (byte) 0x73, (byte) 0x73, (byte) 0x2E, (byte) 0x61, (byte) 0x6D, (byte) 0x70, (byte) 0x6C, (byte) 0x65, (byte) 0x2E, (byte) 0x61, (byte) 0x6D, (byte) 0x70, (byte) 0x6C, (byte) 0x65};
 long expectedChecksum = 1686527995919575308L;
 long actualChecksum = TarUtils.computeCheckSum(buf);
 assertEquals(expectedChecksum, actualChecksum);
 }
}