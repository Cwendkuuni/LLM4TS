package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarEntry;
import org.junit.Test;

/**
 * Test suite for {@link ArArchiveInputStream}.
 */
public class ArArchiveInputStreamTest {

    @Test
    public void testArArchiveInputStreamConstructor() {
        final InputStream input = new ByteArrayInputStream(new byte[0]);
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        assertEquals(input, arArchiveInputStream.input);
    }

    @Test
    public void testGetNextArEntryNoEntryFound() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[0]);
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        assertNull(arArchiveInputStream.getNextArEntry());
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        final String header = "!<arch>\n" +
                             "  Name        Type      Size ModTime  Uid   Gid\n" +
                             "filename.txt  data     1024 2001-01-01 01:01:01 1001 1001\n" +
                             "!<trailer>\n";
        final InputStream input = new ByteArrayInputStream(header.getBytes());
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        final ArArchiveEntry arArchiveEntry = arArchiveInputStream.getNextArEntry();
        assertEquals("filename.txt", arArchiveEntry.getName());
        assertEquals(1024L, arArchiveEntry.getSize());
    }

    @Test
    public void testGetNextArEntryWithInvalidEntryHeader() throws IOException {
        final String header = "!<arch>\n" +
                             "  Name        Type      Size ModTime  Uid   Gid\n" +
                             "filename.txt  data     1024 2001-01-01 01:01:01 1001 1001\n" +
                             "!<trailer>\n" +
                             "!<trailer>\n";
        final InputStream input = new ByteArrayInputStream(header.getBytes());
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        assertThrows(IOException.class, () -> arArchiveInputStream.getNextArEntry());
    }

    @Test
    public void testGetNextArEntryWithMissingTrailer() throws IOException {
        final String header = "!<arch>\n" +
                             "  Name        Type      Size ModTime  Uid   Gid\n" +
                             "filename.txt  data     1024 2001-01-01 01:01:01 1001 1001\n";
        final InputStream input = new ByteArrayInputStream(header.getBytes());
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        assertNull(arArchiveInputStream.getNextArEntry());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        final String header = "!<arch>\n" +
                             "  Name        Type      Size ModTime  Uid   Gid\n" +
                             "filename.txt  data     1024 2001-01-01 01:01:01 1001 1001\n" +
                             "!<trailer>\n";
        final InputStream input = new ByteArrayInputStream(header.getBytes());
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        final ArchiveEntry archiveEntry = arArchiveInputStream.getNextEntry();
        assertEquals(ArArchiveEntry.class, archiveEntry.getClass());
        final ArArchiveEntry arArchiveEntry = (ArArchiveEntry) archiveEntry;
        assertEquals("filename.txt", arArchiveEntry.getName());
        assertEquals(1024L, arArchiveEntry.getSize());
    }

    @Test
    public void testClose() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        final ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        arArchiveInputStream.close();
        assertThrows(IOException.class, () -> arArchiveInputStream.getNextArEntry());
    }

    @Test
    public void testMatches() {
        final byte[] signature = new byte[8];
        final boolean expected = true;
        System.arraycopy(ArArchiveInputStream.HEADER.getBytes(), 0, signature, 0, 8);
        assertEquals(expected, ArArchiveInputStream.matches(signature, 8));
    }

    @Test
    public void testMatchesWithWrongLength() {
        final byte[] signature = new byte[5];
        final boolean expected = false;
        System.arraycopy(ArArchiveInputStream.HEADER.getBytes(), 0, signature, 0, 5);
        assertEquals(expected, ArArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testMatchesWithInvalidSignature() {
        final byte[] signature = new byte[8];
        final boolean expected = false;
        System.arraycopy(ArArchiveInputStream.HEADER.getBytes(), 0, signature, 0, 8);
        signature[0] = (byte) 0x3a;
        assertEquals(expected, ArArchiveInputStream.matches(signature, signature.length));
    }

}