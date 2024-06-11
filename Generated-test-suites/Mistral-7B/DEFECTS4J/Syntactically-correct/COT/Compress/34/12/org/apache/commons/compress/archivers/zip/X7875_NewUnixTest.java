package org.apache.commons.compress.archivers.zip; // adjust the package name if necessary

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for the X7875_NewUnix class.
 */
public class X7875_NewUnixTest {

    

    @Test
    public void testNewInstance() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        Assert.assertEquals("UID should be initialized to ONE_THOUSAND", ONE_THOUSAND, x7875NewUnix.getUID());
        Assert.assertEquals("GID should be initialized to ONE_THOUSAND", ONE_THOUSAND, x7875NewUnix.getGID());
    }

    @Test
    public void testSetUIDAndGetUID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long uid = 123456789;
        x7875NewUnix.setUID(uid);
        Assert.assertEquals("UID should be set correctly", uid, x7875NewUnix.getUID());
    }

    @Test
    public void testSetGIDAndGetGID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long gid = 987654321;
        x7875NewUnix.setGID(gid);
        Assert.assertEquals("GID should be set correctly", gid, x7875NewUnix.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        int uidSize = 1; // UID is typically a 32 bit unsigned value on most UNIX systems, so we return a long to avoid integer overflow
        int gidSize = 1; // Same as above
        int expectedLength = 3 + uidSize + gidSize;
        Assert.assertEquals("GetLocalFileDataLength should return correct length", expectedLength, x7875NewUnix.getLocalFileDataLength().getValue().intValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        int expectedLength = 0;
        Assert.assertEquals("GetCentralDirectoryLength should return zero", expectedLength, x7875NewUnix.getCentralDirectoryLength().getValue().intValue());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        byte[] uidBytes = new byte[]{(byte) 0x7f, (byte) 0x3e, (byte) 0x85, (byte) 0x2a}; // UID: 123456789
        byte[] gidBytes = new byte[]{(byte) 0x7f, (byte) 0x3e, (byte) 0x85, (byte) 0x2b}; // GID: 987654321
        byte[] expectedData = new byte[]{0x01, 0x03, (byte) 0x7f, 0x3e, 0x85, 0x2a, 0x01, 0x03, 0x7f, 0x3e, 0x85, 0x2b};
        byte[] actualData = x7875NewUnix.getLocalFileDataData();
        Assert.assertArrayEquals("GetLocalFileDataData should return correct data", expectedData, actualData);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidUIDSize() {
        byte[] data = new byte[]{0x01, 0x01, (byte) 0x7f, (byte) 0x3e, (byte) 0x85, (byte) 0x2a, 0x01, 0x01};
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidGIDSize() {
        byte[] data = new byte[]{0x01, 0x01, (byte) 0x7f, (byte) 0x3e, (byte) 0x85, (byte) 0x2a, 0x01, 0x02, 0x01, 0x01};
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
    }

    @Test
    public void testParseFromLocalFileDataWithValidData() {
        byte[] data = new byte[]{0x01, 0x03, (byte) 0x7f, 0x3e, 0x85, 0x2a, 0x01, 0x03, 0x7f, 0x3e, 0x85, 0x2b};
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
        long uid = 123456789;
        long gid = 987654321;
        Assert.assertEquals("UID should be set correctly after parsing", uid, x7875NewUnix.getUID());
        Assert.assertEquals("GID should be set correctly after parsing", gid, x7875NewUnix.getGID());
    }

    
}