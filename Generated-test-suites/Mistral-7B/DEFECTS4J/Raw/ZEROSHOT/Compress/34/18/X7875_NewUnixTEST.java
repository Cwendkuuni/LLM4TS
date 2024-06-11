import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {

 @Test
 public void testNewInstance() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 assertNotNull(x7875NewUnix);
 }

 @Test
 public void testGetHeaderId() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 ZipShort expectedHeaderId = new ZipShort(0x7875);
 ZipShort actualHeaderId = x7875NewUnix.getHeaderId();
 assertEquals(expectedHeaderId, actualHeaderId);
 }

 @Test
 public void testGetUid() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 long expectedUid = 1001L;
 x7875NewUnix.setUID(expectedUid);
 long actualUid = x7875NewUnix.getUID();
 assertEquals(expectedUid, actualUid);
 }

 @Test
 public void testSetUid() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 long uid = 1001L;
 x7875NewUnix.setUID(uid);
 BigInteger actualUid = x7875NewUnix.uid;
 BigInteger expectedUid = new BigInteger(Long.toString(uid, 10));
 assertEquals(expectedUid, actualUid);
 }

 @Test
 public void testGetGid() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 long expectedGid = 1002L;
 x7875NewUnix.setGID(expectedGid);
 long actualGid = x7875NewUnix.getGID();
 assertEquals(expectedGid, actualGid);
 }

 @Test
 public void testSetGid() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 long gid = 1002L;
 x7875NewUnix.setGID(gid);
 BigInteger actualGid = x7875NewUnix.gid;
 BigInteger expectedGid = new BigInteger(Long.toString(gid, 10));
 assertEquals(expectedGid, actualGid);
 }

 @Test
 public void testGetLocalFileDataLength() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 int expectedVersion = 1;
 BigInteger uid = new BigInteger("1001");
 BigInteger gid = new BigInteger("1002");
 int uidSize = uid.toByteArray().length;
 int gidSize = gid.toByteArray().length;
 int expectedLength = 3 + uidSize + gidSize;
 ZipShort actualLength = x7875NewUnix.getLocalFileDataLength();
 assertEquals(expectedLength, actualLength.getValue().intValue());
 }

 @Test
 public void testGetCentralDirectoryLength() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 int expectedLength = 0;
 ZipShort actualLength = x7875NewUnix.getCentralDirectoryLength();
 assertEquals(expectedLength, actualLength.getValue().intValue());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetCentralDirectoryData() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 byte[] actualData = x7875NewUnix.getCentralDirectoryData();
 fail("getCentralDirectoryData should not return any data");
 }

 @Test
 public void testParseFromLocalFileData() throws ZipException {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 byte[] data = { 0x58, 0x75, 0x01, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02, 0x01, 0x00, 0x00, 0x00, 0x00 };
 int offset = 0;
 int length = data.length;
 x7875NewUnix.parseFromLocalFileData(data, offset, length);
 int expectedVersion = 1;
 BigInteger expectedUid = new BigInteger("1001");
 BigInteger expectedGid = new BigInteger("1002");
 int uidSize = 3;
 int gidSize = 3;
 assertEquals(expectedVersion, x7875NewUnix.version);
 assertEquals(expectedUid, x7875NewUnix.uid);
 assertEquals(expectedGid, x7875NewUnix.gid);
 assertEquals(uidSize, x7875NewUnix.getLocalFileDataLength().getValue().intValue());
 assertEquals(gidSize, x7875NewUnix.getLocalFileDataLength().getValue().intValue());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testParseFromCentralDirectoryData() throws ZipException {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 byte[] buffer = new byte[0];
 int offset = 0;
 int length = buffer.length;
 x7875NewUnix.parseFromCentralDirectoryData(buffer, offset, length);
 fail("parseFromCentralDirectoryData should not be called");
 }

 @Test
 public void testReset() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.reset();
 assertEquals(1, x7875NewUnix.version);
 assertEquals(BigInteger.ONE, x7875NewUnix.uid);
 assertEquals(BigInteger.ONE, x7875NewUnix.gid);
 }

 @Test
 public void testToString() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.version = 2;
 BigInteger uid = new BigInteger("1003");
 BigInteger gid = new BigInteger("1004");
 x7875NewUnix.uid = uid;
 x7875NewUnix.gid = gid;
 String expectedString = "0x7875 Zip Extra Field: UID=1003 GID=1004";
 String actualString = x7875NewUnix.toString();
 assertEquals(expectedString, actualString);
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 X7875_NewUnix originalX7875NewUnix = new X7875_NewUnix();
 originalX7875NewUnix.version = 3;
 BigInteger uid = new BigInteger("1005");
 BigInteger gid = new BigInteger("1006");
 originalX7875NewUnix.uid = uid;
 originalX7875NewUnix.gid = gid;
 X7875_NewUnix clonedX7875NewUnix = (X7875_NewUnix) originalX7875NewUnix.clone();
 assertEquals(originalX7875NewUnix.version, clonedX7875NewUnix.version);
 assertEquals(originalX7875NewUnix.uid, clonedX7875NewUnix.uid);
 assertEquals(originalX7875NewUnix.gid, clonedX7875NewUnix.gid);
 }

 @Test
 public void testEquals() {
 X7875_NewUnix x7875NewUnix1 = new X7875_NewUnix();
 X7875_NewUnix x7875NewUnix2 = new X7875_NewUnix();
 assertFalse(x7875NewUnix1.equals(x7875NewUnix2));
 X7875_NewUnix x7875NewUnix3 = new X7875_NewUnix();
 x7875NewUnix3.version = x7875NewUnix1.version;
 x7875NewUnix3.uid = x7875NewUnix1.uid;
 x7875NewUnix3.gid = x7875NewUnix1.gid;
 assertTrue(x7875NewUnix1.equals(x7875NewUnix3));
 }

 @Test
 public void testHashCode() {
 X7875_NewUnix x7875NewUnix1 = new X7875_NewUnix();
 X7875_NewUnix x7875NewUnix2 = new X7875_NewUnix();
 assertNotEquals(x7875NewUnix1.hashCode(), x7875NewUnix2.hashCode());
 X7875_NewUnix x7875NewUnix3 = new X7875_NewUnix();
 x7875NewUnix3.version = x7875NewUnix1.version;
 x7875NewUnix3.uid = x7875NewUnix1.uid;
 x7875NewUnix3.gid = x7875NewUnix1.gid;
 assertEquals(x7875NewUnix1.hashCode(), x7875NewUnix3.hashCode());
 }
}