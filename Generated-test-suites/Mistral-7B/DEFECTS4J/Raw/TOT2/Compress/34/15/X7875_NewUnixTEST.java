import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

public class X7875_NewUnixTest {

 // Test for constructor
 @Test
 public void testConstructor() {
 X7875_NewUnix testX7875 = new X7875_NewUnix();
 Assert.assertEquals(1, testX7875.getVersion());
 Assert.assertNull(testX7875.getUID());
 Assert.assertNull(testX7875.getGID());
 }

 // Test for getHeaderId() method
 @Test
 public void testGetHeaderId() {
 X7875_NewUnix testX7875 = new X7875_NewUnix();
 ZipShort expectedHeaderId = new ZipShort(0x7875);
 ZipShort actualHeaderId = testX7875.getHeaderId();
 Assert.assertEquals(expectedHeaderId, actualHeaderId);
 }

 // Test for getUID() method
 @Test
 public void testGetUID() {
 X7875_NewUnix testX7875 = new X7875_NewUnix();
 testX7875.setUID(12345L);
 long expectedUID = 12345L;
 long actualUID = testX7875.getUID();
 Assert.assertEquals(expectedUID, actualUID);
 }

 // Test for getGID() method
 @Test
 public void testGetGID() {
 X7875_NewUnix testX7875 = new X7875_NewUnix();
 testX7875.setGID(67890L);
 long expectedGID = 67890L;
 long actualGID = testX7875.getGID();
 Assert.assertEquals(expectedGID, actualGID);
 }

 // Test for getLocalFileDataLength() method
 @Test
 public void testGetLocalFileDataLength() {
 X7875_NewUnix testX7875 = new X7875_NewUnix();
 testX7875.setUID(12345L);
 testX7875.setGID(67890L);
 int expectedLength = 7 + 1 + 1; // version=1 + uidsize=1 + gidsize=1
 ZipShort actualLength = testX7875.getLocalFileDataLength();
 Assert.assertEquals(new ZipShort(expectedLength), actualLength);
 }

 // Test for parseFromLocalFileData() method
 @Test
 public void testParseFromLocalFileData() throws ZipException {
 byte[] testData = new byte[]{(byte) 0x01, (byte) 0x01, 0x00, 0x00, 0x00, 0x34, 0x35, 0x00, 0x01, 0x01, 0x36, 0x37, 0x38, 0x39, 0x00, 0x36, 0x37};
 X7875_NewUnix testX7875 = new X7875_NewUnix();
 testX7875.parseFromLocalFileData(testData, 0, testData.length);
 Assert.assertEquals(1, testX7875.getVersion());
 BigInteger expectedUID = BigInteger.valueOf(12345L);
 BigInteger expectedGID = BigInteger.valueOf(67890L);
 Assert.assertEquals(expectedUID, testX7875.uid);
 Assert.assertEquals(expectedGID, testX7875.gid);
 }

 // Test for equals() method
 @Test
 public void testEquals() {
 X7875_NewUnix testX7875_1 = new X7875_NewUnix();
 testX7875_1.setUID(12345L);
 testX7875_1.setGID(67890L);
 X7875_NewUnix testX7875_2 = new X7875_NewUnix();
 testX7875_2.setUID(12345L);
 testX7875_2.setGID(67890L);
 X7875_NewUnix testX7875_3 = new X7875_NewUnix();
 testX7875_3.setUID(67890L);
 testX7875_3.setGID(12345L);
 Assert.assertTrue(testX7875_1.equals(testX7875_2));
 Assert.assertFalse(testX7875_1.equals(testX7875_3));
 }

 // Test for hashCode() method
 @Test
 public void testHashCode() {
 X7875_NewUnix testX7875_1 = new X7875_NewUnix();
 testX7875_1.setUID(12345L);
 testX7875_1.setGID(67890L);
 X7875_NewUnix testX7875_2 = new X7875_NewUnix();
 testX7875_2.setUID(67890L);
 testX7875_2.setGID(12345L);
 Assert.assertEquals(testX7875_1.hashCode(), testX7875_2.hashCode());
 }

 // Test for trimLeadingZeroesForceMinLength() method
 @Test
 public void testTrimLeadingZeroesForceMinLength() {
 byte[] testArray_1 = new byte[]{0x01, 0x00, 0x00, 0x00};
 byte[] testArray_2 = new byte[]{0x01, 0x02, 0x03, 0x04};
 byte[] testArray_3 = new byte[]{0x01, 0x02, 0x03};
 byte[] expectedTrimmedArray_1 = new byte[]{0x01};
 byte[] expectedTrimmedArray_2 = new byte[]{0x01, 0x02, 0x03, 0x04};
 byte[] expectedTrimmedArray_3 = new byte[]{0x01, 0x02, 0x03};
 byte[] actualTrimmedArray_1 = X7875_NewUnix.trimLeadingZeroesForceMinLength(testArray_1);
 byte[] actualTrimmedArray_2 = X7875_NewUnix.trimLeadingZeroesForceMinLength(testArray_2);
 byte[] actualTrimmedArray_3 = X7875_NewUnix.trimLeadingZeroesForceMinLength(testArray_3);
 Assert.assertArrayEquals(expectedTrimmedArray_1, actualTrimmedArray_1);
 Assert.assertArrayEquals(expectedTrimmedArray_2, actualTrimmedArray_2);
 Assert.assertArrayEquals(expectedTrimmedArray_3, actualTrimmedArray_3);
 }
}