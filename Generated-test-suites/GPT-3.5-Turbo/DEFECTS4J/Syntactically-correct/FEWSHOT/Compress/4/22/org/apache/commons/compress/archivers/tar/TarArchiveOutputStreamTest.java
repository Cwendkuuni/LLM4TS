package org.apache.commons.compress.archivers.tar;

import org.junit.Test;

import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor1() {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(null);
        assertNotNull(tarOut);
    }

    @Test
    public void testConstructor2() {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(null, 512);
        assertNotNull(tarOut);
    }

    @Test
    public void testConstructor3() {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(null, 512, 1024);
        assertNotNull(tarOut);
    }
    
    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(null);
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOut.getLongFileMode());
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOut.getLongFileMode());
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOut.getLongFileMode());
    }
    
    @Test
    public void testFinish() {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(null);
        try {
            tarOut.finish();
            assertTrue(true);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testClose() {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(null);
        try {
            tarOut.close();
            assertTrue(true);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(null);
        assertEquals(tarOut.getRecordSize(), TarBuffer.DEFAULT_RCDSIZE);
    }
    
    @Test
    public void testPutArchiveEntry() {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(null);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        try {
            tarOut.putArchiveEntry(entry);
            assertTrue(true);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testCloseArchiveEntry() {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(null);
        try {
            tarOut.closeArchiveEntry();
            assertTrue(true);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testWrite() {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(null);
        byte[] buf = new byte[512];
        try {
            tarOut.write(buf, 0, 512);
            assertTrue(true);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testWriteEOFRecord() {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(null);
        try {
            tarOut.writeEOFRecord();
            assertTrue(true);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFlush() {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(null);
        try {
            tarOut.flush();
            assertTrue(true);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveEntry() {
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(null);
        File inputFile = new File("test.txt");
        String entryName = "test.txt";
        try {
            tarOut.createArchiveEntry(inputFile, entryName);
            assertTrue(true);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}