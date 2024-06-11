import static org.junit.Assert.*;
import org.junit.Test;

public class X7875_NewUnixTest {

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(0x7875, x7875.getHeaderId().getValue());
    }

    @Test
    public void testGetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(1000);
        assertEquals(1000, x7875.getUID());
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setGID(1000);
        assertEquals(1000, x7875.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(3, x7875.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(3, x7875.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(1000);
        x7875.setGID(1000);
        byte[] expectedData = {1, 1, 3, 232, 3, 232}; // Example data for UID=1000 and GID=1000
        assertArrayEquals(expectedData, x7875.getLocalFileDataData());
    }

    @Test
    public void testParseFromLocalFileData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] data = {1, 1, 3, 232, 3, 232}; // Example data for UID=1000 and GID=1000
        x7875.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1000, x7875.getUID());
        assertEquals(1000, x7875.getGID());
    }

    @Test
    public void testEquals() {
        X7875_NewUnix x7875_1 = new X7875_NewUnix();
        X7875_NewUnix x7875_2 = new X7875_NewUnix();
        x7875_1.setUID(1000);
        x7875_1.setGID(1000);
        x7875_2.setUID(1000);
        x7875_2.setGID(1000);
        assertTrue(x7875_1.equals(x7875_2));
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(1000);
        x7875.setGID(1000);
        assertEquals(157020221, x7875.hashCode());
    }
}