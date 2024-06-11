import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;
import org.junit.Assert.*;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("main.c");
        String output = ArchiveUtils.toString(entry);
        String expected = "- 2000 main.c";
        assertEquals(expected, output);
    }

    @Test
    public void testToStringDirectory() {
        ArchiveEntry entry = new ArchiveEntry("testfiles");
        entry.setDirectory(true);
        String output = ArchiveUtils.toString(entry);
        String expected = "d     100 testfiles";
        assertEquals(expected, output);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = toAsciiBytes(expected);
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        assertTrue(result);
    }

    @Test
    public void testMatchAsciiBufferMismatch() {
        String expected = "test";
        byte[] buffer = new byte[]{1, 2, 3};
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        assertFalse(result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{'t', 'e', 's', 't'};
        byte[] buffer2 = new byte[]{'t', 'e', 's', 't'};
        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);
        assertTrue(result);
    }

    @Test
    public void testIsEqualDifferentLength() {
        byte[] buffer1 = new byte[]{'t', 'e', 's', 't'};
        byte[] buffer2 = new byte[]{'t', 'e', 's', 't', 't'};
        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);
        assertFalse(result);
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[]{'t', 'e', 's', 't', 0};
        byte[] buffer2 = new byte[]{'t', 'e', 's', 't'};
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length - 1, buffer2, 0, buffer2.length, true);
        assertTrue(result);
    }

    @Test
    public void testIsEqualWithNullFalse() {
        byte[] buffer1 = new byte[]{'t', 'e', 's', 't', 0};
        byte[] buffer2 = new byte[]{'t', 'e', 's', 't'};
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);
        assertFalse(result);
    }

    @Test
    public void testIsEqualWithNullTrue() {
        byte[] buffer1 = new byte[]{'t', 'e', 's', 't', 0};
        byte[] buffer2 = new byte[]{'t', 'e', 's', 't', 0};
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true);
        assertTrue(result);
    }

    @Test
    public void testIsEqualWithDifferentLengths() {
        byte[] buffer1 = new byte[]{'t', 'e', 's', 't'};
        byte[] buffer2 = new byte[]{'t', 'e', 's', 't', 't'};
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true);
        assertFalse(result);
    }

    @Test
    public void testIsArrayZero() {
        byte[] buffer = new byte[10];
        boolean result = ArchiveUtils.isArrayZero(buffer, 0);
        assertFalse(result);

        byte[] zeroBuffer = new byte[5];
        Arrays.fill(zeroBuffer, (byte) 0);
        result = ArchiveUtils.isArrayZero(zeroBuffer, 0);
        assertTrue(result);
    }

}