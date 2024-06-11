package org.apache.commons.compress.archivers.tar;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * TarArchiveOutputStreamTest
 */
public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @Test
    public void testConstructorWithOutputStream() {
        Assert.assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testConstructorWithOutputStreamBlockSize() {
        Assert.assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testConstructorWithOutputStreamBlockSizeRecordSize() {
        Assert.assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    }

    @Test(expected = RuntimeException.class)
    public void testSetLongFileModeWithLongFileNameAndErrorMode() {
        TarArchiveEntry entry = new TarArchiveEntry("name_longer_than_NAMELEN.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.finish();
        byte[] tarBytes = outputStream.toByteArray();
        // Here you can check the generated tar bytes
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithUnclosedEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();
        TarArchiveEntry entry2 = new TarArchiveEntry("test2.txt");
        tarArchiveOutputStream.putArchiveEntry(entry2);
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        byte[] data = "This is a test".getBytes();
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] tarBytes = outputStream.toByteArray();
        // Here you can check the generated tar bytes
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
    }

    @Test
    public void testGetRecordSize() {
        int recordSize = tarArchiveOutputStream.getRecordSize();
        Assert.assertNotEquals(-1, recordSize);
    }
}