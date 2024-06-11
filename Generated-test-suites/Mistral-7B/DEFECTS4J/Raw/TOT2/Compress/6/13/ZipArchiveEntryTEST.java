import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.zip.ZipException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ZipArchiveEntryTest {

    private ZipFile zipFile;
    private File tempFile;

    @Before
    public void setUp() throws Exception {
        tempFile = File.createTempFile("test", null);
        zipFile = new ZipFile(tempFile);
    }

    @After
    public void tearDown() throws Exception {
        zipFile.close();
        tempFile.delete();
    }

    @Test
    public void testEmptyConstructor() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        assertEquals("", entry.getName());
        assertNull(entry.getExtraFields());
        assertNull(entry.getMethod());
        assertNull(entry.getInternalAttributes());
        assertNull(entry.getExternalAttributes());
        assertNull(entry.getPlatform());
    }

    @Test
    public void testZipEntryConstructor() throws ZipException {
        ZipEntry zipEntry = new ZipEntry("test.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        assertEquals("test.txt", entry.getName());
        assertNull(entry.getExtraFields());
        assertEquals(ZipEntry.STORED, entry.getMethod());
        assertFalse(entry.isDirectory());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(0, entry.getExternalAttributes());
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
    }

    @Test
    public void testCloneConstructor() throws ZipException {
        ZipEntry zipEntry = new ZipEntry("test.txt");
        ZipArchiveEntry originalEntry = new ZipArchiveEntry(zipEntry);
        ZipArchiveEntry clonedEntry = (ZipArchiveEntry) originalEntry.clone();
        assertEquals(originalEntry.getName(), clonedEntry.getName());
        assertSame(originalEntry.getExtraFields(), clonedEntry.getExtraFields());
        assertEquals(originalEntry.getMethod(), clonedEntry.getMethod());
        assertFalse(clonedEntry.isDirectory());
        assertEquals(originalEntry.getInternalAttributes(), clonedEntry.getInternalAttributes());
        assertEquals(originalEntry.getExternalAttributes(), clonedEntry.getExternalAttributes());
        assertEquals(originalEntry.getPlatform(), clonedEntry.getPlatform());
    }

    @Test
    public void testFileConstructor() throws IOException, ZipException {
        File inputFile = new File("test.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(inputFile, "test.txt");
        assertEquals("test.txt", entry.getName());
        assertFalse(entry.isDirectory());
        assertEquals(-1, entry.getMethod());
        assertEquals(0, entry.getInternalAttributes());
        assertEquals(inputFile.length(), entry.getSize());
        assertEquals(inputFile.lastModified(), entry.getTime());
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());
    }

    @Test
    public void testSetUnixMode() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        entry.setUnixMode(0755);
        assertEquals(0755, entry.getUnixMode());
    }

    @Test
    public void testGetMethod() throws ZipException {
        ZipEntry zipEntry = new ZipEntry("test.txt");
        ZipArchiveEntry entry = new ZipArchiveEntry(zipEntry);
        assertEquals(ZipEntry.STORED, entry.getMethod());

        ZipArchiveEntry entryWithMethod = new ZipArchiveEntry("");
        entryWithMethod.setMethod(ZipEntry.DEFLATED);
        assertEquals(ZipEntry.DEFLATED, entryWithMethod.getMethod());
    }

    @Test
    public void testSetMethod() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        entry.setMethod(ZipEntry.DEFLATED);
        assertEquals(ZipEntry.DEFLATED, entry.getMethod());
    }

    @Test
    public void testGetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        assertEquals(0, entry.getInternalAttributes());

        ZipArchiveEntry entryWithInternalAttributes = new ZipArchiveEntry("");
        entryWithInternalAttributes.setInternalAttributes(ZipEntry.FLAG_UNIX_GROUP_MASK | ZipEntry.FLAG_UNIX_USER_MASK | ZipEntry.FLAG_UNIX_PERMS);
        assertEquals(ZipEntry.FLAG_UNIX_GROUP_MASK | ZipEntry.FLAG_UNIX_USER_MASK | ZipEntry.FLAG_UNIX_PERMS, entryWithInternalAttributes.getInternalAttributes());
    }

    @Test
    public void testSetInternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        entry.setInternalAttributes(ZipEntry.FLAG_UNIX_GROUP_MASK | ZipEntry.FLAG_UNIX_USER_MASK | ZipEntry.FLAG_UNIX_PERMS);
        assertEquals(ZipEntry.FLAG_UNIX_GROUP_MASK | ZipEntry.FLAG_UNIX_USER_MASK | ZipEntry.FLAG_UNIX_PERMS, entry.getInternalAttributes());
    }

    @Test
    public void testGetExternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        assertEquals(0L, entry.getExternalAttributes());

        ZipArchiveEntry entryWithExternalAttributes = new ZipArchiveEntry("");
        entryWithExternalAttributes.setExternalAttributes(0x22L);
        assertEquals(0x22L, entryWithExternalAttributes.getExternalAttributes());
    }

    @Test
    public void testSetExternalAttributes() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        entry.setExternalAttributes(0x22L);
        assertEquals(0x22L, entry.getExternalAttributes());
    }

    @Test
    public void testGetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        assertEquals(ZipArchiveEntry.PLATFORM_FAT, entry.getPlatform());

        ZipArchiveEntry entryWithPlatform = new ZipArchiveEntry("");
        entryWithPlatform.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entryWithPlatform.getPlatform());
    }

    @Test
    public void testSetPlatform() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        entry.setPlatform(ZipArchiveEntry.PLATFORM_UNIX);
        assertEquals(ZipArchiveEntry.PLATFORM_UNIX, entry.getPlatform());
    }

    @Test
    public void testSetExtraFields() throws ZipException {
        ZipExtraField[] extraFields = new ZipExtraField[]{new ZipExtendedTimestamps(), new ZipLocalFileHeaderVer2Test()};
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        entry.setExtraFields(extraFields);
        assertEquals(2, entry.getExtraFields().length);
        assertTrue(entry.getExtraFields()[0] instanceof ZipExtendedTimestamps);
        assertTrue(entry.getExtraFields()[1] instanceof ZipLocalFileHeaderVer2Test);
    }

    @Test
    public void testGetExtraFields() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        entry.setExtraFields(new ZipExtraField[]{new ZipExtendedTimestamps(), new ZipLocalFileHeaderVer2Test()});
        ZipExtraField[] extraFields = entry.getExtraFields();
        assertEquals(2, extraFields.length);
        assertTrue(extraFields[0] instanceof ZipExtendedTimestamps);
        assertTrue(extraFields[1] instanceof ZipLocalFileHeaderVer2Test);
    }

    @Test
    public void testAddExtraField() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        ZipExtraField extraField = new ZipExtendedTimestamps();
        entry.addExtraField(extraField);
        ZipExtraField[] extraFields = entry.getExtraFields();
        assertEquals(1, extraFields.length);
        assertTrue(extraFields[0] instanceof ZipExtendedTimestamps);
        assertEquals(extraField.getHeaderId(), extraFields[0].getHeaderId());
    }

    @Test
    public void testAddAsFirstExtraField() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        ZipExtraField extraField = new ZipExtendedTimestamps();
        entry.addAsFirstExtraField(extraField);
        ZipExtraField[] extraFields = entry.getExtraFields();
        assertEquals(1, extraFields.length);
        assertTrue(extraFields[0] instanceof ZipExtendedTimestamps);
        assertEquals(extraField.getHeaderId(), extraFields[0].getHeaderId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddExtraFieldWithNullExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        entry.addExtraField(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAsFirstExtraFieldWithNullExtraField() {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        entry.addAsFirstExtraField(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddExtraFieldWithDuplicateHeaderId() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        ZipExtraField extraField1 = new ZipExtendedTimestamps();
        ZipExtraField extraField2 = new ZipExtendedTimestamps();
        entry.addExtraField(extraField1);
        entry.addExtraField(extraField2);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddAsFirstExtraFieldWithDuplicateHeaderId() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        ZipExtraField extraField1 = new ZipExtendedTimestamps();
        ZipExtraField extraField2 = new ZipExtendedTimestamps();
        entry.addAsFirstExtraField(extraField1);
        entry.addAsFirstExtraField(extraField2);
    }

    @Test
    public void testRemoveExtraField() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        ZipExtraField extraField = new ZipExtendedTimestamps();
        entry.addExtraField(extraField);
        entry.removeExtraField(extraField.getHeaderId());
        ZipExtraField[] extraFields = entry.getExtraFields();
        assertEquals(0, extraFields.length);
    }

    @Test(expected = NoSuchElementException.class)
    public void testRemoveExtraFieldWithNonExistentHeaderId() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        entry.removeExtraField(new ZipShort((short) 0));
    }

    @Test
    public void testGetExtraField() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        ZipExtraField extraField = new ZipExtendedTimestamps();
        entry.addExtraField(extraField);
        ZipExtraField retrievedExtraField = entry.getExtraField(extraField.getHeaderId());
        assertEquals(extraField, retrievedExtraField);
    }

    @Test
    public void testGetExtraFieldWithNonExistentHeaderId() throws ZipException {
        ZipArchiveEntry entry = new ZipArchiveEntry("");
        ZipExtraField retrievedExtraField = entry.getExtraField(new ZipShort((short) 0));
        assertNull(retrievedExtraField);
    }
}