package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.apache.commons.compress.utils.CountingInputStream;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.compress.utils.TarConstants;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testFinish() throws IOException {
        OutputStream os = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
        tarOs.finish();
        byte[] eof = new byte[TarConstants.TAR_BLOCKSIZE];
        for (int i = 0; i < eof.length; ++i) {
            eof[i] = 0;
        }
        tarOs.writeEOFRecord();
        assertArrayEquals(eof, tarOs.getBuffer().getBlock());
        tarOs.close();
    }

    @Test
    public void testClose() throws IOException {
        OutputStream os = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
        tarOs.finish();
        tarOs.close();
        byte[] eof = new byte[TarConstants.TAR_BLOCKSIZE * 2];
        for (int i = 0; i < eof.length; ++i) {
            eof[i] = 0;
        }
        byte[] data = new byte[10];
        tarOs = new TarArchiveOutputStream(os);
        tarOs.write(data, 0, data.length);
        tarOs.closeArchiveEntry();
        tarOs.finish();
        tarOs.close();
        byte[] header = TarArchiveEntry.getHeader(new ByteArrayInputStream(data), null);
        byte[] record = new byte[TarConstants.TAR_RECORDSIZE];
        System.arraycopy(header, 0, record, 0, TarConstants.TAR_RECORDHEADERSIZE);
        tarOs.getBuffer().readBlock(record, 0, TarConstants.TAR_RECORDSIZE);
        assertArrayEquals(header, record);
        assertArrayEquals(eof, tarOs.getBuffer().getBlock());
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        OutputStream os = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
        String longFileName = "a" + TarConstants.NAMELEN + "B";
        TarArchiveEntry entry = new TarArchiveEntry(new File(""), longFileName);
        byte[] nameBytes = ArchiveUtils.toAsciiBytes(longFileName);
        tarOs.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOs.putArchiveEntry(entry);
        byte[] header = TarArchiveEntry.getHeader(new ByteArrayInputStream(nameBytes), null);
        byte[] longLinkHeader = TarArchiveEntry.getHeader(new ByteArrayInputStream(nameBytes),
                TarConstants.GNU_LONGLINK);
        byte[] longLinkData = new byte[nameBytes.length + 1];
        System.arraycopy(nameBytes, 0, longLinkData, 0, nameBytes.length);
        longLinkData[nameBytes.length] = 0;
        tarOs.write(longLinkData);
        tarOs.closeArchiveEntry();
        tarOs.finish();
        tarOs.close();
        byte[] data = tarOs.getBuffer().getBlock();
        byte[] expected = new byte[data.length + TarConstants.TAR_BLOCKSIZE];
        System.arraycopy(data, 0, expected, 0, data.length);
        System.arraycopy(header, 0, expected, data.length, header.length);
        System.arraycopy(longLinkHeader, 0, expected, data.length + header.length, longLinkHeader.length);
        assertArrayEquals(expected, tarOs.getBuffer().getBlock());
    }

    @Test(expected = RuntimeException.class)
    public void testSetLongFileModeTruncate() throws IOException {
        OutputStream os = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
        String longFileName = "a" + TarConstants.NAMELEN + "B";
        TarArchiveEntry entry = new TarArchiveEntry(new File(""), longFileName);
        tarOs.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarOs.putArchiveEntry(entry);
        tarOs.finish();
        tarOs.close();
    }

    @Test(expected = RuntimeException.class)
    public void testSetLongFileModeError() throws IOException {
        OutputStream os = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
        String longFileName = "a" + TarConstants.NAMELEN + "B";
        TarArchiveEntry entry = new TarArchiveEntry(new File(""), longFileName);
        tarOs.putArchiveEntry(entry);
        tarOs.finish();
        tarOs.close();
        tarOs.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
    }

    @Test
    public void testWrite() throws IOException {
        OutputStream os = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
        byte[] data = new byte[10];
        tarOs.write(data, 0, data.length);
        tarOs.write(data, 0, data.length);
        tarOs.finish();
        tarOs.close();
        byte[] expected = new byte[2 * (data.length + TarConstants.TAR_BLOCKSIZE)];
        System.arraycopy(data, 0, expected, 0, data.length);
        for (int i = data.length; i < expected.length; ++i) {
            expected[i] = 0;
        }
        assertArrayEquals(expected, tarOs.getBuffer().getBlock());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        OutputStream os = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
        byte[] data = new byte[10];
        TarArchiveEntry entry = new TarArchiveEntry(new File(""), "test.txt");
        tarOs.putArchiveEntry(entry);
        tarOs.write(data, 0, data.length);
        tarOs.closeArchiveEntry();
        tarOs.finish();
        tarOs.close();
        byte[] expected = new byte[data.length + TarConstants.TAR_BLOCKSIZE];
        System.arraycopy(data, 0, expected, 0, data.length);
        assertArrayEquals(expected, tarOs.getBuffer().getBlock());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithFinishedArchive() throws IOException {
        OutputStream os = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
        tarOs.finish();
        tarOs.putArchiveEntry(new TarArchiveEntry(new File("")));
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithoutCloseArchiveEntry() throws IOException {
        OutputStream os = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
        TarArchiveEntry entry = new TarArchiveEntry(new File(""));
        tarOs.putArchiveEntry(entry);
        tarOs.finish();
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        OutputStream os = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
        byte[] data = new byte[10];
        TarArchiveEntry entry = new TarArchiveEntry(new File(""), "test.txt");
        tarOs.putArchiveEntry(entry);
        tarOs.write(data, 0, data.length);
        tarOs.closeArchiveEntry();
        tarOs.finish();
        tarOs.close();
        byte[] expected = new byte[data.length + TarConstants.TAR_BLOCKSIZE];
        System.arraycopy(data, 0, expected, 0, data.length);
        assertArrayEquals(expected, tarOs.getBuffer().getBlock());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithFinishedArchive() throws IOException {
        OutputStream os = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
        tarOs.finish();
        TarArchiveEntry entry = new TarArchiveEntry(new File(""));
        tarOs.putArchiveEntry(entry);
        tarOs.closeArchiveEntry();
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        OutputStream os = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
        File file = new File("test.txt");
        TarArchiveEntry entry = tarOs.createArchiveEntry(file, "test.txt");
        tarOs.putArchiveEntry(entry);
        tarOs.write(new byte[10], 0, 10);
        tarOs.closeArchiveEntry();
        tarOs.finish();
        tarOs.close();
        byte[] expected = new byte[10 + TarConstants.TAR_BLOCKSIZE];
        assertArrayEquals(expected, tarOs.getBuffer().getBlock());
    }

    @Test(expected = IOException.class)
    public void testCreateArchiveEntryWithoutFileName() throws IOException {
        OutputStream os = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
        TarArchiveEntry entry = tarOs.createArchiveEntry(null, "test.txt");
    }

    @Test(expected = ClassCastException.class)
    public void testPutArchiveEntryWithNonTarEntry() throws IOException {
        OutputStream os = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
        File file = new File("test.txt");
        ArchiveEntry nonTarEntry = new ArchiveEntry(file, "test.txt");
        tarOs.putArchiveEntry(nonTarEntry);
    }

    @Test
    public void testHasUnclosedEntry() throws IOException {
        OutputStream os = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
        TarArchiveEntry entry = new TarArchiveEntry(new File(""), "test.txt");
        tarOs.putArchiveEntry(entry);
        tarOs.hasUnclosedEntry();
        assertTrue(tarOs.haveUnclosedEntry);
        tarOs.closeArchiveEntry();
        assertFalse(tarOs.haveUnclosedEntry);
    }

    @Test
    public void testHasUnclosedEntryAfterFinish() throws IOException {
        OutputStream os = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
        TarArchiveEntry entry = new TarArchiveEntry(new File(""), "test.txt");
        tarOs.putArchiveEntry(entry);
        tarOs.finish();
        assertFalse(tarOs.haveUnclosedEntry);
    }

    @Test
    public void testIsFinished() throws IOException {
        OutputStream os = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
        TarArchiveEntry entry = new TarArchiveEntry(new File(""), "test.txt");
        tarOs.putArchiveEntry(entry);
        tarOs.finish();
        assertTrue(tarOs.isFinished());
        tarOs.write(new byte[10], 0, 10);
        assertTrue(tarOs.isFinished());
    }
}