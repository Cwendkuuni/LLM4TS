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
        unix.setUID(1000);
        assertEquals(1000, unix.getUID());
    }

    @Test
    public void testSetGID() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setGID(1000);
        assertEquals(1000, unix.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1000);
        unix.setGID(1000);
        assertEquals(new ZipShort(6), unix.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1000);
        unix.setGID(1000);
        assertEquals(new ZipShort(6), unix.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1000);
        unix.setGID(1000);
        byte[] expected = {1, 4, 1, -24, 1, -24};
        assertArrayEquals(expected, unix.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        X7875_NewUnix unix = new X7875_NewUnix();
        byte[] expected = {};
        assertArrayEquals(expected, unix.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() throws Exception {
        X7875_NewUnix unix = new X7875_NewUnix();
        byte[] data = {1, 4, 1, -24, 1, -24};
        unix.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1000, unix.getUID());
        assertEquals(1000, unix.getGID());
    }

    @Test
    public void testParseFromCentralDirectoryData() throws Exception {
        X7875_NewUnix unix = new X7875_NewUnix();
        byte[] data = {};
        unix.parseFromCentralDirectoryData(data, 0, data.length);
        // No assertions as this method does not change the state of the object
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

        int expected = -1234567 * 1 ^ Integer.rotateLeft(unix.getUID().hashCode(), 16) ^ unix.getGID().hashCode();
        assertEquals(expected, unix.hashCode());
    }

    @Test
    public void testTrimLeadingZeroesForceMinLength() throws Exception {
        byte[] array = {0, 0, 0, 0, 1, 2, 3};
        byte[] expected = {0, 1, 2, 3};
        assertArrayEquals(expected, X7875_NewUnix.trimLeadingZeroesForceMinLength(array));
    }
}