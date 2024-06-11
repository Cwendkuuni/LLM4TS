package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testTarArchiveOutputStream() {
        // TODO: Write test case for TarArchiveOutputStream constructor
    }

    @Test
    public void testSetLongFileMode() {
        // TODO: Write test case for setLongFileMode
    }

    @Test
    public void testFinish() {
        // TODO: Write test case for finish
    }

    @Test
    public void testClose() {
        // TODO: Write test case for close
    }

    @Test
    public void testGetRecordSize() {
        // TODO: Write test case for getRecordSize
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        TarArchiveOutputStream output = new TarArchiveOutputStream(new ByteArrayOutputStream());
        output.putArchiveEntry(entry);
        assertTrue(output.currBytes == 0);
        assertEquals(entry.getName(), output.currName);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        TarArchiveOutputStream output = new TarArchiveOutputStream(new ByteArrayOutputStream());
        output.putArchiveEntry(entry);
        output.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "1234567890".getBytes());
        TarArchiveOutputStream output = new TarArchiveOutputStream(new ByteArrayOutputStream());
        output.putArchiveEntry(entry);
        output.write("1234567890".getBytes());
        output.closeArchiveEntry();
    }

    @Test
    public void testWriteEOFRecord() throws IOException {
        TarArchiveOutputStream output = new TarArchiveOutputStream(new ByteArrayOutputStream());
        output.writeEOFRecord();
    }

    @Test
    public void testFlush() throws IOException {
        TarArchiveOutputStream output = new TarArchiveOutputStream(new ByteArrayOutputStream());
        output.flush();
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("test.txt");
        String entryName = "test.txt";
        TarArchiveOutputStream output = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ArchiveEntry entry = output.createArchiveEntry(inputFile, entryName);
        assertEquals(inputFile.getName(), entry.getName());
    }
}