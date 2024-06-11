import static org.junit.Assert.*;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry1 = new ArchiveEntry();
        entry1.setName("main.c");
        entry1.setDirectory(false);
        entry1.setSize(2000);
        String result1 = ArchiveUtils.toString(entry1);
        assertEquals("-    2000 main.c", result1);

        ArchiveEntry entry2 = new ArchiveEntry();
        entry2.setName("testfiles");
        entry2.setDirectory(true);
        entry2.setSize(100);
        String result2 = ArchiveUtils.toString(entry2);
        assertEquals("d     100 testfiles", result2);
    }

    @Test
    public void testMatchAsciiBufferWithOffsetAndLength() {
        byte[] buffer = "test".getBytes();
        boolean result = ArchiveUtils.matchAsciiBuffer("test", buffer, 0, 4);
        assertTrue(result);

        boolean result2 = ArchiveUtils.matchAsciiBuffer("tes", buffer, 0, 3);
        assertFalse(result2);
    }

    @Test
    public void testMatchAsciiBuffer() {
        byte[] buffer = "test".getBytes();
        boolean result = ArchiveUtils.matchAsciiBuffer("test", buffer);
        assertTrue(result);

        boolean result2 = ArchiveUtils.matchAsciiBuffer("tes", buffer);
        assertFalse(result2);
    }

    @Test
    public void testToAsciiBytes() {
        byte[] result = ArchiveUtils.toAsciiBytes("test");
        byte[] expected = "test".getBytes();
        assertArrayEquals(expected, result);
    }

    @Test
    public void testToAsciiStringWithOffsetAndLength() {
        byte[] buffer = "test".getBytes();
        String result = ArchiveUtils.toAsciiString(buffer, 0, 4);
        assertEquals("test", result);

        String result2 = ArchiveUtils.toAsciiString(buffer, 0, 3);
        assertEquals("tes", result2);
    }

    @Test
    public void testToAsciiString() {
        byte[] buffer = "test".getBytes();
        String result = ArchiveUtils.toAsciiString(buffer);
        assertEquals("test", result);
    }

    @Test
    public void testIsEqualWithOffsetAndLengthWithIgnoreTrailingNulls() {
        byte[] buffer1 = "test1".getBytes();
        byte[] buffer2 = "test2".getBytes();
        boolean result = ArchiveUtils.isEqual(buffer1, 0, 5, buffer2, 0, 5, true);
        assertTrue(result);

        byte[] buffer3 = "test1\0\0\0".getBytes();
        byte[] buffer4 = "test2".getBytes();
        boolean result2 = ArchiveUtils.isEqual(buffer3, 0, 8, buffer4, 0, 5, true);
        assertTrue(result2);
    }

    @Test
    public void testIsEqualWithOffsetAndLength() {
        byte[] buffer1 = "test1".getBytes();
        byte[] buffer2 = "test2".getBytes();
        boolean result = ArchiveUtils.isEqual(buffer1, 0, 5, buffer2, 0, 5);
        assertFalse(result);

        byte[] buffer3 = "test1\0\0\0".getBytes();
        byte[] buffer4 = "test2".getBytes();
        boolean result2 = ArchiveUtils.isEqual(buffer3, 0, 8, buffer4, 0, 5);
        assertFalse(result2);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = "test1".getBytes();
        byte[] buffer2 = "test2".getBytes();
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
        assertFalse(result);

        byte[] buffer3 = "test".getBytes();
        byte[] buffer4 = "test".getBytes();
        boolean result2 = ArchiveUtils.isEqual(buffer3, buffer4);
        assertTrue(result2);
    }

    @Test
    public void testIsEqualWithignoreTrailingNulls() {
        byte[] buffer1 = "test1".getBytes();
        byte[] buffer2 = "test2".getBytes();
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2, true);
        assertFalse(result);

        byte[] buffer3 = "test1\0\0\0".getBytes();
        byte[] buffer4 = "test2".getBytes();
        boolean result2 = ArchiveUtils.isEqual(buffer3, buffer4, true);
        assertFalse(result2);
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = "test1".getBytes();
        byte[] buffer2 = "test2".getBytes();
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, 5, buffer2, 0, 5);
        assertFalse(result);

        byte[] buffer3 = "test1\0\0\0".getBytes();
        byte[] buffer4 = "test2".getBytes();
        boolean result2 = ArchiveUtils.isEqualWithNull(buffer3, 0, 8, buffer4, 0, 5);
        assertFalse(result2);
    }

    @Test
    public void testIsArrayZero() {
        byte[] array = new byte[5];
        boolean result = ArchiveUtils.isArrayZero(array, 5);
        assertTrue(result);

        byte[] array2 = new byte[5];
        array2[0] = 1;
        boolean result2 = ArchiveUtils.isArrayZero(array2, 5);
        assertFalse(result2);
    }

    @Test
    public void testSanitize() {
        String input = "hello";
        String result = ArchiveUtils.sanitize(input);
        assertEquals("hello", result);

        String input2 = "h\0e\0l\0l\0o";
        String result2 = ArchiveUtils.sanitize(input2);
        assertEquals("h?e?l?l?o", result2);
    }

}