import org.junit.Test;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {
    
    @Test
    public void testGetHeaderId() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        assertEquals(0x7875, extraField.getHeaderId().getValue());
    }
    
    @Test
    public void testGetUID() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        extraField.setUID(123456);
        assertEquals(123456, extraField.getUID());
    }
    
    @Test
    public void testGetGID() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        extraField.setGID(789);
        assertEquals(789, extraField.getGID());
    }
    
    @Test
    public void testSetUID() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        extraField.setUID(10000);
        assertEquals(10000, extraField.getUID());
    }
    
    @Test
    public void testSetGID() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        extraField.setGID(2000);
        assertEquals(2000, extraField.getGID());
    }
    
    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        assertEquals(3, extraField.getLocalFileDataLength().getValue());
    }
    
    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        assertEquals(3, extraField.getCentralDirectoryLength().getValue());
    }
    
    // Additional tests for edge cases and exception handling can be added here
    
}