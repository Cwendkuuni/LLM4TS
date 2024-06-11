package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import org.junit.Test;

public class X7875_NewUnixTest {

    @Test
    public void testNewInstance() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertNull(x7875.getUID());
        assertNull(x7875.getGID());
    }

    @Test
    public void testSetGetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = 12345L;
        x7875.setUID(uid);
        assertEquals(uid, x7875.getUID());
    }

    @Test
    public void testSetGetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long gid = 67890L;
        x7875.setGID(gid);
        assertEquals(gid, x7875.getGID());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidDataSize() throws ZipException {
        byte[] data = {0x78, 0x75, 0x01, 0x01, (byte) 0xff, 0x00, 0x00, 0x01};
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidUID() throws ZipException {
        byte[] data = {0x78, 0x75, 0x01, 0x02, (byte) 0xff, 0x01, (byte) 0xff, 0x01};
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidGID() throws ZipException {
        byte[] data = {0x78, 0x75, 0x01, 0x01, 0x01, 0x02, (byte) 0xff, 0x01};
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);
    }

    @Test
    public void testParseFromLocalFileDataValidData() throws ZipException {
        byte[] data = {0x78, 0x75, 0x01, 0x01, 0x31, 0x32, 0x33, 0x34, 0x35, 0x01, 0x01, 0x61, 0x62, 0x63, 0x64, 0x65};
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);
        assertEquals(12345, x7875.getUID());
        assertEquals(65241, x7875.getGID());
    }

    @Test(expected = ZipException.class)
    public void testParseFromCentralDirectoryDataInvalidDataSize() throws ZipException {
        byte[] data = {};
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromCentralDirectoryData(data, 0, data.length);
    }

    @Test
    public void testToString() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(12345);
        x7875.setGID(65241);
        assertEquals("0x7875 Zip Extra Field: UID=12345 GID=65241", x7875.toString());
    }
}