import org.junit.*;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {

    @Test
    public void testConstructor() {
        X7875_NewUnix unix = new X7875_NewUnix();
        assertNotNull(unix);
        assertEquals(1, unix.getUID());
        assertEquals(1, unix.getGID());
    }

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix unix = new X7875_NewUnix();
        assertEquals(new ZipShort(0x7875), unix.getHeaderId());
    }

    @Test
    public void testGetUID() {
        X7875_NewUnix unix = new X7875_NewUnix();
        assertEquals(1, unix.getUID());
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix unix = new X7875_NewUnix();
        assertEquals(1, unix.getGID());
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
        byte[] expectedData = {1, 0, 0, 1, 0, 0};
        assertArrayEquals(expectedData, unix.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        X7875_NewUnix unix = new X7875_NewUnix();
        assertArrayEquals(new byte[0], unix.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() {
        X7875_NewUnix unix = new X7875_NewUnix();
        byte[] data = {1, 0, 0, 1, 0, 0};
        unix.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1, unix.getUID());
        assertEquals(1, unix.getGID());
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        X7875_NewUnix unix = new X7875_NewUnix();
        byte[] data = {};
        unix.parseFromCentralDirectoryData(data, 0, data.length);
        assertEquals(1, unix.getUID());
        assertEquals(1, unix.getGID());
    }

    @Test
    public void testReset() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1000);
        unix.setGID(1000);
        unix.reset();
        assertEquals(1, unix.getUID());
        assertEquals(1, unix.getGID());
    }

    @Test
    public void testToString() {
        X7875_NewUnix unix = new X7875_NewUnix();
        assertEquals("0x7875 Zip Extra Field: UID=1 GID=1", unix.toString());
    }

    @Test
    public void testEquals() {
        X7875_NewUnix unix1 = new X7875_NewUnix();
        X7875_NewUnix unix2 = new X7875_NewUnix();
        X7875_NewUnix unix3 = new X7875_NewUnix();
        unix3.setUID(1000);
        unix3.setGID(1000);

        assertTrue(unix1.equals(unix1));
        assertTrue(unix1.equals(unix2));
        assertFalse(unix1.equals(unix3));
        assertFalse(unix1.equals(null));
        assertFalse(unix1.equals("string"));
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix unix = new X7875_NewUnix();
        assertEquals(-1234567, unix.hashCode());
    }

    @Test
    public void testTrimLeadingZeroesForceMinLength() throws Exception {
        byte[] array = {0, 1, 0};
        byte[] expectedArray = {1, 0};
        byte[] trimmedArray = X7875_NewUnix.trimLeadingZeroesForceMinLength(array);
        assertArrayEquals(expectedArray, trimmedArray);
    }
}