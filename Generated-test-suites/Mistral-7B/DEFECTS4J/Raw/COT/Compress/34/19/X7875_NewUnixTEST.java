package org.apache.commons.compress.archivers.zip; // Make sure to adjust the package name if needed

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

public class X7875_NewUnixTest {

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        ZipShort expectedHeaderId = new ZipShort(0x7875);
        ZipShort actualHeaderId = x7875NewUnix.getHeaderId();
        Assert.assertEquals(expectedHeaderId, actualHeaderId);
    }

    @Test
    public void testGetUID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long expectedUID = 12345;
        x7875NewUnix.setUID(expectedUID);
        long actualUID = x7875NewUnix.getUID();
        Assert.assertEquals(expectedUID, actualUID);
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long expectedGID = 67890;
        x7875NewUnix.setGID(expectedGID);
        long actualGID = x7875NewUnix.getGID();
        Assert.assertEquals(expectedGID, actualGID);
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] data = new byte[]{(byte) 0x01, (byte) 0x01, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01,
            (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01,
            (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01};
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
        long expectedUID = 123456789012345L;
        long expectedGID = 135790123456789L;
        Assert.assertEquals(expectedUID, x7875NewUnix.getUID());
        Assert.assertEquals(expectedGID, x7875NewUnix.getGID());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidData() throws ZipException {
        byte[] data = new byte[]{(byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01};
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(12345);
        x7875NewUnix.setGID(67890);
        ZipShort expectedLength = new ZipShort((short) (3 + 1 + 1));
        ZipShort actualLength = x7875NewUnix.getLocalFileDataLength();
        Assert.assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        ZipShort expectedLength = new ZipShort((short) 0);
        ZipShort actualLength = x7875NewUnix.getCentralDirectoryLength();
        Assert.assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(12345);
        x7875NewUnix.setGID(67890);
        byte[] expectedData = new byte[]{0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01};
        byte[] actualData = x7875NewUnix.getLocalFileDataData();
        Assert.assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testReset() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(12345);
        x7875NewUnix.setGID(67890);
        x7875NewUnix.reset();
        long expectedUID = 1;
        long expectedGID = 1;
        Assert.assertEquals(expectedUID, x7875NewUnix.getUID());
        Assert.assertEquals(expectedGID, x7875NewUnix.getGID());
    }
}