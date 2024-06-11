import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setDirectory(true);
        entry.setSize(100);
        entry.setName("testDir");
        String expected = "d     100 testDir";
        String result = ArchiveUtils.toString(entry);
        assertEquals(expected, result);

        entry.setDirectory(false);
        entry.setSize(2000);
        entry.setName("testFile");
        expected = "-    2000 testFile";
        result = ArchiveUtils.toString(entry);
        assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "testString";
        byte[] buffer = expected.getBytes();
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        assertTrue(result);

        expected = "testString1";
        result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        assertFalse(result);

        expected = "testString";
        byte[] subBuffer = Arrays.copyOfRange(buffer, 0, 5);
        result = ArchiveUtils.matchAsciiBuffer(expected, subBuffer);
        assertFalse(result);
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "testString";
        byte[] expected = inputString.getBytes();
        byte[] result = ArchiveUtils.toAsciiBytes(inputString);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = "testString".getBytes();
        String expected = "testString";
        String result = ArchiveUtils.toAsciiString(inputBytes);
        assertEquals(expected, result);
    }

    @Test
    public void testToAsciiStringWithOffsetAndLength() {
        byte[] inputBytes = "testString".getBytes();
        String expected = "String";
        String result = ArchiveUtils.toAsciiString(inputBytes, 4, 6);
        assertEquals(expected, result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = "testString".getBytes();
        byte[] buffer2 = "testString".getBytes();
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));

        buffer2 = "testString1".getBytes();
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));

        buffer2 = "test".getBytes();
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNulls() {
        byte[] buffer1 = "testString".getBytes();
        byte[] buffer2 = "testString\0".getBytes();
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, true));

        buffer2 = "testString1\0".getBytes();
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2, true));
    }

    @Test
    public void testIsEqualWithOffsetAndLength() {
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "testString".getBytes();
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer1.length));

        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNullsWithOffsetAndLength() {
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "testString\0".getBytes();
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer1.length, true));

        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true));
    }

    @Test
    public void testIsArrayZero() {
        byte[] array = new byte[10];
        assertTrue(ArchiveUtils.isArrayZero(array, array.length));
        
        array[5] = 1;
        assertFalse(ArchiveUtils.isArrayZero(array, array.length));
    }

    @Test
    public void testSanitize() {
        String input = "testString";
        String expected = "testString";
        String result = ArchiveUtils.sanitize(input);
        assertEquals(expected, result);

        input = "test\u0000\u0001String";
        expected = "test??String";
        result = ArchiveUtils.sanitize(input);
        assertEquals(expected, result);

        input = "test\u0001String";
        expected = "test?String";
        result = ArchiveUtils.sanitize(input);
        assertEquals(expected, result);

        input = "test" + new String(new char[260]) + "String";
        expected = "test" + new String(new char[252]) + "String";
        result = ArchiveUtils.sanitize(input);
        assertEquals(expected, result);
    }
}