package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.junit.Test;

public class X7875_NewUnixTest {

    @Test
    public void testNewInstance() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        assertEquals(1, extraField.getVersion());
        assertNull(extraField.getUID());
        assertNull(extraField.getGID());
        ZipShort headerId = extraField.getHeaderId();
        assertEquals(0x7875, headerId.getValue().intValue());
    }

    @Test
    public void testGetUidGidValues() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        extraField.setUID(12345);
        extraField.setGID(54321);
        assertEquals(12345, extraField.getUID());
        assertEquals(54321, extraField.getGID());
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] data = new byte[]{
                0x78, (byte) 0x75, 0, 1, 8, 0, 0, 0, 0, 0, 0, 0, // version=1, uidSize=1
                0, (byte) 0x63, (byte) 0x61, (byte) 0x6d, (byte) 0x65, // UID=cansd
                0, 5, (byte) 0x32, (byte) 0x34, (byte) 0x35, // GID=245
                0, 0 // empty central directory data
        };

        X7875_NewUnix extraField = new X7875_NewUnix();
        extraField.parseFromLocalFileData(data, 0, data.length);

        assertEquals(1, extraField.getVersion());
        assertEquals("12345", extraField.getUID().toString());
        assertEquals("54321", extraField.getGID().toString());
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        // Empty central directory data
        X7875_NewUnix extraField = new X7875_NewUnix();
        byte[] centralDirectoryData = new byte[0];
        extraField.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        assertNull(extraField.getUID());
        assertNull(extraField.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        BigInteger uid = new BigInteger("12345678901234567890123456789012");
        BigInteger gid = new BigInteger("54321098765432109876543210987654");

        extraField.setUID(uid.longValue());
        extraField.setGID(gid.longValue());

        byte[] uidBytes = uid.toByteArray();
        byte[] gidBytes = gid.toByteArray();

        // For testing, we manually reverse the bytes to simulate the little-endian UID/GID
        byte[] uidReversed = new byte[uidBytes.length];
        byte[] gidReversed = new byte[gidBytes.length];
        for (int i = 0; i < uidBytes.length; i++) {
            uidReversed[uidBytes.length - 1 - i] = uidBytes[i];
        }
        for (int i = 0; i < gidBytes.length; i++) {
            gidReversed[gidBytes.length - 1 - i] = gidBytes[i];
        }

        int expectedUIDLength = uidReversed.length;
        int expectedGIDLength = gidReversed.length;

        ZipShort length = extraField.getLocalFileDataLength();
        assertEquals(expectedUIDLength + expectedGIDLength + 4, length.getValue().intValue()); // version + uidSize + gidSize
    }

    @Test
    public void testGetCentralDirectoryLength() {
        // Central directory length is the same as local file data length
        X7875_NewUnix extraField = new X7875_NewUnix();
        ZipShort centralDirectoryLength = extraField.getCentralDirectoryLength();
        assertEquals(extraField.getLocalFileDataLength().getValue().intValue(), centralDirectoryLength.getValue().intValue());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        BigInteger uid = new BigInteger("12345678901234567890123456789012");
        BigInteger gid = new BigInteger("54321098765432109876543210987654");

        extraField.setUID(uid.longValue());
        extraField.setGID(gid.longValue());

        byte[] uidBytes = uid.toByteArray();
        byte[] gidBytes = gid.toByteArray();

        // For testing, we manually reverse the bytes to simulate the little-endian UID/GID
        byte[] uidReversed = new byte[uidBytes.length];
        byte[] gidReversed = new byte[gidBytes.length];
        for (int i = 0; i < uidBytes.length; i++) {
            uidReversed[uidBytes.length - 1 - i] = uidBytes[i];
        }
        for (int i = 0; i < gidBytes.length; i++) {
            gidReversed[gidBytes.length - 1 - i] = gidBytes[i];
        }

        byte[] expectedData = new byte[uidReversed.length + gidReversed.length + 3];

        // version=1, uidSize=uidReversed.length, gidSize=gidReversed.length
        expectedData[0] = (byte) 0x78;
        expectedData[1] = (byte) 0x75;
        expectedData[2] = 1;

        // uid
        for (int i = 0; i < uidReversed.length; i++) {
            expectedData[i + 3] = uidReversed[i];
        }

        // uidSize
        expectedData[uidReversed.length + 3] = (byte) (uidReversed.length + 1);

        // gid
        for (int i = 0; i < gidReversed.length; i++) {
            expectedData[uidReversed.length + gidReversed.length + i + 1] = gidReversed[i];
        }

        // gidSize
        expectedData[uidReversed.length + gidReversed.length + gidReversed.length + 1] = (byte) (gidReversed.length + 1);

        byte[] actualData = extraField.getLocalFileDataData();
        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testGetCentralDirectoryData() {
        X7875_NewUnix extraField = new X7875_NewUnix();
        byte[] centralDirectoryData = extraField.getCentralDirectoryData();
        assertArrayEquals(new byte[0], centralDirectoryData);
    }
}