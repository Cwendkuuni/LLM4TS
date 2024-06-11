package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

    @Test
    public void testMatches() {
        byte[] signature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        assertTrue(ArArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testGetNextArEntryNullInput() {
        ArArchiveInputStream inputStream = new ArArchiveInputStream(null);
        assertNull(inputStream.getNextArEntry());
    }

    @Test
    public void testGetNextArEntryEmptyInput() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream inputStream = new ArArchiveInputStream(input);
        assertNull(inputStream.getNextArEntry());
    }

    @Test
    public void testGetNextArEntryInvalidHeader() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0b, 0x01});
        ArArchiveInputStream inputStream = new ArArchiveInputStream(input);
        assertNull(inputStream.getNextArEntry());
    }

    @Test
    public void testGetNextArEntryValidEntry() throws IOException {
        String name = "test.txt";
        long length = 10;
        InputStream input = new ByteArrayInputStream(new byte[]{
                0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a,
                0x00, 0x14, (byte) name.charAt(0), (byte) name.charAt(1),
                0x00, (byte) name.length() - 2,
                0x68, 0x74, 0x6d, 0x6c, 0x3e, 0x0a,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0a,
                0x00, 0x00, 0x00, 0x09, 0x00, 0x00, 0x0a, 0x00,
                0x00, 0x00, 0x00, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x3c
        });
        ArArchiveInputStream inputStream = new ArArchiveInputStream(input);
        ArArchiveEntry entry = inputStream.getNextArEntry();
        assertTrue(entry instanceof ArArchiveEntry);
        ArArchiveEntry arEntry = (ArArchiveEntry) entry;
        assertEquals(name, arEntry.getName());
        assertEquals(length, arEntry.getSize());

        input.reset();
        entry = inputStream.getNextArEntry();
        assertNull(entry);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        testGetNextArEntryValidEntry();
    }

    @Test
    public void testClose() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream inputStream = new ArArchiveInputStream(input);
        inputStream.close();
        input.close();
    }

    @Test
    public void testRead() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream inputStream = new ArArchiveInputStream(input);
        byte[] buffer = new byte[1];
        int bytesRead = inputStream.read(buffer);
        assertEquals(-1, bytesRead);
        input.close();
    }

    @Test
    public void testReadBytes() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{0x01});
        ArArchiveInputStream inputStream = new ArArchiveInputStream(input);
        byte[] buffer = new byte[1];
        int bytesRead = inputStream.read(buffer);
        assertEquals(1, bytesRead);
        assertEquals((byte) 0x01, buffer[0]);
        input.close();
    }

    @Test
    public void testReadBytesArray() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        ArArchiveInputStream inputStream = new ArArchiveInputStream(input);
        byte[] buffer = new byte[3];
        int bytesRead = inputStream.read(buffer);
        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[]{0x01, 0x02, 0x03}, buffer);
        input.close();
    }

    @Test
    public void testReadBytesArrayPartial() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        ArArchiveInputStream inputStream = new ArArchiveInputStream(input);
        byte[] buffer = new byte[2];
        int bytesRead = inputStream.read(buffer);
        assertEquals(2, bytesRead);
        assertArrayEquals(new byte[]{0x01, 0x02}, buffer);
        input.close();
    }
}