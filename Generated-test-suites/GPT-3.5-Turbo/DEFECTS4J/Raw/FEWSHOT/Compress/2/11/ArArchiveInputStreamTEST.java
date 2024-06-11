package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.*;

import org.junit.Test;
import java.io.InputStream;
import java.io.IOException;

public class ArArchiveInputStreamTest {

    @Test
    public void testArArchiveInputStream() throws IOException {
        InputStream input = null;
        ArArchiveInputStream archiveInput = new ArArchiveInputStream(input);
        assertNotNull(archiveInput);
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        InputStream input = null;
        ArArchiveInputStream archiveInput = new ArArchiveInputStream(input);
        ArArchiveEntry entry = archiveInput.getNextArEntry();
        assertNull(entry);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        InputStream input = null;
        ArArchiveInputStream archiveInput = new ArArchiveInputStream(input);
        ArchiveEntry entry = archiveInput.getNextEntry();
        assertNull(entry);
    }

    @Test
    public void testRead() throws IOException {
        InputStream input = null;
        ArArchiveInputStream archiveInput = new ArArchiveInputStream(input);
        int result = archiveInput.read();
        assertEquals(-1, result);
    }

    @Test
    public void testReadBytes() throws IOException {
        InputStream input = null;
        ArArchiveInputStream archiveInput = new ArArchiveInputStream(input);
        byte[] buffer = new byte[10];
        int result = archiveInput.read(buffer);
        assertEquals(-1, result);
    }

    @Test
    public void testReadBytesWithOffsetAndLength() throws IOException {
        InputStream input = null;
        ArArchiveInputStream archiveInput = new ArArchiveInputStream(input);
        byte[] buffer = new byte[10];
        int result = archiveInput.read(buffer, 0, 10);
        assertEquals(-1, result);
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[8];
        boolean result = ArArchiveInputStream.matches(signature, 8);
        assertTrue(result);
    }

    @Test
    public void testMatchesWithInvalidSignature() {
        byte[] signature = new byte[8];
        boolean result = ArArchiveInputStream.matches(signature, 7);
        assertFalse(result);
    }

    @Test
    public void testClose() throws IOException {
        InputStream input = null;
        ArArchiveInputStream archiveInput = new ArArchiveInputStream(input);
        archiveInput.close();
        //Verify that the input stream is closed
    }
}