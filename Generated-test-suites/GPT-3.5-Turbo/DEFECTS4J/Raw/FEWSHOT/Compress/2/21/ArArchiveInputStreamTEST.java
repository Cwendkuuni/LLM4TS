

package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ArArchiveInputStreamTest {
    @Test
    public void testGetNextArEntry() throws IOException {
        byte[] expectedHeader = ArArchiveEntry.HEADER.getBytes();
        byte[] expectedTrailer = ArArchiveEntry.TRAILER.getBytes();

        InputStream input = new ByteArrayInputStream(expectedHeader);
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(input);
        
        //First entry
        ArArchiveEntry arEntry = archiveInputStream.getNextArEntry();
        assertNotNull(arEntry);
        assertEquals("header", new String(expectedHeader).trim(), arEntry.getHeader());
        
        byte[] entryData = new byte[arEntry.getLength()];
        int bytesRead = 
            archiveInputStream.read(entryData, 0, arEntry.getLength());
        assertEquals("bytes read", arEntry.getLength(), bytesRead);
        assertEquals("header", expectedHeader, entryData);
        
        //Second entry
        input = new ByteArrayInputStream(expectedTrailer);
        archiveInputStream = new ArArchiveInputStream(input);
        arEntry = archiveInputStream.getNextArEntry();
        assertNull(arEntry);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        byte[] expectedHeader = ArArchiveEntry.HEADER.getBytes();
        byte[] expectedTrailer = ArArchiveEntry.TRAILER.getBytes();

        InputStream input = new ByteArrayInputStream(expectedHeader);
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(input);
        
        ArchiveEntry entry = archiveInputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals("header", new String(expectedHeader).trim(), entry.getHeader());
        
        byte[] entryData = new byte[entry.getLength()];
        int bytesRead = 
            archiveInputStream.read(entryData, 0, entry.getLength());
        assertEquals("bytes read", entry.getLength(), bytesRead);
        assertEquals("header", expectedHeader, entryData);
        
        input = new ByteArrayInputStream(expectedTrailer);
        archiveInputStream = new ArArchiveInputStream(input);
        entry = archiveInputStream.getNextEntry();
        assertNull(entry);
    }

    @Test
    public void testClose() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] {});
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(input);
        
        assertFalse(archiveInputStream.isClosed());
        archiveInputStream.close();
        assertTrue(archiveInputStream.isClosed());
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[10];
        InputStream input = new ByteArrayInputStream(new byte[] {1, 2, 3, 4, 5});
        ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(input);
        
        int bytesRead = archiveInputStream.read(buffer, 0, buffer.length);
        assertEquals(5, bytesRead);
        
        input = new ByteArrayInputStream(new byte[] {});
        archiveInputStream = new ArArchiveInputStream(input);
        bytesRead = archiveInputStream.read(buffer, 0, buffer.length);
        assertEquals(-1, bytesRead);
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        assertTrue(ArArchiveInputStream.matches(signature, 8));
        
        signature = new byte[] {0x21, 0x3c, 0x61, 0x72};
        assertFalse(ArArchiveInputStream.matches(signature, 4));
        
        signature = new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x00};
        assertTrue(ArArchiveInputStream.matches(signature, 9));
    }
}
