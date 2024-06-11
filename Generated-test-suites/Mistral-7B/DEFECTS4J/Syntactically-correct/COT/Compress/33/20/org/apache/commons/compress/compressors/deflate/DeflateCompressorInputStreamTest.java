import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for DeflateCompressorInputStream
 */
public class DeflateCompressorInputStreamTest {

    /**
     * Tests constructor DeflateCompressorInputStream(InputStream inputStream)
     * @throws IOException
     */
    @Test
    public void testDeflateCompressorInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x78, 0x5E, 0x06, 0x08, 0x00, 0x2B, 0x01, 0xE2, (byte) 0xFF, 0x50, 0x4B, 0x03, 0x04, 0x14, 0x00, 0x0F, 0xA1, 0x5A, 0x2F, 0x2F, 0x53, 0x53, 0x54, 0x53, 0x53, 0x55, 0x54});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        Assert.assertNotNull(deflateCompressorInputStream);
    }

    /**
     * Tests constructor DeflateCompressorInputStream(InputStream inputStream, DeflateParameters parameters)
     * @throws IOException
     */
    @Test
    public void testDeflateCompressorInputStreamWithParameters() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x78, 0x5E, 0x06, 0x08, 0x00, 0x2B, 0x01, 0xE2, (byte) 0xFF, 0x50, 0x4B, 0x03, 0x04, 0x14, 0x00, 0x0F, 0xA1, 0x5A, 0x2F, 0x2F, 0x53, 0x53, 0x54, 0x53, 0x53, 0x55, 0x54});
        DeflateParameters parameters = new DeflateParameters();
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, parameters);
        Assert.assertNotNull(deflateCompressorInputStream);
    }

    /**
     * Tests method read()
     * @throws IOException
     */
    @Test
    public void testRead() throws IOException {
        byte[] bytes = {0x01, 0x02, 0x03};
        InputStream inputStream = new ByteArrayInputStream(bytes);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        int ret = deflateCompressorInputStream.read();
        Assert.assertEquals(bytes[0], (byte) ret);
        int ret2 = deflateCompressorInputStream.read();
        Assert.assertEquals(bytes[1], (byte) ret2);
        int ret3 = deflateCompressorInputStream.read();
        Assert.assertEquals(bytes[2], (byte) ret3);
        int ret4 = deflateCompressorInputStream.read();
        Assert.assertEquals(-1, ret4);
    }

    /**
     * Tests method read(byte[] buf, int off, int len)
     * @throws IOException
     */
    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] bytes = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        InputStream inputStream = new ByteArrayInputStream(bytes);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[3];
        int ret = deflateCompressorInputStream.read(buffer, 0, 3);
        Assert.assertEquals(3, ret);
        Assert.assertArrayEquals(new byte[]{bytes[0], bytes[1], bytes[2]}, buffer);

        byte[] buffer2 = new byte[6];
        int ret2 = deflateCompressorInputStream.read(buffer2, 0, 6);
        Assert.assertEquals(6, ret2);
        Assert.assertArrayEquals(bytes, buffer2);
    }

    /**
     * Tests method skip(long n)
     * @throws IOException
     */
    @Test
    public void testSkip() throws IOException {
        byte[] bytes = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        InputStream inputStream = new ByteArrayInputStream(bytes);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        long ret = deflateCompressorInputStream.skip(4);
        Assert.assertEquals(4, ret);
        Assert.assertEquals(bytes[5], deflateCompressorInputStream.read());
    }

    /**
     * Tests method available()
     * @throws IOException
     */
    @Test
    public void testAvailable() throws IOException {
        byte[] bytes = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        InputStream inputStream = new ByteArrayInputStream(bytes);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        Assert.assertEquals(6, deflateCompressorInputStream.available());
    }

    /**
     * Tests method close()
     * @throws IOException
     */
    @Test
    public void testClose() throws IOException {
        byte[] bytes = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        InputStream inputStream = new ByteArrayInputStream(bytes);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        deflateCompressorInputStream.close();
        try {
            deflateCompressorInputStream.read();
            Assert.fail();
        } catch (IOException e) {
            //Expected
        }
    }

    /**
     * Tests method close() multiple times
     * @throws IOException
     */
    @Test
    public void testCloseMultipleTimes() throws IOException {
        byte[] bytes = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        InputStream inputStream = new ByteArrayInputStream(bytes);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        deflateCompressorInputStream.close();
        deflateCompressorInputStream.close();
        try {
            deflateCompressorInputStream.read();
            Assert.fail();
        } catch (IOException e) {
            //Expected
        }
    }

    /**
     * Test method checkZlibSignature(byte[] signature, int length) with valid zlib header
     * @throws IOException
     */
    @Test
    public void testCheckZlibSignatureValid() throws IOException {
        byte[] bytes = {0x78, 0x5E, 0x06, 0x08, 0x00, 0x2B, 0x01, 0xE2, (byte) 0xFF, 0x50, 0x4B, 0x03, 0x04, 0x14, 0x00, 0x0F, 0xA1, 0x5A, 0x2F, 0x2F, 0x53, 0x53, 0x54, 0x53, 0x53, 0x55, 0x54};
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(bytes));
        boolean ret = deflateCompressorInputStream.checkZlibSignature(bytes, bytes.length);
        Assert.assertTrue(ret);
    }

    /**
     * Test method checkZlibSignature(byte[] signature, int length) with invalid zlib header
     * @throws IOException
     */
    @Test
    public void testCheckZlibSignatureInvalid() throws IOException {
        byte[] bytes = {0x78, 0x5E, 0x06, 0x08, 0x00, 0x2B, 0x01, 0xE2, (byte) 0xFF, 0x50, 0x4B, 0x03, 0x04, 0x14, 0x00, 0x0F, 0xA1, 0x5A, 0x2F, 0x2F, 0x53, 0x53, 0x54, 0x53, 0x53, 0x55, 0x54, 0x01};
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(bytes));
        boolean ret = deflateCompressorInputStream.checkZlibSignature(bytes, bytes.length);
        Assert.assertFalse(ret);
    }

    /**
     * Tests method checkZlibSignature(byte[] signature, int length) with empty signature
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCheckZlibSignatureEmptySignature() throws IOException {
        byte[] bytes = {};
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(bytes));
        deflateCompressorInputStream.checkZlibSignature(bytes, bytes.length);
    }
}