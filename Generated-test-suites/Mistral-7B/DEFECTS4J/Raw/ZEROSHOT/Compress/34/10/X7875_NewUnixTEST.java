import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class X7875_NewUnixTest {

 @Test
 public void testNewX7875_NewUnixInstance() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 Assertions.assertNotNull(x7875NewUnix);
 }

 @Test
 public void testGetHeaderId() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 ZipShort expectedHeaderId = new ZipShort(0x7875);
 ZipShort actualHeaderId = x7875NewUnix.getHeaderId();
 Assertions.assertEquals(expectedHeaderId, actualHeaderId);
 }

 @Test
 public void testGetUid() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 long expectedUid = 0;
 long actualUid = x7875NewUnix.getUID();
 Assertions.assertEquals(expectedUid, actualUid);
 }

 @Test
 public void testSetUid() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 long expectedUid = 1234567890L;
 x7875NewUnix.setUID(expectedUid);
 long actualUid = x7875NewUnix.getUID();
 Assertions.assertEquals(expectedUid, actualUid);
 }

 @Test
 public void testGetGid() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 long expectedGid = 0;
 long actualGid = x7875NewUnix.getGID();
 Assertions.assertEquals(expectedGid, actualGid);
 }

 @Test
 public void testSetGid() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 long expectedGid = 9876543210L;
 x7875NewUnix.setGID(expectedGid);
 long actualGid = x7875NewUnix.getGID();
 Assertions.assertEquals(expectedGid, actualGid);
 }

 @Test
 public void testGetLocalFileDataLength() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 int expectedLength = 7;
 int actualLength = x7875NewUnix.getLocalFileDataLength().getValue();
 Assertions.assertEquals(expectedLength, actualLength);
 }

 @Test
 public void testGetCentralDirectoryLength() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 int expectedLength = 7;
 int actualLength = x7875NewUnix.getCentralDirectoryLength().getValue();
 Assertions.assertEquals(expectedLength, actualLength);
 }

 @Test
 public void testGetLocalFileDataData() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setUID(1234567890L);
 x7875NewUnix.setGID(9876543210L);
 byte[] expectedData = new byte[] { 0x01, 0x01, 0x00, 0x78, 0x54, 0x32, 0x10, 0x01, 0x01, 0x00, 0x78, 0x75, 0x69, 0x6e, 0x74, 0x65, 0x72, 0x32, 0x31, 0x30, 0x30, 0x00, 0x01, 0x01, 0x00, 0x78, 0x75, 0x69, 0x6e, 0x74, 0x65, 0x72, 0x31, 0x30, 0x31, 0x30, 0x00, 0x03, 0x00 };
 byte[] actualData = x7875NewUnix.getLocalFileDataData();
 Assertions.assertArrayEquals(expectedData, actualData);
 }

 @Test
 public void testParseFromLocalFileData() throws ZipException {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 byte[] data = new byte[] { 0x01, 0x01, (byte) 0x01, 0x78, 0x54, 0x32, 0x10, 0x01, 0x01, 0x01, 0x78, 0x75, 0x69, 0x6e, 0x74, 0x65, 0x72, 0x32, 0x31, 0x30, 0x30, 0x00, 0x01, 0x01, 0x01, 0x78, 0x75, 0x69, 0x6e, 0x74, 0x65, 0x72, 0x31, 0x30, 0x31, 0x30, 0x00, 0x03, 0x00 };
 x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
 int expectedVersion = 1;
 long expectedUid = 1234567890L;
 long expectedGid = 9876543210L;
 int actualVersion = x7875NewUnix.getVersion();
 long actualUid = x7875NewUnix.getUID();
 long actualGid = x7875NewUnix.getGID();
 Assertions.assertEquals(expectedVersion, actualVersion);
 Assertions.assertEquals(expectedUid, actualUid);
 Assertions.assertEquals(expectedGid, actualGid);
 }

 @Test
 public void testParseFromCentralDirectoryData() throws ZipException {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 byte[] data = new byte[0];
 x7875NewUnix.parseFromCentralDirectoryData(data, 0, data.length);
 Assertions.assertThrows(ZipException.class, () -> x7875NewUnix.getUID());
 Assertions.assertThrows(ZipException.class, () -> x7875NewUnix.getGID());
 }
}