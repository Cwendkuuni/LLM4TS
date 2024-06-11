package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TarArchiveOutputStreamTest {
    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream();
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOutputStream.getLongFileMode());
    }

    @Test
    public void testPutArchiveEntry() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream();
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        try {
            tarOutputStream.putArchiveEntry(entry);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(entry, tarOutputStream.getCurrentEntry());
    }

    @Test
    public void testCloseArchiveEntry() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream();
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        try {
            tarOutputStream.putArchiveEntry(entry);
            tarOutputStream.closeArchiveEntry();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNull(tarOutputStream.getCurrentEntry());
    }
    
    @Test
    public void testWrite() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream();
        byte[] data = "Hello, World!".getBytes();
        try {
            tarOutputStream.write(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(data.length, tarOutputStream.getBytesWritten());
    }
    
    @Test
    public void testFinish() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream();
        try {
            tarOutputStream.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(true, tarOutputStream.isFinished());
    }
    
    @Test
    public void testClose() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream();
        try {
            tarOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(true, tarOutputStream.isClosed());
    }
    
    @Test(expected = IOException.class)
    public void testPutArchiveEntryFinished() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream();
        tarOutputStream.finish();
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryUnclosed() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream();
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testCreateArchiveEntry() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream();
        File inputFile = new File("test.txt");
        String entryName = "test.txt";
        try {
            ArchiveEntry archiveEntry = tarOutputStream.createArchiveEntry(inputFile, entryName);
            assertEquals(inputFile, archiveEntry.getFile());
            assertEquals(entryName, archiveEntry.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}