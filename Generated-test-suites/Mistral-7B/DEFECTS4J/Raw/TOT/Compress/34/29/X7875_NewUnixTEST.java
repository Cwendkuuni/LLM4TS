package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class X7875_NewUnixTest {

    @Test
    public void testNewX7875_NewUnixWithValidUIDAndGID() {
        // Create an instance of X7875_NewUnix with valid UID and GID
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(1001);
        x7875.setGID(1002);

        // Test UID and GID values
        Assertions.assertEquals(1001, x7875.getUID());
        Assertions.assertEquals(1002, x7875.getGID());

        // Test local file data length
        Assertions.assertEquals(7, x7875.getLocalFileDataLength().getValue().intValue());

        // Test local file data
        byte[] localFileData = x7875.getLocalFileDataData();
        Assertions.assertArrayEquals(new byte[]{0x78, 0x75, 1, 3, 0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0}, localFileData);

        // Test parsing from local file data
        X7875_NewUnix parsedX7875 = new X7875_NewUnix();
        parsedX7875.parseFromLocalFileData(localFileData, 0, localFileData.length);
        Assertions.assertEquals(1001, parsedX7875.getUID());
        Assertions.assertEquals(1002, parsedX7875.getGID());
    }

    @Test
    public void testNewX7875_NewUnixWithMinimumLengthUIDAndGID() {
        // Create an instance of X7875_NewUnix with minimum length UID and GID
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(1);
        x7875.setGID(1);

        // Test UID and GID values
        Assertions.assertEquals(1, x7875.getUID());
        Assertions.assertEquals(1, x7875.getGID());

        // Test local file data length
        Assertions.assertEquals(5, x7875.getLocalFileDataLength().getValue().intValue());

        // Test local file data
        byte[] localFileData = x7875.getLocalFileDataData();
        Assertions.assertArrayEquals(new byte[]{0x78, 0x75, 1, 1, 0, 1, 0, 1}, localFileData);

        // Test parsing from local file data
        X7875_NewUnix parsedX7875 = new X7875_NewUnix();
        parsedX7875.parseFromLocalFileData(localFileData, 0, localFileData.length);
        Assertions.assertEquals(1, parsedX7875.getUID());
        Assertions.assertEquals(1, parsedX7875.getGID());
    }

    @Test
    public void testNewX7875_NewUnixWithInvalidUIDAndGID() {
        // Create an instance of X7875_NewUnix with invalid UID and GID
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(-1);
        x7875.setGID(-1);

        // Test UID and GID values
        Assertions.assertThrows(ZipException.class, () -> x7875.getUID());
        Assertions.assertThrows(ZipException.class, () -> x7875.getGID());
    }

    @Test
    public void testNewX7875_NewUnixParseFromLocalFileDataWithInvalidData() {
        // Create invalid local file data for parsing
        byte[] invalidData = {0x78, 0x75, 1, 3, 0, 3, 0, 0, 0, 0, 0, 2, 0, 0, 0};

        // Test parsing from invalid data
        X7875_NewUnix parsedX7875 = new X7875_NewUnix();
        Assertions.assertThrows(ZipException.class, () -> parsedX7875.parseFromLocalFileData(invalidData, 0, invalidData.length));
    }

    @Test
    public void testNewX7875_NewUnixParseFromCentralDirectoryData() {
        // Create an instance of X7875_NewUnix with valid UID and GID
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(1001);
        x7875.setGID(1002);

        // Test central directory data length
        Assertions.assertEquals(0, x7875.getCentralDirectoryLength().getValue().intValue());

        // Test central directory data
        byte[] centralDirectoryData = x7875.getCentralDirectoryData();
        Assertions.assertArrayEquals(new byte[0], centralDirectoryData);

        // Test parsing from central directory data
        X7875_NewUnix parsedX7875 = new X7875_NewUnix();
        parsedX7875.parseFromCentralDirectoryData(centralDirectoryData, 0, centralDirectoryData.length);
        Assertions.assertNull(parsedX7875.getUID());
        Assertions.assertNull(parsedX7875.getGID());
    }
}