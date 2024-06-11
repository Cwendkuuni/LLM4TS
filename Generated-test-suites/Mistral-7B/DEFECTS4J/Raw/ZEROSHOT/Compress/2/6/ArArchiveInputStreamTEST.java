package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarEntry;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test class for ArArchiveInputStream
 */
public class ArArchiveInputStreamTest {

    private InputStream inputStream;
    private ArArchiveInputStream arArchiveInputStream;

    @Before
    public void setup() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(ArArchiveEntry.HEADER);
        outputStream.write(new byte[]{"name".getBytes(), 0});
        outputStream.write(new byte[]{"1995-05-04".getBytes()});
        outputStream.write(new byte[]{"user".getBytes(), 0});
        outputStream.write(new byte[]{"group".getBytes(), 0});
        outputStream.write(new byte[]{(byte) 0644, 0});
        outputStream.write(new byte[]{"length".getBytes(), 0});
        outputStream.write(ArArchiveEntry.TRAILER);
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        arArchiveInputStream = new ArArchiveInputStream(inputStream);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry entry = arArchiveInputStream.getNextEntry();
        assertNotNull(entry);
        assertTrue(entry instanceof ArArchiveEntry);
        ArArchiveEntry arEntry = (ArArchiveEntry) entry;
        assertEquals("name", arEntry.getName());
        assertEquals(14L, arEntry.getSize());
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry entry = (ArArchiveEntry) arArchiveInputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals("name", entry.getName());
        assertEquals(14L, entry.getSize());
    }

    @Test(expected = IOException.class)
    public void testNoMoreEntries() throws IOException {
        arArchiveInputStream.getNextEntry();
        arArchiveInputStream.getNextEntry();
        arArchiveInputStream.getNextEntry();
        arArchiveInputStream.getNextEntry();
        arArchiveInputStream.getNextEntry();
        arArchiveInputStream.getNextEntry();
        arArchiveInputStream.getNextEntry();
        arArchiveInputStream.getNextEntry();
        arArchiveInputStream.getNextEntry();
        arArchiveInputStream.getNextEntry();
        arArchiveInputStream.getNextEntry();
        arArchiveInputStream.getNextEntry();
        arArchiveInputStream.getNextEntry();
        arArchiveInputStream.getNextEntry();
        arArchiveInputStream.getNextEntry();
        arArchiveInputStream.getNextEntry();
        arArchiveInputStream.getNextEntry();
        arArchiveInputStream.getNextEntry();
        assertEquals(0, arArchiveInputStream.getNextEntry().getSize());
        arArchiveInputStream.getNextEntry();
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[20];
        signature[0] = 0x3c;
        signature[1] = 0x21;
        signature[2] = 0x72;
        signature[3] = 0x61;
        signature[4] = 0x68;
        signature[5] = 0x63;
        signature[6] = 0x0a;
        signature[7] = 0x3e;
        assertFalse(ArArchiveInputStream.matches(signature, 1));
        assertFalse(ArArchiveInputStream.matches(signature, 5));
        assertTrue(ArArchiveInputStream.matches(signature, 21));
        assertTrue(ArArchiveInputStream.matches(signature, 20));
    }

    @Test
    public void testClose() throws IOException {
        arArchiveInputStream.close();
        assertFalse(inputStream.isOpen());
    }
}