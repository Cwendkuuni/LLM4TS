package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;

public class X7875_NewUnixTest {

    @Test
    public void testNewUnixInstance() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        assertNull(newUnix.getUID());
        assertNull(newUnix.getGID());
        assertEquals(1, newUnix.getVersion());
    }

    @Test
    public void testSetGetUID() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        newUnix.setUID(12345);
        assertEquals(12345, newUnix.getUID());
    }

    @Test
    public void testSetGetGID() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        newUnix.setGID(67890);
        assertEquals(67890, newUnix.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix newUnix = new X7875_NewUnix();

        newUnix.setUID(12345);
        newUnix.setGID(67890);

        byte[] uidBytes = newUnix.uid.toByteArray();
        byte[] gidBytes = newUnix.gid.toByteArray();

        int uidSize = trimLeadingZeroesForceMinLength(uidBytes).length;
        int gidSize = trimLeadingZeroesForceMinLength(gidBytes).length;

        int expectedLength = 3 + uidSize + gidSize;
        assertEquals(expectedLength, newUnix.getLocalFileDataLength().getValue().intValue());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] data = new byte[]{
                0x01, (byte) 0x03, 0x00, 0x75, 0x78, 0x00, 0x00, 0x00, 0x01, 0x00,
                0x75, 0x78, 0x00, 0x00, 0x00, 0x01, 0x00, 0x67, 0x89, 0x00, 0x00, 0x00
        };

        X7875_NewUnix newUnix = new X7875_NewUnix();
        newUnix.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1, newUnix.getVersion());
        assertEquals(12345, newUnix.getUID());
        assertEquals(67890, newUnix.getGID());
    }

    private byte[] trimLeadingZeroesForceMinLength(byte[] array) {
        return X7875_NewUnix.trimLeadingZeroesForceMinLength(array);
    }
}