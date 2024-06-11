import org.junit.Test;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {
    
    @Test
    public void testGetHeaderId() {
        X7875_NewUnix unix = new X7875_NewUnix();
        assertEquals(0x7875, unix.getHeaderId().getValue());
    }

    @Test
    public void testGetUID() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1001);
        assertEquals(1001, unix.getUID());
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setGID(1001);
        assertEquals(1001, unix.getGID());
    }

    @Test
    public void testSetUID() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1001);
        assertEquals(1001, unix.getUID());
    }

    @Test
    public void testSetGID() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setGID(1001);
        assertEquals(1001, unix.getGID());
    }

    @Test
    public void testLocalFileDataLength() {
        X7875_NewUnix unix = new X7875_NewUnix();
        assertEquals(3, unix.getLocalFileDataLength().getValue());
    }

    @Test
    public void testCentralDirectoryLength() {
        X7875_NewUnix unix = new X7875_NewUnix();
        assertEquals(3, unix.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testParseFromLocalFileData() {
        X7875_NewUnix unix = new X7875_NewUnix();
        byte[] data = {0x01, 0x01, 0x01, 0x01, 0x01, 0x01};
        unix.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1, unix.getUID());
        assertEquals(1, unix.getGID());
    }

    @Test
    public void testToString() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1001);
        unix.setGID(1001);
        assertEquals("0x7875 Zip Extra Field: UID=1001 GID=1001", unix.toString());
    }

    // Add more test cases for edge cases and exception scenarios

}