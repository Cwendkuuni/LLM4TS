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
    private InputStream mockInputStream;

    @Before
    public void setUp() {
        mockInputStream = mock(InputStream.class);
        arArchiveInputStream = new ArArchiveInputStream(mockInputStream);
    }

    @After
    public void tearDown() throws IOException {
        arArchiveInputStream.close();
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        // Test typical use case
        String header = "!<arch>\n";
        byte[] expectedHeader = header.getBytes();
        byte[] name = "entry1".getBytes();
        byte[] length = "10".getBytes();
        byte[] trailer = "`\n".getBytes();

        // Prepare mock behavior
        when(mockInputStream.read(any(byte[].class))).thenAnswer(invocation -> {
            byte[] buffer = (byte[]) invocation.getArguments()[0];
            System.arraycopy(expectedHeader, 0, buffer, 0, expectedHeader.length);
            System.arraycopy(name, 0, buffer, expectedHeader.length, name.length);
            System.arraycopy(length, 0, buffer, expectedHeader.length + name.length, length.length);
            System.arraycopy(trailer, 0, buffer, expectedHeader.length + name.length + length.length, trailer.length);
            return expectedHeader.length + name.length + length.length + trailer.length;
        });

        ArArchiveEntry entry = arArchiveInputStream.getNextArEntry();

        // Verify entry attributes
        assertNotNull(entry);
        assertEquals("entry1", entry.getName());
        assertEquals(10, entry.getSize());

        // Verify mock behavior
        verify(mockInputStream, times(1)).read(any(byte[].class));
        verify(mockInputStream, times(1)).close();
    }

    @Test
    public void testGetNextArEntry_InvalidHeader() throws IOException {
        // Test error scenario - Invalid header
        String invalidHeader = "!invalid\n";
        byte[] invalidHeaderBytes = invalidHeader.getBytes();

        // Prepare mock behavior
        when(mockInputStream.read(any(byte[].class))).thenReturn(invalidHeaderBytes.length);
        when(mockInputStream.read(invalidHeaderBytes)).thenReturn(invalidHeaderBytes.length);
        when(mockInputStream.read()).thenReturn((int) '\n');

        try {
            arArchiveInputStream.getNextArEntry();
            fail("IOException expected but not thrown");
        } catch (IOException e) {
            // Verify mock behavior
            verify(mockInputStream, times(1)).read(any(byte[].class));
            verify(mockInputStream, times(1)).close();

            // Verify exception message
            assertEquals("invalid header !invalid", e.getMessage());
        }
    }

    @Test
    public void testGetNextArEntry_EOF() throws IOException {
        // Test error scenario - EOF
        String header = "!<arch>\n";
        byte[] headerBytes = header.getBytes();

        // Prepare mock behavior
        when(mockInputStream.read(any(byte[].class))).thenReturn(headerBytes.length);
        when(mockInputStream.available()).thenReturn(0);

        ArArchiveEntry entry = arArchiveInputStream.getNextArEntry();

        // Verify entry is null
        assertNull(entry);

        // Verify mock behavior
        verify(mockInputStream, times(1)).read(any(byte[].class));
        verify(mockInputStream, times(1)).close();
    }
    
    //Testing for matches() method
    @Test
    public void testMatches() {
        // Test typical use case
        byte[] validSignature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        assertTrue(ArArchiveInputStream.matches(validSignature, validSignature.length));

        // Test invalid signature length
        byte[] invalidSignature = {0x21, 0x3c};
        assertFalse(ArArchiveInputStream.matches(invalidSignature, invalidSignature.length));

        // Test invalid signature content
        byte[] invalidSignatureContent = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0b};
        assertFalse(ArArchiveInputStream.matches(invalidSignatureContent, invalidSignatureContent.length));
    }
}