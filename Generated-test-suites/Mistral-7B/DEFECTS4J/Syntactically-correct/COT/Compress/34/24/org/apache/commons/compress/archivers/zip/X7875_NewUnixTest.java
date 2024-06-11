package org.apache.commons.compress.archivers.zip; // adjust the package name if needed

import static org.junit.Assert.*;
import static org.apache.commons.compress.archivers.zip.ZipUtil.reverse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.zip.DataFormatException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apache.commons.compress.archivers.zip.ZipShort;
import org.apache.commons.compress.archivers.zip.ZipUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Test case for X7875_NewUnix class.
 */
@RunWith(Parameterized.class)
public class X7875_NewUnixTest {
    // Add instance variables and constructor for test data if needed

    private static final int EXPECTED_UID = 1001;
    private static final int EXPECTED_GID = 2001;
    private static final int EXPECTED_VERSION = 1;

    private static final byte[] X7875_DATA = {
            (byte) 0x78, (byte) 0x75, (byte) 0x03, (byte) 0x01, // version, uidSize, uid
            (byte) 0x01, (byte) 0x00,                          // uid[0]
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, // uid[1], uid[2], uid[3], uid[4]
            (byte) 0x01, (byte) 0x00,                          // gidSize, gid[0]
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, // gid[1], gid[2], gid[3], gid[4]
    };

    private X7875_NewUnix testObject;

    @Parameters(name = "{index}: X7875_NewUnix test case with UID={0} and GID={1}")
    public static Iterable<Object[]> data() {
        // Return test data if needed
        return null;
    }

    @Before
    public void setup() {
        testObject = new X7875_NewUnix();
        testObject.setUID(EXPECTED_UID);
        testObject.setGID(EXPECTED_GID);
    }

    @Test
    public void testGetHeaderId() {
        ZipShort expected = new ZipShort(0x7875);
        ZipShort actual = testObject.getHeaderId();
        assertEquals("getHeaderId() returned unexpected header ID", expected, actual);
    }

    @Test
    public void testGetUID() {
        long expected = EXPECTED_UID;
        long actual = testObject.getUID();
        assertEquals("getUID() returned unexpected UID", expected, actual);
    }

    @Test
    public void testGetGID() {
        long expected = EXPECTED_GID;
        long actual = testObject.getGID();
        assertEquals("getGID() returned unexpected GID", expected, actual);
    }

    @Test(expected = AssertionError.class)
    public void testSetNegativeUID() {
        testObject.setUID(-1);
    }

    @Test(expected = AssertionError.class)
    public void testSetNegativeGID() {
        testObject.setGID(-1);
    }

    @Test
    public void testGetLocalFileDataLength() {
        int expectedSize = getTotalSizeOfUidAndGid(EXPECTED_UID, EXPECTED_GID);
        ZipShort actual = testObject.getLocalFileDataLength();
        assertEquals("getLocalFileDataLength() returned unexpected length",
                new ZipShort(expectedSize), actual);
    }

    @Test
    public void testParseFromLocalFileData() throws IOException, ZipException, DataFormatException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream uidGidOutputStream = new ByteArrayOutputStream();
        testObject.getLocalFileDataData().write(outputStream);

        byte[] data = outputStream.toByteArray();
        X7875_NewUnix parsedObject = new X7875_NewUnix();
        parsedObject.parseFromLocalFileData(data, 0, data.length);

        assertEquals("parseFromLocalFileData() returned unexpected version",
                EXPECTED_VERSION, parsedObject.getVersion());
        assertEquals("parseFromLocalFileData() returned unexpected UID",
                EXPECTED_UID, parsedObject.getUID());
        assertEquals("parseFromLocalFileData() returned unexpected GID",
                EXPECTED_GID, parsedObject.getGID());

        // Extract UID and GID from the parsed object to check if they are correctly parsed
        byte[] uidBytes = new byte[parsedObject.getUIDSize()];
        byte[] gidBytes = new byte[parsedObject.getGIDSize()];
        parsedObject.getLocalFileDataData().get(0, uidBytes, 0, uidBytes.length);
        parsedObject.getLocalFileDataData().get(0, gidBytes, 0, gidBytes.length);

        BigInteger parsedUid = new BigInteger(1, reverse(uidBytes));
        BigInteger parsedGid = new BigInteger(1, reverse(gidBytes));

        assertEquals("parseFromLocalFileData() returned unexpected UID",
                EXPECTED_UID, parsedUid.longValue());
        assertEquals("parseFromLocalFileData() returned unexpected GID",
                EXPECTED_GID, parsedGid.longValue());
    }

    @Test
    public void testGetCentralDirectoryData() {
        byte[] actual = testObject.getCentralDirectoryData();
        assertArrayEquals("getCentralDirectoryData() returned unexpected data",
                new byte[0], actual);
    }

    @Test
    public void testParseFromCentralDirectoryData() throws IOException, ZipException, DataFormatException {
        // This test method is not needed since X7875_NewUnix does not store anything inside the central directory
    }

    @Test
    public void testToString() {
        String expected = "0x7875 Zip Extra Field: UID=" + EXPECTED_UID + " GID=" + EXPECTED_GID;
        String actual = testObject.toString();
        assertEquals("toString() returned unexpected string", expected, actual);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        X7875_NewUnix clonedObject = (X7875_NewUnix) testObject.clone();
        assertEquals("clone() returned unexpected object", testObject, clonedObject);
    }

    @Test
    public void testEquals() {
        X7875_NewUnix anotherObject = new X7875_NewUnix();
        anotherObject.setUID(EXPECTED_UID);
        anotherObject.setGID(EXPECTED_GID);
        assertTrue("equals() returned false for identical objects", testObject.equals(anotherObject));

        X7875_NewUnix anotherObjectWithDifferentVersion = new X7875_NewUnix();
        anotherObjectWithDifferentVersion.setUID(EXPECTED_UID);
        anotherObjectWithDifferentVersion.setGID(EXPECTED_GID);
        anotherObjectWithDifferentVersion.setVersion(2);
        assertFalse("equals() returned true for different objects", testObject.equals(anotherObjectWithDifferentVersion));

        X7875_NewUnix anotherObjectWithDifferentUid = new X7875_NewUnix();
        anotherObjectWithDifferentUid.setVersion(EXPECTED_VERSION);
        anotherObjectWithDifferentUid.setGID(EXPECTED_GID);
        anotherObjectWithDifferentUid.setUID(EXPECTED_UID + 1);
        assertFalse("equals() returned true for different objects", testObject.equals(anotherObjectWithDifferentUid));

        X7875_NewUnix anotherObjectWithDifferentGid = new X7875_NewUnix();
        anotherObjectWithDifferentGid.setVersion(EXPECTED_VERSION);
        anotherObjectWithDifferentGid.setUID(EXPECTED_UID);
        anotherObjectWithDifferentGid.setGID(EXPECTED_GID + 1);
        assertFalse("equals() returned true for different objects", testObject.equals(anotherObjectWithDifferentGid));
    }

    @Test
    public void testHashCode() {
        int expectedHashCode = hashCode(EXPECTED_UID, EXPECTED_GID, EXPECTED_VERSION);
        int actualHashCode = testObject.hashCode();
        assertEquals("hashCode() returned unexpected hash code", expectedHashCode, actualHashCode);
    }

    @Test
    public void testParseFromLocalFileDataWithInvalidData() throws IOException, ZipException, DataFormatException {
        byte[] invalidData = {
                (byte) 0x78, (byte) 0x75, (byte) 0x03, (byte) 0x01, // version, uidSize, uid
                (byte) 0x01, (byte) 0x01 // invalid data: gidSize should be 1 or more
        };

        try {
            X7875_NewUnix parsedObject = new X7875_NewUnix();
            parsedObject.parseFromLocalFileData(invalidData, 0, invalidData.length);
            fail("parseFromLocalFileData() did not throw ZipException with invalid data");
        } catch (ZipException e) {
            // expected
        }
    }

    private static int getTotalSizeOfUidAndGid(long uid, long gid) {
        int uidSize = trimLeadingZeroesForceMinLength(uidToByteArray(uid)).length;
        int gidSize = trimLeadingZeroesForceMinLength(gidToByteArray(gid)).length;
        return 3 + uidSize + gidSize;
    }

    private static byte[] uidToByteArray(long uid) {
        byte[] uidBytes = new BigInteger(Long.toString(uid, 10)).toByteArray();
        return trimLeadingZeroesForceMinLength(uidBytes);
    }

    private static byte[] gidToByteArray(long gid) {
        byte[] gidBytes = new BigInteger(Long.toString(gid, 10)).toByteArray();
        return trimLeadingZeroesForceMinLength(gidBytes);
    }

    private static int hashCode(long uid, long gid, int version) {
        // Implement the hashCode() logic based on the fields of the class
    }

    private static byte[] trimLeadingZeroesForceMinLength(byte[] array) {
        // Use the trimLeadingZeroesForceMinLength() method from the class
    }

    @Test
    public void testCreateZipFileWithX7875_NewUnixExtraField() throws IOException {
        String testFileName = "testX7875_NewUnixTest.zip";
        FileOutputStream outputStream = new FileOutputStream(testFileName);
        ZipFile zipFile = new ZipFile(testFileName, "UTF-8");

        File sourceFile = new File("testSourceFile.txt");
        FileOutputStream sourceFileOutputStream = new FileOutputStream(sourceFile);
        sourceFileOutputStream.write("Test data".getBytes());
        sourceFileOutputStream.close();

        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        ZipEntry zipEntry = new ZipEntry("testEntry.txt");
        zipOutputStream.putNextEntry(zipEntry);

        // Write test data to the zip file
        FileInputStream inputStream = new FileInputStream(sourceFile);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) > 0) {
            zipOutputStream.write(buffer, 0, bytesRead);
        }
        zipOutputStream.finish();
        zipOutputStream.close();
        zipFile.close();

        // Read test data from the zip file
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(testFileName));
        ZipEntry zipEntryRead = zipInputStream.getNextEntry();
        byte[] bufferToRead = new byte[1024];
        int bytesReadToRead = 0;
        while ((bytesReadToRead = zipInputStream.read(bufferToRead)) > 0) {
            System.out.print(new String(bufferToRead, 0, bytesReadToRead));
        }
        zipInputStream.close();

        // Parse X7875_NewUnix extra field from the zip file and check its values
        X7875_NewUnix extraField = new X7875_NewUnix();
        extraField.parseFromLocalFileData(getX7875_NewUnixDataFromZipFile(testFileName, "testEntry.txt"),
                0, getX7875_NewUnixDataFromZipFile(testFileName, "testEntry.txt").length);
        long uidFromZipFile = extraField.getUID();
        long gidFromZipFile = extraField.getGID();
        assertEquals("UID from zip file is not the expected one", EXPECTED_UID, uidFromZipFile);
        assertEquals("GID from zip file is not the expected one", EXPECTED_GID, gidFromZipFile);

        // Delete the test file
        File fileToDelete = new File(testFileName);
        fileToDelete.delete();
    }

    private static byte[] getX7875_NewUnixDataFromZipFile(String zipFileName, String entryName) throws IOException {
        ZipFile zipFile = new ZipFile(zipFileName, "UTF-8");
        ZipEntry zipEntry = zipFile.getEntry(entryName);
        ZipInputStream zipInputStream = new ZipInputStream(zipFile.getInputStream(zipEntry));
        byte[] data = new byte[zipEntry.getSize()];
        zipInputStream.read(data);
        zipInputStream.close();
        zipFile.close();
        return data;
    }
}