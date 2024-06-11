import org.apache.commons.compress.archivers.zip.ZipException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.util.zip.ZipException;

import static org.junit.Assert.*;

public class X7875_NewUnixTest {

    private X7875_NewUnix x7875;

    @Before
    public void setUp() {
        x7875 = new X7875_NewUnix();
    }

    @Test
    public void testGetHeaderId() {
        assertEquals(new ZipShort(0x7875), x7875.getHeaderId());
    }

    @Test
    public void testGetUID() {
        x7875.setUID(1000);
        assertEquals(1000, x7875.getUID());
    }

    @Test
    public void testGetGID() {
        x7875.setGID(1000);
        assertEquals(1000, x7875.getGID());
    }

    @Test
    public void testSetUID() {
        x7875.setUID(2000);
        assertEquals(2000, x7875.getUID());
    }

    @Test
    public void testSetGID() {
        x7875.setGID(2000);
        assertEquals(2000, x7875.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        x7875.setUID(1000);
        x7875.setGID(1000);
        assertEquals(new ZipShort(13), x7875.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        assertEquals(new ZipShort(13), x7875.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        x7875.setUID(1000);
        x7875.setGID(1000);
        byte[] expected = {1, 1, 0, 3, 0, -24, 1, 0};
        assertArrayEquals(expected, x7875.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        byte[] expected = new byte[0];
        assertArrayEquals(expected, x7875.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] data = {1, 1, 0, 3, 0, -24, 1, 0};
        x7875.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1000, x7875.getUID());
        assertEquals(1000, x7875.getGID());
    }

    @Test
    public void testEquals() {
        X7875_NewUnix xf1 = new X7875_NewUnix();
        X7875_NewUnix xf2 = new X7875_NewUnix();
        xf1.setUID(1000);
        xf1.setGID(1000);
        xf2.setUID(1000);
        xf2.setGID(1000);
        assertTrue(xf1.equals(xf2));
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix xf1 = new X7875_NewUnix();
        X7875_NewUnix xf2 = new X7875_NewUnix();
        xf1.setUID(1000);
        xf1.setGID(1000);
        xf2.setUID(1000);
        xf2.setGID(1000);
        assertEquals(xf1.hashCode(), xf2.hashCode());
    }

    @Test
    public void testToString() {
        x7875.setUID(1000);
        x7875.setGID(1000);
        assertEquals("0x7875 Zip Extra Field: UID=1000 GID=1000", x7875.toString());
    }
}