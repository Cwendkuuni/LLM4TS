package org.apache.commons.compress.archivers.tar;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {
    private TarArchiveOutputStream tarOutputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
    }

    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutputStream.getLongFileMode());

        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());

        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOutputStream.getLongFileMode());
    }

    @Test
    public void testGetRecordSize() {
        Assert.assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        tarOutputStream.putArchiveEntry(entry);

        Assert.assertTrue(tarOutputStream.haveUnclosedEntry());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithUnclosedEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();

        Assert.assertFalse(tarOutputStream.haveUnclosedEntry());
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        tarOutputStream.putArchiveEntry(entry);

        byte[] data = "Test".getBytes();
        tarOutputStream.write(data);
        tarOutputStream.closeArchiveEntry();

        byte[] expectedData = outputStream.toByteArray();
        Assert.assertArrayEquals(expectedData, data);
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish();

        byte[] expectedData = outputStream.toByteArray();
        Assert.assertTrue(expectedData.length > 0);
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.close();

        Assert.assertTrue(tarOutputStream.isClosed());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File file = new File("testFile");
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(file, "testFile");

        Assert.assertEquals(TarArchiveEntry.class, entry.getClass());
        Assert.assertEquals(file, ((TarArchiveEntry) entry).getFile());
        Assert.assertEquals("testFile", ((TarArchiveEntry) entry).getName());
    }
}