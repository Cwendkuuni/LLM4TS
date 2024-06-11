package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class TarArchiveOutputStreamTest {
    private TarArchiveOutputStream tarOutputStream;
    
    @Before
    public void setUp() {
        tarOutputStream = new TarArchiveOutputStream(null); 
    }
    
    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOutputStream.longFileMode);
        
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.longFileMode);
        
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutputStream.longFileMode);
    }
    
    @Test
    public void testGetRecordSize() {
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarOutputStream.getRecordSize());
    }
    
    @Test
    public void testFinish() {
        tarOutputStream.haveUnclosedEntry = false;
        tarOutputStream.finish();
    }
    
    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        tarOutputStream.haveUnclosedEntry = true;
        tarOutputStream.finish();
    }
    
    @Test
    public void testClose() throws IOException {
        tarOutputStream.closed = false;
        tarOutputStream.close();
    }
    
    @Test
    public void testCloseWhenAlreadyClosed() throws IOException {
        tarOutputStream.closed = true;
        tarOutputStream.close();
    }
    
    @Test
    public void testWrite() throws IOException {
        tarOutputStream.currBytes = 0;

        byte[] wBuf = "Hello World".getBytes();
        int wOffset = 0;
        int numToWrite = wBuf.length;
        
        tarOutputStream.write(wBuf, wOffset, numToWrite);
        
        assertEquals(wBuf.length, tarOutputStream.currBytes);
    }
    
    @Test(expected = IOException.class)
    public void testWriteWithExceedingSizeInHeader() throws IOException {
        tarOutputStream.currBytes = 5;

        byte[] wBuf = "Hello World".getBytes();
        int wOffset = 0;
        int numToWrite = wBuf.length;
        
        tarOutputStream.write(wBuf, wOffset, numToWrite);
    }
    
    @Test
    public void testWriteWithAssembleLen() throws IOException {
        tarOutputStream.assemLen = 5;

        byte[] wBuf = "Hello World".getBytes();
        int wOffset = 0;
        int numToWrite = wBuf.length;
        
        tarOutputStream.write(wBuf, wOffset, numToWrite);
        
        assertEquals(wBuf.length, tarOutputStream.assemLen);
    }
    
    @Test
    public void testWriteWithEmptyAssembleLen() throws IOException {
        tarOutputStream.assemLen = 0;

        byte[] wBuf = "Hello World".getBytes();
        int wOffset = 0;
        int numToWrite = 0;
        
        tarOutputStream.write(wBuf, wOffset, numToWrite);
        
        assertEquals(0, tarOutputStream.assemLen);
    }
    
    @Test
    public void testWriteWithNumToWriteLessThanRecordBufLength() throws IOException {
        byte[] wBuf = new byte[TarBuffer.DEFAULT_RCDSIZE - 1];
        int wOffset = 0;
        int numToWrite = wBuf.length;
        
        tarOutputStream.write(wBuf, wOffset, numToWrite);
        
        assertEquals(wBuf.length, tarOutputStream.assemLen);
    }
    
    @Test
    public void testWriteEOFRecord() throws IOException {
        tarOutputStream.writeEOFRecord();
    }
    
    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("myfile.txt");
        String entryName = "file.txt";
        
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(inputFile, entryName);
        
        assertEquals(inputFile, ((TarArchiveEntry) entry).getFile());
        assertEquals(entryName, ((TarArchiveEntry) entry).getName());
    }
}