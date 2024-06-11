package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.apache.commons.compress.utils.BytesUtil;
import org.junit.Test;

public class X7875_NewUnixTest {

    @Test
    public void testNewInstance() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(1, x7875.getVersion());
        assertNull(x7875.getUID());
        assertNull(x7875.getGID());
    }

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        ZipShort headerId = x7875.getHeaderId();
        assertEquals(0x7875, headerId.getValue());
    }

    @Test
    public void testGetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(123);
        assertEquals(123, x7875.getUID());
    }

    @Test
    public void testSetUIDWithNegativeValue() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertThrows(IllegalArgumentException.class, () -> x7875.setUID(-1));
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setGID(456);
        assertEquals(456, x7875.getGID());
    }

    @Test
    public void testSetGIDWithNegativeValue() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertThrows(IllegalArgumentException.class, () -> x7875.setGID(-1));
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(123);
        x7875.setGID(456);

        int uidSize = BytesUtil.SIZE_OF_LONG;
        int gidSize = BytesUtil.SIZE_OF_LONG;

        ZipShort expectedLength = new ZipShort(3 + uidSize + gidSize);
        ZipShort actualLength = x7875.getLocalFileDataLength();
        assertEquals(expectedLength.getValue(), actualLength.getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(123);
        x7875.setGID(456);

        int uidSize = BytesUtil.SIZE_OF_LONG;
        int gidSize = BytesUtil.SIZE_OF_LONG;

        ZipShort expectedLength = new ZipShort(3 + uidSize + gidSize);
        ZipShort actualLength = x7875.getCentralDirectoryLength();
        assertEquals(expectedLength.getValue(), actualLength.getValue());
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(123);
        x7875.setGID(456);

        byte[] expectedData = new byte[1 + BytesUtil.SIZE_OF_LONG + BytesUtil.SIZE_OF_LONG];
        expectedData[0] = (byte) 0x01;

        // UID
        byte[] uidBytes = Long.toByteArray(123L);
        System.arraycopy(uidBytes, 0, expectedData, 1, uidBytes.length);

        // GID
        byte[] gidBytes = Long.toByteArray(456L);
        System.arraycopy(gidBytes, 0, expectedData, 1 + uidBytes.length, gidBytes.length);

        byte[] actualData = x7875.getLocalFileDataData();
        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        byte[] data = new byte[]{
                0x01, (byte) 0x04, 0x00, 0x00, 0x00, 0x7B,
                0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x01, 0x04, 0x00, 0x00, 0x00, 0x7C,
                0x01, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x01, 0x04, 0x00, 0x00, 0x00, 0x7D};

        int offset = 0;
        int length = data.length;
        x7875.parseFromLocalFileData(data, offset, length);

        assertEquals(1, x7875.getVersion());
        assertEquals(new BigInteger("123"), x7875.getUID());
        assertEquals(new BigInteger("456"), x7875.getGID());
    }

    @Test
    public void testParseFromLocalFileDataWithInvalidData() throws ZipException {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        byte[] data = new byte[]{
                0x01, (byte) 0x04, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x01, 0x04, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x01, 0x04, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

        int offset = 0;
        int length = data.length;
        x7875.parseFromLocalFileData(data, offset, length);

        assertEquals(1, x7875.getVersion());
        assertNull(x7875.getUID());
        assertNull(x7875.getGID());
    }
}