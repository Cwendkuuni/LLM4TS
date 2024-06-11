import static org.junit.Assert.*;
import org.junit.Test;

public class X7875_NewUnixTest {
  
    @Test
    public void testGetHeaderId() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(new ZipShort(0x7875), x7875.getHeaderId());
    }
    
    @Test
    public void testGetUIDAndGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(1000L, x7875.getUID());
        assertEquals(1000L, x7875.getGID());
    }
    
    @Test
    public void testSetUIDAndGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(2000L);
        x7875.setGID(3000L);
        assertEquals(2000L, x7875.getUID());
        assertEquals(3000L, x7875.getGID());
    }
    
    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(new ZipShort(7), x7875.getLocalFileDataLength());
    }
    
    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(new ZipShort(7), x7875.getCentralDirectoryLength());
    }
    
    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] expected = {1, 1, 8, -24, 1, 8, -24};
        assertArrayEquals(expected, x7875.getLocalFileDataData());
    }
    
    @Test
    public void testGetCentralDirectoryData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] expected = {};
        assertArrayEquals(expected, x7875.getCentralDirectoryData());
    }
    
    @Test
    public void testParseFromLocalFileData() throws ZipException {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] data = {1, 1, 8, -24, 1, 8, -24};
        x7875.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1, x7875.getUID());
        assertEquals(1, x7875.getGID());
    }
    
    @Test
    public void testEquals() {
        X7875_NewUnix x7875_1 = new X7875_NewUnix();
        X7875_NewUnix x7875_2 = new X7875_NewUnix();
        assertEquals(x7875_1, x7875_2);
    }
    
    @Test
    public void testHashCode() {
        X7875_NewUnix x7875_1 = new X7875_NewUnix();
        X7875_NewUnix x7875_2 = new X7875_NewUnix();
        assertEquals(x7875_1.hashCode(), x7875_2.hashCode());
    }

}