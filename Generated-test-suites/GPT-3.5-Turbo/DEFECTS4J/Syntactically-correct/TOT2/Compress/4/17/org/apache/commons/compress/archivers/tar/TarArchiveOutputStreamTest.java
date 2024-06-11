package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() throws IOException {
        byte[] data = "Test Data".getBytes();
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tOut.putArchiveEntry(new TarArchiveEntry("testfile.txt"));
        tOut.write(data);
        tOut.closeArchiveEntry();
        tOut.finish();
        assertEquals(1, tOut.currBytes);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        byte[] data = "Test Data".getBytes();
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tOut.putArchiveEntry(new TarArchiveEntry("testfile.txt"));
        tOut.write(data);
        tOut.closeArchiveEntry();
        assertEquals(11, tOut.currBytes);
    }

    @Test
    public void testWrite() throws IOException {
        byte[] data = "Test Data".getBytes();
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tOut.putArchiveEntry(new TarArchiveEntry("testfile.txt"));
        tOut.write(data, 0, data.length);
        tOut.closeArchiveEntry();
        assertEquals(9, tOut.currBytes);
    }

    @Test
    public void testFinish() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(os);
        assertThrows(IOException.class, () -> tOut.finish());
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(os);
        tOut.close();
        assertTrue(tOut.closed);
    }

    @Test
    public void testGetRecordSize() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(os);
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tOut.getRecordSize());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("testfile.txt");
        String entryName = "testfile.txt";
        TarArchiveOutputStream tOut = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ArchiveEntry entry = tOut.createArchiveEntry(inputFile, entryName);
        assertEquals(inputFile.getName(), entry.getName());
    }
}