

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.junit.Assert;
import org.junit.Test;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = { 120, -100, 5, 0, 0, -1, -1, 5, 0, 60, -125, 119, -75, 30, -81, -5 };
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        int result = deflateInputStream.read();

        Assert.assertEquals(120, result);
        deflateInputStream.close();
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] data = { 120, -100, 5, 0, 0, -1, -1, 5, 0, 60, -125, 119, -75, 30, -81, -5 };
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        byte[] buffer = new byte[4];
        int result = deflateInputStream.read(buffer, 0, 4);

        Assert.assertEquals(4, result);
        Assert.assertArrayEquals(new byte[] { 120, -100, 5, 0 }, buffer);
        deflateInputStream.close();
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = { 120, -100, 5, 0, 0, -1, -1, 5, 0, 60, -125, 119, -75, 30, -81, -5 };
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        long result = deflateInputStream.skip(4);

        Assert.assertEquals(4, result);
        deflateInputStream.close();
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] data = { 120, -100, 5, 0, 0, -1, -1, 5, 0, 60, -125, 119, -75, 30, -81, -5 };
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        int result = deflateInputStream.available();

        Assert.assertEquals(16, result);
        deflateInputStream.close();
    }

    @Test
    public void testClose() throws IOException {
        byte[] data = { 120, -100, 5, 0, 0, -1, -1, 5, 0, 60, -125, 119, -75, 30, -81, -5 };
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        deflateInputStream.close();

        // No assertion, just checking if it closes without any exception
    }
}
