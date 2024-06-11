import static org.junit.Assert.*;
import org.junit.Test;

public class X7875_NewUnixTest {

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
    public void testSetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(500);
        assertEquals(500, x7875.getUID());
    }

    @Test
    public void testSetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setGID(500);
        assertEquals(500, x7875.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(1000);
        x7875.setGID(1000);
        assertEquals(7, x7875.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(0, x7875.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testLocalFileDataData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(1000);
        x7875.setGID(1000);
        byte[] expected = new byte[]{1, 1, 0, 3, -24, -46, 1, 0};
        assertArrayEquals(expected, x7875.getLocalFileDataData());
    }

    @Test
    public void testParseFromLocalFileData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] data = new byte[]{1, 1, 0, 3, -24, -46, 1, 0};
        x7875.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1000, x7875.getUID());
        assertEquals(1000, x7875.getGID());
    }

    @Test
    public void testToString() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(1000);
        x7875.setGID(1000);
        assertEquals("0x7875 Zip Extra Field: UID=1000 GID=1000", x7875.toString());
    }

}