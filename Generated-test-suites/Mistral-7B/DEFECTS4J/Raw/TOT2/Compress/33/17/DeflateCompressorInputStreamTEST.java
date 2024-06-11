import static org.junit.Assert.*;

import java.io.*;
import java.util.zip.DeflateParameters;

import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.utils.ByteStreams;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testReadMethod() throws IOException {
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[] {1}));
        int actual = inputStream.read();
        Assert.assertEquals(-1, actual);
        inputStream.close();
    }

    @Test
    public void testReadByteArrayMethod() throws IOException {
        byte[] data = new byte[] {1, 2, 3};
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(data));
        byte[] actual = new byte[3];
        int len = inputStream.read(actual, 0, 3);
        Assert.assertEquals(3, len);
        Assert.assertArrayEquals(new byte[] {1, 2, 3}, actual);
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testReadFromEmptyInputStream() throws IOException {
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[]{}));
        inputStream.read();
    }

    @Test
    public void testIsZlibCompressedWithHeader() throws IOException {
        byte[] zlibHeader = new byte[] {0x78, 0x5E, 0x01, 0x00};
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(zlibHeader));
        boolean actual = inputStream.isZlibCompressed(zlibHeader, zlibHeader.length);
        Assert.assertTrue(actual);
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testIsZlibCompressedWithInvalidData() throws IOException {
        byte[] invalidData = new byte[] {0x01, 0x02, 0x03};
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(invalidData));
        boolean actual = inputStream.isZlibCompressed(invalidData, invalidData.length);
        Assert.assertFalse(actual);
        inputStream.close();
    }

    @Test
    public void testCloseMethod() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {1});
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        deflateInputStream.close();
        assertFalse(deflateInputStream.isOpen());
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testReadFromClosedInputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {1});
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        deflateInputStream.close();
        deflateInputStream.read();
    }

    @Test
    public void testAvailableMethod() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {1});
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        long actual = deflateInputStream.available();
        Assert.assertEquals(1, actual);
        deflateInputStream.close();
        inputStream.close();
    }

    @Test
    public void testSkipMethod() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {1, 2, 3});
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        deflateInputStream.skip(2);
        long actual = deflateInputStream.available();
        Assert.assertEquals(1, actual);
        deflateInputStream.close();
        inputStream.close();
    }
}