package org.apache.commons.compress.archivers.tar;

import org.apache.commons.compress.utils.TestUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) 0755, (byte) 0644, (byte) 0, (byte) ' '};
        long result = TarUtils.parseOctal(buffer, 0, 4);
        assertEquals(0755644L, result);

        buffer = new byte[]{(byte) 0755, (byte) 0644, (byte) 0x00, (byte) ' '};
        result = TarUtils.parseOctal(buffer, 0, 4);
        assertEquals(0755644L, result);

        buffer = new byte[]{(byte) 0, (byte) ' '};
        try {
            result = TarUtils.parseOctal(buffer, 0, 2);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        buffer = new byte[]{(byte) 0755, (byte) 0644, (byte) 0x01, (byte) ' '};
        try {
            result = TarUtils.parseOctal(buffer, 0, 4);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        buffer = new byte[]{(byte) 0755, (byte) 0644, (byte) 0x01, (byte) 'a'};
        try {
            result = TarUtils.parseOctal(buffer, 0, 4);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer1 = new byte[]{(byte) 0755, (byte) 0644, (byte) 0x00, (byte) ' '};
        long result = TarUtils.parseOctalOrBinary(buffer1, 0, 4);
        assertEquals(0755644L, result);

        byte[] buffer2 = new byte[]{(byte) 0x80, (byte) 0x55, (byte) 0x01, (byte) 0x55};
        result = TarUtils.parseOctalOrBinary(buffer2, 0, 4);
        assertEquals(101565L, result);

        byte[] buffer3 = new byte[]{(byte) 0, (byte) ' '};
        try {
            result = TarUtils.parseOctalOrBinary(buffer3, 0, 2);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] buffer4 = new byte[]{(byte) 0x80, (byte) 0x55, (byte) 0x01, (byte) 0x55};
        try {
            result = TarUtils.parseOctalOrBinary(buffer4, 0, 3);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] buffer5 = new byte[]{(byte) 0x80, (byte) 0x55, (byte) 0x01, (byte) 0x55, (byte) 0x01};
        try {
            result = TarUtils.parseOctalOrBinary(buffer5, 0, 5);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseBoolean() {
        byte[] buffer1 = new byte[]{(byte) 0x01};
        boolean result = TarUtils.parseBoolean(buffer1, 0);
        assertTrue(result);

        byte[] buffer2 = new byte[]{(byte) 0};
        result = TarUtils.parseBoolean(buffer2, 0);
        assertFalse(result);

        byte[] buffer3 = new byte[]{(byte) 0x01, (byte) ' '};
        try {
            result = TarUtils.parseBoolean(buffer3, 0);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer1 = new byte[]{(byte) 0x61, (byte) 0x73, (byte) 0x73, (byte) 0x65, (byte) 0x74, (byte) 0x2D, (byte) 0x61, (byte) 0x74, (byte) 0x65, (byte) 0x72, (byte) 0x00};
        String result = TarUtils.parseName(buffer1, 0, 12);
        assertEquals("example-data", result);

        byte[] buffer2 = new byte[]{(byte) 0x61, (byte) 0x73, (byte) 0x73, (byte) 0x65, (byte) 0x74, (byte) 0x2D, (byte) 0x61, (byte) 0x74, (byte) 0x65, (byte) 0x72, (byte) 0x01, (byte) 0x2E, (byte) 0x64, (byte) 0x65, (byte) 0x6D, (byte) 0x00};
        result = TarUtils.parseName(buffer2, 0, 16);
        assertEquals("example-data.demo", result);

        byte[] buffer3 = new byte[]{(byte) 0};
        try {
            result = TarUtils.parseName(buffer3, 0, 0);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatNameBytes() {
        String name = "example-data";
        byte[] buffer = new byte[12];
        int result = TarUtils.formatNameBytes(name, buffer, 0, 12);
        assertEquals(12, result);

        String name2 = "example-data.demo";
        byte[] buffer2 = new byte[16];
        result = TarUtils.formatNameBytes(name2, buffer2, 0, 16);
        assertEquals(16, result);

        byte[] buffer3 = new byte[1];
        try {
            result = TarUtils.formatNameBytes(name, buffer3, 0, 1);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 3686400;
        byte[] buffer = new byte[11];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, 10);

        for (int i = 0; i < 10; i++) {
            assertEquals((byte) (i+48), buffer[i]);
        }
        assertEquals((byte) 0, buffer[10]);
        assertEquals((byte) ' ', buffer[11]);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 3686400;
        byte[] buffer = new byte[13];
        int result = TarUtils.formatOctalBytes(value, buffer, 0, 13);

        for (int i = 0; i < 10; i++) {
            assertEquals((byte) (i+48), buffer[i]);
        }
        assertEquals((byte) ' ', buffer[10]);
        assertEquals((byte) ' ', buffer[11]);
        assertEquals((byte) 0, buffer[12]);
        assertEquals(13, result);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 3686400;
        byte[] buffer = new byte[12];
        int result = TarUtils.formatLongOctalBytes(value, buffer, 0, 12);

        for (int i = 0; i < 10; i++) {
            assertEquals((byte) (i+48), buffer[i]);
        }
        assertEquals((byte) ' ', buffer[10]);
        assertEquals(12, result);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 3686400;
        byte[] buffer = new byte[12];
        int result = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, 12);

        for (int i = 0; i < 11; i++) {
            if (i < 10) {
                assertEquals((byte) (i+48), buffer[i]);
            } else {
                assertEquals((byte) 40, buffer[i]);
            }
        }
        assertEquals(12, result);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{(byte) 0x75, (byte) 0x6e, (byte) 0x6b, (byte) 0x6e, (byte) 0x64, (byte) 0x6f, (byte) 0x6d, (byte) 0x70, (byte) 0x65, (byte) 0x72, (byte) 0x61, (byte) 0x73, (byte) 0x74, (byte) 0x65, (byte) 0x72, (byte) 0x2d, (byte) 0x61, (byte) 0x74, (byte) 0x74, (byte) 0x72, (byte) 0x61, (byte) 0x63, (byte) 0x68, (byte) 0x65, (byte) 0x6D, (byte) 0x65, (byte) 0x6D, (byte) 0x65, (byte) 0x6E, (byte) 0x74, (byte) 0x2D, (byte) 0x61, (byte) 0x74, (byte) 0x74, (byte) 0x72, (byte) 0x61, (byte) 0x63, (byte) 0x68, (byte) 0x65, (byte) 0x6D, (byte) 0x65, (byte) 0x21, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x2};
        long checksum = TarUtils.computeCheckSum(buffer);
        long computedChecksum = 0;
        for (byte b : buffer) {
            computedChecksum += TarUtils.BYTE_MASK & b;
        }
        assertEquals(checksum, computedChecksum);

        byte[] buffer2 = new byte[]{(byte) 0x78, (byte) 0x21, (byte) 0x10, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x1, (byte) 0x1, (byte) 0x1, (byte) 0x1, (byte) 0x1, (byte) 0x1};
        checksum = TarUtils.computeCheckSum(buffer2);
        computedChecksum = 0;
        for (byte b : buffer2) {
            computedChecksum += TarUtils.BYTE_MASK & b;
        }
        assertEquals(checksum, computedChecksum);
    }
}