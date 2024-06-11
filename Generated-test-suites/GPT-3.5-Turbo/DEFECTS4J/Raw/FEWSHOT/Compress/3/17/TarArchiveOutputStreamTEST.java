package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        assertNotNull(tarOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutputStream.longFileMode);
    }

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.finish();

        byte[] expectedOutput = new byte[1024];
        for (int i = 0; i < expectedOutput.length; i++) {
            expectedOutput[i] = 0;
        }

        assertEquals(expectedOutput.length, outputStream.toByteArray().length);
        assertTrue(areByteArraysEqual(expectedOutput, outputStream.toByteArray()));
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.close();
        assertTrue(tarOutputStream.closed);
    }

    @Test
    public void testGetRecordSize() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(null, "testEntry");
        tarOutputStream.putArchiveEntry(entry);
        assertEquals("testEntry", tarOutputStream.currName);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(null, "testEntry");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        assertEquals(0, tarOutputStream.assemLen);
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        byte[] writeBuffer = "test".getBytes();
        int writeBufferOffset = 0;
        int numToWrite = writeBuffer.length;
        tarOutputStream.write(writeBuffer, writeBufferOffset, numToWrite);
        assertEquals(numToWrite, tarOutputStream.assemLen);
    }

    @Test
    public void testWriteEOFRecord() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.writeEOFRecord();
        byte[] expectedOutput = new byte[512];
        for (int i = 0; i < expectedOutput.length; i++) {
            expectedOutput[i] = 0;
        }
        assertEquals(expectedOutput.length, outputStream.toByteArray().length);
        assertTrue(areByteArraysEqual(expectedOutput, outputStream.toByteArray()));
    }

    @Test
    public void testFlush() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.flush();
        // Flush is implemented via FilterOutputStream, no need for additional testing.
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        File inputFile = new File("testFile");
        String entryName = "testEntry";
        ArchiveEntry archiveEntry = tarOutputStream.createArchiveEntry(inputFile, entryName);
        assertEquals(inputFile, archiveEntry.getFile());
        assertEquals(entryName, archiveEntry.getName());
    }

    private boolean areByteArraysEqual(byte[] expectedArray, byte[] actualArray) {
        if (expectedArray.length != actualArray.length)
            return false;
        for (int i = 0; i < expectedArray.length; i++) {
            if (expectedArray[i] != actualArray[i])
                return false;
        }
        return true;
    }
}