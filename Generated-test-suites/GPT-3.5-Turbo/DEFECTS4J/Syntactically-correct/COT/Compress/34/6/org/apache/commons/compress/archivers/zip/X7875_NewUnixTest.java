import org.apache.commons.compress.archivers.zip.ZipException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.util.zip.ZipUtil;

import static org.junit.Assert.*;

public class X7875_NewUnixTest {

    private X7875_NewUnix unix;

    @Before
    public void setUp() {
        unix = new X7875_NewUnix();
    }

    @Test
    public void testGetHeaderId() {
        assertEquals(new ZipShort(0x7875), unix.getHeaderId());
    }

    @Test
    public void testGetUID() {
        unix.setUID(123);
        assertEquals(123, unix.getUID());
    }

    @Test
    public void testGetGID() {
        unix.setGID(456);
        assertEquals(456, unix.getGID());
    }

    @Test
    public void testSetUID() {
        unix.setUID(123);
        assertEquals(123, unix.getUID());
    }

    @Test
    public void testSetGID() {
        unix.setGID(456);
        assertEquals(456, unix.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        unix.setUID(123);
        unix.setGID(456);
        byte[] uidBytes = BigInteger.valueOf(123).toByteArray();
        byte[] gidBytes = BigInteger.valueOf(456).toByteArray();
        byte[] expectedData = new byte[]{1, 2, uidBytes[0], gidBytes[0]};
        assertArrayEquals(expectedData, unix.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        assertEquals(unix.getLocalFileDataLength(), unix.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        byte[] uidBytes = BigInteger.valueOf(123).toByteArray();
        byte[] gidBytes = BigInteger.valueOf(456).toByteArray();
        byte[] expectedData = new byte[]{1, 1, uidBytes[0], 1, gidBytes[0]};
        assertArrayEquals(expectedData, unix.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        assertArrayEquals(new byte[0], unix.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] data = new byte[]{1, 1, 1, 1, 1, 1};
        unix.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1, unix.getUID());
        assertEquals(1, unix.getGID());
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        byte[] data = new byte[]{1, 1, 1, 1, 1, 1};
        unix.parseFromCentralDirectoryData(data, 0, data.length);
        assertEquals(1, unix.getUID());
        assertEquals(1, unix.getGID());
    }

    @Test
    public void testEquals() {
        X7875_NewUnix unix2 = new X7875_NewUnix();
        unix2.setUID(123);
        unix2.setGID(456);
        assertTrue(unix.equals(unix2));
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix unix2 = new X7875_NewUnix();
        unix2.setUID(123);
        unix2.setGID(456);
        assertEquals(unix.hashCode(), unix2.hashCode());
    }

    @Test
    public void testTrimLeadingZeroesForceMinLength() {
        byte[] array = new byte[]{0, 0, 1};
        byte[] expectedArray = new byte[]{1};
        assertArrayEquals(expectedArray, X7875_NewUnix.trimLeadingZeroesForceMinLength(array));
    }

}