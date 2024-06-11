import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ArArchiveInputStreamTest {

    @Test
    public void testConstructor() {
        try {
            ArArchiveInputStream ais = new ArArchiveInputStream(new ByteArrayInputStream(new byte[0]));
            assertNotNull(ais);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream("MESSAGEHEADERMESSAGEHEADER".getBytes());
        ArArchiveInputStream ais = new ArArchiveInputStream(in);

        ArArchiveEntry entry = ais.getNextArEntry();
        assertNotNull(entry);
        assertEquals("MESSAGEHEADER", entry.getName());
        assertEquals(16L, entry.getLength());

        entry = ais.getNextArEntry();
        assertNull(entry);
    }

    @Test(expected = IOException.class)
    public void testGetNextArEntryIOException() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream("MESSAGE".getBytes());
        ArArchiveInputStream ais = new ArArchiveInputStream(in);
        ais.getNextArEntry();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream("MESSAGEHEADERMESSAGEHEADER".getBytes());
        ArArchiveInputStream ais = new ArArchiveInputStream(in);

        ArchiveEntry entry = ais.getNextEntry();
        assertNotNull(entry);
        assertEquals("MESSAGEHEADER", entry.getName());
        assertEquals(16L, entry.getSize());

        entry = ais.getNextEntry();
        assertNull(entry);
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream("MESSAGE".getBytes());
        ArArchiveInputStream ais = new ArArchiveInputStream(in);
        ais.close();
        assertTrue(ais.isClosed());
    }

    @Test
    public void testRead() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        ArArchiveInputStream ais = new ArArchiveInputStream(in);

        int result = ais.read();
        assertEquals(1, result);

        result = ais.read();
        assertEquals(2, result);

        result = ais.read();
        assertEquals(3, result);

        result = ais.read();
        assertEquals(-1, result);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] input = new byte[] { 0x01, 0x02, 0x03 };
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        ArArchiveInputStream ais = new ArArchiveInputStream(in);

        byte[] output = new byte[3];
        int result = ais.read(output);

        assertEquals(3, result);
        assertArrayEquals(input, output);
    }

    @Test
    public void testReadByteArrayOffsetLength() throws IOException {
        byte[] input = new byte[] { 0x01, 0x02, 0x03 };
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        ArArchiveInputStream ais = new ArArchiveInputStream(in);

        byte[] output = new byte[3];
        int result = ais.read(output, 0, 2);

        assertEquals(2, result);
        assertEquals(0x01, output[0]);
        assertEquals(0x02, output[1]);
        assertEquals(0x00, output[2]);
    }

    @Test
    public void testMatches() {
        byte[] signature = { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a };

        boolean result = ArArchiveInputStream.matches(signature, signature.length);

        assertTrue(result);
    }
}