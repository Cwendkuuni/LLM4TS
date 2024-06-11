import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry1 = new ArchiveEntry();
        entry1.setDirectory(true);
        entry1.setSize(100);
        entry1.setName("testfiles");
        assertEquals("d     100 testfiles", ArchiveUtils.toString(entry1));
        
        ArchiveEntry entry2 = new ArchiveEntry();
        entry2.setDirectory(false);
        entry2.setSize(2000);
        entry2.setName("main.c");
        assertEquals("-    2000 main.c", ArchiveUtils.toString(entry2));
    }

    @Test
    public void testMatchAsciiBuffer() {
        byte[] buffer = {97, 98, 99};
        assertTrue(ArchiveUtils.matchAsciiBuffer("abc", buffer, 0, 3));
        assertFalse(ArchiveUtils.matchAsciiBuffer("abc", buffer, 0, 2));
        
        byte[] buffer2 = {65, 66, 67};
        assertTrue(ArchiveUtils.matchAsciiBuffer("ABC", buffer2));
    }

    @Test
    public void testToAsciiBytes() {
        byte[] expected = {97, 98, 99};
        byte[] result = ArchiveUtils.toAsciiBytes("abc");
        assertEquals(expected, result);
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = {97, 98, 99};
        String expected = "abc";
        String result = ArchiveUtils.toAsciiString(inputBytes);
        assertEquals(expected, result);
    }

    @Test
    public void testToAsciiStringWithOffsetAndLength() {
        byte[] inputBytes = {97, 98, 99};
        String expected = "b";
        String result = ArchiveUtils.toAsciiString(inputBytes, 1, 1);
        assertEquals(expected, result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = {97, 98, 99};
        byte[] buffer2 = {97, 98, 99};
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
        
        byte[] buffer3 = {97, 98, 99, 100};
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3));
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNulls() {
        byte[] buffer1 = {97, 98, 99, 0};
        byte[] buffer2 = {97, 98, 99};
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, true));
        
        byte[] buffer3 = {97, 98, 99, 100};
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3, true));
    }

    @Test
    public void testIsEqualWithOffsetsAndLengths() {
        byte[] buffer1 = {97, 98, 99};
        byte[] buffer2 = {97, 98, 99};
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, 3, buffer2, 0, 3));
        
        byte[] buffer3 = {97, 98, 99, 100};
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, 3, buffer3, 0, 4));
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = {0, 1, 2, 3};
        int size = 3;
        assertTrue(ArchiveUtils.isArrayZero(a, size));
        
        byte[] b = {0, 0, 0, 0};
        int size2 = 4;
        assertTrue(ArchiveUtils.isArrayZero(b, size2));
    }

    @Test
    public void testSanitize() {
        String s = "Hello World!";
        String expected = "Hello World!";
        String result = ArchiveUtils.sanitize(s);
        assertEquals(expected, result);
        
        String s2 = "Hello \u0001 World!";
        String expected2 = "Hello ? World!";
        String result2 = ArchiveUtils.sanitize(s2);
        assertEquals(expected2, result2);
    }

}