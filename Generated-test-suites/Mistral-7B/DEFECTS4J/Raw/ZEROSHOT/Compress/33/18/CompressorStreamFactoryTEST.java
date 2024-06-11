import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Tests for the CompressorStreamFactory class.
 */
public class CompressorStreamFactoryTest {

    /**
     * Test creating a compressor input stream from a known signature.
     */
    @Test
    public void testCreateCompressorInputStreamFromKnownSignature() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(BZip2CompressorInputStream.SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream("bzip2", inputStream);
        Assert.assertNotNull(compressorInputStream);
        Assert.assertEquals(BZip2CompressorInputStream.class, compressorInputStream.getClass());
    }

    /**
     * Test creating a compressor input stream from an unknown signature.
     */
    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStreamFromUnknownSignature() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[12]);
        new CompressorStreamFactory().createCompressorInputStream("unknown", inputStream);
    }

    /**
     * Test creating a compressor input stream from a known name.
     */
    @Test
    public void testCreateCompressorInputStreamFromKnownName() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(BZip2CompressorInputStream.SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream("bzip2", inputStream);
        Assert.assertNotNull(compressorInputStream);
        Assert.assertEquals(BZip2CompressorInputStream.class, compressorInputStream.getClass());
    }

    /**
     * Test creating a compressor input stream from an unknown name.
     */
    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStreamFromUnknownName() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(BZip2CompressorInputStream.SIGNATURE);
        new CompressorStreamFactory().createCompressorInputStream("unknown", inputStream);
    }

    /**
     * Test creating a compressor output stream from a known name.
     */
    @Test
    public void testCreateCompressorOutputStreamFromKnownName() throws IOException {
        final OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream("bzip2", outputStream);
        Assert.assertNotNull(compressorOutputStream);
        Assert.assertEquals(BZip2CompressorOutputStream.class, compressorOutputStream.getClass());
    }

    /**
     * Test creating a compressor output stream from an unknown name.
     */
    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStreamFromUnknownName() throws IOException {
        final OutputStream outputStream = new ByteArrayOutputStream();
        new CompressorStreamFactory().createCompressorOutputStream("unknown", outputStream);
    }

    /**
     * Test creating a compressor output stream from a known signature.
     */
    @Test
    public void testCreateCompressorOutputStreamFromKnownSignature() throws IOException {
        final OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream("bzip2", outputStream);
        Assert.assertNotNull(compressorOutputStream);
        Assert.assertEquals(BZip2CompressorOutputStream.class, compressorOutputStream.getClass());
    }

    /**
     * Test creating a compressor output stream from an unknown signature.
     */
    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStreamFromUnknownSignature() throws IOException {
        final OutputStream outputStream = new ByteArrayOutputStream();
        new CompressorStreamFactory().createCompressorOutputStream("unknown", outputStream);
    }

    /**
     * Test creating a compressor input stream using the default decompress concatenated mode.
     */
    @Test
    public void testCreateCompressorInputStreamWithDefaultDecompressConcatenated() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(GzipCompressorInputStream.SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(inputStream);
        Assert.assertNotNull(compressorInputStream);
        Assert.assertTrue(compressorInputStream.isDecompressConcatenated());
    }

    /**
     * Test creating a compressor input stream with decompress concatenated mode disabled.
     */
    @Test
    public void testCreateCompressorInputStreamWithDecompressConcatenatedDisabled() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(GzipCompressorInputStream.SIGNATURE);
        CompressorStreamFactory compressorStreamFactory = new CompressorStreamFactory(false);
        CompressorInputStream compressorInputStream = compressorStreamFactory.createCompressorInputStream(inputStream);
        Assert.assertNotNull(compressorInputStream);
        Assert.assertFalse(compressorInputStream.isDecompressConcatenated());
    }

    /**
     * Test creating a compressor output stream with decompress concatenated mode disabled.
     */
    @Test
    public void testCreateCompressorOutputStreamWithDecompressConcatenatedDisabled() throws IOException {
        final OutputStream outputStream = new ByteArrayOutputStream();
        CompressorStreamFactory compressorStreamFactory = new CompressorStreamFactory(false);
        CompressorOutputStream compressorOutputStream = compressorStreamFactory.createCompressorOutputStream("gzip", outputStream);
        Assert.assertNotNull(compressorOutputStream);
        Assert.assertFalse(compressorOutputStream.isDecompressConcatenated());
    }

    /**
     * Test creating a compressor output stream with decompress concatenated mode enabled.
     */
    @Test
    public void testCreateCompressorOutputStreamWithDecompressConcatenatedEnabled() throws IOException {
        final OutputStream outputStream = new ByteArrayOutputStream();
        CompressorStreamFactory compressorStreamFactory = new CompressorStreamFactory();
        CompressorOutputStream compressorOutputStream = compressorStreamFactory.createCompressorOutputStream("gzip", outputStream);
        Assert.assertNotNull(compressorOutputStream);
        Assert.assertTrue(compressorOutputStream.isDecompressConcatenated());
    }

    /**
     * Test the getDecompressConcatenated method.
     */
    @Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory compressorStreamFactory = new CompressorStreamFactory();
        Assert.assertFalse(compressorStreamFactory.getDecompressConcatenated());

        compressorStreamFactory = new CompressorStreamFactory(true);
        Assert.assertTrue(compressorStreamFactory.getDecompressConcatenated());
    }
}