import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testDefaultConstructor() throws IOException {
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[]{}));
        Assert.assertNotNull(inputStream);
    }

    @Test
    public void testConstructorWithDefaultParameters() throws IOException {
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[]{}));
        Assert.assertSame(new Inflater(false), inputStream.getInputStream().getDelegate());
    }

    @Test
    public void testConstructorWithZlibHeader() throws IOException {
        byte[] zlibHeader = new byte[]{(byte) 0x78, (byte) 0x49, (byte) 0x50, (byte) 0x4c, (byte) 0x4b, (byte) 0x05, (byte) 0x01};
        InputStream zlibInputStream = new ByteArrayInputStream(new byte[][]{zlibHeader, new byte[]{}});
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(zlibInputStream, new DeflateParameters());
        Assert.assertSame(new Inflater(true), inputStream.getInputStream().getDelegate());
    }

    @Test
    public void testRead() throws IOException {
        byte[] data = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03};
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream testStream = new DeflateCompressorInputStream(inputStream);

        byte expected = 0x01;
        byte read = testStream.read();

        Assert.assertEquals(expected, read);
        Assert.assertEquals(1, testStream.available());
    }

    @Test
    public void testReadWithEmptyBuffer() throws IOException {
        byte[] data = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03};
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream testStream = new DeflateCompressorInputStream(inputStream);

        byte[] buffer = new byte[1];
        int bytesRead = testStream.read(buffer, 0, 1);

        Assert.assertEquals(1, bytesRead);
        Assert.assertEquals(expectedByte(data[0]), buffer[0]);
        Assert.assertEquals(2, testStream.available());
    }

    @Test
    public void testReadWithNonEmptyBuffer() throws IOException {
        byte[] data = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03};
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream testStream = new DeflateCompressorInputStream(inputStream);

        byte[] buffer = new byte[2];
        int bytesRead = testStream.read(buffer, 0, 2);

        Assert.assertEquals(2, bytesRead);
        Assert.assertArrayEquals(expectedBytes(data), buffer);
        Assert.assertEquals(0, testStream.available());
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03};
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream testStream = new DeflateCompressorInputStream(inputStream);

        long bytesToSkip = 2;
        long skipped = testStream.skip(bytesToSkip);

        Assert.assertEquals(bytesToSkip, skipped);
        Assert.assertEquals(1, testStream.available());
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] data = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03};
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream testStream = new DeflateCompressorInputStream(inputStream);

        Assert.assertEquals(3, testStream.available());

        testStream.read();
        Assert.assertEquals(2, testStream.available());

        testStream.read(new byte[2][], 0, 2);
        Assert.assertEquals(0, testStream.available());
    }

    @Test
    public void testClose() throws IOException {
        byte[] data = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03};
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream testStream = new DeflateCompressorInputStream(inputStream);

        testStream.close();
        Assert.assertThrows(IOException.class, testStream::read);
    }

    private byte expectedByte(byte b) {
        return b;
    }

    private byte[] expectedBytes(byte[] bytes) {
        return bytes;
    }
}