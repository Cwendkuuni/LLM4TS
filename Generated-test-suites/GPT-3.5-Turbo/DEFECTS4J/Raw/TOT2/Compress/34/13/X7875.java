import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class X7875_NewUnixTest {

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix unix = new X7875_NewUnix();
        assertEquals(0x7875, unix.getHeaderId().getValue());
    }

    @Test
    public void testGetUID() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1000L);
        assertEquals(1000L, unix.getUID());
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setGID(1000L);
        assertEquals(1000L, unix.getGID());
    }

    @Test
    public void testSetUID() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(2000L);
        assertEquals(2000L, unix.getUID());
    }

    @Test
    public void testSetGID() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setGID(2000L);
        assertEquals(2000L, unix.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix unix = new X7875_NewUnix();
        assertEquals(3, unix.getLocalFileDataLength().getValue());
        unix.setUID(1L);
        unix.setGID(1L);
        assertEquals(5, unix.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix unix = new X7875_NewUnix();
        assertEquals(3, unix.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testParseFromLocalFileData() {
        X7875_NewUnix unix = new X7875_NewUnix();
        byte[] data = new byte[]{0x01, 0x01, 0x01, 0x00, 0x00, 0x00};
        unix.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1L, unix.getUID());
        assertEquals(1L, unix.getGID());
    }

}