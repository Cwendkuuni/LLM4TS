package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        // Test with empty input stream
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream emptyInput = new ArArchiveInputStream(emptyInputStream);
        assertNull(emptyInput.getNextArEntry());

        // Test with valid input stream
        byte[] header = new byte[] { 33, 60, 97, 114, 99, 104, 62, 10 };
        byte[] entryName = new byte[] { 102, 111, 111 };
        byte[] entryLength = new byte[] { 49, 48 };
        byte[] trailer = new byte[] { 33, 10 };

        byte[] inputBytes = new byte[header.length + entryName.length + entryLength.length + trailer.length];
        System.arraycopy(header, 0, inputBytes, 0, header.length);
        System.arraycopy(entryName, 0, inputBytes, header.length, entryName.length);
        System.arraycopy(entryLength, 0, inputBytes, header.length + entryName.length, entryLength.length);
        System.arraycopy(trailer, 0, inputBytes, header.length + entryName.length + entryLength.length, trailer.length);

        InputStream input = new ByteArrayInputStream(inputBytes);
        ArArchiveInputStream arInput = new ArArchiveInputStream(input);

        ArArchiveEntry entry = arInput.getNextArEntry();
        assertEquals("foo", entry.getName());
        assertEquals(10, entry.getLength());
        assertNull(arInput.getNextArEntry());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        // Test with empty input stream
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream emptyInput = new ArArchiveInputStream(emptyInputStream);
        assertNull(emptyInput.getNextEntry());

        // Test with valid input stream
        byte[] header = new byte[] { 33, 60, 97, 114, 99, 104, 62, 10 };
        byte[] entryName = new byte[] { 102, 111, 111 };
        byte[] entryLength = new byte[] { 49, 48 };
        byte[] trailer = new byte[] { 33, 10 };

        byte[] inputBytes = new byte[header.length + entryName.length + entryLength.length + trailer.length];
        System.arraycopy(header, 0, inputBytes, 0, header.length);
        System.arraycopy(entryName, 0, inputBytes, header.length, entryName.length);
        System.arraycopy(entryLength, 0, inputBytes, header.length + entryName.length, entryLength.length);
        System.arraycopy(trailer, 0, inputBytes, header.length + entryName.length + entryLength.length, trailer.length);

        InputStream input = new ByteArrayInputStream(inputBytes);
        ArArchiveInputStream arInput = new ArArchiveInputStream(input);

        ArchiveEntry entry = arInput.getNextEntry();
        assertEquals("foo", entry.getName());
        assertEquals(10, entry.getLength());
        assertNull(arInput.getNextEntry());
    }

    @Test
    public void testClose() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream arInput = new ArArchiveInputStream(inputStream);
        arInput.close();
        assertNull(arInput.getNextEntry());
    }

    @Test
    public void testRead() throws IOException {
        byte[] inputBytes = new byte[] { 0x41, 0x42, 0x43 };
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        ArArchiveInputStream arInput = new ArArchiveInputStream(inputStream);
        assertEquals(0x41, arInput.read());
        assertEquals(0x42, arInput.read());
        assertEquals(0x43, arInput.read());
        assertEquals(-1, arInput.read());
    }

    @Test
    public void testReadArray() throws IOException {
        byte[] inputBytes = new byte[] { 0x41, 0x42, 0x43 };
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        ArArchiveInputStream arInput = new ArArchiveInputStream(inputStream);
        byte[] buffer = new byte[2];
        int bytesRead = arInput.read(buffer);
        assertEquals(2, bytesRead);
        assertEquals(0x41, buffer[0]);
        assertEquals(0x42, buffer[1]);
    }

    @Test
    public void testReadArrayWithOffset() throws IOException {
        byte[] inputBytes = new byte[] { 0x41, 0x42, 0x43 };
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        ArArchiveInputStream arInput = new ArArchiveInputStream(inputStream);
        byte[] buffer = new byte[3];
        int bytesRead = arInput.read(buffer, 1, 2);
        assertEquals(2, bytesRead);
        assertEquals(0x00, buffer[0]);
        assertEquals(0x41, buffer[1]);
        assertEquals(0x42, buffer[2]);
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[] { 33, 60, 97, 114, 99, 104, 62, 10 };
        assertTrue(ArArchiveInputStream.matches(signature, signature.length));
    }

}