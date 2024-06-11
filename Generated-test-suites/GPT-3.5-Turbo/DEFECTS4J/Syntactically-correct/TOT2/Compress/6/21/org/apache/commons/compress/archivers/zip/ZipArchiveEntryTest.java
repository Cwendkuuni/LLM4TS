import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.junit.Test;
import java.io.File;
import java.util.zip.ZipException;
import static org.junit.Assert.*;

public class ZipArchiveEntryTest {

    @Test
    public void testZipArchiveEntryConstructorWithName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("testFile.txt");
        assertEquals("testFile.txt", entry.getName());
    }

    @Test
    public void testZipArchiveEntryConstructorWithZipEntry() throws ZipException {
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry("testFile.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        assertEquals("testFile.txt", entry.getName());
    }

    @Test
    public void testZipArchiveEntryConstructorWithExistingEntry() throws ZipException {
        ZipArchiveEntry existingEntry = new ZipArchiveEntry("existingFile.txt");
        ZipArchiveEntry newEntry = new ZipArchiveEntry(existingEntry);
        assertEquals(existingEntry.getName(), newEntry.getName());
    }

    @Test
    public void testClone() {
        ZipArchiveEntry entry = new ZipArchiveEntry("testFile.txt");
        ZipArchiveEntry clonedEntry = (ZipArchiveEntry) entry.clone();
        assertEquals(entry.getName(), clonedEntry.getName());
    }
    
    @Test
    public void testIsSupportedCompressionMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("testFile.txt");
        assertTrue(entry.isSupportedCompressionMethod());
    }

    // Add more test cases as needed

}