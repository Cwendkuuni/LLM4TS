package org.apache.commons.compress.archivers.tar;

import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;

public class TarArchiveOutputStreamTest {
  
    @Test
    public void testConstructor() throws IOException {
        OutputStream os = new FileOutputStream(new File("test.tar"));
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        
        assertEquals(os, tarOut.out);
        assertNotNull(tarOut.buffer);
        assertEquals(TarBuffer.DEFAULT_BLKSIZE, tarOut.buffer.getBlockSize());
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarOut.buffer.getRecordSize());
        assertEquals(0, tarOut.assemLen);
        assertNotNull(tarOut.assemBuf);
        assertNotNull(tarOut.recordBuf);
        
        tarOut.close();
    }
  
    @Test
    public void testSetLongFileMode() throws IOException {
        OutputStream os = new FileOutputStream(new File("test.tar"));
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOut.longFileMode);
        
        tarOut.close();
    }
  
    @Test
    public void testFinish() throws IOException {
        OutputStream os = new FileOutputStream(new File("test.tar"));
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        
        tarOut.finish();
        
        tarOut.close();
    }
  
    @Test
    public void testClose() throws IOException {
        OutputStream os = new FileOutputStream(new File("test.tar"));
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        
        tarOut.close();
        
        assertTrue(tarOut.closed);
    }
  
    @Test
    public void testGetRecordSize() throws IOException {
        OutputStream os = new FileOutputStream(new File("test.tar"));
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        
        assertEquals(tarOut.buffer.getRecordSize(), tarOut.getRecordSize());
        
        tarOut.close();
    }
  
    @Test
    public void testPutArchiveEntry() throws IOException {
        OutputStream os = new FileOutputStream(new File("test.tar"));
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOut.putArchiveEntry(entry);
        
        assertEquals(entry, tarOut.buffer.getDefaultEntry());
        
        tarOut.close();
    }
  
    @Test
    public void testCloseArchiveEntry() throws IOException {
        OutputStream os = new FileOutputStream(new File("test.tar"));
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOut.putArchiveEntry(entry);
        tarOut.closeArchiveEntry();
        
        tarOut.close();
    }
  
    @Test
    public void testWrite() throws IOException {
        OutputStream os = new FileOutputStream(new File("test.tar"));
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOut.putArchiveEntry(entry);
        
        byte[] data = "This is a test".getBytes();
        tarOut.write(data);
        
        tarOut.closeArchiveEntry();
        tarOut.close();
    }
  
    @Test
    public void testWriteEofRecord() throws IOException {
        OutputStream os = new FileOutputStream(new File("test.tar"));
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        
        tarOut.writeEOFRecord();
        
        tarOut.close();
    }
  
    @Test
    public void testFlush() throws IOException {
        OutputStream os = new FileOutputStream(new File("test.tar"));
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        
        tarOut.flush();
        
        tarOut.close();
    }
  
    @Test
    public void testCreateArchiveEntry() throws IOException {
        OutputStream os = new FileOutputStream(new File("test.tar"));
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        
        File inputFile = new File("test.txt");
        String entryName = "test.txt";
        
        ArchiveEntry archiveEntry = tarOut.createArchiveEntry(inputFile, entryName);
        
        assertTrue(archiveEntry instanceof TarArchiveEntry);
        assertEquals(inputFile, ((TarArchiveEntry) archiveEntry).getFile());
        assertEquals(entryName, archiveEntry.getName());
        
        tarOut.close();
    }
}