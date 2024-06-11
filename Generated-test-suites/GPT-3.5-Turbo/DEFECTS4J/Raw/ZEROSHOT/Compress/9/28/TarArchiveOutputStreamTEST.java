package org.apache.commons.compress.archivers.tar;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutputStream.longFileMode);
    }

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.finish();
        assertTrue(tarOutputStream.finished);
    }

    @Test(expected = IOException.class)
    public void testFinishAlreadyFinished() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.finished = true;
        tarOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.haveUnclosedEntry = true;
        tarOutputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.close();
        assertTrue(tarOutputStream.closed);
    }

    @Test(expected = IOException.class)
    public void testCloseAlreadyClosed() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.closed = true;
        tarOutputStream.close();
    }

    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        tarOutputStream.putArchiveEntry(entry);
        assertTrue(tarOutputStream.haveUnclosedEntry);
        assertEquals("testFile", tarOutputStream.currName);
        assertEquals(0, tarOutputStream.currBytes);
        assertFalse(entry.isDirectory());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryFinished() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.finished = true;
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("testFile"));
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryLongFileNameError() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.longFileMode = TarArchiveOutputStream.LONGFILE_ERROR;
        TarArchiveEntry entry = new TarArchiveEntry("thisIsAVeryVeryVeryVeryVeryVeryVeryLongFileName");
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.currBytes = 10;
        tarOutputStream.currSize = 10;
        tarOutputStream.currName = "testFile";
        tarOutputStream.haveUnclosedEntry = true;
        tarOutputStream.closeArchiveEntry();
        assertFalse(tarOutputStream.haveUnclosedEntry);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryFinished() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.finished = true;
        tarOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryNoCurrentEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryIncompleteWrite() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.currBytes = 5;
        tarOutputStream.currSize = 10;
        tarOutputStream.currName = "testFile";
        tarOutputStream.haveUnclosedEntry = true;
        tarOutputStream.assemLen = 5;
        tarOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        byte[] content = "Hello World".getBytes();
        tarOutputStream.write(content, 0, content.length);
        assertArrayEquals(content, outputStream.toByteArray());
    }

    @Test(expected = IOException.class)
    public void testWriteExceedsSize() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.currBytes = 5;
        tarOutputStream.currSize = 10;
        tarOutputStream.currName = "testFile";
        tarOutputStream.write("Hello World".getBytes(), 0, 11);
    }

}