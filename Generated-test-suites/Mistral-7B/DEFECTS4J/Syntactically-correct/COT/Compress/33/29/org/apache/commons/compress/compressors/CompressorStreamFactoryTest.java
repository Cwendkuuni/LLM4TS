import org.apache.commons.compress.compressors.*;
import org.junit.Assert;
import org.junit.Test;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream_Gzip() throws Exception {
        InputStream input = new ByteArrayInputStream(GzipCompressorInputStream.GZIP_SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(GZIP, input);
        Assert.assertNotNull(compressorInputStream);
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_UnknownFormat() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[12]); // random data
        new CompressorStreamFactory().createCompressorInputStream("unknown", input);
    }

    @Test
    public void testCreateCompressorInputStream_Bzip2() throws Exception {
        InputStream input = new ByteArrayInputStream(BZip2CompressorInputStream.BZIP2_SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(BZIP2, input);
        Assert.assertNotNull(compressorInputStream);
    }

    @Test
    public void testCreateCompressorInputStream_Bzip2_DecompressUntilEOF() throws Exception {
        CompressorStreamFactory compressorStreamFactory = new CompressorStreamFactory(true);
        InputStream input = new ByteArrayInputStream(BZip2CompressorInputStream.BZIP2_SIGNATURE);
        CompressorInputStream compressorInputStream = compressorStreamFactory.createCompressorInputStream(BZIP2, input);
        Assert.assertNotNull(compressorInputStream);
        Assert.assertTrue(compressorStreamFactory.getDecompressConcatenated());
    }

    @Test
    public void testCreateCompressorInputStream_Xz() throws Exception {
        InputStream input = new ByteArrayInputStream(XZCompressorInputStream.XZ_SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(XZ, input);
        Assert.assertNotNull(compressorInputStream);
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_InvalidXz() throws Exception {
        InputStream input = new ByteArrayInputStream(XZCompressorInputStream.XZ_SIGNATURE); // valid signature
        XZInputStream xzInputStream = new XZInputStream(input);
        xzInputStream.read(); // read first byte to change the input stream signature
        CompressorStreamFactory compressorStreamFactory = new CompressorStreamFactory();
        CompressorInputStream compressorInputStream = compressorStreamFactory.createCompressorInputStream(XZ, input);
    }

    @Test
    public void testCreateCompressorInputStream_Pack200() throws Exception {
        InputStream input = new ByteArrayInputStream(Pack200CompressorInputStream.PACK200_SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(PACK200, input);
        Assert.assertNotNull(compressorInputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_Deflate() throws Exception {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(DEFLATE, output);
        Assert.assertNotNull(compressorOutputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_Bzip2() throws Exception {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(BZIP2, output);
        Assert.assertNotNull(compressorOutputStream);
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStream_UnknownFormat() throws Exception {
        OutputStream output = new ByteArrayOutputStream();
        new CompressorStreamFactory().createCompressorOutputStream("unknown", output);
    }
}