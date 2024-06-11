import org.apache.commons.compress.archivers.zip.ZipShort;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.junit.Test;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {

 private X7875_NewUnix createX7875_NewUnixInstance() {
 return new X7875_NewUnix();
 }

 @Test
 public void testGetHeaderId() {
 X7875_NewUnix x7875_newUnix = createX7875_NewUnixInstance();
 ZipShort result = x7875_newUnix.getHeaderId();
 ZipShort expected = new ZipShort(0x7875);
 assertEquals(expected, result);
 }

 @Test
 public void testGetUID() {
 X7875_NewUnix x7875_newUnix = createX7875_NewUnixInstance();
 long uid = 1001;
 x7875_newUnix.setUID(uid);
 long result = x7875_newUnix.getUID();
 assertEquals(uid, result);
 }

 @Test
 public void testSetUID() {
 X7875_NewUnix x7875_newUnix = createX7875_NewUnixInstance();
 long uid = 1001;
 x7875_newUnix.setUID(uid);
 long expected = uid;
 assertEquals(expected, x7875_newUnix.getUID());
 }

 @Test(expected = ZipException.class)
 public void testSetNegativeUID() {
 X7875_NewUnix x7875_newUnix = createX7875_NewUnixInstance();
 long uid = -1;
 x7875_newUnix.setUID(uid);
 fail("Should have thrown ZipException");
 }

 @Test
 public void testGetGID() {
 X7875_NewUnix x7875_newUnix = createX7875_NewUnixInstance();
 long gid = 1002;
 x7875_newUnix.setGID(gid);
 long result = x7875_newUnix.getGID();
 assertEquals(gid, result);
 }

 @Test
 public void testSetGID() {
 X7875_NewUnix x7875_newUnix = createX7875_NewUnixInstance();
 long gid = 1002;
 x7875_newUnix.setGID(gid);
 long expected = gid;
 assertEquals(expected, x7875_newUnix.getGID());
 }

 @Test(expected = ZipException.class)
 public void testSetNegativeGID() {
 X7875_NewUnix x7875_newUnix = createX7875_NewUnixInstance();
 long gid = -1;
 x7875_newUnix.setGID(gid);
 fail("Should have thrown ZipException");
 }

 @Test
 public void testGetLocalFileDataLength() {
 X7875_NewUnix x7875_newUnix = createX7875_NewUnixInstance();
 int uidSize = 2;
 int gidSize = 2;
 x7875_newUnix.setUID(1001);
 x7875_newUnix.setGID(2002);
 ZipShort result = x7875_newUnix.getLocalFileDataLength();
 ZipShort expected = new ZipShort((short) (3 + uidSize + gidSize));
 assertEquals(expected, result);
 }

 @Test
 public void testGetCentralDirectoryLength() {
 X7875_NewUnix x7875_newUnix = createX7875_NewUnixInstance();
 ZipShort result = x7875_newUnix.getCentralDirectoryLength();
 ZipShort expected = new ZipShort((short) 0);
 assertEquals(expected, result);
 }

 @Test
 public void testGetLocalFileDataData() {
 X7875_NewUnix x7875_newUnix = createX7875_NewUnixInstance();
 byte[] expected = new byte[]{(byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
 x7875_newUnix.setUID(1001);
 x7875_newUnix.setGID(2002);
 byte[] result = x7875_newUnix.getLocalFileDataData();
 assertArrayEquals(expected, result);
 }

 @Test
 public void testGetCentralDirectoryData() {
 X7875_NewUnix x7875_newUnix = createX7875_NewUnixInstance();
 byte[] expected = new byte[0];
 byte[] result = x7875_newUnix.getCentralDirectoryData();
 assertArrayEquals(expected, result);
 }

 @Test
 public void testParseFromLocalFileData() {
 X7875_NewUnix x7875_newUnix = createX7875_NewUnixInstance();
 byte[] data = new byte[]{(byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00};
 x7875_newUnix.parseFromLocalFileData(data, 0, data.length);
 long expectedUID = 1001;
 long expectedGID = 2002;
 assertEquals(expectedUID, x7875_newUnix.getUID());
 assertEquals(expectedGID, x7875_newUnix.getGID());
 }

 @Test(expected = ZipException.class)
 public void testParseFromLocalFileDataWithInvalidLength() {
 X7875_NewUnix x7875_newUnix = createX7875_NewUnixInstance();
 byte[] data = new byte[]{(byte) 0x01, (byte) 0x01, (byte) 0x01};
 x7875_newUnix.parseFromLocalFileData(data, 0, data.length);
 fail("Should have thrown ZipException");
 }

 @Test
 public void testReset() {
 X7875_NewUnix x7875_newUnix = createX7875_NewUnixInstance();
 x7875_newUnix.parseFromLocalFileData(new byte[]{0x01}, 0, 1);
 x7875_newUnix.reset();
 assertTrue(x7875_newUnix.getUID() == 0);
 assertTrue(x7875_newUnix.getGID() == 0);
 }

 @Test
 public void testToString() {
 X7875_NewUnix x7875_newUnix = createX7875_NewUnixInstance();
 x7875_newUnix.setUID(1001);
 x7875_newUnix.setGID(2002);
 String result = x7875_newUnix.toString();
 String expected = "0x7875 Zip Extra Field: UID=1001 GID=2002";
 assertEquals(expected, result);
 }
}