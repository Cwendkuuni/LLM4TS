import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {

 @Test
 public void testNewX7875_NewUnix() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 assertNull(x7875NewUnix.getUID());
 assertNull(x7875NewUnix.getGID());
 assertEquals(1, x7875NewUnix.getVersion());
 }

 @Test
 public void testSetUIDAndGetUID() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 long uid = 2456789; // some random UID
 x7875NewUnix.setUID(uid);
 long expectedUid = uid;
 long actualUid = x7875NewUnix.getUID();
 assertEquals("UID should be set and retrieved correctly.", expectedUid, actualUid);
 }

 @Test
 public void testSetGIDAndGetGID() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 long gid = 4212947; // some random GID
 x7875NewUnix.setGID(gid);
 long expectedGid = gid;
 long actualGid = x7875NewUnix.getGID();
 assertEquals("GID should be set and retrieved correctly.", expectedGid, actualGid);
 }

 @Test
 public void testGetLocalFileDataLength() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setUID(12345678912345L);
 x7875NewUnix.setGID(23456789023456L);
 int uidSize = trimLeadingZeroesForceMinLength(x7875NewUnix.uid.toByteArray()).length;
 int gidSize = trimLeadingZeroesForceMinLength(x7875NewUnix.gid.toByteArray()).length;
 ZipShort expectedLength = new ZipShort(3 + uidSize + gidSize);
 ZipShort actualLength = x7875NewUnix.getLocalFileDataLength();
 assertEquals("Local file data length should be set and retrieved correctly.", expectedLength.getValue(), actualLength.getValue());
 }

 @Test
 public void testGetCentralDirectoryLength() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setUID(12345678912345L);
 x7875NewUnix.setGID(23456789023456L);
 ZipShort expectedLength = x7875NewUnix.getLocalFileDataLength();
 ZipShort actualLength = x7875NewUnix.getCentralDirectoryLength();
 assertEquals("Central directory length should be equal to local file data length.", expectedLength.getValue(), actualLength.getValue());
 }

 @Test
 public void testParseFromLocalFileData() throws ZipException {
 byte[] testData = new byte[]{(byte) 0x78, (byte) 0x75, (byte) 0x01, (byte) 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x2A, 0x9B, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x00, 0x00, 0x00, 0x00, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x00, 0x00, 0x00, 0x00};
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.parseFromLocalFileData(testData, 0, testData.length);
 long expectedUid = 21345678932132345321L;
 long expectedGid = 31234567893123456312L;
 int expectedVersion = 1;
 assertEquals("UID should be set and retrieved correctly.", expectedUid, x7875NewUnix.getUID());
 assertEquals("GID should be set and retrieved correctly.", expectedGid, x7875NewUnix.getGID());
 assertEquals("Version should be set and retrieved correctly.", expectedVersion, x7875NewUnix.getVersion());
 }

 @Test(expected = ZipException.class)
 public void testParseFromLocalFileDataWithInvalidData() throws ZipException {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 byte[] testData = new byte[]{(byte) 0x78, (byte) 0x75, (byte) 0x01, (byte) 0x01}; // invalid version
 x7875NewUnix.parseFromLocalFileData(testData, 0, testData.length);
 }

 @Test
 public void testToString() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setUID(12345678912345L);
 x7875NewUnix.setGID(23456789023456L);
 String expectedString = "0x7875 Zip Extra Field: UID=12345678912345 GID=23456789023456";
 String actualString = x7875NewUnix.toString();
 assertEquals("toString() should return correct string representation.", expectedString, actualString);
 }
}