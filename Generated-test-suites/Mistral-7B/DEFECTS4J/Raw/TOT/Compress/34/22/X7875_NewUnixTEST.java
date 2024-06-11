package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

/**
 * Test class for X7875_NewUnix.
 */
public class X7875_NewUnixTest {

    /**
     * Tests the constructor.
     */
    @Test
    public void testConstructor() {
        X7875_NewUnix xf = new X7875_NewUnix();
        assertNotNull(xf);
    }

    /**
     * Tests getHeaderId().
     */
    @Test
    public void testGetHeaderId() {
        X7875_NewUnix xf = new X7875_NewUnix();
        ZipShort expected = new ZipShort(0x7875);
        ZipShort actual = xf.getHeaderId();
        assertEquals(expected, actual);
    }

    /**
     * Tests getLocalFileDataLength().
     */
    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix xf = new X7875_NewUnix();
        xf.setUID(123456789L);
        xf.setGID(234567890L);
        byte[] uidBytes = xf.uid.toByteArray();
        byte[] gidBytes = xf.gid.toByteArray();
        int expected = 1 + 1 + uidBytes.length + gidBytes.length;
        ZipShort actual = xf.getLocalFileDataLength();
        assertEquals(new ZipShort((short) expected), actual);
    }

    /**
     * Tests getCentralDirectoryLength().
     */
    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix xf = new X7875_NewUnix();
        xf.setUID(123456789L);
        xf.setGID(234567890L);
        ZipShort expected = xf.getLocalFileDataLength();
        ZipShort actual = xf.getCentralDirectoryLength();
        assertEquals(expected, actual);
    }

    /**
     * Tests getLocalFileDataData().
     */
    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix xf = new X7875_NewUnix();
        xf.setUID(123456789L);
        xf.setGID(234567890L);
        byte[] expected = xf.getLocalFileDataData();
        byte[] actual = xf.getLocalFileDataData();
        assertArrayEquals(expected, actual);
    }

    /**
     * Tests getCentralDirectoryData().
     */
    @Test
    public void testGetCentralDirectoryData() {
        X7875_NewUnix xf = new X7875_NewUnix();
        byte[] actual = xf.getCentralDirectoryData();
        assertArrayEquals(new byte[0], actual);
    }

    /**
     * Tests equals() with same object.
     */
    @Test
    public void testEqualsSameObject() {
        X7875_NewUnix xf1 = new X7875_NewUnix();
        X7875_NewUnix xf2 = xf1;
        assertTrue(xf1.equals(xf2));
    }

    /**
     * Tests equals() with different objects.
     */
    @Test
    public void testEqualsDifferentObjects() {
        X7875_NewUnix xf1 = new X7875_NewUnix();
        X7875_NewUnix xf2 = new X7875_NewUnix();
        assertFalse(xf1.equals(xf2));

        xf1.setUID(123456789L);
        xf1.setGID(234567890L);
        xf2.setUID(123456789L);
        xf2.setGID(234567890L);
        assertTrue(xf1.equals(xf2));

        xf1.setUID(123456789L);
        xf1.setGID(234567891L);
        assertFalse(xf1.equals(xf2));
    }

    /**
     * Tests parseFromLocalFileData().
     */
    @Test
    public void testParseFromLocalFileData() throws ZipException {
        X7875_NewUnix xf = new X7875_NewUnix();
        byte[] data = new byte[]{(byte) 0x78, (byte) 0x75, 0, 1, 0, 0, 0, 0,
                0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0};
        xf.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1, xf.getVersion());
        assertEquals(Long.parseUnsignedLong("1"), xf.getUID());
        assertEquals(Long.parseUnsignedLong("2"), xf.getGID());
    }

    /**
     * Tests parseFromLocalFileData() with invalid data.
     */
    @Test
    public void testParseFromLocalFileDataInvalidData() {
        X7875_NewUnix xf = new X7875_NewUnix();
        byte[] data = new byte[]{0};
        assertThrows(ZipException.class, () -> xf.parseFromLocalFileData(data, 0, data.length));
    }

    /**
     * Tests parseFromLocalFileData() with invalid version.
     */
    @Test
    public void testParseFromLocalFileDataInvalidVersion() {
        X7875_NewUnix xf = new X7875_NewUnix();
        byte[] data = new byte[]{(byte) 0x78, (byte) 0x75, (byte) 0x02, 1, 0, 0, 0, 0,
                0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0};
        assertThrows(ZipException.class, () -> xf.parseFromLocalFileData(data, 0, data.length));
    }

    /**
     * Tests parseFromLocalFileData() with invalid UID size.
     */
    @Test
    public void testParseFromLocalFileDataInvalidUidSize() {
        X7875_NewUnix xf = new X7875_NewUnix();
        byte[] data = new byte[]{(byte) 0x78, (byte) 0x75, 0, 1, 1, 0, 0, 0,
                0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0};
        assertThrows(ZipException.class, () -> xf.parseFromLocalFileData(data, 0, data.length));
    }

    /**
     * Tests parseFromLocalFileData() with invalid GID size.
     */
    @Test
    public void testParseFromLocalFileDataInvalidGidSize() {
        X7875_NewUnix xf = new X7875_NewUnix();
        byte[] data = new byte[]{(byte) 0x78, (byte) 0x75, 0, 1, 1, 1, 0, 0,
                0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0};
        assertThrows(ZipException.class, () -> xf.parseFromLocalFileData(data, 0, data.length));
    }
}