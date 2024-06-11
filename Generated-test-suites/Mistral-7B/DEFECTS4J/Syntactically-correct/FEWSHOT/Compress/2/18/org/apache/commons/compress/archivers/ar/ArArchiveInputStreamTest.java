package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {
    @Test
    public void testMatches() {
        final byte[] signature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        assertEquals(true, ArArchiveInputStream.matches(signature, signature.length));
        assertEquals(false, ArArchiveInputStream.matches(signature, signature.length - 1));
    }

    @Test
    public void testGetNextArEntry_validEntry_returnsArArchiveEntry() throws IOException {
        final ArArchiveInputStream inputStream = createInputStreamWithEntry("testdata/test.ar", "testdata/test.txt");
        final ArArchiveEntry arEntry = inputStream.getNextArEntry();
        assertEquals("test.txt", arEntry.getName());
        assertEquals(7L, arEntry.getSize());
    }

    @Test
    public void testGetNextArEntry_invalidEntry_returnsNull() throws IOException {
        final ArArchiveInputStream inputStream = createInputStreamWithInvalidEntry("testdata/test.ar");
        final ArArchiveEntry arEntry = inputStream.getNextArEntry();
        assertNull(arEntry);
    }

    @Test
    public void testGetNextArEntry_eof_returnsNull() throws IOException {
        final ArArchiveInputStream inputStream = createInputStreamWithEntry("testdata/test.ar");
        final ArArchiveEntry arEntry = inputStream.getNextArEntry();
        final ArArchiveEntry nextArEntry = inputStream.getNextArEntry();
        assertNull(nextArEntry);
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntry_invalidInputStream_throwsException() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        inputStream.mark(1);
        inputStream.close();
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);
        arArchiveInputStream.getNextArEntry();
    }

    private ArArchiveInputStream createInputStreamWithEntry(String archiveFileName, String fileEntryName) throws IOException {
        final InputStream inputStream = ArArchiveInputStreamTest.class.getResourceAsStream(archiveFileName);
        return new ArArchiveInputStream(inputStream);
    }

    private ArArchiveInputStream createInputStreamWithInvalidEntry(String archiveFileName) throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        return new ArArchiveInputStream(inputStream);
    }
}