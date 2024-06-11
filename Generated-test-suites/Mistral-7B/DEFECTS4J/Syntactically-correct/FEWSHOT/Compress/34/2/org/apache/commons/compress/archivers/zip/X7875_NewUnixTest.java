package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;

public class X7875_NewUnixTest {

    @Test
    public void testNewX7875_NewUnix() {
        X7875_NewUnix xfu = new X7875_NewUnix();
        assertEquals(1, xfu.getVersion());
        assertNull(xfu.getUID());
        assertNull(xfu.getGID());
    }

    @Test
    public void testSetAndGetUID() {
        X7875_NewUnix xfu = new X7875_NewUnix();
        xfu.setUID(12345);
        assertEquals(12345, xfu.getUID());
    }

    @Test
    public void testSetAndGetGID() {
        X7875_NewUnix xfu = new X7875_NewUnix();
        xfu.setGID(54321);
        assertEquals(54321, xfu.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix xfu = new X7875_NewUnix();
        xfu.setUID(4);
        xfu.setGID(3);

        int length = xfu.getLocalFileDataLength().getValue();
        assertEquals(10, length);
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix xfu = new X7875_NewUnix();
        xfu.setUID(4);
        xfu.setGID(3);

        int length = xfu.getCentralDirectoryLength().getValue();
        assertEquals(10, length);
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix xfu = new X7875_NewUnix();
        xfu.setUID(4);
        xfu.setGID(3);

        byte[] data = xfu.getLocalFileDataData();

        assertEquals(10, data.length);
        assertEquals(1, data[0]);
        assertEquals(4, data[1]);
        assertEquals(0, data[2]);
        assertEquals(0, data[3]);
        assertEquals(0, data[4]);
        assertEquals(0, data[5]);
        assertEquals(3, data[6]);
        assertEquals(0, data[7]);
        assertEquals(0, data[8]);
        assertEquals(0, data[9]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseFromLocalFileDataWithInvalidData() {
        byte[] data = new byte[]{1, 5, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        X7875_NewUnix xfu = new X7875_NewUnix();
        xfu.parseFromLocalFileData(data, 0, data.length);
    }

    @Test
    public void testParseFromLocalFileDataWithValidData() {
        byte[] data = new byte[]{1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 1, 0, 0, 3, 0, 0, 1};
        X7875_NewUnix xfu = new X7875_NewUnix();
        xfu.parseFromLocalFileData(data, 0, data.length);

        assertEquals(1, xfu.getVersion());
        assertEquals(new BigInteger("1"), xfu.getUID());
        assertEquals(new BigInteger("3"), xfu.getGID());
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        // not tested since this class doesn't store anything inside
        // the central directory.
    }

    @Test
    public void testReset() {
        X7875_NewUnix xfu = new X7875_NewUnix();
        xfu.setUID(12345);
        xfu.setGID(54321);

        xfu.reset();

        assertNull(xfu.getUID());
        assertNull(xfu.getGID());
    }

    @Test
    public void testToString() {
        X7875_NewUnix xfu = new X7875_NewUnix();
        xfu.setUID(12345);
        xfu.setGID(54321);

        assertEquals("0x7875 Zip Extra Field: UID=12345 GID=54321", xfu.toString());
    }
}