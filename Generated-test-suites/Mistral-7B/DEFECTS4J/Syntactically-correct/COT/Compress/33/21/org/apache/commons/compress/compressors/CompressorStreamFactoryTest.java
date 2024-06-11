import org.apache.commons.compress.CompressorException;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

public class CompressorStreamFactoryTest {

    private static final CompressorStreamFactory factory = new CompressorStreamFactory();

    @Test
    public void testCreateBZip2CompressorInputStream() throws Exception {
        InputStream inputStream = createTestBZip2InputStream();
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(BZIP2, inputStream);
        Assert.assertEquals(BZip2CompressorInputStream.class, compressorInputStream.getClass());
        compressorInputStream.close();
    }

    @Test
    public void testCreateGzipCompressorInputStream() throws Exception {
        InputStream inputStream = createTestGzipInputStream();
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(GZIP, inputStream);
        Assert.assertEquals(GzipCompressorInputStream.class, compressorInputStream.getClass());
        compressorInputStream.close();
    }

    @Test
    public void testCreateXZCompressorInputStream() throws Exception {
        InputStream inputStream = createTestXZInputStream();
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(XZ, inputStream);
        Assert.assertEquals(XZCompressorInputStream.class, compressorInputStream.getClass());
        compressorInputStream.close();
    }

    @Test(expected = CompressorException.class)
    public void testCreateUnknownCompressorInputStream() throws Exception {
        InputStream inputStream = createTestXZInputStream();
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream("unknown", inputStream);
    }

    @Test
    public void testCreateBZip2CompressorOutputStream() throws Exception {
        OutputStream outputStream = createTestOutputStream();
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(BZIP2, outputStream);
        compressorOutputStream.close();
        Assert.assertEquals(BZip2CompressorOutputStream.class, compressorOutputStream.getClass());
    }

    @Test
    public void testCreateGzipCompressorOutputStream() throws Exception {
        OutputStream outputStream = createTestOutputStream();
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(GZIP, outputStream);
        compressorOutputStream.close();
        Assert.assertEquals(GzipCompressorOutputStream.class, compressorOutputStream.getClass());
    }

    @Test
    public void testCreateXZCompressorOutputStream() throws Exception {
        OutputStream outputStream = createTestOutputStream();
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(XZ, outputStream);
        compressorOutputStream.close();
        Assert.assertEquals(XZCompressorOutputStream.class, compressorOutputStream.getClass());
    }

    @Test(expected = CompressorException.class)
    public void testCreateUnknownCompressorOutputStream() throws Exception {
        OutputStream outputStream = createTestOutputStream();
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("unknown", outputStream);
    }

    @Test
    public void testGetDecompressConcatenated() {
        Assert.assertFalse(factory.getDecompressConcatenated());
    }

    @Test
    public void testSetDecompressConcatenatedAndCreateBZip2CompressorInputStream() throws Exception {
        InputStream inputStream = createTestBZip2InputStream();
        factory.setDecompressConcatenated(true);
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(BZIP2, inputStream);
        Assert.assertTrue(compressorInputStream.isDecompressConcatenated());
        compressorInputStream.close();
    }

    @Test
    public void testSetDecompressConcatenatedAndCreateGzipCompressorInputStream() throws Exception {
        InputStream inputStream = createTestGzipInputStream();
        factory.setDecompressConcatenated(true);
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(GZIP, inputStream);
        Assert.assertFalse(compressorInputStream.isDecompressConcatenated());
        compressorInputStream.close();
    }

    @Test
    public void testSetDecompressConcatenatedAndCreateXZCompressorInputStream() throws Exception {
        InputStream inputStream = createTestXZInputStream();
        factory.setDecompressConcatenated(true);
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(XZ, inputStream);
        Assert.assertTrue(compressorInputStream.isDecompressConcatenated());
        compressorInputStream.close();
    }

    @Test
    public void testSetDecompressConcatenatedAndCreateDeflateCompressorInputStream() throws Exception {
        InputStream inputStream = createTestDeflateInputStream();
        factory.setDecompressConcatenated(true);
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(DEFLATE, inputStream);
        Assert.assertFalse(compressorInputStream.isDecompressConcatenated());
        compressorInputStream.close();
    }

    private InputStream createTestBZip2InputStream() throws IOException {
        return IOUtils.toInputStream(TEST_BZIP2_DATA);
    }

    private InputStream createTestGzipInputStream() throws IOException {
        return IOUtils.toInputStream(TEST_GZIP_DATA);
    }

    private InputStream createTestXZInputStream() throws IOException {
        return IOUtils.toInputStream(TEST_XZ_DATA);
    }

    private InputStream createTestDeflateInputStream() throws IOException {
        return IOUtils.toInputStream(TEST_DEFLATE_DATA);
    }

    private OutputStream createTestOutputStream() throws IOException {
        return IOUtils.toOutputStream(TEST_OUTPUT);
    }

    private static final byte[] TEST_BZIP2_DATA = new byte[10];
    private static final byte[] TEST_GZIP_DATA = new byte[10];
    private static final byte[] TEST_XZ_DATA = new byte[10];
    private static final byte[] TEST_DEFLATE_DATA = new byte[10];
    private static final byte[] TEST_OUTPUT = new byte[10];

}