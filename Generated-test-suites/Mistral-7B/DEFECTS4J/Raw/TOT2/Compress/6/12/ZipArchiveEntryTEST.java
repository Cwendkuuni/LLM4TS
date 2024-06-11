import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ZipArchiveEntryTest {

 @Test
 public void testConstructorWithName() {
 ZipArchiveEntry zipEntry = new ZipArchiveEntry("entryName.txt");
 assertEquals("entryName.txt", zipEntry.getName());
 }

 @Test
 public void testConstructorWithZipEntry() throws ZipException {
 ZipEntry zipEntry = new ZipEntry("entryName.txt");
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
 assertEquals("entryName.txt", zipArchiveEntry.getName());
 }

 @Test
 public void testConstructorWithZipArchiveEntry() throws ZipException {
 ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry("entryName1.txt");
 ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry(zipArchiveEntry1);
 assertEquals("entryName1.txt", zipArchiveEntry2.getName());
 }

 @Test
 public void testEmptyConstructor() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
 assertNull(zipArchiveEntry.getName());
 }

 @Test
 public void testConstructorWithFileInput() {
 File file = new File("testFile.txt");
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, "entryName.txt");
 assertEquals("entryName.txt", zipArchiveEntry.getName());
 assertEquals(file.length(), zipArchiveEntry.getSize());
 assertEquals(file.lastModified(), zipArchiveEntry.getTime());
 }

 @Test
 public void testSetUnixMode() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("entryName.txt");
 zipArchiveEntry.setUnixMode(0755);
 assertEquals(0755, zipArchiveEntry.getUnixMode());
 }

 @Test
 public void testGetPlatform() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("entryName.txt");
 assertEquals(ZipArchiveEntry.PLATFORM_FAT, zipArchiveEntry.getPlatform());
 }

 @Test
 public void testSetMethod() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("entryName.txt");
 zipArchiveEntry.setMethod(ZipEntry.STORED);
 assertEquals(ZipEntry.STORED, zipArchiveEntry.getMethod());
 }

 @Test
 public void testIsSupportedCompressionMethod() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("entryName.txt");
 assertFalse(zipArchiveEntry.isSupportedCompressionMethod());
 zipArchiveEntry.setMethod(ZipEntry.DEFLATED);
 assertTrue(zipArchiveEntry.isSupportedCompressionMethod());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetMethodNegativeValue() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("entryName.txt");
 zipArchiveEntry.setMethod(-1);
 }

 @Test
 public void testGetInternalAttributes() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("entryName.txt");
 assertEquals(0, zipArchiveEntry.getInternalAttributes());
 }

 @Test
 public void testSetInternalAttributes() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("entryName.txt");
 zipArchiveEntry.setInternalAttributes(0xFFFF);
 assertEquals(0xFFFF, zipArchiveEntry.getInternalAttributes());
 }

 @Test
 public void testGetExternalAttributes() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("entryName.txt");
 assertEquals(0, zipArchiveEntry.getExternalAttributes());
 }

 @Test
 public void testSetExternalAttributes() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("entryName.txt");
 zipArchiveEntry.setExternalAttributes(0x0123456789ABCDEFL);
 assertEquals(0x0123456789ABCDEFL, zipArchiveEntry.getExternalAttributes());
 }

 @Test
 public void testAddExtraField() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("entryName.txt");
 ZipExtraField extraField = new ZipExtendedInfo();
 zipArchiveEntry.addExtraField(extraField);
 assertNotNull(zipArchiveEntry.getExtraFields());
 }

 @Test
 public void testAddAsFirstExtraField() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("entryName.txt");
 ZipExtraField extraField = new ZipExtendedInfo();
 zipArchiveEntry.addAsFirstExtraField(extraField);
 assertEquals(extraField, zipArchiveEntry.getExtraFields()[0]);
 }

 @Test
 public void testRemoveExtraField() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry("entryName.txt");
 ZipExtraField extraField = new ZipExtendedInfo();
 zipArchiveEntry.addExtraField(extraField);
 zipArchiveEntry.removeExtraField(ZipShort.EXT_INFO);
 assertNull(zipArchiveEntry.getExtraField(ZipShort.EXT_INFO));
 }
}