import org.junit.Test;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {

    // Test for getUID
    @Test
    public void testGetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(1000);
        assertEquals(1000, x7875.getUID());
    }

    // Test for getGID
    @Test
    public void testGetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setGID(1000);
        assertEquals(1000, x7875.getGID());
    }

    // Test for setUID
    @Test
    public void testSetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(1000);
        assertEquals(1000, x7875.getUID());
    }

    // Test for setGID
    @Test
    public void testSetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setGID(1000);
        assertEquals(1000, x7875.getGID());
    }

    // Test for getLocalFileDataLength
    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(3, x7875.getLocalFileDataLength().getValue());
    }

    // Test for getCentralDirectoryLength
    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(3, x7875.getCentralDirectoryLength().getValue());
    }

    // Test for parseFromLocalFileData
    @Test
    public void testParseFromLocalFileData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] data = {0x01, 0x01, 0x02, 0x03, 0x03, 0x04};
        x7875.parseFromLocalFileData(data, 0, data.length);
        assertEquals(259, x7875.getUID());
        assertEquals(772, x7875.getGID());
    }

    // Test for equals
    @Test
    public void testEquals() {
        X7875_NewUnix x7875A = new X7875_NewUnix();
        X7875_NewUnix x7875B = new X7875_NewUnix();
        assertTrue(x7875A.equals(x7875B));
    }
}