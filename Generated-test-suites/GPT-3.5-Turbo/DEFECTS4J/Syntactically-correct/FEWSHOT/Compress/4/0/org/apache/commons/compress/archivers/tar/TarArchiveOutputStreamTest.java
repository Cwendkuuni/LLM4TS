package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TarArchiveOutputStreamTest {
    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
        assertNotNull(tarArchiveOutputStream);
        assertEquals(0, tarArchiveOutputStream.getRecordSize());
    }
    
    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
        tarArchiveOutputStream.setLongFileMode(0);
        tarArchiveOutputStream.setLongFileMode(1);
        tarArchiveOutputStream.setLongFileMode(2);
    }
    
    @Test
    public void testFinish() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
        try {
            tarArchiveOutputStream.finish();
        } catch (Exception e) {
            assertNull(e);
        }
    }
    
    @Test
    public void testClose() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
        try {
            tarArchiveOutputStream.close();
        } catch (Exception e) {
            assertNull(e);
        }
    }
    
    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
        assertEquals(512, tarArchiveOutputStream.getRecordSize());
    }
    
    @Test
    public void testPutArchiveEntry() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
        TarArchiveEntry entry = new TarArchiveEntry("filename");
        try {
            tarArchiveOutputStream.putArchiveEntry(entry);
        } catch (Exception e) {
            assertNull(e);
        }
    }
    
    @Test
    public void testCloseArchiveEntry() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
        try {
            tarArchiveOutputStream.closeArchiveEntry();
        } catch (Exception e) {
            assertNull(e);
        }
    }
    
    @Test
    public void testWrite() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
        byte[] wBuf = new byte[10];
        int wOffset = 0;
        int numToWrite = 10;
        try {
            tarArchiveOutputStream.write(wBuf, wOffset, numToWrite);
        } catch (Exception e) {
            assertNull(e);
        }
    }
    
    @Test
    public void testWriteEOFRecord() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
        try {
            tarArchiveOutputStream.writeEOFRecord();
        } catch (Exception e) {
            assertNull(e);
        }
    }
    
    @Test
    public void testFlush() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
        try {
            tarArchiveOutputStream.flush();
        } catch (Exception e) {
            assertNull(e);
        }
    }
    
    @Test
    public void testCreateArchiveEntry() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
        File inputFile = new File("inputFile");
        String entryName = "entryName";
        try {
            ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(inputFile, entryName);
            assertEquals(inputFile, archiveEntry.getFile());
            assertEquals(entryName, archiveEntry.getName());
        } catch (IOException e) {
            assertNull(e);
        }
    }
}