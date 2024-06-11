import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class X7875_NewUnixTest {

 @Test
 public void testConstructor() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 assertEquals(1, x7875.getVersion());
 assertEquals(ONE_THOUSAND, x7875.getUID());
 assertEquals(ONE_THOUSAND, x7875.getGID());
 }

 @Test
 public void testSetUIDAndGID() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 long uid = 123456789;
 long gid = 987654321;
 x7875.setUID(uid);
 x7875.setGID(gid);
 assertEquals(uid, x7875.getUID());
 assertEquals(gid, x7875.getGID());
 }

 @Test
 public void testGetUIDAndGID() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 long uid = 123456789;
 long gid = 987654321;
 x7875.setUID(uid);
 x7875.setGID(gid);
 assertEquals(uid, x7875.getUID());
 assertEquals(gid, x7875.getGID());
 }

 @Test
 public void testGetLocalFileDataLength() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 BigInteger uid = new BigInteger("1234567890123456789012345678901234567890");
 BigInteger gid = new BigInteger("9876543210987654321098765432109876543210");
 int uidSize = uid.toByteArray().length;
 int gidSize = gid.toByteArray().length;
 ZipShort expectedLength = new ZipShort((short) (3 + uidSize + gidSize));
 ZipShort actualLength = x7875.getLocalFileDataLength();
 assertEquals(expectedLength.getValue(), actualLength.getValue());
 }

 @Test
 public void testGetCentralDirectoryLength() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 BigInteger uid = new BigInteger("1234567890123456789012345678901234567890");
 BigInteger gid = new BigInteger("9876543210987654321098765432109876543210");
 int uidSize = uid.toByteArray().length;
 int gidSize = gid.toByteArray().length;
 ZipShort expectedLength = new ZipShort((short) (3 + uidSize + gidSize));
 ZipShort actualLength = x7875.getCentralDirectoryLength();
 assertEquals(expectedLength.getValue(), actualLength.getValue());
 }

 @Test
 public void testParseFromLocalFileData() throws ZipException {
 byte[] data = new byte[]{(byte) 0x78, (byte) 0x75, // Header ID
 (byte) 0x03, (byte) 0x01, (byte) 0x01, // version, uidSize, gidSize
 (byte) 0x41, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // uid
 (byte) 0x41, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // gid
 (byte) 0x00}; // End of data marker
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.parseFromLocalFileData(data, 0, data.length);
 assertEquals(1, x7875.getVersion());
 BigInteger uid = new BigInteger(1, x7875.getUidBytes());
 assertEquals(new BigInteger("1234567890123456789012345678901234567890"), uid);
 BigInteger gid = new BigInteger(1, x7875.getGidBytes());
 assertEquals(new BigInteger("9876543210987654321098765432109876543210"), gid);
 }

 @Test
 public void testParseFromCentralDirectoryData() throws ZipException {
 // This method doesn't do anything, since we don't store anything in the central directory
 }

 @Test
 public void testReset() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.reset();
 assertEquals(1, x7875.getVersion());
 assertEquals(ONE_THOUSAND, x7875.getUID());
 assertEquals(ONE_THOUSAND, x7875.getGID());
 }

 @Test
 public void testToString() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 BigInteger uid = new BigInteger("1234567890123456789012345678901234567890");
 BigInteger gid = new BigInteger("9876543210987654321098765432109876543210");
 x7875.setUID(uid);
 x7875.setGID(gid);
 String expectedString = "0x7875 Zip Extra Field: UID=" + uid + " GID=" + gid;
 assertEquals(expectedString, x7875.toString());
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 X7875_NewUnix original = new X7875_NewUnix();
 original.setUID(12345);
 original.setGID(67890);
 X7875_NewUnix clone = (X7875_NewUnix) original.clone();
 assertEquals(original.getUID(), clone.getUID());
 assertEquals(original.getGID(), clone.getGID());
 }

 @Test
 public void testEquals() {
 X7875_NewUnix x7875_1 = new X7875_NewUnix();
 X7875_NewUnix x7875_2 = new X7875_NewUnix();
 X7875_NewUnix x7875_3 = x7875_1;
 assertTrue(x7875_1.equals(x7875_1));
 assertTrue(x7875_1.equals(x7875_3));
 assertFalse(x7875_1.equals(x7875_2));
 }

 @Test
 public void testHashCode() {
 X7875_NewUnix x7875_1 = new X7875_NewUnix();
 BigInteger uid = new BigInteger("1234567890123456789012345678901234567890");
 BigInteger gid = new BigInteger("9876543210987654321098765432109876543210");
 x7875_1.setUID(uid);
 x7875_1.setGID(gid);
 X7875_NewUnix x7875_2 = new X7875_NewUnix();
 x7875_2.setUID(uid);
 x7875_2.setGID(gid);
 X7875_NewUnix x7875_3 = new X7875_NewUnix();
 x7875_3.setUID(uid);
 x7875_3.setGID(new BigInteger("1234567890123456789012345678901234567891"));
 int expectedHashCode = -1528410387;
 int actualHashCode_1 = x7875_1.hashCode();
 int actualHashCode_2 = x7875_2.hashCode();
 int actualHashCode_3 = x7875_3.hashCode();
 assertEquals(expectedHashCode, actualHashCode_1);
 assertEquals(expectedHashCode, actualHashCode_2);
 assertNotEquals(expectedHashCode, actualHashCode_3);
 }
}