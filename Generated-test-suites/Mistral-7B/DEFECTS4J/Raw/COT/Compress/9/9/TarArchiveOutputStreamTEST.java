package org.apache.commons.compress.archivers.tar;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.compress.utils.TarConstants;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        Assert.assertNull(tarArchiveOutputStream.getLongFileMode());
        Assert.assertFalse(tarArchiveOutputStream.isFinished());
        Assert.assertFalse(tarArchiveOutputStream.isClosed());
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeInvalidMode() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        tarArchiveOutputStream.setLongFileMode(3);
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        tarArchiveOutputStream.finish();
        Assert.assertTrue(tarArchiveOutputStream.isFinished());
    }

    @Test(expected = IOException.class)
    public void testFinishAlreadyFinished() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testFinishWithOpenEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        tarArchiveOutputStream.close();
        Assert.assertTrue(tarArchiveOutputStream.isClosed());
    }

    @Test(expected = IOException.class)
    public void testCloseAlreadyClosed() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        tarArchiveOutputStream.close();
        tarArchiveOutputStream.close();
    }

    @Test
    public void testGetRecordSize() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        Assert.assertEquals(TarConstants.RECORD_SIZE, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        Assert.assertEquals("test.txt", tarArchiveOutputStream.currName);
        Assert.assertFalse(tarArchiveOutputStream.isFinished());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "test_long_filename_" + new String(new char[TarConstants.NAMELEN]).replace("\0", "a");
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(longFileName);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullName() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(null);
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullArchiveEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        tarArchiveOutputStream.putArchiveEntry(null);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.closeArchiveEntry();
        Assert.assertEquals("", tarArchiveOutputStream.currName);
        Assert.assertFalse(tarArchiveOutputStream.haveUnclosedEntry);
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        byte[] testData = "test data".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testData);
        Assert.assertEquals(testData.length, tarArchiveOutputStream.currBytes);
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testWriteWithClosedEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.write(new byte[1]);
    }

    @Test(expected = IOException.class)
    public void testWriteWithLongFileName() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test_long_filename_" + new String(new char[TarConstants.NAMELEN]).replace("\0", "a"));
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        byte[] testData = "test data".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testData);
    }

    @Test
    public void testFinishAndClose() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        byte[] testData = "test data".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(testData);
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
        Assert.assertTrue(tarArchiveOutputStream.isFinished());
        Assert.assertTrue(tarArchiveOutputStream.isClosed());
    }

    @Test
    public void testWriteEOFRecord() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        tarArchiveOutputStream.finish();
        byte[] eofRecord = new byte[TarConstants.RECORD_SIZE];
        for (int i = 0; i < TarConstants.RECORD_SIZE; ++i) {
            eofRecord[i] = 0;
        }
        Assert.assertArrayEquals(eofRecord, tarArchiveOutputStream.recordBuf);
    }
}