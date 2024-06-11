import static org.junit.Assert.*;
import org.junit.Test;

public class ZipArchiveEntryTest {

    @Test
    public void testZipArchiveEntryConstructorWithName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("testFile.txt");
        assertEquals("testFile.txt", entry.getName());
    }

    @Test
    public void testZipArchiveEntryConstructorWithZipEntry() throws ZipException {
        java.util.zip.ZipEntry zipEntry = new java.util.zip.ZipEntry("testFile.zip");
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        assertEquals("testFile.zip", entry.getName());
    }

    @Test
    public void testZipArchiveEntryConstructorWithZipArchiveEntry() throws ZipException {
        ZipArchiveEntry original = new ZipArchiveEntry("originalFile.txt");
        original.setMethod(8); // Dummy compression method for testing
        ZipArchiveEntry entry = new ZipArchiveEntry(original);
        assertEquals("originalFile.txt", entry.getName());
        assertEquals(8, entry.getMethod());
    }

    // Add more test cases for other methods

}