package org.apache.commons.compress.archivers.tar;

import org.apache.commons.compress.utils.TestUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * TarUtils Test File
 */
public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '0', '\u0000'};
        long expected = 017550L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, result);

        byte[] buffer1 = new byte[]{'0', '1', '7', '7', '7', '5', '5', '0', '\u0000'};
        long expected1 = 01177550L;
        long result1 = TarUtils.parseOctal(buffer1, 0, buffer1.length);
        assertEquals(expected1, result1);

        byte[] buffer2 = new byte[]{'5', '5', '5', '\u0000'};
        try {
            TarUtils.parseOctal(buffer2, 0, buffer2.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] buffer3 = new byte[]{'1', '2', '3'};
        try {
            TarUtils.parseOctal(buffer3, 0, buffer3.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = TestUtils.toBytes("name/with/extension.ext");
        String expected = "name/with/extension.ext";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, result);

        byte[] buffer1 = TestUtils.toBytes("name");
        String expected1 = "name";
        String result1 = TarUtils.parseName(buffer1, 0, buffer1.length);
        assertEquals(expected1, result1);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "name/with/extension.ext";
        byte[] expected = TestUtils.toBytes("name/with/extension.ext");
        byte[] buffer = new byte[expected.length];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertArrayEquals(expected, buffer);
        assertEquals(offset, expected.length);

        String name1 = "name";
        byte[] expected1 = TestUtils.toBytes("name");
        byte[] buffer1 = new byte[expected1.length];
        int offset1 = TarUtils.formatNameBytes(name1, buffer1, 0, buffer1.length);
        assertArrayEquals(expected1, buffer1);
        assertEquals(offset1, expected1.length);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 10L;
        byte[] expected = new byte[]{'0', '0', '0', '1', '0', '\u0000'};
        byte[] buffer = new byte[expected.length];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(expected, buffer);

        long value1 = 17550L;
        byte[] expected1 = new byte[]{'0', '1', '7', '5', '5', '0', '\u0000'};
        byte[] buffer1 = new byte[expected1.length];
        TarUtils.formatUnsignedOctalString(value1, buffer1, 0, buffer1.length);
        assertArrayEquals(expected1, buffer1);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 10L;
        byte[] expected = new byte[]{'0', '0', '0', '1', '0', ' ', '\u0000'};
        byte[] buffer = new byte[expected.length];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(expected, buffer);

        long value1 = 17550L;
        byte[] expected1 = new byte[]{'0', '1', '7', '5', '5', '0', ' ', '\u0000'};
        byte[] buffer1 = new byte[expected1.length];
        TarUtils.formatOctalBytes(value1, buffer1, 0, buffer1.length);
        assertArrayEquals(expected1, buffer1);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 10L;
        byte[] expected = new byte[]{'0', '0', '0', '1', '0', ' ', '\u0000'};
        byte[] buffer = new byte[expected.length];
        TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(expected, buffer);

        long value1 = 17550L;
        byte[] expected1 = new byte[]{'0', '1', '1', '1', '1', '7', '5', '5', '0', ' ', '\u0000'};
        byte[] buffer1 = new byte[expected1.length];
        TarUtils.formatLongOctalBytes(value1, buffer1, 0, buffer1.length);
        assertArrayEquals(expected1, buffer1);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 10L;
        byte[] expected = new byte[]{'\u0000', '0', '0', '0', '1', '0', '\u0000', ' ', '\u0000'};
        byte[] buffer = new byte[expected.length];
        TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(expected, buffer);

        long value1 = 17550L;
        byte[] expected1 = new byte[]{'\u0000', '0', '1', '1', '1', '7', '5', '5', '0', '\u0000', ' ', '\u0000'};
        byte[] buffer1 = new byte[expected1.length];
        TarUtils.formatCheckSumOctalBytes(value1, buffer1, 0, buffer1.length);
        assertArrayEquals(expected1, buffer1);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = TestUtils.toBytes("name/with/extension.ext\nsize\nmode\n\u0000\u0000");
        long expected = TarUtils.computeCheckSum(buffer);
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, result);
    }
}