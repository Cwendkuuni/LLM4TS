package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) '7', (byte) '5', (byte) '4', (byte) '3', (byte) '2', (byte) '1'};
        assertEquals(01754321L, TarUtils.parseOctal(buffer, 0, buffer.length));

        byte[] bufferNull = null;
        try {
            TarUtils.parseOctal(bufferNull, 0, 1);
            fail();
        } catch (IllegalArgumentException e) {
            //expected
        }

        byte[] bufferShort = new byte[]{(byte) '5'};
        try {
            TarUtils.parseOctal(bufferShort, 0, bufferShort.length);
            fail();
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{(byte) 'f', (byte) 'o', (byte) 'o', (byte) 0};
        assertEquals("foo", TarUtils.parseName(buffer, 0, buffer.length));

        byte[] bufferNull = null;
        try {
            TarUtils.parseName(bufferNull, 0, 1);
            fail();
        } catch (NullPointerException e) {
            //expected
        }

        byte[] bufferShort = new byte[]{(byte) 'f'};
        try {
            TarUtils.parseName(bufferShort, 0, bufferShort.length);
            fail();
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testFormatNameBytes() {
        String name = "foo";
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 5;
        int result = TarUtils.formatNameBytes(name, buffer, offset, length);
        assertEquals("foo", new String(buffer, offset, result - offset));

        name = "";
        offset = 0;
        length = 5;
        result = TarUtils.formatNameBytes(name, buffer, offset, length);
        assertArrayEquals(new byte[]{0, 0, 0, 0, 0}, buffer, 0, length);

        name = "foo";
        offset = 0;
        length = 1;
        result = TarUtils.formatNameBytes(name, buffer, offset, length);
        assertEquals("f", new String(buffer, offset, result - offset));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 5;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 5;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        assertArrayEquals(new byte[]{(byte) '5'}, buffer, 0, length);

        value = 12345;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        assertArrayEquals(new byte[]{(byte) '011', (byte) '2', (byte) '3', (byte) '4', (byte) '5'}, buffer, 0, length);

        value = 2147483647L;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        assertArrayEquals(new byte[]{(byte) '03777777777'}, buffer, 0, length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 5;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 7;
        TarUtils.formatOctalBytes(value, buffer, offset, length);
        assertArrayEquals(new byte[]{(byte) '011', (byte) ' ', (byte) '0', (byte) '1', (byte) ' ', (byte) '0', (byte) '5'},
                buffer, 0, length);

        value = 12345;
        TarUtils.formatOctalBytes(value, buffer, offset, length);
        assertArrayEquals(new byte[]{(byte) '011', (byte) ' ', (byte) '012', (byte) ' ', (byte) '013', (byte) ' ', (byte) '014',
                (byte) ' ', (byte) '015'}, buffer, 0, length);

        value = 2147483647L;
        TarUtils.formatOctalBytes(value, buffer, offset, length);
        assertArrayEquals(new byte[]{(byte) '03777777776', (byte) ' ', (byte) '7'}, buffer, 0, length);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 5;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 6;
        TarUtils.formatLongOctalBytes(value, buffer, offset, length);
        assertArrayEquals(new byte[]{(byte) '011', (byte) ' ', (byte) '005'}, buffer, 0, length);

        value = 12345;
        TarUtils.formatLongOctalBytes(value, buffer, offset, length);
        assertArrayEquals(new byte[]{(byte) '011', (byte) ' ', (byte) '012', (byte) ' ', (byte) '013', (byte) ' ', (byte) '014',
                (byte) ' ', (byte) '015'}, buffer, 0, length);

        value = -12345;
        try {
            TarUtils.formatLongOctalBytes(value, buffer, offset, length);
            fail();
        } catch (IllegalArgumentException e) {
            //expected
        }
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 5;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 8;
        TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        assertArrayEquals(new byte[]{(byte) '0111', (byte) ' ', (byte) '005'}, buffer, 0, length);

        value = 12345;
        TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        assertArrayEquals(new byte[]{(byte) '0111', (byte) ' ', (byte) '0123', (byte) ' ', (byte) '0145'}, buffer, 0, length);

        value = 2147483647L;
        TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        assertArrayEquals(new byte[]{(byte) '03777777776', (byte) ' ', (byte) '3'}, buffer, 0, length);

        value = -12345;
        try {
            TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
            fail();
        } catch (IllegalArgumentException e) {
            //expected
        }
    }
}