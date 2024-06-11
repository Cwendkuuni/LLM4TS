import org.junit.Test;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import java.io.File;
import static org.junit.Assert.*;

public class ZipArchiveEntryTest {

    @Test
    public void testConstructor() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.zip");
        assertNotNull(zipEntry);
        assertEquals("test.zip", zipEntry.getName());
    }

    @Test
    public void testMethods() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.zip");

        // Test getMethod() and setMethod()
        zipEntry.setMethod(1);
        assertEquals(1, zipEntry.getMethod());

        // Test getInternalAttributes() and setInternalAttributes()
        zipEntry.setInternalAttributes(2);
        assertEquals(2, zipEntry.getInternalAttributes());

        // Test getExternalAttributes() and setExternalAttributes()
        zipEntry.setExternalAttributes(3);
        assertEquals(3, zipEntry.getExternalAttributes());

        // Test setUnixMode() and getUnixMode()
        zipEntry.setUnixMode(4);
        assertEquals(4, zipEntry.getUnixMode());

        // Test getPlatform()
        assertEquals(0, zipEntry.getPlatform());

        // Test getExtraFields() and setExtraFields()
        ZipExtraField[] extraFields = {new ZipExtraField()};
        zipEntry.setExtraFields(extraFields);
        assertArrayEquals(extraFields, zipEntry.getExtraFields());

        // Test addExtraField(), getExtraField() and removeExtraField()
        ZipExtraField newExtraField = new ZipExtraField();
        zipEntry.addExtraField(newExtraField);
        assertSame(newExtraField, zipEntry.getExtraField(newExtraField.getHeaderId()));
        zipEntry.removeExtraField(newExtraField.getHeaderId());
        assertNull(zipEntry.getExtraField(newExtraField.getHeaderId()));

        // Test getLastModifiedDate()
        File file = new File("test.zip");
        zipEntry.setTime(file.lastModified());
        assertEquals(file.lastModified(), zipEntry.getLastModifiedDate().getTime());

        // Test getName()
        assertEquals("test.zip", zipEntry.getName());

        // Test isDirectory()
        assertFalse(zipEntry.isDirectory());

        // Test equals()
        ZipArchiveEntry otherZipEntry = new ZipArchiveEntry("test.zip");
        assertTrue(zipEntry.equals(otherZipEntry));
        assertFalse(zipEntry.equals(null));
    }
}