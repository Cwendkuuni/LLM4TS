import org.junit.Before;
import org.junit.Test;

public class X7875_NewUnixTest {

    private X7875_NewUnix newUnix;

    @Before
    public void setup() {
        newUnix = new X7875_NewUnix();
    }

    @Test
    public void testGetHeaderId() {
        assertEquals(new ZipShort(0x7875), newUnix.getHeaderId());
    }

    @Test
    public void testGetUID() {
        newUnix.setUID(100);
        assertEquals(100, newUnix.getUID());
    }

    @Test
    public void testGetGID() {
        newUnix.setGID(200);
        assertEquals(200, newUnix.getGID());
    }

    @Test
    public void testSetUID() {
        newUnix.setUID(100);
        assertEquals(100, newUnix.getUID());
    }

    @Test
    public void testSetGID() {
        newUnix.setGID(200);
        assertEquals(200, newUnix.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        newUnix.setUID(100);
        newUnix.setGID(200);
        assertEquals(new ZipShort(10), newUnix.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        newUnix.setUID(100);
        newUnix.setGID(200);
        assertEquals(new ZipShort(10), newUnix.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        newUnix.setUID(100);
        newUnix.setGID(200);
        byte[] expectedData = new byte[]{1, 1, 100, 1, 200};
        assertArrayEquals(expectedData, newUnix.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        assertArrayEquals(new byte[0], newUnix.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] data = new byte[]{1, 1, 100, 1, 200};
        newUnix.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1, newUnix.version);
        assertEquals(100, newUnix.getUID());
        assertEquals(200, newUnix.getGID());
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        byte[] data = new byte[]{};
        newUnix.parseFromCentralDirectoryData(data, 0, data.length);
        // No operation, nothing to test.
    }

    @Test
    public void testEquals() {
        X7875_NewUnix other = new X7875_NewUnix();
        other.setUID(100);
        other.setGID(200);
        assertTrue(newUnix.equals(other));
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix other = new X7875_NewUnix();
        other.setUID(100);
        other.setGID(200);
        assertEquals(newUnix.hashCode(), other.hashCode());
    }
}