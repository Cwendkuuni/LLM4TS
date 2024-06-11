package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;

public class X7875_NewUnixTest {

    @Test
    public void testNewInstance() {
        X7875_NewUnix xuf = new X7875_NewUnix();
        assertNull(xuf.getUID());
        assertNull(xuf.getGID());
    }

    @Test
    public void testSetAndGetUID() {
        X7875_NewUnix xuf = new X7875_NewUnix();
        long uid = 123456789;
        xuf.setUID(uid);
        assertEquals(uid, xuf.getUID());
    }

    @Test
    public void testSetAndGetGID() {
        X7875_NewUnix xuf = new X7875_NewUnix();
        long gid = 987654321;
        xuf.setGID(gid);
        assertEquals(gid, xuf.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix xuf = new X7875_NewUnix();
        BigInteger uid = new BigInteger("1234567890123456789012345678901234567890");
        BigInteger gid = new BigInteger("9876543210987654321098765432109876543210");

        int uidSize = uid.toByteArray().length;
        int gidSize = gid.toByteArray().length;

        xuf.uid = uid;
        xuf.gid = gid;

        ZipShort expectedLength = new ZipShort(3 + uidSize + gidSize);
        ZipShort actualLength = xuf.getLocalFileDataLength();

        assertEquals(expectedLength.getValue(), actualLength.getValue());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] data = new byte[]{
                0x78, 0x75, (byte) 0x01, (byte) 0x03,
                0x00, 0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01,
                0x00, 0x2a, 0x09, 0x87, 0x65, 0x43, 0x21, 0x00, 0x03, 0x00, 0x32, 0x10, 0x98,
                0x76, 0x54, 0x32, 0x10, 0x98, 0x76, 0x54, 0x32, 0x10, 0x98, 0x76, 0x54, 0x32,
                0x10
        };

        X7875_NewUnix xuf = new X7875_NewUnix();
        xuf.parseFromLocalFileData(data, 0, data.length);

        assertEquals(1, xuf.getVersion());
        assertEquals(BigInteger.valueOf(0x2a98765432109876L), xuf.getUID());
        assertEquals(BigInteger.valueOf(0x2a98765432109876L), xuf.getGID());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidData() throws ZipException {
        byte[] data = new byte[]{
                0x78, 0x75, (byte) 0x01, (byte) 0x03,
                0x00, 0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01,
                0x00, 0x2a, 0x09, 0x87, 0x65, 0x43, 0x21, 0x00, 0x03, 0x10, 0x32, 0x10, 0x98,
                0x76, 0x54, 0x32, 0x10, 0x98, 0x76, 0x54, 0x32, 0x10, 0x98, 0x76, 0x54, 0x32,
                0x10
        };

        X7875_NewUnix xuf = new X7875_NewUnix();
        xuf.parseFromLocalFileData(data, 0, data.length - 1);
    }

    @Test
    public void testReset() {
        X7875_NewUnix xuf = new X7875_NewUnix();
        xuf.setUID(123456789);
        xuf.setGID(987654321);
        xuf.reset();
        assertNull(xuf.getUID());
        assertNull(xuf.getGID());
    }
}