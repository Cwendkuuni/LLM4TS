package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.CpioUtil;
import org.apache.commons.compress.archivers.ArchiveOutputStream;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testNewCpioArchiveOutputStream() {
        // GIVEN a new CpioArchiveOutputStream
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(
                out, CpioConstants.FORMAT_NEW);

        // THEN it should have a default format of new
        assertEquals(CpioConstants.FORMAT_NEW, cpioArchiveOutputStream.getFormat());

        // THEN it should not be finished
        assertFalse(cpioArchiveOutputStream.isFinished());

        // THEN it should not be closed
        assertFalse(cpioArchiveOutputStream.isClosed());
    }

    @Test
    public void testNewCpioArchiveOutputStreamWithFormat() {
        // GIVEN a new CpioArchiveOutputStream with a specified format
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(
                out, CpioConstants.FORMAT_OLD_BINARY);

        // THEN it should have the specified format
        assertEquals(CpioConstants.FORMAT_OLD_BINARY, cpioArchiveOutputStream.getFormat());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewCpioArchiveOutputStreamWithInvalidFormat() {
        // GIVEN a new CpioArchiveOutputStream with an invalid format
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(
                out, (short) 10);
    }

    @Test
    public void testPutNextEntryWithValidEntry() throws IOException {
        // GIVEN a CpioArchiveOutputStream and a valid CpioArchiveEntry
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(
                out, CpioConstants.FORMAT_NEW);
        String name = "testfile.txt";
        String contents = "12345";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(name);
        entry.setFileSize(contents.length());
        cpioArchiveOutputStream.putNextEntry(entry);

        // WHEN writing the contents of the file
        byte[] testContents = contents.getBytes(StandardCharsets.UTF_8);
        cpioArchiveOutputStream.write(testContents, 0, testContents.length);

        // THEN the entry should be closed
        assertFalse(cpioArchiveOutputStream.isWritingEntry());

        // THEN the next entry should not be written until finish() is called
        assertSame(entry, cpioArchiveOutputStream.getCpioEntry());

        // THEN the written byte count should be equal to the file size
        assertEquals(testContents.length, cpioArchiveOutputStream.getNextEntrySize());

        // THEN the entry name should be in the names map
        Map<String, CpioArchiveEntry> names = cpioArchiveOutputStream.getNames();
        assertSame(entry, names.get(name));

        // THEN the CRC should be equal to the entry's checksum
        assertEquals(entry.getChksum(), cpioArchiveOutputStream.getCrc());

        // THEN the written byte count should be equal to the size written plus the header size
        assertEquals(testContents.length + cpioArchiveOutputStream.getHeaderSize(), cpioArchiveOutputStream.getNextEntrySize());
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithNullOutputStream() {
        // GIVEN a CpioArchiveOutputStream with a null output stream
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(
                null, CpioConstants.FORMAT_NEW);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithNullEntry() {
        // GIVEN a CpioArchiveOutputStream
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(
                out, CpioConstants.FORMAT_NEW);
        // AND a null CpioArchiveEntry
        ArchiveEntry entry = null;
        // WHEN trying to put the null entry
        cpioArchiveOutputStream.putNextEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithDuplicateEntry() throws IOException {
        // GIVEN a CpioArchiveOutputStream and two identical entries
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(
                out, CpioConstants.FORMAT_NEW);
        String name = "testfile.txt";
        String contents = "12345";
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName(name);
        entry1.setFileSize(contents.length());
        cpioArchiveOutputStream.putNextEntry(entry1);
        byte[] testContents = contents.getBytes(StandardCharsets.UTF_8);
        cpioArchiveOutputStream.write(testContents, 0, testContents.length);
        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName(name);
        entry2.setFileSize(testContents.length);
        // WHEN trying to put the second entry
        cpioArchiveOutputStream.putNextEntry(entry2);
    }

    @Test
    public void testPutNextEntryWithEntryWithFormat() throws IOException {
        // GIVEN a CpioArchiveOutputStream and a CpioArchiveEntry with a format
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(
                out, CpioConstants.FORMAT_NEW);
        String name = "testfile.txt";
        String contents = "12345";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(name);
        entry.setFileSize(contents.length());
        entry.setFormat(CpioConstants.FORMAT_OLD_ASCII);
        cpioArchiveOutputStream.putNextEntry(entry);

        // THEN the format of the written entry should be equal to the specified format
        assertEquals(CpioConstants.FORMAT_OLD_ASCII, cpioArchiveOutputStream.getEntryFormat());
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithInvalidEntryFormat() throws IOException {
        // GIVEN a CpioArchiveOutputStream and a CpioArchiveEntry with an invalid format
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(
                out, CpioConstants.FORMAT_NEW);
        String name = "testfile.txt";
        String contents = "12345";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(name);
        entry.setFileSize(contents.length());
        entry.setFormat((short) 10);
        cpioArchiveOutputStream.putNextEntry(entry);
    }

    @Test
    public void testWrite() throws IOException {
        // GIVEN a CpioArchiveOutputStream and a writeable byte array
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(
                out, CpioConstants.FORMAT_NEW);
        byte[] testBytes = new byte[] { 0x01, 0x02, 0x03 };
        // AND some data to write
        int offset = 0;
        int length = testBytes.length;

        // WHEN writing the data
        cpioArchiveOutputStream.write(testBytes, offset, length);

        // THEN the written data should be equal to the data that was written
        byte[] writtenBytes = ((ByteArrayOutputStream) out).toByteArray();
        assertArrayEquals(testBytes, writtenBytes, 0, testBytes.length);
    }

    @Test(expected = IOException.class)
    public void testWriteWithNullOutputStream() {
        // GIVEN a CpioArchiveOutputStream with a null output stream
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(
                null, CpioConstants.FORMAT_NEW);
        byte[] testBytes = new byte[] { 0x01, 0x02, 0x03 };
        int offset = 0;
        int length = testBytes.length;
        // AND some data to write

        // WHEN trying to write the data
        cpioArchiveOutputStream.write(testBytes, offset, length);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testWriteWithNegativeOffset() {
        // GIVEN a CpioArchiveOutputStream
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(
                out, CpioConstants.FORMAT_NEW);
        byte[] testBytes = new byte[] { 0x01, 0x02, 0x03 };
        int offset = -1;
        int length = testBytes.length;

        // WHEN trying to write the data with a negative offset
        cpioArchiveOutputStream.write(testBytes, offset, length);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testWriteWithNegativeLength() {
        // GIVEN a CpioArchiveOutputStream
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(
                out, CpioConstants.FORMAT_NEW);
        byte[] testBytes = new byte[] { 0x01, 0x02, 0x03 };
        int offset = 0;
        int length = -1;

        // WHEN trying to write the data with a negative length
        cpioArchiveOutputStream.write(testBytes, offset, length);
    }

    @Test(expected = IOException.class)
    public void testWriteWithUnwritableByteArray() {
        // GIVEN a CpioArchiveOutputStream
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(
                out, CpioConstants.FORMAT_NEW);
        byte[] testBytes = null;
        int offset = 0;
        int length = testBytes.length;

        // WHEN trying to write the data to a null byte array
        cpioArchiveOutputStream.write(testBytes, offset, length);
    }

    @Test
    public void testFinish() throws IOException {
        // GIVEN a CpioArchiveOutputStream
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(
                out, CpioConstants.FORMAT_NEW);
        String name = "testfile.txt";
        String contents = "12345";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(name);
        entry.setFileSize(contents.length());
        cpioArchiveOutputStream.putNextEntry(entry);
        byte[] testContents = contents.getBytes(StandardCharsets.UTF_8);
        cpioArchiveOutputStream.write(testContents, 0, testContents.length);
        // AND some data to write

        // WHEN finishing the archive
        cpioArchiveOutputStream.finish();

        // THEN the archive should be finished
        assertTrue(cpioArchiveOutputStream.isFinished());

        // THEN the next entry should not be written
        assertNull(cpioArchiveOutputStream.getCpioEntry());

        // THEN the CRC should be reset to 0
        assertEquals(0, cpioArchiveOutputStream.getCrc());

        // THEN the written byte count should be reset to 0
        assertEquals(0, cpioArchiveOutputStream.getNextEntrySize());

        // THEN the output stream should be flushed
        assertEquals(cpioArchiveOutputStream.size(), ((ByteArrayOutputStream) out).size());

        // THEN the next entry should not be written until putNextEntry() is called again
        assertNull(cpioArchiveOutputStream.getCpioEntry());
    }

    @Test
    public void testClose() throws IOException {
        // GIVEN a CpioArchiveOutputStream
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(
                out, CpioConstants.FORMAT_NEW);

        // WHEN closing the archive
        cpioArchiveOutputStream.close();

        // THEN the archive should be closed
        assertTrue(cpioArchiveOutputStream.isClosed());

        // THEN the output stream should be closed
        assertFalse(((ByteArrayOutputStream) out).canWrite());

        // THEN any further write attempts should throw an IOException
        try {
            cpioArchiveOutputStream.write(new byte[] { 0x01 }, 0, 1);
            fail();
        } catch (IOException e) {
            // expected
        }

        // THEN any further putNextEntry attempts should throw an IOException
        try {
            cpioArchiveOutputStream.putNextEntry(new CpioArchiveEntry());
            fail();
        } catch (IOException e) {
            // expected
        }

        // THEN any further finish attempts should throw an IOException
        try {
            cpioArchiveOutputStream.finish();
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        // GIVEN a CpioArchiveOutputStream
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(
                out, CpioConstants.FORMAT_NEW);
        String name = "testfile.txt";
        String contents = "12345";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(name);
        entry.setFileSize(contents.length());

        // WHEN putting an ArchiveEntry instead of a CpioArchiveEntry
        ArchiveEntry archiveEntry = new CpioArchiveEntry();
        cpioArchiveOutputStream.putArchiveEntry(archiveEntry);

        // THEN an IOException should be thrown
        fail();
    }
}