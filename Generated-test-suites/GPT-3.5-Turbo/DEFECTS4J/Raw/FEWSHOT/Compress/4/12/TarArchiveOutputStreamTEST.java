package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;
    
    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }
    
    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
        outputStream.close();
    }
    
    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutputStream.longFileMode);
        
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.longFileMode);
        
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOutputStream.longFileMode);
    }
    
    @Test
    public void testGetRecordSize() {
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarOutputStream.getRecordSize());
    }
    
    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        
        tarOutputStream.putArchiveEntry(entry);
        
        assertTrue(tarOutputStream.haveUnclosedEntry);
        assertEquals(0, tarOutputStream.currBytes);
        assertEquals("file.txt", tarOutputStream.currName);
    }
    
    @Test
    public void testCloseArchiveEntry() throws IOException {
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("file.txt"));
        
        tarOutputStream.closeArchiveEntry();
        
        assertFalse(tarOutputStream.haveUnclosedEntry);
    }
    
    @Test(expected = IOException.class)
    public void testCloseArchiveEntryThrowsIOException() throws IOException {
        tarOutputStream.closeArchiveEntry();
    }
    
    @Test
    public void testWrite() throws IOException {
        byte[] data = "test data".getBytes();
        
        tarOutputStream.write(data);
        
        assertEquals(data.length, tarOutputStream.currBytes);
    }
    
    @Test
    public void testWriteWithOffsetAndLength() throws IOException {
        byte[] data = "test data".getBytes();
        
        tarOutputStream.write(data, 0, 4);
        
        assertEquals(4, tarOutputStream.currBytes);
    }
    
    @Test(expected = IOException.class)
    public void testWriteThrowsIOException() throws IOException {
        byte[] data = "test data".getBytes();
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("file.txt"));
        
        tarOutputStream.write(data);
    }
    
    @Test
    public void testFinish() throws IOException {
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("file.txt"));
        tarOutputStream.finish();
        
        assertTrue(tarOutputStream.haveUnclosedEntry);
    }
    
    @Test(expected = IOException.class)
    public void testFinishThrowsIOException() throws IOException {
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("file.txt"));
        
        tarOutputStream.finish();
    }
    
    @Test
    public void testClose() throws IOException {
        tarOutputStream.close();
        
        assertTrue(tarOutputStream.closed);
    }
    
    @Test(expected = IOException.class)
    public void testCloseThrowsIOException() throws IOException {
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("file.txt"));
        
        tarOutputStream.close();
    }
    
    @Test
    public void testCreateArchiveEntry() throws IOException {
        File input = new File("test.txt");
        String entryName = "test.txt";
        
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(input, entryName);
        
        assertTrue(entry instanceof TarArchiveEntry);
        assertEquals(input, ((TarArchiveEntry) entry).getFile());
        assertEquals(entryName, entry.getName());
    }
}