import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.CpioUtil;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testConstructors() {
        // Test default constructor
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                new ByteArrayOutputStream());
        Assert.assertEquals(CpioConstants.FORMAT_NEW, outputStream.getFormat());

        // Test constructor with specified format
        CpioArchiveOutputStream outputStreamWithFormat = new CpioArchiveOutputStream(
                new ByteArrayOutputStream(), CpioConstants.FORMAT_OLD_BINARY);
        Assert.assertEquals(CpioConstants.FORMAT_OLD_BINARY, outputStreamWithFormat.getFormat());
    }

    @Test
    public void testPutNextEntry() throws IOException {
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                new ByteArrayOutputStream());

        // Test writing a new entry
        CpioArchiveEntry newEntry = new CpioArchiveEntry();
        newEntry.setName("testfile.txt");
        newEntry.setSize(10);
        outputStream.putNextEntry(newEntry);
        Assert.assertEquals(10, outputStream.getNextSizedEntry().getSize());

        // Test writing an old binary entry
        CpioArchiveEntry oldBinaryEntry = new CpioArchiveEntry();
        oldBinaryEntry.setFormat(CpioConstants.FORMAT_OLD_BINARY);
        oldBinaryEntry.setName("testfile.bin");
        oldBinaryEntry.setSize(20);
        outputStream.putNextEntry(oldBinaryEntry);
        Assert.assertEquals(20, outputStream.getNextSizedEntry().getSize());

        // Test writing an entry with an already existing name
        CpioArchiveEntry existingEntry = new CpioArchiveEntry();
        existingEntry.setName("testfile.txt");
        outputStream.putNextEntry(existingEntry);
        try {
            outputStream.putNextEntry(newEntry);
            Assert.fail("Expected an IOException for writing an entry with an already existing name");
        } catch (IOException e) {
            // Expected
        }

        // Test writing an entry with a negative size
        CpioArchiveEntry negativeSizeEntry = new CpioArchiveEntry();
        negativeSizeEntry.setName("testfile.neg");
        negativeSizeEntry.setSize(-10);
        try {
            outputStream.putNextEntry(negativeSizeEntry);
            Assert.fail("Expected an IOException for writing an entry with a negative size");
        } catch (IOException e) {
            // Expected
        }

        // Test writing an entry with a null name
        CpioArchiveEntry nullNameEntry = new CpioArchiveEntry();
        nullNameEntry.setSize(10);
        try {
            outputStream.putNextEntry(nullNameEntry);
            Assert.fail("Expected an IOException for writing an entry with a null name");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testWrite() throws IOException {
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                new ByteArrayOutputStream());
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile.bin");
        entry.setSize(10);
        outputStream.putNextEntry(entry);

        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);
        outputStream.write(testData, 0, testData.length);

        byte[] expectedOutput = new byte[entry.getSize() + testData.length];
        System.arraycopy(testData, 0, expectedOutput, 0, testData.length);
        byte[] actualOutput = ((ByteArrayOutputStream) outputStream.getOutputStream())
                .toByteArray();

        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                new ByteArrayOutputStream());
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile.bin");
        entry.setSize(10);
        outputStream.putNextEntry(entry);

        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);
        outputStream.write(testData, 0, testData.length);

        outputStream.closeArchiveEntry();

        byte[] expectedHeader = new byte[entry.getHeaderSize()];
        byte[] actualHeader = ((ByteArrayOutputStream) outputStream.getOutputStream())
                .toByteArray();

        Map<Integer, Byte[]> testHeaders = new HashMap<>();
        testHeaders.put(CpioConstants.FORMAT_NEW, new byte[12]); // New format header
        testHeaders.put(CpioConstants.FORMAT_OLD_BINARY, new byte[20]); // Old binary format header

        byte[] entryHeader = testHeaders.get(entry.getFormat());

        System.arraycopy(entryHeader, 0, expectedHeader, 0, entryHeader.length);
        Assert.assertArrayEquals(expectedHeader, actualHeader);

        byte[] expectedFooter = new byte[entry.getSize()];
        System.arraycopy(testData, 0, expectedFooter, 0, testData.length);

        byte[] actualFooter = new ByteArrayInputStream(
                ((ByteArrayOutputStream) outputStream.getOutputStream())
                        .toByteArray())
                .readAllBytes();

        Assert.assertArrayEquals(expectedFooter, actualFooter);

        Assert.assertTrue(outputStream.getNextSizedEntry() == null);
    }

    @Test
    public void testFinish() throws IOException {
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                new ByteArrayOutputStream());

        // Test writing multiple entries
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testfile1.txt");
        entry1.setSize(10);
        outputStream.putNextEntry(entry1);
        outputStream.write("Test data 1".getBytes(StandardCharsets.UTF_8));

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testfile2.txt");
        entry2.setSize(20);
        outputStream.putNextEntry(entry2);
        outputStream.write("Test data 2".getBytes(StandardCharsets.UTF_8));

        outputStream.finish();

        byte[] expectedOutput = new byte[entry1.getSize() + "Test data 1".getBytes(StandardCharsets.UTF_8).length
                + entry2.getSize() + "Test data 2".getBytes(StandardCharsets.UTF_8).length
                + 8]; // CPIO trailer size

        byte[] actualOutput = ((ByteArrayOutputStream) outputStream.getOutputStream())
                .toByteArray();

        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testClose() throws IOException {
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                new ByteArrayOutputStream());

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile.txt");
        entry.setSize(10);
        outputStream.putNextEntry(entry);

        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);
        outputStream.write(testData, 0, testData.length);

        outputStream.close();

        Assert.assertFalse(outputStream.isOpen());
        Assert.assertEquals(CpioConstants.FORMAT_OLD_BINARY, outputStream.getFormat());
        Assert.assertTrue(outputStream.getNextSizedEntry() == null);
    }

    @Test
    public void testWritingToTraileredOutputStream() throws IOException {
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                new ByteArrayOutputStream());

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile.txt");
        entry.setSize(10);
        outputStream.putNextEntry(entry);

        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);
        outputStream.write(testData, 0, testData.length);

        outputStream.finish();

        byte[] expectedOutput = new byte[entry.getSize() + testData.length];
        System.arraycopy(testData, 0, expectedOutput, 0, testData.length);

        byte[] actualOutput = ((ByteArrayOutputStream) outputStream.getOutputStream())
                .toByteArray();

        Assert.assertArrayEquals(expectedOutput, actualOutput);

        Assert.assertTrue(outputStream.getNextSizedEntry() == null);
    }

    @Test
    public void testWritingToTraileredOutputStreamWithSwappedHalfWords()
            throws IOException {
        CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(
                new ByteArrayOutputStream());

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile.txt");
        entry.setSize(10);
        entry.setFormat(CpioConstants.FORMAT_OLD_BINARY);
        outputStream.putNextEntry(entry);

        byte[] testData = "Test data".getBytes(StandardCharsets.UTF_8);
        outputStream.write(testData, 0, testData.length);

        outputStream.finish();

        byte[] expectedOutput = new byte[entry.getSize() + testData.length];

        System.arraycopy(CpioUtil.byteArrayToInt(testData, 0, 2), 0, expectedOutput, 0, 2);
        System.arraycopy(testData, 2, expectedOutput, 2, testData.length - 2);

        byte[] actualOutput = ((ByteArrayOutputStream) outputStream.getOutputStream())
                .toByteArray();

        Assert.assertArrayEquals(expectedOutput, actualOutput);

        Assert.assertTrue(outputStream.getNextSizedEntry() == null);
    }
}