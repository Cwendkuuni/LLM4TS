import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

public class X7875_NewUnixTest {

 // Test constructor
 @Test
 public void testConstructor() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 Assert.assertEquals(new ZipShort(0x7875), x7875.getHeaderId());
 Assert.assertNull(x7875.getUID());
 Assert.assertNull(x7875.getGID());
 }

 // Test getHeaderId() method
 @Test
 public void testGetHeaderId() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 Assert.assertEquals(new ZipShort(0x7875), x7875.getHeaderId());
 }

 // Test getUID() and setUID() methods
 @Test
 public void testGetUIDAndSetUID() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 long testUID = 123;
 x7875.setUID(testUID);
 Assert.assertEquals(testUID, x7875.getUID());
 }

 // Test getGID() and setGID() methods
 @Test
 public void testGetGIDAndSetGID() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 long testGID = 456;
 x7875.setGID(testGID);
 Assert.assertEquals(testGID, x7875.getGID());
 }

 // Test getLocalFileDataLength() method
 @Test
 public void testGetLocalFileDataLength() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 BigInteger testUid = BigInteger.valueOf(123);
 BigInteger testGid = BigInteger.valueOf(456);
 x7875.setUID(testUid);
 x7875.setGID(testGid);
 int uidSize = testUid.toByteArray().length;
 int gidSize = testGid.toByteArray().length;
 ZipShort expectedLength = new ZipShort(3 + uidSize + gidSize);
 Assert.assertEquals(expectedLength, x7875.getLocalFileDataLength());
 }

 // Test parseFromLocalFileData() method
 @Test
 public void testParseFromLocalFileData() throws ZipException {
 byte[] testData = new byte[] {
 (byte) 0x5A, (byte) 0x78, (byte) 0x75, (byte) 0x00, (byte) 0x01, // version=1
 (byte) 0x03, (byte) 0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // uidSize=4, uid=0
 (byte) 0x01, (byte) 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // gidSize=1, gid=1
 };
 X7875_NewUnix x7875 = new X7875_NewUnix();
 int offset = 0;
 int length = testData.length;
 x7875.parseFromLocalFileData(testData, offset, length);
 Assert.assertEquals(new ZipShort(0x7875), x7875.getHeaderId());
 Assert.assertEquals(new BigInteger("0"), x7875.getUID());
 Assert.assertEquals(new BigInteger("1"), x7875.getGID());
 }

 // Test getCentralDirectoryLength() method
 @Test
 public void testGetCentralDirectoryLength() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 Assert.assertEquals(x7875.getLocalFileDataLength(), x7875.getCentralDirectoryLength());
 }

 // Test getLocalFileDataData() method
 @Test
 public void testGetLocalFileDataData() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 BigInteger testUid = BigInteger.valueOf(123);
 BigInteger testGid = BigInteger.valueOf(456);
 x7875.setUID(testUid);
 x7875.setGID(testGid);
 byte[] expectedData = new byte[] {
 (byte) 0x5A, (byte) 0x78, (byte) 0x75, // version=1
 (byte) 0x03, (byte) 0x04, // uidSize=4, gidSize=0
 };
 byte[] uidBytes = testUid.toByteArray();
 byte[] gidBytes = testGid.toByteArray();
 byte[] data = x7875.getLocalFileDataData();
 int pos = 0;
 for (byte b : data) {
 Assert.assertEquals(expectedData[pos++], b);
 }
 }

 // Test parseFromLocalFileData() with invalid data
 @Test(expected = ZipException.class)
 public void testParseFromLocalFileDataWithInvalidData() throws ZipException {
 byte[] testData = new byte[] {
 (byte) 0x5A, (byte) 0x78, (byte) 0x75, // version=1
 (byte) 0x03, (byte) 0x04, // uidSize=4, gidSize=0
 (byte) 0x00, (byte) 0xFF, 0x00, 0x00, // uid=0xFF000000
 (byte) 0x01, (byte) 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // gidSize=1, gid=1
 };
 X7875_NewUnix x7875 = new X7875_NewUnix();
 int offset = 0;
 int length = testData.length;
 x7875.parseFromLocalFileData(testData, offset, length);
 }
}