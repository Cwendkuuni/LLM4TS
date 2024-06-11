import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.junit.Test;
import static org.junit.Assert.*;

public class ZipArchiveEntryTest {

    @Test
    public void testCreateNewZipEntry() {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.txt");
        assertNull(zipEntry.getExtraFields());
        assertEquals("test.txt", zipEntry.getName());
        assertEquals(0, zipEntry.getInternalAttributes());
        assertEquals(0, zipEntry.getExternalAttributes());
        assertFalse(zipEntry.isSupportedCompressionMethod());
    }

    @Test
    public void testCreateZipEntryFromZipEntry() throws ZipException {
        ZipEntry zipEntry = new ZipEntry("test.txt");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
        assertEquals("test.txt", zipArchiveEntry.getName());
        assertArrayEquals(zipEntry.getExtra(), zipArchiveEntry.getExtra());
        assertEquals(zipEntry.getMethod(), zipArchiveEntry.getMethod());
        assertEquals(zipEntry.getSize(), zipArchiveEntry.getSize());
        assertEquals(zipEntry.getTime(), zipArchiveEntry.getTime());
        assertFalse(zipArchiveEntry.isSupportedCompressionMethod());
    }

    @Test
    public void testCreateZipArchiveEntryFromZipArchiveEntry() throws ZipException {
        ZipEntry zipEntry = new ZipEntry("test.txt");
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
        ZipArchiveEntry clonedEntry = (ZipArchiveEntry) zipArchiveEntry.clone();
        assertEquals(zipArchiveEntry, clonedEntry);
    }

    @Test
    public void testSetMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setMethod(ZipEntry.STORED);
        assertTrue(zipArchiveEntry.isSupportedCompressionMethod());
        zipArchiveEntry.setMethod(ZipEntry.DEFLATED);
        assertTrue(zipArchiveEntry.isSupportedCompressionMethod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMethodNegative() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setMethod(-1);
    }

    @Test
    public void testGetMethod() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setMethod(ZipEntry.STORED);
        assertEquals(ZipEntry.STORED, zipArchiveEntry.getMethod());
        zipArchiveEntry.setMethod(ZipEntry.DEFLATED);
        assertEquals(ZipEntry.DEFLATED, zipArchiveEntry.getMethod());
        zipArchiveEntry.setMethod(-1);
        assertEquals(-1, zipArchiveEntry.getMethod());
    }

    @Test
    public void testSetInternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setInternalAttributes(ZipEntry.FILE_ATTRIBUTE_HIDDEN);
        assertEquals(ZipEntry.FILE_ATTRIBUTE_HIDDEN, zipArchiveEntry.getInternalAttributes());
        zipArchiveEntry.setInternalAttributes(ZipEntry.FILE_ATTRIBUTE_DIRECTORY | ZipEntry.FILE_ATTRIBUTE_HIDDEN);
        assertEquals(ZipEntry.FILE_ATTRIBUTE_DIRECTORY | ZipEntry.FILE_ATTRIBUTE_HIDDEN, zipArchiveEntry.getInternalAttributes());
    }

    @Test
    public void testGetInternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setInternalAttributes(ZipEntry.FILE_ATTRIBUTE_HIDDEN);
        assertEquals(ZipEntry.FILE_ATTRIBUTE_HIDDEN, zipArchiveEntry.getInternalAttributes());
        zipArchiveEntry.setInternalAttributes(ZipEntry.FILE_ATTRIBUTE_DIRECTORY | ZipEntry.FILE_ATTRIBUTE_HIDDEN);
        assertEquals(ZipEntry.FILE_ATTRIBUTE_DIRECTORY | ZipEntry.FILE_ATTRIBUTE_HIDDEN, zipArchiveEntry.getInternalAttributes());
    }

    @Test
    public void testSetExternalAttributes() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setUnixMode(0755);
        assertEquals(0755, zipArchiveEntry.getUnixMode());
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        zipArchiveEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipArchiveEntry.getPlatform());
    }

    @Test
    public void testAddExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipShort headerId = new ZipShort("custom_field");
        ZipExtraField extraField = new ZipGenericExtraField("custom_field", "custom_value");
        zipArchiveEntry.addExtraField(extraField);
        ZipExtraField[] extraFields = zipArchiveEntry.getExtraFields();
        assertEquals(1, extraFields.length);
        assertEquals(headerId, extraFields[0].getHeaderId());
        assertEquals("custom_value", extraFields[0].toString());
    }

    @Test
    public void testSetExtraFields() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipExtraField[] extraFields = new ZipExtraField[] {
                new ZipGenericExtraField("custom_field1", "custom_value1"),
                new ZipGenericExtraField("custom_field2", "custom_value2")
        };
        zipArchiveEntry.setExtraFields(extraFields);
        ZipExtraField[] testExtraFields = zipArchiveEntry.getExtraFields();
        assertEquals(2, testExtraFields.length);
        assertEquals(extraFields[0].getHeaderId(), testExtraFields[0].getHeaderId());
        assertEquals(extraFields[1].getHeaderId(), testExtraFields[1].getHeaderId());
    }

    @Test
    public void testRemoveExtraField() {
        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("test.txt");
        ZipShort headerId = new ZipShort("custom_field");
        ZipExtraField extraField = new ZipGenericExtraField("custom_field", "custom_value");
        zipArchiveEntry.addExtraField(extraField);
        zipArchiveEntry.removeExtraField(headerId);
        ZipExtraField[] extraFields = zipArchiveEntry.getExtraFields();
        assertEquals(0, extraFields.length);
    }
}