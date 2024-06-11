import org.apache.commons.compress.archivers.zip.*;
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
 ZipShort headerId = x7875NewUnix.getHeaderId();
 assertEquals(0x7875, headerId.getValue().intValue());
 }

 @Test
 public void testGetUid() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 long uid = 12345;
 x7875NewUnix.setUID(uid);
 long actualUid = x7875NewUnix.getUID();
 assertEquals(uid, actualUid);
 }

 @Test
 public void testSetUid() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 long uid = 12345;
 x7875NewUnix.setUID(uid);
 BigInteger expectedUid = new BigInteger(Long.toUnsignedLong(uid));
 BigInteger actualUid = x7875NewUnix.uid;
 assertEquals(expectedUid, actualUid);
 }

 @Test
 public void testGetGid() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 long gid = 23456;
 x7875NewUnix.setGID(gid);
 long actualGid = x7875NewUnix.getGID();
 assertEquals(gid, actualGid);
 }

 @Test
 public void testSetGid() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 long gid = 23456;
 x7875NewUnix.setGID(gid);
 BigInteger expectedGid = new BigInteger(Long.toUnsignedLong(gid));
 BigInteger actualGid = x7875NewUnix.gid;
 assertEquals(expectedGid, actualGid);
 }

 @Test
 public void testGetLocalFileDataLength() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 int uidSize = 3;
 int gidSize = 3;
 x7875NewUnix.version = 1;
 x7875NewUnix.uid = new BigInteger("123".getBytes());
 x7875NewUnix.gid = new BigInteger("456".getBytes());
 ZipShort expectedLength = new ZipShort(3 + uidSize + gidSize);
 ZipShort actualLength = x7875NewUnix.getLocalFileDataLength();
 assertEquals(expectedLength.getValue().intValue(), actualLength.getValue().intValue());
 }

 @Test
 public void testGetCentralDirectoryLength() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 ZipShort expectedLength = x7875NewUnix.getLocalFileDataLength();
 ZipShort actualLength = x7875NewUnix.getCentralDirectoryLength();
 assertEquals(expectedLength.getValue().intValue(), actualLength.getValue().intValue());
 }

 @Test
 public void testGetLocalFileDataData() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 byte[] expectedData = new byte[]{(byte) 0x01, 0x01, 0x00, 0x31, 0x32, 0x33, 0x01, 0x01, 0x00, 0x34, 0x35, 0x36};
 byte[] actualData = x7875NewUnix.getLocalFileDataData();
 assertArrayEquals(expectedData, actualData);
 }

 @Test
 public void testGetCentralDirectoryData() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 byte[] actualData = x7875NewUnix.getCentralDirectoryData();
 assertTrue(actualData.length == 0);
 }

 @Test
 public void testParseFromLocalFileData() throws ZipException {
 byte[] data = new byte[]{(byte) 0x01, 0x01, (byte) 0x31, 0x32, 0x33, (byte) 0x01, 0x01, 0x00, 0x34, 0x35, 0x36};
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
 assertEquals(x7875NewUnix.version, 1);
 BigInteger expectedUid = new BigInteger("123");
 BigInteger actualUid = x7875NewUnix.uid;
 assertEquals(expectedUid, actualUid);
 BigInteger expectedGid = new BigInteger("456");
 BigInteger actualGid = x7875NewUnix.gid;
 assertEquals(expectedGid, actualGid);
 }

 @Test(expected = ZipException.class)
 public void testParseFromLocalFileDataInvalidData() throws ZipException {
 byte[] data = new byte[]{(byte) 0x01, 0x01, (byte) 0x31, 0x32, 0x33, (byte) 0x01, 0x01, 0x00, 0x34, 0x35, 0x36, 0x01};
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
 fail("Exception expected");
 }

 @Test
 public void testReset() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.reset();
 assertEquals(x7875NewUnix.version, 1);
 BigInteger expectedUid = new BigInteger("1000");
 BigInteger actualUid = x7875NewUnix.uid;
 assertEquals(expectedUid, actualUid);
 BigInteger expectedGid = new BigInteger("1000");
 BigInteger actualGid = x7875NewUnix.gid;
 assertEquals(expectedGid, actualGid);
 }
}