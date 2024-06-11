import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class X7875_NewUnixTest {

    private X7875_NewUnix x7875_newUnix;

    @Before
    public void setUp() {
        x7875_newUnix = new X7875_NewUnix();
    }

    @Test
    public void testGetHeaderId() {
        assertEquals(new ZipShort(0x7875), x7875_newUnix.getHeaderId());
    }

    @Test
    public void testGetUID() {
        x7875_newUnix.setUID(12345L);
        assertEquals(12345L, x7875_newUnix.getUID());
    }

    @Test
    public void testGetGID() {
        x7875_newUnix.setGID(98765L);
        assertEquals(98765L, x7875_newUnix.getGID());
    }

    @Test
    public void testSetUID() {
        x7875_newUnix.setUID(54321L);
        assertEquals(54321L, x7875_newUnix.getUID());
    }

    @Test
    public void testSetGID() {
        x7875_newUnix.setGID(56789L);
        assertEquals(56789L, x7875_newUnix.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        x7875_newUnix.setUID(12345L);
        x7875_newUnix.setGID(98765L);
        assertEquals(new ZipShort(9), x7875_newUnix.getLocalFileDataLength());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        assertEquals(new ZipShort(9), x7875_newUnix.getCentralDirectoryLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        x7875_newUnix.setUID(12345L);
        x7875_newUnix.setGID(98765L);
        byte[] expected = {1, 1, 48, 57, 0, 1, 57, 78};
        assertArrayEquals(expected, x7875_newUnix.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        byte[] expected = {};
        assertArrayEquals(expected, x7875_newUnix.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] data = {1, 1, 48, 57, 0, 1, 57, 78};
        x7875_newUnix.parseFromLocalFileData(data, 0, data.length);
        assertEquals(12345L, x7875_newUnix.getUID());
        assertEquals(98765L, x7875_newUnix.getGID());
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        byte[] buffer = {};
        x7875_newUnix.parseFromCentralDirectoryData(buffer, 0, buffer.length);
        // no need to assert anything since this method doesn't do anything
    }

    @Test
    public void testToString() {
        x7875_newUnix.setUID(12345L);
        x7875_newUnix.setGID(98765L);
        assertEquals("0x7875 Zip Extra Field: UID=12345 GID=98765", x7875_newUnix.toString());
    }

    @Test
    public void testEquals() {
        X7875_NewUnix other = new X7875_NewUnix();
        other.setUID(12345L);
        other.setGID(98765L);
        assertTrue(x7875_newUnix.equals(other));
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix other = new X7875_NewUnix();
        other.setUID(12345L);
        other.setGID(98765L);
        assertEquals(x7875_newUnix.hashCode(), other.hashCode());
    }

    @Test
    public void testTrimLeadingZeroesForceMinLength() {
        byte[] array = {0, 0, 0, 0, 1, 2, 3};
        byte[] expected = {1, 2, 3};
        assertArrayEquals(expected, X7875_NewUnix.trimLeadingZeroesForceMinLength(array));
    }
}