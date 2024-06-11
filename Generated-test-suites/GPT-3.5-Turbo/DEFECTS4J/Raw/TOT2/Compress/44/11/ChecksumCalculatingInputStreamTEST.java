import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;

public class ChecksumCalculatingInputStreamTest {

    private ChecksumCalculatingInputStream stream;

    @Before
    public void setUp() {
        stream = new ChecksumCalculatingInputStream(new CRC32(), new ByteArrayInputStream("test".getBytes()));
    }

    @Test
    public void testRead() throws IOException {
        int value = stream.read();
        assertEquals('t', value);
        assertEquals(4, stream.getValue()); // CRC32 for "test"
    }

    @Test
    public void testReadArray() throws IOException {
        byte[] buffer = new byte[4];
        int bytesRead = stream.read(buffer);
        assertEquals(4, bytesRead);
        assertEquals("test", new String(buffer));
        assertEquals(4, stream.getValue()); // CRC32 for "test"
    }

    @Test
    public void testReadArrayWithOffset() throws IOException {
        byte[] buffer = new byte[6];
        int bytesRead = stream.read(buffer, 1, 4);
        assertEquals(4, bytesRead);
        assertEquals(0, buffer[0]); // Padding
        assertEquals("test", new String(buffer, 1, 4));
        assertEquals(4, stream.getValue()); // CRC32 for "test"
    }

    @Test
    public void testSkip() throws IOException {
        long skipped = stream.skip(2);
        assertEquals(2, skipped);
        assertEquals(4, stream.getValue()); // CRC32 for "test"
    }

    @Test
    public void testGetValue() throws IOException {
        assertEquals(0, stream.getValue());
        stream.read();
        stream.read();
        stream.read();
        stream.read();
        assertEquals(4, stream.getValue()); // CRC32 for "test"
    }

    @Test
    public void testExceptionHandling() {
        try {
            stream.read();
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }
}