package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the X7875_NewUnix class.
 */
public class X7875_NewUnixTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(1, x7875.getVersion());
        assertNull(x7875.getUID());
        assertNull(x7875.getGID());
    }

    /**
     * Test get and set UID.
     */
    @Test
    public void testUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = 1234567890L;
        x7875.setUID(uid);
        assertEquals(uid, x7875.getUID());
    }

    /**
     * Test get and set GID.
     */
    @Test
    public void testGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long gid = 9876543210L;
        x7875.setGID(gid);
        assertEquals(gid, x7875.getGID());
    }

    /**
     * Test getLocalFileDataLength.
     */
    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(1234567890L);
        x7875.setGID(9876543210L);
        int uidSize = 1 + Byte.BYTES; // 1 byte for version + 1 byte for UID size
        int gidSize = 1 + Byte.BYTES; // 1 byte for version + 1 byte for GID size
        int expectedLength = uidSize + gidSize + UID.toByteArray().length + GID.toByteArray().length;
        ZipShort actualLength = x7875.getLocalFileDataLength();
        assertEquals(expectedLength, actualLength.getValue());
    }

    /**
     * Test parsing from local file data.
     */
    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] data = new byte[]{(byte) 0x78, (byte) 0x75, (byte) 0x03, 0x00, // header id
                0x01, 0x01, // version and uid size
                (byte) 0x08, 0x42, 0x54, 0x01, 0x23, 0x45, 0x67, 0x89, // uid
                (byte) 0x01, 0x01, // version and gid size
                (byte) 0x08, 0x48, 0x56, 0x02, 0x34, 0x57, 0x68, 0x90}; // gid
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1, x7875.getVersion());
        BigInteger expectedUid = new BigInteger("1234567890");
        BigInteger actualUid = x7875.getUID();
        assertEquals(expectedUid, actualUid);
        BigInteger expectedGid = new BigInteger("9876543210");
        BigInteger actualGid = x7875.getGID();
        assertEquals(expectedGid, actualGid);
    }

    /**
     * Test parsing from local file data with invalid length.
     */
    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidLength() throws ZipException {
        byte[] data = new byte[]{(byte) 0x78, (byte) 0x75}; // header id and version
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);
    }

    /**
     * Test getCentralDirectoryLength.
     */
    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        int expectedLength = x7875.getLocalFileDataLength().getValue();
        ZipShort actualLength = x7875.getCentralDirectoryLength();
        assertEquals(expectedLength, actualLength.getValue());
    }

    /**
     * Test getLocalFileDataData.
     */
    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(1234567890L);
        x7875.setGID(9876543210L);
        byte[] expectedData = new byte[]{(byte) 0x78, (byte) 0x75, // header id
                0x03, 0x01, 0x01, // version, uid size, and gid size
                (byte) 0x08, 0x42, 0x54, 0x01, 0x23, 0x45, 0x67, 0x89, // uid
                (byte) 0x08, 0x48, 0x56, 0x02, 0x34, 0x57, 0x68, 0x90}; // gid
        byte[] actualData = x7875.getLocalFileDataData();
        assertArrayEquals(expectedData, actualData);
    }

    /**
     * Test getCentralDirectoryData.
     */
    @Test
    public void testGetCentralDirectoryData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] expectedData = new byte[0];
        byte[] actualData = x7875.getCentralDirectoryData();
        assertArrayEquals(expectedData, actualData);
    }

    /**
     * Test equals and hashCode.
     */
    @Test
    public void testEqualsAndHashCode() {
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
        assertTrue(x78751.hashCode() == x78752.hashCode());
        assertFalse(x78751.equals(x78753));
        assertFalse(x78751.hashCode() == x78753.hashCode());
    }
}