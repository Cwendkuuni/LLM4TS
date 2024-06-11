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
    public void testNewInstance() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(1, x7875.getVersion());
        assertNull(x7875.getUID());
        assertNull(x7875.getGID());
    }

    @Test
    public void testSetGetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = 1234567890L;
        x7875.setUID(uid);
        assertEquals(uid, x7875.getUID());
    }

    @Test
    public void testSetGetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long gid = 9876543210L;
        x7875.setGID(gid);
        assertEquals(gid, x7875.getGID());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] data = new byte[] { 0x01, (byte) 0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x01, 0x04, 0x00, 0x00, 0x00, 0x00, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, (byte) 0x01, 0x04, 0x00, 0x00, 0x00, 0x00, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39 };
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1, x7875.getVersion());
        assertEquals(new BigInteger("1234567890123456789012345678901234567890"), x7875.getUID());
        assertEquals(new BigInteger("9876543210987654321098765432109876543210"), x7875.getGID());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidData() throws ZipException {
        byte[] data = new byte[] { 0x01, (byte) 0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x01, 0x04, 0x00, 0x00, 0x00, 0x00, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, (byte) 0x01, 0x04, 0x00, 0x00, 0x00, 0x00, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, (byte) 0x03 }; // invalid length
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        BigInteger uid = new BigInteger("1234567890123456789012345678901234567890");
        BigInteger gid = new BigInteger("9876543210987654321098765432109876543210");
        x7875.setUID(uid.longValue());
        x7875.setGID(gid.longValue());
        ZipShort length = x7875.getLocalFileDataLength();
        assertEquals((short) (3 + uid.toByteArray().length + gid.toByteArray().length), length.getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(new ZipShort(0), x7875.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        BigInteger uid = new BigInteger("1234567890123456789012345678901234567890");
        BigInteger gid = new BigInteger("9876543210987654321098765432109876543210");
        x7875.setUID(uid.longValue());
        x7875.setGID(gid.longValue());
        byte[] data = x7875.getLocalFileDataData();
        assertEquals(3 + uid.toByteArray().length + gid.toByteArray().length, data.length);

        byte[] expectedUidBytes = uid.toByteArray();
        byte[] expectedGidBytes = gid.toByteArray();

        for (int i = 0; i < expectedUidBytes.length; i++) {
            assertEquals(expectedUidBytes[i], data[i + 1]);
        }

        for (int i = 0; i < expectedGidBytes.length; i++) {
            assertEquals(expectedGidBytes[i], data[i + 1 + expectedUidBytes.length]);
        }
    }

    @Test
    public void testGetCentralDirectoryData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] data = x7875.getCentralDirectoryData();
        assertEquals(0, data.length);
    }

    @Test
    public void testToString() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        BigInteger uid = new BigInteger("1234567890123456789012345678901234567890");
        BigInteger gid = new BigInteger("9876543210987654321098765432109876543210");
        x7875.setUID(uid.longValue());
        x7875.setGID(gid.longValue());
        assertEquals("0x7875 Zip Extra Field: UID=1234567890123456789012345678901234567890 GID=9876543210987654321098765432109876543210", x7875.toString());
    }
}