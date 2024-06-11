package org.apache.commons.compress.archivers.tar;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {50, 48, 48}; // "200" in octal
        long expected = 200;
        long result = TarUtils.parseOctal(buffer, 0, 3);
        assertEquals(expected, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = {116, 101, 115, 116};
        String expected = "test";
        String result = TarUtils.parseName(buffer, 0, 4);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "test";
        byte[] buffer = new byte[10];
        int expected = 7;
        int result = TarUtils.formatNameBytes(name, buffer, 0, 7);
        assertEquals(expected, result);
        String expectedName = "test\0\0\0\0\0\0";
        String resultName = new String(buffer, 0, 10);
        assertEquals(expectedName, resultName);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 200;
        byte[] buffer = new byte[10];
        int expected = 7;
        TarUtils.formatUnsignedOctalString(value, buffer, 0, 7);
        String expectedValue = "00000310";
        String resultValue = new String(buffer, 0, 7);
        assertEquals(expectedValue, resultValue);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 200;
        byte[] buffer = new byte[10];
        int expected = 8;
        int result = TarUtils.formatOctalBytes(value, buffer, 0, 8);
        assertEquals(expected, result);
        String expectedValue = "00000310 ";
        String resultValue = new String(buffer, 0, 8);
        assertEquals(expectedValue, resultValue);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 200;
        byte[] buffer = new byte[10];
        int expected = 8;
        int result = TarUtils.formatLongOctalBytes(value, buffer, 0, 8);
        assertEquals(expected, result);
        String expectedValue = "00000310 ";
        String resultValue = new String(buffer, 0, 8);
        assertEquals(expectedValue, resultValue);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 200;
        byte[] buffer = new byte[10];
        int expected = 9;
        int result = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, 9);
        assertEquals(expected, result);
        String expectedValue = "00000310\0 ";
        String resultValue = new String(buffer, 0, 9);
        assertEquals(expectedValue, resultValue);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = {50, 48, 48}; // "200" in octal
        long expected = 648;
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, result);
    }
}