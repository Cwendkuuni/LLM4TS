package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * Test cases for CpioArchiveOutputStream class
 */
public class CpioArchiveOutputStreamTest {

    @Test
    public void testEmptyStream() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, FORMAT_NEW);
        cpioOutputStream.finish();
        byte[] expectedOutput = new byte[]{
                MAGIC_NEW[0], MAGIC_NEW[1], MAGIC_NEW[2], MAGIC_NEW[3],
                NAMED_HEADER[0], NAMED_HEADER[1], NAMED_HEADER[2],
                NAMED_HEADER[3], NAMED_HEADER[4], NAMED_HEADER[5],
                NAMED_HEADER[6], NAMED_HEADER[7], NAMED_HEADER[8],
                NAMED_HEADER[9], NAMED_HEADER[10], NAMED_HEADER[11],
                TRAILER_MAGIC[0], TRAILER_MAGIC[1], TRAILER_MAGIC[2],
                TRAILER_MAGIC[3]
        };
        byte[] output = ((ByteArrayOutputStream) outputStream).toByteArray();
        assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testPutNextEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, FORMAT_NEW);

        String entryName = "testEntry.txt";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(10);
        entry.setFormat(FORMAT_NEW);

        cpioOutputStream.putNextEntry(entry);
        byte[] testData = "Test data".getBytes();
        cpioOutputStream.write(testData);
        cpioOutputStream.closeArchiveEntry();
        cpioOutputStream.finish();

        byte[] expectedOutput = createTestOutput(entryName, testData);
        byte[] output = ((ByteArrayOutputStream) outputStream).toByteArray();
        assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testPutNextEntryWithNullEntry() {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, FORMAT_NEW);
        cpioOutputStream.putNextEntry(null);
        try {
            cpioOutputStream.write("Test data".getBytes());
        } catch (IOException e) {
            // expected
        }
        cpioOutputStream.finish();
        assertTrue(cpioOutputStream.isClosed());
    }

    @Test
    public void testPutNextEntryWithDuplicateEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, FORMAT_NEW);

        String entryName = "testEntry.txt";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(10);
        entry.setFormat(FORMAT_NEW);
        cpioOutputStream.putNextEntry(entry);

        assertThrows(IOException.class, () -> cpioOutputStream.putNextEntry(entry));
    }

    @Test
    public void testPutNextEntryWithInvalidFormat() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, (short) 0xFFFF);

        String entryName = "testEntry.txt";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(10);
        entry.setFormat(FORMAT_NEW);

        cpioOutputStream.putNextEntry(entry);
        try {
            cpioOutputStream.finish();
        } finally {
            cpioOutputStream.close();
        }
        assertFalse(cpioOutputStream.isFinished());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, FORMAT_NEW);

        String entryName = "testEntry.txt";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(10);
        entry.setFormat(FORMAT_NEW);
        cpioOutputStream.putNextEntry(entry);

        byte[] testData = "Test data".getBytes();
        cpioOutputStream.write(testData);
        cpioOutputStream.closeArchiveEntry();

        byte[] expectedOutput = createTestOutput(entryName, testData);
        byte[] output = ((ByteArrayOutputStream) outputStream).toByteArray();
        assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testCloseArchiveEntryWithClosedStream() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, FORMAT_NEW);
        cpioOutputStream.finish();

        String entryName = "testEntry.txt";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(10);
        entry.setFormat(FORMAT_NEW);
        cpioOutputStream.putNextEntry(entry);

        try {
            cpioOutputStream.write("Test data".getBytes());
        } finally {
            cpioOutputStream.closeArchiveEntry();
        }

        byte[] expectedOutput = createTestOutput(entryName, "Test data".getBytes());
        byte[] output = ((ByteArrayInputStream) new ByteArrayInputStream(cpioOutputStream.getBuffer()))
                .readAllBytes();
        assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testClose() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, FORMAT_NEW);

        String entryName = "testEntry.txt";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(10);
        entry.setFormat(FORMAT_NEW);
        cpioOutputStream.putNextEntry(entry);

        byte[] testData = "Test data".getBytes();
        cpioOutputStream.write(testData);
        cpioOutputStream.finish();

        assertThrows(IOException.class, () -> cpioOutputStream.putNextEntry(entry));

        cpioOutputStream.close();

        byte[] expectedOutput = createTestOutput(entryName, testData);
        byte[] output = ((ByteArrayOutputStream) outputStream).toByteArray();
        assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testWriteWithClosedStream() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, FORMAT_NEW);
        cpioOutputStream.finish();

        String entryName = "testEntry.txt";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(10);
        entry.setFormat(FORMAT_NEW);
        cpioOutputStream.putNextEntry(entry);

        try {
            cpioOutputStream.write("Test data".getBytes());
        } finally {
            cpioOutputStream.closeArchiveEntry();
        }

        cpioOutputStream.write("Test data2".getBytes());

        byte[] expectedOutput = createTestOutput(entryName, "Test data2".getBytes());
        byte[] output = ((ByteArrayInputStream) new ByteArrayInputStream(cpioOutputStream.getBuffer()))
                .readAllBytes();
        assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testWriteWithNegativeOffsetOrLength() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, FORMAT_NEW);

        String entryName = "testEntry.txt";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(10);
        entry.setFormat(FORMAT_NEW);
        cpioOutputStream.putNextEntry(entry);

        byte[] testData = "Test data".getBytes();

        try {
            cpioOutputStream.write(testData, -1, 10);
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            cpioOutputStream.write(testData, 0, -1);
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        cpioOutputStream.write(testData);
        cpioOutputStream.finish();

        byte[] expectedOutput = createTestOutput(entryName, testData);
        byte[] output = ((ByteArrayOutputStream) outputStream).toByteArray();
        assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testWriteWithWritingPastEndOfEntryData() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, FORMAT_NEW);

        String entryName = "testEntry.txt";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(10);
        entry.setFormat(FORMAT_NEW);
        cpioOutputStream.putNextEntry(entry);

        byte[] testData = "Test data".getBytes();

        cpioOutputStream.write(testData);
        cpioOutputStream.write(testData, 0, testData.length);
        cpioOutputStream.finish();

        byte[] expectedOutput = createTestOutput(entryName, testData.concat(testData));
        byte[] output = ((ByteArrayInputStream) new ByteArrayInputStream(cpioOutputStream.getBuffer()))
                .readAllBytes();
        assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testWriteWithSwappedHalfWords() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, FORMAT_OLD_BINARY);

        String entryName = "testEntry.txt";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(10);
        entry.setFormat(FORMAT_OLD_BINARY);
        cpioOutputStream.putNextEntry(entry);

        byte[] testData = "Test data".getBytes();

        cpioOutputStream.write(testData);
        cpioOutputStream.finish();

        byte[] expectedOutput = createTestOutputSwappedHalfWords(entryName, testData);
        byte[] output = ((ByteArrayOutputStream) outputStream).toByteArray();
        assertArrayEquals(expectedOutput, output);
    }

    private byte[] createTestOutput(String entryName, byte[] testData) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, FORMAT_NEW);

        String entryName2 = entryName + "_copy";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(testData.length);
        entry.setFormat(FORMAT_NEW);
        cpioOutputStream.putNextEntry(entry);
        cpioOutputStream.write(testData);
        cpioOutputStream.putNextEntry(new CpioArchiveEntry(entryName2));
        cpioOutputStream.write(testData);
        cpioOutputStream.finish();

        return ((ByteArrayOutputStream) outputStream).toByteArray();
    }

    private byte[] createTestOutputSwappedHalfWords(String entryName, byte[] testData) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, FORMAT_OLD_BINARY);

        String entryName2 = entryName + "_copy";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(testData.length);
        entry.setFormat(FORMAT_OLD_BINARY);
        cpioOutputStream.putNextEntry(entry);
        CpioUtil.writeSwappedBytes(cpioOutputStream, testData);
        cpioOutputStream.putNextEntry(new CpioArchiveEntry(entryName2));
        CpioUtil.writeSwappedBytes(cpioOutputStream, testData);
        cpioOutputStream.finish();

        return ((ByteArrayOutputStream) outputStream).toByteArray();
    }

    private void assertThrows(Class<? extends Throwable> exceptionClass, Runnable runnable) {
        try {
            runnable.run();
        } catch (Throwable throwable) {
            if (exceptionClass.isInstance(throwable)) {
                return;
            }
            throw new RuntimeException("Unexpected exception: " + throwable.getClass().getName());
        }
        throw new RuntimeException("Expected exception of type " + exceptionClass.getName() + " was not thrown");
    }
}