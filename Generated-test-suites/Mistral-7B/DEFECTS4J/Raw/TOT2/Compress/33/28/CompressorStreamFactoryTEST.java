import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CompressorStreamFactoryTest {

    private CompressorStreamFactory factory;
    private InputStream inputStream;
    private OutputStream outputStream;

    @Before
    public void setUp() {
        factory = new CompressorStreamFactory();
    }

    @After
    public void tearDown() {
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outputStream);
    }

    @Test
    public void testCreateCompressorInputStream_GZIP() throws IOException {
        inputStream = new ByteArrayInputStream(GzipCompressorInputStream.GZIP_SIGNATURE);
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(CompressorStreamFactory.GZIP, inputStream);
        Assert.assertNotNull(compressorInputStream);
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_UnknownFormat() throws IOException {
        inputStream = new ByteArrayInputStream("UnknownFormat".getBytes());
        factory.createCompressorInputStream("UnknownFormat", inputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_GZIP() throws IOException {
        outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.GZIP, outputStream);
        compressorOutputStream.write("Test Data".getBytes());
        compressorOutputStream.finish();
        Assert.assertEquals("Test Data", IOUtils.toString(new GzipCompressorInputStream(new ByteArrayInputStream(outputStream.toByteArray()))));
    }

    @Test
    public void testCreateCompressorOutputStream_BZIP2() throws IOException {
        outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.BZIP2, outputStream);
        compressorOutputStream.write("Test Data".getBytes());
        compressorOutputStream.finish();
        Assert.assertEquals("Test Data", IOUtils.toString(new BZip2CompressorInputStream(new ByteArrayInputStream(outputStream.toByteArray()))));
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStream_UnsupportedFormat() throws IOException {
        outputStream = new ByteArrayOutputStream();
        factory.createCompressorOutputStream(CompressorStreamFactory.LZMA, outputStream);
    }

    @Test
    public void testSetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory(true);
        Assert.assertTrue(factory.getDecompressConcatenated());
    }

    @Test
    public void testSetDecompressConcatenated_Default() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        Assert.assertFalse(factory.getDecompressConcatenated());
    }
}