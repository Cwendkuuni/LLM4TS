import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

public class X7875_NewUnixTest {

 @Test
 public void testGetHeaderId() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 ZipShort headerIdExpected = new ZipShort(0x7875);
 ZipShort headerIdActual = x7875.getHeaderId();
 Assert.assertEquals("Unexpected header id.", headerIdExpected, headerIdActual);
 }

 @Test
 public void testGetUID() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.setUID(12345);
 long uidExpected = 12345;
 long uidActual = x7875.getUID();
 Assert.assertEquals("Unexpected UID.", uidExpected, uidActual);
 }

 @Test
 public void testSetUID() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 long uidToSet = 67890;
 x7875.setUID(uidToSet);
 BigInteger uidExpected = new BigInteger(String.valueOf(uidToSet));
 BigInteger uidActual = x7875.uid;
 Assert.assertEquals("Unexpected UID.", uidExpected, uidActual);
 }

 @Test
 public void testTrimLeadingZeroesForceMinLength() {
 byte[] arrayWithLeadingZeroes = new byte[] {0, 0, 0, 1, 2, 3};
 byte[] expectedTrimmedArray = new byte[] {1, 2, 3};
 byte[] trimmedArrayActual = X7875_NewUnix.trimLeadingZeroesForceMinLength(arrayWithLeadingZeroes);
 Assert.assertArrayEquals("Unexpected trimmed array.", expectedTrimmedArray, trimmedArrayActual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFromLocalFileDataWithInvalidDataSize() {
 byte[] invalidData = new byte[] {1};
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.parseFromLocalFileData(invalidData, 0, invalidData.length);
 }

 @Test
 public void testParseFromLocalFileDataWithValidData() {
 byte[] validData = new byte[] {(byte) 0x78, (byte) 0x75, (byte) 0x01, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05};
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.parseFromLocalFileData(validData, 0, validData.length);
 Assert.assertEquals("Unexpected version.", 1, x7875.version);
 BigInteger uidExpected = new BigInteger("10000000001");
 BigInteger uidActual = x7875.uid;
 Assert.assertEquals("Unexpected UID.", uidExpected, uidActual);
 BigInteger gidExpected = new BigInteger("10000000002");
 BigInteger gidActual = x7875.gid;
 Assert.assertEquals("Unexpected GID.", gidExpected, gidActual);
 }

 @Test
 public void testParseFromLocalFileDataWithNegativeData() {
 byte[] negativeData = new byte[] {(byte) 0x78, (byte) 0x75, (byte) 0x01, (byte) 0xFF, (byte) 0x01, (byte) 0xFF, (byte) 0x01, (byte) 0x02, (byte) 0xFF, (byte) 0x03, (byte) 0xFF, (byte) 0x04, (byte) 0xFF, (byte) 0x05};
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.parseFromLocalFileData(negativeData, 0, negativeData.length);
 BigInteger uidActual = x7875.uid;
 BigInteger gidActual = x7875.gid;
 Assert.assertEquals("Unexpected UID.", new BigInteger("-1"), uidActual);
 Assert.assertEquals("Unexpected GID.", new BigInteger("-1"), gidActual);
 }
}