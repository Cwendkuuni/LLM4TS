import static org.junit.Assert.*;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        // typical use case
        ArchiveEntry entry = new ArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(100);
        entry.setDirectory(false);
        assertEquals("-    100 test.txt", ArchiveUtils.toString(entry));
        
        // edge case
        entry.setName("");
        entry.setSize(0);
        entry.setDirectory(true);
        assertEquals("d         0 ", ArchiveUtils.toString(entry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        // typical use case
        assertTrue(ArchiveUtils.matchAsciiBuffer("test", "test".getBytes(), 0, 4));
        
        // edge case
        assertFalse(ArchiveUtils.matchAsciiBuffer("test", "testing".getBytes(), 0, 7));
    }

    @Test
    public void testToAsciiBytes() {
        // typical use case
        assertArrayEquals("test".getBytes(), ArchiveUtils.toAsciiBytes("test"));
        
        // edge case
        assertArrayEquals("".getBytes(), ArchiveUtils.toAsciiBytes(""));
    }

    @Test
    public void testToAsciiString() {
        // typical use case
        byte[] bytes = "test".getBytes();
        assertEquals("test", ArchiveUtils.toAsciiString(bytes));
        
        // edge case
        assertEquals("", ArchiveUtils.toAsciiString(new byte[] {}));
    }

    @Test
    public void testIsEqual() {
        // typical use case
        assertTrue(ArchiveUtils.isEqual("test".getBytes(), "test".getBytes()));
        
        // edge case
        assertFalse(ArchiveUtils.isEqual("test".getBytes(), "testing".getBytes()));
    }

    @Test
    public void testIsEqualWithNull() {
        // typical use case
        assertTrue(ArchiveUtils.isEqualWithNull("test".getBytes(), 0, 4, "test".getBytes(), 0, 4));
        
        // edge case
        assertFalse(ArchiveUtils.isEqualWithNull("test".getBytes(), 0, 4, "testing".getBytes(), 0, 7));
    }

    @Test
    public void testIsArrayZero() {
        // typical use case
        assertTrue(ArchiveUtils.isArrayZero(new byte[5], 5));
        
        // edge case
        assertFalse(ArchiveUtils.isArrayZero("test".getBytes(), 4));
    }

    @Test
    public void testSanitize() {
        // typical use case
        assertEquals("test", ArchiveUtils.sanitize("test"));
        
        // edge case
        assertEquals("?", ArchiveUtils.sanitize("\u0000"));
    }
}