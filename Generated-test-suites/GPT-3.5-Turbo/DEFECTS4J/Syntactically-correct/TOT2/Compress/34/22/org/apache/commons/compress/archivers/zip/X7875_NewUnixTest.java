import static org.junit.Assert.*;
import org.junit.Test;

public class X7875_NewUnixTest {

    // Test case for getUID method
    @Test
    public void testGetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(123L);
        assertEquals(123L, x7875.getUID());
    }

    // Test case for getGID method
    @Test
    public void testGetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setGID(456L);
        assertEquals(456L, x7875.getGID());
    }

    // Test case for setUID method
    @Test
    public void testSetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(789L);
        assertEquals(789L, x7875.getUID());
    }

    // Test case for setGID method
    @Test
    public void testSetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setGID(101112L);
        assertEquals(101112L, x7875.getGID());
    }

    // Test case for parseFromLocalFileData method
    @Test
    public void testParseFromLocalFileData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] data = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
        x7875.parseFromLocalFileData(data, 0, data.length);
        assertEquals(0x01, x7875.getUID());
        assertEquals(0x02030405060708L, x7875.getGID());
    }

    // Test case for getLocalFileDataData method
    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(32768L);
        x7875.setGID(65536L);
        byte[] expectedData = {0x01, 0x02, (byte) 0x80, 0x01, 0x03, (byte) 0x00, 0x00};
        assertArrayEquals(expectedData, x7875.getLocalFileDataData());
    }

    // Test case for equals method
    @Test
    public void testEquals() {
        X7875_NewUnix x7875_1 = new X7875_NewUnix();
        x7875_1.setUID(777L);
        x7875_1.setGID(888L);
        
        X7875_NewUnix x7875_2 = new X7875_NewUnix();
        x7875_2.setUID(777L);
        x7875_2.setGID(888L);
        
        assertTrue(x7875_1.equals(x7875_2));
    }

    // Test case for hashCode method
    @Test
    public void testHashCode() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(555L);
        x7875.setGID(444L);
        
        X7875_NewUnix x7875_2 = new X7875_NewUnix();
        x7875_2.setUID(555L);
        x7875_2.setGID(444L);
        
        assertEquals(x7875.hashCode(), x7875_2.hashCode());
    }

}