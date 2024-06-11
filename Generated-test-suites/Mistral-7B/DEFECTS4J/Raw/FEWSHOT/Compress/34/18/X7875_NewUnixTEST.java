package org.apache.commons.compress.archivers.zip;

import static org.apache.commons.compress.archivers.zip.ZipUtil.reverse;
import static org.apache.commons.compress.archivers.zip.ZipUtil.signedByteToUnsignedInt;
import static org.apache.commons.compress.archivers.zip.ZipUtil.unsignedIntToSignedByte;
import static org.junit.Assert.*;

import org.junit.Test;

public class X7875_NewUnixTest {

    @Test
    public void testNewInstance() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(1, x7875.getVersion());
        assertNull(x7875.getUID());
        assertNull(x7875.getGID());
    }

    @Test
    public void testSetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(12345);
        assertEquals(12345, ZipUtil.bigToLong(x7875.getUID()));
    }

    @Test
    public void testSetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setGID(54321);
        assertEquals(54321, ZipUtil.bigToLong(x7875.getGID()));
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        int uidSize = Integer.BYTES;
        int gidSize = Integer.BYTES;
        ZipShort expectedLength = new ZipShort(3 + uidSize + gidSize);
        assertEquals(expectedLength.getValue(), x7875.getLocalFileDataLength().getValue());

        // test for empty UID and GID
        x7875.reset();
        expectedLength = new ZipShort(3);
        assertEquals(expectedLength.getValue(), x7875.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        int uidSize = Integer.BYTES;
        int gidSize = Integer.BYTES;
        ZipShort expectedLength = new ZipShort(3 + uidSize + gidSize);
        assertEquals(expectedLength.getValue(), x7875.getCentralDirectoryLength().getValue());

        // test for empty UID and GID
        x7875.reset();
        expectedLength = new ZipShort(3);
        assertEquals(expectedLength.getValue(), x7875.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] expectedData = new byte[0];
        byte[] actualData = x7875.getLocalFileDataData();
        assertArrayEquals(expectedData, actualData);

        // test with UID and GID
        x7875.setUID(12345L);
        x7875.setGID(54321L);
        expectedData = new byte[7];
        expectedData[0] = unsignedIntToSignedByte(x7875.getVersion());
        expectedData[1] = unsignedIntToSignedByte(uidSize);
        System.arraycopy(ZipUtil.bigToBytes(x7875.uid), 0, expectedData, 2, uidSize);
        expectedData[2 + uidSize] = unsignedIntToSignedByte(gidSize);
        System.arraycopy(ZipUtil.bigToBytes(x7875.gid), 0, expectedData, 2 + uidSize, gidSize);
        actualData = x7875.getLocalFileDataData();
        assertArrayEquals(expectedData, actualData);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetCentralDirectoryData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] actualData = x7875.getCentralDirectoryData();
        fail("getCentralDirectoryData should not return any data");
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] testData = new byte[]{
                0x01, 0x02, 0x00, 0x79, 0x2a, 0x1a, 0x1a, 0x1a, 0x00, 0x01, 0x02,
                0x00, 0x2a, 0x1a, 0x1a, 0x00, 0x01, 0x02, 0x00, 0x1a, 0x1a, 0x1a, 0x00,
                0x2a, 0x1a, 0x1a, 0x00, 0x01, 0x02, 0x00, 0x1a, 0x1a, 0x1a, 0x01, 0x02,
                0x01, 0x02, 0x01, 0x02
        };
        x7875.parseFromLocalFileData(testData, 0, testData.length);
        assertEquals(1, x7875.getVersion());
        assertEquals(new BigInteger("2a1a1a0102"), x7875.getUID());
        assertEquals(new BigInteger("2a1a1a0102"), x7875.getGID());
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] testData = new byte[0];
        x7875.parseFromCentralDirectoryData(testData, 0, testData.length);
        // nothing happens
    }

    @Test
    public void testReset() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(12345L);
        x7875.setGID(54321L);
        x7875.reset();
        assertEquals(1, x7875.getVersion());
        assertNull(x7875.getUID());
        assertNull(x7875.getGID());
    }

    @Test
    public void testToString() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals("0x7875 Zip Extra Field: UID=null GID=null", x7875.toString());
        x7875.setUID(new BigInteger("7890abcdefgh"));
        x7875.setGID(new BigInteger("1234pqrstuvwxyz"));
        assertEquals("0x7875 Zip Extra Field: UID=7890abcdefgh GID=1234pqrstuvwxyz", x7875.toString());
    }
}