package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TarArchiveOutputStreamTest {
    
    private TarArchiveOutputStream tarOutputStream;
    private File testFile;
    
    @Before
    public void setUp() throws IOException {
        OutputStream os = new OutputStream();
        tarOutputStream = new TarArchiveOutputStream(os);
        testFile = new File("testFile.txt");
        testFile.createNewFile();
    }
    
    @After
    public void tearDown() {
        testFile.delete();
    }

    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutputStream.getLongFileMode());
    }
    
    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry(testFile, "testFile.txt");
        tarOutputStream.putArchiveEntry(entry);
        assertNotNull(tarOutputStream.getCurName());
        assertNotNull(tarOutputStream.getCurBytes());
        assertNotNull(tarOutputStream.getCurSize());
        assertTrue(entry.isDirectory());
    }
    
    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryWithLongFileNameAndModeError() throws IOException {
        String longFileName = "a".repeat(TarConstants.NAMELEN);
        TarArchiveEntry entry = new TarArchiveEntry(new File(""), longFileName);
        tarOutputStream.putArchiveEntry(entry);
    }
    
    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry(testFile, "testFile.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
    }
    
    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithIncompleteBytes() throws IOException {
        tarOutputStream.setRecordBuf(new byte[100]);
        TarArchiveEntry entry = new TarArchiveEntry(testFile, "testFile.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
    }
    
    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry(testFile, "testFile.txt");
        tarOutputStream.putArchiveEntry(entry);
        byte[] dataBytes = "testData".getBytes();
        tarOutputStream.write(dataBytes, 0, dataBytes.length);
        tarOutputStream.closeArchiveEntry();
    }
    
    @Test
    public void testFinish() throws IOException {
        tarOutputStream.finish();
    }
    
    @Test
    public void testClose() throws IOException {
        tarOutputStream.close();
    }
    
    @Test
    public void testGetRecordSize() {
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarOutputStream.getRecordSize());
    }
    
    @Test
    public void testCreateArchiveEntry() throws IOException {
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(testFile, "testFile.txt");
        assertNotNull(entry);
        assertEquals(TarArchiveEntry.class, entry.getClass());
    }
}