package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ArArchiveInputStreamTest {

    @Test
    public void testMatches() {
        final byte[] signature = new byte[]{ (byte) 0x21, (byte) 0x3c, (byte) 0x61, (byte) 0x72, (byte) 0x63, (byte) 0x68, (byte) 0x3e, (byte) 0x0a };
        assertTrue(ArArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testGetNextArEntryNullInput() {
        ArArchiveInputStream stream = new ArArchiveInputStream(null);
        assertNull(stream.getNextArEntry());
    }

    @Test
    public void testGetNextArEntryEmptyInput() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(ArArchiveEntry.HEADER);
        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        ArArchiveInputStream stream = new ArArchiveInputStream(input);
        assertNull(stream.getNextArEntry());
    }

    @Test
    public void testGetNextArEntryInvalidHeader() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x01, 0x02, 0x03}); // length is incorrect
        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        ArArchiveInputStream stream = new ArArchiveInputStream(input);
        assertNull(stream.getNextArEntry());
    }

    @Test
    public void testGetNextArEntryValidEntry() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(ArArchiveEntry.HEADER);
        output.write(new byte[]{"name".getBytes(), Long.TO_BYTE_ARRAY[0], Long.TO_BYTE_ARRAY[1], Long.TO_BYTE_ARRAY[2], Long.TO_BYTE_ARRAY[3], Long.TO_BYTE_ARRAY[4], Long.TO_BYTE_ARRAY[5], Long.TO_BYTE_ARRAY[6], Long.TO_BYTE_ARRAY[7], Long.TO_BYTE_ARRAY[8], Long.TO_BYTE_ARRAY[9]});
        output.write(ArArchiveEntry.TRAILER);
        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        ArArchiveInputStream stream = new ArArchiveInputStream(input);
        ArArchiveEntry entry = stream.getNextArEntry();
        assertEquals("name", entry.getName());
        assertEquals(10L, entry.getSize());
    }

    @Test
    public void testGetNextArEntryMultipleEntries() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(ArArchiveEntry.HEADER);
        output.write(new byte[]{"name1".getBytes(), Long.TO_BYTE_ARRAY[0], Long.TO_BYTE_ARRAY[1], Long.TO_BYTE_ARRAY[2], Long.TO_BYTE_ARRAY[3], Long.TO_BYTE_ARRAY[4], Long.TO_BYTE_ARRAY[5], Long.TO_BYTE_ARRAY[6], Long.TO_BYTE_ARRAY[7], Long.TO_BYTE_ARRAY[8], Long.TO_BYTE_ARRAY[9]});
        output.write(ArArchiveEntry.TRAILER);
        output.write(ArArchiveEntry.HEADER);
        output.write(new byte[]{"name2".getBytes(), Long.TO_BYTE_ARRAY[0], Long.TO_BYTE_ARRAY[1], Long.TO_BYTE_ARRAY[2], Long.TO_BYTE_ARRAY[3], Long.TO_BYTE_ARRAY[4], Long.TO_BYTE_ARRAY[5], Long.TO_BYTE_ARRAY[6], Long.TO_BYTE_ARRAY[7], Long.TO_BYTE_ARRAY[8], Long.TO_BYTE_ARRAY[9]});
        output.write(ArArchiveEntry.TRAILER);
        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        ArArchiveInputStream stream = new ArArchiveInputStream(input);
        ArArchiveEntry entry1 = stream.getNextArEntry();
        assertEquals("name1", entry1.getName());
        assertEquals(10L, entry1.getSize());
        ArArchiveEntry entry2 = stream.getNextArEntry();
        assertEquals("name2", entry2.getName());
        assertEquals(10L, entry2.getSize());
        assertNull(stream.getNextArEntry());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(ArArchiveEntry.HEADER);
        output.write(new byte[]{"name".getBytes(), Long.TO_BYTE_ARRAY[0], Long.TO_BYTE_ARRAY[1], Long.TO_BYTE_ARRAY[2], Long.TO_BYTE_ARRAY[3], Long.TO_BYTE_ARRAY[4], Long.TO_BYTE_ARRAY[5], Long.TO_BYTE_ARRAY[6], Long.TO_BYTE_ARRAY[7], Long.TO_BYTE_ARRAY[8], Long.TO_BYTE_ARRAY[9]});
        output.write(ArArchiveEntry.TRAILER);
        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        ArArchiveInputStream stream = new ArArchiveInputStream(input);
        ArchiveEntry entry = stream.getNextEntry();
        assertEquals(ArArchiveEntry.class, entry.getClass());
        ArArchiveEntry arEntry = (ArArchiveEntry) entry;
        assertEquals("name", arEntry.getName());
        assertEquals(10L, arEntry.getSize());
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        ArArchiveInputStream stream = new ArArchiveInputStream(input);
        stream.close();
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryAfterClose() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        ArArchiveInputStream stream = new ArArchiveInputStream(input);
        stream.close();
        stream.getNextArEntry();
    }

    @Test
    public void testRead() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        ArArchiveInputStream stream = new ArArchiveInputStream(input);
        int read = stream.read();
        assertEquals(-1, read);
    }

    @Test
    public void testReadArray() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        ArArchiveInputStream stream = new ArArchiveInputStream(input);
        byte[] buffer = new byte[1];
        int len = stream.read(buffer, 0, buffer.length);
        assertEquals(-1, len);
    }

    @Test
    public void testReadArrayWithData() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(new byte[]{0x01, 0x02, 0x03, 0x04});
        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        ArArchiveInputStream stream = new ArArchiveInputStream(input);
        byte[] buffer = new byte[4];
        int len = stream.read(buffer, 0, buffer.length);
        assertEquals(4, len);
        assertArrayEquals(new byte[]{0x01, 0x02, 0x03, 0x04}, buffer);
    }
}