package org.apache.commons.compress.archivers.zip; // import necessary packages

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;

/**
 * Test class for X7875_NewUnix class
 */
public class X7875_NewUnixTest {

    

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        assertEquals(1, x7875NewUnix.getVersion());
        assertEquals(BigInteger.ONE, x7875NewUnix.getUID());
        assertEquals(BigInteger.ONE, x7875NewUnix.getGID());
    }

    /**
     * Test getHeaderId method
     */
    @Test
    public void testGetHeaderId() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        ZipShort headerId = x7875NewUnix.getHeaderId();
        assertEquals(0x7875, headerId.getValue().intValue());
    }

    /**
     * Test getLocalFileDataLength method
     */
    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        ZipShort length = x7875NewUnix.getLocalFileDataLength();
        assertEquals(3, length.getValue().shortValue());
    }

    /**
     * Test getCentralDirectoryLength method
     */
    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        ZipShort length = x7875NewUnix.getCentralDirectoryLength();
        assertEquals(3, length.getValue().shortValue());
    }

    /**
     * Test getLocalFileDataData method
     */
    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        byte[] localFileDataData = x7875NewUnix.getLocalFileDataData();
        assertEquals(3 + BigInteger.ONE.toByteArray().length + BigInteger.ONE.toByteArray().length, localFileDataData.length);
    }

    /**
     * Test parseFromLocalFileData method
     */
    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] data = new byte[]{0x01, (byte) 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1, x7875NewUnix.getVersion());
        assertEquals(BigInteger.ONE, x7875NewUnix.getUID());
        assertEquals(BigInteger.ONE, x7875NewUnix.getGID());
    }

    /**
     * Test parseFromCentralDirectoryData method
     */
    @Test
    public void testParseFromCentralDirectoryData() throws ZipException {
        // This method does nothing, so we don't test it.
    }

    /**
     * Test getUID method
     */
    @Test
    public void testGetUID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(12345);
        assertEquals(12345, x7875NewUnix.getUID().longValue());
    }

    /**
     * Test getGID method
     */
    @Test
    public void testGetGID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setGID(54321);
        assertEquals(54321, x7875NewUnix.getGID().longValue());
    }

    /**
     * Test setUID method
     */
    @Test
    public void testSetUID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(12345);
        assertEquals(12345, x7875NewUnix.getUID().longValue());
    }

    /**
     * Test setGID method
     */
    @Test
    public void testSetGID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setGID(54321);
        assertEquals(54321, x7875NewUnix.getGID().longValue());
    }

    /**
     * Test equals method
     */
    @Test
    public void testEquals() {
        X7875_NewUnix x7875NewUnix1 = new X7875_NewUnix();
        X7875_NewUnix x7875NewUnix2 = new X7875_NewUnix();
        X7875_NewUnix x7875NewUnix3 = x7875NewUnix1;
        x7875NewUnix1.setUID(12345);
        x7875NewUnix1.setGID(54321);
        x7875NewUnix2.setUID(12345);
        x7875NewUnix2.setGID(54321);
        x7875NewUnix3.setUID(12345);
        x7875NewUnix3.setGID(54321);

        assertEquals(x7875NewUnix1, x7875NewUnix2);
        assertEquals(x7875NewUnix1, x7875NewUnix3);
        assertNotEquals(x7875NewUnix1, new Object());
        assertNotEquals(x7875NewUnix1, null);

        X7875_NewUnix x7875NewUnix4 = new X7875_NewUnix();
        x7875NewUnix4.setUID(54321);
        x7875NewUnix4.setGID(12345);
        assertNotEquals(x7875NewUnix1, x7875NewUnix4);
    }

    /**
     * Test hashCode method
     */
    @Test
    public void testHashCode() {
        X7875_NewUnix x7875NewUnix1 = new X7875_NewUnix();
        X7875_NewUnix x7875NewUnix2 = new X7875_NewUnix();
        X7875_NewUnix x7875NewUnix3 = x7875NewUnix1;
        X7875_NewUnix x7875NewUnix4 = new X7875_NewUnix();
        x7875NewUnix1.setUID(12345);
        x7875NewUnix1.setGID(54321);
        x7875NewUnix2.setUID(54321);
        x7875NewUnix2.setGID(12345);
        x7875NewUnix3.setUID(12345);
        x7875NewUnix3.setGID(54321);
        x7875NewUnix4.setUID(54321);
        x7875NewUnix4.setGID(12345);

        assertEquals(x7875NewUnix1.hashCode(), x7875NewUnix2.hashCode());
        assertEquals(x7875NewUnix1.hashCode(), x7875NewUnix3.hashCode());
        assertEquals(x7875NewUnix1.hashCode(), x7875NewUnix4.hashCode());

        X7875_NewUnix x7875NewUnix5 = new X7875_NewUnix();
        x7875NewUnix5.setUID(12345L << 32 | 54321L);
        x7875NewUnix5.setGID(54321L << 32 | 12345L);
        assertNotEquals(x7875NewUnix1.hashCode(), x7875NewUnix5.hashCode());
    }

    /**
     * Test trimLeadingZeroesForceMinLength method
     */
    @Test
    public void testTrimLeadingZeroesForceMinLength() {
        byte[] testData = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F};
        byte[] expectedData = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F};

        byte[] trimmedData = X7875_NewUnix.trimLeadingZeroesForceMinLength(testData);

        assertArrayEquals(expectedData, trimmedData);
    }

    /**
     * Test parseFromLocalFileData with invalid data
     */
    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidData() throws ZipException {
        byte[] invalidData = new byte[]{0x01, 0x03, 0x00, (byte) 0xFF, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.parseFromLocalFileData(invalidData, 0, invalidData.length);
    }

    
}