import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ArArchiveInputStreamTest {

    @Mock
    private InputStream mockInputStream;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetNextArEntry_WithValidEntry_ReturnsArArchiveEntry() throws IOException {
        byte[] bytes = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x10, 0x20, 0x30, 0x40, 0x50, 0x60, 0x70, 0x80, 0x90, 0xa0, 0xb0, 0xc0, 0xd0, 0xe0, 0xf0, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d};

        InputStream inputStream = new ByteArrayInputStream(bytes);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        ArArchiveEntry arArchiveEntry = arArchiveInputStream.getNextArEntry();

        assertNotNull(arArchiveEntry);
        assertEquals("header", arArchiveEntry.getName());
        assertEquals(189120722933731L, arArchiveEntry.getLength());
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntry_WithInvalidHeader_ThrowsIOException() throws IOException {
        byte[] bytes = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x10, 0x20, 0x30, 0x40, 0x50, 0x60, 0x70, 0x80, 0x90, 0xa0, 0xb0, 0xc0, 0xd0, 0xe0, 0xf0, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b};

        InputStream inputStream = new ByteArrayInputStream(bytes);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        arArchiveInputStream.getNextArEntry();
    }

    @Test
    public void testGetNextArEntry_WithNoEntry_ReturnsNull() throws IOException {
        byte[] bytes = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};

        InputStream inputStream = new ByteArrayInputStream(bytes);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        ArArchiveEntry arArchiveEntry = arArchiveInputStream.getNextArEntry();

        assertNull(arArchiveEntry);
    }

    @Test
    public void testGetNextArEntry_WithIncompleteEntry_ReturnsNull() throws IOException {
        byte[] bytes = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x10, 0x20, 0x30, 0x40, 0x50, 0x60, 0x70, 0x80, 0x90, 0xa0, 0xb0, 0xc0, 0xd0, 0xe0, 0xf0};

        InputStream inputStream = new ByteArrayInputStream(bytes);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        ArArchiveEntry arArchiveEntry = arArchiveInputStream.getNextArEntry();

        assertNull(arArchiveEntry);
    }

    @Test
    public void testGetNextArEntry_WithIncompleteEntryHeaderData_ThrowsIOException() throws IOException {
        byte[] bytes = {0x21, 0x3c, 0x61, 0x72, 0x3e, 0x0a, 0x10, 0x20, 0x30, 0x40, 0x50, 0x60, 0x70, 0x80, 0x90, 0xa0, 0xb0, 0xc0, 0xd0, 0xe0, 0xf0, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d};

        InputStream inputStream = new ByteArrayInputStream(bytes);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        try {
            arArchiveInputStream.getNextArEntry();
            fail("Expected an IOException to be thrown");
        } catch (IOException e) {
            assertEquals("invalid header ", e.getMessage());
        }
    }

    @Test
    public void testGetNextEntry_WithValidEntry_ReturnsArArchiveEntry() throws IOException {
        byte[] bytes = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x10, 0x20, 0x30, 0x40, 0x50, 0x60, 0x70, 0x80, 0x90, 0xa0, 0xb0, 0xc0, 0xd0, 0xe0, 0xf0, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d};

        InputStream inputStream = new ByteArrayInputStream(bytes);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        ArchiveEntry archiveEntry = arArchiveInputStream.getNextEntry();

        assertNotNull(archiveEntry);
        assertTrue(archiveEntry instanceof ArArchiveEntry);
        assertEquals("header", ((ArArchiveEntry) archiveEntry).getName());
        assertEquals(189120722933731L, ((ArArchiveEntry) archiveEntry).getLength());
    }

    @Test(expected = IOException.class)
    public void testGetNextEntry_WithInvalidHeader_ThrowsIOException() throws IOException {
        byte[] bytes = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x10, 0x20, 0x30, 0x40, 0x50, 0x60, 0x70, 0x80, 0x90, 0xa0, 0xb0, 0xc0, 0xd0, 0xe0, 0xf0, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b};

        InputStream inputStream = new ByteArrayInputStream(bytes);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        arArchiveInputStream.getNextEntry();
    }

    @Test
    public void testRead_WithValidData_ReturnsByte() throws IOException {
        byte[] bytes = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};

        InputStream inputStream = new ByteArrayInputStream(bytes);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        int read = arArchiveInputStream.read();

        assertEquals(33, read);
    }

    @Test
    public void testRead_WithEmptyData_ReturnsMinusOne() throws IOException {
        byte[] bytes = {};

        InputStream inputStream = new ByteArrayInputStream(bytes);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        int read = arArchiveInputStream.read();

        assertEquals(-1, read);
    }

    @Test
    public void testRead_WithValidByteArray_ReturnsLength() throws IOException {
        byte[] bytes = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};

        InputStream inputStream = new ByteArrayInputStream(bytes);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        byte[] buffer = new byte[8];
        int read = arArchiveInputStream.read(buffer);

        assertEquals(8, read);
        assertArrayEquals(bytes, buffer);
    }

    @Test
    public void testRead_WithIncompleteData_ReturnsLength() throws IOException {
        byte[] bytes = {0x21, 0x3c, 0x61, 0x72};

        InputStream inputStream = new ByteArrayInputStream(bytes);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        byte[] buffer = new byte[8];
        int read = arArchiveInputStream.read(buffer);

        assertEquals(4, read);
        assertArrayEquals(new byte[] {0x21, 0x3c, 0x61, 0x72, 0x00, 0x00, 0x00, 0x00}, buffer);
    }

    @Test
    public void testRead_WithEmptyByteArray_ReturnsZero() throws IOException {
        byte[] bytes = {};

        InputStream inputStream = new ByteArrayInputStream(bytes);
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        byte[] buffer = new byte[8];
        int read = arArchiveInputStream.read(buffer);

        assertEquals(0, read);
        assertArrayEquals(new byte[] {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}, buffer);
    }

    @Test
    public void testMatches_WithValidSignature_ReturnsTrue() {
        byte[] signature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        int length = 8;

        boolean matches = ArArchiveInputStream.matches(signature, length);

        assertTrue(matches);
    }

    @Test
    public void testMatches_WithInvalidHeader_ReturnsFalse() {
        byte[] signature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x00};
        int length = 8;

        boolean matches = ArArchiveInputStream.matches(signature, length);

        assertFalse(matches);
    }

    @Test
    public void testMatches_WithInvalidLength_ReturnsFalse() {
        byte[] signature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        int length = 7;

        boolean matches = ArArchiveInputStream.matches(signature, length);

        assertFalse(matches);
    }
}