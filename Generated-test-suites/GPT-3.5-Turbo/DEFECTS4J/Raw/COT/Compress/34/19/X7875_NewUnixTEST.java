import org.junit.Test;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {
    @Test
    public void testGetHeaderId() {
        X7875_NewUnix unix = new X7875_NewUnix();
        assertEquals(new ZipShort(0x7875), unix.getHeaderId());
    }
    
    @Test
    public void testGetUID() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1000);
        assertEquals(1000, unix.getUID());
    }
    
    @Test
    public void testGetGID() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setGID(1000);
        assertEquals(1000, unix.getGID());
    }
    
    @Test
    public void testSetUID() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1000);
        assertEquals(1000, unix.getUID());
    }
    
    @Test
    public void testSetGID() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setGID(1000);
        assertEquals(1000, unix.getGID());
    }
    
    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix unix = new X7875_NewUnix();
        ZipShort expected = new ZipShort(6);
        assertEquals(expected, unix.getLocalFileDataLength());
    }
    
    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix unix = new X7875_NewUnix();
        ZipShort expected = new ZipShort(6);
        assertEquals(expected, unix.getCentralDirectoryLength());
    }
    
    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix unix = new X7875_NewUnix();
        byte[] expected = new byte[] {1, 0, 1, 0, 1, 0};
        unix.setUID(256);
        unix.setGID(256);
        assertArrayEquals(expected, unix.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        X7875_NewUnix unix = new X7875_NewUnix();
        byte[] expected = new byte[0];
        assertArrayEquals(expected, unix.getCentralDirectoryData());
    }
    
    @Test
    public void testParseFromLocalFileData() throws ZipException {
        X7875_NewUnix unix = new X7875_NewUnix();
        byte[] data = new byte[] {1, 0, 1, 0, 1, 0};
        unix.parseFromLocalFileData(data, 0, data.length);
        assertEquals(256, unix.getUID());
        assertEquals(256, unix.getGID());
    }
    
    @Test
    public void testClone() throws CloneNotSupportedException {
        X7875_NewUnix unix1 = new X7875_NewUnix();
        unix1.setUID(1000);
        unix1.setGID(1000);
        X7875_NewUnix unix2 = (X7875_NewUnix) unix1.clone();
        assertEquals(unix1, unix2);
    }
    
    @Test
    public void testEquals() {
        X7875_NewUnix unix1 = new X7875_NewUnix();
        unix1.setUID(1000);
        unix1.setGID(1000);
        X7875_NewUnix unix2 = new X7875_NewUnix();
        unix2.setUID(1000);
        unix2.setGID(1000);
        assertTrue(unix1.equals(unix2));
    }
    
    @Test
    public void testHashCode() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1000);
        unix.setGID(1000);
        int expected = -1234567 * 1;
        expected ^= Integer.rotateLeft(1000, 16);
        expected ^= 1000;
        assertEquals(expected, unix.hashCode());
    }
    
    @Test
    public void testToString() {
        X7875_NewUnix unix = new X7875_NewUnix();
        unix.setUID(1000);
        unix.setGID(1000);
        String expected = "0x7875 Zip Extra Field: UID=1000 GID=1000";
        assertEquals(expected, unix.toString());
    }
}