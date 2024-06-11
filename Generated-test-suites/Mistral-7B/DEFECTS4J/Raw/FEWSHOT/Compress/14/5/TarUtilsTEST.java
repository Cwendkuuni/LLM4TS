package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) 0777, (byte) 0644, (byte) 0666, (byte) 0755, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
        long result;

        result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0777L, result);

        result = TarUtils.parseOctal(buffer, 1, buffer.length - 1);
        assertEquals(0644L, result);

        result = TarUtils.parseOctal(buffer, 2, buffer.length - 2);
        assertEquals(0666L, result);

        result = TarUtils.parseOctal(buffer, 3, buffer.length - 3);
        assertEquals(0755L, result);

        result = TarUtils.parseOctal(buffer, 7, buffer.length - 7);
        assertEquals(0L, result);

        result = TarUtils.parseOctal(buffer, 8, buffer.length - 8);
        assertEquals(0L, result);

        try {
            result = TarUtils.parseOctal(buffer, 0, 0);
            assertFalse("IllegalArgumentException expected", true);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            result = TarUtils.parseOctal(buffer, 1, 0);
            assertFalse("IllegalArgumentException expected", true);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            result = TarUtils.parseOctal(buffer, buffer.length, 1);
            assertFalse("IllegalArgumentException expected", true);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            result = TarUtils.parseOctal(buffer, 0, 2);
            assertFalse("IllegalArgumentException expected", true);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer1 = new byte[]{(byte) 077, (byte) 0};
        byte[] buffer2 = new byte[]{0x5a, (byte) 0x30, (byte) 0x31, (byte) 0x32, 0x40, 0};
        long result;

        result = TarUtils.parseOctalOrBinary(buffer1, 0, buffer1.length);
        assertEquals(0177_000L, result);

        result = TarUtils.parseOctalOrBinary(buffer2, 0, buffer2.length);
        assertEquals(0x5a303132L, result);

        try {
            result = TarUtils.parseOctalOrBinary(buffer2, 1, buffer2.length - 1);
            assertFalse("IllegalArgumentException expected", true);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            result = TarUtils.parseOctalOrBinary(buffer1, buffer1.length + 1, 1);
            assertFalse("IllegalArgumentException expected", true);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseBoolean() {
        byte[] buffer = new byte[]{1, (byte) 0};
        boolean result;

        result = TarUtils.parseBoolean(buffer, 0);
        assertTrue(result);

        result = TarUtils.parseBoolean(buffer, 1);
        assertFalse(result);

        try {
            result = TarUtils.parseBoolean(new byte[0][], 0);
            assertFalse("IllegalArgumentException expected", true);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{(byte) 0x68, (byte) 0x65, (byte) 0x6c, (byte) 0x6c, (byte) 0x6f, (byte) 0, (byte) 0};
        String name = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("hello", name);

        try {
            name = TarUtils.parseName(buffer, buffer.length + 1, buffer.length);
            assertFalse("IllegalArgumentException expected", true);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatNameBytes() {
        String name = "foo.bar.baz";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);

        assertEquals("foo.bar.baz", new String(buffer, 0, offset));

        // Test padding with trailing zeros
        byte[] buffer2 = new byte[16];
        offset = TarUtils.formatNameBytes(name, buffer2, 0, buffer2.length);

        assertEquals("foo.bar.baz          ", new String(buffer2, 0, offset));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 0x12345678L;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);

        assertEquals("01275634077", new String(buffer, 0, offset));
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 0x12345678L;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);

        assertEquals("01275634077\0 ", new String(buffer, 0, offset));
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 0x12345678L;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);

        assertEquals("012345678\0", new String(buffer, 0, offset));

        value = Long.parseUnsignedLong("1234567890123456789012345678901234567890", 8);
        offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);

        assertEquals("01234567\01234567\01234567\01234567\01234567\01234567\01234567\01234567\01234567\01234567\01234567\01234567", new String(buffer, 0, offset));
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 0x12345678L;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);

        assertEquals("01234567000 \0", new String(buffer, 0, offset));
    }
}