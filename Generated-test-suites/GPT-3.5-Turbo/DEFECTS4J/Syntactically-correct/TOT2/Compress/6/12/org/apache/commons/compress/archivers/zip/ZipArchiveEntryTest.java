import static org.junit.Assert.*;
import org.junit.Test;

public class ZipArchiveEntryTest {

    @Test
    public void testZipArchiveEntry_DefaultConstructor() {
        ZipArchiveEntry entry = new ZipArchiveEntry("testFile.zip");
        
        assertEquals("testFile.zip", entry.getName());
        assertFalse(entry.isDirectory());
        assertEquals(-1, entry.getMethod());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(0, entry.getExternalAttributes());
        assertEquals(0, entry.getUnixMode());
    }

    @Test
    public void testZipArchiveEntry_Clone() {
        ZipArchiveEntry entry1 = new ZipArchiveEntry("testFile.zip");
        entry1.setMethod(1);
        entry1.setUnixMode(755);

        ZipArchiveEntry entry2 = (ZipArchiveEntry) entry1.clone();

        assertEquals(entry1.getName(), entry2.getName());
        assertEquals(entry1.getMethod(), entry2.getMethod());
        assertEquals(entry1.getUnixMode(), entry2.getUnixMode());
    }

    @Test
    public void testZipArchiveEntry_SetExtra() {
        ZipExtraField field = new ZipExtraField();
        field.setCentralDirectoryData(new byte[]{0x01, 0x02, 0x03});

        ZipArchiveEntry entry = new ZipArchiveEntry("testFile.zip");
        entry.addExtraField(field);

        assertEquals(1, entry.getExtraFields().length);
        ZipExtraField extraField = entry.getExtraFields()[0];
        assertArrayEquals(new byte[]{0x01, 0x02, 0x03}, extraField.getCentralDirectoryData());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZipArchiveEntry_SetMethod_Negative() {
        ZipArchiveEntry entry = new ZipArchiveEntry("testFile.zip");
        entry.setMethod(-1);
    }

    @Test
    public void testZipArchiveEntry_GetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("testFile.zip");
        entry.setMethod(2);

        assertEquals(2, entry.getMethod());
    }

    // Additional test methods for other functionalities can be added here

}