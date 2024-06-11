import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.junit.Assert;
import org.junit.Test;

public class X7875_NewUnixTest {

 @Test
 public void testGetHeaderId() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 ZipShort headerId = x7875.getHeaderId();
 Assert.assertEquals(new ZipShort(0x7875), headerId);
 }

 @Test
 public void testGetUIDAndSetUID() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 long uid = 123456789L;
 x7875.setUID(uid);
 long actualUid = x7875.getUID();
 Assert.assertEquals(uid, actualUid);
 }

 @Test
 public void testSetUIDWithNegativeNumber() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 long uid = -123456789L;
 try {
 x7875.setUID(uid);
 Assert.fail("Expected ZipException but no exception was thrown");
 } catch (ZipException e) {
 // Expected exception
 }
 }

 @Test
 public void testGetGIDAndSetGID() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 long gid = 987654321L;
 x7875.setGID(gid);
 long actualGid = x7875.getGID();
 Assert.assertEquals(gid, actualGid);
 }

 @Test
 public void testSetGIDWithNegativeNumber() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 long gid = -987654321L;
 try {
 x7875.setGID(gid);
 Assert.fail("Expected ZipException but no exception was thrown");
 } catch (ZipException e) {
 // Expected exception
 }
 }

 @Test
 public void testGetLocalFileDataLength() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 BigInteger uid = new BigInteger("12345678912345678912345678912345678912345");
 BigInteger gid = new BigInteger("98765432198765432198765432198765432198765");
 x7875.uid = uid;
 x7875.gid = gid;
 int uidSize = uid.toByteArray().length;
 int gidSize = gid.toByteArray().length;
 ZipShort actualLength = x7875.getLocalFileDataLength();
 int expectedLength = (short) (3 + uidSize + gidSize);
 Assert.assertEquals(expectedLength, actualLength.getNumber().intValue());
 }

 @Test
 public void testGetCentralDirectoryLength() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 ZipShort actualLength = x7875.getCentralDirectoryLength();
 Assert.assertEquals(new ZipShort(0), actualLength);
 }

 @Test
 public void testGetLocalFileDataData() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 BigInteger uid = new BigInteger("12345678912345678912345678912345678912345");
 BigInteger gid = new BigInteger("98765432198765432198765432198765432198765");
 x7875.uid = uid;
 x7875.gid = gid;
 byte[] actualData = x7875.getLocalFileDataData();
 byte[] expectedData = new byte[3 + uid.toByteArray().length + gid.toByteArray().length];
 for (int i = 0; i < 3; i++) {
 expectedData[i] = (byte) (i + 1);
 }
 System.arraycopy(uid.toByteArray(), 0, expectedData, 3, uid.toByteArray().length);
 System.arraycopy(gid.toByteArray(), 0, expectedData, 3 + uid.toByteArray().length, gid.toByteArray().length);
 Assert.assertArrayEquals(expectedData, actualData);
 }

 @Test
 public void testParseFromLocalFileData() throws ZipException {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 byte[] data = new byte[9];
 data[0] = 1;
 data[1] = (byte) (uid.toByteArray().length);
 System.arraycopy(uid.toByteArray(), 0, data, 2, uid.toByteArray().length);
 data[2 + uid.toByteArray().length] = (byte) (gid.toByteArray().length);
 System.arraycopy(gid.toByteArray(), 0, data, 2 + uid.toByteArray().length, gid.toByteArray().length);
 x7875.parseFromLocalFileData(data, 0, data.length);
 BigInteger actualUid = x7875.uid;
 BigInteger actualGid = x7875.gid;
 Assert.assertEquals(uid, actualUid);
 Assert.assertEquals(gid, actualGid);
 }
}