package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

    @Test
    public void testMatches() {
        byte[] signature = new byte[]{0x3c, 0x21, 0x72, 0x61, 0x68, 0x63, 0x0a, 0x3e};
        assertNotNull(ArArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testGetNextArEntryNullInput() {
        InputStream input = null;
        ArArchiveInputStream arStream = new ArArchiveInputStream(input);
        ArArchiveEntry entry = arStream.getNextArEntry();
        assertNull(entry);
    }

    @Test
    public void testGetNextArEntryEmptyInput() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream arStream = new ArArchiveInputStream(input);
        ArArchiveEntry entry = arStream.getNextArEntry();
        assertNull(entry);
    }

    @Test
    public void testGetNextArEntryInvalidHeader() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(ArArchiveEntry.HEADER.getBytes());
        outputStream.write(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08});
        InputStream input = new ByteArrayInputStream(outputStream.toByteArray());
        ArArchiveInputStream arStream = new ArArchiveInputStream(input);
        ArArchiveEntry entry = arStream.getNextArEntry();
        assertNull(entry);
    }

    @Test
    public void testGetNextArEntryValidEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(ArArchiveEntry.HEADER.getBytes());
        outputStream.write(ArArchiveEntry.TRAILER.getBytes());
        byte[] name = "test.txt".getBytes();
        byte[] lengthBytes = Long.toString(name.length + 16).getBytes();

        outputStream.write(name, 0, name.length);
        outputStream.write(lengthBytes, 0, lengthBytes.length);

        InputStream input = new ByteArrayInputStream(outputStream.toByteArray());
        ArArchiveInputStream arStream = new ArArchiveInputStream(input);
        ArArchiveEntry entry = arStream.getNextArEntry();
        assertNotNull(entry);
        assertEquals("test.txt", entry.getName());
        assertEquals(name.length + 16, entry.getSize());
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryIncompleteEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(ArArchiveEntry.HEADER.getBytes());
        outputStream.write(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f});
        InputStream input = new ByteArrayInputStream(outputStream.toByteArray());
        ArArchiveInputStream arStream = new ArArchiveInputStream(input);
        arStream.getNextArEntry();
    }

    @Test
    public void testGetNextArEntryEndOfStream() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(ArArchiveEntry.HEADER.getBytes());
        outputStream.write(ArArchiveEntry.TRAILER.getBytes());
        InputStream input = new ByteArrayInputStream(outputStream.toByteArray());
        ArArchiveInputStream arStream = new ArArchiveInputStream(input);
        ArArchiveEntry entry = arStream.getNextArEntry();
        assertNotNull(entry);
        ArArchiveEntry nextEntry = arStream.getNextArEntry();
        assertNull(nextEntry);
    }

    @Test
    public void testGetNextArEntryMultipleEntries() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(ArArchiveEntry.HEADER.getBytes());
        outputStream.write(ArArchiveEntry.TRAILER.getBytes());

        byte[] name1 = "test1.txt".getBytes();
        byte[] lengthBytes1 = Long.toString(name1.length + 16).getBytes();
        outputStream.write(name1, 0, name1.length);
        outputStream.write(lengthBytes1, 0, lengthBytes1.length);

        byte[] name2 = "test2.txt".getBytes();
        byte[] lengthBytes2 = Long.toString(name2.length + 16).getBytes();
        outputStream.write(name2, 0, name2.length);
        outputStream.write(lengthBytes2, 0, lengthBytes2.length);

        InputStream input = new ByteArrayInputStream(outputStream.toByteArray());
        ArArchiveInputStream arStream = new ArArchiveInputStream(input);
        ArArchiveEntry entry1 = arStream.getNextArEntry();
        ArArchiveEntry entry2 = arStream.getNextArEntry();
        ArArchiveEntry entry3 = arStream.getNextArEntry();
        assertNotNull(entry1);
        assertNotNull(entry2);
        assertNull(entry3);
    }
}