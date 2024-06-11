import org.junit.Test;
import static org.junit.Assert.*;

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
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(1000);
        x7875.setGID(1000);
        assertEquals(11, x7875.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(3, x7875.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testParseFromLocalFileData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] data = {1, 4, 0, 0, 0, 0, 1, 9, 1, 0, 0, 0, 0};
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
}