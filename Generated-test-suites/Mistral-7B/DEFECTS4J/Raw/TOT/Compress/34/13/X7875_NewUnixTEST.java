package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for X7875_NewUnix.
 */
public class X7875_NewUnixTest {

 private X7875_NewUnix x7875;

 @Before
 public void setUp() {
 x7875 = new X7875_NewUnix();
 }

 /**
 * Test for the getHeaderId() method.
 */
 @Test
 public void testGetHeaderId() {
 ZipShort expected = new ZipShort(0x7875);
 ZipShort actual = x7875.getHeaderId();
 assertEquals(expected, actual);
 }

 /**
 * Test for the getUID() method with valid UID.
 */
 @Test
 public void testGetUIDValid() {
 x7875.setUID(1234567890L);
 long expected = 1234567890L;
 long actual = x7875.getUID();
 assertEquals(expected, actual);
 }

 /**
 * Test for the getUID() method with invalid UID (negative value).
 */
 @Test(expected = ZipException.class)
 public void testGetUIDInvalid() {
 x7875.setUID(-1234567890L);
 }

 /**
 * Test for the setUID() method with valid UID.
 */
 @Test
 public void testSetUIDValid() {
 x7875.setUID(1234567890L);
 long expected = 1234567890L;
 x7875.setUID(expected);
 long actual = x7875.getUID();
 assertEquals(expected, actual);
 }

 /**
 * Test for the setUID() method with invalid UID (negative value).
 */
 @Test(expected = ZipException.class)
 public void testSetUIDInvalid() {
 x7875.setUID(-1234567890L);
 }

 /**
 * Test for the getGID() method with valid GID.
 */
 @Test
 public void testGetGIDValid() {
 x7875.setGID(234567890123L);
 long expected = 234567890123L;
 long actual = x7875.getGID();
 assertEquals(expected, actual);
 }

 /**
 * Test for the getGID() method with invalid GID (negative value).
 */
 @Test(expected = ZipException.class)
 public void testGetGIDInvalid() {
 x7875.setGID(-234567890123L);
 }

 /**
 * Test for the setGID() method with valid GID.
 */
 @Test
 public void testSetGIDValid() {
 x7875.setGID(234567890123L);
 long expected = 234567890123L;
 x7875.setGID(expected);
 long actual = x7875.getGID();
 assertEquals(expected, actual);
 }

 /**
 * Test for the setGID() method with invalid GID (negative value).
 */
 @Test(expected = ZipException.class)
 public void testSetGIDInvalid() {
 x7875.setGID(-234567890123L);
 }

 /**
 * Test for the getLocalFileDataLength() method with valid UID and GID sizes.
 */
 @Test
 public void testGetLocalFileDataLengthValid() {
 int uidSize = 4;
 int gidSize = 4;
 x7875.setUID(1234567890L);
 x7875.setGID(234567890123L);
 ZipShort expected = new ZipShort(3 + uidSize + gidSize);
 ZipShort actual = x7875.getLocalFileDataLength();
 assertEquals(expected, actual);
 }

 /**
 * Test for the getLocalFileDataLength() method with empty UID and GID.
 */
 @Test
 public void testGetLocalFileDataLengthEmpty() {
 x7875.reset();
 ZipShort expected = new ZipShort(0);
 ZipShort actual = x7875.getLocalFileDataLength();
 assertEquals(expected, actual);
 }

 /**
 * Test for the getCentralDirectoryLength() method.
 * This method is identical to getLocalFileDataLength() method.
 */
 @Test
 public void testGetCentralDirectoryLength() {
 ZipShort expected = x7875.getLocalFileDataLength();
 ZipShort actual = x7875.getCentralDirectoryLength();
 assertEquals(expected, actual);
 }

 /**
 * Test for the getLocalFileDataData() method.
 */
 @Test
 public void testGetLocalFileDataDataValid() {
 x7875.setUID(1234567890L);
 x7875.setGID(234567890123L);
 byte[] expected = new byte[]{(byte) 0x78, (byte) 0x75, (byte) 0x01, (byte) 0x01,
 (byte) 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
 (byte) 0x03, 0x00, 0x01, 0x00, 0x03, 0x00, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38,
 0x39, 0x00, 0x03, 0x00, 0x02, 0x03, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39};
 byte[] actual = x7875.getLocalFileDataData();
 assertArrayEquals(expected, actual);
 }

 /**
 * Test for the getCentralDirectoryData() method.
 * This method always returns an empty byte array since this class does not store
 * any data inside the central directory.
 */
 @Test
 public void testGetCentralDirectoryData() {
 byte[] expected = new byte[0];
 byte[] actual = x7875.getCentralDirectoryData();
 assertArrayEquals(expected, actual);
 }

 /**
 * Test for the parseFromLocalFileData() method.
 */
 @Test
 public void testParseFromLocalFileDataValid() throws ZipException {
 byte[] data = new byte[]{(byte) 0x78, (byte) 0x75, (byte) 0x01, (byte) 0x01,
 (byte) 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
 (byte) 0x03, 0x00, 0x01, 0x00, 0x03, 0x00, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38,
 0x39, 0x00, 0x03, 0x00, 0x02, 0x03, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39};
 int offset = 0;
 int length = data.length;
 x7875.parseFromLocalFileData(data, offset, length);
 long expectedUID = 1234567890L;
 long actualUID = x7875.getUID();
 assertEquals(expectedUID, actualUID);
 long expectedGID = 234567890123L;
 long actualGID = x7875.getGID();
 assertEquals(expectedGID, actualGID);
 }

 /**
 * Test for the parseFromLocalFileData() method with invalid data.
 */
 @Test(expected = ZipException.class)
 public void testParseFromLocalFileDataInvalid() throws ZipException {
 byte[] data = new byte[]{(byte) 0x78, (byte) 0x75, (byte) 0x01, (byte) 0x01};
 int offset = 0;
 int length = data.length;
 x7875.parseFromLocalFileData(data, offset, length);
 }

 /**
 * Test for the parseFromCentralDirectoryData() method.
 * This method is not implemented as this class does not store any data inside the central directory.
 */
 @Test
 public void testParseFromCentralDirectoryData() throws ZipException {
 byte[] buffer = new byte[0];
 int offset = 0;
 int length = buffer.length;
 x7875.parseFromCentralDirectoryData(buffer, offset, length);
 }
}