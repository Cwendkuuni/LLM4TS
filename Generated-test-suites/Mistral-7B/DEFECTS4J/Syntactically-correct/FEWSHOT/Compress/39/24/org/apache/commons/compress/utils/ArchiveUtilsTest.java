package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = null;
        assertEquals("-", ArchiveUtils.toString(entry));

        entry = new MockArchiveEntry("test.txt");
        assertEquals("- 0 test.txt", ArchiveUtils.toString(entry));

        entry.setSize(1024);
        assertEquals("- 1024 test.txt", ArchiveUtils.toString(entry));

        entry.setSize(1024);
        entry.setName("dir/test.txt");
        assertEquals("d 1024 dir/test.txt", ArchiveUtils.toString(entry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = new byte[3];
        Arrays.fill(buffer, (byte) 't');

        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));

        expected += "es";
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    @Test
    public void testToAsciiBytes() {
        String expected = "test";
        byte[] bytes = ArchiveUtils.toAsciiBytes(expected);

        assertEquals(expected.getBytes(Charset.forName("US-ASCII")), bytes);
    }

    @Test
    public void testToAsciiString() {
        byte[] buffer = new byte[3];
        Arrays.fill(buffer, (byte) 't');

        assertEquals("test", ArchiveUtils.toAsciiString(buffer));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[] {'t', 'e', 's', 't'};
        byte[] buffer2 = new byte[] {'t', 'e', 's', 't'};

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));

        byte[] buffer3 = new byte[] {'t', 'e', 's', 't', 'a'};
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, false));

        byte[] buffer4 = new byte[] {'t', 'e', 's', 't'};
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer4, false));

        byte[] buffer5 = new byte[] {'t', 'e', 's', 't'};
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer5, true));

        byte[] buffer6 = new byte[] {'t', 'e', 's', 't'};
        byte[] buffer7 = new byte[] {'t', 'e', 's', 't'};
        assertTrue(ArchiveUtils.isEqual(buffer6, buffer7, false));
        assertTrue(ArchiveUtils.isEqual(buffer6, buffer7, true));

        byte[] buffer8 = new byte[] {'t', 'e', 's', 't'};
        byte[] buffer9 = new byte[] {'t', 'e', 's', 't', 0};
        assertFalse(ArchiveUtils.isEqual(buffer8, buffer9, false));
        assertTrue(ArchiveUtils.isEqual(buffer8, buffer9, true));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[] {'t', 'e', 's', 't'};
        byte[] buffer2 = new byte[] {'t', 'e', 's', 't'};
        byte[] buffer3 = null;

        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, buffer3, 0, buffer3.length, 0, buffer3.length));
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, buffer2, 0, buffer1.length, 0, buffer2.length));
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, buffer2, 0, buffer1.length, 0, buffer2.length, true));
    }

    @Test
    public void testIsArrayZero() {
        byte[] buffer = new byte[5];

        assertFalse(ArchiveUtils.isArrayZero(buffer, 0));
        assertFalse(ArchiveUtils.isArrayZero(buffer, 1));
        assertFalse(ArchiveUtils.isArrayZero(buffer, 2));
        assertFalse(ArchiveUtils.isArrayZero(buffer, 3));
        assertFalse(ArchiveUtils.isArrayZero(buffer, 4));
        assertTrue(ArchiveUtils.isArrayZero(buffer, 5));
        assertTrue(ArchiveUtils.isArrayZero(buffer, 0, 5));
    }

    @Test
    public void testSanitize() {
        String input = "\u0000\u0001test\u007F\u00FF\u2000\u2014test\u2020\u2026test";
        String expected = "test????test?test";

        assertEquals(expected, ArchiveUtils.sanitize(input));
    }
}

class MockArchiveEntry extends ArchiveEntry {

    public MockArchiveEntry(String name) {
        super(name);
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public long getSize() {
        return 0;
    }

}