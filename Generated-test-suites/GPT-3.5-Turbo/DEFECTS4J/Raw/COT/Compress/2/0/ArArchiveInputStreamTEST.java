import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream arArchiveInputStream;
    private InputStream mockedInputStream;

    @Before
    public void setUp() throws Exception {
        mockedInputStream = mock(InputStream.class);
        arArchiveInputStream = new ArArchiveInputStream(mockedInputStream);
    }

    @After
    public void tearDown() throws Exception {
        arArchiveInputStream.close();
    }

    @Test
    public void testGetNextArEntry_WithValidEntry_ReturnsArArchiveEntry() throws IOException {
        byte[] header = "!<arch>\n".getBytes();
        byte[] name = "file1.txt            ".getBytes();
        byte[] length = "10        ".getBytes();
        byte[] trailer = "`\n".getBytes();
        byte[] entryBytes = joinArrays(header, name, length, trailer);

        when(mockedInputStream.available()).thenReturn(entryBytes.length);
        when(mockedInputStream.read(any(byte[].class), anyInt(), anyInt())).thenAnswer(invoke -> {
            byte[] buffer = invoke.getArgument(0);
            int offset = invoke.getArgument(1);
            int lengthValue = invoke.getArgument(2);
            System.arraycopy(entryBytes, 0, buffer, offset, lengthValue);
            return lengthValue;
        });

        ArArchiveEntry entry = arArchiveInputStream.getNextArEntry();

        assertNotNull(entry);
        assertEquals("file1.txt", entry.getName());
        assertEquals(10, entry.getLength());
    }

    @Test
    public void testGetNextArEntry_WithEmptyStream_ReturnsNull() throws IOException {
        when(mockedInputStream.available()).thenReturn(0);

        ArArchiveEntry entry = arArchiveInputStream.getNextArEntry();

        assertNull(entry);
    }

    @Test
    public void testGetNextArEntry_WithIncompleteEntryHeader_ThrowsIOException() throws IOException {
        byte[] header = "!<ar".getBytes();

        when(mockedInputStream.available()).thenReturn(header.length);
        when(mockedInputStream.read(any(byte[].class))).thenReturn(header.length);

        try {
            arArchiveInputStream.getNextArEntry();
            fail("IOException not thrown");
        } catch (IOException e) {
            assertEquals("failed to read header", e.getMessage());
        }
    }

    @Test
    public void testGetNextArEntry_WithInvalidEntryHeader_ThrowsIOException() throws IOException {
        byte[] header = "!<arhcr\n".getBytes();

        when(mockedInputStream.available()).thenReturn(header.length);
        when(mockedInputStream.read(any(byte[].class))).thenReturn(header.length);

        try {
            arArchiveInputStream.getNextArEntry();
            fail("IOException not thrown");
        } catch (IOException e) {
            assertEquals("invalid header !<arhcr", e.getMessage());
        }
    }

    @Test
    public void testGetNextArEntry_WithEntryLengthNotMultipleOf2_SkipsPaddingByte() throws IOException {
        byte[] header = "!<arch>\n".getBytes();
        byte[] name = "file2.txt            ".getBytes();
        byte[] length = "11        ".getBytes();
        byte[] trailer = "`\n".getBytes();
        byte[] entryBytes = joinArrays(header, name, length, trailer);

        when(mockedInputStream.available()).thenReturn(entryBytes.length + 1);
        when(mockedInputStream.read(any(byte[].class), anyInt(), anyInt())).thenAnswer(invoke -> {
            byte[] buffer = invoke.getArgument(0);
            int offset = invoke.getArgument(1);
            int lengthValue = invoke.getArgument(2);
            System.arraycopy(entryBytes, 0, buffer, offset, lengthValue);
            return lengthValue;
        });

        ArArchiveEntry entry = arArchiveInputStream.getNextArEntry();

        assertNotNull(entry);
        assertEquals("file2.txt", entry.getName());
        assertEquals(11, entry.getLength());
        // Padding byte skipped
        verify(mockedInputStream, times(1)).read();
    }

    @Test
    public void testGetNextEntry_DelegatesToGetNextArEntry() throws IOException {
        ArchiveEntry mockedEntry = mock(ArchiveEntry.class);
        when(arArchiveInputStream.getNextArEntry()).thenReturn(mockedEntry);

        ArchiveEntry entry = arArchiveInputStream.getNextEntry();

        assertEquals(mockedEntry, entry);
        verify(arArchiveInputStream, times(1)).getNextArEntry();
    }

    @Test
    public void testClose_ClosesInputStream() throws IOException {
        arArchiveInputStream.close();

        verify(mockedInputStream, times(1)).close();
    }

    @Test
    public void testRead_ReadsBytesFromInputStream() throws IOException {
        byte[] buffer = new byte[10];
        when(mockedInputStream.read(any(byte[].class))).thenReturn(5);

        int result = arArchiveInputStream.read(buffer);

        assertEquals(5, result);
        verify(mockedInputStream, times(1)).read(buffer, 0, buffer.length);
    }

    @Test
    public void testRead_UpdatesOffset() throws IOException {
        byte[] buffer = new byte[10];
        when(mockedInputStream.read(any(byte[].class))).thenReturn(5);

        arArchiveInputStream.read(buffer);

        assertEquals(5, arArchiveInputStream.getOffset());
    }

    @Test
    public void testRead_WithOffset_ReadsBytesFromInputStream() throws IOException {
        byte[] buffer = new byte[10];
        when(mockedInputStream.read(any(byte[].class), anyInt(), anyInt())).thenReturn(5);

        int result = arArchiveInputStream.read(buffer, 2, 3);

        assertEquals(5, result);
        verify(mockedInputStream, times(1)).read(buffer, 2, 3);
    }

    @Test
    public void testRead_WithOffset_UpdatesOffset() throws IOException {
        byte[] buffer = new byte[10];
        when(mockedInputStream.read(any(byte[].class), anyInt(), anyInt())).thenReturn(5);

        arArchiveInputStream.read(buffer, 2, 3);

        assertEquals(5, arArchiveInputStream.getOffset());
    }

    @Test
    public void testMatches_WithValidSignatureAndLength_ReturnsTrue() {
        byte[] signature = { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a };
        int length = 8;

        boolean result = ArArchiveInputStream.matches(signature, length);

        assertTrue(result);
    }

    @Test
    public void testMatches_WithInvalidLength_ReturnsFalse() {
        byte[] signature = { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a };
        int length = 7;

        boolean result = ArArchiveInputStream.matches(signature, length);

        assertFalse(result);
    }

    @Test
    public void testMatches_WithInvalidSignature_ReturnsFalse() {
        byte[] signature = { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0b };
        int length = 8;

        boolean result = ArArchiveInputStream.matches(signature, length);

        assertFalse(result);
    }

    private byte[] joinArrays(byte[]... arrays) {
        int totalLength = 0;
        for (byte[] array : arrays) {
            totalLength += array.length;
        }
        byte[] result = new byte[totalLength];
        int offset = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }
}