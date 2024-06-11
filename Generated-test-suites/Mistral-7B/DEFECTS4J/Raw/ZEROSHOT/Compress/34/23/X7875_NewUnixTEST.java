import static org.junit.Assert.*;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.apache.commons.compress.archivers.zip.X7875_NewUnix;

public class X7875_NewUnixTest {

 private X7875_NewUnix createNewX7875_NewUnix() {
 return new X7875_NewUnix();
 }

 @Test
 public void testGetHeaderId() {
 X7875_NewUnix newUnix = createNewX7875_NewUnix();
 ZipShort headerId = newUnix.getHeaderId();
 assertEquals("Unexpected header id", newShort((short) 0x7875), headerId);
 }

 @Test
 public void testGetUID() {
 X7875_NewUnix newUnix = createNewX7875_NewUnix();
 newUnix.setUID(0); // assume a UID of 0 is for root
 long uid = newUnix.getUID();
 assertEquals("Unexpected UID", 0, uid);
 }

 @Test
 public void testSetUID() {
 X7875_NewUnix newUnix = createNewX7875_NewUnix();
 newUnix.setUID(1234567890L);
 long uid = newUnix.getUID();
 assertEquals("Unexpected UID", 1234567890L, uid);
 }

 @Test
 public void testGetGID() {
 X7875_NewUnix newUnix = createNewX7875_NewUnix();
 newUnix.setGID(0); // assume a GID of 0 is for group root
 long gid = newUnix.getGID();
 assertEquals("Unexpected GID", 0, gid);
 }

 @Test
 public void testSetGID() {
 X7875_NewUnix newUnix = createNewX7875_NewUnix();
 newUnix.setGID(9876543210L);
 long gid = newUnix.getGID();
 assertEquals("Unexpected GID", 9876543210L, gid);
 }

 @Test
 public void testGetLocalFileDataLength() throws Exception {
 X7875_NewUnix newUnix = createNewX7875_NewUnix();
 newUnix.setUID(1);
 newUnix.setGID(1);
 ZipShort length = newUnix.getLocalFileDataLength();
 ZipShort expectedLength = new ZipShort((short) 6);
 assertEquals("Unexpected length", expectedLength, length);
 }

 @Test
 public void testGetCentralDirectoryLength() throws Exception {
 X7875_NewUnix newUnix = createNewX7875_NewUnix();
 newUnix.setUID(1);
 newUnix.setGID(1);
 ZipShort length = newUnix.getCentralDirectoryLength();
 ZipShort expectedLength = new ZipShort((short) 6);
 assertEquals("Unexpected length", expectedLength, length);
 }

 @Test
 public void testGetLocalFileDataData() throws Exception {
 X7875_NewUnix newUnix = createNewX7875_NewUnix();
 newUnix.setUID(1);
 newUnix.setGID(1);
 byte[] data = newUnix.getLocalFileDataData();
 // compare byte[] arrays using Arrays.equals()
 byte[] expectedData = new byte[]{(byte) 0x55, (byte) 0x78, (byte) 0x01, (byte) 0x00,
 (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x01};
 assertTrue("Unexpected data", Arrays.equals(expectedData, data));
 }

 @Test
 public void testParseFromLocalFileData() throws Exception {
 X7875_NewUnix newUnix = createNewX7875_NewUnix();
 byte[] data = new byte[]{(byte) 0x55, (byte) 0x78, (byte) 0x01, (byte) 0x01,
 (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x00,
 (byte) 0x01};
 newUnix.parseFromLocalFileData(data, 0, data.length);
 assertEquals("Unexpected UID", new BigInteger("1"), newUnix.getUID());
 assertEquals("Unexpected GID", new BigInteger("1"), newUnix.getGID());
 }

 @Test(expected = ZipException.class)
 public void testParseFromLocalFileDataInvalidData() throws Exception {
 X7875_NewUnix newUnix = createNewX7875_NewUnix();
 byte[] data = new byte[]{(byte) 0x55, (byte) 0x78, (byte) 0x01, (byte) 0x01,
 (byte) 0x02, (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x00,
 (byte) 0x01};
 newUnix.parseFromLocalFileData(data, 0, data.length);
 fail("Parse should have thrown an exception");
 }

 @Test
 public void testReset() throws Exception {
 X7875_NewUnix newUnix = createNewX7875_NewUnix();
 newUnix.setUID(1);
 newUnix.setGID(1);
 newUnix.reset();
 assertEquals("Unexpected UID", new BigInteger("0"), newUnix.getUID());
 assertEquals("Unexpected GID", new BigInteger("0"), newUnix.getGID());
 }
}