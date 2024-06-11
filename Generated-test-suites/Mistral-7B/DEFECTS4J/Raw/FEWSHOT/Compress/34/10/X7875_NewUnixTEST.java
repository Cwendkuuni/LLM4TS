package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.utils.ZipShort;

import java.math.BigInteger;

import org.junit.Test;

/**
 * Test cases for X7875_NewUnix class
 */
public class X7875_NewUnixTest {

    @Test
    public void testCreateAndGetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = 12345;
        x7875.setUID(uid);
        assertEquals(uid, x7875.getUID());
    }

    @Test
    public void testCreateAndGetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long gid = 67890;
        x7875.setGID(gid);
        assertEquals(gid, x7875.getGID());
    }

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        ZipShort headerId = x7875.getHeaderId();
        assertEquals(0x7875, headerId.getValue().intValue());
    }

    @Test
    public void testLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        // Set UID and GID to their byte representation with leading zeros
        x7875.setUID(12345);
        x7875.setGID(67890);

        int uidSize = trimLeadingZeroesForceMinLength(x7875.uid.toByteArray()).length;
        int gidSize = trimLeadingZeroesForceMinLength(x7875.gid.toByteArray()).length;

        ZipShort localFileDataLength = x7875.getLocalFileDataLength();
        assertEquals(new ZipShort((short) (3 + uidSize + gidSize)), localFileDataLength);
    }

    @Test
    public void testCentralDirectoryLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        // Set UID and GID to their byte representation with leading zeros
        x7875.setUID(12345);
        x7875.setGID(67890);

        int uidSize = trimLeadingZeroesForceMinLength(x7875.uid.toByteArray()).length;
        int gidSize = trimLeadingZeroesForceMinLength(x7875.gid.toByteArray()).length;

        ZipShort centralDirectoryLength = x7875.getCentralDirectoryLength();
        assertEquals(new ZipShort((short) (3 + uidSize + gidSize)), centralDirectoryLength);
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        // Set UID and GID to their byte representation with leading zeros
        x7875.setUID(12345);
        x7875.setGID(67890);

        byte[] uidBytes = x7875.uid.toByteArray();
        byte[] gidBytes = x7875.gid.toByteArray();

        // Remove leading zeros
        uidBytes = trimLeadingZeroesForceMinLength(uidBytes);
        gidBytes = trimLeadingZeroesForceMinLength(gidBytes);

        byte[] data = x7875.getLocalFileDataData();
        assertArrayEquals(new byte[] { 1, 1, uidBytes[0], uidBytes[1], uidBytes[2], uidBytes[3], gidBytes[0], gidBytes[1], gidBytes[2], gidBytes[3] }, data);
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] data = new byte[] { 1, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 };

        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);

        assertEquals(1, x7875.version);
        assertEquals(new BigInteger("12345"), x7875.uid);
        assertEquals(new BigInteger("67890"), x7875.gid);
    }

    /**
     * Test parsing from invalid data
     */
    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidData() throws ZipException {
        byte[] data = new byte[] { 1, 1, (byte) 0xFF, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 };

        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);
    }

    @Test
    public void testReset() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(new byte[] { 1, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 }, 0, 21);
        x7875.reset();
        assertNull(x7875.uid);
        assertNull(x7875.gid);
    }

    /**
     * Helper method to trim leading zeros from a byte array
     */
    private byte[] trimLeadingZeroesForceMinLength(byte[] array) {
        if (array == null) {
            return array;
        }

        int pos = 0;
        for (byte b : array) {
            if (b != 0) {
                break;
            }
            pos++;
        }

        byte[] trimmedArray = new byte[Math.max(1, array.length - pos)];
        int startPos = trimmedArray.length - (array.length - pos);
        System.arraycopy(array, pos, trimmedArray, startPos, trimmedArray.length - startPos);
        return trimmedArray;
    }
}