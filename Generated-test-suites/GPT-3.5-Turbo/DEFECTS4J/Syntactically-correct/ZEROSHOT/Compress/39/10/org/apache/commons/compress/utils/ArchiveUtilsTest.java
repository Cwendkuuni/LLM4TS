import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(100);
        assertEquals("-    100 test.txt", ArchiveUtils.toString(entry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        byte[] buffer = "test".getBytes();
        assertEquals(true, ArchiveUtils.matchAsciiBuffer("test", buffer));
    }

    @Test
    public void testToAsciiBytes() {
        byte[] expected = "test".getBytes();
        assertEquals(expected, ArchiveUtils.toAsciiBytes("test"));
    }

    @Test
    public void testToAsciiString() {
        byte[] buffer = "test".getBytes();
        assertEquals("test", ArchiveUtils.toAsciiString(buffer));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "test".getBytes();
        assertEquals(true, ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsArrayZero() {
        byte[] buffer = new byte[10];
        assertEquals(true, ArchiveUtils.isArrayZero(buffer, 10));
    }

    @Test
    public void testSanitize() {
        String s = "abc123";
        assertEquals(s, ArchiveUtils.sanitize(s));
    }

}