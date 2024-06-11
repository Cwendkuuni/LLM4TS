package org.apache.commons.compress.archivers.zip;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.util.zip.ZipException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class X7875_NewUnixTest {

    private X7875_NewUnix x7875_NewUnix;

    @Before
    public void setUp() {
        x7875_NewUnix = new X7875_NewUnix();
    }

    @Test
    public void testGetHeaderId() {
        assertEquals(new ZipShort(0x7875), x7875_NewUnix.getHeaderId());
    }

    @Test
    public void testGetUID() {
        x7875_NewUnix.setUID(1000);
        assertEquals(1000, x7875_NewUnix.getUID());
    }

    @Test
    public void testGetGID() {
        x7875_NewUnix.setGID(1000);
        assertEquals(1000, x7875_NewUnix.getGID());
    }

    @Test
    public void testSetUID() {
        x7875_NewUnix.setUID(2000);
        assertEquals(2000, x7875_NewUnix.getUID());
    }

    @Test
    public void testSetGID() {
        x7875_NewUnix.setGID(2000);
        assertEquals(2000, x7875_NewUnix.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        x7875_NewUnix.setUID(1000);
        x7875_NewUnix.setGID(1000);

        byte[] expectedDataLength = {0x06};
        assertArrayEquals(expectedDataLength, x7875_NewUnix.getLocalFileDataLength().getBytes());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        x7875_NewUnix.setUID(1000);
        x7875_NewUnix.setGID(1000);

        byte[] expectedDataLength = {0x06};
        assertArrayEquals(expectedDataLength, x7875_NewUnix.getCentralDirectoryLength().getBytes());
    }

    @Test
    public void testGetLocalFileDataData() {
        x7875_NewUnix.setUID(1000);
        x7875_NewUnix.setGID(1000);

        byte[] expectedData = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        assertArrayEquals(expectedData, x7875_NewUnix.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        byte[] expectedData = {};
        assertArrayEquals(expectedData, x7875_NewUnix.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] data = {0x01, 0x04, 0x02, 0x03, 0x04, 0x05, 0x06};
        x7875_NewUnix.parseFromLocalFileData(data, 0, data.length);

        assertEquals(1, x7875_NewUnix.getUID());
        assertEquals(16909060, x7875_NewUnix.getGID());
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        byte[] data = {0x01, 0x04, 0x02, 0x03, 0x04, 0x05, 0x06};
        x7875_NewUnix.parseFromCentralDirectoryData(data, 0, data.length);

        // No parsing actually takes place, nothing to test
    }

    @Test
    public void testToString() {
        x7875_NewUnix.setUID(1000);
        x7875_NewUnix.setGID(1000);

        assertEquals("0x7875 Zip Extra Field: UID=1000 GID=1000", x7875_NewUnix.toString());
    }

    @Test
    public void testEquals() {
        X7875_NewUnix xf1 = new X7875_NewUnix();
        X7875_NewUnix xf2 = new X7875_NewUnix();

        assertEquals(true, xf1.equals(xf2));
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix xf = new X7875_NewUnix();
        xf.setUID(1000);
        xf.setGID(1000);

        assertEquals(680274860, xf.hashCode());
    }
}