import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class X7875_NewUnixTest {

 @Test
 public void testNewInstance() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 assertEquals(1, x7875NewUnix.getVersion());
 assertNull(x7875NewUnix.getUID());
 assertNull(x7875NewUnix.getGID());
 }

 @Test
 public void testSetGetUID() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setUID(1234567890123456789L);
 assertEquals(1234567890123456789L, x7875NewUnix.getUID());
 }

 @Test
 public void testSetGetGID() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setGID(2345678901234567890L);
 assertEquals(2345678901234567890L, x7875NewUnix.getGID());
 }

 @Test
 public void testGetLocalFileDataLength() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setUID(1234567890123456789L);
 x7875NewUnix.setGID(2345678901234567890L);
 ZipShort length = x7875NewUnix.getLocalFileDataLength();
 assertEquals(new ZipShort((short) 7), length);
 }

 @Test
 public void testGetCentralDirectoryLength() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setUID(1234567890123456789L);
 x7875NewUnix.setGID(2345678901234567890L);
 ZipShort length = x7875NewUnix.getCentralDirectoryLength();
 assertEquals(length, x7875NewUnix.getLocalFileDataLength());
 }

 @Test
 public void testGetLocalFileDataData() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setUID(1234567890123456789L);
 x7875NewUnix.setGID(2345678901234567890L);
 byte[] data = x7875NewUnix.getLocalFileDataData();
 assertEquals(7, data.length);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetCentralDirectoryData() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setUID(1234567890123456789L);
 x7875NewUnix.setGID(2345678901234567890L);
 byte[] data = x7875NewUnix.getCentralDirectoryData();
 assertNull(data);
 }

 @Test
 public void testParseFromLocalFileData() throws ZipException {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 byte[] data = new byte[]{
 0x58, 0x75, 0x01, (byte) 0x03, 0x01,
 0x00, 0x00, 0x78, 0x12, 0x34, 0x56, 0x78,
 0x12, 0x34, 0x56, 0x78, 0x12, 0x34, 0x56, 0x78
 };
 x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
 assertEquals(1, x7875NewUnix.getVersion());
 assertEquals(1234567890123456789L, x7875NewUnix.getUID());
 assertEquals(1234567890123456789L, x7875NewUnix.getGID());
 }

 @Test(expected = ZipException.class)
 public void testParseFromLocalFileDataInvalidVersion() throws ZipException {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 byte[] data = new byte[]{
 0x58, 0x75, 0x02, (byte) 0x03, 0x01,
 0x00, 0x00, 0x78, 0x12, 0x34, 0x56, 0x78,
 0x12, 0x34, 0x56, 0x78, 0x12, 0x34, 0x56, 0x78
 };
 x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
 }

 @Test(expected = ZipException.class)
 public void testParseFromLocalFileDataInvalidUIDSize() throws ZipException {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 byte[] data = new byte[]{
 0x58, 0x75, 0x01, (byte) 0x03, 0x00,
 0x00, 0x78, 0x12, 0x34, 0x56, 0x78, 0x12, 0x34, 0x56, 0x78
 };
 x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
 }

 @Test(expected = ZipException.class)
 public void testParseFromLocalFileDataInvalidGIDSize() throws ZipException {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 byte[] data = new byte[]{
 0x58, 0x75, 0x01, (byte) 0x03, 0x01,
 0x00, 0x00, 0x78, 0x12, 0x34, 0x56, 0x79, 0x12, 0x34, 0x56, 0x78
 };
 x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setUID(1234567890123456789L);
 x7875NewUnix.setGID(2345678901234567890L);
 X7875_NewUnix clonedX7875NewUnix = (X7875_NewUnix) x7875NewUnix.clone();
 assertEquals(x7875NewUnix.getUID(), clonedX7875NewUnix.getUID());
 assertEquals(x7875NewUnix.getGID(), clonedX7875NewUnix.getGID());
 }

 @Test
 public void testEquals() {
 X7875_NewUnix x7875NewUnix1 = new X7875_NewUnix();
 X7875_NewUnix x7875NewUnix2 = new X7875_NewUnix();
 X7875_NewUnix x7875NewUnix3 = new X7875_NewUnix();
 x7875NewUnix1.setUID(1234567890123456789L);
 x7875NewUnix1.setGID(2345678901234567890L);
 x7875NewUnix2.setUID(1234567890123456789L);
 x7875NewUnix2.setGID(2345678901234567890L);
 x7875NewUnix3.setUID(1234567890123456789L);
 x7875NewUnix3.setGID(1234567890123456789L);
 assertTrue(x7875NewUnix1.equals(x7875NewUnix2));
 assertFalse(x7875NewUnix1.equals(x7875NewUnix3));
 }

 @Test
 public void testHashCode() {
 X7875_NewUnix x7875NewUnix1 = new X7875_NewUnix();
 X7875_NewUnix x7875NewUnix2 = new X7875_NewUnix();
 X7875_NewUnix x7875NewUnix3 = new X7875_NewUnix();
 x7875NewUnix1.setUID(1234567890123456789L);
 x7875NewUnix1.setGID(2345678901234567890L);
 x7875NewUnix2.setUID(1234567890123456789L);
 x7875NewUnix2.setGID(2345678901234567890L);
 x7875NewUnix3.setUID(1234567890123456789L);
 x7875NewUnix3.setGID(1234567890123456789L);
 assertEquals(x7875NewUnix1.hashCode(), x7875NewUnix2.hashCode());
 assertNotEquals(x7875NewUnix1.hashCode(), x7875NewUnix3.hashCode());
 }
}