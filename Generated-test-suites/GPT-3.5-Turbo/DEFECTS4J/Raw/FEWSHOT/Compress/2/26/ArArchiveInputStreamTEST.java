package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Before;
import org.junit.Test;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream arInputStream;

    @Before
    public void setUp() {
        // create a dummy input stream for testing
        byte[] dummyData = new byte[100];
        InputStream dummyInputStream = new ByteArrayInputStream(dummyData);
        arInputStream = new ArArchiveInputStream(dummyInputStream);
    }

    @Test
    public void testGetNextArEntry_success() throws IOException {
        ArArchiveEntry entry = arInputStream.getNextArEntry();
        assertNotNull(entry);
        assertEquals("testFileName", entry.getName());
        assertEquals(100, entry.getSize());
    }

    @Test
    public void testGetNextArEntry_eof() throws IOException {
        // create EOF condition
        arInputStream.read();

        ArArchiveEntry entry = arInputStream.getNextArEntry();
        assertNull(entry);
    }

    @Test(expected=IOException.class)
    public void testGetNextArEntry_invalidHeader() throws IOException {
        // create invalid header condition
        arInputStream.read(new byte[] { 0x0a, 0x0a, 0x0a, 0x0a });

        arInputStream.getNextArEntry();
    }

    @Test
    public void testGetNextEntry_success() throws IOException {
        ArchiveEntry entry = arInputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals("testFileName", entry.getName());
        assertEquals(100, entry.getSize());
    }

    @Test
    public void testClose() throws IOException {
        arInputStream.close();
        // verify that input stream is closed
    }

    @Test
    public void testRead() throws IOException {
        int data = arInputStream.read();
        // verify data read
    }

    @Test
    public void testReadBytes() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = arInputStream.read(buffer);
        // verify data read
    }

    @Test
    public void testReadBytesAndOffset() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = arInputStream.read(buffer, 0, buffer.length);
        // verify data read
    }

    @Test
    public void testMatches_true() {
        byte[] signature = new byte[] { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a };
        boolean result = ArArchiveInputStream.matches(signature, 8);
        assertEquals(true, result);
    }

    @Test
    public void testMatches_false() {
        byte[] signature = new byte[] { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x0a };
        boolean result = ArArchiveInputStream.matches(signature, 7);
        assertEquals(false, result);
    }
}