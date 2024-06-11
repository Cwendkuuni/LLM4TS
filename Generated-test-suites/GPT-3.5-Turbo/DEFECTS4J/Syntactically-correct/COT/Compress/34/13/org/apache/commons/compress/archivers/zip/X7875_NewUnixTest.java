
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
        assertEquals(new ZipShort(5), unix.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1000);
        unix.setGID(1000);
        assertEquals(new ZipShort(5), unix.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1000);
        unix.setGID(1000);
        byte[] expected = {1, 2, 3, 4, 5};
        assertArrayEquals(expected, unix.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        X7875_NewUnix unix = new X7875_NewUnix();
        assertEquals(new byte[0], unix.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() {
        X7875_NewUnix unix = new X7875_NewUnix();
        byte[] data = {1, 2, 3, 4, 5};
        unix.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1, unix.getUID());
        assertEquals(2, unix.getGID());
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        X7875_NewUnix unix = new X7875_NewUnix();
        byte[] data = {1, 2, 3, 4, 5};
        unix.parseFromCentralDirectoryData(data, 0, data.length);
    }

    @Test
    public void testToString() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1000);
        unix.setGID(1000);
        assertEquals("0x7875 Zip Extra Field: UID=1000 GID=1000", unix.toString());
    }
}
