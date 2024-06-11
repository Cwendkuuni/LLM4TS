import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.GzipCompressorOutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CompressorStreamFactoryTest {

    private CompressorStreamFactory factory = new CompressorStreamFactory();

    @Test
    public void testCreateCompressorInputStream_GZIP() throws IOException {
        InputStream input = new ByteArrayInputStream(GzipCompressorInputStream.GZIP_SIGNATURE);
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(GZIP, input);
        Assert.assertEquals(GzipCompressorInputStream.class, compressorInputStream.getClass());
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_UnknownFormat() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[10]);
        factory.createCompressorInputStream("unknown", input);
    }

    @Test
    public void testCreateCompressorInputStream_BZIP2() throws IOException {
        InputStream input = new ByteArrayInputStream(BZip2CompressorInputStream.BZIP2_SIGNATURE);
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(BZIP2, input);
        Assert.assertEquals(BZip2CompressorInputStream.class, compressorInputStream.getClass());
    }

    @Test
    public void testCreateCompressorInputStream_BZIP2_DecompressConcatenated() throws IOException {
        InputStream input = new ByteArrayInputStream(BZip2CompressorInputStream.BZIP2_SIGNATURE);
        CompressorStreamFactory factoryWithConcatenated = new CompressorStreamFactory(true);
        CompressorInputStream compressorInputStream = factoryWithConcatenated.createCompressorInputStream(BZIP2, input);
        Assert.assertTrue(factoryWithConcatenated.getDecompressConcatenated());
        Assert.assertEquals(BZip2CompressorInputStream.class, compressorInputStream.getClass());
    }

    @Test
    public void testCreateCompressorInputStream_LZMA() throws IOException {
        InputStream input = new ByteArrayInputStream(LZMACompressorInputStream.LZMA_SIGNATURE);
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(LZMA, input);
        Assert.assertEquals(LZMACompressorInputStream.class, compressorInputStream.getClass());
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_LZMA_Unsupported() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[10]);
        factory.createCompressorInputStream(LZMA, input);
    }

    @Test
    public void testCreateCompressorInputStream_PACK200() throws IOException {
        InputStream input = new ByteArrayInputStream(Pack200CompressorInputStream.PACK200_SIGNATURE);
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(PACK200, input);
        Assert.assertEquals(Pack200CompressorInputStream.class, compressorInputStream.getClass());
    }

    @Test
    public void testCreateCompressorInputStream_DEFLATE() throws IOException {
        InputStream input = new ByteArrayInputStream(DeflateCompressorInputStream.DEFLATE_SIGNATURE);
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(DEFLATE, input);
        Assert.assertEquals(DeflateCompressorInputStream.class, compressorInputStream.getClass());
    }

    @Test
    public void testCreateCompressorOutputStream_GZIP() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(GZIP, output);
        compressorOutputStream.write("Test".getBytes());
        compressorOutputStream.finish();
        Assert.assertEquals(GzipCompressorOutputStream.class, compressorOutputStream.getClass());
        Assert.assertEquals(GzipCompressorOutputStream.GZIP_SIGNATURE, output.toByteArray());
    }

    @Test
    public void testCreateCompressorOutputStream_BZIP2() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(BZIP2, output);
        compressorOutputStream.write("Test".getBytes());
        compressorOutputStream.finish();
        Assert.assertEquals(BZip2CompressorOutputStream.class, compressorOutputStream.getClass());
        Assert.assertEquals(BZip2CompressorOutputStream.BZIP2_SIGNATURE, output.toByteArray());
    }

    @Test
    public void testCreateCompressorOutputStream_DEFLATE() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(DEFLATE, output);
        compressorOutputStream.write("Test".getBytes());
        compressorOutputStream.finish();
        Assert.assertEquals(DeflateCompressorOutputStream.class, compressorOutputStream.getClass());
        Assert.assertEquals(DeflateCompressorOutputStream.DEFLATE_SIGNATURE, output.toByteArray());
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStream_InvalidFormat() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        factory.createCompressorOutputStream("invalid", output);
    }
}