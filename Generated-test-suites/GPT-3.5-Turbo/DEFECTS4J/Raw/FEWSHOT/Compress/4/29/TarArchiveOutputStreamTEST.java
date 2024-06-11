package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TarArchiveOutputStreamTest {
    
    @Test
    public void testConstructor() {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(null);
        assertTrue(tao.getRecordSize() > 0);
        assertNull(tao.createArchiveEntry(null, null));
    }
    
    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(null);
        tao.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tao.getLongFileMode());
    }
    
    @Test
    public void testPutArchiveEntry() throws Exception {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(null);
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        tao.putArchiveEntry(entry);
        assertTrue(tao.haveUnclosedEntry);
    }
    
    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithoutPutArchiveEntry() throws Exception {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(null);
        tao.closeArchiveEntry();
    }
    
    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithoutFinish() throws Exception {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(null);
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        tao.putArchiveEntry(entry);
        tao.closeArchiveEntry();
    }
    
    @Test
    public void testCloseArchiveEntryWithEnoughBytesWritten() throws Exception {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(null);
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        entry.setSize(5);
        tao.putArchiveEntry(entry);
        byte[] bytes = new byte[5];
        tao.write(bytes, 0, 5);
        tao.closeArchiveEntry();
        assertEquals(0, tao.assemLen);
        assertEquals(5, tao.currBytes);
        assertEquals(5, tao.currSize);
        assertFalse(tao.haveUnclosedEntry);
    }
    
    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithNotEnoughBytesWritten() throws Exception {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(null);
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        entry.setSize(10);
        tao.putArchiveEntry(entry);
        byte[] bytes = new byte[5];
        tao.write(bytes, 0, 5);
        tao.closeArchiveEntry();
    }
    
    @Test
    public void testWrite() throws Exception {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(null);
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        entry.setSize(5);
        tao.putArchiveEntry(entry);
        byte[] bytes = new byte[5];
        tao.write(bytes, 0, 5);
        assertEquals(0, tao.assemLen);
        assertEquals(5, tao.currBytes);
        assertEquals(5, tao.currSize);
    }
    
    @Test(expected = IOException.class)
    public void testWriteExceedingSize() throws Exception {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(null);
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        entry.setSize(5);
        tao.putArchiveEntry(entry);
        byte[] bytes = new byte[10];
        tao.write(bytes, 0, 10);
    }

    @Test
    public void testWriteEOFRecord() throws Exception {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(null);
        tao.writeEOFRecord();
    }
    
    @Test
    public void testFinish() throws Exception {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(null);
        tao.finish();
    }

    @Test
    public void testClose() throws Exception {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(null);
        tao.close();
    }

    @Test
    public void testFlush() throws Exception {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(null);
        tao.flush();
    }

}