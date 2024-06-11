package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new MockArchiveEntry("test.txt");
        String result = ArchiveUtils.toString(entry);
        assertEquals("-       0 test.txt", result);

        entry.setDirectory(true);
        result = ArchiveUtils.toString(entry);
        assertEquals("d       0 test.txt", result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "expected";
        byte[] buffer = toAsciiBytes(expected);

        byte[] testBuffer1 = new byte[5];
        Arrays.fill(testBuffer1, (byte) 'e');
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, testBuffer1, 0, testBuffer1.length));

        byte[] testBuffer2 = new byte[5];
        System.arraycopy(buffer, 0, testBuffer2, 0, buffer.length);
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, testBuffer2, 0, testBuffer2.length));

        testBuffer2[4] = (byte) 0x01;
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, testBuffer2, 0, testBuffer2.length));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[] { (byte) 'e', (byte) 'x', (byte) 'p', (byte) 'e', (byte) 'd' };
        byte[] buffer2 = new byte[] { (byte) 'e', (byte) 'x', (byte) 'p', (byte) 'e', (byte) 'd' };
        byte[] buffer3 = new byte[] { (byte) 'e', (byte) 'x', (byte) 'p', (byte) 'p', (byte) 'd' };

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, false));

        byte[] buffer4 = new byte[] { (byte) 'e', (byte) 'x', (byte) 'p', (byte) 'e', (byte) 'd' };
        byte[] buffer5 = new byte[] { (byte) 'e', (byte) 'x', (byte) 'p', (byte) 'd', (byte) 'd' };

        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer5, 0, buffer5.length, false));

        byte[] buffer6 = new byte[] { (byte) 'e', (byte) 'x', (byte) 'p', (byte) 'e', (byte) 'd' };
        byte[] buffer7 = new byte[] { (byte) 'e', (byte) 'x', (byte) 'p', (byte) 'e', (byte) 0, (byte) 0 };

        assertTrue(ArchiveUtils.isEqual(buffer6, 0, buffer6.length, buffer7, 0, buffer7.length, true));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[] { (byte) 'e', (byte) 'x', (byte) 'p', (byte) 'e', (byte) 'd' };
        byte[] buffer2 = new byte[] { (byte) 'e', (byte) 'x', (byte) 'p', (byte) 'e', (byte) 'd', (byte) 0, (byte) 0 };

        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));

        byte[] buffer3 = new byte[] { (byte) 'e', (byte) 'x', (byte) 'p', (byte) 'e', (byte) 'd' };
        byte[] buffer4 = new byte[] { (byte) 'e', (byte) 'x', (byte) 'p', (byte) 'e', (byte) 'd', (byte) 0 };

        assertFalse(ArchiveUtils.isEqualWithNull(buffer3, 0, buffer3.length, buffer4, 0, buffer4.length));
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[5];
        assertFalse(ArchiveUtils.isArrayZero(a, 5));

        byte[] b = new byte[5];
        Arrays.fill(b, (byte) 0);
        assertTrue(ArchiveUtils.isArrayZero(b, 5));
    }

    @Test
    public void testSanitize() {
        String input = "test\u0001string\u007F";
        String expected = "test??string?";
        String result = ArchiveUtils.sanitize(input);
        assertEquals(expected, result);
    }
}