import static org.junit.Assert.*;
import org.junit.Test;

public class X7875_NewUnixTest {

    @Test
    public void testGetUID() {
        X7875_NewUnix x = new X7875_NewUnix();
        x.setUID(123);
        assertEquals(123, x.getUID());
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix x = new X7875_NewUnix();
        x.setGID(456);
        assertEquals(456, x.getGID());
    }

    @Test
    public void testSetUID() {
        X7875_NewUnix x = new X7875_NewUnix();
        x.setUID(123);
        assertEquals(123, x.getUID());
    }

    @Test
    public void testSetGID() {
        X7875_NewUnix x = new X7875_NewUnix();
        x.setGID(456);
        assertEquals(456, x.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x = new X7875_NewUnix();
        x.setUID(123);
        x.setGID(456);
        assertEquals(new ZipShort(10), x.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x = new X7875_NewUnix();
        x.setUID(123);
        x.setGID(456);
        assertEquals(new ZipShort(10), x.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix x = new X7875_NewUnix();
        x.setUID(123);
        x.setGID(456);
        byte[] expected = new byte[] {1, 4, 123, 0, 0, 0, 0, -64, 1, 0, 0};
        assertArrayEquals(expected, x.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        X7875_NewUnix x = new X7875_NewUnix();
        byte[] expected = new byte[0];
        assertArrayEquals(expected, x.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() {
        X7875_NewUnix x = new X7875_NewUnix();
        byte[] data = new byte[] {1, 4, 123, 0, 0, 0, 0, -64, 1, 0, 0};
        x.parseFromLocalFileData(data, 0, data.length);
        assertEquals(123, x.getUID());
        assertEquals(456, x.getGID());
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        X7875_NewUnix x = new X7875_NewUnix();
        byte[] buffer = new byte[0];
        x.parseFromCentralDirectoryData(buffer, 0, buffer.length);
    }

    @Test
    public void testEquals() {
        X7875_NewUnix x1 = createObject();
        X7875_NewUnix x2 = createObject();
        X7875_NewUnix x3 = new X7875_NewUnix();
        x3.setUID(789);
        x3.setGID(987);

        assertTrue(x1.equals(x2));
        assertFalse(x1.equals(x3));
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix x1 = createObject();
        X7875_NewUnix x2 = createObject();

        assertEquals(x1.hashCode(), x2.hashCode());
    }

    @Test
    public void testToString() {
        X7875_NewUnix x = createObject();
        String expected = "0x7875 Zip Extra Field: UID=123 GID=456";
        assertEquals(expected, x.toString());
    }

    private X7875_NewUnix createObject() {
        X7875_NewUnix x = new X7875_NewUnix();
        x.setUID(123);
        x.setGID(456);
        return x;
    }
}