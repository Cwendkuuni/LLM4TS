package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("test.txt");
        entry.setSize(123);
        entry.setDirectory(false);

        String result = ArchiveUtils.toString(entry);
        assertEquals("- 123 test.txt", result);

        ArchiveEntry directoryEntry = new ArchiveEntry("test_dir/");
        directoryEntry.setSize(0);
        directoryEntry.setDirectory(true);

        result = ArchiveUtils.toString(directoryEntry);
        assertEquals("d 0 test_dir/", result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = toAsciiBytes(expected);

        byte[] buffer1 = Arrays.copyOfRange(buffer, 1, buffer.length);

        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer1));
        assertFalse(ArchiveUtils.matchAsciiBuffer("test2", buffer));
        assertFalse(ArchiveUtils.matchAsciiBuffer("test", new byte[]{1}));
    }

    @Test
    public void testToAsciiString() {
        byte[] buffer = toAsciiBytes("test");
        assertEquals("test", ArchiveUtils.toAsciiString(buffer));
        assertEquals("test", ArchiveUtils.toAsciiString(buffer, 0, buffer.length));
        assertEquals("test", ArchiveUtils.toAsciiString(buffer, 1, buffer.length - 1));
        try {
            ArchiveUtils.toAsciiString(null);
            fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = toAsciiBytes("test1");
        byte[] buffer2 = toAsciiBytes("test1");
        byte[] buffer3 = toAsciiBytes("test2");

        byte[] buffer1_offset1 = Arrays.copyOfRange(buffer1, 1, buffer1.length);
        byte[] buffer2_offset2 = new byte[]{0, 't', 'e', 's', 't', '2'};

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2_offset2, 0, buffer2_offset2.length));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length));
        assertFalse(ArchiveUtils.isEqual(buffer1, 1, buffer1.length - 1, buffer2, 0, buffer2.length));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 1, buffer2.length - 1));
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3, true));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = toAsciiBytes("test1");
        byte[] buffer2 = toAsciiBytes("test1");
        byte[] buffer3 = toAsciiBytes("test2");
        byte[] buffer4 = null;

        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 1, buffer1.length - 1, buffer2, 1, buffer2.length - 1));
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, buffer2, true));
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, buffer3, true));
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, buffer4, true));
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[10];
        assertTrue(ArchiveUtils.isArrayZero(a, 0));
        assertTrue(ArchiveUtils.isArrayZero(a, 5));
        assertFalse(ArchiveUtils.isArrayZero(a, 1));
    }

    @Test
    public void testSanitize() {
        String s1 = "test\u0001\u007Ftest\u001F";
        String s2 = ArchiveUtils.sanitize(s1);
        assertEquals("test??test??", s2);
    }
}