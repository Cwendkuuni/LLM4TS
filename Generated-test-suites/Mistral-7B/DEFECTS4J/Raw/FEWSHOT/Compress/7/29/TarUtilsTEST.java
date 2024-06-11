package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] testData = new byte[]{'0', '7', '7', '5', '0', '0', '0', '0'};
        assertEquals(0177500L, TarUtils.parseOctal(testData, 0, testData.length));

        testData = new byte[]{'7', '5', '0', '0'};
        assertEquals(017500L, TarUtils.parseOctal(testData, 0, testData.length));

        testData = new byte[]{'1', '0', '0', '7', '5', '0', '0', '0'};
        assertEquals(0100750L, TarUtils.parseOctal(testData, 0, testData.length));

        testData = new byte[]{'0', '0'};
        assertNull(TarUtils.parseOctal(testData, 0, testData.length));

        testData = new byte[]{'1', '2'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(testData, 0, testData.length));
    }

    @Test
    public void testParseName() {
        byte[] testData = new byte[]{'a', 'b', 'c'};
        assertEquals("abc", TarUtils.parseName(testData, 0, testData.length));

        testData = new byte[]{'a', ' ', 'b', ' ', 'c'};
        assertEquals("abc", TarUtils.parseName(testData, 0, testData.length));

        testData = new byte[]{'a', 'b', 'c', '.'};
        assertEquals("abc.", TarUtils.parseName(testData, 0, testData.length));

        testData = new byte[]{'a', 'b', 'c', 0};
        assertNull(TarUtils.parseName(testData, 0, testData.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "abc";
        byte[] expected = new byte[]{'a', 'b', 'c', 0, 0, 0, 0, 0};
        byte[] buffer = new byte[8];

        int offset = TarUtils.formatNameBytes(name, buffer, 0, expected.length);
        assertArrayEquals(expected, buffer, 0, expected.length);
        assertEquals(offset, expected.length);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 177500L;
        byte[] expected = new byte[]{'0', '1', '7', '7', '5', '0', '0'};

        byte[] buffer = new byte[7];
        int offset = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(expected, buffer, 0, expected.length);
        assertEquals(offset, buffer.length);

        value = 100750L;
        buffer = new byte[6];
        offset = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '0', '0', '7', '5'}, buffer, 0, buffer.length);
        assertEquals(offset, buffer.length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 177500L;
        byte[] expected = new byte[]{'0', '1', '7', '7', '5', '0', ' ', '0'};

        byte[] buffer = new byte[8];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(expected, buffer, 0, expected.length);
        assertEquals(offset, buffer.length);

        value = 100750L;
        buffer = new byte[7];
        offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '0', '0', '7', '5', ' ', '0'}, buffer, 0, buffer.length);
        assertEquals(offset, buffer.length);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 1775001L;
        byte[] expected = new byte[]{'0', '1', '7', '7', '5', '0', ' ', '0'};

        byte[] buffer = new byte[8];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(expected, buffer, 0, expected.length);
        assertEquals(offset, buffer.length);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] data = new byte[]{'a', 'b', 'c'};
        long expected = (0x00 & 0xFF) << 24 | (0x01 & 0xFF) << 16 | (0x02 & 0xFF) << 8 | (0x03 & 0xFF);
        assertEquals(expected, TarUtils.computeCheckSum(data));
    }
}