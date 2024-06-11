import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry();
        
        entry.setSize(2000);
        entry.setName("main.c");
        assertFalse(ArchiveUtils.toString(entry).isEmpty());
        
        entry.setDirectory(true);
        entry.setSize(100);
        entry.setName("testfiles");
        assertFalse(ArchiveUtils.toString(entry).isEmpty());
    }

    @Test
    public void testMatchAsciiBuffer() {
        assertTrue(ArchiveUtils.matchAsciiBuffer("test", "test".getBytes(), 0, 4));
        assertFalse(ArchiveUtils.matchAsciiBuffer("test", "fail".getBytes()));
    }

    @Test
    public void testToAsciiBytes() {
        byte[] asciiBytes = ArchiveUtils.toAsciiBytes("test");
        assertArrayEquals("test".getBytes(), asciiBytes);
    }

    @Test
    public void testToAsciiString() {
        String asciiString = ArchiveUtils.toAsciiString("test".getBytes());
        assertEquals("test", asciiString);
    }

    @Test
    public void testIsEqual() {
        assertTrue(ArchiveUtils.isEqual(new byte[]{1, 2, 3}, new byte[]{1, 2, 3}));
        assertFalse(ArchiveUtils.isEqual(new byte[]{1, 2, 3}, new byte[]{4, 5, 6}));
    }

    @Test
    public void testIsArrayZero() {
        assertTrue(ArchiveUtils.isArrayZero(new byte[]{0, 0, 0}, 3));
        assertFalse(ArchiveUtils.isArrayZero(new byte[]{1, 0, 0}, 3));
    }

    @Test
    public void testSanitize() {
        String sanitizedString = ArchiveUtils.sanitize("\u0000Test\u0001");
        assertEquals("?Test?", sanitizedString);
    }

    @Test
    public void testIsEqualWithNull() {
        assertTrue(ArchiveUtils.isEqualWithNull(new byte[]{1, 2, 0}, 0, 3, new byte[]{1, 2}, 0, 2));
    }
}