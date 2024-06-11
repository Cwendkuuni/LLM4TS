package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.utils.CpioUtil;

/**
 * Test class for CpioArchiveOutputStream
 */
public class CpioArchiveOutputStreamTest {

    /**
     * Test writing to CpioArchiveOutputStream with different formats
     */
    @org.junit.Test
    public void testWriteWithDifferentFormats() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(
                outputStream, CpioConstants.FORMAT_NEW);

        // write an entry
        String entryName = "test_entry";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        cpioOutputStream.putArchiveEntry(entry);
        String testString = "Test content";
        byte[] testBytes = testString.getBytes();
        cpioOutputStream.write(testBytes, 0, testBytes.length);
        cpioOutputStream.closeArchiveEntry();
        cpioOutputStream.finish();

        byte[] expectedResultNew = getExpectedResultNewFormat(entryName,
                testString);
        assertEquals("Unexpected result for new format", expectedResultNew,
                outputStream.toByteArray());

        // write an entry with crc
        cpioOutputStream = new CpioArchiveOutputStream(outputStream,
                CpioConstants.FORMAT_NEW_CRC);
        entry = new CpioArchiveEntry();
        entry.setName(entryName);
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.write(testBytes, 0, testBytes.length);
        cpioOutputStream.closeArchiveEntry();
        cpioOutputStream.finish();

        byte[] expectedResultNewCrc = getExpectedResultNewFormatWithCrc(
                entryName, testString);
        assertEquals("Unexpected result for new format with CRC",
                expectedResultNewCrc, outputStream.toByteArray());

        // write an entry with old format
        cpioOutputStream = new CpioArchiveOutputStream(outputStream);
        entry = new CpioArchiveEntry();
        entry.setName(entryName);
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.write(testBytes, 0, testBytes.length);
        cpioOutputStream.closeArchiveEntry();
        cpioOutputStream.finish();

        byte[] expectedResultOld = getExpectedResultOldFormat(entryName,
                testString);
        assertEquals("Unexpected result for old format", expectedResultOld,
                outputStream.toByteArray());

        outputStream.close();
    }

    /**
     * Test writing to CpioArchiveOutputStream with invalid formats
     */
    @org.junit.Test
    public void testWriteWithInvalidFormats() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        assertThrows(IllegalArgumentException.class, () -> {
            new CpioArchiveOutputStream(outputStream, (short) 1234);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new CpioArchiveOutputStream(outputStream, (short) -1);
        });
    }

    /**
     * Test writing to CpioArchiveOutputStream with an existing name
     */
    @org.junit.Test
    public void testWriteWithExistingName() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(
                outputStream);

        String entryName = "test_entry";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.write("Test content".getBytes(), 0,
                "Test content".length());
        cpioOutputStream.closeArchiveEntry();

        assertThrows(IOException.class, () -> {
            cpioOutputStream.putArchiveEntry(entry);
        });

        cpioOutputStream.finish();
        outputStream.close();
    }

    /**
     * Test writing to CpioArchiveOutputStream with a null entry
     */
    @org.junit.Test
    public void testWriteWithNullEntry() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(
                outputStream);

        assertThrows(NullPointerException.class, () -> {
            cpioOutputStream.putArchiveEntry(null);
        });

        cpioOutputStream.finish();
        outputStream.close();
    }

    /**
     * Test creating an ArchiveEntry from a file
     */
    @org.junit.Test
    public void testCreateArchiveEntryFromFile() throws IOException {
        String testFilePath = "test_file.txt";
        File testFile = new File(testFilePath);
        testFile.createNewFile();

        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(
                new FileOutputStream(testFilePath + ".cpio"));
        ArchiveEntry entry = cpioOutputStream.createArchiveEntry(testFile,
                "test_entry");

        assertEquals("Unexpected name for created ArchiveEntry", "test_entry",
                entry.getName());
        assertEquals("Unexpected size for created ArchiveEntry", testFile.length(),
                entry.getSize());

        cpioOutputStream.close();
        testFile.delete();
    }

    /**
     * Test writing to CpioArchiveOutputStream with empty input
     */
    @org.junit.Test
    public void testWriteWithEmptyInput() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(
                outputStream);

        String entryName = "test_entry";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.finish();

        byte[] expectedResultEmpty = getExpectedResultOldFormat(entryName, "");
        assertEquals("Unexpected result for empty entry", expectedResultEmpty,
                outputStream.toByteArray());

        outputStream.close();
    }

    /**
     * Test closing CpioArchiveOutputStream with unclosed entries
     */
    @org.junit.Test
    public void testCloseWithUnclosedEntries() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(
                outputStream);

        String entryName = "test_entry";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        cpioOutputStream.putArchiveEntry(entry);

        assertThrows(IllegalStateException.class, () -> {
            cpioOutputStream.close();
        });

        cpioOutputStream.finish();
        outputStream.close();
    }

    /**
     * Test closing CpioArchiveOutputStream twice
     */
    @org.junit.Test
    public void testCloseTwice() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(
                outputStream);

        String entryName = "test_entry";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.write("Test content".getBytes(), 0,
                "Test content".length());
        cpioOutputStream.closeArchiveEntry();
        cpioOutputStream.finish();
        cpioOutputStream.close();

        byte[] expectedResult = getExpectedResultNewFormat(entryName,
                "Test content");
        assertEquals("Unexpected result for closing twice", expectedResult,
                outputStream.toByteArray());

        outputStream.close();
    }

    /**
     * Helper method to generate expected result for new format without CRC
     */
    private byte[] getExpectedResultNewFormat(String entryName, String testString) {
        String expectedResult = MAGIC_NEW + "\n"
                + Long.toUnsignedLong(CpioConstants.C_ISREG)
                + "\n" + Long.toUnsignedLong(640 + 4096)
                + "\n" + Long.toUnsignedLong(0)
                + "\n" + Long.toUnsignedLong(0)
                + "\n" + Long.toUnsignedLong(0)
                + "\n" + Long.toUnsignedLong(0)
                + "\n" + Long.toUnsignedLong(0)
                + "\n" + Long.toUnsignedLong(System.currentTimeMillis() / 1000)
                + "\n" + Long.toUnsignedLong(testString.length()) + "\n"
                + Long.toUnsignedLong(testString.length())
                + "\n" + testString + "\n";
        return CpioUtil.String2ByteArray(expectedResult);
    }

    /**
     * Helper method to generate expected result for new format with CRC
     */
    private byte[] getExpectedResultNewFormatWithCrc(String entryName,
            String testString) {
        byte[] testBytes = testString.getBytes();
        long crc = 0;
        for (byte b : testBytes) {
            crc += b & 0xFF;
        }
        String expectedResult = MAGIC_NEW_CRC + "\n"
                + Long.toUnsignedLong(CpioConstants.C_ISREG)
                + "\n" + Long.toUnsignedLong(640 + 4096)
                + "\n" + Long.toUnsignedLong(0)
                + "\n" + Long.toUnsignedLong(0)
                + "\n" + Long.toUnsignedLong(0)
                + "\n" + Long.toUnsignedLong(0)
                + "\n" + Long.toUnsignedLong(0)
                + "\n" + Long.toUnsignedLong(System.currentTimeMillis() / 1000)
                + "\n" + Long.toUnsignedLong(testString.length()) + "\n"
                + Long.toUnsignedLong(testString.length())
                + "\n" + testString + "\n" + Long.toUnsignedLong(crc);
        return CpioUtil.String2ByteArray(expectedResult);
    }

    /**
     * Helper method to generate expected result for old format
     */
    private byte[] getExpectedResultOldFormat(String entryName, String testString) {
        String expectedResult = Long.toUnsignedOctalString(0600) + "\n"
                + Long.toUnsignedLong(CpioConstants.C_ISREG)
                + "\n" + Long.toUnsignedLong(640 + 4096)
                + "\n" + Long.toUnsignedLong(0)
                + "\n" + Long.toUnsignedLong(System.currentTimeMillis() / 1000)
                + "\n" + (entryName.length() + 1) + "\n"
                + Long.toUnsignedLong(testString.length()) + "\n"
                + testString + "\n";
        return CpioUtil.String2ByteArray(expectedResult);
    }
}