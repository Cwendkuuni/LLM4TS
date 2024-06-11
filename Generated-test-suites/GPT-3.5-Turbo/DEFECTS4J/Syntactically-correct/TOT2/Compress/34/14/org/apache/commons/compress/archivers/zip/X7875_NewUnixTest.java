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
        x7875.setGID(500);
        assertEquals(500, x7875.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(6, x7875.getLocalFileDataLength().getValue());
    }

    @Test
    public void testSetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(2000);
        assertEquals(2000, x7875.getUID());
    }

    @Test
    public void testSetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setGID(100);
        assertEquals(100, x7875.getGID());
    }

    @Test
    public void testParseFromLocalFileData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] data = {0x01, 0x02, 0x04, 0x0A, 0x0B};
        x7875.parseFromLocalFileData(data, 0, data.length);
        assertEquals(258, x7875.getUID());
        assertEquals(2667, x7875.getGID());
    }

    @Test
    public void testToString() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals("0x7875 Zip Extra Field: UID=1000 GID=1000", x7875.toString());
    }
}