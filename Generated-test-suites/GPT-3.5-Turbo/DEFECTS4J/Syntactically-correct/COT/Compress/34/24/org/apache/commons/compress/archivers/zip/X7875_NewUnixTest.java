import org.junit.Test;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix x = new X7875_NewUnix();
        assertEquals(0x7875, x.getHeaderId().getValue());
    }

    @Test
    public void testGetUID() {
        X7875_NewUnix x = new X7875_NewUnix();
        x.setUID(100);
        assertEquals(100, x.getUID());
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix x = new X7875_NewUnix();
        x.setGID(200);
        assertEquals(200, x.getGID());
    }

    @Test
    public void testSetUID() {
        X7875_NewUnix x = new X7875_NewUnix();
        x.setUID(100);
        assertEquals(100, x.getUID());
    }

    @Test
    public void testSetGID() {
        X7875_NewUnix x = new X7875_NewUnix();
        x.setGID(200);
        assertEquals(200, x.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x = new X7875_NewUnix();
        x.setUID(100);
        x.setGID(200);
        assertEquals(7, x.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x = new X7875_NewUnix();
        x.setUID(100);
        x.setGID(200);
        assertEquals(7, x.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix x = new X7875_NewUnix();
        x.setUID(100);
        x.setGID(200);
        byte[] expected = {1, 1, 100, 1, 200};
        assertArrayEquals(expected, x.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        X7875_NewUnix x = new X7875_NewUnix();
        byte[] expected = {};
        assertArrayEquals(expected, x.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        X7875_NewUnix x = new X7875_NewUnix();
        byte[] data = {1, 1, 100, 1, 200};
        x.parseFromLocalFileData(data, 0, data.length);
        assertEquals(100, x.getUID());
        assertEquals(200, x.getGID());
    }

    @Test
    public void testReset() {
        X7875_NewUnix x = new X7875_NewUnix();
        x.setUID(100);
        x.setGID(200);
        x.reset();
        assertEquals(1000, x.getUID());
        assertEquals(1000, x.getGID());
    }

    @Test
    public void testToString() {
        X7875_NewUnix x = new X7875_NewUnix();
        x.setUID(100);
        x.setGID(200);
        assertEquals("0x7875 Zip Extra Field: UID=100 GID=200", x.toString());
    }

    @Test
    public void testEquals() {
        X7875_NewUnix x1 = new X7875_NewUnix();
        x1.setUID(100);
        x1.setGID(200);
        X7875_NewUnix x2 = new X7875_NewUnix();
        x2.setUID(100);
        x2.setGID(200);
        assertEquals(x1, x2);
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix x = new X7875_NewUnix();
        x.setUID(100);
        x.setGID(200);
        assertEquals(1995672, x.hashCode());
    }
}