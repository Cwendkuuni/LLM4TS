import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import static org.junit.Assert.*;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] inputBytes = new byte[]{1, 2, 3, 4, 5};
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream);
        int result = deflateStream.read();
        assertEquals(1, result);
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] inputBytes = new byte[]{1, 2, 3, 4, 5};
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[5];
        int result = deflateStream.read(buffer, 0, buffer.length);
        assertEquals(5, result);
        assertArrayEquals(inputBytes, buffer);
    }

    @Test
    public void testSkip() throws IOException {
        byte[] inputBytes = new byte[]{1, 2, 3, 4, 5};
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream);
        long result = deflateStream.skip(3);
        assertEquals(3, result);
        assertEquals(4, deflateStream.read());
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] inputBytes = new byte[]{1, 2, 3, 4, 5};
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream);
        int result = deflateStream.available();
        assertEquals(5, result);
    }

    @Test
    public void testClose() throws IOException {
        byte[] inputBytes = new byte[]{1, 2, 3, 4, 5};
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream);
        deflateStream.close();
    }

    @Test
    public void testCheckSignature() {
        byte[] inputBytes = new byte[]{1, 2, 3, 4, 5};
        boolean result = DeflateCompressorInputStream.checkSignature(inputBytes, 5);
        assertTrue(result);
    }
}