package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new MockArchiveEntry("main.c");
        assertEquals("- 2000 main.c\n", ArchiveUtils.toString(entry));

        ArchiveEntry directoryEntry = new MockArchiveEntry("testfiles/");
        assertEquals("d 100 testfiles/\n", ArchiveUtils.toString(directoryEntry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "archive";
        byte[] buffer = toAsciiBytes(expected);

        byte[] buffer1 = new byte[5];
        Arrays.fill(buffer1, (byte) 'a');
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer1, 0, buffer1.length));

        byte[] buffer2 = new byte[5];
        System.arraycopy(buffer, 0, buffer2, 0, buffer.length);
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer2, 0, buffer.length));

        byte[] buffer3 = new byte[6];
        System.arraycopy(buffer, 0, buffer3, 0, buffer.length);
        buffer3[buffer3.length - 1] = (byte) 'x';
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer3, 0, buffer3.length));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = toAsciiBytes("archive.tar.gz");
        byte[] buffer2 = toAsciiBytes("archive.tar.gz");
        byte[] buffer3 = toAsciiBytes("archive.tar.gz\n");
        byte[] buffer4 = toAsciiBytes("archive.tar.gz\r\n");

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer4, 0, buffer4.length, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, true));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer4, 0, buffer4.length, true));

        byte[] buffer5 = new byte[buffer1.length + 1];
        System.arraycopy(buffer1, 0, buffer5, 0, buffer1.length);
        buffer5[buffer5.length - 1] = (byte) 'x';
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer5, 0, buffer5.length, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer5, 0, buffer5.length, true));
    }

    @Test
    public void testSanitize() {
        String input = "C:\\Users\\user\\Desktop\\file with spaces and special characters.txt";
        String expected = "C:\\Users\\user\\Desktop\\file with spaces and ???????????????.txt";
        assertEquals(expected, ArchiveUtils.sanitize(input));
    }
}

class MockArchiveEntry extends ArchiveEntry {

    public MockArchiveEntry(String name) {
        super(name);
    }

    @Override
    public long getSize() {
        return 2000;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }
}