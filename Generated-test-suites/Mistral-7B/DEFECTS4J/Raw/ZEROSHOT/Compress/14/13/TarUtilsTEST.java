package org.apache.commons.compress.archivers.tar;

import org.apache.commons.compress.utils.TestUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    private byte[] createTestBuffer(int length) {
        return TestUtils.generateRandomBytes(length);
    }

    @Test
    public void testParseOctal() {
        byte[] buffer = createTestBuffer(10);
        long result = TarUtils.parseOctal(buffer, 0, 5);
        assertEquals(012345L, result);

        try {
            TarUtils.parseOctal(buffer, 5, 5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected
        }

        try {
            TarUtils.parseOctal(buffer, 0, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected
        }

        try {
            TarUtils.parseOctal(buffer, 0, 11);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected
        }
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer = createTestBuffer(10);

        long result = TarUtils.parseOctalOrBinary(buffer, 0, 5);
        assertEquals(012345L, result);

        byte[] binaryBuffer = new byte[]{(byte) 0x81, (byte) 0x01, (byte) 0x23, (byte) 0x45};
        result = TarUtils.parseOctalOrBinary(binaryBuffer, 0, 4);
        assertEquals(16845L, result);

        try {
            TarUtils.parseOctalOrBinary(buffer, 5, 5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected
        }

        try {
            TarUtils.parseOctalOrBinary(buffer, 0, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected
        }

        try {
            TarUtils.parseOctalOrBinary(buffer, 0, 11);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected
        }
    }

    @Test
    public void testParseBoolean() {
        byte[] buffer = new byte[]{0x01};
        assertTrue(TarUtils.parseBoolean(buffer, 0));

        byte[] bufferFalse = new byte[]{0x00};
        assertFalse(TarUtils.parseBoolean(bufferFalse, 0));

        byte[] bufferLeadingNul = new byte[]{0x00};
        assertFalse(TarUtils.parseBoolean(bufferLeadingNul, 0));

        byte[] bufferTrailingNul = new byte[]{0x01, (byte) 0x00};
        assertTrue(TarUtils.parseBoolean(bufferTrailingNul, 0));

        byte[] bufferTrailingSpace = new byte[]{0x01, (byte) 0x20};
        assertTrue(TarUtils.parseBoolean(bufferTrailingSpace, 0));
    }

    @Test
    public void testParseName() {
        byte[] buffer = "TestName".getBytes();
        String name = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("TestName", name);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "TestName";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals("TestName", new String(buffer, 0, offset));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String expected = "0075114123";
        assertEquals(expected, new String(buffer, 0, buffer.length));
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        String expected = "0000075114123 ";
        assertEquals(expected, new String(buffer, 0, offset));
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 12345;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        String expected = "00012345";
        assertEquals(expected, new String(buffer, 0, offset));

        long largeValue = 123456789012345L;
        buffer = new byte[10];
        offset = TarUtils.formatLongOctalOrBinaryBytes(largeValue, buffer, 0, buffer.length);
        expected = "0177754645442101756245";
        assertEquals(expected, new String(buffer, 0, offset));

        long binaryValue = 123456789012345L;
        buffer = new byte[10];
        offset = TarUtils.formatLongOctalOrBinaryBytes(binaryValue, buffer, 0, buffer.length);
        expected = "\u0081\u0001\u0032\u0034\u0056\u0037\u0038\u0039\u0054\u0032";
        assertEquals(expected, new String(buffer, 0, offset));
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        String expected = "000075114123\u0000";
        assertEquals(expected, new String(buffer, 0, offset));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = "TestName\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000".getBytes();
        long checksum = TarUtils.computeCheckSum(buffer);
        long computedChecksum = 0;
        for (int i = 0; i < buffer.length; ++i) {
            computedChecksum += TarUtils.BYTE_MASK & buffer[i];
        }
        assertEquals(checksum, computedChecksum);
    }
}