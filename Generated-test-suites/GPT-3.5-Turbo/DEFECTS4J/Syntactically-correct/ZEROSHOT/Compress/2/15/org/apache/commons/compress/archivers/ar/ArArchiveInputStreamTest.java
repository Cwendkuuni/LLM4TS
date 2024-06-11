import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        InputStream input = new FileInputStream("test.ar");
        ArArchiveInputStream ais = new ArArchiveInputStream(input);
        ArArchiveEntry entry = ais.getNextArEntry();
        assertNotNull(entry);
        assertEquals("test", entry.getName());
        assertEquals(10, entry.getLength());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        InputStream input = new FileInputStream("test.ar");
        ArArchiveInputStream ais = new ArArchiveInputStream(input);
        ArchiveEntry entry = ais.getNextEntry();
        assertNotNull(entry);
        assertEquals("test", entry.getName());
        assertEquals(10, entry.getLength());
    }

    @Test
    public void testRead() throws IOException {
        InputStream input = new FileInputStream("test.ar");
        ArArchiveInputStream ais = new ArArchiveInputStream(input);
        int data = ais.read();
        assertEquals(97, data);
    }

    @Test
    public void testReadByteArray() throws IOException {
        InputStream input = new FileInputStream("test.ar");
        ArArchiveInputStream ais = new ArArchiveInputStream(input);
        byte[] buffer = new byte[5];
        int bytesRead = ais.read(buffer);
        assertEquals(5, bytesRead);
        assertArrayEquals(new byte[]{97, 98, 99, 100, 101}, buffer);
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        InputStream input = new FileInputStream("test.ar");
        ArArchiveInputStream ais = new ArArchiveInputStream(input);
        byte[] buffer = new byte[10];
        int bytesRead = ais.read(buffer, 2, 5);
        assertEquals(5, bytesRead);
        assertArrayEquals(new byte[]{0, 0, 97, 98, 99, 100, 101, 0, 0, 0}, buffer);
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        boolean result = ArArchiveInputStream.matches(signature, signature.length);
        assertTrue(result);
    }

    @Test
    public void testClose() throws IOException {
        InputStream input = new FileInputStream("test.ar");
        ArArchiveInputStream ais = new ArArchiveInputStream(input);
        ais.close();
        assertTrue(ais.isClosed());
    }

}