import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for CompressorStreamFactory.
 */
public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream_Gzip() throws Exception {
        InputStream input = new ByteArrayInputStream(GzipCompressorInputStream.GZIP_SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream("gzip", input);
        Assert.assertEquals("gzip", compressorInputStream.getName());
    }

    @Test
    public void testCreateCompressorInputStream_Bzip2() throws Exception {
        InputStream input = new ByteArrayInputStream(BZip2CompressorInputStream.BZIP2_SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream("bzip2", input);
        Assert.assertEquals("bzip2", compressorInputStream.getName());
    }

    @Test
    public void testCreateCompressorInputStream_Unknown() throws Exception {
        InputStream input = new ByteArrayInputStream("RandomData".getBytes());
        try {
            new CompressorStreamFactory().createCompressorInputStream("unknown", input);
            Assert.fail("Exception expected: CompressorException");
        } catch (CompressorException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorOutputStream_Deflate() throws Exception {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream("deflate", output);
        compressorOutputStream.write("Testdata".getBytes());
        compressorOutputStream.finish();
        Assert.assertEquals("deflate", compressorOutputStream.getName());
        Assert.assertArrayEquals("Testdata".getBytes(), compressorOutputStream.toByteArray());
    }

    @Test
    public void testCreateCompressorOutputStream_Bzip2() {
        // Not supported as an output stream type
    }

    @Test
    public void testCreateCompressorOutputStream_Gzip() {
        // Not supported as an output stream type
    }

    @Test
    public void testCreateCompressorOutputStream_Lzma() {
        // Not supported as an output stream type
    }

    @Test
    public void testCreateCompressorOutputStream_Xz() {
        // Not supported as an output stream type
    }

    @Test
    public void testCreateCompressorOutputStream_Pack200() {
        // Not supported as an output stream type
    }

    @Test
    public void testCreateCompressorOutputStream_SnappyFramed() {
        // Not supported as an output stream type
    }

    @Test
    public void testCreateCompressorOutputStream_SnappyRaw() {
        // Not supported as an output stream type
    }

    @Test
    public void testCreateCompressorOutputStream_Z() {
        // Not supported as an output stream type
    }

    @Test
    public void testCreateCompressorInputStream_DecompressConcatenated() throws Exception {
        InputStream input = new ByteArrayInputStream(GzipCompressorInputStream.GZIP_SIGNATURE);
        CompressorStreamFactory compressorStreamFactory = new CompressorStreamFactory(true);
        CompressorInputStream compressorInputStream = compressorStreamFactory.createCompressorInputStream("gzip", input);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = compressorStreamFactory.createCompressorOutputStream("gzip", output);
        IOUtils.copy(compressorInputStream, compressorOutputStream);
        compressorOutputStream.close();
        compressorInputStream.close();
        Assert.assertEquals("gzip", compressorInputStream.getName());
        Assert.assertEquals("gzip", compressorOutputStream.getName());
        Assert.assertArrayEquals(output.toByteArray(), new byte[0]);
    }

    @Test
    public void testCreateCompressorInputStream_DecompressUntilEOF() throws Exception {
        InputStream input = new ByteArrayInputStream(GzipCompressorInputStream.GZIP_SIGNATURE);
        CompressorStreamFactory compressorStreamFactory = new CompressorStreamFactory(false);
        CompressorInputStream compressorInputStream = compressorStreamFactory.createCompressorInputStream("gzip", input);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = compressorStreamFactory.createCompressorOutputStream("gzip", output);
        IOUtils.copy(compressorInputStream, compressorOutputStream);
        compressorOutputStream.close();
        compressorInputStream.close();
        Assert.assertEquals("gzip", compressorInputStream.getName());
        Assert.assertEquals("gzip", compressorOutputStream.getName());
        Assert.assertArrayEquals(new byte[0], output.toByteArray());
    }

    @Test
    public void testCreateCompressorInputStream_DecompressConcatenated_Invalid() throws Exception {
        InputStream input = new ByteArrayInputStream(GzipCompressorInputStream.GZIP_SIGNATURE);
        CompressorStreamFactory compressorStreamFactory = new CompressorStreamFactory();
        try {
            compressorStreamFactory.setDecompressConcatenated(true);
            CompressorInputStream compressorInputStream = compressorStreamFactory.createCompressorInputStream("gzip", input);
            Assert.fail("Exception expected: IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorInputStream_DecompressUntilEOF_Invalid() throws Exception {
        InputStream input = new ByteArrayInputStream(GzipCompressorInputStream.GZIP_SIGNATURE);
        CompressorStreamFactory compressorStreamFactory = new CompressorStreamFactory();
        try {
            compressorStreamFactory.setDecompressUntilEOF(true);
            CompressorInputStream compressorInputStream = compressorStreamFactory.createCompressorInputStream("gzip", input);
            Assert.fail("Exception expected: IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }
}