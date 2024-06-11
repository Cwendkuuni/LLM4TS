import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

public class X7875_NewUnixTest {

 private static final byte[] UID = new byte[]{0, 0, 1, 2, 3, 4, 5, 6};
 private static final byte[] GID = new byte[]{7, 8, 9, 10, 11, 12, 13, 14};

 @Test
 public void testNewInstance() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 Assert.assertEquals(1, x7875NewUnix.getVersion());
 Assert.assertNull(x7875NewUnix.getUID());
 Assert.assertNull(x7875NewUnix.getGID());
 }

 @Test
 public void testSetGetUID() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setUID(1234567890L);
 Assert.assertEquals(1, x7875NewUnix.getVersion());
 Assert.assertEquals(1234567890L, x7875NewUnix.getUID());
 }

 @Test
 public void testSetGetGID() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setGID(9876543210L);
 Assert.assertEquals(1, x7875NewUnix.getVersion());
 Assert.assertEquals(9876543210L, x7875NewUnix.getGID());
 }

 @Test
 public void testGetLocalFileDataLength() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setUID(UID);
 x7875NewUnix.setGID(GID);
 ZipShort expectedLength = new ZipShort((short) (3 + UID.length + GID.length));
 Assert.assertEquals(expectedLength, x7875NewUnix.getLocalFileDataLength());
 }

 @Test
 public void testGetCentralDirectoryLength() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setUID(UID);
 x7875NewUnix.setGID(GID);
 Assert.assertEquals(x7875NewUnix.getLocalFileDataLength(), x7875NewUnix.getCentralDirectoryLength());
 }

 @Test
 public void testGetLocalFileDataData() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setUID(UID);
 x7875NewUnix.setGID(GID);
 byte[] expectedData = new byte[3 + UID.length + GID.length];
 int pos = 0;
 expectedData[pos++] = (byte) 0x01;
 expectedData[pos++] = (byte) UID.length;
 System.arraycopy(UID, 0, expectedData, pos, UID.length);
 pos += UID.length;
 expectedData[pos++] = (byte) GID.length;
 System.arraycopy(GID, 0, expectedData, pos, GID.length);
 Assert.assertArrayEquals(expectedData, x7875NewUnix.getLocalFileDataData());
 }

 @Test
 public void testParseFromLocalFileData() throws ZipException {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 byte[] data = new byte[3 + UID.length + GID.length];
 int pos = 0;
 data[pos++] = (byte) 0x01;
 data[pos++] = (byte) UID.length;
 System.arraycopy(UID, 0, data, pos, UID.length);
 pos += UID.length;
 data[pos++] = (byte) GID.length;
 System.arraycopy(GID, 0, data, pos, GID.length);
 x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
 Assert.assertEquals(1, x7875NewUnix.getVersion());
 Assert.assertArrayEquals(UID, x7875NewUnix.getUID().toByteArray());
 Assert.assertArrayEquals(GID, x7875NewUnix.getGID().toByteArray());
 }

 @Test(expected = ZipException.class)
 public void testParseFromLocalFileDataInvalidVersion() throws ZipException {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 byte[] data = new byte[]{0x00, 0x02, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
 x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
 }

 @Test(expected = ZipException.class)
 public void testParseFromLocalFileDataInvalidUIDLength() throws ZipException {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 byte[] data = new byte[]{0x01, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
 x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
 }

 @Test(expected = ZipException.class)
 public void testParseFromLocalFileDataInvalidGIDLength() throws ZipException {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 byte[] data = new byte[]{0x01, 0x02, 0x01, 0x02, 0x03, 0x04, 0x05, 0x07};
 x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
 }

 @Test
 public void testParseFromCentralDirectoryData() throws ZipException {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.parseFromCentralDirectoryData(new byte[0], 0, 0);
 Assert.assertNull(x7875NewUnix.getUID());
 Assert.assertNull(x7875NewUnix.getGID());
 }

 @Test
 public void testReset() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setUID(UID);
 x7875NewUnix.setGID(GID);
 x7875NewUnix.reset();
 Assert.assertEquals(1, x7875NewUnix.getVersion());
 Assert.assertNull(x7875NewUnix.getUID());
 Assert.assertNull(x7875NewUnix.getGID());
 }

 @Test
 public void testToString() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setUID(UID);
 x7875NewUnix.setGID(GID);
 String expectedToString = "0x7875 Zip Extra Field: UID=[1, 2, 3, 4, 5, 6] GID=[7, 8, 9, 10, 11, 12, 13, 14]";
 Assert.assertEquals(expectedToString, x7875NewUnix.toString());
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 X7875_NewUnix original = new X7875_NewUnix();
 original.setUID(UID);
 original.setGID(GID);
 X7875_NewUnix clone = (X7875_NewUnix) original.clone();
 Assert.assertEquals(original.getVersion(), clone.getVersion());
 Assert.assertArrayEquals(original.getUID().toByteArray(), clone.getUID().toByteArray());
 Assert.assertArrayEquals(original.getGID().toByteArray(), clone.getGID().toByteArray());
 }

 @Test
 public void testEquals() {
 X7875_NewUnix x7875NewUnix1 = new X7875_NewUnix();
 X7875_NewUnix x7875NewUnix2 = new X7875_NewUnix();
 X7875_NewUnix x7875NewUnix3 = new X7875_NewUnix();
 x7875NewUnix1.setUID(UID);
 x7875NewUnix1.setGID(GID);
 x7875NewUnix2.setUID(UID);
 x7875NewUnix2.setGID(GID);
 x7875NewUnix3.setUID(new byte[]{0, 1, 2, 3, 4, 5, 6, 7});
 x7875NewUnix3.setGID(new byte[]{8, 9, 10, 11, 12, 13, 14, 15});
 Assert.assertTrue(x7875NewUnix1.equals(x7875NewUnix2));
 Assert.assertFalse(x7875NewUnix1.equals(x7875NewUnix3));
 Assert.assertFalse(x7875NewUnix1.equals(null));
 }

 @Test
 public void testHashcode() {
 X7875_NewUnix x7875NewUnix1 = new X7875_NewUnix();
 X7875_NewUnix x7875NewUnix2 = new X7875_NewUnix();
 X7875_NewUnix x7875NewUnix3 = new X7875_NewUnix();
 x7875NewUnix1.setUID(UID);
 x7875NewUnix1.setGID(GID);
 x7875NewUnix2.setUID(UID);
 x7875NewUnix2.setGID(GID);
 x7875NewUnix3.setUID(new byte[]{0, 1, 2, 3, 4, 5, 6, 7});
 x7875NewUnix3.setGID(new byte[]{8, 9, 10, 11, 12, 13, 14, 15});
 Assert.assertEquals(x7875NewUnix1.hashCode(), x7875NewUnix2.hashCode());
 Assert.assertNotSame(x7875NewUnix1.hashCode(), x7875NewUnix3.hashCode());
 }
}