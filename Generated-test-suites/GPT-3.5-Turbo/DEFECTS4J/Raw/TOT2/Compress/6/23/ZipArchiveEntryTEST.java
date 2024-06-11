import static org.junit.Assert.*;
import org.junit.Test;

public class ZipArchiveEntryTest {

    @Test
    public void testConstructorWithName() {
        ZipArchiveEntry entry = new ZipArchiveEntry("testFile.txt");
        assertEquals("testFile.txt", entry.getName());
    }

    @Test
    public void testConstructorWithEntry() {
        java.util.zip.ZipEntry baseEntry = new java.util.zip.ZipEntry("testFile.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(baseEntry);
        assertEquals("testFile.txt", entry.getName());
    }

    @Test
    public void testGetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("testFile.txt");
        entry.setUnixMode(755);
        assertEquals(755, entry.getUnixMode());
    }

    @Test
    public void testAddExtraField() {
        ZipExtraField ze = new ZipExtraField();
        ze.setHeaderId(new ZipShort(1));
        ZipArchiveEntry entry = new ZipArchiveEntry("testFile.txt");
        entry.addExtraField(ze);
        assertEquals(1, entry.getExtraFields().length);
    }

    @Test
    public void testRemoveExtraField() {
        ZipExtraField ze = new ZipExtraField();
        ze.setHeaderId(new ZipShort(1));
        ZipArchiveEntry entry = new ZipArchiveEntry("testFile.txt");
        entry.addExtraField(ze);
        entry.removeExtraField(new ZipShort(1));
        assertEquals(0, entry.getExtraFields().length);
    }

    @Test
    public void testSetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("testFile.txt");
        entry.setMethod(2);
        assertEquals(2, entry.getMethod());
    }

}