package org.apache.commons.compress.archivers.zip; // Update the package name if necessary

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;

public class X7875_NewUnixTest {

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        ZipShort headerId = x7875NewUnix.getHeaderId();
        assertEquals(0x7875, headerId.getValue().intValue());
    }

    @Test
    public void testGetUID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(1234567890L);
        long uid = x7875NewUnix.getUID();
        assertEquals(1234567890L, uid);
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setGID(9876543210L);
        long gid = x7875NewUnix.getGID();
        assertEquals(9876543210L, gid);
    }

    @Test
    public void testSetUID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(1234567890L);
        long uid = x7875NewUnix.getUID();
        assertEquals(1234567890L, uid);
    }

    @Test
    public void testSetGID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setGID(9876543210L);
        long gid = x7875NewUnix.getGID();
        assertEquals(9876543210L, gid);
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(1234567890L);
        x7875NewUnix.setGID(9876543210L);

        int uidSize = 4;
        int gidSize = 4;

        // Test default situation
        ZipShort length = x7875NewUnix.getLocalFileDataLength();
        assertEquals(7 + uidSize + gidSize, length.getValue().intValue());

        // Test UID and GID size variation
        x7875NewUnix.setUID(1L);
        length = x7875NewUnix.getLocalFileDataLength();
        assertEquals(7 + 1 + gidSize, length.getValue().intValue());

        x7875NewUnix.setGID(1L);
        length = x7875NewUnix.getLocalFileDataLength();
        assertEquals(7 + uidSize + 1, length.getValue().intValue());

        // Test minimal length scenario
        x7875NewUnix.setUID(12345);
        length = x7875NewUnix.getLocalFileDataLength();
        assertEquals(7 + 3, length.getValue().intValue());

        x7875NewUnix.setGID(65432);
        length = x7875NewUnix.getLocalFileDataLength();
        assertEquals(7 + uidSize + 2, length.getValue().intValue());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] data = new byte[10];
        data[0] = (byte) 0x01;
        data[1] = (byte) 0x03;

        byte[] uidBytes = new byte[]{0x01, 0x02, 0x03, 0x04};
        byte[] gidBytes = new byte[]{0x05, 0x06, 0x07, 0x08};

        System.arraycopy(uidBytes, 0, data, 2, uidBytes.length);
        System.arraycopy(gidBytes, 0, data, 6, gidBytes.length);

        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.parseFromLocalFileData(data, 0, data.length);

        assertEquals(1, x7875NewUnix.getVersion());
        assertEquals(1, x7875NewUnix.getUid().compareTo(new BigInteger(new String(new String(new byte[] {0x01, 0x02, 0x03, 0x04})))));
        assertEquals(1, x7875NewUnix.getGid().compareTo(new BigInteger(new String(new byte[] {0x05, 0x06, 0x07, 0x08}))));
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidData() throws ZipException {
        byte[] data = new byte[10];
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
    }

    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        // This test does not make sense since this class does not store
        // anything inside the central directory.
    }

    @Test
    public void testReset() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(1234567890L);
        x7875NewUnix.setGID(9876543210L);
        x7875NewUnix.reset();

        assertEquals(1, x7875NewUnix.getVersion());
        assertEquals(1, x7875NewUnix.getUid().compareTo(BigInteger.ONE_THOUSAND));
        assertEquals(1, x7875NewUnix.getGid().compareTo(BigInteger.ONE_THOUSAND));
    }

    @Test
    public void testToString() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(1234567890L);
        x7875NewUnix.setGID(9876543210L);

        String expected = "0x7875 Zip Extra Field: UID=1234567890 GID=9876543210";
        assertEquals(expected, x7875NewUnix.toString());
    }
}