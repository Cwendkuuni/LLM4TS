
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = {1, 2, 3};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream inflaterIn = new DeflateCompressorInputStream(inputStream);
        
        // Test reading bytes
        assertEquals(1, inflaterIn.read());
        assertEquals(2, inflaterIn.read());
        assertEquals(3, inflaterIn.read());
        assertEquals(-1, inflaterIn.read());
        
        inflaterIn.close();
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] data = {1, 2, 3};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream inflaterIn = new DeflateCompressorInputStream(inputStream);

        // Test reading bytes into buffer
        byte[] buffer = new byte[4];
        int bytesRead = inflaterIn.read(buffer, 0, 4);
        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[]{1, 2, 3, 0}, buffer);

        inflaterIn.close();
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = {1, 2, 3};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream inflaterIn = new DeflateCompressorInputStream(inputStream);

        // Test skipping bytes
        long skipped = inflaterIn.skip(2);
        assertEquals(2, skipped);
        assertEquals(3, inflaterIn.read());

        inflaterIn.close();
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] data = {1, 2, 3};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream inflaterIn = new DeflateCompressorInputStream(inputStream);

        // Test available bytes
        assertEquals(3, inflaterIn.available());

        inflaterIn.close();
    }

    @Test
    public void testClose() throws IOException {
        byte[] data = {1, 2, 3};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream inflaterIn = new DeflateCompressorInputStream(inputStream);

        // Test closing the stream
        inflaterIn.close();

        // Attempting to read after closing should throw IOException
        try {
            inflaterIn.read();
            fail("Expected IOException not thrown");
        } catch (IOException e) {
            // Expected exception
        }
    }
}
