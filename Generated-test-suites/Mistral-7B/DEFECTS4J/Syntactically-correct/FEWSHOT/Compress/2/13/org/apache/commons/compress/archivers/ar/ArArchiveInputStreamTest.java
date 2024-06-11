package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.junit.Test;

public class ArArchiveInputStreamTest {
    @Test
    public void testGetNextArEntryWithEmptyInputStream() throws IOException {
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(mock(InputStream.class));
        assertNull(arArchiveInputStream.getNextArEntry());
    }

    @Test
    public void testGetNextArEntryWithInvalidHeader() throws IOException {
        InputStream input = mock(InputStream.class);
        when(input.read(anyByteArray())).thenReturn(-1);

        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        assertNull(arArchiveInputStream.getNextArEntry());
    }

    @Test
    public void testGetNextArEntryWithValidEntry() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"\u001F\u001CAR\u0012\u0000\u0000\u0001test.txt\u0000\u0000\u0002\u0000\u0014test.txt\u001A\u0000\u0000\u0000\u0000\u0002test.txt\u001A"});
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);

        ArchiveEntry nextEntry = arArchiveInputStream.getNextEntry();
        assertInstanceOf(ArArchiveEntry.class, nextEntry);
        ArArchiveEntry arArchiveEntry = (ArArchiveEntry) nextEntry;
        assertEquals("test.txt", arArchiveEntry.getName());
        assertEquals(2L, arArchiveEntry.getSize());
    }

    @Test
    public void testGetNextArEntryWithMultipleEntries() throws IOException {
        String arHeader = "!<AR>\n" +
                "  2\n" +
                "test1.txt\u001A\u0000\u0002\u0000\u0014test1.txt\u001A\u0002\u0000\u0002test1.txt\u001A" +
                "\ntest2.txt\u001A\u0000\u0002\u0000\u0014test2.txt\u001A\u0002\u0001\u0002test2.txt\u001A";

        InputStream input = new ByteArrayInputStream(arHeader.getBytes());
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);

        ArchiveEntry entry1 = arArchiveInputStream.getNextEntry();
        assertEquals("test1.txt", entry1.getName());
        assertEquals(2L, entry1.getSize());

        ArchiveEntry entry2 = arArchiveInputStream.getNextEntry();
        assertEquals("test2.txt", entry2.getName());
        assertEquals(1L, entry2.getSize());

        ArchiveEntry entry3 = arArchiveInputStream.getNextEntry();
        assertNull(entry3);
    }

    @Test
    public void testMatchesWithValidSignature() {
        byte[] signature = new byte[]{0x3c, 0x21, 0x72, 0x61, 0x68, 0x63, 0x0a, 0x3e};
        assertTrue(ArArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testMatchesWithInvalidSignature() {
        byte[] signature = new byte[]{0x3c, 0x21, 0x72, 0x61, 0x68, 0x63, 0x0a, 0x3e, 0x01};
        assertFalse(ArArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testReadByteWithValidInputStream() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{0x01});
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        assertEquals((int) 0x01, arArchiveInputStream.read());
    }

    @Test
    public void testReadByteWithEmptyInputStream() throws IOException {
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(mock(InputStream.class));
        assertEquals(-1, arArchiveInputStream.read());
    }

    @Test
    public void testReadByteWithInvalidInputStream() throws IOException {
        InputStream input = mock(InputStream.class);
        when(input.read()).thenReturn(IOException.class);

        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        assertEquals(-1, arArchiveInputStream.read());
    }

    @Test
    public void testReadBytesWithValidInputStream() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"test".getBytes()});
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);

        byte[] readBytes = new byte[3];
        int actualLength = arArchiveInputStream.read(readBytes);
        assertEquals(3, actualLength);
        assertArrayEquals(new byte[]{"t", "e", "s"}, readBytes);
    }

    @Test
    public void testReadBytesWithEmptyInputStream() throws IOException {
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(mock(InputStream.class));
        byte[] readBytes = new byte[3];
        int actualLength = arArchiveInputStream.read(readBytes);
        assertEquals(0, actualLength);
    }

    @Test
    public void testReadBytesWithInvalidInputStream() throws IOException {
        InputStream input = mock(InputStream.class);
        when(input.read(anyByteArray())).thenThrow(IOException.class);

        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        byte[] readBytes = new byte[3];
        int actualLength = arArchiveInputStream.read(readBytes);
        assertEquals(-1, actualLength);
    }

    @Test
    public void testClose() throws IOException {
        InputStream input = mock(InputStream.class);

        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(input);
        arArchiveInputStream.close();
        verify(input).close();
    }
}