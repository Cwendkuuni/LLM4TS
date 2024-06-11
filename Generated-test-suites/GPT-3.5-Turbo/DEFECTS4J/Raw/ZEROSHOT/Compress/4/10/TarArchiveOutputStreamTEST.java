package org.apache.commons.compress.archivers.tar;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarArchiveOutputStream;
    private OutputStream outputStream;
    
    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarArchiveOutputStream.getLongFileMode());

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(TarBuffer.DEFAULT_BLKSIZE, tarArchiveOutputStream.getRecordSize());
    }

    @Test(expected = IOException.class)
    public void testFinishThrowsException() throws IOException {
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testFinish() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testCloseThrowsException() throws IOException {
        tarArchiveOutputStream.close();
    }

    @Test
    public void testClose() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("test.txt");
        ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(inputFile, "test.txt");
        assertEquals(inputFile, ((TarArchiveEntry) entry).getFile());
        assertEquals("test.txt", ((TarArchiveEntry) entry).getName());
    }

    // Add more test methods for other methods in TarArchiveOutputStream class

}