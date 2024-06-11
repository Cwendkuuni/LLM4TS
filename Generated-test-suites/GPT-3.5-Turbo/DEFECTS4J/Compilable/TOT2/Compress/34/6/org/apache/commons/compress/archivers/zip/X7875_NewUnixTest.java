package org.apache.commons.compress.archivers.zip;
import org.junit.Test;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(0x7875, x7875.getHeaderId().getValue());
    }

    @Test
    public void testGetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(1000);
        assertEquals(1000, x7875.getUID());
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setGID(1000);
        assertEquals(1000, x7875.getGID());
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
    public void testSetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(500);
        assertEquals(500, x7875.getUID());
    }

    @Test
    public void testSetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setGID(500);
        assertEquals(500, x7875.getGID());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] expectedData = {1, 1, 0, 1, 1};
        assertArrayEquals(expectedData, x7875.getLocalFileDataData());
    }

    @Test
    public void testGetCentralDirectoryData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertArrayEquals(new byte[0], x7875.getCentralDirectoryData());
    }

    @Test
    public void testEquals() {
        X7875_NewUnix x7875_1 = new X7875_NewUnix();
        X7875_NewUnix x7875_2 = new X7875_NewUnix();
        x7875_1.setUID(1000);
        x7875_2.setUID(1000);
        x7875_1.setGID(1000);
        x7875_2.setGID(1000);

        assertTrue(x7875_1.equals(x7875_2));
    }

    @Test
    public void testToString() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals("0x7875 Zip Extra Field: UID=1000 GID=1000", x7875.toString());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        X7875_NewUnix clone = (X7875_NewUnix) x7875.clone();
        assertEquals(x7875.toString(), clone.toString());
    }

    @Test
    public void testTrimLeadingZeroesForceMinLength() {
        byte[] inputArray = {0, 0, 0, 1};
        byte[] expectedArray = {1};
        assertArrayEquals(expectedArray, X7875_NewUnix.trimLeadingZeroesForceMinLength(inputArray));
    }

}