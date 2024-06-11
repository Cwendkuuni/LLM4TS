package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream arInputStream;
    private InputStream mockInputStream;

    @Before
    public void setUp() {
        mockInputStream = mock(InputStream.class);
        arInputStream = new ArArchiveInputStream(mockInputStream);
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        // Test when offset is 0
        when(mockInputStream.read(any(byte[].class))).thenReturn(8);
        ArArchiveEntry entry = arInputStream.getNextArEntry();
        assertEquals("header", entry.getName());
        assertEquals(0, entry.getLength());

        // Test when input is available
        when(mockInputStream.available()).thenReturn(1);
        entry = arInputStream.getNextArEntry();
        assertEquals("file", entry.getName());
        assertEquals(10, entry.getLength());

        // Test when input is not available
        when(mockInputStream.available()).thenReturn(0);
        entry = arInputStream.getNextArEntry();
        assertNull(entry);

        verify(mockInputStream, times(1)).read(any(byte[].class));
        verify(mockInputStream, times(2)).available();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        // Test when offset is 0
        when(mockInputStream.read(any(byte[].class))).thenReturn(8);
        ArArchiveEntry entry = arInputStream.getNextEntry();
        assertEquals("header", entry.getName());
        assertEquals(0, entry.getLength());

        // Test when input is available
        when(mockInputStream.available()).thenReturn(1);
        entry = arInputStream.getNextEntry();
        assertEquals("file", entry.getName());
        assertEquals(10, entry.getLength());

        // Test when input is not available
        when(mockInputStream.available()).thenReturn(0);
        entry = arInputStream.getNextEntry();
        assertNull(entry);

        verify(mockInputStream, times(1)).read(any(byte[].class));
        verify(mockInputStream, times(2)).available();
    }

    @Test
    public void testClose() throws IOException {
        arInputStream.close();

        verify(mockInputStream, times(1)).close();
    }

    @Test
    public void testRead() throws IOException {
        when(mockInputStream.read()).thenReturn(65);

        int result = arInputStream.read();
        assertEquals(65, result);

        verify(mockInputStream, times(1)).read();
    }

    @Test
    public void testReadArray() throws IOException {
        byte[] byteArray = new byte[5];
        when(mockInputStream.read(byteArray)).thenReturn(5);

        int result = arInputStream.read(byteArray);
        assertEquals(5, result);

        verify(mockInputStream, times(1)).read(byteArray);
    }

    @Test
    public void testReadArrayWithOffsetAndLength() throws IOException {
        byte[] byteArray = new byte[5];
        when(mockInputStream.read(byteArray, 1, 3)).thenReturn(3);

        int result = arInputStream.read(byteArray, 1, 3);
        assertEquals(3, result);

        verify(mockInputStream, times(1)).read(byteArray, 1, 3);
    }

    @Test
    public void testMatches() {
        byte[] signature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};

        boolean result = ArArchiveInputStream.matches(signature, 8);
        assertEquals(true, result);
    }
}