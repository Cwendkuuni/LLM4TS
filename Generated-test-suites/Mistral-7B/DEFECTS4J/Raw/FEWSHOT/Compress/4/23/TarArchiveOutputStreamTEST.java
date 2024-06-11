package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(out);
        assertNull(tarStream.getBuffer());
        assertEquals(0, tarStream.getRecordSize());
        assertTrue(tarStream instanceof ArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        int[] longFileModes = { TarArchiveOutputStream.LONGFILE_ERROR,
                TarArchiveOutputStream.LONGFILE_TRUNCATE,
                TarArchiveOutputStream.LONGFILE_GNU };
        for (int longFileMode : longFileModes) {
            TarArchiveOutputStream tarStream = new TarArchiveOutputStream(
                    new ByteArrayOutputStream());
            tarStream.setLongFileMode(longFileMode);
            assertEquals(longFileMode, tarStream.getLongFileMode());
        }
    }

    @Test
    public void testPutArchiveEntryWithValidFileName() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(out);
        String fileName = "testFile.txt";
        TarArchiveEntry entry = new TarArchiveEntry(fileName);
        tarStream.putArchiveEntry(entry);
        byte[] expectedHeader = TarConstants.MAGIC_NUMBER;
        assertArrayEquals(expectedHeader, tarStream.getRecordBuf());
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(out);
        String fileName = "testFileWithLongName.txt";
        TarArchiveEntry entry = new TarArchiveEntry(fileName);
        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarStream.putArchiveEntry(entry);
        byte[] expectedHeader = TarConstants.MAGIC_NUMBER;
        assertArrayEquals(expectedHeader, tarStream.getRecordBuf());
        byte[] longFileNameBytes = fileName.getBytes(StandardCharsets.UTF_8);
        int size = longFileNameBytes.length + 1;
        byte[] expectedLongFileNameBytes = new byte[size];
        System.arraycopy(longFileNameBytes, 0, expectedLongFileNameBytes, 0, size);
        assertArrayEquals(expectedLongFileNameBytes, tarStream.getRecordBuf(), size + 1);
    }

    @Test
    public void testPutArchiveEntryWithNullArchiveEntry() {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertThrows(ClassCastException.class, () -> tarStream.putArchiveEntry(null));
    }

    @Test
    public void testFinish() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(out);
        tarStream.finish();
        byte[] expectedEOFHeader = new byte[TarConstants.TAR_RECORD_SIZE];
        Arrays.fill(expectedEOFHeader, (byte) 0);
        assertArrayEquals(expectedEOFHeader, tarStream.getRecordBuf());
        byte[] archiveContent = out.toByteArray();
        int eofIndex = archiveContent.length - TarConstants.TAR_RECORD_SIZE;
        assertArrayEquals(expectedEOFHeader, Arrays.copyOfRange(archiveContent, eofIndex, eofIndex + TarConstants.TAR_RECORD_SIZE));
    }

    @Test
    public void testClose() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(out);
        tarStream.close();
        byte[] expectedEOFHeader = new byte[TarConstants.TAR_RECORD_SIZE];
        Arrays.fill(expectedEOFHeader, (byte) 0);
        assertArrayEquals(expectedEOFHeader, tarStream.getRecordBuf());
        byte[] archiveContent = out.toByteArray();
        int eofIndex = archiveContent.length - TarConstants.TAR_RECORD_SIZE;
        assertArrayEquals(expectedEOFHeader, Arrays.copyOfRange(archiveContent, eofIndex, eofIndex + TarConstants.TAR_RECORD_SIZE));
    }

    @Test
    public void testCloseBeforeFinish() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(out);
        tarStream.finish();
        tarStream.close();
        byte[] expectedEOFHeader = new byte[TarConstants.TAR_RECORD_SIZE];
        Arrays.fill(expectedEOFHeader, (byte) 0);
        assertArrayEquals(expectedEOFHeader, tarStream.getRecordBuf());
    }

    @Test
    public void testWrite() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(out);
        byte[] wBuf = "test data".getBytes();
        int wOffset = 0;
        int numToWrite = wBuf.length;
        tarStream.write(wBuf, wOffset, numToWrite);
        byte[] expectedContent = Arrays.copyOfRange(wBuf, 0, numToWrite);
        byte[] archiveContent = out.toByteArray();
        assertArrayEquals(expectedContent, Arrays.copyOfRange(archiveContent, 0, numToWrite));
    }

    @Test
    public void testWriteWithBufferedData() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(out);
        byte[] wBuf = "test data".getBytes();
        int wOffset = 0;
        int numToWrite = wBuf.length;
        int assemLen = 0;
        while (numToWrite > 0) {
            int remainingToWrite = numToWrite;
            if (remainingToWrite > TarConstants.TAR_RECORD_SIZE) {
                remainingToWrite = TarConstants.TAR_RECORD_SIZE;
            }
            tarStream.write(wBuf, wOffset, remainingToWrite);
            numToWrite -= remainingToWrite;
            wOffset += remainingToWrite;
            assemLen += remainingToWrite;
        }
        tarStream.close();
        byte[] archiveContent = out.toByteArray();
        byte[] expectedContent = Arrays.copyOfRange(wBuf, 0, numToWrite);
        assertArrayEquals(expectedContent, Arrays.copyOfRange(archiveContent, 0, numToWrite));
    }

    @Test
    public void testWriteWithNotEnoughSpace() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(out);
        byte[] wBuf = "test data".getBytes();
        int wOffset = 0;
        int numToWrite = wBuf.length;
        int assemLen = 0;
        while (numToWrite > 0) {
            if (numToWrite <= TarConstants.TAR_RECORD_SIZE) {
                tarStream.write(wBuf, wOffset, numToWrite);
                break;
            }
            tarStream.write(wBuf, wOffset, TarConstants.TAR_RECORD_SIZE);
            numToWrite -= TarConstants.TAR_RECORD_SIZE;
            wOffset += TarConstants.TAR_RECORD_SIZE;
            assemLen += TarConstants.TAR_RECORD_SIZE;
        }
        byte[] archiveContent = out.toByteArray();
        int eofIndex = archiveContent.length - TarConstants.TAR_RECORD_SIZE;
        byte[] expectedEOFHeader = new byte[TarConstants.TAR_RECORD_SIZE];
        Arrays.fill(expectedEOFHeader, (byte) 0);
        assertArrayEquals(expectedEOFHeader, Arrays.copyOfRange(archiveContent, eofIndex, eofIndex + TarConstants.TAR_RECORD_SIZE));
        byte[] expectedContent = Arrays.copyOfRange(wBuf, 0, numToWrite);
        byte[] actualContent = Arrays.copyOfRange(archiveContent, 0, eofIndex + numToWrite);
        assertArrayEquals(expectedContent, actualContent);
    }

    @Test
    public void testCloseArchiveEntryBeforeFinish() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(out);
        TarArchiveEntry entry = new TarArchiveEntry("testEntry.txt");
        tarStream.putArchiveEntry(entry);
        tarStream.closeArchiveEntry();
        tarStream.finish();
        byte[] archiveContent = out.toByteArray();
        int eofIndex = archiveContent.length - TarConstants.TAR_RECORD_SIZE;
        byte[] expectedEOFHeader = new byte[TarConstants.TAR_RECORD_SIZE];
        Arrays.fill(expectedEOFHeader, (byte) 0);
        assertArrayEquals(expectedEOFHeader, Arrays.copyOfRange(archiveContent, eofIndex, eofIndex + TarConstants.TAR_RECORD_SIZE));
        int entryIndex = archiveContent.length - TarConstants.TAR_RECORD_SIZE;
        byte[] expectedEntryHeader = entry.getHeader(TarConstants.STANDARD_GNU);
        assertArrayEquals(expectedEntryHeader, Arrays.copyOfRange(archiveContent, entryIndex, entryIndex + TarConstants.TAR_RECORD_SIZE));
    }

    @Test
    public void testCloseArchiveEntryAfterFinish() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(out);
        TarArchiveEntry entry = new TarArchiveEntry("testEntry.txt");
        tarStream.putArchiveEntry(entry);
        tarStream.finish();
        tarStream.closeArchiveEntry();
        byte[] archiveContent = out.toByteArray();
        int eofIndex = archiveContent.length - TarConstants.TAR_RECORD_SIZE;
        byte[] expectedEOFHeader = new byte[TarConstants.TAR_RECORD_SIZE];
        Arrays.fill(expectedEOFHeader, (byte) 0);
        assertArrayEquals(expectedEOFHeader, Arrays.copyOfRange(archiveContent, eofIndex, eofIndex + TarConstants.TAR_RECORD_SIZE));
        int entryIndex = archiveContent.length - TarConstants.TAR_RECORD_SIZE;
        byte[] expectedEntryHeader = entry.getHeader(TarConstants.STANDARD_GNU);
        assertArrayEquals(expectedEntryHeader, Arrays.copyOfRange(archiveContent, entryIndex, entryIndex + TarConstants.TAR_RECORD_SIZE));
    }

    @Test
    public void testFinishWithoutClosingArchiveEntry() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(out);
        TarArchiveEntry entry = new TarArchiveEntry("testEntry.txt");
        tarStream.putArchiveEntry(entry);
        tarStream.finish();
        byte[] archiveContent = out.toByteArray();
        int eofIndex = archiveContent.length - TarConstants.TAR_RECORD_SIZE;
        byte[] expectedEOFHeader = new byte[TarConstants.TAR_RECORD_SIZE];
        Arrays.fill(expectedEOFHeader, (byte) 0);
        assertArrayEquals(expectedEOFHeader, Arrays.copyOfRange(archiveContent, eofIndex, eofIndex + TarConstants.TAR_RECORD_SIZE));
        int entryIndex = archiveContent.length - TarConstants.TAR_RECORD_SIZE;
        byte[] expectedEntryHeader = entry.getHeader(TarConstants.STANDARD_GNU);
        assertArrayEquals(expectedEntryHeader, Arrays.copyOfRange(archiveContent, entryIndex, entryIndex + TarConstants.TAR_RECORD_SIZE));
        assertFalse(tarStream.haveUnclosedEntry);
    }
}