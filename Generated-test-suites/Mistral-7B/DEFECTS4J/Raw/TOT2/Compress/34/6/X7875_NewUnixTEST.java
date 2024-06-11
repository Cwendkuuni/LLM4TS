import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {

 private static final ZipShort HEADER_ID = new ZipShort(0x7875);

 @Test
 public void testNewInstance() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 assertEquals(1, x7875.getVersion());
 assertNull(x7875.getUID());
 assertNull(x7875.getGID());
 }

 @Test
 public void testSetAndGetUid() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.setUID(1234567890L);
 assertEquals(1234567890L, x7875.getUID());
 }

 @Test
 public void testSetAndGetGid() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.setGID(234567890123L);
 assertEquals(234567890123L, x7875.getGID());
 }

 @Test
 public void testGetLocalFileDataLength() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.setUID(1234567890L);
 x7875.setGID(234567890123L);
 ZipShort expectedLength = new ZipShort(5 + 1 + 1);
 assertEquals(expectedLength, x7875.getLocalFileDataLength());
 }

 @Test
 public void testGetCentralDirectoryLength() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.setUID(1234567890L);
 x7875.setGID(234567890123L);
 ZipShort expectedLength = new ZipShort(0);
 assertEquals(expectedLength, x7875.getCentralDirectoryLength());
 }

 @Test
 public void testGetLocalFileDataData() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.setUID(1234567890L);
 x7875.setGID(234567890123L);
 byte[] expectedData = new byte[5 + 1 + 1 + 8 + 8];
 // UID = 1234567890
 byte[] uidBytes = new byte[8];
 unsignedIntToSignedByte((short) 8, uidBytes, 0);
 unsignedIntToSignedByte((short) 0, uidBytes, 1);
 for (int i = 2; i < uidBytes.length; i++) {
 unsignedIntToSignedByte((short) 0, uidBytes, i);
 }
 System.arraycopy(reverse(uidBytes), 0, expectedData, 0, 8);
 // GID = 234567890123
 byte[] gidBytes = new byte[8];
 unsignedIntToSignedByte((short) 8, gidBytes, 0);
 unsignedIntToSignedByte((short) 0, gidBytes, 1);
 for (int i = 2; i < gidBytes.length; i++) {
 unsignedIntToSignedByte((short) 0, gidBytes, i);
 }
 System.arraycopy(reverse(gidBytes), 0, expectedData, 8, 8);
 byte[] actualData = x7875.getLocalFileDataData();
 assertArrayEquals(expectedData, actualData);
 }

 @Test(expected = ZipException.class)
 public void testParseFromLocalFileDataWithInvalidData() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 byte[] data = new byte[1];
 x7875.parseFromLocalFileData(data, 0, data.length);
 fail("Should throw exception");
 }

 @Test(expected = ZipException.class)
 public void testParseFromLocalFileDataWithMissingVersionByte() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 byte[] data = new byte[2];
 data[0] = (byte) 0x78;
 data[1] = (byte) 0x75;
 x7875.parseFromLocalFileData(data, 0, data.length);
 fail("Should throw exception");
 }

 @Test(expected = ZipException.class)
 public void testParseFromLocalFileDataWithInvalidUidSize() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 byte[] data = new byte[3];
 data[0] = (byte) 0x78;
 data[1] = (byte) 0x75;
 data[2] = (byte) 0x01;
 x7875.parseFromLocalFileData(data, 0, data.length);
 fail("Should throw exception");
 }

 @Test(expected = ZipException.class)
 public void testParseFromLocalFileDataWithInvalidGidSize() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 byte[] data = new byte[3];
 data[0] = (byte) 0x78;
 data[1] = (byte) 0x75;
 data[2] = (byte) 0x01;
 data[3] = (byte) 0x01;
 x7875.parseFromLocalFileData(data, 0, data.length + 1);
 fail("Should throw exception");
 }

 @Test
 public void testParseFromLocalFileDataWithValidData() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 byte[] data = new byte[5 + 1 + 1 + 8 + 8];
 // UID = 1234567890
 byte[] uidBytes = new byte[8];
 unsignedIntToSignedByte((short) 8, uidBytes, 0);
 unsignedIntToSignedByte((short) 0, uidBytes, 1);
 for (int i = 2; i < uidBytes.length; i++) {
 unsignedIntToSignedByte((short) 0, uidBytes, i);
 }
 System.arraycopy(reverse(uidBytes), 0, data, 0, 8);
 // GID = 234567890123
 byte[] gidBytes = new byte[8];
 unsignedIntToSignedByte((short) 8, gidBytes, 0);
 unsignedIntToSignedByte((short) 0, gidBytes, 1);
 for (int i = 2; i < gidBytes.length; i++) {
 unsignedIntToSignedByte((short) 0, gidBytes, i);
 }
 System.arraycopy(reverse(gidBytes), 0, data, 8, 8);
 x7875.parseFromLocalFileData(data, 0, data.length);
 assertEquals(1, x7875.getVersion());
 BigInteger uid = x7875.getUID();
 assertEquals(1234567890L, uid.longValue());
 BigInteger gid = x7875.getGID();
 assertEquals(234567890123L, gid.longValue());
 }
}