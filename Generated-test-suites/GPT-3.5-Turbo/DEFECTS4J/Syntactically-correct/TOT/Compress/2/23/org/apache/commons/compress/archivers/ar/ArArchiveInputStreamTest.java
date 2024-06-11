import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream arInputStream;
    private byte[] validArEntryBytes;
    private byte[] invalidArEntryBytes;
    private byte[] incompleteArEntryBytes;
    private byte[] emptyArEntryBytes;

    @Before
    public void setUp() {
        validArEntryBytes = "!<arch>\n".getBytes();
        invalidArEntryBytes = "invalid".getBytes();
        incompleteArEntryBytes = "incompleteArEntry".getBytes();
        emptyArEntryBytes = "".getBytes();
    }

    @Test
    public void testGetNextArEntry_ValidEntry_ReturnsArArchiveEntry() throws IOException {
        InputStream input = new ByteArrayInputStream(validArEntryBytes);
        arInputStream = new ArArchiveInputStream(input);

        ArArchiveEntry arEntry = arInputStream.getNextArEntry();

        assertNotNull(arEntry);
        assertEquals("!", arEntry.getName());
        assertEquals(0, arEntry.getLength());
    }

    @Test
    public void testGetNextArEntry_InvalidEntry_IOExceptionThrown() throws IOException {
        InputStream input = new ByteArrayInputStream(invalidArEntryBytes);
        arInputStream = new ArArchiveInputStream(input);

        try {
            arInputStream.getNextArEntry();
            fail("IOException should have been thrown");
        } catch (IOException e) {
            assertEquals("invalid header invalid", e.getMessage());
        }
    }

    @Test
    public void testGetNextArEntry_IncompleteEntry_IOExceptionThrown() throws IOException {
        InputStream input = new ByteArrayInputStream(incompleteArEntryBytes);
        arInputStream = new ArArchiveInputStream(input);

        try {
            arInputStream.getNextArEntry();
            fail("IOException should have been thrown");
        } catch (IOException e) {
            assertEquals("failed to read entry header", e.getMessage());
        }
    }

    @Test
    public void testGetNextArEntry_EmptyEntry_ReturnsNull() throws IOException {
        InputStream input = new ByteArrayInputStream(emptyArEntryBytes);
        arInputStream = new ArArchiveInputStream(input);

        assertNull(arInputStream.getNextArEntry());
    }

    @Test
    public void testGetNextEntry_ValidEntry_ReturnsArArchiveEntry() throws IOException {
        InputStream input = new ByteArrayInputStream(validArEntryBytes);
        arInputStream = new ArArchiveInputStream(input);

        ArchiveEntry entry = arInputStream.getNextEntry();

        assertNotNull(entry);
        assertTrue(entry instanceof ArArchiveEntry);
        assertEquals("!", entry.getName());
        assertEquals(0, entry.getLength());
    }

    @Test
    public void testClose_InputNotClosed_InputClosed() throws IOException {
        InputStream input = new ByteArrayInputStream(validArEntryBytes);
        arInputStream = new ArArchiveInputStream(input);

        arInputStream.close();

        assertTrue(arInputStream.isClosed());
    }

    @Test
    public void testRead_ByteRead_ReturnsByte() throws IOException {
        InputStream input = new ByteArrayInputStream(validArEntryBytes);
        arInputStream = new ArArchiveInputStream(input);

        int data = arInputStream.read();

        assertEquals(33, data);
    }

    @Test
    public void testRead_BytesRead_ReturnsLength() throws IOException {
        InputStream input = new ByteArrayInputStream(validArEntryBytes);
        arInputStream = new ArArchiveInputStream(input);
        byte[] buffer = new byte[validArEntryBytes.length];

        int bytesRead = arInputStream.read(buffer);

        assertEquals(validArEntryBytes.length, bytesRead);
    }

    @Test
    public void testRead_BytesRead_ReturnsPartialLength() throws IOException {
        InputStream input = new ByteArrayInputStream(validArEntryBytes);
        arInputStream = new ArArchiveInputStream(input);
        byte[] buffer = new byte[10];

        int bytesRead = arInputStream.read(buffer);

        assertEquals(10, bytesRead);
    }

    @Test
    public void testMatches_ValidSignature_LengthGreaterThan8_ReturnsTrue() {
        byte[] signature = validArEntryBytes;
        int length = validArEntryBytes.length;

        assertTrue(ArArchiveInputStream.matches(signature, length));
    }

    @Test
    public void testMatches_InvalidSignature_LengthLessThan8_ReturnsFalse() {
        byte[] signature = invalidArEntryBytes;
        int length = invalidArEntryBytes.length;

        assertFalse(ArArchiveInputStream.matches(signature, length));
    }

    @Test
    public void testMatches_ValidSignature_LengthEqualTo8_ReturnsTrue() {
        byte[] signature = validArEntryBytes;
        int length = 8;

        assertTrue(ArArchiveInputStream.matches(signature, length));
    }

    @Test
    public void testMatches_ValidSignature_LengthGreaterThan8_ReturnsFalse() {
        byte[] signature = validArEntryBytes;
        int length = 7;

        assertFalse(ArArchiveInputStream.matches(signature, length));
    }

}