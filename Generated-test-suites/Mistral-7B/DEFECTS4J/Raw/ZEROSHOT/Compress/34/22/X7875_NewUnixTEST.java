import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {

 X7875_NewUnix newUnix = new X7875_NewUnix();
 X7875_NewUnix clonedNewUnix = null;

 @Test
 public void testGetHeaderId() {
 ZipShort expectedHeaderId = new ZipShort(0x7875);
 ZipShort actualHeaderId = newUnix.getHeaderId();
 assertEquals("Unexpected header id", expectedHeaderId, actualHeaderId);
 }

 @Test
 public void testGetUID() {
 long expectedUID = 0; // Root user UID
 newUnix.setUID(expectedUID);
 long actualUID = newUnix.getUID();
 assertEquals("Unexpected UID", expectedUID, actualUID);
 }

 @Test
 public void testSetUID() {
 long newUID = 1001; // User ID for test user 'bin'
 newUnix.setUID(newUID);
 long actualUID = newUnix.getUID();
 assertEquals("Unexpected UID after setUID", newUID, actualUID);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetUIDNegativeValue() {
 newUnix.setUID(-1); // Negative value should throw an exception
 }

 @Test
 public void testGetGID() {
 long expectedGID = 0; // Root group GID
 newUnix.setGID(expectedGID);
 long actualGID = newUnix.getGID();
 assertEquals("Unexpected GID", expectedGID, actualGID);
 }

 @Test
 public void testSetGID() {
 long newGID = 1001; // Group ID for test group 'bin'
 newUnix.setGID(newGID);
 long actualGID = newUnix.getGID();
 assertEquals("Unexpected GID after setGID", newGID, actualGID);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetGIDNegativeValue() {
 newUnix.setGID(-1); // Negative value should throw an exception
 }

 @Test
 public void testGetLocalFileDataLength() throws ZipException {
 int expectedLength = 5;
 int uidSize = 2;
 int gidSize = 2;
 int versionSize = 1;
 int totalSize = versionSize + uidSize + gidSize;
 ZipShort length = newUnix.getLocalFileDataLength();
 assertEquals("Unexpected local file data length", newShort((short) (totalSize)), length.getValue());
 }

 @Test
 public void testGetCentralDirectoryLength() throws ZipException {
 int expectedLength = 0;
 ZipShort length = newUnix.getCentralDirectoryLength();
 assertEquals("Unexpected central directory length", newShort((short) (expectedLength)), length.getValue());
 }

 @Test
 public void testGetLocalFileDataData() throws ZipException {
 byte[] expectedData = new byte[5];
 int uidSize = 2;
 int gidSize = 2;
 int version = 1;
 int pos = 0;
 expectedData[pos++] = (byte) unsignedIntToSignedByte(version);
 expectedData[pos++] = (byte) unsignedIntToSignedByte(uidSize);
 System.arraycopy(uid.toByteArray(), 0, expectedData, pos, uidSize);
 pos += uidSize;
 expectedData[pos++] = (byte) unsignedIntToSignedByte(gidSize);
 System.arraycopy(gid.toByteArray(), 0, expectedData, pos, gidSize);
 byte[] actualData = newUnix.getLocalFileDataData();
 assertArrayEquals("Unexpected local file data", expectedData, actualData);
 }

 @Test
 public void testGetCentralDirectoryData() throws ZipException {
 byte[] expectedData = new byte[0];
 byte[] actualData = newUnix.getCentralDirectoryData();
 assertArrayEquals("Unexpected central directory data", expectedData, actualData);
 }

 @Test
 public void testParseFromLocalFileData() throws ZipException {
 byte[] data = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09};
 int offset = 0;
 int length = data.length;
 newUnix.parseFromLocalFileData(data, offset, length);
 long expectedUID = new BigInteger(new byte[]{(byte) 0x05, (byte) 0x06}).longValue();
 long expectedGID = new BigInteger(new byte[]{(byte) 0x09}).longValue();
 assertEquals("Unexpected UID after parsing", expectedUID, newUnix.getUID());
 assertEquals("Unexpected GID after parsing", expectedGID, newUnix.getGID());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFromLocalFileDataInvalidDataSize() throws ZipException {
 byte[] data = new byte[]{(byte) 0x01, (byte) 0x02};
 int offset = 0;
 int length = data.length;
 newUnix.parseFromLocalFileData(data, offset, length);
 fail("Unexpected exception not thrown");
 }

 @Test
 public void testParseFromLocalFileDataInvalidVersion() throws ZipException {
 byte[] data = new byte[]{(byte) 0x02, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09};
 int offset = 0;
 int length = data.length;
 try {
 newUnix.parseFromLocalFileData(data, offset, length);
 fail("Unexpected exception not thrown");
 } catch (ZipException e) {
 // Expected exception
 }
 }

 @Test
 public void testParseFromLocalFileDataInvalidUidSize() throws ZipException {
 byte[] data = new byte[]{(byte) 0x01, (byte) 0x01, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09};
 int offset = 0;
 int length = data.length;
 try {
 newUnix.parseFromLocalFileData(data, offset, length);
 fail("Unexpected exception not thrown");
 } catch (ZipException e) {
 // Expected exception
 }
 }

 @Test
 public void testParseFromLocalFileDataInvalidGidSize() throws ZipException {
 byte[] data = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x0a};
 int offset = 0;
 int length = data.length;
 try {
 newUnix.parseFromLocalFileData(data, offset, length);
 fail("Unexpected exception not thrown");
 } catch (ZipException e) {
 // Expected exception
 }
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 clonedNewUnix = (X7875_NewUnix) newUnix.clone();
 assertEquals("Unexpected cloned object", newUnix, clonedNewUnix);
 }

 @Test
 public void testEquals() {
 assertTrue("Unexpected equals result", newUnix.equals(newUnix));
 }

 @Test
 public void testEqualsDifferentObject() {
 X7875_NewUnix differentObject = new X7875_NewUnix();
 assertFalse("Unexpected equals result", newUnix.equals(differentObject));
 }

 @Test
 public void testHashCode() {
 int expectedHashCode = 1234567;
 int actualHashCode = newUnix.hashCode();
 assertEquals("Unexpected hash code", expectedHashCode, actualHashCode);
 }
}