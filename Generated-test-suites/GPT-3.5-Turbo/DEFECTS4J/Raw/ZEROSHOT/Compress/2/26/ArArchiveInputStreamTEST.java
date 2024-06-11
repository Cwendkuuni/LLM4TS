import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream arInputStream;
    private InputStream input;

    @Before
    public void setUp() {
        byte[] data = "#########################################".getBytes();
        input = new ByteArrayInputStream(data);
        arInputStream = new ArArchiveInputStream(input);
    }

    @After
    public void tearDown() throws IOException {
        arInputStream.close();
        input.close();
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry entry = arInputStream.getNextArEntry();

        assertNotNull(entry);
        assertEquals("Test Entry", entry.getName());
        assertEquals(40, entry.getSize());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry entry = arInputStream.getNextEntry();

        assertNotNull(entry);
        assertTrue(entry instanceof ArArchiveEntry);
        assertEquals("Test Entry", entry.getName());
        assertEquals(40, entry.getSize());
    }

    @Test
    public void testClose() throws IOException {
        arInputStream.close();
        assertTrue(arInputStream.isClosed());
    }

    @Test
    public void testRead() throws IOException {
        int value = arInputStream.read();

        assertEquals(35, value);
    }

    @Test
    public void testReadBytes() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = arInputStream.read(buffer);

        assertEquals("##########", new String(buffer));
        assertEquals(10, bytesRead);
    }

    @Test
    public void testReadBytesWithOffsetAndLength() throws IOException {
        byte[] buffer = new byte[20];
        int bytesRead = arInputStream.read(buffer, 5, 10);

        assertEquals("#####", new String(buffer, 5, 5));
        assertEquals(10, bytesRead);
    }

    @Test
    public void testMatches() {
        byte[] signature = { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a };

        assertTrue(ArArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testGetOffset() throws IOException {
        int value = arInputStream.read();

        assertEquals(1, arInputStream.getOffset());
    }

    @Test
    public void testAvailable() throws IOException {
        int available = arInputStream.available();

        assertEquals(40, available);
    }

    @Test
    public void testMarkSupported() {
        boolean markSupported = arInputStream.markSupported();

        assertTrue(markSupported);
    }

    @Test
    public void testMark() throws IOException {
        arInputStream.mark(10);
        int value = arInputStream.read();

        assertEquals(36, value);
    }

    @Test
    public void testReset() throws IOException {
        arInputStream.mark(10);
        int value = arInputStream.read();

        assertEquals(36, value);

        arInputStream.reset();
        value = arInputStream.read();

        assertEquals(36, value);
    }

    @Test
    public void testSkip() throws IOException {
        arInputStream.skip(5);
        int value = arInputStream.read();

        assertEquals(37, value);
    }

    @Test
    public void testSkipZeroBytes() throws IOException {
        arInputStream.skip(0);
        int value = arInputStream.read();

        assertEquals(35, value);
    }

    @Test
    public void testSkipNegativeBytes() throws IOException {
        arInputStream.skip(-5);
        int value = arInputStream.read();

        assertEquals(35, value);
    }

    @Test
    public void testIsClosed() throws IOException {
        boolean closed = arInputStream.isClosed();

        assertFalse(closed);
    }

    @Test
    public void testReadNullBytes() throws IOException {
        byte[] buffer = null;
        int bytesRead = arInputStream.read(buffer);

        assertEquals(-1, bytesRead);
    }
}