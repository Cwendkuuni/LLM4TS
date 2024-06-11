import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

public class X7875_NewUnixTest {

 // Test constructor
 @Test
 public void testX7875_NewUnixConstructor() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 Assert.assertEquals(X7875_NewUnix.HEADER_ID, x7875NewUnix.getHeaderId());
 Assert.assertEquals(new Long(1), x7875NewUnix.getVersion());
 Assert.assertEquals(new Long(0), x7875NewUnix.getUID());
 Assert.assertEquals(new Long(0), x7875NewUnix.getGID());
 }

 // Test getHeaderId()
 @Test
 public void testGetHeaderId() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 Assert.assertEquals(X7875_NewUnix.HEADER_ID, x7875NewUnix.getHeaderId());
 }

 // Test getUID()
 @Test
 public void testGetUID() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setUID(12345L);
 Assert.assertEquals(new Long(12345), x7875NewUnix.getUID());
 }

 // Test getGID()
 @Test
 public void testGetGID() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setGID(54321L);
 Assert.assertEquals(new Long(54321), x7875NewUnix.getGID());
 }

 // Test getLocalFileDataLength()
 @Test
 public void testGetLocalFileDataLength() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 byte[] uidBytes = new byte[]{(byte) 0x1, (byte) 0x2, (byte) 0x3, (byte) 0x4};
 byte[] gidBytes = new byte[]{(byte) 0x5, (byte) 0x6, (byte) 0x7, (byte) 0x8};
 x7875NewUnix.setUID(ZipUtil.bigToLong(ZipUtil.bigIntegerFromBytes(uidBytes)));
 x7875NewUnix.setGID(ZipUtil.bigToLong(ZipUtil.bigIntegerFromBytes(gidBytes)));
 ZipShort length = x7875NewUnix.getLocalFileDataLength();
 Assert.assertEquals(new ZipShort((short) (3 + uidBytes.length + gidBytes.length)), length);
 }

 // Test getCentralDirectoryLength()
 @Test
 public void testGetCentralDirectoryLength() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 Assert.assertEquals(x7875NewUnix.getLocalFileDataLength(), x7875NewUnix.getCentralDirectoryLength());
 }

 // Test getLocalFileDataData()
 @Test
 public void testGetLocalFileDataData() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 byte[] uidBytes = new byte[]{(byte) 0x1, (byte) 0x2, (byte) 0x3, (byte) 0x4};
 byte[] gidBytes = new byte[]{(byte) 0x5, (byte) 0x6, (byte) 0x7, (byte) 0x8};
 x7875NewUnix.setUID(ZipUtil.bigToLong(ZipUtil.bigIntegerFromBytes(uidBytes)));
 x7875NewUnix.setGID(ZipUtil.bigToLong(ZipUtil.bigIntegerFromBytes(gidBytes)));
 byte[] expectedData = new byte[3 + uidBytes.length + gidBytes.length];
 expectedData[0] = unsignedIntToSignedByte(x7875NewUnix.getVersion());
 for (int i = 0; i < uidBytes.length; i++) {
 expectedData[1 + i] = unsignedIntToSignedByte(uidBytes.length);
 expectedData[1 + i + uidBytes.length] = uidBytes[i];
 }
 for (int i = 0; i < gidBytes.length; i++) {
 expectedData[1 + uidBytes.length + i] = unsignedIntToSignedByte(gidBytes.length);
 expectedData[1 + uidBytes.length + gidBytes.length + i] = gidBytes[i];
 }
 byte[] actualData = x7875NewUnix.getLocalFileDataData();
 Assert.assertArrayEquals(expectedData, actualData);
 }

 // Test parseFromLocalFileData()
 @Test
 public void testParseFromLocalFileData() throws ZipException {
 byte[] testData = new byte[]{ 0x78, 0x75, 0x01, 0x03, 0x01, 0x00, 0x00, 0x00,
 0x02, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x00, 0x04, 0x00, 0x00, 0x00,
 0x05, 0x00, 0x00, 0x00, 0x06, 0x00, 0x00, 0x00, 0x07, 0x00, 0x00, 0x00,
 0x08 };
 X7875_NewUnix expectedX7875NewUnix = new X7875_NewUnix();
 expectedX7875NewUnix.setVersion((short) 0x01);
 byte[] uidBytes = new byte[]{ 0x01, 0x02, 0x03, 0x04 };
 expectedX7875NewUnix.setUID(ZipUtil.bigToLong(ZipUtil.bigIntegerFromBytes(uidBytes)));
 byte[] gidBytes = new byte[]{ 0x05, 0x06, 0x07, 0x08 };
 expectedX7875NewUnix.setGID(ZipUtil.bigToLong(ZipUtil.bigIntegerFromBytes(gidBytes)));
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.parseFromLocalFileData(testData, 0, testData.length);
 Assert.assertEquals(expectedX7875NewUnix.getVersion(), x7875NewUnix.getVersion());
 Assert.assertEquals(expectedX7875NewUnix.getUID(), x7875NewUnix.getUID());
 Assert.assertEquals(expectedX7875NewUnix.getGID(), x7875NewUnix.getGID());
 }

 // Test parseFromCentralDirectoryData()
 @Test
 public void testParseFromCentralDirectoryData() throws ZipException {
 // Not applicable, as this class does not store anything in the central directory
 }

 // Test reset()
 @Test
 public void testReset() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setUID(12345L);
 x7875NewUnix.setGID(54321L);
 x7875NewUnix.reset();
 Assert.assertEquals(new Long(1), x7875NewUnix.getVersion());
 Assert.assertEquals(new Long(0), x7875NewUnix.getUID());
 Assert.assertEquals(new Long(0), x7875NewUnix.getGID());
 }
}