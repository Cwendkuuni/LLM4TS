package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Before;
import org.junit.Test;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream inputStream;
    private InputStream mockInputStream;

    @Before
    public void setup() throws IOException {
        mockInputStream = mock(InputStream.class);
        inputStream = new ArArchiveInputStream(mockInputStream);
    }

    @Test
    public void testGetNextArEntry_HeaderReadCorrectly() throws IOException {
        byte[] expectedHeader = ArArchiveEntry.HEADER.getBytes();
        byte[] realizedHeader = new byte[expectedHeader.length];
        
        when(mockInputStream.read(realizedHeader)).thenReturn(expectedHeader.length);
        when(mockInputStream.available()).thenReturn(1);
        
        ArArchiveEntry entry = inputStream.getNextArEntry();
        
        verify(mockInputStream).read(realizedHeader);
        
        for (int i = 0; i < expectedHeader.length; i++) {
            assertEquals(expectedHeader[i], realizedHeader[i]);
        }
    }

    @Test(expected=IOException.class)
    public void testGetNextArEntry_HeaderReadFails() throws IOException {
        byte[] expectedHeader = ArArchiveEntry.HEADER.getBytes();
        byte[] realizedHeader = new byte[expectedHeader.length];
        
        when(mockInputStream.read(realizedHeader)).thenReturn(4);
        
        inputStream.getNextArEntry();
    }

    @Test
    public void testGetNextArEntry_EntryReadSuccessfully() throws IOException {
        byte[] entryHeader = "test_entry".getBytes();
        byte[] entryContent = "test_content".getBytes();
        byte[] trailer = ArArchiveEntry.TRAILER.getBytes();
        
        when(mockInputStream.read()).thenReturn(0); // Ensure offset is initialized to 0
        when(mockInputStream.available()).thenReturn(1, 1, 1, 1, 1, 1, 1, 0); // Return 1 byte available on each read
        when(mockInputStream.read(entryHeader)).thenReturn(entryHeader.length);
        when(mockInputStream.read(any(byte[].class), anyInt(), anyInt())).thenReturn(entryContent.length);
        when(mockInputStream.read(trailer)).thenReturn(trailer.length);
        
        ArArchiveEntry entry = inputStream.getNextArEntry();
        
        verify(mockInputStream, times(8)).read();
        verify(mockInputStream).read(entryHeader);
        verify(mockInputStream).read(entryContent, 0, entryContent.length);
        verify(mockInputStream).read(trailer);
        
        assertEquals("test_entry", entry.getName());
        assertEquals(entryContent.length, entry.getSize());
    }

    @Test
    public void testGetNextArEntry_NoEntryAvailable() throws IOException {
        when(mockInputStream.available()).thenReturn(0);
        
        ArArchiveEntry entry = inputStream.getNextArEntry();
        
        assertNull(entry);
    }

    @Test
    public void testGetNextEntry_CallsGetNextArEntry() throws IOException {
        inputStream.getNextEntry();
        
        verify(mockInputStream).available();
        verify(mockInputStream, never()).read();
        verify(mockInputStream, never()).read(any(byte[].class));
        verify(mockInputStream, never()).read(any(byte[].class), anyInt(), anyInt());
    }

    @Test
    public void testClose_ClosesInputStream() throws IOException {
        inputStream.close();
        
        verify(mockInputStream).close();
    }

    @Test
    public void testRead_ReadsOneByte() throws IOException {
        when(mockInputStream.read()).thenReturn(1);
        
        int result = inputStream.read();
        
        assertEquals(1, result);
    }

    @Test
    public void testRead_ReadsByteArray() throws IOException {
        byte[] buffer = new byte[5];
        
        when(mockInputStream.read(buffer)).thenReturn(buffer.length);
        
        int result = inputStream.read(buffer);
        
        assertEquals(buffer.length, result);
    }

    @Test
    public void testRead_ReadsByteArrayWithOffsetAndLength() throws IOException {
        byte[] buffer = new byte[10];
        int offset = 2;
        int length = 5;
        
        when(mockInputStream.read(buffer, offset, length)).thenReturn(length);
        
        int result = inputStream.read(buffer, offset, length);
        
        assertEquals(length, result);
    }

    @Test
    public void testMatches_TrueWhenSignatureMatches() {
        byte[] signature = { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a };
        int length = 8;
        
        boolean result = ArArchiveInputStream.matches(signature, length);
        
        assertEquals(true, result);
    }

    @Test
    public void testMatches_FalseWhenSignatureDoesNotMatch() {
        byte[] signature = { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x0a };
        int length = 9;
        
        boolean result = ArArchiveInputStream.matches(signature, length);
        
        assertEquals(false, result);
    }
}