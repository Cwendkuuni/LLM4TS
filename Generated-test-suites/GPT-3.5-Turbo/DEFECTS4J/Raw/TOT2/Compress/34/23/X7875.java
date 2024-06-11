import org.junit.Test;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {
    
    @Test
    public void testGetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(1001);
        assertEquals(1001, x7875.getUID());
    }
    
    @Test
    public void testGetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setGID(1001);
        assertEquals(1001, x7875.getGID());
    }
    
    @Test
    public void testLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(1001);
        x7875.setGID(1001);
        assertEquals(5, x7875.getLocalFileDataLength().getValue());
    }
    
    @Test
    public void testCentralDirectoryLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(0, x7875.getCentralDirectoryLength().getValue());
    }
    
    @Test
    public void testParseFromLocalFileData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] data = {1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0};
        x7875.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1, x7875.getUID());
        assertEquals(1, x7875.getGID());
    }
}