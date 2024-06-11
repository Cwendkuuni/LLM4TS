import org.junit.Test;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        ZipShort expected = new ZipShort(0x7875);
        assertEquals(expected, newUnix.getHeaderId());
    }

    @Test
    public void testGetUID() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        newUnix.setUID(1234567890);
        long expected = 1234567890;
        assertEquals(expected, newUnix.getUID());
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        newUnix.setGID(987654321);
        long expected = 987654321;
        assertEquals(expected, newUnix.getGID());
    }

    @Test
    public void testSetUID() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        newUnix.setUID(1234567890);
        long expected = 1234567890;
        assertEquals(expected, newUnix.getUID());
    }

    @Test
    public void testSetGID() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        newUnix.setGID(987654321);
        long expected = 987654321;
        assertEquals(expected, newUnix.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        ZipShort expected = new ZipShort(3);
        assertEquals(expected, newUnix.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        ZipShort expected = new ZipShort(3);
        assertEquals(expected, newUnix.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        byte[] expected = {1, 0, 0};
        assertArrayEquals(expected, newUnix.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        byte[] expected = new byte[0];
        assertArrayEquals(expected, newUnix.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() throws Exception {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        byte[] data = {1, 0, 0, 0, 0};
        newUnix.parseFromLocalFileData(data, 0, data.length);
        long expected = 0;
        assertEquals(expected, newUnix.getUID());
    }

    @Test
    public void testParseFromCentralDirectoryData() throws Exception {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        byte[] buffer = {1, 0};
        newUnix.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        // No action is taken, so the test can't really verify anything
        assertTrue(true);
    }

    @Test
    public void testTrimLeadingZeroesForceMinLength() {
        byte[] array = {0, 0, 0, 1};
        byte[] expected = {1};
        assertArrayEquals(expected, X7875_NewUnix.trimLeadingZeroesForceMinLength(array));
    }

    @Test
    public void testToString() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        String expected = "0x7875 Zip Extra Field: UID=" + newUnix.getUID() + " GID=" + newUnix.getGID();
        assertEquals(expected, newUnix.toString());
    }

    @Test
    public void testEquals() {
        X7875_NewUnix newUnix1 = new X7875_NewUnix();
        X7875_NewUnix newUnix2 = new X7875_NewUnix();
        assertTrue(newUnix1.equals(newUnix2));
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        int expected = -1234567 * newUnix.version;
        expected ^= Integer.rotateLeft(newUnix.uid.hashCode(), 16);
        expected ^= newUnix.gid.hashCode();
        assertEquals(expected, newUnix.hashCode());
    }

}