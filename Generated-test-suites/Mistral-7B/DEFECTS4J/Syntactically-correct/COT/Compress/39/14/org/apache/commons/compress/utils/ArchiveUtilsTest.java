package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

/**
 * Test class for ArchiveUtils
 */
public class ArchiveUtilsTest {

    /**
     * Test method for {@link ArchiveUtils#toString(ArchiveEntry)}
     */
    @Test
    public void testToString() {
        ArchiveEntry archiveEntry = new ArchiveEntry("main.c");
        archiveEntry.setSize(2000);
        archiveEntry.setDirectory(false);

        String actual = ArchiveUtils.toString(archiveEntry);
        String expected = "- 2000 main.c";

        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link ArchiveUtils#matchAsciiBuffer(String, byte[], int, int)}
     */
    @Test
    public void testMatchAsciiBuffer() {
        String expected = "expectedString";
        byte[] buffer = "expectedString".getBytes();

        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));

        byte[] wrongBuffer = new byte[buffer.length + 1];
        System.arraycopy(buffer, 0, wrongBuffer, 0, buffer.length);
        wrongBuffer[buffer.length] = (byte) 0x01;

        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, wrongBuffer));
    }

    /**
     * Test method for {@link ArchiveUtils#matchAsciiBuffer(String, byte[])}
     */
    @Test
    public void testMatchAsciiBufferWithOffset() {
        String expected = "expectedString";
        byte[] buffer = "expectedString".getBytes();

        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));

        byte[] wrongBuffer = new byte[buffer.length + 1];
        System.arraycopy(buffer, 0, wrongBuffer, 0, buffer.length);
        wrongBuffer[buffer.length] = (byte) 0x01;

        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, wrongBuffer));
    }

    /**
     * Test method for {@link ArchiveUtils#toAsciiBytes(String)}
     */
    @Test
    public void testToAsciiBytes() {
        String inputString = "expectedString";
        byte[] expectedBytes = expectedString.getBytes(CharsetNames.US_ASCII);

        byte[] actualBytes = ArchiveUtils.toAsciiBytes(inputString);

        assertArrayEquals(expectedBytes, actualBytes);
    }

    /**
     * Test method for {@link ArchiveUtils#toAsciiString(byte[])}
     */
    @Test
    public void testToAsciiString() {
        String expected = "expectedString";
        byte[] buffer = expected.getBytes(CharsetNames.US_ASCII);

        String actual = ArchiveUtils.toAsciiString(buffer);

        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link ArchiveUtils#toAsciiString(byte[], int, int)}
     */
    @Test
    public void testToAsciiStringWithOffset() {
        String expected = "expectedString";
        byte[] buffer = expected.getBytes(CharsetNames.US_ASCII);

        String actual = ArchiveUtils.toAsciiString(buffer, 0, buffer.length);

        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link ArchiveUtils#isEqual(byte[], int, int, byte[], int, int, boolean)}
     */
    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[] {0x01, 0x02, 0x03, 0x04, 0x05};
        byte[] buffer2 = new byte[] {0x01, 0x02, 0x03, 0x04, 0x05};
        byte[] buffer3 = new byte[] {0x01, 0x02, 0x03, 0x04, 0x06};

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, false));

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true));
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, true));
    }

    /**
     * Test method for {@link ArchiveUtils#isEqual(byte[], int, int, byte[], int, int)}
     */
    @Test
    public void testIsEqualWithOffset() {
        byte[] buffer1 = new byte[] {0x01, 0x02, 0x03, 0x04, 0x05};
        byte[] buffer2 = new byte[] {0x01, 0x02, 0x03, 0x04, 0x05};
        byte[] buffer3 = new byte[] {0x01, 0x02, 0x03, 0x04, 0x06};

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, false));

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, true));

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length - 1, buffer2, 0, buffer2.length - 1, false));
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length - 1, buffer3, 0, buffer3.length - 1, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length - 1, buffer3, 0, buffer3.length, false));

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, buffer2.length - 1, buffer2.length, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, buffer3.length - 1, buffer3.length, false));

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, buffer2.length - 1, buffer2.length, true));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, buffer3.length - 1, buffer3.length, true));
    }

    /**
     * Test method for {@link ArchiveUtils#isArrayZero(byte[], int)}
     */
    @Test
    public void testIsArrayZero() {
        byte[] buffer1 = new byte[5];
        Arrays.fill(buffer1, (byte) 0x00);

        assertTrue(ArchiveUtils.isArrayZero(buffer1, buffer1.length));

        byte[] buffer2 = new byte[5];
        Arrays.fill(buffer2, (byte) 0x01);

        assertFalse(ArchiveUtils.isArrayZero(buffer2, buffer2.length));
    }

    /**
     * Test method for {@link ArchiveUtils#sanitize(String)}
     */
    @Test
    public void testSanitize() {
        String input = "expectedString\u0001\u0002";
        String expected = "expectedString?";

        String actual = ArchiveUtils.sanitize(input);

        assertEquals(expected, actual);
    }
}