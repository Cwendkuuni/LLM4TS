package org.apache.commons.compress.archivers.zip;
import org.junit.Test;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {

    @Test
    public void testConstructor() {
        X7875_NewUnix unix = new X7875_NewUnix();
        assertEquals(1, unix.getUID());
        assertEquals(1, unix.getGID());
    }

    @Test
    public void testSetUID() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(100);
        assertEquals(100, unix.getUID());
    }

    @Test
    public void testSetGID() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setGID(200);
        assertEquals(200, unix.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix unix = new X7875_NewUnix();
        assertEquals(5, unix.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix unix = new X7875_NewUnix();
        assertEquals(5, unix.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix unix = new X7875_NewUnix();
        byte[] data = unix.getLocalFileDataData();
        byte[] expected = new byte[]{1, 1, 0, 0, 0, 0};
        assertArrayEquals(expected, data);
    }

    @Test
    public void testGetCentralDirectoryData() {
        X7875_NewUnix unix = new X7875_NewUnix();
        byte[] data = unix.getCentralDirectoryData();
        assertEquals(0, data.length);
    }

    @Test
    public void testParseFromLocalFileData() throws java.util.zip.ZipException {
        X7875_NewUnix unix = new X7875_NewUnix();
        byte[] data = new byte[]{1, 1, 0, 0, 0, 0};
        unix.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1, unix.getUID());
        assertEquals(1, unix.getGID());
    }

    @Test
    public void testParseFromCentralDirectoryData() throws java.util.zip.ZipException {
        X7875_NewUnix unix = new X7875_NewUnix();
        byte[] data = new byte[0];
        unix.parseFromCentralDirectoryData(data, 0, data.length);
    }

    @Test
    public void testEquals() {
        X7875_NewUnix unix1 = new X7875_NewUnix();
        X7875_NewUnix unix2 = new X7875_NewUnix();
        assertTrue(unix1.equals(unix2));
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix unix = new X7875_NewUnix();
        assertEquals(-1234567, unix.hashCode());
    }
}