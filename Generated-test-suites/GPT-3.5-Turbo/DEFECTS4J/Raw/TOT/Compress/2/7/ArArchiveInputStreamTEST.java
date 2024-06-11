import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream arInputStream;

    @Before
    public void setUp() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("!<arch>\n".getBytes());
        arInputStream = new ArArchiveInputStream(byteArrayInputStream);
    }

    @After
    public void tearDown() throws IOException {
        arInputStream.close();
    }

    @Test
    public void testGetNextArEntry_returnsValidEntry() throws IOException {
        ArArchiveEntry entry = arInputStream.getNextArEntry();
        assertNotNull(entry);
        assertEquals("file1", entry.getName());
        assertEquals(10, entry.getSize());
    }

    @Test
    public void testGetNextArEntry_returnsNullAtEndOfFile() throws IOException {
        ArArchiveEntry entry = arInputStream.getNextArEntry();
        assertNull(entry);
    }

    @Test
    public void testGetNextEntry_returnsValidEntry() throws IOException {
        // The behavior of getNextEntry() should be the same as getNextArEntry()
        ArchiveEntry entry = arInputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals("file1", entry.getName());
        assertEquals(10, entry.getSize());
    }

    @Test
    public void testRead_returnsValidByte() throws IOException {
        int b = arInputStream.read();
        assertEquals('!', b);
    }

    @Test
    public void testReadIntoArray_returnsValidBytes() throws IOException {
        byte[] buffer = new byte[5];
        int bytesRead = arInputStream.read(buffer);
        assertEquals(5, bytesRead);
        assertArrayEquals(new byte[]{'!', '<', 'a', 'r', 'c'}, buffer);
    }

    @Test
    public void testReadIntoArrayWithOffset_returnsValidBytes() throws IOException {
        byte[] buffer = new byte[6];
        int bytesRead = arInputStream.read(buffer, 1, 4);
        assertEquals(4, bytesRead);
        assertArrayEquals(new byte[]{0, '!', '<', 'a', 0, 0}, buffer);
    }

    @Test
    public void testMatches_returnsTrueWithValidSignature() {
        byte[] signature = {'!', '<', 'a', 'r', 'c', 'h', '>', '\n'};
        assertTrue(ArArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testMatches_returnsFalseWithInvalidSignature() {
        byte[] signature = {'<', 'a', 'r', 'c', 'h', '>', '\n'};
        assertFalse(ArArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testMatches_returnsFalseWithInvalidLength() {
        byte[] signature = {'!', '<', 'a', 'r', 'c', 'h', '>', '\n'};
        assertFalse(ArArchiveInputStream.matches(signature, 7));
    }

}