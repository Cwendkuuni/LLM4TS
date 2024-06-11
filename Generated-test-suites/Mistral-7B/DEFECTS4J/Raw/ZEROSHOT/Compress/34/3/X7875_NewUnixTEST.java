import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class X7875_NewUnixTest {

 private static final byte[] TEST_UID = new byte[]{(byte) 0x11, (byte) 0x22, (byte) 0x33};
 private static final byte[] TEST_GID = new byte[]{(byte) 0x44, (byte) 0x55, (byte) 0x66};

 @Test
 public void testNewInstance() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 assertEquals(1, x7875.getVersion());
 assertNull(x7875.getUID());
 assertNull(x7875.getGID());
 }

 @Test
 public void testSetGetUID() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.setUID(12345L);
 assertEquals(12345L, x7875.getUID());
 x7875.setUID(Long.MIN_VALUE);
 assertEquals(Long.MIN_VALUE, x7875.getUID());
 x7875.setUID(Long.MAX_VALUE);
 assertEquals(Long.MAX_VALUE, x7875.getUID());
 x7875.setUID(new BigInteger("1234567890123456789012345678901234567890"));
 assertEquals(new BigInteger("1234567890123456789012345678901234567890"), x7875.getUID());
 }

 @Test
 public void testSetGetGID() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.setGID(23456L);
 assertEquals(23456L, x7875.getGID());
 x7875.setGID(Long.MIN_VALUE);
 assertEquals(Long.MIN_VALUE, x7875.getGID());
 x7875.setGID(Long.MAX_VALUE);
 assertEquals(Long.MAX_VALUE, x7875.getGID());
 x7875.setGID(new BigInteger("234567890123456789012345678901234567890"));
 assertEquals(new BigInteger("234567890123456789012345678901234567890"), x7875.getGID());
 }

 @Test
 public void testParseFromLocalFileData() throws ZipException {
 byte[] data = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, TEST_UID[0], TEST_UID[1], TEST_UID[2], (byte) 0x03, TEST_GID[0], TEST_GID[1], TEST_GID[2], (byte) 0x03};
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.parseFromLocalFileData(data, 0, data.length);
 assertEquals(1, x7875.getVersion());
 assertEquals(new BigInteger(new String(TEST_UID)), x7875.getUID());
 assertEquals(new BigInteger(new String(TEST_GID)), x7875.getGID());
 }

 @Test(expected = ZipException.class)
 public void testParseFromLocalFileDataInvalid() throws ZipException {
 byte[] data = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03};
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.parseFromLocalFileData(data, 0, data.length);
 }

 @Test
 public void testParseFromCentralDirectoryData() throws ZipException {
 // Nothing to test here as getCentralDirectoryData() returns an empty byte array.
 }

 @Test
 public void testToString() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.setUID(12345L);
 x7875.setGID(23456L);
 assertEquals("0x7875 Zip Extra Field: UID=12345 GID=23456", x7875.toString());
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 X7875_NewUnix original = new X7875_NewUnix();
 original.setUID(12345L);
 original.setGID(23456L);
 X7875_NewUnix clone = (X7875_NewUnix) original.clone();
 assertEquals(original.getUID(), clone.getUID());
 assertEquals(original.getGID(), clone.getGID());
 }

 @Test
 public void testEquals() {
 X7875_NewUnix x7875_1 = new X7875_NewUnix();
 X7875_NewUnix x7875_2 = new X7875_NewUnix();
 X7875_NewUnix x7875_3 = new X7875_NewUnix();
 x7875_1.setUID(12345L);
 x7875_1.setGID(23456L);
 x7875_2.setUID(12345L);
 x7875_2.setGID(23456L);
 x7875_3.setUID(56789L);
 x7875_3.setGID(87654L);
 assertTrue(x7875_1.equals(x7875_2));
 assertFalse(x7875_1.equals(x7875_3));
 assertFalse(x7875_1.equals(null));
 assertFalse(x7875_1.equals(""));
 }

 @Test
 public void testHashCode() {
 X7875_NewUnix x7875_1 = new X7875_NewUnix();
 X7875_NewUnix x7875_2 = new X7875_NewUnix();
 X7875_NewUnix x7875_3 = new X7875_NewUnix();
 x7875_1.setUID(12345L);
 x7875_1.setGID(23456L);
 x7875_2.setUID(12345L);
 x7875_2.setGID(23456L);
 x7875_3.setUID(56789L);
 x7875_3.setGID(87654L);
 int hashCode_1 = x7875_1.hashCode();
 int hashCode_2 = x7875_2.hashCode();
 int hashCode_3 = x7875_3.hashCode();
 assertTrue(x7875_1.equals(x7875_2) && hashCode_1 == hashCode_2);
 assertFalse(x7875_1.equals(x7875_3) && hashCode_1 != hashCode_3);
 }
}