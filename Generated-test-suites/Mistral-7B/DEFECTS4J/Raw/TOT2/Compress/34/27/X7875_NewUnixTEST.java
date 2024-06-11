import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class X7875_NewUnixTest {

 // Test case for constructor and getHeaderId() method
 @Test
 public void testX7875_NewUnixCreationAndHeaderId() {
 X7875_NewUnix newUnix = new X7875_NewUnix();
 ZipShort headerId = newUnix.getHeaderId();
 assertEquals("Incorrect header ID", new Short((short) 0x7875), headerId.getValue());
 }

 // Test case for getUID() method with typical value
 @Test
 public void testGetUIDWithTypicalValue() {
 X7875_NewUnix newUnix = new X7875_NewUnix();
 long uid = 12345;
 newUnix.setUID(uid);
 assertEquals("Incorrect UID", uid, newUnix.getUID());
 }

 // Test case for getUID() method with edge value (maximum value)
 @Test
 public void testGetUIDWithMaxValue() {
 X7875_NewUnix newUnix = new X7875_NewUnix();
 long maxValue = Long.MAX_VALUE;
 newUnix.setUID(maxValue);
 long uid = newUnix.getUID();
 assertEquals("Incorrect UID", maxValue, uid);
 }

 // Test case for getGID() method with typical value
 @Test
 public void testGetGIDWithTypicalValue() {
 X7875_NewUnix newUnix = new X7875_NewUnix();
 long gid = 67890;
 newUnix.setGID(gid);
 assertEquals("Incorrect GID", gid, newUnix.getGID());
 }

 // Test case for getGID() method with edge value (minimum value)
 @Test
 public void testGetGIDWithMinValue() {
 X7875_NewUnix newUnix = new X7875_NewUnix();
 long minValue = Long.MIN_VALUE;
 newUnix.setGID(minValue);
 long gid = newUnix.getGID();
 assertEquals("Incorrect GID", minValue, gid);
 }

 // Test case for getLocalFileDataLength() method with typical values for UID and GID
 @Test
 public void testGetLocalFileDataLengthWithTypicalValues() {
 X7875_NewUnix newUnix = new X7875_NewUnix();
 newUnix.setUID(12345);
 newUnix.setGID(67890);
 ZipShort expectedLength = new ZipShort((short) (3 + 1 + 1 + 1 + 4 + 4));
 ZipShort actualLength = newUnix.getLocalFileDataLength();
 assertEquals("Incorrect local file data length", expectedLength.getValue(), actualLength.getValue());
 }

 // Test case for getCentralDirectoryLength() method
 @Test
 public void testGetCentralDirectoryLength() {
 X7875_NewUnix newUnix = new X7875_NewUnix();
 ZipShort expectedLength = new ZipShort((short) 0);
 ZipShort actualLength = newUnix.getCentralDirectoryLength();
 assertEquals("Incorrect central directory length", expectedLength.getValue(), actualLength.getValue());
 }

 // Test case for getLocalFileDataData() method
 @Test
 public void testGetLocalFileDataData() {
 X7875_NewUnix newUnix = new X7875_NewUnix();
 newUnix.setUID(12345);
 newUnix.setGID(67890);
 byte[] expectedData = new byte[12];
 byte[] actualData = newUnix.getLocalFileDataData();
 assertNotNull("Local file data data should not be null", actualData);
 assertEquals("Incorrect local file data data", expectedData, actualData);
 }

 // Test case for getCentralDirectoryData() method
 @Test
 public void testGetCentralDirectoryData() {
 X7875_NewUnix newUnix = new X7875_NewUnix();
 byte[] expectedData = new byte[0];
 byte[] actualData = newUnix.getCentralDirectoryData();
 assertEquals("Incorrect central directory data", expectedData, actualData);
 }

 // Test case for parseFromLocalFileData() method
 @Test
 public void testParseFromLocalFileData() throws ZipException {
 X7875_NewUnix newUnix = new X7875_NewUnix();
 byte[] data = new byte[12];
 data[0] = 1;
 data[1] = 1;
 System.arraycopy(new byte[] { 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0 }, 2, data, 2, 10);
 newUnix.parseFromLocalFileData(data, 0, data.length);
 assertEquals("Incorrect UID", new BigInteger("1"), newUnix.getUID());
 assertEquals("Incorrect GID", new BigInteger("1"), newUnix.getGID());
 }

 // Test case for parseFromCentralDirectoryData() method
 @Test
 public void testParseFromCentralDirectoryData() throws ZipException {
 X7875_NewUnix newUnix = new X7875_NewUnix();
 newUnix.parseFromCentralDirectoryData(new byte[0], 0, 0);
 assertNotNull("parseFromCentralDirectoryData() should not return null", newUnix);
 assertEquals("UID should be 1", new BigInteger("1"), newUnix.getUID());
 assertEquals("GID should be 1", new BigInteger("1"), newUnix.getGID());
 }
}