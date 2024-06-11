
package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Test;

public class TarArchiveOutputStreamTest {
    
    @Test
    public void testConstructor() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(os);
        
        assertTrue(tao.isBigNumberModeEnabled());
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tao.getRecordSize());
        
        tao.close();
    }
    
    @Test
    public void testSetBigNumberModeEnabled() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(os);
        
        assertTrue(tao.isBigNumberModeEnabled());
        
        tao.setBigNumberModeEnabled(false);
        assertFalse(tao.isBigNumberModeEnabled());
        
        tao.close();
    }
    
    @Test
    public void testSetLongFileMode() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(os);
        
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tao.getLongFileMode());
        
        tao.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tao.getLongFileMode());
        
        tao.close();
    }
    
    @Test
    public void testPutArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(os);
        
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        tao.putArchiveEntry(entry);
        
        assertEquals("file.txt", entry.getName());
        
        tao.closeArchiveEntry();
        tao.close();
    }
    
    @Test
    public void testCloseArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(os);
        
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        tao.putArchiveEntry(entry);
        
        tao.closeArchiveEntry();
        
        assertFalse(tao.haveUnclosedEntry);
        
        tao.close();
    }
    
    @Test
    public void testWrite() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(os);
        
        byte[] data = "Hello World".getBytes();
        
        TarArchiveEntry entry = new TarArchiveEntry("file.txt", data.length);
        tao.putArchiveEntry(entry);
        
        tao.write(data);
        
        assertEquals(data.length, entry.getSize());
        
        tao.closeArchiveEntry();
        tao.close();
    }
    
    @Test
    public void testFinish() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(os);
        
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        tao.putArchiveEntry(entry);
        
        tao.finish();
        
        assertTrue(tao.finished);
        
        tao.close();
    }
    
    @Test
    public void testClose() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(os);
        
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        tao.putArchiveEntry(entry);
        
        tao.closeArchiveEntry();
        
        assertTrue(tao.closed);
        
        tao.close();
    }
    
    @Test
    public void testGetRecordSize() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(os);
        
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tao.getRecordSize());
        
        tao.close();
    }
    
    @Test
    public void testCreateArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(os);
        
        TarArchiveEntry entry = tao.createArchiveEntry(new File("file.txt"), "file.txt");
        
        assertEquals("file.txt", entry.getName());
        
        tao.close();
    }
    
    @Test(expected = IOException.class)
    public void testCreateArchiveEntryFinished() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(os);
        
        tao.finish();
        
        tao.createArchiveEntry(new File("file.txt"), "file.txt");
        
        tao.close();
    }
}
