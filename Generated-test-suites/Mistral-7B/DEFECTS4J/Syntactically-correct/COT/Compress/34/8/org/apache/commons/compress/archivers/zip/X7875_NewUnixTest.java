package org.apache.commons.compress.archivers.zip; // import necessary classes

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for X7875_NewUnix class.
 */
public class X7875_NewUnixTest {

    @Test
    public void testConstructor() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(1, x7875.getVersion());
        assertNull(x7875.getUID());
        assertNull(x7875.getGID());

        X7875_NewUnix clone = (X7875_NewUnix) x7875.clone();
        assertEquals(x7875, clone);
    }

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(X7875_NewUnix.HEADER_ID, x7875.getHeaderId());
    }

    @Test
    public void testGetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        x7875.setUID(12345);
        assertEquals(12345L, x7875.getUID());

        x7875.setUID(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, x7875.getUID());

        x7875.setUID(-12345);
        assertEquals(-12345L, x7875.getUID());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUIDWithInvalidValue() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(-50000);
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        x7875.setGID(12345);
        assertEquals(12345L, x7875.getGID());

        x7875.setGID(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, x7875.getGID());

        x7875.setGID(-12345);
        assertEquals(-12345L, x7875.getGID());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetGIDWithInvalidValue() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setGID(-50000);
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        x7875.setUID(1);
        x7875.setGID(1);
        assertEquals(new ZipShort(3), x7875.getLocalFileDataLength());

        x7875.setUID(3);
        x7875.setGID(4);
        assertEquals(new ZipShort(3 + 1 + 1), x7875.getLocalFileDataLength());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] invalidData = new byte[]{0x78, 0x75};
        x7875.parseFromLocalFileData(invalidData, 0, invalidData.length);
        fail("Should throw ZipException");
    }

    @Test
    public void testParseFromLocalFileData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        byte[] data = new byte[]{
                0x78, 0x75, (byte) 0x01, (byte) 0x01, // HeaderID, version=1
                0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // UID (4 bytes, 16 bits signed) = 1
                0x01, 0x00, 0x00, 0x00, // GID (1 byte, 8 bits signed) = 1
                0x01, 0x00 // Version=1
        };

        x7875.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1, x7875.getVersion());
        assertEquals(1L, x7875.getUID());
        assertEquals(1L, x7875.getGID());
    }

    @Test
    public void testToString() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals("0x7875 Zip Extra Field: UID=1 GID=1", x7875.toString());

        x7875.setUID(12345);
        x7875.setGID(54321);
        assertEquals("0x7875 Zip Extra Field: UID=12345 GID=54321", x7875.toString());
    }
}