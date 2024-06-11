import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = {3, 9, 5, 2, 0, 7};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);

        assertEquals(3, compressorInputStream.read());
        assertEquals(9, compressorInputStream.read());
    }

    @Test
    public void testReadWithByteArray() throws IOException {
        byte[] data = {3, 9, 5, 2, 0, 7};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);

        byte[] output = new byte[4];
        int bytesRead = compressorInputStream.read(output, 0, 4);
        
        assertEquals(4, bytesRead);
        assertArrayEquals(new byte[]{3, 9, 5, 2}, output);
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = {3, 9, 5, 2, 0, 7};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);

        long skipped = compressorInputStream.skip(2);

        assertEquals(2, skipped);
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] data = {3, 9, 5, 2, 0, 7};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);

        assertEquals(6, compressorInputStream.available());
    }

    @Test
    public void testClose() throws IOException {
        byte[] data = {3, 9, 5, 2, 0, 7};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);

        compressorInputStream.close();
        assertTrue(true);
    }

    @Test
    public void testHeaderCheck() {
        byte[] header = {120, -28, 0, 1};
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(null);

        assertFalse(compressorInputStream.checkZlibHeader(header, 4));
    }
}