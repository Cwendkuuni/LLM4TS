
package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {
    private TarArchiveOutputStream tarArchiveOutputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
        outputStream.close();
    }

    @Test
    public void testConstructor() {
        assertNotNull(tarArchiveOutputStream);
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
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        assertTrue(tarArchiveOutputStream.haveUnclosedEntry());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryFinished() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry();

        assertFalse(tarArchiveOutputStream.haveUnclosedEntry());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryNoEntry() throws IOException {
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryFinished() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        byte[] data = "This is a test file.".getBytes();
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        entry.setSize(data.length);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(data);
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] outputData = outputStream.toByteArray();
        assertEquals(data.length, outputData.length);
        for (int i = 0; i < data.length; i++) {
            assertEquals(data[i], outputData[i]);
        }
    }

    @Test(expected = IOException.class)
    public void testWriteTooManyBytes() throws IOException {
        byte[] data = "This is a test file.".getBytes();
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        entry.setSize(data.length - 1);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(data);
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testFinish() throws IOException {
        tarArchiveOutputStream.finish();
        assertTrue(tarArchiveOutputStream.isFinished());

        byte[] outputData = outputStream.toByteArray();
        assertEquals(1024, outputData.length);
        for (byte b : outputData) {
            assertEquals(0, b);
        }
    }

    @Test(expected = IOException.class)
    public void testFinishTwice() throws IOException {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        tarArchiveOutputStream.close();
        assertTrue(tarArchiveOutputStream.isClosed());
    }

    @Test(expected = IOException.class)
    public void testCloseTwice() throws IOException {
        tarArchiveOutputStream.close();
        tarArchiveOutputStream.close();
    }
}
