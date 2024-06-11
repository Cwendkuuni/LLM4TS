import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = {0, 1, 2, 3, 4};
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        int result = deflateInputStream.read();
        assertEquals(0, result);
        deflateInputStream.close();
    }

    @Test
    public void testRead_withBuffer() throws IOException {
        byte[] data = {0, 1, 2, 3, 4};
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buf = new byte[3];
        int result = deflateInputStream.read(buf, 0, buf.length);
        assertEquals(3, result);
        assertArrayEquals(new byte[]{0, 1, 2}, buf);
        deflateInputStream.close();
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = {0, 1, 2, 3, 4};
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        long result = deflateInputStream.skip(2);
        assertEquals(2, result);
        deflateInputStream.close();
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] data = {0, 1, 2, 3, 4};
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        int result = deflateInputStream.available();
        assertEquals(5, result);
        deflateInputStream.close();
    }

    @Test
    public void testClose() throws IOException {
        byte[] data = {0, 1, 2, 3, 4};
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        deflateInputStream.close();
    }
}