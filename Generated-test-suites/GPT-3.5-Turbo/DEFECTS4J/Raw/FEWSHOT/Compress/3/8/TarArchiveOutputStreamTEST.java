package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(null);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOutputStream.getLongFileMode());
        
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());
        
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(null);
        try {
            tarOutputStream.finish();
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    public void testClose() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(null);
        try {
            tarOutputStream.close();
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(null);
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(null);
        try {
            TarArchiveEntry entry = new TarArchiveEntry("testFile");
            tarOutputStream.putArchiveEntry(entry);
            tarOutputStream.closeArchiveEntry();
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    public void testCloseArchiveEntry() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(null);
        try {
            tarOutputStream.closeArchiveEntry();
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    public void testWrite() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(null);
        try {
            byte[] wBuf = new byte[100];
            tarOutputStream.write(wBuf, 0, wBuf.length);
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    public void testWriteEOFRecord() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(null);
        try {
            tarOutputStream.writeEOFRecord();
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    public void testFlush() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(null);
        try {
            tarOutputStream.flush();
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    public void testCreateArchiveEntry() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(null);
        try {
            File inputFile = new File("testFile");
            String entryName = "entryName";
            
            TarArchiveEntry entry = (TarArchiveEntry)tarOutputStream.createArchiveEntry(inputFile, entryName);
            assertEquals(inputFile, entry.getFile());
            assertEquals(entryName, entry.getName());
        } catch (Exception e) {
            assertNull(e);
        }
    }

}