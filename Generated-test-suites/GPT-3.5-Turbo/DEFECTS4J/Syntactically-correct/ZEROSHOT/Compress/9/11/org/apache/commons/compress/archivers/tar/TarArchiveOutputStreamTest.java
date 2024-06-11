package org.apache.commons.compress.archivers.tar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.zip.Deflater;

import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    private File testFile;
    private OutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws IOException {
        testFile = new File("test.tar");
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
        testFile.delete();
    }

    @Test
    public void testConstructor() {
        Assert.assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testGetRecordSize() {
        int recordSize = tarArchiveOutputStream.getRecordSize();
        Assert.assertEquals(TarBuffer.DEFAULT_RCDSIZE, recordSize);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();
        Assert.assertFalse(tarArchiveOutputStream.haveUnclosedEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryFinished() throws IOException {
        tarArchiveOutputStream.finish();
        TarArchiveEntry entry = new TarArchiveEntry("test");
        tarArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();
        Assert.assertFalse(tarArchiveOutputStream.haveUnclosedEntry);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryNoEntry() throws IOException {
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryFinished() throws IOException {
        tarArchiveOutputStream.finish();
        TarArchiveEntry entry = new TarArchiveEntry("test");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] buf = "This is a test".getBytes();
        tarArchiveOutputStream.write(buf);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        byte[] result = ((ByteArrayOutputStream) outputStream).toByteArray();
        Assert.assertNotNull(result);
        Assert.assertEquals(26, result.length);
    }

    @Test(expected = IOException.class)
    public void testWriteExceedSize() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test");
        entry.setSize(10);
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] buf = "This is a test".getBytes();
        tarArchiveOutputStream.write(buf);
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testFinish() throws IOException {
        tarArchiveOutputStream.finish();
        Assert.assertTrue(tarArchiveOutputStream.finished);
    }

    @Test(expected = IOException.class)
    public void testFinishTwice() throws IOException {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        tarArchiveOutputStream.close();
        Assert.assertTrue(tarArchiveOutputStream.closed);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("test");
        ArchiveEntry result = tarArchiveOutputStream.createArchiveEntry(inputFile, "test");
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof TarArchiveEntry);
    }

    @Test(expected = IOException.class)
    public void testCreateArchiveEntryFinished() throws IOException {
        tarArchiveOutputStream.finish();
        File inputFile = new File("test");
        tarArchiveOutputStream.createArchiveEntry(inputFile, "test");
    }
}