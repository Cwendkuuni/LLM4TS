package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.utils.ByteStreams;
import org.apache.commons.compress.utils.CpioUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testCreateOutputStreamWithNewFormat() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        cpioStream.finish();
        byte[] expectedMagicNew = CpioConstants.MAGIC_NEW.getBytes();
        byte[] actualMagicNew = ((ByteArrayOutputStream) out).toByteArray();
        assertArrayEquals(expectedMagicNew, actualMagicNew);
        cpioStream.close();
    }

    @Test
    public void testCreateOutputStreamWithDefaultFormat() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out);
        assertEquals(CpioConstants.FORMAT_NEW, cpioStream.getFormat());
        cpioStream.finish();
        byte[] expectedMagicNew = CpioConstants.MAGIC_NEW.getBytes();
        byte[] actualMagicNew = ((ByteArrayOutputStream) out).toByteArray();
        assertArrayEquals(expectedMagicNew, actualMagicNew);
        cpioStream.close();
    }

    @Test
    public void testWriteSameEntryTwice() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out);
        String filename = "testfile.txt";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(filename);
        cpioStream.putNextEntry(entry);
        byte[] testData = "Test data".getBytes();
        cpioStream.write(testData, 0, testData.length);
        cpioStream.closeArchiveEntry();
        entry.setName(filename + "_v2.txt");
        cpioStream.putNextEntry(entry);
        cpioStream.write(testData, 0, testData.length);
        cpioStream.closeArchiveEntry();
        byte[] expectedData = new byte[testData.length * 2];
        System.arraycopy(testData, 0, expectedData, 0, testData.length);
        System.arraycopy(testData, 0, expectedData, testData.length, testData.length);
        byte[] actualData = ((ByteArrayOutputStream) out).toByteArray();
        assertArrayEquals(expectedData, actualData);
        cpioStream.close();
    }

    @Test
    public void testWriteDifferentEntryFormats() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out);
        String oldAsciiFileName = "oldascii_file.txt";
        CpioArchiveEntry oldAsciiEntry = new CpioArchiveEntry();
        oldAsciiEntry.setName(oldAsciiFileName);
        oldAsciiEntry.setFormat(CpioConstants.FORMAT_OLD_ASCII);
        cpioStream.putNextEntry(oldAsciiEntry);
        byte[] oldAsciiTestData = "Old Ascii Data".getBytes();
        cpioStream.write(oldAsciiTestData, 0, oldAsciiTestData.length);
        cpioStream.closeArchiveEntry();

        String newFormatFileName = "newformat_file.txt";
        CpioArchiveEntry newFormatEntry = new CpioArchiveEntry();
        newFormatEntry.setName(newFormatFileName);
        newFormatEntry.setFormat(CpioConstants.FORMAT_NEW);
        cpioStream.putNextEntry(newFormatEntry);
        byte[] newFormatTestData = "New Format Data".getBytes();
        cpioStream.write(newFormatTestData, 0, newFormatTestData.length);
        cpioStream.closeArchiveEntry();

        byte[] expectedData = new byte[oldAsciiTestData.length + newFormatTestData.length];
        System.arraycopy(oldAsciiTestData, 0, expectedData, 0, oldAsciiTestData.length);
        System.arraycopy(newFormatTestData, 0, expectedData, oldAsciiTestData.length, newFormatTestData.length);
        byte[] actualData = ((ByteArrayOutputStream) out).toByteArray();
        assertArrayEquals(expectedData, actualData);
        cpioStream.close();
    }

    @Test
    public void testWriteSameEntryWithDifferentFormats() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out);
        String filename = "testfile.txt";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(filename);
        cpioStream.putNextEntry(entry);
        byte[] testData = "Test data".getBytes();
        cpioStream.write(testData, 0, testData.length);
        cpioStream.closeArchiveEntry();
        entry.setFormat(CpioConstants.FORMAT_OLD_ASCII);
        cpioStream.putNextEntry(entry);
        cpioStream.write(testData, 0, testData.length);
        cpioStream.closeArchiveEntry();
        byte[] expectedData = new byte[testData.length * 2];
        System.arraycopy(testData, 0, expectedData, 0, testData.length);
        System.arraycopy(testData, 0, expectedData, testData.length, testData.length);
        byte[] actualData = ((ByteArrayOutputStream) out).toByteArray();
        assertArrayEquals(expectedData, actualData);
        cpioStream.close();
    }

    @Test
    public void testWriteWithInvalidEntryFormat() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out);
        String invalidFileName = "testfile.txt";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(invalidFileName);
        entry.setFormat(-1); // invalid format
        cpioStream.putNextEntry(entry);
        byte[] testData = "Test data".getBytes();
        assertThrows(IllegalArgumentException.class, () -> cpioStream.write(testData, 0, testData.length));
        cpioStream.closeArchiveEntry();
        cpioStream.close();
    }

    @Test
    public void testWriteWithDupplicateName() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out);
        String duplicateFileName = "testfile.txt";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(duplicateFileName);
        cpioStream.putNextEntry(entry);
        byte[] testData = "Test data".getBytes();
        cpioStream.write(testData, 0, testData.length);
        cpioStream.closeArchiveEntry();
        assertThrows(IllegalStateException.class, () -> cpioStream.putNextEntry(entry));
        cpioStream.close();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out);
        String name = "testfile.txt";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(name);
        cpioStream.putArchiveEntry(entry);
        byte[] testData = "Test data".getBytes();
        cpioStream.write(testData, 0, testData.length);
        cpioStream.closeArchiveEntry();
        byte[] expectedData = new byte[testData.length];
        byte[] actualData = ((ByteArrayOutputStream) out).toByteArray();
        assertArrayEquals(expectedData, actualData);
        cpioStream.close();
    }

    @Test
    public void testPutNextEntryWithNullEntry() {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out);
        assertThrows(NullPointerException.class, () -> cpioStream.putNextEntry(null));
        cpioStream.close();
    }

    @Test
    public void testPutNextEntryWithNullName() {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(null);
        assertThrows(NullPointerException.class, () -> cpioStream.putNextEntry(entry));
        cpioStream.close();
    }

    @Test
    public void testPutNextEntryWithEmptyName() {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("");
        assertThrows(IllegalArgumentException.class, () -> cpioStream.putNextEntry(entry));
        cpioStream.close();
    }

    @Test
    public void testReadEntriesFromStream() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioStream = new CpioArchiveOutputStream(out);
        String name1 = "file1.txt";
        String name2 = "file2.txt";
        String name3 = "file3.txt";
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName(name1);
        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName(name2);
        CpioArchiveEntry entry3 = new CpioArchiveEntry();
        entry3.setName(name3);

        cpioStream.putNextEntry(entry1);
        byte[] testData1 = "Test data 1".getBytes();
        cpioStream.write(testData1, 0, testData1.length);
        cpioStream.closeArchiveEntry();

        cpioStream.putNextEntry(entry2);
        byte[] testData2 = "Test data 2".getBytes();
        cpioStream.write(testData2, 0, testData2.length);
        cpioStream.closeArchiveEntry();

        cpioStream.putNextEntry(entry3);
        byte[] testData3 = "Test data 3".getBytes();
        cpioStream.write(testData3, 0, testData3.length);
        cpioStream.closeArchiveEntry();

        cpioStream.finish();

        InputStream inputStream = new ByteArrayInputStream(((ByteArrayOutputStream) out).toByteArray());
        CpioArchiveInputStream cpioInput = new CpioArchiveInputStream(inputStream);

        assertEquals(name1, cpioInput.getNextEntry().getName());
        byte[] expectedData1 = testData1;
        byte[] actualData1 = new byte[testData1.length];
        assertEquals(expectedData1.length, cpioInput.read(actualData1, 0, expectedData1.length));
        assertArrayEquals(expectedData1, actualData1);

        assertEquals(name2, cpioInput.getNextEntry().getName());
        byte[] expectedData2 = testData2;
        byte[] actualData2 = new byte[testData2.length];
        assertEquals(expectedData2.length, cpioInput.read(actualData2, 0, expectedData2.length));
        assertArrayEquals(expectedData2, actualData2);

        assertEquals(name3, cpioInput.getNextEntry().getName());
        byte[] expectedData3 = testData3;
        byte[] actualData3 = new byte[testData3.length];
        assertEquals(expectedData3.length, cpioInput.read(actualData3, 0, expectedData3.length));
        assertArrayEquals(expectedData3, actualData3);

        assertFalse(cpioInput.hasNextEntry());
        cpioInput.close();

        cpioStream.close();
    }

}