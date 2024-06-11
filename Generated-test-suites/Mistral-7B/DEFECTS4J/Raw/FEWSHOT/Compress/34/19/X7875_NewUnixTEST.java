package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;

public class X7875_NewUnixTest {

    @Test
    public void testNewUnix() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        assertEquals(1, newUnix.getVersion());
        assertNull(newUnix.getUID());
        assertNull(newUnix.getGID());
        assertEquals(new ZipShort((short) 0x7875), newUnix.getHeaderId());
    }

    @Test
    public void testSetAndGetUID() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        newUnix.setUID(1234567890L);
        assertEquals(1234567890L, newUnix.getUID());
    }

    @Test
    public void testSetAndGetGID() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        newUnix.setGID(234567890L);
        assertEquals(234567890L, newUnix.getGID());
    }

    @Test
    public void testParseFromLocalFileData() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        byte[] data = new byte[]{
                0x01, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x01, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x09, // UID = 123456789
                0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x09, // GID = 123456789
        };
        newUnix.parseFromLocalFileData(data, 0, data.length);
        assertEquals(123456789L, newUnix.getUID());
        assertEquals(123456789L, newUnix.getGID());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidLength() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        byte[] data = new byte[]{
                0x01, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x03, // UID = 123456789
                0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02, // GID = 123456788
        };
        newUnix.parseFromLocalFileData(data, 0, data.length);
        fail("Unexpected exception not thrown");
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        newUnix.setUID(1234567890L);
        newUnix.setGID(234567890L);
        byte[] expectedData = new byte[]{(byte) 0x01, (byte) 0x01, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x09, (byte) 0x01,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x09, (byte) 0x01,
                (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00};
        byte[] actualData = newUnix.getLocalFileDataData();
        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        newUnix.setUID(1234567890L);
        newUnix.setGID(234567890L);
        ZipShort expectedLength = new ZipShort((short) (5 + 1 + 1 + 9 + 1 + 1 + 9 + 1));
        ZipShort actualLength = newUnix.getLocalFileDataLength();
        assertEquals(expectedLength.getValue(), actualLength.getValue());
    }

    @Test
    public void testGetCentralDirectoryData() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        byte[] expectedData = new byte[0];
        byte[] actualData = newUnix.getCentralDirectoryData();
        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testToString() {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        newUnix.setUID(1234567890L);
        newUnix.setGID(234567890L);
        String expectedString = "0x7875 Zip Extra Field: UID=1234567890 GID=1234567890";
        String actualString = newUnix.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testTrimLeadingZeroesForceMinLength() {
        byte[] expectedArray = new byte[]{0x01, 0x00};
        byte[] actualArray = X7875_NewUnix.trimLeadingZeroesForceMinLength(new byte[]{0x00, 0x01});
        assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    public void testTrimLeadingZeroesForceMinLengthWithEmptyArray() {
        byte[] actualArray = X7875_NewUnix.trimLeadingZeroesForceMinLength(new byte[0]);
        byte[] expectedArray = new byte[0];
        assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    public void testTrimLeadingZeroesForceMinLengthWithSingleByte() {
        byte[] expectedArray = new byte[]{0x01};
        byte[] actualArray = X7875_NewUnix.trimLeadingZeroesForceMinLength(new byte[]{0x01});
        assertArrayEquals(expectedArray, actualArray);
    }

    @Test
    public void testTrimLeadingZeroesForceMinLengthWithMultipleBytes() {
        byte[] expectedArray = new byte[]{0x01, 0x02};
        byte[] actualArray = X7875_NewUnix.trimLeadingZeroesForceMinLength(new byte[]{0x00, 0x01, 0x02});
        assertArrayEquals(expectedArray, actualArray);
    }
}