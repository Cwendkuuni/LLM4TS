package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.*;

public class ZipArchiveEntryTest {

    @Test
    public void testZipArchiveEntry() {
        // Test constructor with name
        ZipArchiveEntry zipEntry = new ZipArchiveEntry("test.txt");
        assertEquals("test.txt", zipEntry.getName());
        assertNull(zipEntry.getSize());
        assertNull(zipEntry.getTime());
        assertEquals(-1, zipEntry.getMethod());
        assertEquals(0, zipEntry.getInternalAttributes());
        assertEquals(0, zipEntry.getExternalAttributes());
        assertNull(zipEntry.getExtraFields());

        // Test constructor with ZipEntry
        ZipEntry zipEntryFromZipEntry = new ZipArchiveEntry(new ZipEntry("test.txt"));
        assertEquals("test.txt", zipEntryFromZipEntry.getName());
        assertNull(zipEntryFromZipEntry.getSize());
        assertNull(zipEntryFromZipEntry.getTime());
        assertEquals(-1, zipEntryFromZipEntry.getMethod());
        assertEquals(0, zipEntryFromZipEntry.getInternalAttributes());
        assertEquals(0, zipEntryFromZipEntry.getExternalAttributes());
        assertNull(zipEntryFromZipEntry.getExtraFields());

        // Test constructor with File
        File file = new File("test.txt");
        ZipArchiveEntry zipEntryFromFile = new ZipArchiveEntry(file, "test.txt");
        assertEquals("test.txt", zipEntryFromFile.getName());
        assertEquals(file.length(), zipEntryFromFile.getSize());
        assertEquals(file.lastModified(), zipEntryFromFile.getTime());
        assertEquals(-1, zipEntryFromFile.getMethod());
        assertEquals(0, zipEntryFromFile.getInternalAttributes());
        assertEquals(0, zipEntryFromFile.getExternalAttributes());
        assertNull(zipEntryFromFile.getExtraFields());

        // Test cloneable
        try {
            ZipArchiveEntry cloned = zipEntryFromFile.clone();
            assertEquals("test.txt", cloned.getName());
            assertEquals(file.length(), cloned.getSize());
            assertEquals(file.lastModified(), cloned.getTime());
            assertEquals(-1, cloned.getMethod());
            assertEquals(0, cloned.getInternalAttributes());
            assertEquals(0, cloned.getExternalAttributes());
            assertNull(cloned.getExtraFields());
        } catch (CloneNotSupportedException e) {
            fail("should not throw CloneNotSupportedException");
        }

        // Test setMethod
        zipEntry.setMethod(ZipEntry.STORED);
        assertEquals(ZipEntry.STORED, zipEntry.getMethod());

        // Test getUnixMode and setUnixMode
        zipEntry.setUnixMode(0755);
        assertEquals(0755, zipEntry.getUnixMode());

        // Test getPlatform
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipEntry.getPlatform());

        // Test setPlatform
        zipEntry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, zipEntry.getPlatform());

        // Test setExtraFields
        ZipExtraField extraField = new ZipExtraField("Test Extra Field", (byte) 0x10);
        zipEntry.setExtraFields(new ZipExtraField[]{extraField});
        assertEquals(1, zipEntry.getExtraFields().length);
        assertEquals(extraField.getClass(), zipEntry.getExtraFields()[0].getClass());
        assertEquals("Test Extra Field", zipEntry.getExtraFields()[0].getName());
        assertEquals((byte) 0x10, zipEntry.getExtraFields()[0].getHeaderId());

        // Test addExtraField
        zipEntry.addExtraField(new ZipExtraField("Test Extra Field 2", (byte) 0x20));
        assertEquals(2, zipEntry.getExtraFields().length);

        // Test addAsFirstExtraField
        zipEntry.addAsFirstExtraField(new ZipExtraField("Test Extra Field 3", (byte) 0x30));
        assertEquals(3, zipEntry.getExtraFields().length);

        // Test removeExtraField
        zipEntry.removeExtraField(new ZipShort((short) 0x10));
        assertEquals(2, zipEntry.getExtraFields().length);
        assertNull(zipEntry.getExtraField(new ZipShort((short) 0x10)));

        // Test getExtraField
        ZipExtraField extraFieldGet = zipEntry.getExtraField(new ZipShort((short) 0x20));
        assertEquals("Test Extra Field 2", extraFieldGet.getName());
    }
}