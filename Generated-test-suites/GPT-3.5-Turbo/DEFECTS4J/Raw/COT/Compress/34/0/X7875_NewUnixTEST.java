import org.junit.Test;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {

    @Test
    public void testConstructor() {
        X7875_NewUnix unix = new X7875_NewUnix();
        assertEquals(1, unix.version);
        assertEquals(1000, unix.getUID());
        assertEquals(1000, unix.getGID());
    }

    @Test
    public void testSetUID() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(500);
        assertEquals(500, unix.getUID());
    }

    @Test
    public void testSetGID() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setGID(500);
        assertEquals(500, unix.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix unix = new X7875_NewUnix();
        assertEquals(6, unix.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix unix = new X7875_NewUnix();
        assertEquals(6, unix.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix unix = new X7875_NewUnix();
        byte[] expected = { 1, 1, 4, 0, 1, 0 };
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
        byte[] data = { 1, 1, 4, 0, 1, 0 };
        unix.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1, unix.version);
        assertEquals(1000, unix.getUID());
        assertEquals(1000, unix.getGID());
    }

    @Test
    public void testEquals() {
        X7875_NewUnix unix1 = new X7875_NewUnix();
        X7875_NewUnix unix2 = new X7875_NewUnix();
        assertTrue(unix1.equals(unix2));
        unix2.setUID(500);
        assertFalse(unix1.equals(unix2));
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix unix1 = new X7875_NewUnix();
        X7875_NewUnix unix2 = new X7875_NewUnix();
        assertEquals(unix1.hashCode(), unix2.hashCode());
        unix2.setUID(500);
        assertNotEquals(unix1.hashCode(), unix2.hashCode());
    }

}