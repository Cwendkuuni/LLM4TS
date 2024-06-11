import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {

 private X7875_NewUnix createX7875_NewUnixInstance() {
 return new X7875_NewUnix();
 }

 @Test
 public void testGetHeaderId() {
 X7875_NewUnix x7875NewUnix = createX7875_NewUnixInstance();
 ZipShort expectedHeaderId = new ZipShort(0x7875);
 ZipShort actualHeaderId = x7875NewUnix.getHeaderId();
 assertEquals(expectedHeaderId, actualHeaderId);
 }

 @Test
 public void testGetUID() {
 X7875_NewUnix x7875NewUnix = createX7875_NewUnixInstance();
 long expectedUID = 1001;
 x7875NewUnix.setUID(expectedUID);
 long actualUID = x7875NewUnix.getUID();
 assertEquals(expectedUID, actualUID);
 }

 @Test
 public void testSetUID() {
 X7875_NewUnix x7875NewUnix = createX7875_NewUnixInstance();
 long expectedUID = 1001;
 x7875NewUnix.setUID(expectedUID);
 BigInteger actualUid = x7875NewUnix.uid;
 BigInteger expectedUid = new BigInteger(Long.toUnsignedLong(expectedUID));
 assertEquals(expectedUid, actualUid);
 }

 @Test
 public void testGetGID() {
 X7875_NewUnix x7875NewUnix = createX7875_NewUnixInstance();
 long expectedGID = 1002;
 x7875NewUnix.setGID(expectedGID);
 long actualGID = x7875NewUnix.getGID();
 assertEquals(expectedGID, actualGID);
 }

 @Test
 public void testSetGID() {
 X7875_NewUnix x7875NewUnix = createX7875_NewUnixInstance();
 long expectedGID = 1002;
 x7875NewUnix.setGID(expectedGID);
 BigInteger actualGid = x7875NewUnix.gid;
 BigInteger expectedGid = new BigInteger(Long.toUnsignedLong(expectedGID));
 assertEquals(expectedGid, actualGid);
 }

 @Test
 public void testGetLocalFileDataLength() {
 X7875_NewUnix x7875NewUnix = createX7875_NewUnixInstance();
 x7875NewUnix.setUID(1001);
 x7875NewUnix.setGID(1002);
 int expectedLength = 3 + (1 + 1); // version + uid size + gid size
 ZipShort actualLength = x7875NewUnix.getLocalFileDataLength();
 assertEquals(expectedLength, actualLength.getValue());
 }

 @Test
 public void testGetCentralDirectoryLength() {
 X7875_NewUnix x7875NewUnix = createX7875_NewUnixInstance();
 x7875NewUnix.setUID(1001);
 x7875NewUnix.setGID(1002);
 ZipShort expectedLength = new ZipShort(3);
 ZipShort actualLength = x7875NewUnix.getCentralDirectoryLength();
 assertEquals(expectedLength, actualLength);
 }

 @Test
 public void testGetLocalFileDataData() throws ZipException {
 X7875_NewUnix x7875NewUnix = createX7875_NewUnixInstance();
 x7875NewUnix.setUID(1001);
 x7875NewUnix.setGID(1002);
 byte[] expectedData = new byte[]{(byte) 0x01, (byte) 0x01, 0x00, 0x00, (byte) 0x01, 0x00, 0x00, 0x00, (byte) 0x01, 0x00, 0x00, 0x00, (byte) 0x02, 0x00, 0x00, 0x00};
 byte[] actualData = x7875NewUnix.getLocalFileDataData();
 assertArrayEquals(expectedData, actualData);
 }

 @Test(expected = ZipException.class)
 public void testParseFromCentralDirectoryData() throws ZipException {
 X7875_NewUnix x7875NewUnix = createX7875_NewUnixInstance();
 byte[] buffer = new byte[0];
 int offset = 0;
 int length = 0;
 x7875NewUnix.parseFromCentralDirectoryData(buffer, offset, length);
 }

 @Test
 public void testReset() {
 X7875_NewUnix x7875NewUnix = createX7875_NewUnixInstance();
 x7875NewUnix.setUID(1001);
 x7875NewUnix.setGID(1002);
 x7875NewUnix.reset();
 assertNull(x7875NewUnix.uid);
 assertNull(x7875NewUnix.gid);
 }

}