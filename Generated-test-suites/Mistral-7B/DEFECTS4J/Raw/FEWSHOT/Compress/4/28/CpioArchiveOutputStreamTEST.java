package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;

/**
 * JUnit 4 test cases for CpioArchiveOutputStream class.
 */
public class CpioArchiveOutputStreamTest {

    @Test
    public void testCpioArchiveOutputStreamCreation() {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(out);
        assertNull(outputStream.getEntry());
        assertFalse(outputStream.isClosed());
        assertFalse(outputStream.isFinished());
        assertEquals(FORMAT_NEW, outputStream.getEntryFormat());
        assertEquals(0, outputStream.getCrc());
        assertEquals(0, outputStream.getWritten());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(out);

        final String testFileName = "testFile.txt";
        final String testFileContent = "Test content";
        final File testFile = new File(testFileName);

        final CpioArchiveEntry entry = outputStream.createArchiveEntry(testFile, testFileName);

        outputStream.putArchiveEntry(entry);
        assertEquals(entry, outputStream.getEntry());
        assertFalse(outputStream.isClosed());
        assertFalse(outputStream.isFinished());
        assertEquals(FORMAT_NEW, outputStream.getEntryFormat());

        final byte[] expectedMagicNumber = ArchiveUtils.toAsciiBytes(MAGIC_NEW);
        final byte[] actualMagicNumber = outputStream.getBuffer();
        assertArrayEquals(expectedMagicNumber, actualMagicNumber);

        outputStream.write(testFileContent.getBytes());
        assertEquals(testFileContent.length(), outputStream.getWritten());

        outputStream.closeArchiveEntry();
        assertNull(outputStream.getEntry());
        assertEquals(testFileContent.length(), outputStream.getWritten());

        final byte[] expectedTrailer = CpioArchiveEntry.toCpioTrailerBytes();
        final byte[] actualTrailer = outputStream.getBuffer();
        assertArrayEquals(expectedTrailer, actualTrailer);

        outputStream.finish();
        assertTrue(outputStream.isFinished());
        assertTrue(outputStream.isClosed());
    }

    @Test
    public void testPutArchiveEntryWithFormat() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(out, FORMAT_NEW_CRC);

        final String testFileName = "testFile.txt";
        final String testFileContent = "Test content";
        final File testFile = new File(testFileName);

        final CpioArchiveEntry entry = outputStream.createArchiveEntry(testFile, testFileName);

        outputStream.putArchiveEntry(entry);
        assertEquals(entry, outputStream.getEntry());
        assertFalse(outputStream.isClosed());
        assertFalse(outputStream.isFinished());
        assertEquals(FORMAT_NEW_CRC, outputStream.getEntryFormat());

        final byte[] expectedMagicNumber = ArchiveUtils.toAsciiBytes(MAGIC_NEW_CRC);
        final byte[] actualMagicNumber = outputStream.getBuffer();
        assertArrayEquals(expectedMagicNumber, actualMagicNumber);

        outputStream.write(testFileContent.getBytes());
        assertEquals(testFileContent.length(), outputStream.getWritten());

        outputStream.closeArchiveEntry();
        assertNull(outputStream.getEntry());
        assertEquals(testFileContent.length(), outputStream.getWritten());

        final byte[] expectedTrailer = CpioArchiveEntry.toCpioTrailerBytes();
        final byte[] actualTrailer = outputStream.getBuffer();
        assertArrayEquals(expectedTrailer, actualTrailer);

        outputStream.finish();
        assertTrue(outputStream.isFinished());
        assertTrue(outputStream.isClosed());
    }

    @Test
    public void testPutArchiveEntryWithInvalidFormat() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(out);

        final String testFileName = "testFile.txt";
        final String testFileContent = "Test content";
        final File testFile = new File(testFileName);

        final CpioArchiveEntry entry = outputStream.createArchiveEntry(testFile, testFileName);

        outputStream.putArchiveEntry(entry);
        assertThrows(IllegalArgumentException.class, () -> outputStream.putArchiveEntry(entry));
    }

    @Test
    public void testPutArchiveEntryWithDuplicateEntryName() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(out);

        final String testFileName = "testFile.txt";
        final String testFileContent = "Test content";
        final File testFile = new File(testFileName);

        final CpioArchiveEntry firstEntry = outputStream.createArchiveEntry(testFile, testFileName);
        outputStream.putArchiveEntry(firstEntry);

        final CpioArchiveEntry secondEntry = outputStream.createArchiveEntry(testFile, testFileName);
        assertThrows(IOException.class, () -> outputStream.putArchiveEntry(secondEntry));
    }

    @Test
    public void testWrite() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(out);

        final String testFileName = "testFile.txt";
        final String testFileContent = "Test content";
        final File testFile = new File(testFileName);

        final CpioArchiveEntry entry = outputStream.createArchiveEntry(testFile, testFileName);
        outputStream.putArchiveEntry(entry);

        final byte[] testData = testFileContent.getBytes();
        final int testDataLength = testData.length;
        outputStream.write(testData, 0, testDataLength);
        assertEquals(testDataLength, outputStream.getWritten());

        final byte[] expectedTrailer = CpioArchiveEntry.toCpioTrailerBytes();
        final byte[] actualTrailer = outputStream.getBuffer();
        assertArrayEquals(expectedTrailer, actualTrailer);
        outputStream.finish();
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(out);

        final String testFileName = "testFile.txt";
        final String testFileContent = "Test content";
        final File testFile = new File(testFileName);

        final CpioArchiveEntry entry = outputStream.createArchiveEntry(testFile, testFileName);
        outputStream.putArchiveEntry(entry);

        final byte[] testData = testFileContent.getBytes();
        final int testDataLength = testData.length;
        outputStream.write(testData, 0, testDataLength);

        outputStream.closeArchiveEntry();

        final byte[] expectedTrailer = CpioArchiveEntry.toCpioTrailerBytes();
        final byte[] actualTrailer = outputStream.getBuffer();
        assertArrayEquals(expectedTrailer, actualTrailer);

        assertNull(outputStream.getEntry());
        assertEquals(testDataLength, outputStream.getWritten());
    }

    @Test
    public void testFinish() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(out);

        final String testFileName = "testFile.txt";
        final String testFileContent = "Test content";
        final File testFile = new File(testFileName);

        final CpioArchiveEntry entry = outputStream.createArchiveEntry(testFile, testFileName);
        outputStream.putArchiveEntry(entry);

        final byte[] testData = testFileContent.getBytes();
        final int testDataLength = testData.length;
        outputStream.write(testData, 0, testDataLength);

        outputStream.finish();

        final byte[] expectedTrailer = CpioArchiveEntry.toCpioTrailerBytes();
        final byte[] actualTrailer = outputStream.getBuffer();
        assertArrayEquals(expectedTrailer, actualTrailer);

        assertTrue(outputStream.isFinished());
        assertTrue(outputStream.isClosed());
    }

    @Test
    public void testClose() throws IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(out);

        final String testFileName = "testFile.txt";
        final String testFileContent = "Test content";
        final File testFile = new File(testFileName);

        final CpioArchiveEntry entry = outputStream.createArchiveEntry(testFile, testFileName);
        outputStream.putArchiveEntry(entry);

        final byte[] testData = testFileContent.getBytes();
        final int testDataLength = testData.length;
        outputStream.write(testData, 0, testDataLength);

        outputStream.close();

        final byte[] expectedTrailer = CpioArchiveEntry.toCpioTrailerBytes();
        final byte[] actualTrailer = outputStream.getBuffer();
        assertArrayEquals(expectedTrailer, actualTrailer);

        assertTrue(outputStream.isFinished());
        assertTrue(outputStream.isClosed());
    }

    @Test
    public void testNameMap() {
        final OutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream outputStream = new CpioArchiveOutputStream(out);

        final String testFileName1 = "testFile1.txt";
        final String testFileName2 = "testFile2.txt";

        final CpioArchiveEntry entry1 = outputStream.createArchiveEntry(new File(testFileName1), testFileName1);
        outputStream.putArchiveEntry(entry1);

        final CpioArchiveEntry entry2 = outputStream.createArchiveEntry(new File(testFileName2), testFileName2);
        outputStream.putArchiveEntry(entry2);

        @SuppressWarnings("unchecked")
        final Map<String, CpioArchiveEntry> nameMap = (Map<String, CpioArchiveEntry>) outputStream.getNames();

        assertEquals(testFileName1, nameMap.get(testFileName1).getName());
        assertEquals(testFileName2, nameMap.get(testFileName2).getName());
    }
}