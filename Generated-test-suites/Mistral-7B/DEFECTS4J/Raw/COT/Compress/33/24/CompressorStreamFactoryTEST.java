import org.apache.commons.compress.CompressorException;
import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.XZCompressorInputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * CompressorStreamFactoryTest
 */
public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream_Gzip() throws IOException, CompressorException {
        InputStream inputStream = new ByteArrayInputStream(GzipCompressorInputStream.GZIP_SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream("gzip", inputStream);
        Assert.assertEquals("gzip", compressorInputStream.getName());
    }

    @Test
    public void testCreateCompressorInputStream_Bzip2() throws IOException, CompressorException {
        InputStream inputStream = new ByteArrayInputStream(BZip2CompressorInputStream.BZIP2_SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream("bzip2", inputStream);
        Assert.assertEquals("bzip2", compressorInputStream.getName());
    }

    @Test
    public void testCreateCompressorInputStream_Xz() throws IOException, CompressorException {
        InputStream inputStream = new ByteArrayInputStream(XZCompressorInputStream.XZ_SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream("xz", inputStream);
        Assert.assertEquals("xz", compressorInputStream.getName());
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_Unknown() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("UnknownSignature".getBytes());
        new CompressorStreamFactory().createCompressorInputStream("unknown", inputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_Gzip() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream("gzip", outputStream);
        compressorOutputStream.finish();
        Assert.assertEquals("gzip", compressorOutputStream.getName());
    }

    @Test
    public void testCreateCompressorOutputStream_Bzip2() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream("bzip2", outputStream);
        compressorOutputStream.finish();
        Assert.assertEquals("bzip2", compressorOutputStream.getName());
    }

    @Test
    public void testCreateCompressorOutputStream_Xz() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream("xz", outputStream);
        compressorOutputStream.finish();
        Assert.assertEquals("xz", compressorOutputStream.getName());
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStream_Unknown() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        new CompressorStreamFactory().createCompressorOutputStream("unknown", outputStream);
    }

    @Test
    public void testCreateCompressorInputStream_Deflate() throws IOException, CompressorException {
        InputStream inputStream = new ByteArrayInputStream(DeflateCompressorInputStream.DEFLATE_SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream("deflate", inputStream);
        Assert.assertEquals("deflate", compressorInputStream.getName());
    }

    @Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory compressorStreamFactory = new CompressorStreamFactory(true);
        Assert.assertEquals(true, compressorStreamFactory.getDecompressConcatenated());

        compressorStreamFactory = new CompressorStreamFactory();
        Assert.assertEquals(false, compressorStreamFactory.getDecompressConcatenated());
    }
}