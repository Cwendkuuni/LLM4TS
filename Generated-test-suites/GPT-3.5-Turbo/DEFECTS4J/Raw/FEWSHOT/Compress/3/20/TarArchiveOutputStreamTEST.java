
package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os, 512, 1024);
        
        assertEquals(os, tarOut.out);
        assertEquals(512, tarOut.buffer.getRecordSize());
        assertEquals(1024, tarOut.buffer.getBlockSize());
        assertEquals(1024, tarOut.recordBuf.length);
        assertEquals(1024, tarOut.assemBuf.length);
        assertEquals(0, tarOut.assemLen);
        assertEquals(false, tarOut.closed);
        
        tarOut.close();
    }
    
    @Test
    public void testSetLongFileMode() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOut.longFileMode);
        
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOut.longFileMode);
        
        tarOut.close();
    }
    
    @Test
    public void testGetRecordSize() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        
        assertEquals(512, tarOut.getRecordSize());
        
        tarOut.close();
    }
    
    @Test
    public void testFinish() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        
        tarOut.finish();
        
        byte[] expected = new byte[tarOut.getRecordSize()];
        assertArrayEquals(expected, ((ByteArrayOutputStream) os).toByteArray());
        
        tarOut.close();
    }
    
    @Test
    public void testClose() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        
        tarOut.close();
        
        assertEquals(true, tarOut.closed);
    }
    
    @Test
    public void testPutArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        
        ArchiveEntry entry = new TarArchiveEntry("test");
        tarOut.putArchiveEntry(entry);
        assertEquals(entry, tarOut.currName);
        
        tarOut.close();
    }
    
    @Test
    public void testCloseArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        
        TarArchiveEntry entry = new TarArchiveEntry("test");
        entry.setSize(10);
        tarOut.putArchiveEntry(entry);
        tarOut.closeArchiveEntry();
        
        assertEquals(0, tarOut.assemLen);
        assertEquals(0, tarOut.currBytes);
        
        tarOut.close();
    }
    
    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithIncompleteEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        
        TarArchiveEntry entry = new TarArchiveEntry("test");
        entry.setSize(10);
        tarOut.putArchiveEntry(entry);
        tarOut.write(new byte[] {1, 2, 3});
        tarOut.closeArchiveEntry();
        
        tarOut.close();
    }
    
    @Test
    public void testWrite() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        
        TarArchiveEntry entry = new TarArchiveEntry("test");
        entry.setSize(10);
        tarOut.putArchiveEntry(entry);
        tarOut.write(new byte[] {1, 2, 3});
        tarOut.write(new byte[] {4, 5, 6}, 0, 2);
        tarOut.closeArchiveEntry();
        
        tarOut.close();
    }
    
    @Test
    public void testWriteEOFRecord() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        
        tarOut.writeEOFRecord();
        
        byte[] expected = new byte[tarOut.getRecordSize()];
        assertArrayEquals(expected, os.toByteArray());
        
        tarOut.close();
    }
    
    @Test
    public void testFlush() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        
        tarOut.flush();
        
        tarOut.close();
    }
    
    @Test
    public void testCreateArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        
        ArchiveEntry entry = tarOut.createArchiveEntry(new File("test"), "entry");
        
        assertEquals(TarArchiveEntry.class, entry.getClass());
        
        tarOut.close();
    }

}

