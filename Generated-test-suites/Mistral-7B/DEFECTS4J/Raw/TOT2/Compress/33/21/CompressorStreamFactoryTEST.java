import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.snappy.SnappyCompressorInputStream;
import org.apache.commons.compress.test.TestUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream_GZip() throws IOException, CompressorException {
        String name = CompressorStreamFactory.GZIP;
        InputStream inputStream = new ByteArrayInputStream(TestUtils.GZIP_DATA);
        CompressorInputStream compressorInputStream = CompressorStreamFactory.newInstance().createCompressorInputStream(name, inputStream);
        Assert.assertEquals(CompressorStreamFactory.GZIP, compressorInputStream.getFormatName());
        IOUtils.assertEquals(TestUtils.GZIP_DATA, IOUtils.toByteArray(compressorInputStream));
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStream_BZip2() throws IOException, CompressorException {
        String name = CompressorStreamFactory.BZIP2;
        InputStream inputStream = new ByteArrayInputStream(TestUtils.BZIP2_DATA);
        CompressorInputStream compressorInputStream = CompressorStreamFactory.newInstance().createCompressorInputStream(name, inputStream);
        Assert.assertEquals(CompressorStreamFactory.BZIP2, compressorInputStream.getFormatName());
        IOUtils.assertEquals(TestUtils.BZIP2_DATA, IOUtils.toByteArray(compressorInputStream));
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStream_Deflate() throws IOException, CompressorException {
        String name = CompressorStreamFactory.DEFLATE;
        InputStream inputStream = new ByteArrayInputStream(TestUtils.DEFLATE_DATA);
        CompressorInputStream compressorInputStream = CompressorStreamFactory.newInstance().createCompressorInputStream(name, inputStream);
        Assert.assertEquals(CompressorStreamFactory.DEFLATE, compressorInputStream.getFormatName());
        IOUtils.assertEquals(TestUtils.DEFLATE_DATA, IOUtils.toByteArray(compressorInputStream));
        compressorInputStream.close();
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_UnsupportedFormat() throws IOException, CompressorException {
        String name = "unsupported";
        InputStream inputStream = new ByteArrayInputStream(TestUtils.GZIP_DATA);
        CompressorStreamFactory compressorStreamFactory = CompressorStreamFactory.newInstance();
        compressorStreamFactory.setDecompressConcatenated(false); // to avoid error message "This setting applies to the gzip, bzip2 and xz formats only."
        CompressorInputStream compressorInputStream = compressorStreamFactory.createCompressorInputStream(name, inputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_GZip() throws IOException {
        String name = CompressorStreamFactory.GZIP;
        OutputStream outputStream = new ByteArrayOutputStream(TestUtils.GZIP_DATA.length);
        CompressorOutputStream compressorOutputStream = CompressorStreamFactory.newInstance().createCompressorOutputStream(name, outputStream);
        byte[] dataToWrite = TestUtils.GZIP_DATA;
        compressorOutputStream.write(dataToWrite, 0, dataToWrite.length);
        compressorOutputStream.finish();
        Assert.assertEquals(TestUtils.GZIP_DATA, outputStream.toByteArray());
        compressorOutputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStream_BZip2() throws IOException {
        String name = CompressorStreamFactory.BZIP2;
        OutputStream outputStream = new ByteArrayOutputStream(TestUtils.BZIP2_DATA.length);
        CompressorOutputStream compressorOutputStream = CompressorStreamFactory.newInstance().createCompressorOutputStream(name, outputStream);
        byte[] dataToWrite = TestUtils.BZIP2_DATA;
        compressorOutputStream.write(dataToWrite, 0, dataToWrite.length);
        compressorOutputStream.finish();
        Assert.assertEquals(TestUtils.BZIP2_DATA, outputStream.toByteArray());
        compressorOutputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStream_Deflate() throws IOException {
        String name = CompressorStreamFactory.DEFLATE;
        OutputStream outputStream = new ByteArrayOutputStream(TestUtils.DEFLATE_DATA.length);
        CompressorOutputStream compressorOutputStream = CompressorStreamFactory.newInstance().createCompressorOutputStream(name, outputStream);
        byte[] dataToWrite = TestUtils.DEFLATE_DATA;
        compressorOutputStream.write(dataToWrite, 0, dataToWrite.length);
        compressorOutputStream.finish();
        Assert.assertEquals(TestUtils.DEFLATE_DATA, outputStream.toByteArray());
        compressorOutputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStream_DifferentFormat() throws IOException {
        String name = CompressorStreamFactory.BZIP2;
        OutputStream outputStream = new ByteArrayOutputStream(TestUtils.GZIP_DATA.length);
        CompressorOutputStream compressorOutputStream = CompressorStreamFactory.newInstance().createCompressorOutputStream(name, outputStream);
        byte[] dataToWrite = TestUtils.GZIP_DATA;
        compressorOutputStream.write(dataToWrite, 0, dataToWrite.length);
        compressorOutputStream.finish();
        Assert.assertNotEquals(TestUtils.GZIP_DATA, outputStream.toByteArray());
        compressorOutputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStream_SameFormat() throws IOException {
        String name = CompressorStreamFactory.GZIP;
        OutputStream outputStream = new ByteArrayOutputStream(TestUtils.GZIP_DATA.length);
        CompressorOutputStream compressorOutputStream = CompressorStreamFactory.newInstance().createCompressorOutputStream(name, outputStream);
        byte[] dataToWrite = TestUtils.GZIP_DATA;
        compressorOutputStream.write(dataToWrite, 0, dataToWrite.length);
        compressorOutputStream.finish();
        Assert.assertEquals(TestUtils.GZIP_DATA, outputStream.toByteArray());
        compressorOutputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStream_NullName() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        Assert.assertNull(CompressorStreamFactory.newInstance().createCompressorOutputStream(null, outputStream));
    }

    @Test
    public void testCreateCompressorOutputStream_NullOutputStream() throws IOException {
        String name = CompressorStreamFactory.GZIP;
        Assert.assertNull(CompressorStreamFactory.newInstance().createCompressorOutputStream(name, null));
    }

    @Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory compressorStreamFactory = CompressorStreamFactory.newInstance();
        Assert.assertFalse(compressorStreamFactory.getDecompressConcatenated());
        compressorStreamFactory = new CompressorStreamFactory(false);
        Assert.assertFalse(compressorStreamFactory.getDecompressConcatenated());
        compressorStreamFactory = new CompressorStreamFactory(true);
        Assert.assertTrue(compressorStreamFactory.getDecompressConcatenated());
    }
}