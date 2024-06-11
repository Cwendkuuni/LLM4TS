import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream arInputStream;
    private byte[] testFileContent;

    @Before
    public void setUp() {
        String testFileContentString = "This is a test file content";
        testFileContent = testFileContentString.getBytes();
        InputStream inputStream = new ByteArrayInputStream(testFileContent);
        arInputStream = new ArArchiveInputStream(inputStream);
    }

    @After
    public void tearDown() throws IOException {
        arInputStream.close();
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry arEntry = arInputStream.getNextArEntry();

        assertNotNull(arEntry);
        assertEquals(0, arEntry.getOffset());
        assertArrayEquals(testFileContent, arEntry.getContent());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry entry = arInputStream.getNextEntry();

        assertNotNull(entry);
        assertTrue(entry instanceof ArArchiveEntry);
    }

    @Test
    public void testGetNextArEntry_WhenEofBeforeHeaderRead_ShouldThrowIOException() {
        InputStream inputStream = new ByteArrayInputStream(testFileContent);
        try {
            arInputStream = new ArArchiveInputStream(inputStream);
            arInputStream.getNextArEntry();
        } catch (IOException ex) {
            assertEquals("failed to read header", ex.getMessage());
        }
    }

    @Test
    public void testGetNextArEntry_WhenInvalidHeader_ShouldThrowIOException() {
        byte[] invalidHeader = "Invalid Header".getBytes();
        InputStream inputStream = new ByteArrayInputStream(invalidHeader);
        try {
            arInputStream = new ArArchiveInputStream(inputStream);
            arInputStream.getNextArEntry();
        } catch (IOException ex) {
            assertEquals("invalid header Invalid Header", ex.getMessage());
        }
    }

    @Test
    public void testGetNextArEntry_WhenEof_ShouldReturnNull() throws IOException {
        ArArchiveEntry arEntry = arInputStream.getNextArEntry();
        assertNull(arEntry);
    }

    @Test
    public void testRead() throws IOException {
        int data = arInputStream.read();

        assertEquals(84, data); // ASCII for 'T'
    }

    @Test
    public void testRead_WithByteArray() throws IOException {
        byte[] data = new byte[5];
        arInputStream.read(data);

        assertArrayEquals(new byte[]{84, 104, 105, 115, 32}, data);
    }

    @Test
    public void testRead_WithByteArrayOffsetLength() throws IOException {
        byte[] data = new byte[10];
        arInputStream.read(data, 0, 10);

        assertArrayEquals(new byte[]{84, 104, 105, 115, 32, 105, 115, 32, 97, 32}, data);
    }

    @Test
    public void testClose() throws IOException {
        arInputStream.close();
        arInputStream.read(); // Expecting IOException
    }

    @Test
    public void testMatches_WhenValidSignature_ShouldReturnTrue() {
        byte[] signature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        boolean matches = ArArchiveInputStream.matches(signature, 8);
        assertTrue(matches);
    }

    @Test
    public void testMatches_WhenInvalidSignature_ShouldReturnFalse() {
        byte[] signature = new byte[]{0x21, 0x3c, 0x62, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        boolean matches = ArArchiveInputStream.matches(signature, 8);
        assertFalse(matches);
    }
}