package org.apache.commons.compress.archivers.zip;
import static org.junit.Assert.*;

import org.junit.Test;

public class X7875_NewUnixTest {

    @Test
    public void testConstructor() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertNotNull(x7875);
    }

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(0x7875, x7875.getHeaderId().getValue());
    }

    @Test
    public void testSetUIDAndGetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(1000);
        assertEquals(1000, x7875.getUID());
    }

    @Test
    public void testSetGIDAndGetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setGID(1000);
        assertEquals(1000, x7875.getGID());
    }

    @Test
    public void testLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(3, x7875.getLocalFileDataLength().getValue());
    }

    @Test
    public void testCentralDirectoryLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(3, x7875.getCentralDirectoryLength().getValue());
    }

    @Test
    public void testLocalFileDataData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] data = x7875.getLocalFileDataData();
        assertNotNull(data);
        assertEquals(3, data.length);
    }

    @Test
    public void testCentralDirectoryData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] data = x7875.getCentralDirectoryData();
        assertNotNull(data);
        assertEquals(0, data.length);
    }

    @Test
    public void testParseFromLocalFileData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] data = {0x01, 0x01, 0x00, 0x01, 0x00};
        try {
            x7875.parseFromLocalFileData(data, 0, data.length);
        } catch (Exception e) {
            fail("Exception should not be thrown for valid data");
        }
    }

    @Test
    public void testEquals() {
        X7875_NewUnix x7875A = new X7875_NewUnix();
        X7875_NewUnix x7875B = new X7875_NewUnix();
        assertEquals(x7875A, x7875B);
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertNotNull(x7875.hashCode());
    }
}