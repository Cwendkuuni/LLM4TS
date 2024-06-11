package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.utils.ZipShort;

import java.math.BigInteger;

import org.junit.Test;

public class X7875_NewUnixTest {

    @Test
    public void testInit() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(1, x7875.getVersion());
        assertNull(x7875.getUID());
        assertNull(x7875.getGID());
        assertEquals(new ZipShort(0x7875), x7875.getHeaderId());
    }

    @Test
    public void testSetGetUid() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = 123456L;
        x7875.setUID(uid);
        assertEquals(uid, x7875.getUID());
    }

    @Test
    public void testSetGetGid() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long gid = 123456L;
        x7875.setGID(gid);
        assertEquals(gid, x7875.getGID());
    }

    @Test
    public void testLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = 123456L;
        long gid = 78910L;
        x7875.setUID(uid);
        x7875.setGID(gid);

        byte[] uidBytes = uid.toByteArray();
        byte[] gidBytes = gid.toByteArray();

        int uidSize = trimLeadingZeroesForceMinLength(uidBytes).length;
        int gidSize = trimLeadingZeroesForceMinLength(gidBytes).length;

        ZipShort length = x7875.getLocalFileDataLength();
        assertEquals(new ZipShort(3 + uidSize + gidSize), length);
    }

    @Test
    public void testCentralDirectoryLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = 123456L;
        long gid = 78910L;
        x7875.setUID(uid);
        x7875.setGID(gid);

        byte[] uidBytes = uid.toByteArray();
        byte[] gidBytes = gid.toByteArray();

        int uidSize = trimLeadingZeroesForceMinLength(uidBytes).length;
        int gidSize = trimLeadingZeroesForceMinLength(gidBytes).length;

        ZipShort length = x7875.getCentralDirectoryLength();
        assertEquals(length, x7875.getLocalFileDataLength());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        byte[] data = new byte[]{-1, (byte) 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 1, 0, 1, 0, 2, 0};

        int offset = 0;
        x7875.parseFromLocalFileData(data, offset, data.length);

        assertEquals(1, x7875.getVersion());
        assertEquals(new BigInteger("1", new byte[]{1, 0, 1, 0}), x7875.getUID());
        assertEquals(new BigInteger("2", new byte[]{1, 0, 1, 0, 2, 0}), x7875.getGID());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidData() throws ZipException {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        byte[] data = new byte[]{-1, (byte) 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 1, 0, 1, 0, 2, 1};

        int offset = 0;
        x7875.parseFromLocalFileData(data, offset, data.length);
    }

    @Test
    public void testToString() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(new BigInteger("1234567890123456789012345678901234567890"));
        x7875.setGID(new BigInteger("7891011111111111111111111111111111111111"));

        assertEquals("0x7875 Zip Extra Field: UID=1234567890123456789012345678901234567890 GID=7891011111111111111111111111111111111111", x7875.toString());
    }

    private static int trimLeadingZeroesForceMinLength(byte[] array) {
        int pos = 0;
        for (byte b : array) {
            if (b != 0) {
                break;
            }
            pos++;
        }

        return Math.max(1, array.length - pos);
    }
}