package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Before;
import org.junit.Test;

public class ArArchiveInputStreamTest {
    private ArArchiveInputStream archiveInputStream;
    private InputStream mockInputStream;

    @Before
    public void setup() {
        mockInputStream = mock(InputStream.class);
        archiveInputStream = new ArArchiveInputStream(mockInputStream);
    }

    @Test
    public void testGetNextArEntry_ValidEntries() throws IOException {
        byte[] headerBytes = "#!<arch>\n".getBytes();
        byte[] nameBytes = "file".getBytes();
        byte[] lengthBytes = "6          ".getBytes();
        byte[] trailerBytes = "`\n".getBytes();

        when(mockInputStream.read())
                .thenReturn(headerBytes[0])
                .thenReturn(headerBytes[1])
                .thenReturn(headerBytes[2])
                .thenReturn(headerBytes[3])
                .thenReturn(headerBytes[4])
                .thenReturn(headerBytes[5])
                .thenReturn(headerBytes[6])
                .thenReturn(headerBytes[7])
                .thenReturn(headerBytes[8]);

        when(mockInputStream.available())
                .thenReturn(1)
                .thenReturn(0);

        when(mockInputStream.read(nameBytes))
                .thenReturn(3)
                .thenReturn(0);

        when(mockInputStream.read(lengthBytes))
                .thenReturn(4)
                .thenReturn(0);

        when(mockInputStream.read(trailerBytes))
                .thenReturn(2)
                .thenReturn(0);

        ArArchiveEntry entry = archiveInputStream.getNextArEntry();

        assertEquals("file", entry.getName());
        assertEquals(6, entry.getLength());
        assertNull(archiveInputStream.getNextArEntry());
    }

    @Test
    public void testGetNextArEntry_InvalidHeader() throws IOException {
        byte[] headerBytes = "#!invalid\n".getBytes();

        when(mockInputStream.read())
                .thenReturn(headerBytes[0])
                .thenReturn(headerBytes[1])
                .thenReturn(headerBytes[2])
                .thenReturn(headerBytes[3])
                .thenReturn(headerBytes[4])
                .thenReturn(headerBytes[5])
                .thenReturn(headerBytes[6])
                .thenReturn(headerBytes[7])
                .thenReturn(headerBytes[8]);

        IOException exception = null;
        try {
            archiveInputStream.getNextArEntry();
        } catch (IOException e) {
            exception = e;
        }

        assertEquals("invalid header #!invalid", exception.getMessage());
    }

    @Test
    public void testGetNextArEntry_NullEntry() throws IOException {
        when(mockInputStream.available())
                .thenReturn(0);

        assertNull(archiveInputStream.getNextArEntry());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArArchiveEntry entry = archiveInputStream.getNextEntry();

        assertEquals(entry, archiveInputStream.getNextArEntry());
    }

    @Test
    public void testClose() throws IOException {
        archiveInputStream.close();

        assertNull(archiveInputStream.getNextArEntry());
    }

    @Test
    public void testRead() throws IOException {
        when(mockInputStream.read())
                .thenReturn(1)
                .thenReturn(-1);

        assertEquals(1, archiveInputStream.read());
        assertEquals(-1, archiveInputStream.read());
    }

    @Test
    public void testRead_ByteArray() throws IOException {
        byte[] buffer = new byte[10];

        when(mockInputStream.read(buffer))
                .thenReturn(5)
                .thenReturn(-1);

        assertEquals(5, archiveInputStream.read(buffer));
        assertEquals(-1, archiveInputStream.read(buffer));
    }

    @Test
    public void testRead_ByteArray_Offset_Length() throws IOException {
        byte[] buffer = new byte[10];

        when(mockInputStream.read(buffer, 0, 5))
                .thenReturn(5)
                .thenReturn(-1);

        assertEquals(5, archiveInputStream.read(buffer, 0, 5));
        assertEquals(-1, archiveInputStream.read(buffer, 0, 5));
    }

    @Test
    public void testMatches() {
        byte[] validSignature = {'#', '!', '<', 'a', 'r', 'c', 'h', '>', '\n'};
        byte[] invalidSignature = {'#', '!', '<', 'i', 'n', 'v', 'a', 'l', 'i', 'd'};

        assertTrue(ArArchiveInputStream.matches(validSignature, validSignature.length));
        assertFalse(ArArchiveInputStream.matches(invalidSignature, invalidSignature.length));
    }
}