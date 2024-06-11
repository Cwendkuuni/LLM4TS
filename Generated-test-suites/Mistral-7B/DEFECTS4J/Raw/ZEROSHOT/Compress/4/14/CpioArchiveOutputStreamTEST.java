package org.apache.commons.compress.archivers.cpio;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Tests for the CpioArchiveOutputStream class.
 */
public class CpioArchiveOutputStreamTest {

    @Test
    public void testCpioArchiveOutputStream() throws IOException {
        // Test construction with specific format
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        // Test construction with default format
        OutputStream outputStreamDefault = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStreamDefault = new CpioArchiveOutputStream(outputStreamDefault);

        // Test exception when passing an unsupported format
        try {
            CpioArchiveOutputStream cpioArchiveOutputStreamInvalid = new CpioArchiveOutputStream(outputStream, (short) 123);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test putArchiveEntry method
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName("testfile");
        cpioArchiveEntry.setMode(CpioConstants.C_ISREG);
        cpioArchiveEntry.setTime(System.currentTimeMillis());
        cpioArchiveEntry.setSize(5);

        cpioArchiveOutputStream.putArchiveEntry(cpioArchiveEntry);

        // Test exception when putArchiveEntry is called with an entry that is already in the stream
        cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName("testfile");
        try {
            cpioArchiveOutputStream.putArchiveEntry(cpioArchiveEntry);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }

        // Test writing data to the entry
        byte[] data = "12345".getBytes();
        cpioArchiveOutputStream.write(data, 0, data.length);

        // Test closing the entry
        cpioArchiveOutputStream.closeArchiveEntry();

        // Test exception when putArchiveEntry is called after closeArchiveEntry
        try {
            cpioArchiveOutputStream.putArchiveEntry(cpioArchiveEntry);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }

        // Test finishing the stream
        cpioArchiveOutputStream.finish();

        // Test exception when finish is called more than once
        try {
            cpioArchiveOutputStream.finish();
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }

        // Test creating a new archive entry
        File testFile = new File("testFile.txt");
        CpioArchiveEntry testEntry = cpioArchiveOutputStreamDefault.createArchiveEntry(testFile, "testFile.txt");

        // Test writing data to the new archive entry
        testFile.createNewFile();
        byte[] testData = "Test File".getBytes();
        cpioArchiveOutputStreamDefault.putArchiveEntry(testEntry);
        cpioArchiveOutputStreamDefault.write(testData, 0, testData.length);
        cpioArchiveOutputStreamDefault.closeArchiveEntry();

        // Test reading the created archive
        ByteArrayInputStream inputStream = new ByteArrayInputStream(cpioArchiveOutputStream.toByteArray());
        ArchiveUtils.unmarshal(inputStream);
    }

    /**
     * Helper method to convert a byte array to a short.
     *
     * @param bytes the byte array
     * @return the short value
     */
    private short bytesToShort(byte[] bytes) {
        short value = 0;
        for (int i = 0; i < 2; i++) {
            value = (short) (value << 8 | (bytes[i] & 0xFF));
        }
        return value;
    }

    /**
     * Helper method to convert a byte array to a long.
     *
     * @param bytes the byte array
     * @param length the number of bytes to read
     * @param swapHalfWords whether to swap half words in the resulting long
     * @return the long value
     */
    private long bytesToLong(byte[] bytes, int length, boolean swapHalfWords) {
        long value = 0;
        for (int i = 0; i < length; i += 4) {
            long tmp = 0;
            for (int j = 0; j < 4; j++) {
                tmp = (tmp << 8) | (bytes[i + j] & 0xFF);
            }
            if (swapHalfWords) {
                tmp = ((tmp << 16) | (tmp >>> 16));
            }
            value += tmp;
        }
        return value;
    }

    /**
     * Helper method to convert a byte array to a string.
     *
     * @param bytes the byte array
     * @return the string value
     */
    private String bytesToString(byte[] bytes) {
        return new String(bytes, 0, bytes.length, "UTF-8");
    }

    /**
     * Helper method to test reading an archive entry.
     *
     * @param archive the archive input stream
     * @param index the index of the entry to read
     * @return the archive entry
     * @throws IOException if an I/O error occurs
     */
    private ArchiveEntry readArchiveEntry(ArchiveInputStream archive, int index) throws IOException {
        ArchiveEntry entry = archive.getNextEntry();
        for (int i = 0; i < index; i++) {
            if (entry == null) {
                throw new IOException("Expected entry at index " + index);
            }
            entry = archive.getNextEntry();
        }
        return entry;
    }

    /**
     * Helper method to test reading the header of an archive entry.
     *
     * @param archive the archive input stream
     * @param index the index of the entry to read
     * @return the header of the entry
     * @throws IOException if an I/O error occurs
     */
    private CpioArchiveEntry readArchiveEntryHeader(ArchiveInputStream archive, int index) throws IOException {
        CpioArchiveEntry entry = (CpioArchiveEntry) readArchiveEntry(archive, index);
        return entry;
    }

    /**
     * Helper method to read the data of an archive entry.
     *
     * @param archive the archive input stream
     * @param index the index of the entry to read
     * @return the data of the entry
     * @throws IOException if an I/O error occurs
     */
    private byte[] readArchiveEntryData(ArchiveInputStream archive, int index) throws IOException {
        CpioArchiveEntry entry = readArchiveEntryHeader(archive, index);
        byte[] data = new byte[entry.getSize()];
        InputStream inputStream = archive.getInputStream(entry);
        inputStream.read(data);
        inputStream.close();
        return data;
    }

    /**
     * Helper method to test writing an archive with multiple entries.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testWriteMultipleEntries() throws IOException {
        OutputStream outputStream = new FileOutputStream("test.cpio");
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);

        // Test writing the first entry
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testfile1.txt");
        entry1.setMode(CpioConstants.C_ISREG);
        entry1.setTime(System.currentTimeMillis());
        entry1.setSize(5);
        cpioArchiveOutputStream.putArchiveEntry(entry1);
        byte[] data1 = "12345".getBytes();
        cpioArchiveOutputStream.write(data1, 0, data1.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        // Test writing the second entry
        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testfile2.txt");
        entry2.setMode(CpioConstants.C_ISREG);
        entry2.setTime(System.currentTimeMillis());
        entry2.setSize(10);
        cpioArchiveOutputStream.putArchiveEntry(entry2);
        byte[] data2 = "Test File 2".getBytes();
        cpioArchiveOutputStream.write(data2, 0, data2.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        // Test finishing the stream
        cpioArchiveOutputStream.finish();
        outputStream.close();

        // Test reading the created archive
        File inputFile = new File("test.cpio");
        ArchiveInputStream archive = new CpioFileArchiveInputStream(inputFile);

        // Test reading the first entry
        CpioArchiveEntry entry1Read = readArchiveEntryHeader(archive, 0);
        Assert.assertEquals("testfile1.txt", entry1Read.getName());
        Assert.assertEquals(5, entry1Read.getSize());
        Assert.assertEquals(CpioConstants.C_ISREG, entry1Read.getFileType());

        // Test reading the data of the first entry
        byte[] data1Read = readArchiveEntryData(archive, 0);
        Assert.assertArrayEquals(data1, data1Read);

        // Test reading the second entry
        CpioArchiveEntry entry2Read = readArchiveEntryHeader(archive, 1);
        Assert.assertEquals("testfile2.txt", entry2Read.getName());
        Assert.assertEquals(10, entry2Read.getSize());
        Assert.assertEquals(CpioConstants.C_ISREG, entry2Read.getFileType());

        // Test reading the data of the second entry
        byte[] data2Read = readArchiveEntryData(archive, 1);
        Assert.assertArrayEquals(data2, data2Read);

        // Test exception when reading a non-existent entry
        try {
            byte[] data3Read = readArchiveEntryData(archive, 2);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }

        archive.close();
        inputFile.delete();
    }
}