import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveUtilsTest {

    @Test
    public void testToStringForDirectory() {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setDirectory(true);
        entry.setSize(100);
        entry.setName("testfiles");

        String expected = "d     100 testfiles";
        String result = ArchiveUtils.toString(entry);

        assertEquals(expected, result);
    }

    @Test
    public void testToStringForFile() {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setDirectory(false);
        entry.setSize(2000);
        entry.setName("main.c");

        String expected = "-    2000 main.c";
        String result = ArchiveUtils.toString(entry);

        assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBufferWithExpectedString() {
        String expected = "Hello";
        byte[] buffer = "Hello".getBytes();

        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testMatchAsciiBufferWithDifferentExpectedString() {
        String expected = "Hello";
        byte[] buffer = "World".getBytes();

        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testMatchAsciiBufferWithOffsetAndLength() {
        String expected = "World";
        byte[] buffer = "Hello World!".getBytes();

        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 6, 5));
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "Hello";
        byte[] expected = "Hello".getBytes();
        byte[] result = ArchiveUtils.toAsciiBytes(inputString);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testToAsciiStringWithBytes() {
        byte[] inputBytes = "Hello".getBytes();
        String expected = "Hello";
        String result = ArchiveUtils.toAsciiString(inputBytes);

        assertEquals(expected, result);
    }

    @Test
    public void testToAsciiStringWithOffsetAndLength() {
        byte[] inputBytes = "Hello World!".getBytes();
        String expected = "World";
        String result = ArchiveUtils.toAsciiString(inputBytes, 6, 5);

        assertEquals(expected, result);
    }

    @Test
    public void testIsEqualWithSameBuffers() {
        byte[] buffer1 = "Hello".getBytes();
        byte[] buffer2 = "Hello".getBytes();

        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsEqualWithDifferentBuffers() {
        byte[] buffer1 = "Hello".getBytes();
        byte[] buffer2 = "World".getBytes();

        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsEqualWithOffsetAndLength() {
        byte[] buffer1 = "Hello World!".getBytes();
        byte[] buffer2 = "World".getBytes();

        assertTrue(ArchiveUtils.isEqual(buffer1, 6, 5, buffer2, 0, 5));
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNulls() {
        byte[] buffer1 = "Hello\0\0\0\0".getBytes();
        byte[] buffer2 = "Hello".getBytes();

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, 9, buffer2, 0, 5, true));
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNullsAndDifferentBuffers() {
        byte[] buffer1 = "Hello\0\0\0\0".getBytes();
        byte[] buffer2 = "World".getBytes();

        assertFalse(ArchiveUtils.isEqual(buffer1, 0, 9, buffer2, 0, 5, true));
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNullsAndDifferentLengths() {
        byte[] buffer1 = "Hello".getBytes();
        byte[] buffer2 = "Hello\0\0\0\0".getBytes();

        assertFalse(ArchiveUtils.isEqual(buffer1, 0, 5, buffer2, 0, 9, true));
    }

    @Test
    public void testIsArrayZeroWithZeroBytes() {
        byte[] a = new byte[10];

        assertTrue(ArchiveUtils.isArrayZero(a, 10));
    }

    @Test
    public void testIsArrayZeroWithNonZeroBytes() {
        byte[] a = new byte[10];
        a[5] = 1;

        assertFalse(ArchiveUtils.isArrayZero(a, 10));
    }

    @Test
    public void testSanitizeWithPrintableCharacters() {
        String inputString = "Hello";
        String expected = "Hello";
        String result = ArchiveUtils.sanitize(inputString);

        assertEquals(expected, result);
    }

    @Test
    public void testSanitizeWithNonPrintableCharacters() {
        String inputString = "\nHello\tWorld";
        String expected = "?Hello?World";
        String result = ArchiveUtils.sanitize(inputString);

        assertEquals(expected, result);
    }

    @Test
    public void testSanitizeWithLongString() {
        String inputString = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed a semper neque, ac ullamcorper arcu. Nunc fermentum pellentesque ipsum, sed";
        String expected = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed a semper neque, ac ullamcorper arcu. Nunc fermentum pellentesque ipsu?";
        String result = ArchiveUtils.sanitize(inputString);

        assertEquals(expected, result);
    }
}