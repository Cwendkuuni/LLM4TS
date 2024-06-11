import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.archivers.zip.ZipShort;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ZipArchiveEntryTest {

 private static final String TEST_NAME = "test.txt";
 private static final int TEST_METHOD = ZipEntry.STORED;
 private static final int TEST_INTERNAL_ATTRIBUTES = 0x20;
 private static final long TEST_EXTERNAL_ATTRIBUTES = 0x12345678L;
 private static final int TEST_PLATFORM = ZipArchiveEntry.PLATFORM_FAT;
 private static final byte[] TEST_LOCAL_EXTRA_DATA = new byte[0];
 private static final byte[] TEST_CENTRAL_DIRECTORY_EXTRA_DATA = new byte[0];
 private static final ZipShort TEST_EXTRA_FIELD_ID = new ZipShort((short) 0x1234);
 private static final ZipExtraField TEST_EXTRA_FIELD = new ZipGenericExtraField(TEST_EXTRA_FIELD_ID, TEST_LOCAL_EXTRA_DATA, TEST_CENTRAL_DIRECTORY_EXTRA_DATA);

 @org.junit.Test
 public void testZipArchiveEntryEmptyConstructor() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry();
 assertNull(zipArchiveEntry.getName());
 assertNotNull(zipArchiveEntry.getMethod());
 assertEquals(-1, zipArchiveEntry.getMethod());
 assertNull(zipArchiveEntry.getInternalAttributes());
 assertEquals(0, zipArchiveEntry.getExternalAttributes());
 assertNull(zipArchiveEntry.getExtraFields());
 }

 @org.junit.Test
 public void testZipArchiveEntryStringConstructor() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(TEST_NAME);
 assertEquals(TEST_NAME, zipArchiveEntry.getName());
 assertNull(zipArchiveEntry.getMethod());
 assertEquals(-1, zipArchiveEntry.getMethod());
 assertNull(zipArchiveEntry.getInternalAttributes());
 assertEquals(0, zipArchiveEntry.getExternalAttributes());
 assertNull(zipArchiveEntry.getExtraFields());
 }

 @org.junit.Test
 public void testZipArchiveEntryZipEntryConstructor() throws ZipException {
 ZipEntry zipEntry = new ZipEntry(TEST_NAME);
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(zipEntry);
 assertEquals(TEST_NAME, zipArchiveEntry.getName());
 assertEquals(TEST_METHOD, zipArchiveEntry.getMethod());
 assertNull(zipArchiveEntry.getInternalAttributes());
 assertEquals(0, zipArchiveEntry.getExternalAttributes());
 assertNull(zipArchiveEntry.getExtraFields());
 }

 @org.junit.Test
 public void testZipArchiveEntryStringConstructorWithExtraFields() throws ZipException {
 byte[] testLocalExtraData = new byte[10];
 byte[] testCentralDirectoryExtraData = new byte[10];
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(TEST_NAME, TEST_LOCAL_EXTRA_DATA, TEST_CENTRAL_DIRECTORY_EXTRA_DATA);
 assertEquals(TEST_NAME, zipArchiveEntry.getName());
 assertNotNull(zipArchiveEntry.getMethod());
 assertEquals(TEST_METHOD, zipArchiveEntry.getMethod());
 assertNull(zipArchiveEntry.getInternalAttributes());
 assertEquals(0, zipArchiveEntry.getExternalAttributes());
 assertEquals(1, zipArchiveEntry.getExtraFields().length);
 assertTrue(zipArchiveEntry.getExtraFields()[0] instanceof ZipGenericExtraField);
 assertEquals(TEST_EXTRA_FIELD_ID, zipArchiveEntry.getExtraFields()[0].getHeaderId());
 assertEquals(TEST_LOCAL_EXTRA_DATA, zipArchiveEntry.getExtraFields()[0].getLocalFileDataData());
 assertEquals(TEST_CENTRAL_DIRECTORY_EXTRA_DATA, zipArchiveEntry.getExtraFields()[0].getCentralDirectoryData());
 }

 @org.junit.Test
 public void testZipArchiveEntryZipArchiveEntryConstructor() throws ZipException {
 ZipArchiveEntry originalZipArchiveEntry = new ZipArchiveEntry(TEST_NAME, TEST_LOCAL_EXTRA_DATA, TEST_CENTRAL_DIRECTORY_EXTRA_DATA);
 ZipArchiveEntry copyZipArchiveEntry = new ZipArchiveEntry(originalZipArchiveEntry);
 assertEquals(TEST_NAME, copyZipArchiveEntry.getName());
 assertEquals(TEST_METHOD, copyZipArchiveEntry.getMethod());
 assertNull(copyZipArchiveEntry.getInternalAttributes());
 assertEquals(0, copyZipArchiveEntry.getExternalAttributes());
 assertEquals(1, copyZipArchiveEntry.getExtraFields().length);
 assertTrue(copyZipArchiveEntry.getExtraFields()[0] instanceof ZipGenericExtraField);
 assertEquals(TEST_EXTRA_FIELD_ID, copyZipArchiveEntry.getExtraFields()[0].getHeaderId());
 assertEquals(TEST_LOCAL_EXTRA_DATA, copyZipArchiveEntry.getExtraFields()[0].getLocalFileDataData());
 assertEquals(TEST_CENTRAL_DIRECTORY_EXTRA_DATA, copyZipArchiveEntry.getExtraFields()[0].getCentralDirectoryData());
 }

 @org.junit.Test
 public void testZipArchiveEntryClonedObject() throws ZipException, CloneNotSupportedException {
 ZipArchiveEntry originalZipArchiveEntry = new ZipArchiveEntry(TEST_NAME, TEST_LOCAL_EXTRA_DATA, TEST_CENTRAL_DIRECTORY_EXTRA_DATA);
 ZipArchiveEntry copyZipArchiveEntry = (ZipArchiveEntry) originalZipArchiveEntry.clone();
 assertEquals(TEST_NAME, copyZipArchiveEntry.getName());
 assertEquals(TEST_METHOD, copyZipArchiveEntry.getMethod());
 assertNull(copyZipArchiveEntry.getInternalAttributes());
 assertEquals(0, copyZipArchiveEntry.getExternalAttributes());
 assertEquals(1, copyZipArchiveEntry.getExtraFields().length);
 assertTrue(copyZipArchiveEntry.getExtraFields()[0] instanceof ZipGenericExtraField);
 assertEquals(TEST_EXTRA_FIELD_ID, copyZipArchiveEntry.getExtraFields()[0].getHeaderId());
 assertEquals(TEST_LOCAL_EXTRA_DATA, copyZipArchiveEntry.getExtraFields()[0].getLocalFileDataData());
 assertEquals(TEST_CENTRAL_DIRECTORY_EXTRA_DATA, copyZipArchiveEntry.getExtraFields()[0].getCentralDirectoryData());
 }

 @org.junit.Test
 public void testZipArchiveEntryStringConstructorWithNullName() {
 assertThrows(NullPointerException.class, () -> new ZipArchiveEntry(null));
 }

 @org.junit.Test
 public void testZipArchiveEntryConstructorWithNullLocalExtraData() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(TEST_NAME, null, TEST_CENTRAL_DIRECTORY_EXTRA_DATA);
 assertNotNull(zipArchiveEntry.getMethod());
 assertEquals(-1, zipArchiveEntry.getMethod());
 assertNull(zipArchiveEntry.getInternalAttributes());
 assertEquals(0, zipArchiveEntry.getExternalAttributes());
 assertNull(zipArchiveEntry.getExtraFields());
 }

 @org.junit.Test
 public void testZipArchiveEntryConstructorWithNullCentralDirectoryExtraData() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(TEST_NAME, TEST_LOCAL_EXTRA_DATA, null);
 assertNotNull(zipArchiveEntry.getMethod());
 assertEquals(-1, zipArchiveEntry.getMethod());
 assertNull(zipArchiveEntry.getInternalAttributes());
 assertEquals(0, zipArchiveEntry.getExternalAttributes());
 assertNull(zipArchiveEntry.getExtraFields());
 }

 @org.junit.Test
 public void testZipArchiveEntryStringConstructorWithEmptyLocalExtraData() {
 byte[] testEmptyLocalExtraData = new byte[0];
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(TEST_NAME, testEmptyLocalExtraData, TEST_CENTRAL_DIRECTORY_EXTRA_DATA);
 assertNotNull(zipArchiveEntry.getMethod());
 assertEquals(-1, zipArchiveEntry.getMethod());
 assertNull(zipArchiveEntry.getInternalAttributes());
 assertEquals(0, zipArchiveEntry.getExternalAttributes());
 assertNull(zipArchiveEntry.getExtraFields());
 }

 @org.junit.Test
 public void testZipArchiveEntryStringConstructorWithEmptyCentralDirectoryExtraData() {
 byte[] testEmptyCentralDirectoryExtraData = new byte[0];
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(TEST_NAME, TEST_LOCAL_EXTRA_DATA, testEmptyCentralDirectoryExtraData);
 assertNotNull(zipArchiveEntry.getMethod());
 assertEquals(-1, zipArchiveEntry.getMethod());
 assertNull(zipArchiveEntry.getInternalAttributes());
 assertEquals(0, zipArchiveEntry.getExternalAttributes());
 assertNull(zipArchiveEntry.getExtraFields());
 }

 @org.junit.Test
 public void testZipArchiveEntryMethods() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(TEST_NAME);
 assertEquals(TEST_NAME, zipArchiveEntry.getName());
 assertEquals(-1, zipArchiveEntry.getMethod());
 assertNull(zipArchiveEntry.getInternalAttributes());
 assertEquals(0, zipArchiveEntry.getExternalAttributes());
 assertNull(zipArchiveEntry.getExtraFields());
 zipArchiveEntry.setMethod(TEST_METHOD);
 assertEquals(TEST_METHOD, zipArchiveEntry.getMethod());
 zipArchiveEntry.setInternalAttributes(TEST_INTERNAL_ATTRIBUTES);
 assertEquals(TEST_INTERNAL_ATTRIBUTES, zipArchiveEntry.getInternalAttributes());
 zipArchiveEntry.setExternalAttributes(TEST_EXTERNAL_ATTRIBUTES);
 assertEquals(TEST_EXTERNAL_ATTRIBUTES, zipArchiveEntry.getExternalAttributes());
 zipArchiveEntry.setPlatform(TEST_PLATFORM);
 assertEquals(TEST_PLATFORM, zipArchiveEntry.getPlatform());
 zipArchiveEntry.setExtraFields(new ZipExtraField[]{TEST_EXTRA_FIELD});
 assertArrayEquals(new ZipExtraField[]{TEST_EXTRA_FIELD}, zipArchiveEntry.getExtraFields());
 }

 @org.junit.Test
 public void testZipArchiveEntryMethodsWithExtraFields() {
 byte[] testLocalExtraData = new byte[10];
 byte[] testCentralDirectoryExtraData = new byte[10];
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(TEST_NAME, testLocalExtraData, testCentralDirectoryExtraData);
 assertEquals(TEST_NAME, zipArchiveEntry.getName());
 assertEquals(TEST_METHOD, zipArchiveEntry.getMethod());
 assertNull(zipArchiveEntry.getInternalAttributes());
 assertEquals(0, zipArchiveEntry.getExternalAttributes());
 assertEquals(1, zipArchiveEntry.getExtraFields().length);
 assertTrue(zipArchiveEntry.getExtraFields()[0] instanceof ZipGenericExtraField);
 assertEquals(TEST_EXTRA_FIELD_ID, zipArchiveEntry.getExtraFields()[0].getHeaderId());
 assertEquals(testLocalExtraData, zipArchiveEntry.getExtraFields()[0].getLocalFileDataData());
 assertEquals(testCentralDirectoryExtraData, zipArchiveEntry.getExtraFields()[0].getCentralDirectoryData());
 zipArchiveEntry.setInternalAttributes(TEST_INTERNAL_ATTRIBUTES);
 assertEquals(TEST_INTERNAL_ATTRIBUTES, zipArchiveEntry.getInternalAttributes());
 zipArchiveEntry.setExternalAttributes(TEST_EXTERNAL_ATTRIBUTES);
 assertEquals(TEST_EXTERNAL_ATTRIBUTES, zipArchiveEntry.getExternalAttributes());
 zipArchiveEntry.setPlatform(TEST_PLATFORM);
 assertEquals(TEST_PLATFORM, zipArchiveEntry.getPlatform());
 zipArchiveEntry.setExtraFields(new ZipExtraField[]{TEST_EXTRA_FIELD, new ZipGenericExtraField(TEST_EXTRA_FIELD_ID, new byte[0], new byte[0])});
 assertEquals(2, zipArchiveEntry.getExtraFields().length);
 }

 @org.junit.Test
 public void testZipArchiveEntryIsSupportedCompressionMethod() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(TEST_NAME);
 assertFalse(zipArchiveEntry.isSupportedCompressionMethod());
 zipArchiveEntry.setMethod(ZipEntry.STORED);
 assertTrue(zipArchiveEntry.isSupportedCompressionMethod());
 zipArchiveEntry.setMethod(ZipEntry.DEFLATED);
 assertTrue(zipArchiveEntry.isSupportedCompressionMethod());
 zipArchiveEntry.setMethod(ZipEntry.UNKNOWN);
 assertFalse(zipArchiveEntry.isSupportedCompressionMethod());
 }

 @org.junit.Test
 public void testZipArchiveEntryGetName() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(TEST_NAME);
 assertEquals(TEST_NAME, zipArchiveEntry.getName());
 ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry(TEST_NAME + "/");
 assertEquals(TEST_NAME, zipArchiveEntry2.getName());
 }

 @org.junit.Test
 public void testZipArchiveEntryIsDirectory() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(TEST_NAME);
 assertFalse(zipArchiveEntry.isDirectory());
 ZipArchiveEntry zipArchiveEntryDirectory = new ZipArchiveEntry(TEST_NAME + "/");
 assertTrue(zipArchiveEntryDirectory.isDirectory());
 }

 @org.junit.Test
 public void testZipArchiveEntryHashCode() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(TEST_NAME);
 assertEquals(TEST_NAME.hashCode(), zipArchiveEntry.hashCode());
 }

 @org.junit.Test
 public void testZipArchiveEntryEqualsObject() {
 ZipArchiveEntry zipArchiveEntry1 = new ZipArchiveEntry(TEST_NAME);
 ZipArchiveEntry zipArchiveEntry2 = new ZipArchiveEntry(TEST_NAME);
 assertTrue(zipArchiveEntry1.equals(zipArchiveEntry2));
 ZipArchiveEntry zipArchiveEntry3 = new ZipArchiveEntry(TEST_NAME + "/");
 assertFalse(zipArchiveEntry1.equals(zipArchiveEntry3));
 Object otherObject = new Object();
 assertFalse(zipArchiveEntry1.equals(otherObject));
 }

 @org.junit.Test
 public void testZipArchiveEntryGetLastModifiedDate() {
 ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(TEST_NAME);
 Date lastModifiedDate = zipArchiveEntry.getLastModifiedDate();
 assertNotNull(lastModifiedDate);
 }

}