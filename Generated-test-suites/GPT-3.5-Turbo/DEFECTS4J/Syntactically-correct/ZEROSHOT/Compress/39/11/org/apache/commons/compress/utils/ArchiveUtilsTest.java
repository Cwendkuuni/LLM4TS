import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setName("main.c");
        entry.setDirectory(false);
        entry.setSize(2000);
        
        String expected = "-    2000 main.c";
        assertEquals(expected, ArchiveUtils.toString(entry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "hello";
        byte[] buffer = expected.getBytes();
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "hello";
        byte[] expectedBytes = inputString.getBytes();
        assertArrayEquals(expectedBytes, ArchiveUtils.toAsciiBytes(inputString));
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = {104, 101, 108, 108, 111};
        String expectedString = "hello";
        assertEquals(expectedString, ArchiveUtils.toAsciiString(inputBytes));
    }

    @Test
    public void testToAsciiStringWithOffsetAndLength() {
        byte[] inputBytes = {104, 101, 108, 108, 111};
        int offset = 1;
        int length = 3;
        String expectedString = "ell";
        assertEquals(expectedString, ArchiveUtils.toAsciiString(inputBytes, offset, length));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = {104, 101, 108, 108, 111};
        byte[] buffer2 = {104, 101, 108, 108, 111};
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNulls() {
        byte[] buffer1 = {104, 101, 108, 108, 111, 0, 0, 0};
        byte[] buffer2 = {104, 101, 108, 108, 111};
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, true));
    }

    @Test
    public void testIsEqualWithOffsetAndLength() {
        byte[] buffer1 = {104, 101, 108, 108, 111};
        byte[] buffer2 = {101, 108, 108};
        int offset1 = 1;
        int offset2 = 0;
        int length = 3;
        assertTrue(ArchiveUtils.isEqual(buffer1, offset1, length, buffer2, offset2, length));
    }

    @Test
    public void testIsEqualWithOffsetAndLengthAndIgnoreTrailingNulls() {
        byte[] buffer1 = {104, 101, 108, 108, 111, 0, 0, 0};
        byte[] buffer2 = {101, 108, 108};
        int offset1 = 1;
        int offset2 = 0;
        int length = 3;
        assertTrue(ArchiveUtils.isEqual(buffer1, offset1, length, buffer2, offset2, length, true));
    }

    @Test
    public void testIsEqualWithArrayZero() {
        byte[] buffer = {0, 0, 0, 0, 0};
        int size = 5;
        assertTrue(ArchiveUtils.isArrayZero(buffer, size));
    }

    @Test
    public void testSanitize() {
        String inputString = "h\u0000el\bb\to\nwo\rld";
        String expectedString = "h?el?b?o?wo?ld";
        assertEquals(expectedString, ArchiveUtils.sanitize(inputString));
    }

}