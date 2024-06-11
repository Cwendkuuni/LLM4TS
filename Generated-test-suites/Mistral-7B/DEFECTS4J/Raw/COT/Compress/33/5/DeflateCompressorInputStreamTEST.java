import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Tests for DeflateCompressorInputStream class
 */
public class DeflateCompressorInputStreamTest {

    /**
     * Test constructor with valid InputStream
     */
    @Test
    public void testConstructorWithInputStream() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0xDA, (byte) 0x10, (byte) 0x1, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0xFF});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        Assert.assertNotNull(deflateCompressorInputStream);
    }

    /**
     * Test constructor with InputStream and DeflateParameters
     */
    @Test
    public void testConstructorWithInputStreamAndDeflateParameters() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0xDA, (byte) 0x10, (byte) 0x1, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0xFF});
        DeflateParameters deflateParameters = new DeflateParameters();
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, deflateParameters);
        Assert.assertNotNull(deflateCompressorInputStream);
    }

    /**
     * Test read method
     */
    @Test
    public void testReadMethod() throws IOException {
        byte[] compressedData = new byte[]{(byte) 0x78, (byte) 0xDA, (byte) 0x10, (byte) 0x1, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0xFF, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0};
        InputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        byte readByte = (byte) deflateCompressorInputStream.read();
        Assert.assertEquals((byte) 0x78, readByte);

        readByte = (byte) deflateCompressorInputStream.read();
        Assert.assertEquals((byte) 0xDA, readByte);

        int numBytesRead = 0;
        byte[] buffer = new byte[compressedData.length];
        while ((readByte = (byte) deflateCompressorInputStream.read()) != -1) {
            buffer[numBytesRead++] = readByte;
        }

        Assert.assertArrayEquals(compressedData, buffer);
    }

    /**
     * Test read method with empty buffer
     */
    @Test
    public void testReadMethodWithEmptyBuffer() throws IOException {
        byte[] compressedData = new byte[]{(byte) 0x78, (byte) 0xDA, (byte) 0x10, (byte) 0x1, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0xFF};
        InputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        byte[] buffer = new byte[0];
        int numBytesRead = deflateCompressorInputStream.read(buffer);
        Assert.assertEquals(0, numBytesRead);
    }

    /**
     * Test read method with buffer size larger than available data
     */
    @Test
    public void testReadMethodWithBufferSizeLargerThanAvailableData() throws IOException {
        byte[] compressedData = new byte[]{(byte) 0x78, (byte) 0xDA, (byte) 0x10, (byte) 0x1, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0xFF, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0};
        InputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        byte[] buffer = new byte[compressedData.length * 2];
        int numBytesRead = deflateCompressorInputStream.read(buffer);
        Assert.assertEquals(compressedData.length, numBytesRead);
    }

    /**
     * Test skip method
     */
    @Test
    public void testSkipMethod() throws IOException {
        byte[] compressedData = new byte[]{(byte) 0x78, (byte) 0xDA, (byte) 0x10, (byte) 0x1, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0xFF};
        InputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        long numBytesSkipped = deflateCompressorInputStream.skip(compressedData.length);
        Assert.assertEquals(compressedData.length, numBytesSkipped);
    }

    /**
     * Test skip method with negative number of bytes to skip
     */
    @Test(expected = IOException.class)
    public void testSkipMethodWithNegativeNumberOfBytesToSkip() throws IOException {
        byte[] compressedData = new byte[]{(byte) 0x78, (byte) 0xDA, (byte) 0x10, (byte) 0x1, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0xFF};
        InputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        deflateCompressorInputStream.skip(-1);
    }

    /**
     * Test available method
     */
    @Test
    public void testAvailableMethod() throws IOException {
        byte[] compressedData = new byte[]{(byte) 0x78, (byte) 0xDA, (byte) 0x10, (byte) 0x1, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0xFF};
        InputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        int numAvailableBytes = deflateCompressorInputStream.available();
        Assert.assertEquals(compressedData.length - deflateCompressorInputStream.getBytesRead(), numAvailableBytes);
    }

    /**
     * Test close method
     */
    @Test
    public void testCloseMethod() throws IOException {
        byte[] compressedData = new byte[]{(byte) 0x78, (byte) 0xDA, (byte) 0x10, (byte) 0x1, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0xFF};
        InputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        deflateCompressorInputStream.close();

        byte readByte = (byte) deflateCompressorInputStream.read();
        Assert.assertEquals(-1, readByte);
    }
}