package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.utils.ZipShort;

public class X7875_NewUnixTest {

    @Test
    public void testNewInstance() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(1, x7875.getVersion());
        assertNull(x7875.getUID());
        assertNull(x7875.getGID());
    }

    @Test
    public void testSetGetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = 123456789;
        x7875.setUID(uid);
        assertEquals(uid, x7875.getUID());
    }

    @Test
    public void testSetGetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long gid = 987654321;
        x7875.setGID(gid);
        assertEquals(gid, x7875.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(123456789);
        x7875.setGID(987654321);

        int uidSize = Integer.BYTES;
        int gidSize = Integer.BYTES;

        byte[] uidBytes = Long.toByteArray(x7875.getUID());
        byte[] gidBytes = Long.toByteArray(x7875.getGID());

        int expectedLength = 3 + uidSize + gidSize;
        ZipShort length = x7875.getLocalFileDataLength();
        assertEquals(expectedLength, length.getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(123456789);
        x7875.setGID(987654321);

        int uidSize = Integer.BYTES;
        int gidSize = Integer.BYTES;

        byte[] uidBytes = Long.toByteArray(x7875.getUID());
        byte[] gidBytes = Long.toByteArray(x7875.getGID());

        int expectedLength = 3 + uidSize + gidSize;
        ZipShort length = x7875.getCentralDirectoryLength();
        assertEquals(expectedLength, length.getValue());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] data = new byte[]{-1, (byte) 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (byte) 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);

        assertEquals(1, x7875.getVersion());
        assertEquals(new BigInteger("123456789"), x7875.getUID());
        assertEquals(new BigInteger("987654321"), x7875.getGID());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidData() throws ZipException {
        byte[] data = new byte[]{};

        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);
    }
}