package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;
import org.junit.Test;

public class X7875_NewUnixTest {

    @Test
    public void testX7875_NewUnix_Constructor() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        assertNull(x7875NewUnix.getUID());
        assertNull(x7875NewUnix.getGID());
        assertEquals(1, x7875NewUnix.getVersion());
    }

    @Test
    public void testX7875_NewUnix_SetUID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long uid = 12345678L;
        x7875NewUnix.setUID(uid);
        assertEquals(uid, x7875NewUnix.getUID());
    }

    @Test
    public void testX7875_NewUnix_SetGID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long gid = 23456789L;
        x7875NewUnix.setGID(gid);
        assertEquals(gid, x7875NewUnix.getGID());
    }

    @Test
    public void testX7875_NewUnix_getLocalFileDataLength() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        int uidSize = Integer.BYTES;
        int gidSize = Integer.BYTES;
        int length = uidSize + gidSize + Integer.BYTES;
        ZipShort expectedLength = new ZipShort((short) length);
        ZipShort actualLength = x7875NewUnix.getLocalFileDataLength();
        assertEquals(expectedLength.getValue(), actualLength.getValue());
    }

    @Test
    public void testX7875_NewUnix_getCentralDirectoryLength() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        int length = x7875NewUnix.getLocalFileDataLength().getValue();
        ZipShort expectedLength = new ZipShort((short) length);
        ZipShort actualLength = x7875NewUnix.getCentralDirectoryLength();
        assertEquals(expectedLength.getValue(), actualLength.getValue());
    }

    @Test
    public void testX7875_NewUnix_getLocalFileDataData() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long uid = 12345678L;
        long gid = 23456789L;
        x7875NewUnix.setUID(uid);
        x7875NewUnix.setGID(gid);
        byte[] expectedData = new byte[3 + Integer.BYTES + Integer.BYTES];
        expectedData[0] = (byte) 0x78;
        expectedData[1] = (byte) 0x75;
        expectedData[2] = (byte) 0x01;
        int uidSize = Integer.BYTES;
        int gidSize = Integer.BYTES;
        int versionSize = Integer.BYTES;
        byte[] uidBytes = Long.toByteArray(uid);
        byte[] gidBytes = Long.toByteArray(gid);
        int pos = 3;
        System.arraycopy(new byte[]{ (byte) 0x01 }, 0, expectedData, pos, uidSize);
        pos += uidSize;
        System.arraycopy(uidBytes, 0, expectedData, pos, uidBytes.length);
        pos += uidBytes.length;
        System.arraycopy(new byte[]{ (byte) 0x01 }, 0, expectedData, pos, gidSize);
        pos += gidSize;
        System.arraycopy(gidBytes, 0, expectedData, pos, gidBytes.length);
        byte[] actualData = x7875NewUnix.getLocalFileDataData();
        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testX7875_NewUnix_parseFromLocalFileData() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        byte[] data = new byte[6 + Integer.BYTES + Integer.BYTES];
        data[0] = (byte) 0x78;
        data[1] = (byte) 0x75;
        int version = 1;
        data[2] = (byte) unsignedIntToSignedByte(version);
        int uidSize = Integer.BYTES;
        int gidSize = Integer.BYTES;
        long uid = 12345678L;
        long gid = 23456789L;
        byte[] uidBytes = Long.toByteArray(uid);
        byte[] gidBytes = Long.toByteArray(gid);
        System.arraycopy(new byte[]{ (byte) uidSize }, 3, data, 3, Integer.BYTES);
        System.arraycopy(uidBytes, 0, data, 4, uidBytes.length);
        System.arraycopy(new byte[]{ (byte) gidSize }, data.length - Integer.BYTES - 1, data, data.length - Integer.BYTES, Integer.BYTES);
        System.arraycopy(gidBytes, 0, data, data.length - Integer.BYTES - gidBytes.length, gidBytes.length);
        x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
        assertEquals(version, x7875NewUnix.getVersion());
        assertEquals(uid, x7875NewUnix.getUID());
        assertEquals(gid, x7875NewUnix.getGID());
    }

    @Test(expected = ZipException.class)
    public void testX7875_NewUnix_parseFromCentralDirectoryData() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        byte[] data = new byte[0];
        x7875NewUnix.parseFromCentralDirectoryData(data, 0, data.length);
    }
}