import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.archivers.zip.X7875_NewUnix;

import static org.junit.Assert.*;

public class X7875_NewUnixTest {

    private X7875_NewUnix x7875_newUnix;

    @Before
    public void setUp() {
        x7875_newUnix = new X7875_NewUnix();
    }

    @Test
    public void testGetHeaderId() {
        assertEquals(0x7875, x7875_newUnix.getHeaderId().getValue());
    }

    @Test
    public void testGetUID() {
        x7875_newUnix.setUID(12345);
        assertEquals(12345, x7875_newUnix.getUID());
    }

    @Test
    public void testGetGID() {
        x7875_newUnix.setGID(54321);
        assertEquals(54321, x7875_newUnix.getGID());
    }

    @Test
    public void testSetUID() {
        x7875_newUnix.setUID(1111);
        assertEquals(1111, x7875_newUnix.getUID());
    }

    @Test
    public void testSetGID() {
        x7875_newUnix.setGID(2222);
        assertEquals(2222, x7875_newUnix.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        x7875_newUnix.setUID(12345);
        x7875_newUnix.setGID(54321);
        assertEquals(9, x7875_newUnix.getLocalFileDataLength().getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        x7875_newUnix.setUID(12345);
        x7875_newUnix.setGID(54321);
        assertEquals(9, x7875_newUnix.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testGetLocalFileDataData() {
        x7875_newUnix.setUID(12345);
        x7875_newUnix.setGID(54321);
        byte[] expectedData = {1, 2, 57, 48, 123, 0, 0, 0, 0};
        assertArrayEquals(expectedData, x7875_newUnix.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        assertArrayEquals(new byte[0], x7875_newUnix.getCentralDirectoryData());
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] data = {1, 2, 57, 48, 123, 9, 88, 0, 0, 0, 0};
        x7875_newUnix.parseFromLocalFileData(data, 0, data.length);
        assertEquals(123, x7875_newUnix.getUID());
        assertEquals(88, x7875_newUnix.getGID());
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        assertArrayEquals(new byte[0], x7875_newUnix.getCentralDirectoryData());
    }

    @Test
    public void testToString() {
        x7875_newUnix.setUID(12345);
        x7875_newUnix.setGID(54321);
        assertEquals("0x7875 Zip Extra Field: UID=12345 GID=54321", x7875_newUnix.toString());
    }

    @Test
    public void testEquals() {
        X7875_NewUnix other = new X7875_NewUnix();
        other.setUID(12345);
        other.setGID(54321);
        assertTrue(x7875_newUnix.equals(other));
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix other = new X7875_NewUnix();
        other.setUID(12345);
        other.setGID(54321);
        assertEquals(x7875_newUnix.hashCode(), other.hashCode());
    }

    @Test
    public void testTrimLeadingZeroesForceMinLength() {
        byte[] array = {0, 0, 0, 1};
        byte[] expectedArray = {1};
        assertArrayEquals(expectedArray, X7875_NewUnix.trimLeadingZeroesForceMinLength(array));
    }
}