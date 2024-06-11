import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {

 private X7875_NewUnix getDefaultInstance() {
 return new X7875_NewUnix();
 }

 @Test
 public void testGetHeaderId() {
 X7875_NewUnix x7875 = getDefaultInstance();
 ZipShort expected = new ZipShort(0x7875);
 ZipShort actual = x7875.getHeaderId();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetUID() {
 X7875_NewUnix x7875 = getDefaultInstance();
 long expected = 1001;
 x7875.setUID(expected);
 long actual = x7875.getUID();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetUID() {
 X7875_NewUnix x7875 = getDefaultInstance();
 long uid = 1001;
 x7875.setUID(uid);
 BigInteger actualUid = x7875.uid;
 BigInteger expectedUid = BigInteger.valueOf(uid);
 assertTrue(actualUid.equals(expectedUid));
 }

 @Test
 public void testGetGID() {
 X7875_NewUnix x7875 = getDefaultInstance();
 long expected = 1002;
 x7875.setGID(expected);
 long actual = x7875.getGID();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetGID() {
 X7875_NewUnix x7875 = getDefaultInstance();
 long gid = 1002;
 x7875.setGID(gid);
 BigInteger actualGid = x7875.gid;
 BigInteger expectedGid = BigInteger.valueOf(gid);
 assertTrue(actualGid.equals(expectedGid));
 }

 @Test
 public void testGetLocalFileDataLength() {
 X7875_NewUnix x7875 = getDefaultInstance();
 int uidSize = 3;
 int gidSize = 3;
 x7875.uid = new BigInteger(1, new byte[]{(byte)0x01, (byte)0x02, (byte)0x03});
 x7875.gid = new BigInteger(1, new byte[]{(byte)0x04, (byte)0x05, (byte)0x06});
 ZipShort expected = new ZipShort(uidSize + gidSize + 1); // version=1
 ZipShort actual = x7875.getLocalFileDataLength();
 assertEquals(expected, actual);
 }

 @Test
 public void testParseFromLocalFileData() throws ZipException {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 byte[] data = {0x01, 0x01, (byte)0x01, 0x02, 0x02, 0x02, 0x03, 0x03, 0x03};
 x7875.parseFromLocalFileData(data, 0, data.length);
 assertEquals(new BigInteger("10001"), x7875.uid);
 assertEquals(new BigInteger("10002"), x7875.gid);
 assertEquals(1, x7875.version);
 }

 @Test(expected = ZipException.class)
 public void testParseFromLocalFileDataWithInvalidData() throws ZipException {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 byte[] data = {0x01, 0x01, 0x01, 0x02, (byte)0xFF, 0x02, 0x03, 0x03, 0x03};
 x7875.parseFromLocalFileData(data, 0, data.length);
 fail("Exception expected but not thrown");
 }

}