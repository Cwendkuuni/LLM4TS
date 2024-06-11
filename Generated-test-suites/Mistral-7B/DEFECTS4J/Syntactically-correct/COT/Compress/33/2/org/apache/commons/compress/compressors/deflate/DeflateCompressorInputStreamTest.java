import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DeflateCompressorInputStreamTest {

    // Test constructor with valid InputStream
    @Test
    public void testDeflateCompressorInputStreamValid() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0x49, (byte) 0x50, (byte) 0x4C, (byte) 0x4B, (byte) 0x05, (byte) 0x02, (byte) 0x14});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        Assert.assertNotNull(deflateCompressorInputStream);
    }

    // Test constructor with null InputStream
    @Test(expected = NullPointerException.class)
    public void testDeflateCompressorInputStreamNullInputStream() {
        new DeflateCompressorInputStream(null);
    }

    // Test read method with valid byte[] and offset and length
    @Test
    public void testReadValid() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[5];
        int length = deflateCompressorInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(length, buffer.length);
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, buffer);
    }

    // Test read method with empty byte[] and valid offset and length
    @Test
    public void testReadEmptyBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[0];
        int length = deflateCompressorInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(length, 0);
    }

    // Test read method with null byte[] and valid offset and length
    @Test(expected = NullPointerException.class)
    public void testReadNullBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = null;
        deflateCompressorInputStream.read(buffer, 0, buffer.length);
    }

    // Test read method with valid byte[] and invalid offset and length
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidOffsetAndLength() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[5];
        deflateCompressorInputStream.read(buffer, 5, buffer.length);
    }

    // Test skip method with valid long
    @Test
    public void testSkipValid() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        long length = 3;
        deflateCompressorInputStream.skip(length);
        int read = deflateCompressorInputStream.read();
        Assert.assertEquals(read, 4);
    }

    // Test skip method with negative long
    @Test(expected = IllegalArgumentException.class)
    public void testSkipNegativeLong() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        deflateCompressorInputStream.skip(-1);
    }

    // Test available method with valid InputStream
    @Test
    public void testAvailableValid() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        int available = deflateCompressorInputStream.available();
        Assert.assertEquals(available, 5);
    }

    // Test close method
    @Test
    public void testClose() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        deflateCompressorInputStream.close();
        Assert.assertThrows(IOException.class, () -> deflateCompressorInputStream.read());
    }

    // Test constructor with valid InputStream and DeflateParameters
    @Test
    public void testDeflateCompressorInputStreamWithDeflateParametersValid() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0x49, (byte) 0x50, (byte) 0x4C, (byte) 0x4B, (byte) 0x05, (byte) 0x02, (byte) 0x14});
        DeflateParameters deflateParameters = new DeflateParameters();
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, deflateParameters);
        Assert.assertNotNull(deflateCompressorInputStream);
    }

    // Test read method with zlib header and valid byte[] and offset and length
    @Test
    public void testReadWithZlibHeaderValid() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0x49, (byte) 0x50, (byte) 0x4C, (byte) 0x4B, (byte) 0x05, (byte) 0x02, (byte) 0x14, 1, 2, 3, 4, 5});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[5];
        int length = deflateCompressorInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(length, 5);
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, buffer);
    }

    // Test read method with zlib header and empty byte[] and valid offset and length
    @Test
    public void testReadWithZlibHeaderEmptyBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0x49, (byte) 0x50, (byte) 0x4C, (byte) 0x4B, (byte) 0x05, (byte) 0x02, (byte) 0x14, 1, 2, 3, 4, 5});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[0];
        int length = deflateCompressorInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(length, 0);
    }

    // Test read method with zlib header and null byte[] and valid offset and length
    @Test(expected = NullPointerException.class)
    public void testReadWithZlibHeaderNullBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0x49, (byte) 0x50, (byte) 0x4C, (byte) 0x4B, (byte) 0x05, (byte) 0x02, (byte) 0x14, 1, 2, 3, 4, 5});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = null;
        deflateCompressorInputStream.read(buffer, 0, buffer.length);
    }

    // Test read method with zlib header and valid byte[] and invalid offset and length
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithZlibHeaderInvalidOffsetAndLength() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0x49, (byte) 0x50, (byte) 0x4C, (byte) 0x4B, (byte) 0x05, (byte) 0x02, (byte) 0x14, 1, 2, 3, 4, 5});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[5];
        deflateCompressorInputStream.read(buffer, 5, buffer.length);
    }

    // Test skip method with zlib header and valid long
    @Test
    public void testSkipWithZlibHeaderValid() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0x49, (byte) 0x50, (byte) 0x4C, (byte) 0x4B, (byte) 0x05, (byte) 0x02, (byte) 0x14, 1, 2, 3, 4, 5});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        long length = 3;
        deflateCompressorInputStream.skip(length);
        int read = deflateCompressorInputStream.read();
        Assert.assertEquals(read, 4);
    }

    // Test skip method with zlib header and negative long
    @Test(expected = IllegalArgumentException.class)
    public void testSkipWithZlibHeaderNegativeLong() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0x49, (byte) 0x50, (byte) 0x4C, (byte) 0x4B, (byte) 0x05, (byte) 0x02, (byte) 0x14, 1, 2, 3, 4, 5});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        deflateCompressorInputStream.skip(-1);
    }

    // Test available method with zlib header and valid InputStream
    @Test
    public void testAvailableWithZlibHeaderValid() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0x49, (byte) 0x50, (byte) 0x4C, (byte) 0x4B, (byte) 0x05, (byte) 0x02, (byte) 0x14, 1, 2, 3, 4, 5});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        int available = deflateCompressorInputStream.available();
        Assert.assertEquals(available, 5);
    }

    // Test close method with zlib header
    @Test
    public void testCloseWithZlibHeader() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0x49, (byte) 0x50, (byte) 0x4C, (byte) 0x4B, (byte) 0x05, (byte) 0x02, (byte) 0x14, 1, 2, 3, 4, 5});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        deflateCompressorInputStream.close();
        Assert.assertThrows(IOException.class, () -> deflateCompressorInputStream.read());
    }
}