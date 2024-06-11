package org.apache.commons.compress.archivers.tar;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.compress.utils.TarConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarOutputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @Test
    public void testConstructorWithOutputStream() throws IOException {
        tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(outputStream));
        Assert.assertNotNull(tarOutputStream);
    }

    @Test
    public void testConstructorWithOutputStreamBlockSize() throws IOException {
        tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(outputStream), TarBuffer.DEFAULT_BLKSIZE);
        Assert.assertNotNull(tarOutputStream);
    }

    @Test
    public void testConstructorWithOutputStreamBlockSizeRecordSize() throws IOException {
        tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(outputStream), TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
        Assert.assertNotNull(tarOutputStream);
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeWithInvalidMode() throws IOException {
        tarOutputStream.setLongFileMode(-1);
    }

    @Test
    public void testFinish() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish();
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry tarEntry = (TarArchiveEntry) new TarArchiveInputStream(inputStream).getNextEntry();
        Assert.assertEquals("test.txt", tarEntry.getName());
    }

    @Test(expected = IllegalStateException.class)
    public void testFinishTwice() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish();
        tarOutputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.close();
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry tarEntry = (TarArchiveEntry) new TarArchiveInputStream(inputStream).getNextEntry();
        Assert.assertEquals("test.txt", tarEntry.getName());
    }

    @Test(expected = IllegalStateException.class)
    public void testCloseTwice() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.close();
        tarOutputStream.close();
    }

    @Test
    public void testGetRecordSize() throws IOException {
        Assert.assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write("Test Content".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.closeArchiveEntry();
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry tarEntry = (TarArchiveEntry) new TarArchiveInputStream(inputStream).getNextEntry();
        Assert.assertEquals("test.txt", tarEntry.getName());
        Assert.assertEquals(12, tarEntry.getSize());
        Assert.assertEquals("Test Content".getBytes(StandardCharsets.UTF_8).length, tarEntry.getSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithNullEntry() throws IOException {
        tarOutputStream.putArchiveEntry(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testPutArchiveEntryWithoutCloseArchiveEntry() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.close();
    }

    @Test
    public void testWriteToArchiveEntry() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        byte[] data = "Test Content".getBytes(StandardCharsets.UTF_8);
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.closeArchiveEntry();
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry tarEntry = (TarArchiveEntry) new TarArchiveInputStream(inputStream).getNextEntry();
        Assert.assertEquals("test.txt", tarEntry.getName());
        Assert.assertEquals(12, tarEntry.getSize());
        InputStream inputDataStream = tarEntry.getInputStream();
        byte[] readData = new byte[data.length];
        int read = inputDataStream.read(readData, 0, data.length);
        Assert.assertEquals(data.length, read);
        Assert.assertArrayEquals(data, readData);
    }

    @Test
    public void testLongFileName() throws IOException {
        String longFileName = "a/long/path/to/file.txt";
        ArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write("Test Content".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.closeArchiveEntry();
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry tarEntry = (TarArchiveEntry) new TarArchiveInputStream(inputStream).getNextEntry();
        Assert.assertEquals(longFileName, tarEntry.getName());
    }

    @Test(expected = RuntimeException.class)
    public void testLongFileNameWithLongFileModeError() throws IOException {
        String longFileName = "a/long/path/to/file.txt";
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        ArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testLongFileNameWithLongFileModeTruncate() throws IOException {
        String longFileName = "a/long/path/to/file.txt";
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        ArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write("Test Content".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.closeArchiveEntry();
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry tarEntry = (TarArchiveEntry) new TarArchiveInputStream(inputStream).getNextEntry();
        Assert.assertEquals("a/long/path/to/file.txt", tarEntry.getName().substring(0, TarConstants.NAMELEN));
    }

    @Test
    public void testLongFileNameWithLongFileModeGNU() throws IOException {
        String longFileName = "a/long/path/to/file.txt";
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        ArchiveEntry entry = new TarArchiveEntry(longFileName, TarConstants.GNU_LONGLINK, TarConstants.LF_GNUTYPE_LONGNAME);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write("Test Content".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.closeArchiveEntry();
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry tarEntry = (TarArchiveEntry) new TarArchiveInputStream(inputStream).getNextEntry();
        Assert.assertEquals(longFileName, tarEntry.getName());
    }
}