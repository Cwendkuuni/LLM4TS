import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        byte[] data = "!<arch>\n".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ArArchiveInputStream arStream = new ArArchiveInputStream(input);

        assertNotNull(arStream.getNextArEntry());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        byte[] data = "!<arch>\n".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ArArchiveInputStream arStream = new ArArchiveInputStream(input);

        assertNotNull(arStream.getNextEntry());
    }

    @Test
    public void testRead() throws IOException {
        byte[] data = "test data".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ArArchiveInputStream arStream = new ArArchiveInputStream(input);

        assertEquals('t', arStream.read());
    }

    @Test
    public void testReadBytes() throws IOException {
        byte[] data = "test data".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ArArchiveInputStream arStream = new ArArchiveInputStream(input);

        byte[] buffer = new byte[4];
        arStream.read(buffer);
        assertArrayEquals(new byte[] {'t', 'e', 's', 't'}, buffer);
    }

    @Test
    public void testMatches() {
        byte[] signature = "!<arch>\n".getBytes();
        int length = 8;

        assertTrue(ArArchiveInputStream.matches(signature, length));
    }

    @Test
    public void testClose() throws IOException {
        byte[] data = "test data".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ArArchiveInputStream arStream = new ArArchiveInputStream(input);

        arStream.close();
        assertTrue(arStream.isClosed());
    }

}