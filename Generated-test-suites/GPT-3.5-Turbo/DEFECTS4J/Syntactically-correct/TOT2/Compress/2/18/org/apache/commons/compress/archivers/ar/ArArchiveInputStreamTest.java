import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        byte[] header = "#arhc\n".getBytes();
        byte[] entry = "file1.txt0000066".getBytes();
        byte[] trailer = "#arhc\n".getBytes();

        ByteArrayInputStream in = new ByteArrayInputStream(header);
        ArArchiveInputStream ar = new ArArchiveInputStream(in);

        ArArchiveEntry result = ar.getNextArEntry();

        assertNotNull(result);
        assertEquals("file1.txt", result.getName());
        assertEquals(66, result.getSize());

    }

    @Test
    public void testGetNextArEntryUnexpectedHeader() throws IOException {
        byte[] header = "#abhc\n".getBytes();

        ByteArrayInputStream in = new ByteArrayInputStream(header);
        ArArchiveInputStream ar = new ArArchiveInputStream(in);

        try {
            ArArchiveEntry result = ar.getNextArEntry();
            fail("Expected IOException");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void testGetNextArEntryEOF() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        ArArchiveInputStream ar = new ArArchiveInputStream(in);

        ArArchiveEntry result = ar.getNextArEntry();

        assertNull(result);
    }

    @Test
    public void testGetNextArEntryIncomplete() throws IOException {
        byte[] header = "#abhc\n".getBytes();

        ByteArrayInputStream in = new ByteArrayInputStream(header);
        ArArchiveInputStream ar = new ArArchiveInputStream(in);

        try {
            ArArchiveEntry result = ar.getNextArEntry();
            fail("Expected IOException");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void testRead() throws IOException {
        byte[] data = "test".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ArArchiveInputStream ar = new ArArchiveInputStream(in);

        int result = ar.read();

        assertEquals('t', result);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] data = "test".getBytes();
        byte[] buffer = new byte[4];

        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ArArchiveInputStream ar = new ArArchiveInputStream(in);

        int result = ar.read(buffer);

        assertEquals(4, result);
        assertArrayEquals(data, buffer);
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        byte[] data = "test".getBytes();
        byte[] buffer = new byte[4];

        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ArArchiveInputStream ar = new ArArchiveInputStream(in);

        int result = ar.read(buffer, 1, 2);

        assertEquals(2, result);
        assertArrayEquals(new byte[] {0, 't', 0, 0}, buffer);
    }

    @Test
    public void testMatchesValidSignature() {
        byte[] signature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};

        assertTrue(ArArchiveInputStream.matches(signature, 8));
    }

    @Test
    public void testMatchesInvalidSignature() {
        byte[] signature = {0x12, 0x34, 0x56, 0x78};

        assertFalse(ArArchiveInputStream.matches(signature, 4));
    }

}