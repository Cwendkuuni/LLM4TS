import static org.junit.Assert.*;
import org.junit.Test;

public class ZipArchiveEntryTest {

    @Test
    public void testZipArchiveEntryConstructorWithName() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.zip");
        assertEquals("test.zip", zipEntry.getName());
    }

    @Test
    public void testZipArchiveEntryConstructorWithZipEntry() {
        java.util.zip.ZipEntry baseEntry = new java.util.zip.ZipEntry("test.zip");
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(baseEntry);
        assertEquals("test.zip", zipEntry.getName());
    }

    @Test
    public void testZipArchiveEntryConstructorWithZipArchiveEntry() {
        ZipArchiveEntry originalEntry = new ZipArchiveEntry("original.zip");
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(originalEntry);
        assertEquals("original.zip", zipEntry.getName());
    }

    // More test cases for other methods can be added here

}