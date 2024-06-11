package org.apache.commons.compress.archivers.zip;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;

public class X7875_NewUnixTest {

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix unix = new X7875_NewUnix();
        Assert.assertEquals(new ZipShort(0x7875), unix.getHeaderId());
    }

    @Test
    public void testGetUID() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1000);
        Assert.assertEquals(1000L, unix.getUID());
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setGID(1000);
        Assert.assertEquals(1000L, unix.getGID());
    }

    @Test
    public void testSetUID() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1500);
        Assert.assertEquals(1500L, unix.getUID());
    }

    @Test
    public void testSetGID() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setGID(1500);
        Assert.assertEquals(1500L, unix.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1000);
        unix.setGID(1000);
        Assert.assertEquals(new ZipShort(7), unix.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1000);
        unix.setGID(1000);
        Assert.assertEquals(new ZipShort(7), unix.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1000);
        unix.setGID(1000);
        byte[] expectedData = {0x01, 0x02, 0x03, 0x03, 0x41, 0x42, 0x43};
        Assert.assertArrayEquals(expectedData, unix.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        X7875_NewUnix unix = new X7875_NewUnix();
        Assert.assertArrayEquals(new byte[0], unix.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() throws Exception {
        X7875_NewUnix unix = new X7875_NewUnix();
        byte[] data = {0x01, 0x02, 0x03, 0x03, 0x41, 0x42, 0x43};
        unix.parseFromLocalFileData(data, 0, data.length);
        Assert.assertEquals(1, unix.getUID());
        Assert.assertEquals(2, unix.getGID());
    }

    @Test
    public void testToString() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1000);
        unix.setGID(1000);
        Assert.assertEquals("0x7875 Zip Extra Field: UID=1000 GID=1000", unix.toString());
    }

    @Test
    public void testEquals() {
        X7875_NewUnix unix1 = new X7875_NewUnix();
        unix1.setUID(1000);
        unix1.setGID(1000);

        X7875_NewUnix unix2 = new X7875_NewUnix();
        unix2.setUID(1000);
        unix2.setGID(1000);

        X7875_NewUnix unix3 = new X7875_NewUnix();
        unix3.setUID(2000);
        unix3.setGID(2000);

        Assert.assertEquals(unix1, unix2);
        Assert.assertNotEquals(unix1, unix3);
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix unix1 = new X7875_NewUnix();
        unix1.setUID(1000);
        unix1.setGID(1000);

        X7875_NewUnix unix2 = new X7875_NewUnix();
        unix2.setUID(1000);
        unix2.setGID(1000);

        Assert.assertEquals(unix1.hashCode(), unix2.hashCode());
    }
}