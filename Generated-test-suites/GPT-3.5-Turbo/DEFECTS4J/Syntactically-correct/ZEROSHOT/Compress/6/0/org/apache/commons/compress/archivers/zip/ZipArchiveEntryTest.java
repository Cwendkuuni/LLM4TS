import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ZipArchiveEntryTest {

    private ZipArchiveEntry zipArchiveEntry;

    @Before
    public void setUp() {
        zipArchiveEntry = new ZipArchiveEntry("test.zip");
    }

    @Test
    public void testGetName() {
        assertEquals("test.zip", zipArchiveEntry.getName());
    }

    @Test
    public void testIsDirectory() {
        assertFalse(zipArchiveEntry.isDirectory());
    }

    @Test
    public void testHashCode() {
        int expectedHashCode = "test.zip".hashCode();
        assertEquals(expectedHashCode, zipArchiveEntry.hashCode());
    }

    @Test
    public void testEquals() {
        ZipArchiveEntry other = new ZipArchiveEntry("test.zip");
        assertTrue(zipArchiveEntry.equals(other));
    }
}