package org.apache.commons.compress.archivers.zip; // adjust the package name if necessary

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;

/**
 * Test class for X7875_NewUnix class.
 */
public class X7875_NewUnixTest {

    

    @Test
    public void testNewInstance() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        assertNotNull(newUnix);
    }

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        ZipShort headerId = newUnix.getHeaderId();
        assertEquals(new ZipShort(0x7875), headerId);
    }

    @Test
    public void testGetUIDAndSetUID() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        long uid = 12345;
        newUnix.setUID(uid);
        long actualUid = newUnix.getUID();
        assertEquals(uid, actualUid);
    }

    @Test
    public void testGetGIDAndSetGID() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        long gid = 67890;
        newUnix.setGID(gid);
        long actualGid = newUnix.getGID();
        assertEquals(gid, actualGid);
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        BigInteger uid = BigInteger.valueOf(12345);
        BigInteger gid = BigInteger.valueOf(67890);
        int version = 1;

        newUnix.uid = uid;
        newUnix.gid = gid;

        ZipShort length = newUnix.getLocalFileDataLength();
        int expectedLength = 3 + uid.toByteArray().length + gid.toByteArray().length;
        assertEquals(new ZipShort(expectedLength), length);
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        BigInteger uid = BigInteger.valueOf(12345);
        BigInteger gid = BigInteger.valueOf(67890);
        int version = 1;

        newUnix.uid = uid;
        newUnix.gid = gid;

        ZipShort length = newUnix.getCentralDirectoryLength();
        int expectedLength = 3 + uid.toByteArray().length + gid.toByteArray().length;
        assertEquals(new ZipShort(expectedLength), length);
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        BigInteger uid = BigInteger.valueOf(12345);
        BigInteger gid = BigInteger.valueOf(67890);
        int version = 1;

        newUnix.uid = uid;
        newUnix.gid = gid;

        byte[] data = newUnix.getLocalFileDataData();

        int expectedLength = 3 + uid.toByteArray().length + gid.toByteArray().length;
        assertEquals(expectedLength, data.length);

        // Check the first byte is version.
        assertEquals(unsignedIntToSignedByte(version), data[0]);

        // Check the next two bytes represent the size of UID and GID.
        int uidSize = unsignedIntToSignedByte(uid.toByteArray().length);
        int gidSize = unsignedIntToSignedByte(gid.toByteArray().length);
        assertEquals(uidSize, data[1]);
        assertEquals(gidSize, data[2]);

        // Check the remaining bytes are UID and GID.
        byte[] uidBytes = uid.toByteArray();
        byte[] gidBytes = gid.toByteArray();
        for (int i = 0; i < uidBytes.length; i++) {
            assertEquals(uidBytes[i], data[3 + i]);
        }

        for (int i = 0; i < gidBytes.length; i++) {
            assertEquals(gidBytes[i], data[3 + uidBytes.length + i]);
        }
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        X7875_NewUnix newUnix = new X7875_NewUnix();

        byte[] data = new byte[]{
                1, // version
                3, (byte) 0x05, (byte) 0x0a, (byte) 0x3b, (byte) 0x76, // UID (101110111011011011011111)
                1, (byte) 0x05, (byte) 0x3c, (byte) 0x6f, (byte) 0x64, // GID (111111101111101011111011)
                (byte) 0xc0, 0x0c, 0x00, 0x00 // should be ignored
        };

        newUnix.parseFromLocalFileData(data, 0, data.length);

        assertEquals(1, newUnix.version);
        assertEquals(BigInteger.valueOf(101110111011011011011111L), newUnix.uid);
        assertEquals(BigInteger.valueOf(111111101111101011111011L), newUnix.gid);
    }

    @Test
    public void testParseFromLocalFileData_InvalidDataLength() throws ZipException {
        X7875_NewUnix newUnix = new X7875_NewUnix();

        byte[] data = new byte[]{
                1, // version
                3, (byte) 0x05, (byte) 0x0a, (byte) 0x3b // UID (101110111011011011011111)
        };

        assertThrows(ZipException.class, () -> newUnix.parseFromLocalFileData(data, 0, data.length));
    }

    @Test
    public void testParseFromLocalFileData_InvalidVersion() throws ZipException {
        X7875_NewUnix newUnix = new X7875_NewUnix();

        byte[] data = new byte[]{
                2, // version
                3 // unused
        };

        assertThrows(ZipException.class, () -> newUnix.parseFromLocalFileData(data, 0, data.length));
    }

    @Test
    public void testToString() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        BigInteger uid = BigInteger.valueOf(12345);
        BigInteger gid = BigInteger.valueOf(67890);
        int version = 1;

        newUnix.uid = uid;
        newUnix.gid = gid;

        String actualToString = newUnix.toString();
        String expectedToString = "0x7875 Zip Extra Field: UID=" + uid + " GID=" + gid;
        assertEquals(expectedToString, actualToString);
    }

    
}