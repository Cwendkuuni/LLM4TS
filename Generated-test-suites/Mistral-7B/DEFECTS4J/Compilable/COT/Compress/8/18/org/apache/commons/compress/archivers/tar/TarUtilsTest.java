package org.apache.commons.compress.archivers.tar;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) '7', (byte) '5', (byte) '5', (byte) '5', (byte) '0'};
        long expectedResult = 0175550L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalWithInvalidByte() {
        byte[] buffer = new byte[]{(byte) '7', (byte) '5', (byte) '5', (byte) '5', (byte) '8'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{(byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e'};
        String expectedResult = "abcd e";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "testName";
        byte[] buffer = new byte[10];
        int expectedOffset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        byte[] result = new byte[expectedOffset];
        System.arraycopy(buffer, 0, result, 0, expectedOffset);
        String expectedResult = "testName";
        String resultString = new String(result, 0, expectedOffset);
        assertEquals(expectedResult, resultString);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345L;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String expectedResult = "012345";
        String result = new String(buffer, 0, 6);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345L;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        String expectedResult = "012345 ";
        String result = new String(buffer, 0, offset);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 1234567890L;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        String expectedResult = "01234567540 ";
        String result = new String(buffer, 0, offset);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{(byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e'};
        long expectedResult = 1001011_01101011L;
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(expectedResult, result);
    }
}