package org.apache.commons.compress.archivers.tar;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.apache.commons.compress.archivers.tar.TarUtils.BYTE_MASK;

/**
 * Test class for TarUtils utility class.
 */
public class TarUtilsTest {

    /**
     * Test for parseOctal method.
     */
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '4', ' ', '0', '7', '7', '7', ' ', '7', '5', '5', '4'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assertions.assertEquals(01755477754L, result);

        buffer = new byte[]{'0', '1', '2', '3'};
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            Assertions.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        buffer = new byte[]{'0', '1', '2'};
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            Assertions.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        buffer = new byte[0];
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            Assertions.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test for parseName method.
     */
    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'.' , 'a', '.', 'b', '.', 'c', '.', 'd', '.', 'e', '.', 'f', '.', 'g', 0};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        Assertions.assertEquals("a.b.c.d.e.f.g", result);

        buffer = new byte[0];
        try {
            TarUtils.parseName(buffer, 0, buffer.length);
            Assertions.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test for formatNameBytes method.
     */
    @Test
    public void testFormatNameBytes() {
        String name = "a.b.c.d.e.f.g";
        byte[] buffer = new byte[16];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);

        Assertions.assertEquals(16, offset);
        byte[] expectedBuffer = new byte[]{
                'a', '.', 'b', '.', 'c', '.', 'd', '.', 'e', '.', 'f', '.', 'g', 0, 0, 0, 0
        };
        Assertions.assertArrayEquals(expectedBuffer, buffer, 0, expectedBuffer.length);

        name = "";
        offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        Assertions.assertEquals(1, offset);
        Assertions.assertEquals(0, buffer[0]);
    }

    /**
     * Test for formatUnsignedOctalString method.
     */
    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[16];
        long value = 0x1234567890123456L;

        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        byte[] expectedBuffer = new byte[]{
                '0', '0', '0', '7', '5', '5', '6', '7', '5', '6', '6', '4', '5', '6', '7', '8',
                '9', '0', '1', '2', '3', '4', '5', '6'
        };
        Assertions.assertArrayEquals(expectedBuffer, buffer, 0, expectedBuffer.length);

        value = 0x0L;
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        Assertions.assertEquals(1, buffer.length);
        Assertions.assertEquals(0, buffer[0]);
    }

    /**
     * Test for formatOctalBytes method.
     */
    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[16];
        long value = 0x1234567890123456L;

        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        Assertions.assertEquals(16, offset);

        byte[] expectedBuffer = new byte[]{
                '0', '0', '0', '7', '5', '5', '6', '7', '5', '6', '6', '4', '5', '6', '7', '8',
                '9', '0', '1', '2', '3', '4', '5', '6', ' '
        };
        Assertions.assertArrayEquals(expectedBuffer, buffer, 0, expectedBuffer.length);
    }

    /**
     * Test for computeCheckSum method.
     */
    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{1, 2, 3, 4, 5};
        long checksum = TarUtils.computeCheckSum(buffer);

        long computedChecksum = 0;
        for (int i = 0; i < buffer.length; ++i) {
            computedChecksum += BYTE_MASK & buffer[i];
        }

        Assertions.assertEquals(checksum, computedChecksum);

        buffer = new byte[0];
        long checksum2 = TarUtils.computeCheckSum(buffer);
        Assertions.assertEquals(0, checksum2);
    }
}