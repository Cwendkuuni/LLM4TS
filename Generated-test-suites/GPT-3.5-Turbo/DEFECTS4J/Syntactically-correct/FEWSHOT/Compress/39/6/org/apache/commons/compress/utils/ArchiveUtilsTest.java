import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setDirectory(true);
        entry.setName("testDirectory");
        entry.setSize(0);
        assertEquals("d       0 testDirectory", ArchiveUtils.toString(entry));

        entry.setDirectory(false);
        entry.setName("testFile");
        entry.setSize(100);
        assertEquals("-     100 testFile", ArchiveUtils.toString(entry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        byte[] buffer = { 65, 66, 67 };
        assertTrue(ArchiveUtils.matchAsciiBuffer("ABC", buffer));

        buffer = "Hello".getBytes();
        assertFalse(ArchiveUtils.matchAsciiBuffer("World", buffer));

        byte[] expected = "Hello".getBytes();
        assertTrue(ArchiveUtils.matchAsciiBuffer("Hello", expected));
        assertTrue(ArchiveUtils.matchAsciiBuffer("Hell", expected));

        assertFalse(ArchiveUtils.matchAsciiBuffer("Hello ", expected));

        expected = "World".getBytes();
        assertFalse(ArchiveUtils.matchAsciiBuffer("Hello", expected));

        byte[] buffer1 = { 65, 66, 67 };
        byte[] buffer2 = { 65, 66, 67 };
        assertTrue(ArchiveUtils.matchAsciiBuffer(buffer1, buffer2));

        buffer1 = "Hello".getBytes();
        buffer2 = "World".getBytes();
        assertFalse(ArchiveUtils.matchAsciiBuffer(buffer1, buffer2));
    }

    @Test
    public void testToAsciiBytes() {
        byte[] expected = { 65, 66, 67 };
        byte[] result = ArchiveUtils.toAsciiBytes("ABC");
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }

        expected = "Hello".getBytes();
        result = ArchiveUtils.toAsciiBytes("Hello");
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = { 65, 66, 67 };
        assertEquals("ABC", ArchiveUtils.toAsciiString(inputBytes));

        inputBytes = "Hello".getBytes();
        assertEquals("Hello", ArchiveUtils.toAsciiString(inputBytes));
    }

    @Test
    public void testToAsciiStringWithOffsetAndLength() {
        byte[] inputBytes = { 65, 66, 67 };
        assertEquals("ABC", ArchiveUtils.toAsciiString(inputBytes, 0, inputBytes.length));

        inputBytes = "Hello".getBytes();
        assertEquals("Hello", ArchiveUtils.toAsciiString(inputBytes, 0, inputBytes.length));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = { 65, 66, 67 };
        byte[] buffer2 = { 65, 66, 67 };
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));

        buffer1 = "Hello".getBytes();
        buffer2 = "World".getBytes();
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));

        buffer1 = "Hello".getBytes();
        buffer2 = "Hello".getBytes();
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNulls() {
        byte[] buffer1 = { 65, 0, 0, 0 };
        byte[] buffer2 = { 65, 0, 0, 0 };
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true));

        buffer1 = "Hello".getBytes();
        buffer2 = "World".getBytes();
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true));

        buffer1 = "Hello".getBytes();
        buffer2 = "Hello".getBytes();
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true));
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNullsAndDifferentLength() {
        byte[] buffer1 = { 65, 66, 67, 0, 0, 0 };
        byte[] buffer2 = { 65, 66, 67 };
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true));

        buffer1 = "Hello World".getBytes();
        buffer2 = "Hello".getBytes();
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true));
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[5];
        assertTrue(ArchiveUtils.isArrayZero(a, a.length));

        a = new byte[5];
        a[0] = 1;
        assertFalse(ArchiveUtils.isArrayZero(a, a.length));

        a = new byte[5];
        a[4] = 0;
        assertTrue(ArchiveUtils.isArrayZero(a, a.length));

    }

    @Test
    public void testSanitize() {
        String s = "Hello World";
        assertEquals("Hello World", ArchiveUtils.sanitize(s));

        s = "\u0001\u0002\u009F";
        assertEquals("???", ArchiveUtils.sanitize(s));
    }
}