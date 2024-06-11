import static org.junit.Assert.*;
import org.apache.commons.compress.archivers.zip.*;

public class X7875_NewUnixTest {

 private X7875_NewUnix newUnix;

 @Before
 public void setUp() {
 newUnix = new X7875_NewUnix();
 }

 @Test
 public void testEmptyConstructor() {
 // check default values
 assertEquals(1, newUnix.getVersion());
 assertNull(newUnix.getUID());
 assertNull(newUnix.getGID());
 }

 @Test
 public void testSetAndGetUid() {
 newUnix.setUID(123456789L);
 assertEquals(123456789L, newUnix.getUID());
 }

 @Test
 public void testSetAndGetGid() {
 newUnix.setGID(234567890L);
 assertEquals(234567890L, newUnix.getGID());
 }

 @Test
 public void testLocalFileDataLength() {
 newUnix.setUID(123456789L);
 newUnix.setGID(234567890L);

 int expectedLength = 5; // version + uidSize + gidSize
 assertEquals(expectedLength, newUnix.getLocalFileDataLength().getValue().intValue());
 }

 @Test
 public void testCentralDirectoryLength() {
 testLocalFileDataLength();
 assertEquals(0, newUnix.getCentralDirectoryLength().getValue().intValue());
 }

 @Test
 public void testLocalFileDataData() throws ZipException {
 newUnix.setUID(123456789L);
 newUnix.setGID(234567890L);

 byte[] expectedData = new byte[7];
 expectedData[0] = 0x78;
 expectedData[1] = (byte) 0x75;
 expectedData[2] = (byte) 0x01;
 expectedData[3] = 0;
 expectedData[4] = 0x01; // uid size
 expectedData[5] = (byte) 0x08; // uid length
 expectedData[6] = 0x49; // first byte of uid (0x49 is the least significant byte of 0x49687352)
 System.arraycopy(newUnix.getUID().toByteArray(), 0, expectedData, 6, newUnix.getUID().toByteArray().length);

 byte[] resultData = newUnix.getLocalFileDataData();
 assertArrayEquals(expectedData, resultData);
 }

 @Test(expected = ZipException.class)
 public void testParseFromCentralDirectoryData() throws ZipException {
 newUnix.parseFromCentralDirectoryData(new byte[0], 0, 0);
 }

 @Test
 public void testParseFromLocalFileData() throws ZipException {
 byte[] data = newUnix.getLocalFileDataData();
 newUnix.parseFromLocalFileData(data, 0, data.length);
 assertEquals(1, newUnix.getVersion());
 assertEquals(123456789L, newUnix.getUID());
 assertEquals(234567890L, newUnix.getGID());
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 X7875_NewUnix cloned = (X7875_NewUnix) newUnix.clone();
 assertEquals(1, cloned.getVersion());
 assertEquals(123456789L, cloned.getUID());
 assertEquals(234567890L, cloned.getGID());
 }

 @Test
 public void testEquals() {
 X7875_NewUnix another = new X7875_NewUnix();
 X7875_NewUnix anotherWithSameValues = new X7875_NewUnix();
 anotherWithSameValues.setUID(123456789L);
 anotherWithSameValues.setGID(234567890L);
 X7875_NewUnix anotherWithDifferentValues = new X7875_NewUnix();
 anotherWithDifferentValues.setUID(987654321L);
 anotherWithDifferentValues.setGID(876543210L);

 assertTrue(newUnix.equals(newUnix));
 assertTrue(newUnix.equals(anotherWithSameValues));
 assertFalse(newUnix.equals(another));
 assertFalse(newUnix.equals(anotherWithDifferentValues));
 }

 @Test
 public void testHashCode() {
 X7875_NewUnix another = new X7875_NewUnix();
 another.setUID(123456789L);
 another.setGID(234567890L);

 int newHashCode = newUnix.hashCode();
 int anotherHashCode = another.hashCode();

 // hashCode() must return different values for different objects
 // even if their equals() returns true.
 assertNotEquals(newHashCode, anotherHashCode);
 }
}