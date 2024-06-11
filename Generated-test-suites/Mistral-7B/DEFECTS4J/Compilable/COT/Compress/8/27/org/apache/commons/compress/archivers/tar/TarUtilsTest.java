package org.apache.commons.compress.archivers.tar;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TarUtilsTest {

    @Test
    public void testParseOctal_ValidInput() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '5', '0'};
        int offset = 0;
        int length = buffer.length;
        long expectedResult = 0175550L;
        long actualResult = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseOctal_TrailingNul() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '5'};
        int offset = 0;
        int length = buffer.length;
        long expectedResult = 017555L;
        long actualResult = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseOctal_MissingTrailingNul() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '5'};
        int offset = 0;
        int length = buffer.length - 1;
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, offset, length));
    }

    @Test
    public void testParseOctal_InvalidByte() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '5', 'X'};
        int offset = 0;
        int length = buffer.length;
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, offset, length));
    }

    @Test
    public void testParseName_ValidInput() {
        byte[] buffer = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'v', (byte) 'a', (byte) 'l', (byte) 'u', (byte) 'e', (byte) '.', (byte) 't', (byte) 'x', (byte) 't', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 'p', (byte) 'o', (byte) 'w', (byte) 'e', (byte) 'r'};
        int offset = 0;
        int length = buffer.length;
        String expectedResult = "tar.val.txt.tar.gz.powere";
        String actualResult = TarUtils.parseName(buffer, offset, length);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatNameBytes_ValidInput() {
        String name = "tar.val.txt.tar.gz.powere";
        byte[] buffer = new byte[32];
        int offset = 0;
        int length = 20;
        int expectedLength = 20;
        int actualLength = TarUtils.formatNameBytes(name, buffer, offset, length);
        assertEquals(expectedLength, actualLength);
        String result = new String(buffer, 0, actualLength);
        assertEquals(name, result);
    }

    @Test
    public void testFormatNameBytes_BufferTooSmall() {
        String name = "tar.val.txt.tar.gz.powere";
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = name.length();
        int expectedLength = 5;
        int actualLength = TarUtils.formatNameBytes(name, buffer, offset, length);
        assertEquals(expectedLength, actualLength);
        String result = new String(buffer, 0, actualLength);
        assertEquals("", result);
    }

    @Test
    public void testFormatUnsignedOctalString_ValidInput() {
        long value = 12345;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 8;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        String result = new String(buffer, 0, 8);
        assertEquals("012361", result);
    }

    @Test
    public void testFormatUnsignedOctalString_ValueTooLargeForBuffer() {
        long value = 123456789012345L;
        byte[] buffer = new byte[8];
        int offset = 0;
        int length = 8;
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatUnsignedOctalString(value, buffer, offset, length));
    }

    @Test
    public void testFormatOctalBytes_ValidInput() {
        long value = 12345;
        byte[] buffer = new byte[11];
        int offset = 0;
        int length = 9;
        TarUtils.formatOctalBytes(value, buffer, offset, length);
        String result = new String(buffer, 0, 9);
        assertEquals("012361 ", result);
    }

    @Test
    public void testFormatOctalBytes_BufferTooSmall() {
        long value = 12345;
        byte[] buffer = new byte[8];
        int offset = 0;
        int length = 9;
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatOctalBytes(value, buffer, offset, length));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'t', 'a', 'r', '.', 'v', 'a', 'l', 'u', 'e', '.', 't', 'x', 't', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p', '.', 'p', 'o', 'w', 'e', 'r'};
        long expectedResult = 1338564478;
        long actualResult = TarUtils.computeCheckSum(buffer);
        assertEquals(expectedResult, actualResult);
    }

}