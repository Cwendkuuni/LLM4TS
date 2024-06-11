
package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream arInputStream;

    @Before
    public void setUp() throws IOException {
        String header = "!<arch>\n";
        byte[] headerBytes = header.getBytes();
        InputStream inputStream = new ByteArrayInputStream(headerBytes);
        arInputStream = new ArArchiveInputStream(inputStream);
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry entry = arInputStream.getNextArEntry();
        assertNotNull(entry);
        assertEquals("file.txt", entry.getName());
        assertEquals(100L, entry.getLength());

        assertNull(arInputStream.getNextArEntry());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry entry = arInputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals("file.txt", entry.getName());
        assertEquals(100L, entry.getSize());

        assertNull(arInputStream.getNextEntry());
    }

    @Test
    public void testClose() throws IOException {
        arInputStream.close();
        
        // Verify that the input stream is closed
        // by attempting to read from it
        byte[] buffer = new byte[10];
        int bytesRead = arInputStream.read(buffer);
        assertEquals(-1, bytesRead);
    }

    @Test
    public void testRead() throws IOException {
        // Read the header
        int byte1 = arInputStream.read();
        assertEquals(33, byte1);
        int byte2 = arInputStream.read();
        assertEquals(60, byte2);
        int byte3 = arInputStream.read();
        assertEquals(97, byte3);
        int byte4 = arInputStream.read();
        assertEquals(114, byte4);
        int byte5 = arInputStream.read();
        assertEquals(99, byte5);
        int byte6 = arInputStream.read();
        assertEquals(104, byte6);
        int byte7 = arInputStream.read();
        assertEquals(62, byte7);
        int byte8 = arInputStream.read();
        assertEquals(10, byte8);

        // Read the content
        byte[] content = new byte[100];
        int bytesRead = arInputStream.read(content);
        assertEquals(100, bytesRead);
        String contentString = new String(content);
        assertEquals("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla nec tortor eu erat placerat bibendum.", contentString);
    }

    @Test
    public void testReadWithOffset() throws IOException {
        byte[] buffer = new byte[20];
        int offset = 4;
        int bytesRead = arInputStream.read(buffer, offset, 10);
        assertEquals(10, bytesRead);

        for (int i = 0; i < offset; i++) {
            assertEquals(0, buffer[i]);
        }

        for (int i = offset; i < offset + bytesRead; i++) {
            assertEquals(0, buffer[i]);
        }
        
        for (int i = offset + bytesRead; i < buffer.length; i++) {
            assertEquals(0, buffer[i]);
        }
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[]{33, 60, 97, 114, 99, 104, 62, 10};
        assertTrue(ArArchiveInputStream.matches(signature, 8));

        byte[] partialSignature = new byte[]{33, 60, 97, 114};
        assertFalse(ArArchiveInputStream.matches(partialSignature, 4));

        byte[] invalidSignature = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};
        assertFalse(ArArchiveInputStream.matches(invalidSignature, 8));
    }
}

