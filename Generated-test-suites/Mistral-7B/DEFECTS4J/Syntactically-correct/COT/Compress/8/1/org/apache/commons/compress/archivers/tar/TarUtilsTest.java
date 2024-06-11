package org.apache.commons.compress.archivers.tar;

import org.apache.commons.compress.utils.TestUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * TarUtilsTest.java
 */
public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '7', '7', '7', '7', '0'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(01755775770L, result);

        byte[] buffer1 = new byte[]{'0', '1', '2', '3', '4'};
        try {
            TarUtils.parseOctal(buffer1, 0, buffer1.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        byte[] buffer2 = new byte[]{'0'};
        try {
            TarUtils.parseOctal(buffer2, 0, buffer2.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'a', 'b', 'c', 'd', 'e', '0'};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("abcde", result);

        byte[] buffer1 = new byte[]{'a', 'b', 'c', 'd', 'e'};
        try {
            TarUtils.parseName(buffer1, 0, buffer1.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        byte[] buffer2 = new byte[]{'a', 'b', 'c', 'd', 'e', 'f'};
        String result1 = TarUtils.parseName(buffer2, 0, 5);
        assertEquals("abcde", result1);
        String result2 = TarUtils.parseName(buffer2, 0, 6);
        assertEquals("abcde", result2);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "abcdefg";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(9, offset);
        byte[] result = new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', '0', '0', '0'};
        assertArrayEquals(result, buffer, 0, result.length);

        String name1 = "abcdefg";
        byte[] buffer1 = new byte[5];
        int offset1 = TarUtils.formatNameBytes(name1, buffer1, 0, buffer1.length);
        assertEquals(4, offset1);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123456789012345L;
        byte[] buffer = new byte[20];
        int offset = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertEquals(18, offset);
        byte[] result = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '7', '5', '6', '7', '8', '9', '0', '1', '2', '3'};
        assertArrayEquals(result, buffer, 0, result.length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123456789012345L;
        byte[] buffer = new byte[16];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(14, offset);
        byte[] result = new byte[]{'0', '1', '2', '3', '4', '5', '7', '5', '6', '7', '8', '9', '0', '1', '2', '3', ' '};
        assertArrayEquals(result, buffer, 0, result.length);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123456789012345L;
        byte[] buffer = new byte[16];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(14, offset);
        byte[] result = new byte[]{'0', '1', '2', '3', '4', '5', '7', '5', '6', '7', '8', '9', '0', '1', '2', '3', '0'};
        assertArrayEquals(result, buffer, 0, result.length);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'a', 'b', 'c', 'd', 'e'};
        long checksum = TarUtils.computeCheckSum(buffer);
        assertEquals(-4347954634692222201L, checksum);
    }
}