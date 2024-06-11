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
        long uid = 1001;
        x7875.setUID(uid);
        assertEquals(uid, x7875.getUID());
    }

    @Test
    public void testSetGetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long gid = 1002;
        x7875.setGID(gid);
        assertEquals(gid, x7875.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        int uidSize = 2;
        int gidSize = 2;

        x7875.setUID(1001);
        x7875.setGID(1002);

        assertEquals(3 + uidSize + gidSize, x7875.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        int uidSize = 2;
        int gidSize = 2;

        x7875.setUID(1001);
        x7875.setGID(1002);

        assertEquals(x7875.getLocalFileDataLength().getValue(), x7875.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        int uidSize = 2;
        int gidSize = 2;

        long uid = 1001;
        long gid = 1002;

        x7875.setUID(uid);
        x7875.setGID(gid);

        byte[] expectedData = new byte[3 + uidSize + gidSize];
        expectedData[0] = (byte) unsignedIntToSignedByte(x7875.getVersion());
        expectedData[1] = (byte) unsignedIntToSignedByte(uidSize);
        System.arraycopy(ZipUtil.bigToByteArray(new BigInteger(uid.toString())), 0, expectedData, 2, uidSize);
        expectedData[2 + uidSize] = (byte) unsignedIntToSignedByte(gidSize);
        System.arraycopy(ZipUtil.bigToByteArray(new BigInteger(gid.toString())), 0, expectedData, 3 + uidSize, gidSize);

        byte[] actualData = x7875.getLocalFileDataData();

        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] data = new byte[]{
                0x78, 0x75, 0x01, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x02
        };

        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);

        assertEquals(1, x7875.getVersion());
        assertEquals(1, x7875.getUID().intValue());
        assertEquals(2, x7875.getGID().intValue());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidData() throws ZipException {
        byte[] data = new byte[]{
                0x78, 0x75, 0x01, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x03
        };

        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);

        fail("Should throw ZipException");
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        //CentralDirectory data should be empty since this class doesn't store anything inside the central directory.
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromCentralDirectoryData(new byte[0], 0, 0);
    }
}