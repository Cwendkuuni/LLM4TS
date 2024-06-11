package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.compress.utils.TarConstants;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testFinish() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out);
        tarOut.finish();
        byte[] expectedEOF = new byte[TarConstants.RECORD_SIZE];
        Arrays.fill(expectedEOF, (byte) 0);
        assertArrayEquals(expectedEOF, out.toByteArray(), 0, expectedEOF.length);
        byte[] expectedEOF2 = new byte[TarConstants.RECORD_SIZE];
        Arrays.fill(expectedEOF2, (byte) 0);
        assertArrayEquals(expectedEOF2, out.toByteArray(), expectedEOF.length,
                expectedEOF2.length);
    }

    @Test(expected = IOException.class)
    public void testFinishTwice() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out);
        tarOut.finish();
        tarOut.finish();
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out);
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out);
        File file = new File("testFile.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file, "testFile.txt");
        tarOut.putArchiveEntry(entry);
        assertFalse(tarOut.finished);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithFinishedStream() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out);
        tarOut.finish();
        File file = new File("testFile.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file, "testFile.txt");
        tarOut.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out);
        String longFileName = "a".repeat(TarConstants.NAMELEN + 1);
        File file = new File(longFileName);
        TarArchiveEntry entry = new TarArchiveEntry(file, longFileName);
        tarOut.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameAndLongFileModeError()
            throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out);
        TarArchiveEntry entry = new TarArchiveEntry(new File("testFile.txt"),
                "testFile.txt");
        tarOut.putArchiveEntry(entry);
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        File file = new File("longFileName".repeat(TarConstants.NAMELEN + 1));
        TarArchiveEntry longFileNameEntry = new TarArchiveEntry(file,
                "longFileName".repeat(TarConstants.NAMELEN + 1));
        tarOut.putArchiveEntry(longFileNameEntry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out);
        File file = new File("testFile.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file, "testFile.txt");
        tarOut.putArchiveEntry(entry);
        tarOut.closeArchiveEntry();
        assertNull(tarOut.currName);
        assertNull(tarOut.currSize);
        assertFalse(tarOut.haveUnclosedEntry);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithFinishedStream() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out);
        tarOut.finish();
        File file = new File("testFile.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file, "testFile.txt");
        tarOut.putArchiveEntry(entry);
        tarOut.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out);
        byte[] data = "testData".getBytes();
        tarOut.write(data, 0, data.length);
        byte[] expectedData = new byte[data.length];
        System.arraycopy(data, 0, expectedData, 0, data.length);
        assertArrayEquals(expectedData, out.toByteArray(), 0,
                expectedData.length);
    }

    @Test(expected = IOException.class)
    public void testWriteWithFinishedStream() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out);
        tarOut.finish();
        byte[] data = "testData".getBytes();
        tarOut.write(data, 0, data.length);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(out);
        File file = new File("testFile.txt");
        TarArchiveEntry entry = tarOut.createArchiveEntry(file, "testFile.txt");
        assertSame(file, entry.getFile());
        assertSame("testFile.txt", entry.getName());
    }
}