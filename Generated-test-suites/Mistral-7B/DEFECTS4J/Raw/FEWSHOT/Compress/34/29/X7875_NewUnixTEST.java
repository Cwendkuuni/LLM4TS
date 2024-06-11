package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;

public class X7875_NewUnixTest {

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        ZipShort headerId = x7875.getHeaderId();
        assertEquals(0x7875, headerId.getValue());
    }

    @Test
    public void testGetUIDAndGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = 123456;
        long gid = 7891011;

        x7875.setUID(uid);
        x7875.setGID(gid);

        assertEquals(uid, x7875.getUID());
        assertEquals(gid, x7875.getGID());
    }

    @Test
    public void testSetUIDAndGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        long uid = 123456;
        long gid = 7891011;

        x7875.setUID(uid);
        x7875.setGID(gid);

        assertEquals(uid, x7875.getUID());
        assertEquals(gid, x7875.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        int uidSize = 5;
        int gidSize = 3;

        byte[] uidBytes = new byte[uidSize];
        byte[] gidBytes = new byte[gidSize];

        x7875.uid = new BigInteger(1, reverse(uidBytes));
        x7875.gid = new BigInteger(1, reverse(gidBytes));

        ZipShort length = x7875.getLocalFileDataLength();
        assertEquals(uidSize + gidSize + 3, length.getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        int uidSize = 5;
        int gidSize = 3;

        byte[] uidBytes = new byte[uidSize];
        byte[] gidBytes = new byte[gidSize];

        x7875.uid = new BigInteger(1, reverse(uidBytes));
        x7875.gid = new BigInteger(1, reverse(gidBytes));

        ZipShort length = x7875.getCentralDirectoryLength();
        assertEquals(length.getValue(), x7875.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        int uidSize = 5;
        int gidSize = 3;

        byte[] uidBytes = new byte[uidSize];
        byte[] gidBytes = new byte[gidSize];

        x7875.uid = new BigInteger(1, reverse(uidBytes));
        x7875.gid = new BigInteger(1, reverse(gidBytes));

        byte[] expectedData = new byte[uidSize + gidSize + 3];

        int pos = 0;
        expectedData[pos++] = unsignedIntToSignedByte(x7875.version);
        expectedData[pos++] = unsignedIntToSignedByte(uidSize);
        System.arraycopy(uidBytes, 0, expectedData, pos, uidSize);
        pos += uidSize;
        expectedData[pos++] = unsignedIntToSignedByte(gidSize);
        System.arraycopy(gidBytes, 0, expectedData, pos, gidSize);

        byte[] actualData = x7875.getLocalFileDataData();
        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testParseFromLocalFileData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        byte[] data = new byte[]{(byte) 0x01, (byte) 0x05, 0x00, 0x00, 0x00, 0x03, (byte) 0x01, (byte) 0x03, 0x00, 0x00, 0x00};

        x7875.parseFromLocalFileData(data, 0, data.length);

        assertEquals(1, x7875.version);
        assertEquals(5, x7875.uid.toByteArray().length);
        assertEquals(1, x7875.gid.toByteArray().length);

        byte[] uidBytes = x7875.uid.toByteArray();
        assertEquals(0x01, uidBytes[0]);
        assertEquals(0x61, uidBytes[1]);
        assertEquals(0x73, uidBytes[2]);
        assertEquals(0x73, uidBytes[3]);
        assertEquals(0x65, uidBytes[4]);

        byte[] gidBytes = x7875.gid.toByteArray();
        assertEquals(0x01, gidBytes[0]);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        byte[] data = new byte[]{(byte) 0x01, (byte) 0x05, 0x00, 0x00, 0x00, 0x03, 0x01, (byte) 0x04}; // invalid gid size

        x7875.parseFromLocalFileData(data, 0, data.length);
    }

    @Test
    public void testReset() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(123456);
        x7875.setGID(7891011);
        x7875.reset();

        assertEquals(1, x7875.version);
        assertNull(x7875.uid);
        assertNull(x7875.gid);
    }
}