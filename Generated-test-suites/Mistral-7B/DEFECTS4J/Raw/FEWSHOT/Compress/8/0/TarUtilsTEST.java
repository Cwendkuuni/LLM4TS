package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] testData = new byte[]{-65, '7', '5', '1', '1', '0', '3', '1', '5', '0', '0', '2', '7', '0', '0'};
        assertEquals(0177511031502L, TarUtils.parseOctal(testData, 0, testData.length));

        testData = new byte[]{'0'};
        assertEquals(0L, TarUtils.parseOctal(testData, 0, testData.length));

        testData = new byte[]{'0', '7', '0'};
        assertEquals(0170L, TarUtils.parseOctal(testData, 0, testData.length));

        testData = new byte[]{'7', '7', '7'};
        assertEquals(0777L, TarUtils.parseOctal(testData, 0, testData.length));

        testData = new byte[]{'5', '1', '3'};
        assertEquals(01531L, TarUtils.parseOctal(testData, 0, testData.length));

        testData = new byte[]{'1', '2', '0'};
        assertEquals(01201L, TarUtils.parseOctal(testData, 0, testData.length));

        testData = new byte[]{'\0'};
        try {
            TarUtils.parseOctal(testData, 0, testData.length);
            assert False();
        } catch (IllegalArgumentException e) {
            // expected
        }

        testData = new byte[]{'8'};
        try {
            TarUtils.parseOctal(testData, 0, testData.length);
            assert False();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseName() {
        byte[] testData = new byte[]{'0', '1', '2', '3', '4', '5'};
        assertEquals("012345", TarUtils.parseName(testData, 0, testData.length));

        testData = new byte[]{'0', '0'};
        assertTrue(TarUtils.parseName(testData, 0, testData.length).isEmpty());

        testData = new byte[]{'0', '1', '\0'};
        assertTrue(TarUtils.parseName(testData, 0, testData.length).isEmpty());

        testData = new byte[]{'1', '\0'};
        assertNull(TarUtils.parseName(testData, 0, testData.length));

        testData = new byte[]{'0', '1', '2', '3'};
        assertEquals("0123", TarUtils.parseName(testData, 0, testData.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "testName";
        byte[] buffer = new byte[name.length() + 1];

        TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertArrayEquals(name.getBytes(), buffer, 0, buffer.length);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 0x0123456789ABCDEF0L;

        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);

        byte[] expected = new byte[]{'0', '1', '2', '0', '3', '4', '5', '6', '7', '7', '0', '0'};
        assertArrayEquals(expected, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 0x0123456789ABCDEF0L;

        byte[] buffer = new byte[12];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);

        byte[] expected = new byte[]{'0', '1', '2', '0', '3', '4', '5', '6', '7', '7', '0', ' ', '0'};
        assertArrayEquals(expected, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 0x0123456789ABCDEF0L;

        byte[] buffer = new byte[11];
        TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);

        byte[] expected = new byte[]{'0', '1', '2', '0', '3', '4', '5', '6', '7', '7'};
        assertArrayEquals(expected, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long checksum = 0x123456789ABCDEF0L;

        byte[] buffer = new byte[13];
        TarUtils.formatCheckSumOctalBytes(checksum, buffer, 0, buffer.length);

        byte[] expected = new byte[]{'0', '1', '2', '0', '3', '4', '5', '6', '7', '7', '0', ' ', '0'};
        assertArrayEquals(expected, buffer, 0, buffer.length);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] data = new byte[]{'a', 'b', 'c', 'd'};
        long checksum = TarUtils.computeCheckSum(data);
        assertEquals(0xABCDABCDL, checksum);
    }
}