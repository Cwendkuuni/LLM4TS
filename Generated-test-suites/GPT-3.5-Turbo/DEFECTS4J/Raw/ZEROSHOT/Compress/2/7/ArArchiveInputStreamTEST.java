import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArArchiveInputStreamTest {

    @Test
    public void testConstructor() {
        InputStream input = null;
        ArArchiveInputStream ar = new ArArchiveInputStream(input);
        assertNotNull(ar);
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        InputStream input = null;
        ArArchiveInputStream ar = new ArArchiveInputStream(input);
        ArArchiveEntry entry = ar.getNextArEntry();
        assertNull(entry);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        InputStream input = null;
        ArArchiveInputStream ar = new ArArchiveInputStream(input);
        ArchiveEntry entry = ar.getNextEntry();
        assertNull(entry);
    }

    @Test
    public void testClose() throws IOException {
        InputStream input = null;
        ArArchiveInputStream ar = new ArArchiveInputStream(input);
        ar.close();
        assertTrue(ar.isClosed());
    }

    @Test
    public void testRead() throws IOException {
        InputStream input = null;
        ArArchiveInputStream ar = new ArArchiveInputStream(input);
        int result = ar.read();
        assertEquals(-1, result);
    }

    @Test
    public void testReadBytes() throws IOException {
        InputStream input = null;
        ArArchiveInputStream ar = new ArArchiveInputStream(input);
        byte[] buffer = new byte[10];
        int result = ar.read(buffer);
        assertEquals(-1, result);
    }

    @Test
    public void testReadBytesWithOffsetAndLength() throws IOException {
        InputStream input = null;
        ArArchiveInputStream ar = new ArArchiveInputStream(input);
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 5;
        int result = ar.read(buffer, offset, length);
        assertEquals(-1, result);
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[8];
        int length = 8;
        boolean result = ArArchiveInputStream.matches(signature, length);
        assertTrue(result);
    }
}