package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = newArchiveEntry("test.txt", false, 1024, "test.txt");
        String result = ArchiveUtils.toString(entry);
        assertEquals("- 1024 test.txt\n", result);
    }

    @Test
    public void testToStringForDirectory() {
        ArchiveEntry entry = newArchiveEntry("test/", true, 0, "test/");
        String result = ArchiveUtils.toString(entry);
        assertEquals("d 0 test/\n", result);
    }

    @Test
    public void testMatchAsciiBufferNull() {
        assertFalse(ArchiveUtils.matchAsciiBuffer(null, new byte[0], 0, 0));
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] input = expected.getBytes(CharsetNames.US_ASCII);
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, input));
    }

    @Test
    public void testMatchAsciiBufferDifferentLength() {
        String expected = "test";
        byte[] input = expected.getBytes(CharsetNames.US_ASCII);
        byte[] other = new byte[input.length + 1];
        System.arraycopy(input, 0, other, 0, input.length);
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, other));
    }

    @Test
    public void testMatchAsciiBufferMismatch() {
        String expected = "test";
        byte[] input = expected.getBytes(CharsetNames.US_ASCII);
        byte[] other = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            other[i] = (byte) (input[i] + 1);
        }
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, other));
    }

    @Test
    public void testIsEqualNullBuffer() {
        byte[] buffer1 = null;
        byte[] buffer2 = new byte[0];
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, 0, buffer2, 0, buffer2.length, false));
    }

    @Test
    public void testIsEqualZeroLengthBuffers() {
        byte[] buffer1 = new byte[0];
        byte[] buffer2 = new byte[0];
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, 0, buffer2, 0, buffer2.length, false));
    }

    @Test
    public void testIsEqualSameBuffer() {
        byte[] buffer1 = new byte[]{"a".getBytes(CharsetNames.US_ASCII)[0]};
        byte[] buffer2 = new byte[]{"a".getBytes(CharsetNames.US_ASCII)[0]};
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
    }

    @Test
    public void testIsEqualDifferentBuffers() {
        byte[] buffer1 = new byte[]{"a".getBytes(CharsetNames.US_ASCII)[0]};
        byte[] buffer2 = new byte[]{"b".getBytes(CharsetNames.US_ASCII)[0]};
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
    }

    @Test
    public void testIsEqualDifferentOffsetsSameBuffer() {
        byte[] buffer = new byte[]{"a".getBytes(CharsetNames.US_ASCII)[0], "b".getBytes(CharsetNames.US_ASCII)[0]};
        assertTrue(ArchiveUtils.isEqual(buffer, 0, buffer.length, buffer, 1, buffer.length, false));
    }

    @Test
    public void testIsEqualDifferentOffsetsDifferentBuffers() {
        byte[] buffer1 = new byte[]{"a".getBytes(CharsetNames.US_ASCII)[0]};
        byte[] buffer2 = new byte[]{"b".getBytes(CharsetNames.US_ASCII)[0]};
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
    }

    @Test
    public void testIsEqualWithNullTrailing() {
        byte[] buffer1 = new byte[]{"a".getBytes(CharsetNames.US_ASCII)[0], 0};
        byte[] buffer2 = new byte[]{"a".getBytes(CharsetNames.US_ASCII)[0], 0};
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    @Test
    public void testIsEqualWithNullTrailingInFirstBuffer() {
        byte[] buffer1 = new byte[]{"a".getBytes(CharsetNames.US_ASCII)[0], 0};
        byte[] buffer2 = new byte[]{"a".getBytes(CharsetNames.US_ASCII)[0]};
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    @Test
    public void testIsEqualWithNullTrailingInSecondBuffer() {
        byte[] buffer1 = new byte[]{"a".getBytes(CharsetNames.US_ASCII)[0]};
        byte[] buffer2 = new byte[]{"a".getBytes(CharsetNames.US_ASCII)[0], 0};
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    @Test
    public void testIsEqualWithMismatchTrailing() {
        byte[] buffer1 = new byte[]{"a".getBytes(CharsetNames.US_ASCII)[0], 0};
        byte[] buffer2 = new byte[]{"a".getBytes(CharsetNames.US_ASCII)[0], "b".getBytes(CharsetNames.US_ASCII)[0]};
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    @Test
    public void testIsEqualWithMismatchTrailingInFirstBuffer() {
        byte[] buffer1 = new byte[]{"a".getBytes(CharsetNames.US_ASCII)[0], 0};
        byte[] buffer2 = new byte[]{"a".getBytes(CharsetNames.US_ASCII)[0]};
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    @Test
    public void testIsEqualWithMismatchTrailingInSecondBuffer() {
        byte[] buffer1 = new byte[]{"a".getBytes(CharsetNames.US_ASCII)[0]};
        byte[] buffer2 = new byte[]{"a".getBytes(CharsetNames.US_ASCII)[0], 0};
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    @Test
    public void testIsEqualWithMismatch() {
        byte[] buffer1 = new byte[]{"a".getBytes(CharsetNames.US_ASCII)[0]};
        byte[] buffer2 = new byte[]{"b".getBytes(CharsetNames.US_ASCII)[0]};
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    @Test
    public void testIsEqualWithMismatchDifferentLengths() {
        byte[] buffer1 = new byte[]{"a".getBytes(CharsetNames.US_ASCII)[0]};
        byte[] buffer2 = new byte[]{"a".getBytes(CharsetNames.US_ASCII)[0], "b".getBytes(CharsetNames.US_ASCII)[0]};
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    @Test
    public void testIsEqualWithMismatchDifferentLengthsFirstLonger() {
        byte[] buffer1 = new byte[]{"a".getBytes(CharsetNames.US_ASCII)[0], "b".getBytes(CharsetNames.US_ASCII)[0]};
        byte[] buffer2 = new byte[]{"a".getBytes(CharsetNames.US_ASCII)[0]};
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    @Test
    public void testIsEqualWithMismatchDifferentLengthsSecondLonger() {
        byte[] buffer1 = new byte[]{"a".getBytes(CharsetNames.US_ASCII)[0]};
        byte[] buffer2 = new byte[]{"a".getBytes(CharsetNames.US_ASCII)[0], "b".getBytes(CharsetNames.US_ASCII)[0]};
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    @Test
    public void testIsArrayZero() {
        byte[] array = new byte[0];
        assertTrue(ArchiveUtils.isArrayZero(array, 0));
    }

    @Test
    public void testIsArrayZeroFalse() {
        byte[] array = new byte[1];
        assertFalse(ArchiveUtils.isArrayZero(array, 0));
    }

    @Test
    public void testSanitize() {
        String input = "test\u0001";
        String expected = "test?";
        assertEquals(expected, ArchiveUtils.sanitize(input));
    }

    @Test
    public void testSanitizeLongString() {
        String input = "test\u0001test\u0002test\u0003test\u0004test\u0005test\u0006test\u0007test\u0008test\u0009test\u000atest\u000btest\u000ctest\u000dtest\u000e";
        String expected = "test?test?test?test?test?test?test?test?test?test?test?test?test?test?test?test?test?test?test?test?test?";
        assertEquals(expected, ArchiveUtils.sanitize(input));
    }

    @Test
    public void testSanitizeEmptyString() {
        String input = "";
        String expected = "";
        assertEquals(expected, ArchiveUtils.sanitize(input));
    }

    @Test
    public void testSanitizeEmptyStringWithTrailingNull() {
        String input = "";
        String expected = "";
        assertEquals(expected, ArchiveUtils.sanitize(input));
    }

    @Test
    public void testSanitizeWithTrailingNull() {
        String input = "test\u0000";
        String expected = "test?";
        assertEquals(expected, ArchiveUtils.sanitize(input));
    }

    @Test
    public void testSanitizeWithTrailingNulls() {
        String input = "test\u0001test\u0002test\u0003test\u0004test\u0005test\u0006test\u0007test\u0008test\u0009test\u000atest\u000btest\u000ctest\u000dtest\u000e\u0000";
        String expected = "test?test?test?test?test?test?test?test?test?test?test?test?test?test?test?test?test?test?test?test?test?";
        assertEquals(expected, ArchiveUtils.sanitize(input));
    }

    @Test
    public void testSanitizeWithUnsupportedCharacter() {
        String input = "test\uD800";
        String expected = "test?";
        assertEquals(expected, ArchiveUtils.sanitize(input));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSanitizeWithNullInput() {
        String input = null;
        ArchiveUtils.sanitize(input);
    }

    private ArchiveEntry newArchiveEntry(final String name, final boolean isDirectory, final long size, final String comment) {
        return new ArchiveEntry() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public boolean isDirectory() {
                return isDirectory;
            }

            @Override
            public long getSize() {
                return size;
            }

            @Override
            public String getComment() {
                return comment;
            }
        };
    }
}