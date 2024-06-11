import org.junit.Test;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix unix = new X7875_NewUnix();
        assertEquals(new ZipShort(0x7875), unix.getHeaderId());
    }

    @Test
    public void testGetUID() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1000);
        assertEquals(1000, unix.getUID());
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setGID(1000);
        assertEquals(1000, unix.getGID());
    }

    @Test
    public void testSetUID() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(2000);
        assertEquals(2000, unix.getUID());
    }

    @Test
    public void testSetGID() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setGID(2000);
        assertEquals(2000, unix.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix unix = new X7875_NewUnix();
        assertEquals(new ZipShort(3), unix.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix unix = new X7875_NewUnix();
        assertEquals(new ZipShort(3), unix.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1000);
        unix.setGID(1000);
        byte[] expectedData = {(byte) 1, (byte) 1, (byte) 0xE8, (byte) 3, (byte) 1, (byte) 0xE8};
        assertArrayEquals(expectedData, unix.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        X7875_NewUnix unix = new X7875_NewUnix();
        assertArrayEquals(new byte[0], unix.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        X7875_NewUnix unix = new X7875_NewUnix();
        byte[] data = {(byte) 1, (byte) 0xE8, (byte) 3, (byte) 1, (byte) 0xE8};
        unix.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1, unix.getUID());
        assertEquals(1, unix.getGID());
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        X7875_NewUnix unix = new X7875_NewUnix();
        byte[] data = {(byte) 1, (byte) 0xE8, (byte) 3, (byte) 1, (byte) 0xE8};
        unix.parseFromCentralDirectoryData(data, 0, data.length);
    }

    @Test
    public void testToString() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1000);
        unix.setGID(1000);
        assertEquals("0x7875 Zip Extra Field: UID=1000 GID=1000", unix.toString());
    }

    @Test
    public void testEquals() {
        X7875_NewUnix unix1 = new X7875_NewUnix();
        unix1.setUID(1000);
        unix1.setGID(1000);
        X7875_NewUnix unix2 = new X7875_NewUnix();
        unix2.setUID(1000);
        unix2.setGID(1000);
        assertTrue(unix1.equals(unix2));
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1000);
        unix.setGID(1000);
        assertEquals(-1378268672, unix.hashCode());
    }

    @Test
    public void testTrimLeadingZeroesForceMinLength() {
        byte[] array = {0, 0, 0, 1};
        byte[] expectedArray = {1};
        assertArrayEquals(expectedArray, X7875_NewUnix.trimLeadingZeroesForceMinLength(array));
    }
}