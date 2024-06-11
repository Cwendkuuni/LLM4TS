package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] testBytes = new byte[]{-64, -64, '3', '1', '7', '7', '7'};
        assertEquals(0177440L, TarUtils.parseOctal(testBytes, 0, testBytes.length));

        testBytes = new byte[]{0};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(testBytes, 0, testBytes.length));

        testBytes = new byte[]{'1', '2', '3'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(testBytes, 0, testBytes.length));
    }

    @Test
    public void testParseName() {
        byte[] testBytes = new byte[]{'h', 'e', 'l', 'l', 'o', 0};
        assertEquals("hello", TarUtils.parseName(testBytes, 0, testBytes.length));

        testBytes = new byte[0];
        assertNull(TarUtils.parseName(testBytes, 0, testBytes.length));

        testBytes = new byte[]{'h', 'e', 'l', 'l', 'o', '.', 't', 'x', 't'};
        assertEquals("hello.txt", TarUtils.parseName(testBytes, 0, testBytes.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "testName";
        byte[] buffer = new byte[10];

        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(name, new String(buffer, 0, offset));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long testValue = 255;
        byte[] buffer = new byte[10];

        TarUtils.formatUnsignedOctalString(testValue, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '3', '7', '7', '7'}, buffer);

        testValue = 1;
        TarUtils.formatUnsignedOctalString(testValue, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0'}, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        long testValue = 255;
        byte[] buffer = new byte[10];

        int offset = TarUtils.formatOctalBytes(testValue, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '3', '7', '7', '7', ' ', 0}, buffer);

        testValue = 1;
        offset = TarUtils.formatOctalBytes(testValue, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0'}, buffer);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long testValue = 255;
        byte[] buffer = new byte[10];

        int offset = TarUtils.formatLongOctalBytes(testValue, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '3', '7', '7', '7'}, buffer);

        testValue = 1;
        offset = TarUtils.formatLongOctalBytes(testValue, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0'}, buffer);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long testValue = 255;
        byte[] buffer = new byte[10];

        int offset = TarUtils.formatCheckSumOctalBytes(testValue, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '3', '7', '7', '7', ' ', '0'}, buffer);

        testValue = 1;
        offset = TarUtils.formatCheckSumOctalBytes(testValue, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0'}, buffer);
    }
}