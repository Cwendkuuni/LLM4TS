package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.junit.Test;

public class X7875_NewUnixTest {

    @Test
    public void testNewUnix() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(1, x7875.getVersion());
        assertNull(x7875.getUID());
        assertNull(x7875.getGID());
    }

    @Test
    public void testSetUIDAndGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(123);
        x7875.setGID(456);
        assertEquals(123, x7875.getUID());
        assertEquals(456, x7875.getGID());
    }

    @Test
    public void testGetUIDAndGIDAsLong() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(123);
        x7875.setGID(456);
        assertEquals(123, x7875.getUID());
        assertEquals(456, x7875.getGID());

        long uid = 123456789;
        long gid = 987654321;
        x7875.setUID(uid);
        x7875.setGID(gid);
        assertEquals(uid, x7875.getUID());
        assertEquals(gid, x7875.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(123);
        x7875.setGID(456);

        int uidSize = x7875.getUID().toString().length();
        int gidSize = x7875.getGID().toString().length();

        ZipShort expectedLength = new ZipShort(3 + uidSize + gidSize);
        ZipShort actualLength = x7875.getLocalFileDataLength();

        assertEquals(expectedLength.getValue(), actualLength.getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(123);
        x7875.setGID(456);

        int uidSize = x7875.getUID().toString().length();
        int gidSize = x7875.getGID().toString().length();

        ZipShort expectedLength = new ZipShort(3 + uidSize + gidSize);
        ZipShort actualLength = x7875.getCentralDirectoryLength();

        assertEquals(expectedLength.getValue(), actualLength.getValue());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(123);
        x7875.setGID(456);

        byte[] expectedData = new byte[3 + x7875.getUID().toByteArray().length + x7875.getGID().toByteArray().length];

        int pos = 0;
        expectedData[pos++] = unsignedIntToSignedByte(x7875.getVersion());
        expectedData[pos++] = unsignedIntToSignedByte(x7875.getUID().toByteArray().length);
        System.arraycopy(x7875.getUID().toByteArray(), 0, expectedData, pos, x7875.getUID().toByteArray().length);
        pos += x7875.getUID().toByteArray().length;
        expectedData[pos++] = unsignedIntToSignedByte(x7875.getGID().toByteArray().length);
        System.arraycopy(x7875.getGID().toByteArray(), 0, expectedData, pos, x7875.getGID().toByteArray().length);

        byte[] actualData = x7875.getLocalFileDataData();

        assertEquals(expectedData.length, actualData.length);
        for (int i = 0; i < expectedData.length; i++) {
            assertEquals(expectedData[i], actualData[i]);
        }
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] data = new byte[]{-1, 1, 0, 0, 0, 0, 1, 0, 0, 2, 0, 0, 0, 0, 4, 5, 6};
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);

        assertEquals(1, x7875.getVersion());
        assertEquals(new BigInteger("1"), x7875.getUID());
        assertEquals(new BigInteger("2"), x7875.getGID());
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        // Not implemented
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataWithException() throws ZipException {
        byte[] data = new byte[0];
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromCentralDirectoryData(data, 0, data.length);
    }

    @Test
    public void testReset() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(123);
        x7875.setGID(456);

        x7875.reset();

        assertEquals(1, x7875.getVersion());
        assertNull(x7875.getUID());
        assertNull(x7875.getGID());
    }
}