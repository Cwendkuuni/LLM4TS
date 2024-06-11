import org.apache.commons.compress.CompressorException;
import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.XZCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.apache.commons.compress.compressors.CompressorStreamFactory.BZIP2;
import static org.apache.commons.compress.compressors.CompressorStreamFactory.DEFLATE;
import static org.apache.commons.compress.compressors.CompressorStreamFactory.GZIP;
import static org.apache.commons.compress.compressors.CompressorStreamFactory.XZ;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStreamBZIP2() throws CompressorException, IOException {
        InputStream inputStream = new ByteArrayInputStream(BZip2CompressorInputStream.BZIP2_SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(BZIP2, inputStream);
        Assert.assertNotNull(compressorInputStream);
        IOUtils.closeQuietly(compressorInputStream);
    }

    @Test
    public void testCreateCompressorInputStreamGZIP() throws CompressorException, IOException {
        InputStream inputStream = new ByteArrayInputStream(GzipCompressorInputStream.GZIP_SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(GZIP, inputStream);
        Assert.assertNotNull(compressorInputStream);
        IOUtils.closeQuietly(compressorInputStream);
    }

    @Test
    public void testCreateCompressorInputStreamXZ() throws CompressorException, IOException {
        InputStream inputStream = new ByteArrayInputStream(XZCompressorInputStream.XZ_SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(XZ, inputStream);
        Assert.assertNotNull(compressorInputStream);
        IOUtils.closeQuietly(compressorInputStream);
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStreamInvalidSignature() throws CompressorException, IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[12]);
        new CompressorStreamFactory().createCompressorInputStream(BZIP2, inputStream);
    }

    @Test
    public void testCreateCompressorOutputStreamBZIP2() throws CompressorException, IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(BZIP2, outputStream);
        compressorOutputStream.write(new byte[1024]);
        compressorOutputStream.finish();
        IOUtils.closeQuietly(compressorOutputStream);
        Assert.assertEquals(BZip2CompressorOutputStream.BZIP2_SIGNATURE, outputStream.toByteArray());
    }

    @Test
    public void testCreateCompressorOutputStreamGZIP() throws CompressorException, IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(GZIP, outputStream);
        compressorOutputStream.write(new byte[1024]);
        compressorOutputStream.finish();
        IOUtils.closeQuietly(compressorOutputStream);
        Assert.assertEquals(GzipCompressorOutputStream.GZIP_SIGNATURE, outputStream.toByteArray());
    }

    @Test
    public void testCreateCompressorOutputStreamXZ() throws CompressorException, IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(XZ, outputStream);
        compressorOutputStream.write(new byte[1024]);
        compressorOutputStream.finish();
        IOUtils.closeQuietly(compressorOutputStream);
        Assert.assertEquals(XZCompressorOutputStream.XZ_SIGNATURE, outputStream.toByteArray());
    }

    @Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        Assert.assertFalse(factory.getDecompressConcatenated());

        CompressorStreamFactory factoryConcatenated = new CompressorStreamFactory(true);
        Assert.assertTrue(factoryConcatenated.getDecompressConcatenated());
    }
}