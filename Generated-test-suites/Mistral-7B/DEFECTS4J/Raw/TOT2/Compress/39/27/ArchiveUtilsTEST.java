import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;
import org.junit.Assert.*;

public class ArchiveUtilsTest {

    

    @Test
    public void testToStringMethod() {
        ArchiveEntry entry = new ArchiveEntry("main.c");
        String result = ArchiveUtils.toString(entry);
        String expected = "- 2000 main.c";
        assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBufferMethod() {
        String expected = "expectedString";
        byte[] buffer = "expectedString".getBytes();
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testMatchAsciiBufferMethodWithUnsupportedEncodingException() {
        String expected = "expectedString";
        byte[] buffer = new byte[10];
        ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length);
    }

    @Test
    public void testToAsciiBytesMethod() {
        String inputString = "inputString";
        byte[] result = ArchiveUtils.toAsciiBytes(inputString);
        byte[] expected = inputString.getBytes();
        assertArrayEquals(expected, result);
    }

    @Test
    public void testToAsciiStringMethod() {
        byte[] inputBytes = "inputBytes".getBytes();
        String result = ArchiveUtils.toAsciiString(inputBytes);
        String expected = "inputBytes";
        assertEquals(expected, result);
    }

    @Test
    public void testIsEqualMethod() {
        byte[] buffer1 = new byte[]{'a', 'b', 'c'};
        byte[] buffer2 = new byte[]{'a', 'b', 'c'};
        byte[] buffer3 = new byte[]{'a', 'b', 'd'};
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length));
    }

    @Test
    public void testIsEqualMethodWithNullTrailing() {
        byte[] buffer1 = new byte[]{'a', 'b', 'c', 0};
        byte[] buffer2 = new byte[]{'a', 'b', 'c', 'd', 0};
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    @Test
    public void testIsEqualMethodWithNullTrailingInBuffer2() {
        byte[] buffer1 = new byte[]{'a', 'b', 'c'};
        byte[] buffer2 = new byte[]{'a', 'b', 'c', 0};
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    @Test
    public void testIsArrayZeroMethod() {
        byte[] array = new byte[10];
        assertTrue(ArchiveUtils.isArrayZero(array, 5));
        assertFalse(ArchiveUtils.isArrayZero(array, 6));
    }

    @Test
    public void testSanitizeMethod() {
        String input = "inputString with !@#$%^&*()_+={};':\"|,.<>?";
        String expected = "inputString..........";
        String result = ArchiveUtils.sanitize(input);
        assertEquals(expected, result);
    }

    
}