package org.apache.commons.compress.archivers.zip;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.*;

public class X7875_NewUnixTest {

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix unix = new X7875_NewUnix();
        assertEquals(0x7875, unix.getHeaderId().getValue());
    }

    @Test
    public void testGetUIDAndGetGID() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1000);
        unix.setGID(2000);
        assertEquals(1000, unix.getUID());
        assertEquals(2000, unix.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1000);
        unix.setGID(2000);
        assertEquals(5, unix.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1000);
        unix.setGID(2000);
        assertEquals(5, unix.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1000);
        unix.setGID(2000);
        byte[] expected = {1, 4, 0, 0, 1, -48, 7, -24};
        assertArrayEquals(expected, unix.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        X7875_NewUnix unix = new X7875_NewUnix();
        assertArrayEquals(new byte[0], unix.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() throws Exception {
        X7875_NewUnix unix = new X7875_NewUnix();
        byte[] data = {1, 4, 0, 0, 1, -48, 7, -24};
        unix.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1000, unix.getUID());
        assertEquals(2000, unix.getGID());
    }

    @Test
    public void testEquals() {
        X7875_NewUnix unix1 = new X7875_NewUnix();
        unix1.setUID(1000);
        unix1.setGID(2000);

        X7875_NewUnix unix2 = new X7875_NewUnix();
        unix2.setUID(1000);
        unix2.setGID(2000);

        assertTrue(unix1.equals(unix2));
        assertFalse(unix1.equals(null));
        assertFalse(unix1.equals("Test"));

        unix2.setUID(2000);
        assertFalse(unix1.equals(unix2));
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix unix1 = new X7875_NewUnix();
        unix1.setUID(1000);
        unix1.setGID(2000);

        X7875_NewUnix unix2 = new X7875_NewUnix();
        unix2.setUID(1000);
        unix2.setGID(2000);

        assertEquals(unix1.hashCode(), unix2.hashCode());

        unix2.setUID(2000);
        assertNotEquals(unix1.hashCode(), unix2.hashCode());
    }

    @Test
    public void testClone() throws Exception {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1000);
        unix.setGID(2000);
        X7875_NewUnix clone = (X7875_NewUnix) unix.clone();
        assertArrayEquals(unix.getLocalFileDataData(), clone.getLocalFileDataData());
    }

    @Test
    public void testToString() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1000);
        unix.setGID(2000);
        assertEquals("0x7875 Zip Extra Field: UID=1000 GID=2000", unix.toString());
    }

    @Test
    public void testTrimLeadingZeroesForceMinLength() {
        byte[] array = {0, 0, 1, 2, 3, 0, 0};
        byte[] expected = {1, 2, 3};
        assertArrayEquals(expected, X7875_NewUnix.trimLeadingZeroesForceMinLength(array));
    }
}