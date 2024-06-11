import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class X7875_NewUnixTest {

 private X7875_NewUnix x7875NewUnix;

 @Before
 public void setUp() {
 x7875NewUnix = new X7875_NewUnix();
 }

 @Test
 public void testGetHeaderId() {
 ZipShort headerId = x7875NewUnix.getHeaderId();
 assertEquals(0x7875, headerId.getValue().intValue());
 }

 @Test
 public void testGetUIDAndSetUID() {
 long uid = 1234567890L;
 x7875NewUnix.setUID(uid);
 long retrievedUid = x7875NewUnix.getUID();
 assertEquals(uid, retrievedUid);
 }

 @Test
 public void testGetGIDAndSetGID() {
 long gid = 9876543210L;
 x7875NewUnix.setGID(gid);
 long retrievedGid = x7875NewUnix.getGID();
 assertEquals(gid, retrievedGid);
 }

 @Test
 public void testGetLocalFileDataLength() {
 // UID size is 4 bytes (32 bits)
 // GID size is 4 bytes (32 bits)
 // version is 1 byte
 int expectedLocalFileDataLength = 4 + 4 + 1;
 ZipShort localFileDataLength = x7875NewUnix.getLocalFileDataLength();
 assertEquals(expectedLocalFileDataLength, localFileDataLength.getValue().intValue());
 }

 @Test
 public void testGetCentralDirectoryLength() {
 // Central directory does not store any data for this extra field
 ZipShort centralDirectoryLength = x7875NewUnix.getCentralDirectoryLength();
 assertEquals(0, centralDirectoryLength.getValue().intValue());
 }

 @Test
 public void testGetLocalFileDataData() {
 byte[] expectedData = new byte[1 + 4 + 4]; // version + UID + GID
 x7875NewUnix.setUID(1234567890L);
 x7875NewUnix.setGID(9876543210L);
 byte[] actualData = x7875NewUnix.getLocalFileDataData();
 assertArrayEquals(expectedData, actualData);
 }

 @Test
 public void testParseFromLocalFileData() throws ZipException {
 byte[] data = x7875NewUnix.getLocalFileDataData();
 x7875NewUnix.reset();
 x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
 long retrievedUid = x7875NewUnix.getUID();
 long retrievedGid = x7875NewUnix.getGID();
 long uid = 1234567890L;
 long gid = 9876543210L;
 assertEquals(uid, retrievedUid);
 assertEquals(gid, retrievedGid);
 }

 @Test(expected = ZipException.class)
 public void testParseFromLocalFileDataWithInvalidData() throws ZipException {
 byte[] invalidData = new byte[]{0x78, 0x75, 0x01};
 x7875NewUnix.reset();
 x7875NewUnix.parseFromLocalFileData(invalidData, 0, invalidData.length);
 }

 @Test
 public void testEqualsAndHashCode() {
 X7875_NewUnix x7875NewUnix1 = new X7875_NewUnix();
 x7875NewUnix1.setUID(1234567890L);
 x7875NewUnix1.setGID(9876543210L);
 X7875_NewUnix x7875NewUnix2 = new X7875_NewUnix();
 X7875_NewUnix x7875NewUnix3 = x7875NewUnix1;
 X7875_NewUnix x7875NewUnix4 = new X7875_NewUnix();
 x7875NewUnix4.setUID(1234567890L);
 x7875NewUnix4.setGID(9876543211L);

 assertEquals(x7875NewUnix1, x7875NewUnix3);
 assertNotEquals(x7875NewUnix1, x7875NewUnix2);
 assertNotEquals(x7875NewUnix1, x7875NewUnix4);
 }

}