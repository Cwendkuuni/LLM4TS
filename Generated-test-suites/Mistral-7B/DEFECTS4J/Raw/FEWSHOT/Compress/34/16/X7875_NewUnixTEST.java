package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import org.junit.Test;

public class X7875_NewUnixTest {

    @Test
    public void testX7875_NewUnix_DefaultConstructor() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        assertNull(x7875NewUnix.getUID());
        assertNull(x7875NewUnix.getGID());
        assertEquals(1, x7875NewUnix.getVersion());
    }

    @Test
    public void testX7875_NewUnix_SetUIDAndGetUID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long uid = 12345;
        x7875NewUnix.setUID(uid);
        assertEquals(uid, x7875NewUnix.getUID());
    }

    @Test
    public void testX7875_NewUnix_SetGIDAndGetGID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long gid = 54321;
        x7875NewUnix.setGID(gid);
        assertEquals(gid, x7875NewUnix.getGID());
    }

    @Test
    public void testX7875_NewUnix_GetLocalFileDataLength() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        int uidSize = 2;
        int gidSize = 3;
        x7875NewUnix.setUID(12345);
        x7875NewUnix.setGID(54321);
        ZipShort expectedLength = new ZipShort((short) (3 + uidSize + gidSize));
        ZipShort actualLength = x7875NewUnix.getLocalFileDataLength();
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testX7875_NewUnix_GetCentralDirectoryLength() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        int uidSize = 2;
        int gidSize = 3;
        x7875NewUnix.setUID(12345);
        x7875NewUnix.setGID(54321);
        ZipShort expectedLength = new ZipShort((short) (3 + uidSize + gidSize));
        ZipShort actualLength = x7875NewUnix.getCentralDirectoryLength();
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testX7875_NewUnix_GetLocalFileDataData() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long uid = 12345;
        long gid = 54321;
        x7875NewUnix.setUID(uid);
        x7875NewUnix.setGID(gid);
        byte[] expectedData = new byte[6 + uid.toString().getBytes().length + gid.toString().getBytes().length];
        x7875NewUnix.parseFromLocalFileData(expectedData, 0, expectedData.length);

        byte[] actualData = x7875NewUnix.getLocalFileDataData();
        assertArrayEquals(expectedData, actualData);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testX7875_NewUnix_GetCentralDirectoryData() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.getCentralDirectoryData();
    }

    @Test
    public void testX7875_NewUnix_ParseFromLocalFileData() throws ZipException {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        byte[] data = new byte[12];
        data[0] = (byte) 0x78;
        data[1] = (byte) 0x75;
        data[2] = (byte) 0x01;
        data[3] = (byte) 0x02;
        System.arraycopy(new BigInteger(12345).toByteArray(), 0, data, 4, 4);
        System.arraycopy(new BigInteger(54321).toByteArray(), 0, data, 8, 4);

        x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1, x7875NewUnix.getVersion());
        assertEquals(12345, x7875NewUnix.getUID());
        assertEquals(54321, x7875NewUnix.getGID());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testX7875_NewUnix_ParseFromCentralDirectoryData() throws ZipException {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        byte[] buffer = new byte[10];
        x7875NewUnix.parseFromCentralDirectoryData(buffer, 0, buffer.length);
    }
}