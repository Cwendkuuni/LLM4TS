package org.apache.commons.compress.archivers.zip; // update the package if necessary

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.*;

public class X7875_NewUnixTest {

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix testObject = new X7875_NewUnix();
        ZipShort expected = new ZipShort(0x7875);
        ZipShort actual = testObject.getHeaderId();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetUID() {
        X7875_NewUnix testObject = new X7875_NewUnix();
        long expected = 1001;
        testObject.setUID(expected);
        long actual = testObject.getUID();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetUID() {
        X7875_NewUnix testObject = new X7875_NewUnix();
        long expected = 1001;
        testObject.setUID(expected);
        long actual = testObject.getUID();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetGID() {
        X7875_NewUnix testObject = new X7875_NewUnix();
        long expected = 1002;
        testObject.setGID(expected);
        long actual = testObject.getGID();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetGID() {
        X7875_NewUnix testObject = new X7875_NewUnix();
        long expected = 1002;
        testObject.setGID(expected);
        long actual = testObject.getGID();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix testObject = new X7875_NewUnix();
        BigInteger uid = new BigInteger("12345678901234567890"); // 11 bytes
        BigInteger gid = new BigInteger("09876543210987654321"); // 10 bytes
        testObject.uid = uid;
        testObject.gid = gid;

        ZipShort expected = new ZipShort((int) (1 + uid.toByteArray().length + gid.toByteArray().length));
        ZipShort actual = testObject.getLocalFileDataLength();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetCentralDirectoryLength() {
        this.testGetLocalFileDataLength(); // testGetLocalFileDataLength() also tests getCentralDirectoryLength()
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        X7875_NewUnix testObject = new X7875_NewUnix();
        byte[] data = new byte[]{
                0x55, 0x78, 0x01, 0x03, 0x00, 0x12, 0x34, 0x56, 0x78, 0x90,
                0x10, 0x02, 0x05, 0x43, 0x21, 0x09, 0x87, 0x65, 0x43, 0x21, 0x0a
        };

        testObject.parseFromLocalFileData(data, 0, data.length);

        long expectedUID = new BigInteger("12345678901234567890").longValue();
        long expectedGID = new BigInteger("09876543210987654321").longValue();
        long actualUID = testObject.getUID();
        long actualGID = testObject.getGID();

        assertEquals(expectedUID, actualUID);
        assertEquals(expectedGID, actualGID);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidLength() throws ZipException {
        X7875_NewUnix testObject = new X7875_NewUnix();
        byte[] data = new byte[]{
                0x55, 0x78, 0x01, 0x03, 0x00, 0x12, 0x34, 0x56, 0x78, 0x90,
                0x10, 0x02, 0x05, 0x43, 0x21, 0x09 // data is missing the last byte
        };

        testObject.parseFromLocalFileData(data, 0, data.length);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidVersion() throws ZipException {
        X7875_NewUnix testObject = new X7875_NewUnix();
        byte[] data = new byte[]{
                0x55, 0x78, 0x01, 0x03, 0x01, 0x12, 0x34, 0x56, 0x78, 0x90,
                0x10, 0x02, 0x05, 0x43, 0x21, 0x09, 0x87, 0x65, 0x43, 0x21, 0x0a
        };

        testObject.parseFromLocalFileData(data, 0, data.length);
    }
}