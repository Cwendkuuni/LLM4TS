package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.*;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '7', (byte) '5', (byte) '5', (byte) '4'};
        long expected = 017554;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, result);

        buffer = new byte[]{(byte) '0', (byte) '1', (byte) '0', (byte) '1', (byte) '1', (byte) '1', (byte) '1'};
        expected = 0111111;
        result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, result);

        buffer = new byte[]{(byte) '7', (byte) '7', (byte) '7'};
        try {
            result = TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{(byte) 'm', (byte) 'y', (byte) 'T', (byte) 'e', (byte) 's', (byte) 't', (byte) '.'};
        String expected = "myTest.";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, result);

        buffer = new byte[]{(byte) '.', (byte) '.', (byte) '.', (byte) '.'};
        result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("", result);

        buffer = new byte[]{(byte) ' '};
        result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "myTest.";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        byte[] expected = new byte[]{(byte) 'm', (byte) 'y', (byte) 'T', (byte) 'e', (byte) 's', (byte) 't', (byte) '.', (byte) '.', (byte) '.'};
        assertArrayEquals(expected, buffer, 0, offset);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 42;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{(byte) '0', (byte) '5', (byte) '2'};
        assertArrayEquals(expected, new byte[]{buffer[0], buffer[1], buffer[2]});

        value = 60451;
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        expected = new byte[]{(byte) '0', (byte) '1', (byte) '0', (byte) '4', (byte) '5', (byte) '1'};
        assertArrayEquals(expected, new byte[]{buffer[0], buffer[1], buffer[2], buffer[3], buffer[4], buffer[5]});
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 42;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{(byte) '0', (byte) '5', (byte) '2', (byte) ' ', (byte) '0'};
        assertArrayEquals(expected, buffer, 0, offset);

        value = 60451;
        offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        expected = new byte[]{(byte) '0', (byte) '1', (byte) '0', (byte) '4', (byte) '5', (byte) '1', (byte) ' ', (byte) '0'};
        assertArrayEquals(expected, buffer, 0, offset);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 42;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{(byte) '0', (byte) '5', (byte) '2', (byte) ' '};
        assertArrayEquals(expected, buffer, 0, offset);

        value = 60451;
        offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        expected = new byte[]{(byte) '0', (byte) '1', (byte) '0', (byte) '4', (byte) '5', (byte) '1'};
        assertArrayEquals(expected, buffer, 0, offset);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 42;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        byte[] expected = new byte[]{(byte) '0', (byte) '5', (byte) '2', (byte) ' ', (byte) '0'};
        assertArrayEquals(expected, buffer, 0, offset);

        value = 60451;
        offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        expected = new byte[]{(byte) '0', (byte) '1', (byte) '0', (byte) '4', (byte) '5', (byte) '1', (byte) ' ', (byte) '0'};
        assertArrayEquals(expected, buffer, 0, offset);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{(byte) 'm', (byte) 'y', (byte) 'T', (byte) 'e', (byte) 's', (byte) 't', (byte) '.'};
        long expected = TarUtils.computeCheckSum(buffer);
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, result);

        buffer = new byte[0];
        result = TarUtils.computeCheckSum(buffer);
        assertEquals(0, result);
    }
}