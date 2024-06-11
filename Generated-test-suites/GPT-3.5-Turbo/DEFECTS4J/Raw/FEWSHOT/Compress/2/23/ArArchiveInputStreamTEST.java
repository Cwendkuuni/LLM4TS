package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        byte[] arEntryBytes = "headername.moc            1600116738         0         0         644        7          \nSomeContent".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(arEntryBytes);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        
        ArArchiveEntry arEntry = arArchiveInputStream.getNextArEntry();
        
        assertEquals("headername.moc", arEntry.getName());
        assertEquals(1600116738, arEntry.getLastModified());
        assertEquals(0, arEntry.getUserId());
        assertEquals(0, arEntry.getGroupId());
        assertEquals(644, arEntry.getMode());
        assertEquals(7, arEntry.getSize());
    }
    
    @Test
    public void testGetNextArEntry_NoEntry() throws IOException {
        byte[] arEntryBytes = ArArchiveEntry.TRAILER.getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(arEntryBytes);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        
        ArArchiveEntry arEntry = arArchiveInputStream.getNextArEntry();
        
        assertNull(arEntry);
    }
    
    @Test
    public void testGetNextEntry() throws IOException {
        byte[] arEntryBytes = "headername.moc            1600116738         0         0         644        7          \nSomeContent".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(arEntryBytes);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        
        ArchiveEntry entry = arArchiveInputStream.getNextEntry();
        
        assertTrue(entry instanceof ArArchiveEntry);
        ArArchiveEntry arEntry = (ArArchiveEntry) entry;
        assertEquals("headername.moc", arEntry.getName());
        assertEquals(1600116738, arEntry.getLastModified());
        assertEquals(0, arEntry.getUserId());
        assertEquals(0, arEntry.getGroupId());
        assertEquals(644, arEntry.getMode());
        assertEquals(7, arEntry.getSize());
    }
    
    @Test
    public void testClose() throws IOException {
        byte[] arEntryBytes = "headername.moc            1600116738         0         0         644        7          \nSomeContent".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(arEntryBytes);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        
        arArchiveInputStream.close();
        
        assertFalse(arArchiveInputStream.available() > 0);
        assertTrue(arArchiveInputStream.isClosed());
    }
    
    @Test
    public void testRead() throws IOException {
        byte[] arEntryBytes = "headername.moc            1600116738         0         0         644        7          \nSomeContent".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(arEntryBytes);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        
        int read = arArchiveInputStream.read();
        
        assertEquals('h', read);
    }
    
    @Test
    public void testReadArray() throws IOException {
        byte[] arEntryBytes = "headername.moc            1600116738         0         0         644        7          \nSomeContent".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(arEntryBytes);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        
        byte[] buffer = new byte[10];
        int read = arArchiveInputStream.read(buffer);
        
        assertEquals(10, read);
        assertEquals('h', buffer[0]);
        assertEquals('e', buffer[1]);
        assertEquals('a', buffer[2]);
        assertEquals('d', buffer[3]);
        assertEquals('e', buffer[4]);
        assertEquals('r', buffer[5]);
        assertEquals('n', buffer[6]);
        assertEquals('a', buffer[7]);
        assertEquals('m', buffer[8]);
        assertEquals('e', buffer[9]);
    }
    
    @Test
    public void testReadArrayWithOffsetAndLength() throws IOException {
        byte[] arEntryBytes = "headername.moc            1600116738         0         0         644        7          \nSomeContent".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(arEntryBytes);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        
        byte[] buffer = new byte[10];
        int read = arArchiveInputStream.read(buffer, 3, 6);
        
        assertEquals(6, read);
        assertEquals(0, buffer[0]);
        assertEquals(0, buffer[1]);
        assertEquals(0, buffer[2]);
        assertEquals('h', buffer[3]);
        assertEquals('e', buffer[4]);
        assertEquals('a', buffer[5]);
        assertEquals('d', buffer[6]);
        assertEquals(0, buffer[7]);
        assertEquals(0, buffer[8]);
        assertEquals(0, buffer[9]);
    }
    
    @Test
    public void testMatches() {
        byte[] signature = { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a };
        
        boolean matches = ArArchiveInputStream.matches(signature, 8);
        
        assertTrue(matches);
    }

}