package org.apache.commons.compress.archivers.zip; // adjust the package name if necessary

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        ZipShort expectedHeaderId = new ZipShort(0x7875);
        ZipShort actualHeaderId = x7875.getHeaderId();

        assertEquals("Unexpected header ID.", expectedHeaderId, actualHeaderId);
    }

    @Test
    public void testGetUIDAndSetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        long uid = 12345L;
        x7875.setUID(uid);
        long actualUid = x7875.getUID();

        assertEquals("Unexpected UID after setting it.", uid, actualUid);

        // Check if UID can be set to a negative value
        x7875.setUID(-1);
        actualUid = x7875.getUID();
        assertEquals("Unexpected UID after setting it to a negative value.", 1L, actualUid);
    }

    @Test
    public void testGetGIDAndSetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        long gid = 67890L;
        x7875.setGID(gid);
        long actualGid = x7875.getGID();

        assertEquals("Unexpected GID after setting it.", gid, actualGid);

        // Check if GID can be set to a negative value
        x7875.setGID(-1);
        actualGid = x7875.getGID();
        assertEquals("Unexpected GID after setting it to a negative value.", 1L, actualGid);
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        int uidSize = 3;
        int gidSize = 4;
        x7875.uid = BigInteger.valueOf(12345);
        x7875.gid = BigInteger.valueOf(67890);

        ZipShort expectedLength = new ZipShort(3 + uidSize + gidSize);
        ZipShort actualLength = x7875.getLocalFileDataLength();

        assertEquals("Unexpected length of extra field data.", expectedLength, actualLength);
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        int uidSize = 3;
        int gidSize = 4;
        x7875.uid = BigInteger.valueOf(12345);
        x7875.gid = BigInteger.valueOf(67890);

        ZipShort expectedLength = new ZipShort(3 + uidSize + gidSize);
        ZipShort actualLength = x7875.getCentralDirectoryLength();

        assertEquals("Unexpected length of extra field data in central directory.", expectedLength, actualLength);
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        X7875_NewUnix expectedX7875 = new X7875_NewUnix();
        expectedX7875.setUID(12345L);
        expectedX7875.setGID(67890L);

        byte[] data = getTestDataForParseFromLocalFileData();

        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);

        assertEquals("Unexpected UID after parsing.", expectedX7875.getUID(), x7875.getUID());
        assertEquals("Unexpected GID after parsing.", expectedX7875.getGID(), x7875.getGID());
        assertEquals("Unexpected version after parsing.", expectedX7875.getVersion(), x7875.getVersion());
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        // This test case is not applicable since parseFromCentralDirectoryData method does not do anything.
        // It is marked as a "package-private" method, and it's intended for internal usage only.
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidData() throws ZipException {
        byte[] data = getTestDataForParseFromLocalFileDataWithInvalidData();

        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);

        // The test is expected to fail with a ZipException since the data provided is invalid.
    }

    private byte[] getTestDataForParseFromLocalFileData() {
        byte[] versionByte = new byte[]{(byte) 1};
        byte[] uidSizeByte = new byte[]{3};
        byte[] uidBytes = new byte[]{0, 0, 0, 1, 2, 3, 4, 5};
        byte[] gidSizeByte = new byte[]{4};
        byte[] gidBytes = new byte[]{6, 7, 8, 9, 10, 11, 12, 13, 14, 15};

        byte[] data = new byte[uidBytes.length + gidBytes.length + versionByte.length + uidSizeByte.length + gidSizeByte.length];
        System.arraycopy(versionByte, 0, data, 0, versionByte.length);
        System.arraycopy(uidSizeByte, 0, data, uidSizeByte.length, 1);
        System.arraycopy(uidBytes, 0, data, uidSizeByte.length + versionByte.length, uidBytes.length);
        System.arraycopy(gidSizeByte, 0, data, uidBytes.length + versionByte.length + uidSizeByte.length, 1);
        System.arraycopy(gidBytes, 0, data, uidBytes.length + versionByte.length + uidSizeByte.length + gidSizeByte.length, gidBytes.length);

        return data;
    }

    private byte[] getTestDataForParseFromLocalFileDataWithInvalidData() {
        byte[] data = new byte[]{0x78}; // Invalid header ID

        return data;
    }
}