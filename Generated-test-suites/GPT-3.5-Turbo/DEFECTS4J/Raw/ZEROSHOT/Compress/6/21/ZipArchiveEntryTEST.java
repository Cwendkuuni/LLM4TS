import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import static org.junit.Assert.*;

public class ZipArchiveEntryTest {

    private ZipArchiveEntry zipArchiveEntry;

    @Before
    public void setUp() {
        zipArchiveEntry = new ZipArchiveEntry("test.zip");
    }

    @Test
    public void testConstructorWithName() {
        assertEquals("test.zip", zipArchiveEntry.getName());
    }

    @Test
    public void testConstructorWithZipEntry() throws ZipException {
        ZipEntry zipEntry = new ZipEntry("test.zip");
        zipArchiveEntry = new ZipArchiveEntry(zipEntry);
        assertEquals(zipEntry.getName(), zipArchiveEntry.getName());
    }

    @Test
    public void testConstructorWithZipArchiveEntry() throws ZipException {
        ZipArchiveEntry original = new ZipArchiveEntry("test.zip");
        ZipArchiveEntry clone = new ZipArchiveEntry(original);
        assertEquals(original.getName(), clone.getName());
    }

    @Test
    public void testSetName() {
        zipArchiveEntry.setName("new.zip");
        assertEquals("new.zip", zipArchiveEntry.getName());
    }

    @Test
    public void testIsDirectory() {
        assertFalse(zipArchiveEntry.isDirectory());
        zipArchiveEntry.setName("directory/");
        assertTrue(zipArchiveEntry.isDirectory());
    }

    @Test
    public void testHashCode() {
        ZipArchiveEntry original = new ZipArchiveEntry("test.zip");
        ZipArchiveEntry clone = new ZipArchiveEntry("test.zip");
        assertEquals(original.hashCode(), clone.hashCode());
    }

    @Test
    public void testEquals() {
        ZipArchiveEntry original = new ZipArchiveEntry("test.zip");
        ZipArchiveEntry clone = new ZipArchiveEntry("test.zip");
        assertTrue(original.equals(clone));
    }
}