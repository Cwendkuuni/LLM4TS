package org.apache.commons.compress.archivers.zip; // import necessary classes here

import org.junit.Test;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        ZipShort headerId = x7875.getHeaderId();
        assertEquals(HEADER_ID, headerId);
    }

    @Test
    public void testGetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = x7875.getUID();
        assertEquals(1000L, uid); // default UID value

        x7875.setUID(2000L);
        uid = x7875.getUID();
        assertEquals(2000L, uid);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUIDNegativeValue() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(-1L);
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long gid = x7875.getGID();
        assertEquals(1000L, gid); // default GID value

        x7875.setGID(3000L);
        gid = x7875.getGID();
        assertEquals(3000L, gid);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetGIDNegativeValue() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setGID(-1L);
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        x7875.setUID(1001L);
        x7875.setGID(1002L);

        ZipShort length = x7875.getLocalFileDataLength();
        assertEquals(new ZipShort((short) (3 + 1 + 1 + 1 + 1 + 1)), length);
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        x7875.setUID(1001L);
        x7875.setGID(1002L);

        ZipShort length = x7875.getCentralDirectoryLength();
        assertEquals(length, x7875.getLocalFileDataLength());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        x7875.setUID(1001L);
        x7875.setGID(1002L);

        byte[] data = x7875.getLocalFileDataData();

        // Version, UID size, UID, GID size, GID
        assertEquals(new byte[]{0x78, 0x75, 0x01, 0x01, 0x01, 0x01, 0x01, 0x02}, data);
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        byte[] data = new byte[]{0x78, 0x75, (byte) 0x01, 0x01, 0x01, 0x01, 0x01, 0x02, 0x01, 0x01, 0x01, 0x01, 0x02};
        x7875.parseFromLocalFileData(data, 0, data.length);

        assertEquals(1001L, x7875.getUID());
        assertEquals(1002L, x7875.getGID());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidData() throws ZipException {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        byte[] data = new byte[]{0x78, 0x75, 0x01, 0x01, 0x01, 0x01, 0x01, (byte) 0x7F}; // invalid GID size
        x7875.parseFromLocalFileData(data, 0, data.length);
    }

    @Test
    public void testToString() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        x7875.setUID(1001L);
        x7875.setGID(1002L);

        String toString = x7875.toString();
        assertEquals("0x7875 Zip Extra Field: UID=1001 GID=1002", toString);
    }
}