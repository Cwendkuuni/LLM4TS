package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.junit.Test;

public class ArArchiveInputStreamTest {

    @Test
    public void testMatches() {
        final byte[] header = new byte[] { (byte) 0x3c, (byte) 0x21, (byte) 0x72, (byte) 0x61, (byte) 0x68, (byte) 0x63, (byte) 0x0a, (byte) 0x3e };
        assertTrue(ArArchiveInputStream.matches(header, header.length));
    }

    @Test
    public void testGetNextArEntryNullInput() {
        final InputStream input = null;
        try {
            final ArArchiveInputStream stream = new ArArchiveInputStream(input);
            assertNull(stream.getNextArEntry());
        } catch (final IOException e) {
            // expected
        }
    }

    @Test
    public void testGetNextArEntryEmptyInput() throws IOException {
        final byte[] inputData = new byte[0];
        final ByteArrayInputStream input = new ByteArrayInputStream(inputData);
        final ArArchiveInputStream stream = new ArArchiveInputStream(input);
        assertNull(stream.getNextArEntry());
    }

    @Test
    public void testGetNextArEntryInvalidInput() throws IOException {
        final byte[] inputData = new byte[] { 0x3c, 0x21, 0x72, 0x61, 0x68, 0x63, 0x0a, 0x3e, 0x01 };
        final ByteArrayInputStream input = new ByteArrayInputStream(inputData);
        final ArArchiveInputStream stream = new ArArchiveInputStream(input);
        assertNull(stream.getNextArEntry());
    }

    @Test
    public void testGetNextArEntryValidEntry() throws IOException {
        final byte[] inputData = new byte[] { 0x3c, 0x21, 0x72, 0x61, 0x68, 0x63, 0x0a, 0x41, 0x62, 0x6d, 0x65, 0x72, 0x73, 0x65, 0x74, 0x2d, 0x75, 0x73, 0x69, 0x6e, 0x67, 0x3e, 0x0a, 0x53, 0x74, 0x61, 0x72, 0x79, 0x2d, 0x63, 0x6f, 0x6d, 0x70, 0x6f, 0x63, 0x65, 0x64, 0x0a, 0x41, 0x62, 0x6d, 0x65, 0x72, 0x73, 0x65, 0x74, 0x2d, 0x63, 0x6f, 0x6e, 0x66, 0x66, 0x66, 0x65, 0x3e, 0x0a };
        final ByteArrayInputStream input = new ByteArrayInputStream(inputData);
        final ArArchiveInputStream stream = new ArArchiveInputStream(input);
        final ArArchiveEntry entry = stream.getNextArEntry();
        assertTrue(entry instanceof ArArchiveEntry);
        assertTrue(entry.getName().equals("foo-bar.txt"));
        assertTrue(entry.getSize() == 12345);
    }

    @Test
    public void testGetNextArEntryEndOfStream() throws IOException {
        final byte[] inputData = new byte[] { 0x3c, 0x21, 0x72, 0x61, 0x68, 0x63, 0x0a, 0x41, 0x62, 0x6d, 0x65, 0x72, 0x73, 0x65, 0x74, 0x2d, 0x75, 0x73, 0x69, 0x6e, 0x67, 0x3e, 0x0a, 0x53, 0x74, 0x61, 0x72, 0x79, 0x2d, 0x63, 0x6f, 0x6d, 0x70, 0x6f, 0x63, 0x65, 0x64, 0x0a, 0x41, 0x62, 0x6d, 0x65, 0x72, 0x73, 0x65, 0x74, 0x2d, 0x63, 0x6f, 0x6e, 0x66, 0x66, 0x66, 0x65, 0x3e, 0x0a, 0x00 };
        final ByteArrayInputStream input = new ByteArrayInputStream(inputData);
        final ArArchiveInputStream stream = new ArArchiveInputStream(input);
        final ArArchiveEntry entry = stream.getNextArEntry();
        assertNull(entry);
    }
}