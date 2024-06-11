package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.tar.TarUtils;

public class TarUtilsTest {

    // parseOctal test cases

    @Test
    public void testParseOctal_validInput() {
        byte[] buffer = {49,50,53,0,51};  // 125 in Octal
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(125, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_invalidDigit() {
        byte[] buffer = {47,51,53,0,3};  // '/' is an invalid octal digit
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseOctal_trailingSpace() {
        byte[] buffer = {49,50,53,32,51};  // 125 with trailing space
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(125, result);
    }

    @Test
    public void testParseOctal_zeroPaddedInput() {
        byte[] buffer = {49,50,53,48,48};  // 125 with leading zeros
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(125, result);
    }

    // parseName test cases

    @Test
    public void testParseName_validInput() {
        byte[] buffer = {84,101,115,116,0,83,116,114,105,110,103};  // "Test" followed by "String"
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("Test", result);
    }

    @Test
    public void testParseName_noNullTerminator() {
        byte[] buffer = {84,101,115,116,83,116,114,105,110,103};  // "Test" without null terminator
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("TestString", result);
    }

    // formatNameBytes test cases

    @Test
    public void testFormatNameBytes_validInput() {
        String name = "Test";
        byte[] buf = new byte[8];
        int result = TarUtils.formatNameBytes(name, buf, 0, 8);
        assertEquals(8, result);
        assertEquals(84, buf[0]);
        assertEquals(101, buf[1]);
        assertEquals(115, buf[2]);
        assertEquals(116, buf[3]);
        assertEquals(0, buf[4]);
        assertEquals(0, buf[5]);
        assertEquals(0, buf[6]);
        assertEquals(0, buf[7]);
    }

    @Test
    public void testFormatNameBytes_bufferLongerThanName() {
        String name = "Test";
        byte[] buf = new byte[6];
        int result = TarUtils.formatNameBytes(name, buf, 0, 6);
        assertEquals(6, result);
        assertEquals(84, buf[0]);
        assertEquals(101, buf[1]);
        assertEquals(115, buf[2]);
        assertEquals(116, buf[3]);
        assertEquals(0, buf[4]);
        assertEquals(0, buf[5]);
    }

    // formatUnsignedOctalString test cases

    @Test
    public void testFormatUnsignedOctalString_validInput() {
        long value = 125;
        byte[] buf = new byte[4];
        TarUtils.formatUnsignedOctalString(value, buf, 0, 4);
        assertEquals(51, buf[0]);
        assertEquals(50, buf[1]);
        assertEquals(53, buf[2]);
        assertEquals(0, buf[3]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_bufferTooSmall() {
        long value = 125;
        byte[] buf = new byte[2];
        TarUtils.formatUnsignedOctalString(value, buf, 0, 2);
    }

    // formatOctalBytes test cases

    @Test
    public void testFormatOctalBytes_validInput() {
        long value = 125;
        byte[] buf = new byte[4];
        int result = TarUtils.formatOctalBytes(value, buf, 0, 4);
        assertEquals(4, result);
        assertEquals(55, buf[0]);
        assertEquals(50, buf[1]);
        assertEquals(53, buf[2]);
        assertEquals(0, buf[3]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytes_bufferTooSmall() {
        long value = 125;
        byte[] buf = new byte[2];
        TarUtils.formatOctalBytes(value, buf, 0, 2);
    }

    // formatLongOctalBytes test cases

    @Test
    public void testFormatLongOctalBytes_validInput() {
        long value = 125;
        byte[] buf = new byte[3];
        int result = TarUtils.formatLongOctalBytes(value, buf, 0, 3);
        assertEquals(3, result);
        assertEquals(55, buf[0]);
        assertEquals(50, buf[1]);
        assertEquals(0, buf[2]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatLongOctalBytes_bufferTooSmall() {
        long value = 125;
        byte[] buf = new byte[1];
        TarUtils.formatLongOctalBytes(value, buf, 0, 1);
    }

    // formatCheckSumOctalBytes test cases

    @Test
    public void testFormatCheckSumOctalBytes_validInput() {
        long value = 125;
        byte[] buf = new byte[4];
        int result = TarUtils.formatCheckSumOctalBytes(value, buf, 0, 4);
        assertEquals(4, result);
        assertEquals(55, buf[0]);
        assertEquals(50, buf[1]);
        assertEquals(0, buf[2]);
        assertEquals(32, buf[3]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatCheckSumOctalBytes_bufferTooSmall() {
        long value = 125;
        byte[] buf = new byte[2];
        TarUtils.formatCheckSumOctalBytes(value, buf, 0, 2);
    }

    // computeCheckSum test case

    @Test
    public void testComputeCheckSum_validInput() {
        byte[] buf = {84,101,115,116};
        long result = TarUtils.computeCheckSum(buf);
        assertEquals(434, result);
    }

}