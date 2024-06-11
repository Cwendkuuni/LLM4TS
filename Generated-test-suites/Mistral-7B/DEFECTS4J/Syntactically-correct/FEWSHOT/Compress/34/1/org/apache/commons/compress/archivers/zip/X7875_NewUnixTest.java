package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.zip.ZipShort;
import org.apache.commons.compress.archivers.zip.X7875_NewUnix;

public class X7875_NewUnixTest {

    @Test
    public void testNewInstance() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertNotNull(x7875);
    }

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        ZipShort headerId = x7875.getHeaderId();
        assertEquals(HEADER_ID, headerId);
    }

    @Test
    public void testGetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = 1234567890L;
        x7875.setUID(uid);
        long actualUid = x7875.getUID();
        assertEquals(uid, actualUid);
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long gid = 9876543210L;
        x7875.setGID(gid);
        long actualGid = x7875.getGID();
        assertEquals(gid, actualGid);
    }

    @Test
    public void testSetUIDAndGetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = 1234567890L;
        x7875.setUID(uid);
        long actualUid = x7875.getUID();
        assertEquals(uid, actualUid);
    }

    @Test
    public void testSetGIDAndGetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long gid = 9876543210L;
        x7875.setGID(gid);
        long actualGid = x7875.getGID();
        assertEquals(gid, actualGid);
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        int uidSize = 5;
        int gidSize = 3;

        x7875.setUID(BigInteger.valueOf(1234567890L));
        x7875.setGID(BigInteger.valueOf(9876543210L));

        ZipShort localFileDataLength = x7875.getLocalFileDataLength();
        int expectedLocalFileDataLength = 3 + uidSize + gidSize;
        assertEquals(expectedLocalFileDataLength, localFileDataLength.getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        int uidSize = 5;
        int gidSize = 3;

        x7875.setUID(BigInteger.valueOf(1234567890L));
        x7875.setGID(BigInteger.valueOf(9876543210L));

        ZipShort centralDirectoryLength = x7875.getCentralDirectoryLength();
        int expectedCentralDirectoryLength = x7875.getLocalFileDataLength().getValue();
        assertEquals(expectedCentralDirectoryLength, centralDirectoryLength.getValue());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        int uidSize = 5;
        int gidSize = 3;

        // Set UID and GID to test byte array lengths
        BigInteger uid = BigInteger.valueOf(1234567890L);
        BigInteger gid = BigInteger.valueOf(9876543210L);
        x7875.setUID(uid);
        x7875.setGID(gid);

        byte[] localFileDataData = x7875.getLocalFileDataData();

        int versionByte = x7875.getVersion();
        int uidSizeByte = (byte) (uidSize + 1);
        int gidSizeByte = (byte) (gidSize + 1);

        byte[] expectedLocalFileDataData = new byte[uidSize + gidSize + 3];
        expectedLocalFileDataData[0] = (byte) versionByte;
        expectedLocalFileDataData[1] = uidSizeByte;
        System.arraycopy(uid.toByteArray(), 0, expectedLocalFileDataData, 2, uidSize);
        expectedLocalFileDataData[2 + uidSize] = gidSizeByte;
        System.arraycopy(gid.toByteArray(), 0, expectedLocalFileDataData, 3 + uidSize, gidSize);

        byte[] trimmedUid = X7875_NewUnix.trimLeadingZeroesForceMinLength(uid.toByteArray());
        byte[] trimmedGid = X7875_NewUnix.trimLeadingZeroesForceMinLength(gid.toByteArray());

        // reverse the byte arrays
        for (int i = 0; i < trimmedUid.length / 2; i++) {
            byte temp = trimmedUid[i];
            trimmedUid[i] = trimmedUid[trimmedUid.length - i - 1];
            trimmedUid[trimmedUid.length - i - 1] = temp;
        }

        for (int i = 0; i < trimmedGid.length / 2; i++) {
            byte temp = trimmedGid[i];
            trimmedGid[i] = trimmedGid[trimmedGid.length - i - 1];
            trimmedGid[trimmedGid.length - i - 1] = temp;
        }

        // system arraycopy reversed byte arrays back to original byte array
        System.arraycopy(trimmedUid, 0, expectedLocalFileDataData, 2, uidSize);
        System.arraycopy(trimmedGid, 0, expectedLocalFileDataData, 3 + uidSize, gidSize);

        // Compare the expected and actual byte arrays
        assertArrayEquals(expectedLocalFileDataData, localFileDataData);
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] data = new byte[12];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (i % 256);
        }

        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);

        int expectedVersion = 1;
        int uidSize = 5;
        int gidSize = 3;

        byte[] uidBytes = new byte[uidSize];
        System.arraycopy(data, 1, uidBytes, 0, uidSize);

        byte[] gidBytes = new byte[gidSize];
        System.arraycopy(data, 1 + uidSize, gidBytes, 0, gidSize);

        BigInteger uid = new BigInteger(1, uidBytes);
        BigInteger gid = new BigInteger(1, gidBytes);

        assertEquals(expectedVersion, x7875.getVersion());
        assertEquals(uid, x7875.getUID());
        assertEquals(gid, x7875.getGID());
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryData() throws ZipException {
        byte[] data = new byte[0];
        new X7875_NewUnix().parseFromCentralDirectoryData(data, 0, data.length);
        fail("Expected ZipException to be thrown");
    }

    @Test
    public void testReset() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(BigInteger.valueOf(1234567890L));
        x7875.setGID(BigInteger.valueOf(9876543210L));
        x7875.reset();

        long uid = x7875.getUID();
        long gid = x7875.getGID();
        assertEquals(ONE_THOUSAND.longValue(), uid);
        assertEquals(ONE_THOUSAND.longValue(), gid);
    }
}