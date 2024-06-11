import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {

    private static X7875_NewUnix unix;

    @BeforeClass
    public static void setUp() {
        unix = new X7875_NewUnix();
    }

    @Test
    public void testGetHeaderId() {
        assertEquals(0x7875, unix.getHeaderId().getValue());
    }

    @Test
    public void testGetUID() {
        unix.setUID(12345L);
        assertEquals(12345L, unix.getUID());
    }

    @Test
    public void testGetGID() {
        unix.setGID(67890L);
        assertEquals(67890L, unix.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        unix.setUID(12345L);
        unix.setGID(67890L);
        assertEquals(7, unix.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        unix.setUID(12345L);
        unix.setGID(67890L);
        assertEquals(7, unix.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testGetLocalFileDataData() {
        unix.setUID(12345L);
        unix.setGID(67890L);
        byte[] expected = new byte[]{1, 4, -96, 57, 0, 0};
        assertArrayEquals(expected, unix.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        assertArrayEquals(new byte[0], unix.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() throws Exception {
        byte[] data = new byte[]{1, 4, -96, 57, 0, 0};
        unix.parseFromLocalFileData(data, 0, 6);
        assertEquals(12345L, unix.getUID());
        assertEquals(67890L, unix.getGID());
    }

    @Test
    public void testParseFromCentralDirectoryData() throws Exception {
        byte[] data = new byte[]{};
        unix.parseFromCentralDirectoryData(data, 0, 0);
        assertEquals(1, unix.version);
        assertNull(unix.uid);
        assertNull(unix.gid);
    }

    @Test
    public void testEquals() {
        unix.setUID(12345L);
        unix.setGID(67890L);

        X7875_NewUnix unix2 = new X7875_NewUnix();
        unix2.setUID(12345L);
        unix2.setGID(67890L);

        assertTrue(unix.equals(unix2));
    }

    @Test
    public void testHashCode() {
        unix.setUID(12345L);
        unix.setGID(67890L);

        X7875_NewUnix unix2 = new X7875_NewUnix();
        unix2.setUID(12345L);
        unix2.setGID(67890L);

        assertEquals(unix.hashCode(), unix2.hashCode());
    }

    // additional edge cases and error scenarios
    @Test
    public void testGetUIDWhenMaxUnsignedValue() {
        unix.setUID(4294967295L);
        assertEquals(4294967295L, unix.getUID());
    }

    @Test
    public void testGetGIDWhenMaxUnsignedValue() {
        unix.setGID(4294967295L);
        assertEquals(4294967295L, unix.getGID());
    }
}