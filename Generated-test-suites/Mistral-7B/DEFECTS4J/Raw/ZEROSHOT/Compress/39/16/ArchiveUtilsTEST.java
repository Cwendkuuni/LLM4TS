package org.apache.commons.compress.utils;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("main.c");
        entry.setSize(2000);
        entry.setDirectory(false);

        String result = ArchiveUtils.toString(entry);
        String expected = "- 2000 main.c";

        assertEquals(expected, result);

        ArchiveEntry directoryEntry = new ArchiveEntry("testfiles");
        directoryEntry.setSize(100);
        directoryEntry.setDirectory(true);

        result = ArchiveUtils.toString(directoryEntry);
        expected = "d 100 testfiles/";

        assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = "test".getBytes();

        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testMatchAsciiBufferWithOffset() {
        String expected = "test";
        byte[] buffer = new byte[]{0x74, 0x65, 0x73, 0x74, 0x65, 0x64, 0x00};

        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length - 1));
    }

    @Test(expected = RuntimeException.class)
    public void testMatchAsciiBufferWithInvalidCharset() {
        String expected = "test";
        byte[] buffer = "test".getBytes();

        try {
            ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length, "invalidCharset");
            fail();
        } catch (RuntimeException e) {
            // Expected
        }
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "test";
        byte[] result = ArchiveUtils.toAsciiBytes(inputString);

        assertArrayEquals(result, "test".getBytes());
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = new byte[]{0x74, 0x65, 0x73, 0x74, 0x65, 0x64};
        String result = ArchiveUtils.toAsciiString(inputBytes);

        assertEquals("test", result);
    }

    @Test
    public void testToAsciiStringWithOffsetAndLength() {
        byte[] inputBytes = new byte[]{0x74, 0x65, 0x73, 0x74, 0x65, 0x64, 0x00};
        String result = ArchiveUtils.toAsciiString(inputBytes, 0, 6);

        assertEquals("test", result);
    }

    @Test(expected = RuntimeException.class)
    public void testToAsciiStringWithInvalidCharset() {
        byte[] inputBytes = new byte[]{0x74, 0x65, 0x73, 0x74, 0x65, 0x64};

        try {
            ArchiveUtils.toAsciiString(inputBytes, 0, inputBytes.length, "invalidCharset");
            fail();
        } catch (RuntimeException e) {
            // Expected
        }
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{0x74, 0x65, 0x73, 0x74, 0x65, 0x64, 0x00};
        byte[] buffer2 = new byte[]{0x74, 0x65, 0x73, 0x74, 0x65, 0x64, 0x00};

        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsEqualWithOffsetsAndLengths() {
        byte[] buffer1 = new byte[]{0x74, 0x65, 0x73, 0x74, 0x65, 0x64, 0x00};
        byte[] buffer2 = new byte[]{0x74, 0x65, 0x73, 0x74, 0x65, 0x64, 0x00};

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    @Test
    public void testIsEqualWithDifferentLengths() {
        byte[] buffer1 = new byte[]{0x74, 0x65, 0x73, 0x74, 0x65, 0x64, 0x00};
        byte[] buffer2 = new byte[]{0x74, 0x65, 0x73, 0x74, 0x65, 0x64};

        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsEqualWithNullTrailingBytesIgnored() {
        byte[] buffer1 = new byte[]{0x74, 0x65, 0x73, 0x74, 0x65, 0x64, 0x00, 0x00, 0x00};
        byte[] buffer2 = new byte[]{0x74, 0x65, 0x73, 0x74, 0x65, 0x64};

        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, true));
    }

    @Test
    public void testIsEqualWithNullTrailingBytesNotIgnored() {
        byte[] buffer1 = new byte[]{0x74, 0x65, 0x73, 0x74, 0x65, 0x64, 0x00, 0x00, 0x00};
        byte[] buffer2 = new byte[]{0x74, 0x65, 0x73, 0x74, 0x65, 0x64};

        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsArrayZero() {
        byte[] array = new byte[0];

        assertTrue(ArchiveUtils.isArrayZero(array, 0));

        byte[] zeroArray = new byte[]{0, 0, 0, 0};

        assertTrue(ArchiveUtils.isArrayZero(zeroArray, zeroArray.length));
    }

    @Test
    public void testSanitize() {
        String input = "test\u0001\u0002\u0123\u01FF";
        String expected = "test????\u0123\u01FF";

        String result = ArchiveUtils.sanitize(input);

        assertEquals(expected, result);
    }
}