package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;

public class X7875_NewUnixTest {

    @Test
    public void testNewInstance() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(X7875_NewUnix.HEADER_ID, x7875.getHeaderId());
        assertNull(x7875.getUID());
        assertNull(x7875.getGID());
    }

    @Test
    public void testSetAndGetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = 1234567890L;
        x7875.setUID(uid);
        assertEquals(uid, x7875.getUID());
    }

    @Test
    public void testSetAndGetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long gid = 9876543210L;
        x7875.setGID(gid);
        assertEquals(gid, x7875.getGID());
    }

    @Test
    public void testLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = 1234567890L;
        long gid = 9876543210L;
        x7875.setUID(uid);
        x7875.setGID(gid);

        int uidSize = Long.BYTES;
        int gidSize = Long.BYTES;
        int totalDataSize = uidSize + gidSize + 3; // version=1 + uidsize=1 + gidsize=1

        ZipShort expectedLocalFileDataLength = new ZipShort((short) totalDataSize);
        assertEquals(expectedLocalFileDataLength, x7875.getLocalFileDataLength());
    }

    @Test
    public void testCentralDirectoryLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = 1234567890L;
        long gid = 9876543210L;
        x7875.setUID(uid);
        x7875.setGID(gid);

        int uidSize = Long.BYTES;
        int gidSize = Long.BYTES;
        int totalDataSize = uidSize + gidSize + 3; // version=1 + uidsize=1 + gidsize=1

        ZipShort expectedCentralDirectoryLength = new ZipShort((short) 0);
        assertEquals(expectedCentralDirectoryLength, x7875.getCentralDirectoryLength());
    }

    @Test
    public void testLocalFileDataData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = 1234567890L;
        long gid = 9876543210L;
        x7875.setUID(uid);
        x7875.setGID(gid);

        byte[] expectedData = new byte[3 + Long.BYTES + Long.BYTES];

        // version=1
        expectedData[0] = (byte) 0x01;

        // uidsize=x (x is the number of bytes in uid)
        int uidSize = Long.BYTES;
        expectedData[1] = (byte) (uidSize + 1);
        System.arraycopy(ZipUtil.longToByteArray(uid), 0, expectedData, 2, uidSize);

        // gidsize=y (y is the number of bytes in gid)
        int gidSize = Long.BYTES;
        expectedData[2 + uidSize] = (byte) (gidSize + 1);
        System.arraycopy(ZipUtil.longToByteArray(gid), 0, expectedData, 2 + uidSize + 1, gidSize);

        byte[] actualData = x7875.getLocalFileDataData();
        assertArrayEquals(expectedData, actualData);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidData() {
        byte[] data = new byte[]{-1, 1, 2, 3, 4, 5};
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);
        fail("Expected ZipException");
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] data = new byte[]{0x01, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0};
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);

        assertEquals(1, x7875.getVersion());
        assertEquals(0, x7875.getUID().signum());
        assertEquals(0, x7875.getGID().signum());
    }

    @Test
    public void testEquals() {
        X7875_NewUnix x78751 = new X7875_NewUnix();
        X7875_NewUnix x78752 = new X7875_NewUnix();
        X7875_NewUnix x78753 = new X7875_NewUnix();
        x78751.setUID(1234567890L);
        x78751.setGID(9876543210L);
        x78752.setUID(1234567890L);
        x78752.setGID(9876543210L);
        x78753.setUID(1234567891L);
        x78753.setGID(9876543210L);

        assertTrue(x78751.equals(x78752));
        assertFalse(x78751.equals(x78753));
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix x78751 = new X7875_NewUnix();
        X7875_NewUnix x78752 = new X7875_NewUnix();
        X7875_NewUnix x78753 = new X7875_NewUnix();
        x78751.setUID(1234567890L);
        x78751.setGID(9876543210L);
        x78752.setUID(1234567890L);
        x78752.setGID(9876543210L);
        x78753.setUID(1234567891L);
        x78753.setGID(9876543210L);

        int expectedHashCode1 = -1234567
                * (1 + (int) (x78751.getUID().longValue() >>> 32)
                + (int) (x78751.getUID().longValue() & 0xFFFFFFFF)
                + (int) (x78751.getGID().longValue() >>> 32)
                + (int) (x78751.getGID().longValue() & 0xFFFFFFFF));
        int expectedHashCode2 = expectedHashCode1;
        int expectedHashCode3 = -1234567
                * (1 + (int) (x78753.getUID().longValue() >>> 32)
                + (int) (x78753.getUID().longValue() & 0xFFFFFFFF)
                + (int) (x78753.getGID().longValue() >>> 32)
                + (int) (x78753.getGID().longValue() & 0xFFFFFFFF));

        assertEquals(expectedHashCode1, x78751.hashCode());
        assertEquals(expectedHashCode2, x78752.hashCode());
        assertEquals(expectedHashCode3, x78753.hashCode());
    }
}