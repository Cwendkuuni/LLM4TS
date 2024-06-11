import static org.junit.Assert.*;
import org.junit.Test;

public class X7875_NewUnixTest {

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(0x7875, x7875.getHeaderId().getValue());
    }

    @Test
    public void testGetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(12345);
        assertEquals(12345, x7875.getUID());
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setGID(54321);
        assertEquals(54321, x7875.getGID());
    }

    @Test
    public void testSetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(9876);
        assertEquals(9876, x7875.getUID());
    }

    @Test
    public void testSetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setGID(6789);
        assertEquals(6789, x7875.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(3, x7875.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(3, x7875.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(123);
        x7875.setGID(456);
        byte[] expected = { 1, 1, 123, 1, 456 };
        assertArrayEquals(expected, x7875.getLocalFileDataData());
    }

    @Test
    public void testParseFromLocalFileData() throws Exception {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] data = { 1, 1, 123, 1, 456 };
        x7875.parseFromLocalFileData(data, 0, data.length);
        assertEquals(123, x7875.getUID());
        assertEquals(456, x7875.getGID());
    }

    @Test
    public void testEquals() {
        X7875_NewUnix x7875_1 = new X7875_NewUnix();
        X7875_NewUnix x7875_2 = new X7875_NewUnix();
        assertTrue(x7875_1.equals(x7875_2));
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertNotNull(x7875.hashCode());
    }
}