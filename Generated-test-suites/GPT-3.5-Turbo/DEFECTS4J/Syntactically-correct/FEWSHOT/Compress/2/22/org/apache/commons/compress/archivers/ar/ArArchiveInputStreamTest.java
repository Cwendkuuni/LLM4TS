
package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.junit.Test;

public class ArArchiveInputStreamTest {

    @Test
    public void testConstructor() {
        InputStream input = mock(InputStream.class);
        ArArchiveInputStream ar = new ArArchiveInputStream(input);
        assertEquals(input, ar.input);
        assertEquals(0, ar.offset);
        assertEquals(false, ar.closed);
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        InputStream input = new ByteArrayInputStream(
            "#1/1      0       0       100644  2                       `_\nDummy File".getBytes());
        ArArchiveInputStream ar = new ArArchiveInputStream(input);

        ArchiveEntry entry = ar.getNextEntry();
        assertEquals(ArArchiveEntry.class, entry.getClass());
        assertEquals("Dummy File", ((ArArchiveEntry) entry).getName());
        assertEquals(13, ((ArArchiveEntry) entry).getSize());
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntry_InvalidHeader() throws IOException {
        InputStream input = new ByteArrayInputStream(
            "not a valid header\nDummy File".getBytes());
        ArArchiveInputStream ar = new ArArchiveInputStream(input);

        ar.getNextEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntry_NotEnoughBytesToReadHeader() throws IOException {
        InputStream input = new ByteArrayInputStream(
            "#1/1".getBytes());
        ArArchiveInputStream ar = new ArArchiveInputStream(input);

        ar.getNextEntry();
    }

    @Test
    public void testGetNextArEntry_EOF() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream ar = new ArArchiveInputStream(input);

        ArchiveEntry entry = ar.getNextEntry();
        assertNull(entry);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        InputStream input = new ByteArrayInputStream(
            "#1/1      0       0       100644  2                       `_\nDummy File".getBytes());
        ArArchiveInputStream ar = new ArArchiveInputStream(input);

        ArchiveEntry entry = ar.getNextEntry();
        assertEquals(ArArchiveEntry.class, entry.getClass());
        assertEquals("Dummy File", ((ArArchiveEntry) entry).getName());
        assertEquals(13, ((ArArchiveEntry) entry).getSize());
    }

    @Test(expected = IOException.class)
    public void testGetNextEntry_InvalidHeader() throws IOException {
        InputStream input = new ByteArrayInputStream(
            "not a valid header\nDummy File".getBytes());
        ArArchiveInputStream ar = new ArArchiveInputStream(input);

        ar.getNextEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextEntry_NotEnoughBytesToReadHeader() throws IOException {
        InputStream input = new ByteArrayInputStream(
            "#1/1".getBytes());
        ArArchiveInputStream ar = new ArArchiveInputStream(input);

        ar.getNextEntry();
    }

    @Test
    public void testClose() throws IOException {
        InputStream input = mock(InputStream.class);
        ArArchiveInputStream ar = new ArArchiveInputStream(input);

        ar.close();
    }

    @Test
    public void testRead() throws IOException {
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        ArArchiveInputStream ar = new ArArchiveInputStream(input);

        int result = ar.read();
        assertEquals('T', result);
    }

    @Test
    public void testRead_WithByteArray() throws IOException {
        byte[] buffer = new byte[5];
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        ArArchiveInputStream ar = new ArArchiveInputStream(input);

        int result = ar.read(buffer);
        assertEquals(5, result);
        assertEquals('T', buffer[0]);
        assertEquals('e', buffer[1]);
        assertEquals('s', buffer[2]);
        assertEquals('t', buffer[3]);
        assertEquals(' ', buffer[4]);
    }

    @Test
    public void testRead_WithByteArray_OffsetAndLength() throws IOException {
        byte[] buffer = new byte[5];
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        ArArchiveInputStream ar = new ArArchiveInputStream(input);

        int result = ar.read(buffer, 1, 3);
        assertEquals(3, result);
        assertEquals(0, buffer[0]);
        assertEquals('T', buffer[1]);
        assertEquals('e', buffer[2]);
        assertEquals('s', buffer[3]);
        assertEquals(0, buffer[4]);
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[8];

        signature[0] = 0x21;
        signature[1] = 0x3c;
        signature[2] = 0x61;
        signature[3] = 0x72;
        signature[4] = 0x63;
        signature[5] = 0x68;
        signature[6] = 0x3e;
        signature[7] = 0x0a;

        boolean result = ArArchiveInputStream.matches(signature, signature.length);
        assertEquals(true, result);
    }
}
