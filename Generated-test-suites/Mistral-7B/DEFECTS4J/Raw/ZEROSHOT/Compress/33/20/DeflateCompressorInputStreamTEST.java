import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Tests for {@link DeflateCompressorInputStream} class.
 */
public class DeflateCompressorInputStreamTest {

    /**
     * Test constructor DeflateCompressorInputStream with InputStream.
     */
    @Test
    public void testDeflateCompressorInputStream_WithInputStream() {
        InputStream inputStream = new ByteArrayInputStream("deflate compressed data".getBytes());
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        Assert.assertNotNull(deflateCompressorInputStream);
    }

    /**
     * Test constructor DeflateCompressorInputStream with InputStream and DeflateParameters.
     */
    @Test
    public void testDeflateCompressorInputStream_WithInputStreamAndParameters() {
        InputStream inputStream = new ByteArrayInputStream("deflate compressed data".getBytes());
        DeflateParameters deflateParameters = new DeflateParameters();
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, deflateParameters);
        Assert.assertNotNull(deflateCompressorInputStream);
    }

    /**
     * Test read method.
     */
    @Test
    public void testRead() throws IOException {
        byte[] compressedData = "compressed data".getBytes();
        InputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[1];
        int read = deflateCompressorInputStream.read(buffer, 0, 1);
        Assert.assertEquals(1, read);
        Assert.assertEquals((byte) 0xC5, buffer[0]);
        // read remaining bytes
        while (deflateCompressorInputStream.read(buffer, 0, buffer.length) > 0) {
            // do nothing
        }
    }

    /**
     * Test read method with byte array.
     */
    @Test
    public void testRead_WithByteArray() throws IOException {
        byte[] compressedData = "compressed data".getBytes();
        InputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] expectedData = new byte[]{0xC5, 68, -100, -89, -88, 120, -110, -111, 122, -106, -97, -121, -112, 113, 121, 114, 121};
        byte[] buffer = new byte[expectedData.length];
        int read = deflateCompressorInputStream.read(buffer, 0, expectedData.length);
        Assert.assertEquals(expectedData.length, read);
        Assert.assertArrayEquals(expectedData, buffer);
    }

    /**
     * Test skip method.
     */
    @Test
    public void testSkip() throws IOException {
        byte[] compressedData = "compressed data".getBytes();
        InputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        long expectedData = 5;
        long skipped = deflateCompressorInputStream.skip(expectedData);
        Assert.assertEquals(expectedData, skipped);
    }

    /**
     * Test available method.
     */
    @Test
    public void testAvailable() throws IOException {
        byte[] compressedData = "compressed data".getBytes();
        InputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        int expectedData = 19;
        int available = deflateCompressorInputStream.available();
        Assert.assertEquals(expectedData, available);
    }

    /**
     * Test close method.
     */
    @Test
    public void testClose() throws IOException {
        byte[] compressedData = "compressed data".getBytes();
        InputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        deflateCompressorInputStream.close();
    }

    /**
     * Test check zlib header.
     */
    @Test
    public void testCheckZlibHeader() throws IOException {
        byte[] compressedDataWithZlibHeader = new byte[]{0x78, 0xDA, 0x10, 0x00, 3, 0x0, 0x14, 0x0, 0x0, 0x0, 0x0, 0xFF, 0xFF, 0};
        InputStream inputStreamWithZlibHeader = new ByteArrayInputStream(compressedDataWithZlibHeader);
        DeflateCompressorInputStream deflateCompressorInputStreamWithZlibHeader = new DeflateCompressorInputStream(inputStreamWithZlibHeader);
        Assert.assertTrue(deflateCompressorInputStreamWithZlibHeader.isZlibHeaderPresent());

        byte[] compressedDataWithoutZlibHeader = "compressed data".getBytes();
        InputStream inputStreamWithoutZlibHeader = new ByteArrayInputStream(compressedDataWithoutZlibHeader);
        DeflateCompressorInputStream deflateCompressorInputStreamWithoutZlibHeader = new DeflateCompressorInputStream(inputStreamWithoutZlibHeader);
        Assert.assertFalse(deflateCompressorInputStreamWithoutZlibHeader.isZlibHeaderPresent());
    }
}