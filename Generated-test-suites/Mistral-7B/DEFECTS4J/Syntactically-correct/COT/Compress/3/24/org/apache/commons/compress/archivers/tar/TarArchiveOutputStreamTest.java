package org.apache.commons.compress.archivers.tar;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.test.CommonTestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * TarArchiveOutputStreamTest class to test all methods in TarArchiveOutputStream class.
 */
public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        Assert.assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryWithLongFileNameWhenLongFileModeIsError() throws IOException {
        String longFileName = CommonTestUtils.generateLongFileName();
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        ArchiveEntry archiveEntry = new TarArchiveEntry(new File("test.txt"), longFileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test
    public void testPutArchiveEntryWithLongFileNameWhenLongFileModeIsTruncate() throws IOException {
        String longFileName = CommonTestUtils.generateLongFileName();
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        ArchiveEntry archiveEntry = new TarArchiveEntry(new File("test.txt"), longFileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(inputStream);
        ArchiveEntry readArchiveEntry = tarArchiveInputStream.getNextEntry();
        Assert.assertEquals(longFileName, readArchiveEntry.getName());
    }

    @Test
    public void testPutArchiveEntryWithValidFileName() throws IOException {
        String validFileName = "validFileName.txt";
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        ArchiveEntry archiveEntry = new TarArchiveEntry(new File("test.txt"), validFileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(inputStream);
        ArchiveEntry readArchiveEntry = tarArchiveInputStream.getNextEntry();
        Assert.assertEquals(validFileName, readArchiveEntry.getName());
    }

    @Test
    public void testPutArchiveEntryAndCloseArchiveEntry() throws IOException {
        String validFileName = "validFileName.txt";
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        ArchiveEntry archiveEntry = new TarArchiveEntry(new File("test.txt"), validFileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.closeArchiveEntry();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(inputStream);
        ArchiveEntry readArchiveEntry = tarArchiveInputStream.getNextEntry();
        Assert.assertEquals(validFileName, readArchiveEntry.getName());
        tarArchiveInputStream.close();
    }

    @Test
    public void testWrite() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        byte[] data = "Test data".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(inputStream);
        byte[] readData = new byte[data.length];
        tarArchiveInputStream.read(readData, 0, data.length);
        Assert.assertArrayEquals(data, readData);
    }

    @Test
    public void testFinish() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.finish();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(inputStream);
        ArchiveEntry readArchiveEntry = tarArchiveInputStream.getNextEntry();
        Assert.assertNull(readArchiveEntry);
    }

    @Test
    public void testClose() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.close();
        Assert.assertTrue(tarArchiveOutputStream.isClosed());
    }

    @Test(expected = IOException.class)
    public void testWriteAfterClose() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.close();
        tarArchiveOutputStream.write(new byte[10], 0, 10);
    }
}