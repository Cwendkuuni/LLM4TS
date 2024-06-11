import static org.junit.Assert.*;
import org.junit.Test;

public class X7875_NewUnixTest {
    @Test
    public void testGetHeaderId() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        assertEquals(new ZipShort(0x7875), x7875_NewUnix.getHeaderId());
    }

    @Test
    public void testGetUID() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        x7875_NewUnix.setUID(100);
        assertEquals(100, x7875_NewUnix.getUID());
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        x7875_NewUnix.setGID(200);
        assertEquals(200, x7875_NewUnix.getGID());
    }

    @Test
    public void testSetUID() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        x7875_NewUnix.setUID(300);
        assertEquals(300, x7875_NewUnix.getUID());
    }

    @Test
    public void testSetGID() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        x7875_NewUnix.setGID(400);
        assertEquals(400, x7875_NewUnix.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        ZipShort expectedLength = new ZipShort(3);
        assertEquals(expectedLength, x7875_NewUnix.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        ZipShort expectedLength = new ZipShort(3);
        assertEquals(expectedLength, x7875_NewUnix.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        byte[] expectedData = {1, 0, 0};
        assertArrayEquals(expectedData, x7875_NewUnix.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        byte[] expectedData = {};
        assertArrayEquals(expectedData, x7875_NewUnix.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() throws Exception {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        byte[] data = {1, 0, 0};
        x7875_NewUnix.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1, x7875_NewUnix.getUID());
        assertEquals(0, x7875_NewUnix.getGID());
    }

    @Test
    public void testParseFromCentralDirectoryData() throws Exception {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        byte[] data = {};
        x7875_NewUnix.parseFromCentralDirectoryData(data, 0, data.length);
    }

    @Test
    public void testToString() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        String expectedString = "0x7875 Zip Extra Field: UID=1000 GID=1000";
        assertEquals(expectedString, x7875_NewUnix.toString());
    }

    @Test
    public void testEquals() {
        X7875_NewUnix x7875_NewUnix1 = new X7875_NewUnix();
        X7875_NewUnix x7875_NewUnix2 = new X7875_NewUnix();
        assertTrue(x7875_NewUnix1.equals(x7875_NewUnix2));
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix x7875_NewUnix = new X7875_NewUnix();
        int expectedHashCode = -1234567 * 1 ^ Integer.rotateLeft(1000, 16) ^ 1000;
        assertEquals(expectedHashCode, x7875_NewUnix.hashCode());
    }
}