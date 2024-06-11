
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.junit.Test;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] input = new byte[] { 0x11, 0x22, 0x33 };
        InputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        int result = deflateInputStream.read();

        assertEquals(0x11, result);
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] input = new byte[] { 0x11, 0x22, 0x33 };
        InputStream inputStream = new ByteArrayInputStream(input);
        byte[] buffer = new byte[3];
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        int result = deflateInputStream.read(buffer, 0, buffer.length);

        assertEquals(3, result);
        assertArrayEquals(input, buffer);
    }

    @Test
    public void testSkip() throws IOException {
        byte[] input = new byte[] { 0x11, 0x22, 0x33 };
        InputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        long result = deflateInputStream.skip(2);

        assertEquals(2, result);
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] input = new byte[] { 0x11, 0x22, 0x33 };
        InputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        int result = deflateInputStream.available();

        assertEquals(3, result);
    }

    @Test
    public void testClose() throws IOException {
        byte[] input = new byte[] { 0x11, 0x22, 0x33 };
        InputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        deflateInputStream.close();
    }

}
