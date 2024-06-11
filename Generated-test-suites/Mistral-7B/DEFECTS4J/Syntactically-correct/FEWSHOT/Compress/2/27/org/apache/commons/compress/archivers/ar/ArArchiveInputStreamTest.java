package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArArchiveStreamFactory;
import org.apache.commons.compress.utils.ByteStreams;
import org.junit.Test;

public class ArArchiveInputStreamTest {

    @Test
    public void testReadHeader() throws IOException {
        final InputStream input = mock(InputStream.class);
        final ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(input);

        final byte[] header = new byte[] { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, (byte) 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48 };
        when(input.read(header)).thenReturn(header.length);
        when(input.read()).thenReturn(0);

        final ArArchiveEntry entry = archiveInputStream.getNextArEntry();
        assertNotNull(entry);
        assertEquals("4142434445464748", entry.getName().trim());
        assertEquals(512L, entry.getSize());
    }

    @Test
    public void testReadTrailer() throws IOException {
        final InputStream input = mock(InputStream.class);
        final ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(input);

        final byte[] header = new byte[] { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a };
        final byte[] trailer = new byte[] { 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48 };
        when(input.read(header)).thenReturn(header.length);
        when(input.read(trailer)).thenReturn(trailer.length);
        when(input.read()).thenReturn(0);

        final ArArchiveEntry entry = archiveInputStream.getNextArEntry();
        assertNotNull(entry);
        assertEquals("4142434445464748", entry.getName().trim());
        assertEquals(512L, entry.getSize());
    }

    @Test
    public void testNoHeaderFound() throws IOException {
        final InputStream input = mock(InputStream.class);
        final ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(input);

        when(input.available()).thenReturn(0);
        when(input.read()).thenReturn(-1);

        assertNull(archiveInputStream.getNextArEntry());
    }

    @Test
    public void testReadPartialHeader() throws IOException {
        final InputStream input = mock(InputStream.class);
        final ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(input);

        final byte[] header = new byte[] { 0x21, 0x3c, 0x61, 0x72 };
        final int read = 4;
        when(input.read(header)).thenReturn(read);

        try {
            archiveInputStream.getNextArEntry();
            fail("IOException expected");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testReadPartialTrailer() throws IOException {
        final InputStream input = mock(InputStream.class);
        final ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(input);

        final byte[] header = new byte[] { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a };
        final byte[] trailer = new byte[] { 0x41, 0x42, 0x43 };
        when(input.read(header)).thenReturn(header.length);
        when(input.read(trailer)).thenReturn(-1);
        when(input.read()).thenReturn(0);

        try {
            archiveInputStream.getNextArEntry();
            fail("IOException expected");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testReadTrailerWithCRC() throws IOException {
        final InputStream input = mock(InputStream.class);
        final ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(input);

        final byte[] header = new byte[] { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a };
        final byte[] trailer = new byte[] { 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48 };
        final byte[] expectedCrc = new byte[] { 0x11, 0x22, 0x33, 0x44 };
        when(input.read(header)).thenReturn(header.length);
        when(input.read(trailer)).thenReturn(trailer.length);
        when(input.read(expectedCrc)).thenReturn(expectedCrc.length);

        final ArArchiveEntry entry = archiveInputStream.getNextArEntry();
        assertNotNull(entry);
        assertEquals("4142434445464748", entry.getName().trim());
        assertEquals(512L, entry.getSize());

        final CRC32 crc = new CRC32();
        crc.update(header);
        crc.update(trailer);
        final byte[] calculatedCrc = crc.getValue();
        assertArrayEquals("CRC mismatch", expectedCrc, calculatedCrc);
    }

    @Test
    public void testReadFileContent() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[] { 0x1, 0x2, 0x3, 0x4, 0x5 });
        final ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(input);

        final ArArchiveEntry entry = archiveInputStream.getNextArEntry();
        assertNotNull(entry);
        assertEquals("", entry.getName().trim());
        assertEquals(5L, entry.getSize());

        final byte[] bytes = new byte[5];
        assertArrayEquals("Content does not match", new byte[] { 0x1, 0x2, 0x3, 0x4, 0x5 }, archiveInputStream.readAllBytes());
    }

    @Test
    public void testMatchesSignature() {
        final byte[] signature = new byte[] { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48 };
        assertTrue("Invalid signature", ArArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testDoesNotMatchSignature() {
        final byte[] signature = new byte[] { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x41, 0x43, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48 };
        assertFalse("Invalid signature", ArArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testReadAllBytes() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[] { 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8 });
        final ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(input);

        final byte[] bytes = archiveInputStream.readAllBytes();
        assertArrayEquals("Content does not match", new byte[] { 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8 }, bytes);
    }

    @Test
    public void testReadAllBytesFromClosedInputStream() throws IOException {
        final InputStream input = mock(InputStream.class);
        final ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(input);
        archiveInputStream.close();

        try {
            archiveInputStream.readAllBytes();
            fail("IOException expected");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testReadAllBytesWithLargeInputStream() throws IOException {
        final byte[] data = new byte[1024 * 1024];
        final InputStream input = new ByteArrayInputStream(data);
        final ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(input);

        final byte[] bytes = archiveInputStream.readAllBytes();
        assertArrayEquals("Content does not match", data, bytes);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[] { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x00, 0x00, 0x00, 0x00, 0x00, 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a });
        final ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(input);

        final ArArchiveEntry firstEntry = archiveInputStream.getNextArEntry();
        final ArArchiveEntry secondEntry = archiveInputStream.getNextArEntry();

        assertNotNull(firstEntry);
        assertNotNull(secondEntry);
        assertEquals("4142434445464748", firstEntry.getName().trim());
        assertEquals("", secondEntry.getName().trim());
    }

    @Test
    public void testClose() throws IOException {
        final InputStream input = mock(InputStream.class);
        final ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(input);

        archiveInputStream.close();
        verify(input).close();
    }

    @Test
    public void testReadWithOffset() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final byte[] data = "data".getBytes();
        final InputStream input = new ByteArrayInputStream(data);
        final ArArchiveInputStream archiveInputStream = new ArArchiveInputStream(new ByteStreams.ByteArrayInputStream(output.toByteArray()));
        final byte[] expected = new byte[2];

        archiveInputStream.read(expected, 1, 2);
        assertEquals("Unexpected data read", "ta", new String(expected));
    }
}