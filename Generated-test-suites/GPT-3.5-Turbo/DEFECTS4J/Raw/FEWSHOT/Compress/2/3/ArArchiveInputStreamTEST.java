import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArArchiveInputStreamTest {
    
    @Test
    public void testGetNextArEntry() throws IOException {
        byte[] header = new byte[] { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a };
        byte[] trailer = "trailer".getBytes();
        byte[] name = "testName".getBytes();
        byte[] length = "10".getBytes();
        
        byte[] data = new byte[header.length + name.length + length.length + trailer.length];
        System.arraycopy(header, 0, data, 0, header.length);
        System.arraycopy(name, 0, data, header.length, name.length);
        System.arraycopy(length, 0, data, header.length + name.length, length.length);
        System.arraycopy(trailer, 0, data, header.length + name.length + length.length, trailer.length);
        
        InputStream testStream = new ByteArrayInputStream(data);
        ArArchiveInputStream archiveStream = new ArArchiveInputStream(testStream);
        
        // Test correct entry
        ArArchiveEntry entry = archiveStream.getNextArEntry();
        assertEquals("testName", entry.getName());
        assertEquals(10, entry.getLength());
        
        // Test null entry when EOF is reached
        assertNull(archiveStream.getNextArEntry());
        
        archiveStream.close();
    }
    
    @Test
    public void testGetNextEntry() throws IOException {
        byte[] header = new byte[] { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a };
        byte[] trailer = "trailer".getBytes();
        byte[] name = "testName".getBytes();
        byte[] length = "10".getBytes();
        
        byte[] data = new byte[header.length + name.length + length.length + trailer.length];
        System.arraycopy(header, 0, data, 0, header.length);
        System.arraycopy(name, 0, data, header.length, name.length);
        System.arraycopy(length, 0, data, header.length + name.length, length.length);
        System.arraycopy(trailer, 0, data, header.length + name.length + length.length, trailer.length);
        
        InputStream testStream = new ByteArrayInputStream(data);
        ArArchiveInputStream archiveStream = new ArArchiveInputStream(testStream);
        
        // Test correct entry
        ArchiveEntry entry = archiveStream.getNextEntry();
        assertEquals("testName", entry.getName());
        assertEquals(10, entry.getLength());
        
        // Test null entry when EOF is reached
        assertNull(archiveStream.getNextEntry());
        
        archiveStream.close();
    }
    
    @Test
    public void testClose() throws IOException {
        byte[] header = new byte[] { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a };
        byte[] name = "testName".getBytes();
        byte[] length = "10".getBytes();
        byte[] trailer = new byte[] { 0x74, 0x72, 0x61, 0x69, 0x6c, 0x65, 0x72 };
        
        byte[] data = new byte[header.length + name.length + length.length + trailer.length];
        System.arraycopy(header, 0, data, 0, header.length);
        System.arraycopy(name, 0, data, header.length, name.length);
        System.arraycopy(length, 0, data, header.length + name.length, length.length);
        System.arraycopy(trailer, 0, data, header.length + name.length + length.length, trailer.length);
        
        InputStream testStream = new ByteArrayInputStream(data);
        ArArchiveInputStream archiveStream = new ArArchiveInputStream(testStream);
        
        archiveStream.close();
        assertTrue(archiveStream.isClosed());
    }
    
    @Test
    public void testReadByte() throws IOException {
        byte[] data = new byte[] { 0x10, 0x20, 0x30, 0x40, 0x50 };
        InputStream testStream = new ByteArrayInputStream(data);
        ArArchiveInputStream archiveStream = new ArArchiveInputStream(testStream);
        
        assertEquals(0x10, archiveStream.read());
        assertEquals(0x20, archiveStream.read());
        assertEquals(0x30, archiveStream.read());
        assertEquals(0x40, archiveStream.read());
        assertEquals(0x50, archiveStream.read());
        
        archiveStream.close();
    }
    
    @Test
    public void testReadByteArray() throws IOException {
        byte[] data = new byte[] { 0x10, 0x20, 0x30, 0x40, 0x50 };
        InputStream testStream = new ByteArrayInputStream(data);
        ArArchiveInputStream archiveStream = new ArArchiveInputStream(testStream);
        
        byte[] buffer = new byte[2];
        archiveStream.read(buffer);
        assertEquals(0x10, buffer[0]);
        assertEquals(0x20, buffer[1]);
        
        archiveStream.read(buffer);
        assertEquals(0x30, buffer[0]);
        assertEquals(0x40, buffer[1]);
        
        archiveStream.read(buffer);
        assertEquals(0x50, buffer[0]);
        assertEquals(0x00, buffer[1]);
        
        archiveStream.close();
    }
    
    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        byte[] data = new byte[] { 0x10, 0x20, 0x30, 0x40, 0x50 };
        InputStream testStream = new ByteArrayInputStream(data);
        ArArchiveInputStream archiveStream = new ArArchiveInputStream(testStream);
        
        byte[] buffer = new byte[4];
        archiveStream.read(buffer, 1, 3);
        assertEquals(0x00, buffer[0]);
        assertEquals(0x10, buffer[1]);
        assertEquals(0x20, buffer[2]);
        assertEquals(0x30, buffer[3]);
        
        archiveStream.close();
    }
    
    @Test
    public void testMatches() {
        byte[] signature1 = new byte[] { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a };
        assertTrue(ArArchiveInputStream.matches(signature1, 8));
        
        byte[] signature2 = new byte[] { 0x21, 0x3c };
        assertFalse(ArArchiveInputStream.matches(signature2, 2));
    }
}