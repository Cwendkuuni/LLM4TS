package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.zip.ZipShort;
import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.junit.Test;

/**
 * Test cases for X7875_NewUnix class.
 */
public class X7875_NewUnixTest {

    @Test
    public void testNewInstance() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertNotNull(x7875);
        assertEquals(1, x7875.getVersion());
        assertNull(x7875.getUID());
        assertNull(x7875.getGID());
    }

    @Test
    public void testSetAndGetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = 1234L;
        x7875.setUID(uid);
        assertEquals(uid, x7875.getUID());
    }

    @Test
    public void testSetAndGetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long gid = 5678L;
        x7875.setGID(gid);
        assertEquals(gid, x7875.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        int uidSize = 2; // for UID "1234"
        int gidSize = 2; // for GID "5678"

        x7875.setUID(1234);
        x7875.setGID(5678);

        ZipShort expectedLength = new ZipShort((short) (3 + uidSize + gidSize));
        assertEquals(expectedLength, x7875.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        int uidSize = 2; // for UID "1234"
        int gidSize = 2; // for GID "5678"

        x7875.setUID(1234);
        x7875.setGID(5678);

        ZipShort expectedLength = x7875.getLocalFileDataLength();
        assertEquals(expectedLength, x7875.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        int uidSize = 2; // for UID "1234"
        int gidSize = 2; // for GID "5678"

        long uid = 1234;
        long gid = 5678;
        x7875.setUID(uid);
        x7875.setGID(gid);

        byte[] expectedData = new byte[3 + uidSize + gidSize];
        expectedData[0] = (byte) unsignedIntToSignedByte(x7875.getVersion());
        expectedData[1] = (byte) unsignedIntToSignedByte(uidSize);
        System.arraycopy(uidToByteArray(uid), 0, expectedData, 2, uidSize);
        expectedData[2 + uidSize] = (byte) unsignedIntToSignedByte(gidSize);
        System.arraycopy(gidToByteArray(gid), 0, expectedData, 3 + uidSize, gidSize);

        byte[] actualData = x7875.getLocalFileDataData();
        assertArrayEquals(expectedData, actualData);
    }

    @Test(expected = ZipException.class)
    public void testParseFromInvalidLocalFileData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        byte[] data = new byte[]{-1, 1, 0};
        x7875.parseFromLocalFileData(data, 0, data.length);
    }

    @Test
    public void testParseFromValidLocalFileData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        int uidSize = 2; // for UID "1234"
        int gidSize = 2; // for GID "5678"

        long uid = 1234;
        long gid = 5678;

        byte[] data = new byte[]{1, (byte) unsignedIntToSignedByte(uidSize),
                (byte) unsignedIntToSignedByte(uidSize),
                (byte) unsignedIntToSignedByte(gidSize),
                (byte) unsignedIntToSignedByte(gidSize),
                (byte) unsignedIntToSignedByte(uidSize), 0, 0,
                (byte) unsignedIntToSignedByte(gidSize), 0, 0};

        x7875.parseFromLocalFileData(data, 0, data.length);
        assertEquals(uid, x7875.getUID());
        assertEquals(gid, x7875.getGID());
    }

    private static byte[] uidToByteArray(long uid) {
        byte[] bytes = new BigInteger(uid.toString()).toByteArray();
        return reverse(bytes);
    }

    private static byte[] gidToByteArray(long gid) {
        byte[] bytes = new BigInteger(gid.toString()).toByteArray();
        return reverse(bytes);
    }

    private static byte[] reverse(byte[] data) {
        for (int i = 0; i < data.length / 2; i++) {
            byte temp = data[i];
            data[i] = data[data.length - i - 1];
            data[data.length - i - 1] = temp;
        }
        return data;
    }
}