package org.apache.commons.compress.archivers.zip; // make sure the package is correct

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;

/**
 * Test class for X7875_NewUnix class.
 */
public class X7875_NewUnixTest {

    @Test
    public void testNewInstance() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        Assert.assertEquals(x7875.getHeaderId(), new ZipShort(0x7875));
        Assert.assertEquals(x7875.getVersion(), 1);
        Assert.assertNull(x7875.getUID());
        Assert.assertNull(x7875.getGID());
    }

    @Test
    public void testSetGetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = 123456;
        x7875.setUID(uid);
        Assert.assertEquals(x7875.getUID(), uid);
    }

    @Test
    public void testSetGetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long gid = 7891011;
        x7875.setGID(gid);
        Assert.assertEquals(x7875.getGID(), gid);
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = 123456;
        long gid = 7891011;
        x7875.setUID(uid);
        x7875.setGID(gid);

        int expectedLength = 3 + 1 + 1 + uid.toString().length() + 1 + gid.toString().length();
        Assert.assertEquals(x7875.getLocalFileDataLength().getValue(), expectedLength);
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = 123456;
        long gid = 7891011;
        x7875.setUID(uid);
        x7875.setGID(gid);

        int expectedLength = x7875.getLocalFileDataLength().getValue();
        Assert.assertEquals(x7875.getCentralDirectoryLength().getValue(), expectedLength);
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = 123456;
        long gid = 7891011;
        x7875.setUID(uid);
        x7875.setGID(gid);

        byte[] expectedData = new byte[x7875.getLocalFileDataLength().getValue()];

        int versionIndex = 0;
        expectedData[versionIndex++] = (byte) unsignedIntToSignedByte(x7875.getVersion());

        int uidSizeIndex = versionIndex;
        expectedData[uidSizeIndex++] = (byte) unsignedIntToSignedByte(uid.toString().length());

        System.arraycopy(uid.toString().getBytes(), 0, expectedData, uidSizeIndex, uid.toString().length());
        uidSizeIndex += uid.toString().length();

        int gidSizeIndex = uidSizeIndex;
        expectedData[gidSizeIndex++] = (byte) unsignedIntToSignedByte(gid.toString().length());

        System.arraycopy(gid.toString().getBytes(), 0, expectedData, gidSizeIndex, gid.toString().length());

        byte[] result = x7875.getLocalFileDataData();
        Assert.assertArrayEquals(expectedData, result);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidVersion() throws ZipException {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] data = new byte[]{0x01, (byte) 0xFA, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01}; // version should be 0x01

        x7875.parseFromLocalFileData(data, 0, data.length);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidUidSize() throws ZipException {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] data = new byte[]{0x01, 0x01, 0x00, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01}; // uid size should be > 0

        x7875.parseFromLocalFileData(data, 0, data.length);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidGidSize() throws ZipException {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] data = new byte[]{0x01, 0x01, 0x01, 0x01, 0x00, 0x01, 0x01, 0x01, 0x01, 0x01}; // gid size should be > 0

        x7875.parseFromLocalFileData(data, 0, data.length);
    }

    @Test
    public void testParseFromLocalFileDataWithValidData() throws ZipException {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        String uidString = "1234567890";
        String gidString = "2345678901";

        byte[] data = new byte[]{0x01, (byte) 0x03 + uidString.length() + gidString.length(), 0x01, 0x01};
        System.arraycopy(uidString.getBytes(), 0, data, 3, uidString.length());
        int uidSizeIndex = 3 + uidString.length();
        System.arraycopy(gidString.getBytes(), 0, data, uidSizeIndex, gidString.length());

        x7875.parseFromLocalFileData(data, 0, data.length);

        Assert.assertEquals(x7875.getVersion(), 1);
        Assert.assertEquals(x7875.getUID(), new BigInteger(uidString, 10));
        Assert.assertEquals(x7875.getGID(), new BigInteger(gidString, 10));
    }
}