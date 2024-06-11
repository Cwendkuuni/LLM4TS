package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;

public class X7875_NewUnixTest {

    @Test
    public void testX7875_NewUnix() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        assertEquals(1, x7875NewUnix.getVersion());
        assertNull(x7875NewUnix.getUID());
        assertNull(x7875NewUnix.getGID());
        assertEquals(new ZipShort(0x7875), x7875NewUnix.getHeaderId());
    }

    @Test
    public void testSetUIDAndGID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long uid = 123;
        long gid = 456;

        x7875NewUnix.setUID(uid);
        x7875NewUnix.setGID(gid);

        assertEquals(uid, x7875NewUnix.getUID());
        assertEquals(gid, x7875NewUnix.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long uid = 123;
        long gid = 456;
        x7875NewUnix.setUID(uid);
        x7875NewUnix.setGID(gid);

        int uidSize = Integer.numberOfLeadingZeros(Long.toBinaryString(uid.longValue()).length()) + 1;
        int gidSize = Integer.numberOfLeadingZeros(Long.toBinaryString(gid.longValue()).length()) + 1;

        int expectedLength = 3 + uidSize + gidSize;
        ZipShort actualLength = x7875NewUnix.getLocalFileDataLength();
        assertEquals(expectedLength, actualLength.getValue());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] data = new byte[]{
                0x78, 0x75, // header id
                0x03, // version
                0x01, 0x00, 0x74, 0x65, 0x73, 0x74, // UID: 123
                0x01, 0x00, 0x49, 0x27, 0x00, 0x00, 0x00, // GID: 456
        };

        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.parseFromLocalFileData(data, 0, data.length);

        assertEquals(1, x7875NewUnix.getVersion());
        assertEquals(123, x7875NewUnix.getUID().longValue());
        assertEquals(456, x7875NewUnix.getGID().longValue());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidData() throws ZipException {
        byte[] data = new byte[]{
                0x78, 0x75, // header id
                0x03, // version
                0x01, 0x00, // UID: 123
                0x01, 0x00, 0x00 // GID: 0 (invalid length)
        };

        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
        fail(); // should not reach here
    }

    @Test
    public void testToString() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long uid = 123;
        long gid = 456;
        x7875NewUnix.setUID(uid);
        x7875NewUnix.setGID(gid);

        assertEquals("0x7875 Zip Extra Field: UID=" + uid + " GID=" + gid, x7875NewUnix.toString());
    }
}