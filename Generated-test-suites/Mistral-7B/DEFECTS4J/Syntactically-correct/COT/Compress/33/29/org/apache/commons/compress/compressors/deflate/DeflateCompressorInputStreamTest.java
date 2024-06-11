import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for DeflateCompressorInputStream.
 */
public class DeflateCompressorInputStreamTest {

    /**
     * Test constructor DeflateCompressorInputStream(InputStream inputStream).
     */
    @Test
    public void testDeflateCompressorInputStreamConstructor() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x78, 0xDA, 0x10, 0x1, 0x0, 0x0, 0x0, 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        Assert.assertNotNull(deflateCompressorInputStream);
    }

    /**
     * Test constructor DeflateCompressorInputStream(InputStream inputStream, DeflateParameters parameters).
     */
    @Test
    public void testDeflateCompressorInputStreamConstructorWithParameters() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x78, 0xDA, 0x10, 0x1, 0x0, 0x0, 0x0, 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9});
        DeflateParameters parameters = new DeflateParameters();
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, parameters);
        Assert.assertNotNull(deflateCompressorInputStream);
    }

    /**
     * Test method read().
     */
    @Test
    public void testRead() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        int readByte = deflateCompressorInputStream.read();
        Assert.assertEquals(0x01, readByte);
        readByte = deflateCompressorInputStream.read();
        Assert.assertEquals(0x02, readByte);
        readByte = deflateCompressorInputStream.read();
        Assert.assertEquals(0x03, readByte);
        readByte = deflateCompressorInputStream.read();
        Assert.assertEquals(-1, readByte);

        // test reading a single byte with a buffer
        byte[] buffer = new byte[1];
        deflateCompressorInputStream.read(buffer);
        Assert.assertEquals(0x01, buffer[0]);
        deflateCompressorInputStream.read(buffer);
        Assert.assertEquals(0x02, buffer[0]);
        deflateCompressorInputStream.read(buffer);
        Assert.assertEquals(0x03, buffer[0]);
        deflateCompressorInputStream.read(buffer);
        Assert.assertArrayEquals(new byte[0], buffer);
    }

    /**
     * Test method read(byte[] buf, int off, int len).
     */
    @Test
    public void testReadByteArray() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        byte[] buffer = new byte[3];
        int bytesRead = deflateCompressorInputStream.read(buffer);
        Assert.assertEquals(3, bytesRead);
        Assert.assertArrayEquals(new byte[]{0x01, 0x02, 0x03}, buffer);

        buffer = new byte[4];
        bytesRead = deflateCompressorInputStream.read(buffer);
        Assert.assertEquals(4, bytesRead);
        Assert.assertArrayEquals(new byte[]{0x01, 0x02, 0x03, 0x04}, buffer);

        buffer = new byte[5];
        bytesRead = deflateCompressorInputStream.read(buffer);
        Assert.assertEquals(5, bytesRead);
        Assert.assertArrayEquals(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05}, buffer);
    }

    /**
     * Test method skip(long n).
     */
    @Test
    public void testSkip() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        long skippedBytes = deflateCompressorInputStream.skip(3);
        Assert.assertEquals(3, skippedBytes);

        skippedBytes = deflateCompressorInputStream.skip(11);
        Assert.assertEquals(11, skippedBytes);

        skippedBytes = deflateCompressorInputStream.skip(1);
        Assert.assertEquals(1, skippedBytes);

        skippedBytes = deflateCompressorInputStream.skip(Short.MAX_VALUE);
        Assert.assertEquals(Short.MAX_VALUE, skippedBytes);

        skippedBytes = deflateCompressorInputStream.skip(Long.MAX_VALUE);
        Assert.assertEquals(Long.MAX_VALUE, skippedBytes);
    }

    /**
     * Test method available().
     */
    @Test
    public void testAvailable() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        int availableBytes = deflateCompressorInputStream.available();
        Assert.assertEquals(9, availableBytes);

        byte[] buffer = new byte[5];
        int bytesRead = deflateCompressorInputStream.read(buffer);
        availableBytes = deflateCompressorInputStream.available();
        Assert.assertEquals(4, availableBytes);

        bytesRead = deflateCompressorInputStream.read(buffer);
        availableBytes = deflateCompressorInputStream.available();
        Assert.assertEquals(0, availableBytes);
    }

    /**
     * Test method close().
     */
    @Test
    public void testClose() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        deflateCompressorInputStream.close();

        try {
            int readByte = deflateCompressorInputStream.read();
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }

    /**
     * Test method checkHeaderSignature().
     */
    @Test
    public void testCheckHeaderSignature() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(new byte[]{0x78, 0xDA, 0x10, 0x1, 0x0, 0x0, 0x0, 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9});
        outputStream.flush();
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        boolean isZlibHeaderPresent = deflateCompressorInputStream.checkHeaderSignature(new byte[inputStream.available()]);
        Assert.assertTrue(isZlibHeaderPresent);

        inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09});
        deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        isZlibHeaderPresent = deflateCompressorInputStream.checkHeaderSignature(new byte[inputStream.available()]);
        Assert.assertFalse(isZlibHeaderPresent);
    }
}