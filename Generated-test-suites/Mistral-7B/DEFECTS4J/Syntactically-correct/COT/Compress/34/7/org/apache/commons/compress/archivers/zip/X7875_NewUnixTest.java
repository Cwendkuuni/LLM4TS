package org.apache.commons.compress.archivers.zip; // make sure test class is in same package as the class under test

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the X7875_NewUnix class.
 */
public class X7875_NewUnixTest {

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        ZipShort headerId = x7875.getHeaderId();
        Assert.assertEquals(0x7875, headerId.getValue());
    }

    @Test
    public void testGetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = 123456789;
        x7875.setUID(uid);
        long actualUid = x7875.getUID();
        Assert.assertEquals(uid, actualUid);
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long gid = 234567890;
        x7875.setGID(gid);
        long actualGid = x7875.getGID();
        Assert.assertEquals(gid, actualGid);
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        int uidSize = 2;
        int gidSize = 2;
        x7875.setUID(123456789);
        x7875.setGID(234567890);
        ZipShort length = x7875.getLocalFileDataLength();
        int expectedLength = 1 + uidSize + gidSize;
        Assert.assertEquals(expectedLength, length.getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        int uidSize = 2;
        int gidSize = 2;
        x7875.setUID(123456789);
        x7875.setGID(234567890);
        ZipShort length = x7875.getCentralDirectoryLength();
        int expectedLength = x7875.getLocalFileDataLength().getValue();
        Assert.assertEquals(expectedLength, length.getValue());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        X7875_NewUnix expectedX7875 = new X7875_NewUnix();
        long uid = 123456789;
        long gid = 234567890;
        expectedX7875.setUID(uid);
        expectedX7875.setGID(gid);

        byte[] data = new byte[9];
        data[0] = (byte) 0x01; // version
        data[1] = (byte) 0x02; // uid size
        byte[] uidBytes = new byte[2];
        System.arraycopy(Long.toString(uid).getBytes(), 0, uidBytes, 0, 2);
        System.arraycopy(uidBytes, 0, data, 2, 2);
        data[4] = (byte) 0x02; // gid size
        byte[] gidBytes = new byte[2];
        System.arraycopy(Long.toString(gid).getBytes(), 0, gidBytes, 0, 2);
        System.arraycopy(gidBytes, 0, data, 5, 2);

        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);
        Assert.assertEquals(expectedX7875.getUID(), x7875.getUID());
        Assert.assertEquals(expectedX7875.getGID(), x7875.getGID());
        Assert.assertEquals(expectedX7875.getVersion(), x7875.getVersion());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidLength() throws ZipException {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] data = new byte[5];
        x7875.parseFromLocalFileData(data, 0, data.length);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidVersion() throws ZipException {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] data = new byte[7];
        data[0] = (byte) 0x02; // invalid version
        data[1] = (byte) 0x02; // uid size
        byte[] uidBytes = new byte[2];
        System.arraycopy(Long.toString(123456789).getBytes(), 0, uidBytes, 0, 2);
        System.arraycopy(uidBytes, 0, data, 2, 2);
        data[4] = (byte) 0x02; // gid size
        byte[] gidBytes = new byte[2];
        System.arraycopy(Long.toString(234567890).getBytes(), 0, gidBytes, 0, 2);
        System.arraycopy(gidBytes, 0, data, 5, 2);

        x7875.parseFromLocalFileData(data, 0, data.length);
    }

}