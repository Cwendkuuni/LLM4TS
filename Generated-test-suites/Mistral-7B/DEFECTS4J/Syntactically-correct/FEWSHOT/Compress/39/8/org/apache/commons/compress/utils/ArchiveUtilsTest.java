package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new MockArchiveEntry("main.c");
        entry.setSize(2000);
        entry.setDirectory(false);

        String expected = "- 2000 main.c";
        String result = ArchiveUtils.toString(entry);

        assertEquals(expected, result);

        ArchiveEntry directoryEntry = new MockArchiveEntry("testfiles");
        directoryEntry.setSize(100);
        directoryEntry.setDirectory(true);

        expected = "d 100 testfiles/";
        result = ArchiveUtils.toString(directoryEntry);

        assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "MagicString";
        byte[] buffer = toAsciiBytes(expected);

        byte[] testBuffer1 = new byte[buffer.length];
        System.arraycopy(buffer, 0, testBuffer1, 0, buffer.length);

        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, testBuffer1, 0, testBuffer1.length));

        byte[] testBuffer2 = new byte[buffer.length + 1];
        System.arraycopy(buffer, 0, testBuffer2, 0, buffer.length);
        testBuffer2[buffer.length] = (byte) 0x01;

        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, testBuffer2, 0, testBuffer2.length));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 };
        byte[] buffer2 = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 };
        byte[] buffer3 = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x06, 0x05 };

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x00 };
        byte[] buffer2 = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x00, 0x00 };
        byte[] buffer3 = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 };

        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length));
    }

    @Test
    public void testIsArrayZero() {
        byte[] array = new byte[10];
        assertTrue(ArchiveUtils.isArrayZero(array, 10));
        assertFalse(ArchiveUtils.isArrayZero(array, 5));
    }

    @Test
    public void testSanitize() {
        String input = "test\u0001string with special characters: !@#$%&*()_+={}[]|\\:;\"'<>,.?/~`";
        String expected = "test????string with special characters: !?????@#?????$??????%??????&????????";
        String result = ArchiveUtils.sanitize(input);

        assertEquals(expected, result);
    }

}

class MockArchiveEntry extends ArchiveEntry {

    public MockArchiveEntry(String name) {
        super(name);
    }

    @Override
    public long getSize() {
        return 0;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }
}