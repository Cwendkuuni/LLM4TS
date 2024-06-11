
package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream ar;

    @Before
    public void setUp() throws Exception {
        // TODO: Initialize the input stream with test data

        // Example test data
        byte[] testData = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        InputStream inputStream = new ByteArrayInputStream(testData);
        ar = new ArArchiveInputStream(inputStream);
    }

    @After
    public void tearDown() throws Exception {
        // TODO: Cleanup resources
        ar.close();
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        // TODO: Test getNextArEntry() method

        ArArchiveEntry entry = ar.getNextArEntry();
        assertNotNull(entry);
        assertEquals("test entry", entry.getName());
        assertEquals(10, entry.getLength());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        // TODO: Test getNextEntry() method

        ArchiveEntry entry = ar.getNextEntry();
        assertNotNull(entry);
        assertTrue(entry instanceof ArArchiveEntry);
        assertEquals("test entry", entry.getName());
        assertEquals(10, entry.getLength());
    }

    @Test
    public void testRead() throws IOException {
        // TODO: Test read() method

        int dataByte = ar.read();
        assertEquals(0x21, dataByte);
    }

    @Test
    public void testReadByteArray() throws IOException {
        // TODO: Test read(byte[] b) method

        byte[] data = new byte[10];
        int bytesRead = ar.read(data);

        assertEquals(8, bytesRead);
        assertArrayEquals(new byte[] {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x00, 0x00}, data);
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        // TODO: Test read(byte[] b, int off, int len) method

        byte[] data = new byte[10];
        int bytesRead = ar.read(data, 2, 6);

        assertEquals(6, bytesRead);
        assertArrayEquals(new byte[] {0x00, 0x00, 0x21, 0x3c, 0x61, 0x72, 0x63, 0x00, 0x00, 0x00}, data);
    }

    @Test
    public void testMatches() {
        // TODO: Test matches() method

        byte[] signature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        assertTrue(ArArchiveInputStream.matches(signature, 8));
    }

    @Test
    public void testClose() throws IOException {
        // TODO: Test close() method

        assertFalse(ar.isClosed());
        ar.close();
        assertTrue(ar.isClosed());
    }
}
