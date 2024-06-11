import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test class for DeflateCompressorInputStream
 */
public class DeflateCompressorInputStreamTest {

    private InputStream inputStream;
    private DeflateCompressorInputStream deflateCompressorInputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        byte[] compressedData = new byte[]{(byte) 0x78, 0x9c, 0x1f, 0xf3, 0x6b, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x03, 0x80, 0x27, 0x1b, 0x07, 0xda};
        outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStreamTemp = new ByteArrayInputStream(compressedData);
        deflateCompressorInputStream = new DeflateCompressorInputStream(inputStreamTemp, new DeflateParameters());
        this.inputStream = deflateCompressorInputStream;
    }

    @Test
    public void testConstructorWithInputStream() {
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        Assert.assertNotNull(deflateCompressorInputStream);
    }

    @Test
    public void testConstructorWithInputStreamAndDeflateParameters() {
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());
        Assert.assertNotNull(deflateCompressorInputStream);
    }

    @Test
    public void testReadMethod() throws IOException {
        int readByte = inputStream.read();
        int readByteFromDeflate = deflateCompressorInputStream.read();
        Assert.assertEquals(readByte, readByteFromDeflate);
    }

    @Test
    public void testReadByteArrayMethod() throws IOException {
        byte[] buffer = new byte[5];
        int length = inputStream.read(buffer);
        int lengthFromDeflate = deflateCompressorInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(length, lengthFromDeflate);
    }

    @Test
    public void testSkipMethod() throws IOException {
        long skippedBytes = inputStream.skip(10);
        long skippedBytesFromDeflate = deflateCompressorInputStream.skip(10);
        Assert.assertEquals(skippedBytes, skippedBytesFromDeflate);
    }

    @Test
    public void testAvailableMethod() throws IOException {
        int availableBytes = inputStream.available();
        int availableBytesFromDeflate = deflateCompressorInputStream.available();
        Assert.assertEquals(availableBytes, availableBytesFromDeflate);
    }

    @Test
    public void testCloseMethod() throws IOException {
        deflateCompressorInputStream.close();
        inputStream.close();
        Assert.assertFalse(deflateCompressorInputStream.isOpen());
        Assert.assertFalse(inputStream.isOpen());
    }

    @Test(expected = IOException.class)
    public void testThrowsIOExceptionWhenClosed() throws IOException {
        deflateCompressorInputStream.close();
        int readByte = deflateCompressorInputStream.read();
    }

    @Test
    public void testChecksZlibSignature() {
        byte[] compressedDataWithZlibHeader = new byte[]{(byte) 0x78, 0x9c, 0x1f, 0xf3, 0x6b, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x03, 0x80, 0x27, 0x1b, 0x07, 0xda, 0x50, 0x4b, 0x03, 0x04, 0x14};
        InputStream zlibCompressedInputStream = new ByteArrayInputStream(compressedDataWithZlibHeader);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(zlibCompressedInputStream, new DeflateParameters());
        boolean isZlibCompressed = deflateCompressorInputStream.isZlibCompressed();
        Assert.assertTrue(isZlibCompressed);
    }

    @Test
    public void testIsNotZlibCompressedWithoutZlibHeader() {
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());
        boolean isZlibCompressed = deflateCompressorInputStream.isZlibCompressed();
        Assert.assertFalse(isZlibCompressed);
    }
}