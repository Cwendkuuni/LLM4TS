package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.utils.ByteStreams;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for TarUtils.
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method.
     */
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '7', '5', '5', '5', '5', '5', '0', '0'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(077555550L, result);

        buffer = new byte[]{'0', '1'};
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        buffer = new byte[]{'0', '7', '7', '5', '5', '5', '5', '5', '1'};
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        buffer = new byte[]{'0'};
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        buffer = new byte[0];
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test parseOctalOrBinary method.
     */
    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer = new byte[]{'0', '1', '1', '0', '1', '1', '1', '1', '1', '1'};
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(10110111111L, result);

        buffer = new byte[]{'0', '1', '1', '0', '1', '1', '1', '1', '1', '1', '1'};
        try {
            TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        byte[] binaryBuffer = new byte[]{(byte) 0x80, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
        result = TarUtils.parseOctalOrBinary(binaryBuffer, 0, binaryBuffer.length);
        assertEquals(0x0102030405060708L, result);

        byte[] binaryBufferTooLarge = new byte[]{(byte) 0x80, 0x01, (byte) 0xFF, 0x01};
        try {
            TarUtils.parseOctalOrBinary(binaryBufferTooLarge, 0, binaryBufferTooLarge.length);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test parseBoolean method.
     */
    @Test
    public void testParseBoolean() {
        byte[] bufferTrue = new byte[]{'1'};
        assertTrue(TarUtils.parseBoolean(bufferTrue, 0));

        byte[] bufferFalse = new byte[]{'0'};
        assertFalse(TarUtils.parseBoolean(bufferFalse, 0));

        byte[] bufferTrailing = new byte[]{'0', '1'};
        assertTrue(TarUtils.parseBoolean(bufferTrailing, 0));

        byte[] bufferLeading = new byte[]{'1', '0'};
        assertTrue(TarUtils.parseBoolean(bufferLeading, 0));
    }

    /**
     * Test parseName method.
     */
    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'a', '.', 'b', '.', 'c'};
        String name = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("a.b.c", name);

        buffer = new byte[]{'/'};
        name = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("/", name);

        buffer = new byte[]{'a', '.'};
        name = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("a.", name);

        buffer = new byte[]{'a'};
        name = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("a", name);

        buffer = new byte[0];
        name = TarUtils.parseName(buffer, 0, buffer.length);
        assertNull(name);
    }

    /**
     * Test formatNameBytes method.
     */
    @Test
    public void testFormatNameBytes() {
        String name = "a.b/c.d";
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        for (int i = 0; i < offset; ++i) {
            assertEquals((byte) name.charAt(i), buffer[i]);
        }
        for (int i = offset; i < buffer.length; ++i) {
            assertEquals(0, buffer[i]);
        }
    }

    /**
     * Test formatUnsignedOctalString method.
     */
    @Test
    public void testFormatUnsignedOctalString() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int length = TarUtils.formatUnsignedOctalString(12345, output.getBuffer(), 0, output.size());
        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        long result = TarUtils.parseOctalOrBinary(input.readAllBytes(), 0, input.available());
        assertEquals(12345L, result);
    }

    /**
     * Test formatOctalBytes method.
     */
    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatOctalBytes(012345, buffer, 0, buffer.length);
        assertEquals(11, offset);
        for (int i = 0; i < offset; ++i) {
            assertEquals((byte) '0', buffer[i]);
            assertEquals((byte) (i < 9 ? '0' + (char) (buffer[i] & 0x07) : buffer[i]), buffer[i]);
        }
        for (int i = offset; i < buffer.length; ++i) {
            assertEquals(0, buffer[i]);
        }
    }

    /**
     * Test formatLongOctalOrBinaryBytes method.
     */
    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatLongOctalOrBinaryBytes(0x12345678901234L, buffer, 0, buffer.length);
        assertEquals(11, offset);
        for (int i = 0; i < offset; ++i) {
            assertEquals((byte) (i < 9 ? '0' + (char) (buffer[i] & 0x07) : buffer[i]), buffer[i]);
        }
        for (int i = offset; i < buffer.length; ++i) {
            assertEquals(0, buffer[i]);
        }

        byte[] binaryBuffer = new byte[8];
        TarUtils.formatLongOctalOrBinaryBytes(0x12345678901234L, binaryBuffer, 0, binaryBuffer.length);
        byte firstByte = binaryBuffer[0];
        assertEquals(0x80 | (byte) (0x12 >> 5), firstByte);
        for (int i = 1; i < binaryBuffer.length; ++i) {
            assertEquals((byte) (0xFF & (byte) (0x12345678901234L >> (8 * (binaryBuffer.length - i - 1)))), binaryBuffer[i]);
        }
    }

    /**
     * Test computeCheckSum method.
     */
    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'a', 'b', 'c', 'd'};
        long checkSum = TarUtils.computeCheckSum(buffer);
        long expected = 0xABCDEFABC + 0xABCDEFAB + 0xABCDEFAB + 0xABCDEFAB;
        assertEquals(expected, checkSum);
    }
}