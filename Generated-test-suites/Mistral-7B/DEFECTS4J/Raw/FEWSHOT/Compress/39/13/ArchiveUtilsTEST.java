package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.utils.CharsetNames;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new MockArchiveEntry("test.txt", "test/test.txt", true, 1024);
        String result = ArchiveUtils.toString(entry);
        assertEquals("- 1024 test.txt\n", result);

        entry = new MockArchiveEntry("test.dir/", "", false, 0);
        result = ArchiveUtils.toString(entry);
        assertEquals("d 0 test.dir/\n", result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = new byte[5];
        Arrays.fill(buffer, (byte) 't');

        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testMatchAsciiBufferWithMismatch() {
        String expected = "test";
        byte[] buffer = new byte[5];
        Arrays.fill(buffer, (byte) 'f');

        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testMatchAsciiBufferWithOffset() {
        String expected = "test";
        byte[] buffer = new byte[10];
        Arrays.fill(buffer, (byte) 't', 5);

        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 5, buffer.length));
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, 5));
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    @Test
    public void testMatchAsciiBufferWithNull() {
        String expected = "test";
        byte[] buffer = new byte[5];
        Arrays.fill(buffer, (byte) 0);

        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    @Test
    public void testToAsciiBytes() throws UnsupportedEncodingException {
        String input = "test";
        byte[] output = ArchiveUtils.toAsciiBytes(input);

        assertArrayEquals(input.getBytes(CharsetNames.US_ASCII), output);
    }

    @Test
    public void testToAsciiString() {
        byte[] input = new byte[5];
        Arrays.fill(input, (byte) 't');

        assertEquals("test", ArchiveUtils.toAsciiString(input));
    }

    @Test
    public void testToAsciiStringWithOffset() {
        byte[] input = new byte[10];
        Arrays.fill(input, (byte) 't', 5);

        assertEquals("test", ArchiveUtils.toAsciiString(input, 5, 5));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[] { 't', 'e', 's', 't' };
        byte[] buffer2 = new byte[] { 't', 'e', 's', 't' };

        assertTrue(ArchiveUtils.isEqual(buffer1, buffer1, buffer1.length, buffer2, buffer2.length, buffer2.length, false));
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, buffer1.length, buffer2, buffer2.length, buffer2.length, false));
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, buffer1.length, buffer2, buffer2.length, buffer2.length, true));

        byte[] buffer3 = new byte[] { 't', 'e', 's', 't', (byte) 0 };
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3, buffer1.length, buffer3, buffer3.length, buffer3.length, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3, buffer1.length, buffer3, buffer3.length, buffer3.length, true));

        byte[] buffer4 = new byte[] { 'f', 'e', 's', 't' };
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer4, buffer1.length, buffer4, buffer4.length, buffer4.length, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer4, buffer1.length, buffer4, buffer4.length, buffer4.length, true));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[] { 't', 'e', 's', 't', (byte) 0 };
        byte[] buffer2 = new byte[] { 't', 'e', 's', 't' };

        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, buffer2, buffer1.length, buffer2, buffer2.length));
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, buffer2, buffer1.length, buffer2, buffer2.length + 1));
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, buffer2, buffer1.length + 1, buffer2, buffer2.length));
    }

    @Test
    public void testIsEqualWithTrailingNulls() {
        byte[] buffer1 = new byte[] { 't', 'e', 's', 't', (byte) 0 };
        byte[] buffer2 = new byte[] { 't', 'e', 's', 't' };

        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, buffer1.length, buffer2, buffer2.length, buffer2.length, true));
    }

    @Test
    public void testIsEqualWithOffsets() {
        byte[] buffer1 = new byte[] { 't', 'e', 's', 't', (byte) 0 };
        byte[] buffer2 = new byte[] { 't', 'e', 's', 't' };

        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, 0, buffer1.length, 0, buffer2.length, false));
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, buffer1.length - 1, buffer1.length, 0, buffer2.length, false));
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, buffer1.length - 1, buffer1.length, buffer2.length - 1, buffer2.length, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2, buffer1.length - 1, buffer1.length, buffer2.length, buffer2.length, true));
    }

    @Test
    public void testIsEqualWithNulls() {
        byte[] buffer1 = new byte[] { 't', 'e', 's', 't', (byte) 0, (byte) 0 };
        byte[] buffer2 = new byte[] { 't', 'e', 's', 't', (byte) 0 };

        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, buffer1.length, buffer2, buffer2.length, buffer2.length, false));
    }

    @Test
    public void testIsArrayZero() {
        byte[] array = new byte[5];

        assertTrue(ArchiveUtils.isArrayZero(array, 5));
        assertFalse(ArchiveUtils.isArrayZero(array, 4));
    }

    @Test
    public void testSanitize() {
        String input = "test\u0000\u0001\u007Ftest\u001Ftest\u001Ftest";
        String output = ArchiveUtils.sanitize(input);

        assertEquals("test? ???test?test?test", output);
    }
}

class MockArchiveEntry extends ArchiveEntry {
    public MockArchiveEntry(String name, String comment, boolean directory, long size) {
        super(name, comment, directory, size);
    }
}