import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class X7875_NewUnixTest {

 private static final X7875_NewUnix TEST_DATA = new X7875_NewUnix();
 static {
 TEST_DATA.setUID(12345);
 TEST_DATA.setGID(67890);
 }

 @Test
 public void testGetHeaderId() {
 assertEquals(X7875_NewUnix.HEADER_ID, TEST_DATA.getHeaderId());
 }

 @Test
 public void testGetUID() {
 assertEquals(12345, TEST_DATA.getUID());
 }

 @Test
 public void testSetUID() {
 X7875_NewUnix testDataCopy = new X7875_NewUnix();
 testDataCopy.setUID(45678);
 assertEquals(45678, testDataCopy.getUID());
 }

 @Test
 public void testGetGID() {
 assertEquals(67890, TEST_DATA.getGID());
 }

 @Test
 public void testSetGID() {
 X7875_NewUnix testDataCopy = new X7875_NewUnix();
 testDataCopy.setGID(90876);
 assertEquals(90876, testDataCopy.getGID());
 }

 @Test
 public void testGetLocalFileDataLength() {
 ZipShort expectedDataLength = new ZipShort(3 + 5 + 5); // version=1 + uidSize=5 + gidSize=5
 assertEquals(expectedDataLength, TEST_DATA.getLocalFileDataLength());
 }

 @Test
 public void testGetCentralDirectoryLength() {
 assertEquals(TEST_DATA.getLocalFileDataLength(), TEST_DATA.getCentralDirectoryLength());
 }

 @Test
 public void testGetLocalFileDataData() {
 byte[] expectedData = new byte[3 + 5 + 5];
 // version=1
 expectedData[0] = (byte) 0x01;
 // uidSize=5
 expectedData[1] = (byte) 0x05;
 // uid=12345
 for (int i = 0; i < 5; i++) {
 expectedData[2 + i] = (byte) 0x34;
 expectedData[2 + i + 5] = (byte) 0x35;
 }
 // gidSize=5
 expectedData[2 + 5] = (byte) 0x05;
 // gid=67890
 for (int i = 0; i < 5; i++) {
 expectedData[3 + i] = (byte) 0x67;
 expectedData[3 + i + 5] = (byte) 0x89;
 }
 byte[] actualData = TEST_DATA.getLocalFileDataData();
 assertArrayEquals(expectedData, actualData);
 }

 @Test
 public void testParseFromLocalFileData() throws ZipException {
 byte[] testData = TEST_DATA.getLocalFileDataData();
 X7875_NewUnix parsedData = new X7875_NewUnix();
 parsedData.parseFromLocalFileData(testData, 0, testData.length);
 assertEquals(TEST_DATA.getUID(), parsedData.getUID());
 assertEquals(TEST_DATA.getGID(), parsedData.getGID());
 }

 @Test(expected = ZipException.class)
 public void testParseFromLocalFileDataInvalidData() throws ZipException {
 byte[] invalidData = new byte[] { 0x78, 0x75, 0x01, 0x05, 0x01, 0x05, 0x01, 0x05, 0x01 };
 X7875_NewUnix parsedData = new X7875_NewUnix();
 parsedData.parseFromLocalFileData(invalidData, 0, invalidData.length);
 assertNull(parsedData.getUID());
 assertNull(parsedData.getGID());
 }

 @Test(expected = ZipException.class)
 public void testParseLocalFileDataMissingData() throws ZipException {
 byte[] missingData = new byte[] { 0x78, 0x75, 0x01 };
 X7875_NewUnix parsedData = new X7875_NewUnix();
 parsedData.parseFromLocalFileData(missingData, 0, missingData.length);
 assertNull(parsedData.getUID());
 assertNull(parsedData.getGID());
 }

 @Test
 public void testReset() {
 X7875_NewUnix testDataCopy = new X7875_NewUnix();
 testDataCopy.parseFromLocalFileData(TEST_DATA.getLocalFileDataData(), 0, TEST_DATA.getLocalFileDataLength().getValue());
 testDataCopy.reset();
 assertEquals(0, testDataCopy.getUID());
 assertEquals(0, testDataCopy.getGID());
 }

}