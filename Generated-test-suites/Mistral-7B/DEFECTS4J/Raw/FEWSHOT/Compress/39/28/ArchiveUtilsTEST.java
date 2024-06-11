package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("test.txt");
        assertEquals("-       0 test.txt\n", ArchiveUtils.toString(entry));

        entry.setDirectory(true);
        assertEquals("d       0 test.txt/\n", ArchiveUtils.toString(entry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        final String expected = "expected";
        final byte[] buffer = toAsciiBytes(expected);

        final boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        assertTrue(result);

        final byte[] wrongBuffer = new byte[]{1, 2, 3};
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, wrongBuffer));
    }

    @Test
    public void testMatchAsciiBuffer_null() {
        final String expected = "expected";
        final byte[] buffer = toAsciiBytes(expected);

        assertFalse(ArchiveUtils.matchAsciiBuffer(null, buffer));
        assertFalse(ArchiveUtils.matchAsciiBuffer(buffer, null));
    }

    @Test
    public void testIsEqual() {
        final byte[] buffer1 = new byte[]{'A', 'B', 'C'};
        final byte[] buffer2 = new byte[]{'A', 'B', 'C'};

        final boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);
        assertTrue(result);

        final byte[] buffer3 = new byte[]{'A', 'B', 'D'};
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, false));

        final byte[] buffer4 = new byte[]{'A', 'B', 'C'};
        final int offset1 = 1;
        final int length1 = 2;
        final int offset2 = 1;
        final int length2 = 2;
        assertTrue(ArchiveUtils.isEqual(buffer1, offset1, length1, buffer4, offset2, length2, false));

        final byte[] buffer5 = new byte[]{'A', 'B', 'C', 'D'};
        assertFalse(ArchiveUtils.isEqual(buffer1, offset1, length1, buffer5, 0, buffer5.length, false));

        final byte[] buffer6 = new byte[]{'A', 'B', 'C', 'D'};
        final boolean ignoreTrailingNulls = true;
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer6, 0, buffer6.length, ignoreTrailingNulls));
    }

    @Test
    public void testIsEqual_null() {
        final byte[] buffer1 = new byte[]{'A', 'B', 'C'};
        final byte[] buffer2 = null;

        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, 0, false));
    }

    @Test
    public void testIsEqualWithNull() {
        final byte[] buffer1 = new byte[]{'A', 'B', 'C'};
        final byte[] buffer2 = new byte[]{'A', 'B', 'C'};

        final boolean ignoreTrailingNulls = true;
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));

        final byte[] buffer3 = new byte[]{'A', 'B', 'D'};
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length));

        final byte[] buffer4 = new byte[]{'A', 'B', 'C', 'D'};
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer4, 0, buffer4.length));
    }

    @Test
    public void testIsArrayZero() {
        final byte[] a = new byte[0];
        assertTrue(ArchiveUtils.isArrayZero(a, 0));

        final byte[] a1 = new byte[1];
        assertFalse(ArchiveUtils.isArrayZero(a1, 1));

        final byte[] a2 = new byte[5];
        assertTrue(ArchiveUtils.isArrayZero(a2, 5));
    }

    @Test
    public void testSanitize() {
        final String s1 = "ABC\u0001 DEF\u007fGHI\u001F";
        final String result1 = ArchiveUtils.sanitize(s1);
        assertEquals("ABC? ?DEF??GHI??", result1);

        final String s2 = "";
        final String result2 = ArchiveUtils.sanitize(s2);
        assertEquals("", result2);
    }
}