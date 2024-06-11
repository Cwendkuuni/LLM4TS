import static org.junit.Assert.*;
import org.junit.Test;

public class X7875_NewUnixTest {

    @Test
    public void testGetUID() {
        X7875_NewUnix x = new X7875_NewUnix();
        x.setUID(1000);
        assertEquals(1000, x.getUID());
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix x = new X7875_NewUnix();
        x.setGID(1000);
        assertEquals(1000, x.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x = new X7875_NewUnix();
        x.setUID(1000);
        x.setGID(1000);
        assertEquals(7, x.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x = new X7875_NewUnix();
        x.setUID(1000);
        x.setGID(1000);
        assertEquals(7, x.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix x = new X7875_NewUnix();
        x.setUID(1000);
        x.setGID(1000);
        byte[] expected = {1, 1, 0, 1, 1, 0, 1};
        assertArrayEquals(expected, x.getLocalFileDataData());
    }

    @Test
    public void testParseFromLocalFileData() {
        X7875_NewUnix x = new X7875_NewUnix();
        byte[] data = {1, 1, 0, 1, 1, 0, 1};
        x.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1000, x.getUID());
        assertEquals(1000, x.getGID());
    }

    @Test
    public void testEquals() {
        X7875_NewUnix x1 = new X7875_NewUnix();
        X7875_NewUnix x2 = new X7875_NewUnix();
        x1.setUID(1000);
        x1.setGID(1000);
        x2.setUID(1000);
        x2.setGID(1000);
        assertTrue(x1.equals(x2));
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix x = new X7875_NewUnix();
        x.setUID(1000);
        x.setGID(1000);
        assertEquals(-1409727501, x.hashCode());
    }
}