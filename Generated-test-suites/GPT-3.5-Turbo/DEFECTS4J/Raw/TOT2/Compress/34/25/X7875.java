import static org.junit.Assert.*;
import org.junit.*;

public class X7875_NewUnixTest {

    private X7875_NewUnix unix;

    @Before
    public void setUp() {
        unix = new X7875_NewUnix();
    }

    @Test
    public void testGetHeaderId() {
        assertEquals(new ZipShort(0x7875), unix.getHeaderId());
    }

    @Test
    public void testGetUID() {
        unix.setUID(1001);
        assertEquals(1001, unix.getUID());
    }

    @Test
    public void testGetGID() {
        unix.setGID(1002);
        assertEquals(1002, unix.getGID());
    }

    @Test
    public void testSetUID() {
        unix.setUID(2001);
        assertEquals(2001, unix.getUID());
    }

    @Test
    public void testSetGID() {
        unix.setGID(2002);
        assertEquals(2002, unix.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        ZipShort expectedLength = new ZipShort(9);
        assertEquals(expectedLength, unix.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        ZipShort expectedLength = new ZipShort(0);
        assertEquals(expectedLength, unix.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        byte[] expectedData = {1, 2, 0, 1, 0, 2};
        unix.setUID(1);
        unix.setGID(2);
        assertArrayEquals(expectedData, unix.getLocalFileDataData());
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] data = {1, 2, 0, 1, 0, 2};
        unix.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1, unix.getUID());
        assertEquals(2, unix.getGID());
    }

    @Test
    public void testToString() {
        unix.setUID(100);
        unix.setGID(200);
        assertEquals("0x7875 Zip Extra Field: UID=100 GID=200", unix.toString());
    }

    @Test
    public void testEquals() {
        X7875_NewUnix otherUnix = new X7875_NewUnix();
        otherUnix.setUID(100);
        otherUnix.setGID(200);
        unix.setUID(100);
        unix.setGID(200);
        assertTrue(unix.equals(otherUnix));
    }

    @Test
    public void testHashCode() {
        unix.setUID(100);
        unix.setGID(200);
        X7875_NewUnix otherUnix = new X7875_NewUnix();
        otherUnix.setUID(100);
        otherUnix.setGID(200);
        assertEquals(unix.hashCode(), otherUnix.hashCode());
    }

    @Test
    public void testClone() {
        try {
            X7875_NewUnix clonedUnix = (X7875_NewUnix) unix.clone();
            assertNotSame(unix, clonedUnix);
            assertEquals(unix.toString(), clonedUnix.toString());
        } catch (CloneNotSupportedException e) {
            fail("Clone not supported");
        }
    }

    @Test
    public void testTrimLeadingZeroesForceMinLength() {
        byte[] array = {0, 0, 1, 2};
        byte[] expectedArray = {1, 2};
        assertArrayEquals(expectedArray, X7875_NewUnix.trimLeadingZeroesForceMinLength(array));
    }
}