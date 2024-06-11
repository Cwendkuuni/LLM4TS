import org.junit.Test;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {

    @Test
    public void testConstructor() {
        X7875_NewUnix unix = new X7875_NewUnix();
        assertNotNull(unix);
        assertEquals(1, unix.getVersion());
        assertEquals(1000, unix.getUID());
        assertEquals(1000, unix.getGID());
    }

    @Test
    public void testSetterAndGetter() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(2000);
        unix.setGID(3000);

        assertEquals(2000, unix.getUID());
        assertEquals(3000, unix.getGID());
    }

    @Test
    public void testLocalFileDataLength() {
        X7875_NewUnix unix = new X7875_NewUnix();
        assertEquals(5, unix.getLocalFileDataLength().getValue());
    }

    @Test
    public void testCentralDirectoryLength() {
        X7875_NewUnix unix = new X7875_NewUnix();
        assertEquals(5, unix.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testLocalFileDataData() {
        X7875_NewUnix unix = new X7875_NewUnix();
        byte[] data = unix.getLocalFileDataData();
        assertNotNull(data);
        assertEquals(5, data.length);
    }

    @Test
    public void testParseFromLocalFileData() {
        X7875_NewUnix unix = new X7875_NewUnix();
        byte[] data = {1, 1, 2, 3, 4, 1, 5};
        unix.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1, unix.getVersion());
        assertEquals(16909060, unix.getUID());
        assertEquals(5, unix.getGID());
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        X7875_NewUnix unix = new X7875_NewUnix();
        byte[] buffer = {};
        unix.parseFromCentralDirectoryData(buffer, 0, 0);
        // no exception should be thrown
        assertTrue(true);
    }

    @Test
    public void testEquals() {
        X7875_NewUnix unix1 = new X7875_NewUnix();
        X7875_NewUnix unix2 = new X7875_NewUnix();
        unix1.setUID(2000);
        unix2.setUID(2000);
        assertTrue(unix1.equals(unix2));
    }
}