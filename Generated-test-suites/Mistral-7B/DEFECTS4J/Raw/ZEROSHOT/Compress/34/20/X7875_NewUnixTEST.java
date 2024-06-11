import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {

 private X7875_NewUnix createNewUnix() {
 return new X7875_NewUnix();
 }

 @Test
 public void testGetHeaderId() {
 X7875_NewUnix x7875 = createNewUnix();
 ZipShort expectedHeaderId = new ZipShort(0x7875);
 ZipShort actualHeaderId = x7875.getHeaderId();
 assertEquals(expectedHeaderId.getValue(), actualHeaderId.getValue());
 }

 @Test
 public void testGetUID() {
 X7875_NewUnix x7875 = createNewUnix();
 x7875.setUID(12345);
 long expectedUid = 12345;
 long actualUid = x7875.getUID();
 assertEquals(expectedUid, actualUid);
 }

 @Test
 public void testSetUID() {
 X7875_NewUnix x7875 = createNewUnix();
 x7875.setUID(12345);
 BigInteger expectedUid = new BigInteger("12345");
 BigInteger actualUid = x7875.uid;
 assertEquals(expectedUid, actualUid);
 }

 @Test
 public void testGetGID() {
 X7875_NewUnix x7875 = createNewUnix();
 x7875.setGID(23456);
 long expectedGid = 23456;
 long actualGid = x7875.getGID();
 assertEquals(expectedGid, actualGid);
 }

 @Test
 public void testSetGID() {
 X7875_NewUnix x7875 = createNewUnix();
 x7875.setGID(23456);
 BigInteger expectedGid = new BigInteger("23456");
 BigInteger actualGid = x7875.gid;
 assertEquals(expectedGid, actualGid);
 }

 @Test
 public void testGetLocalFileDataLength() throws ZipException {
 X7875_NewUnix x7875 = createNewUnix();
 x7875.setUID(123);
 x7875.setGID(456);
 ZipShort expectedLength = new ZipShort(3 + 1 + 1 + 1 + 1 + 1);
 ZipShort actualLength = x7875.getLocalFileDataLength();
 assertEquals(expectedLength.getValue(), actualLength.getValue());
 }

 @Test
 public void testGetCentralDirectoryLength() throws ZipException {
 X7875_NewUnix x7875 = createNewUnix();
 x7875.setUID(123);
 x7875.setGID(456);
 ZipShort expectedLength = new ZipShort(3 + 1 + 1 + 1 + 1 + 1);
 ZipShort actualLength = x7875.getCentralDirectoryLength();
 assertEquals(expectedLength.getValue(), actualLength.getValue());
 }

 @Test
 public void testGetLocalFileDataData() throws ZipException {
 X7875_NewUnix x7875 = createNewUnix();
 x7875.setUID(123);
 x7875.setGID(456);
 byte[] expectedData = new byte[]{
 0x01, 0x01, 0x00, 0x79, 0x01, 0x00, 0x01, 0x00,
 0x79, 0x01, 0x00, 0x02, 0x01, 0x00, 0x01, 0x00,
 0x79
 };
 byte[] actualData = x7875.getLocalFileDataData();
 assertArrayEquals(expectedData, actualData);
 }

 @Test(expected = ZipException.class)
 public void testParseFromCentralDirectoryDataFails() throws ZipException {
 X7875_NewUnix x7875 = createNewUnix();
 byte[] data = new byte[0];
 x7875.parseFromCentralDirectoryData(data, 0, data.length);
 }

 @Test
 public void testReset() {
 X7875_NewUnix x7875 = createNewUnix();
 x7875.setUID(123);
 x7875.setGID(456);
 x7875.reset();
 assertEquals(BigInteger.ZERO, x7875.uid);
 assertEquals(BigInteger.ZERO, x7875.gid);
 }

}