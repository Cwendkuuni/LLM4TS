package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;

public class X7875_NewUnixTest {

    @Test
    public void testNewInstance() {
        X7875_NewUnix testObject = new X7875_NewUnix();
        assertEquals(1, testObject.getVersion());
        assertNull(testObject.getUID());
        assertNull(testObject.getGID());
    }

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix testObject = new X7875_NewUnix();
        ZipShort expected = new ZipShort(0x7875);
        ZipShort actual = testObject.getHeaderId();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetUID() {
        X7875_NewUnix testObject = new X7875_NewUnix();
        testObject.setUID(12345);
        long expected = 12345;
        long actual = testObject.getUID();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetUID() {
        X7875_NewUnix testObject = new X7875_NewUnix();
        BigInteger uid = new BigInteger("12345");
        testObject.setUID(uid.longValue());
        BigInteger actualUid = testObject.getUID();
        assertEquals(uid, actualUid);
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix testObject = new X7875_NewUnix();
        testObject.setGID(54321);
        long expected = 54321;
        long actual = testObject.getGID();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetGID() {
        X7875_NewUnix testObject = new X7875_NewUnix();
        BigInteger gid = new BigInteger("54321");
        testObject.setGID(gid.longValue());
        BigInteger actualGid = testObject.getGID();
        assertEquals(gid, actualGid);
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix testObject = new X7875_NewUnix();
        testObject.setUID(123);
        testObject.setGID(456);

        int expectedUidSize = 3; // 1 byte for version, 1 byte for UID size, 1 byte for UID
        int expectedGidSize = 3; // 1 byte for version, 1 byte for GID size, 1 byte for GID
        int totalExtraFieldDataLength = expectedUidSize + expectedGidSize;

        ZipShort actualLength = testObject.getLocalFileDataLength();
        assertEquals(new ZipShort(totalExtraFieldDataLength), actualLength);
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix testObject = new X7875_NewUnix();
        testObject.setUID(123);
        testObject.setGID(456);

        int expectedLength = testObject.getLocalFileDataLength().getValue();
        ZipShort actualLength = testObject.getCentralDirectoryLength();
        assertEquals(new ZipShort(expectedLength), actualLength);
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix testObject = new X7875_NewUnix();
        testObject.setUID(123);
        testObject.setGID(456);

        byte[] expectedUidBytes = new byte[]{0, 0, 0, 123};
        byte[] expectedGidBytes = new byte[]{0, 0, 0, 5, 0, 6};

        byte[] actualBytes = testObject.getLocalFileDataData();

        int versionBytePosition = 0;
        int uidSizeBytePosition = 1;
        int gidSizeBytePosition = 2;

        assertEquals(testObject.getVersion(), unsignedIntToSignedByte(actualBytes[versionBytePosition]));
        assertEquals(new ZipShort(expectedUidSize + expectedGidSize), new ZipShort(actualBytes, uidSizeBytePosition, 1));

        int uidBytePosition = uidSizeBytePosition + 1;
        int gidBytePosition = uidBytePosition + expectedUidSize;

        assertArrayEquals(expectedUidBytes, Arrays.copyOfRange(actualBytes, uidBytePosition, uidBytePosition + expectedUidSize));
        assertArrayEquals(expectedGidBytes, Arrays.copyOfRange(actualBytes, gidBytePosition, gidBytePosition + expectedGidSize));
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] testData = new byte[]{1, 3, 0, 0, 1, 2, 3, 0, 5, 0, 6};
        X7875_NewUnix testObject = new X7875_NewUnix();

        testObject.parseFromLocalFileData(testData, 0, testData.length);

        assertEquals(1, testObject.getVersion());
        assertEquals(new BigInteger("1"), testObject.getUID());
        assertEquals(new BigInteger("5"), testObject.getGID());
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        X7875_NewUnix testObject = new X7875_NewUnix();
        // Empty central directory data
        testObject.parseFromCentralDirectoryData(new byte[0], 0, 0);
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataInvalidData() throws ZipException {
        X7875_NewUnix testObject = new X7875_NewUnix();
        byte[] invalidData = new byte[]{1, 2, 3, 4, 5};
        testObject.parseFromCentralDirectoryData(invalidData, 0, invalidData.length);
    }
}