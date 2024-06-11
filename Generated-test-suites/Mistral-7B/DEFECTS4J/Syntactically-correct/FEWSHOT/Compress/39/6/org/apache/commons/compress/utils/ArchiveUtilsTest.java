package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("main.c");
        entry.setSize(2000);
        entry.setDirectory(false);

        String str = ArchiveUtils.toString(entry);
        assertEquals("- 2000 main.c", str);

        entry.setDirectory(true);
        str = ArchiveUtils.toString(entry);
        assertEquals("d 2000 main.c", str);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String str = "foo";
        byte[] buffer = toAsciiBytes(str);
        assertTrue(ArchiveUtils.matchAsciiBuffer(str, buffer));
        assertTrue(ArchiveUtils.matchAsciiBuffer(str, buffer, 0, buffer.length));
        assertTrue(ArchiveUtils.matchAsciiBuffer(str, buffer, 0, buffer.length, false));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{'f', 'o', 'o'};
        byte[] buffer2 = new byte[]{'f', 'o', 'o'};
        byte[] buffer3 = new byte[]{'b', 'a', 'r'};
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer1, 0, buffer1.length, buffer3, 0, buffer3.length));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, true));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[]{'f', 'o', 'o'};
        byte[] buffer2 = new byte[]{'f', 'o', 'o'};
        byte[] buffer3 = null;
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer3, 0, 1));
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length));
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, true));
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true));
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[5];
        assertFalse(ArchiveUtils.isArrayZero(a, 0));
        assertTrue(ArchiveUtils.isArrayZero(a, 5));
        assertTrue(ArchiveUtils.isArrayZero(new byte[0][], 0));
    }

    @Test
    public void testSanitize() {
        String s1 = "foo\u0000bar";
        String s2 = ArchiveUtils.sanitize(s1);
        assertEquals("foo????bar", s2);

        String s3 = "foo\u0123bar";
        String s4 = ArchiveUtils.sanitize(s3);
        assertEquals("foo?????bar", s4);

        String s5 = "foo";
        String s6 = ArchiveUtils.sanitize(s5);
        assertEquals("foo", s6);
    }
}