package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

/**
 * Test cases for CompressorStreamFactory.
 */
public class CompressorStreamFactoryTest {
    @Test
    public void testGetDefaultFactory() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertSame(factory.getClass(), CompressorStreamFactory.class);
    }

    @Test
    public void testCreateCompressorInputStream_nullInputStream() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createCompressorInputStream(null);
        });
    }

    @Test
    public void testCreateCompressorInputStream_unsupportedSignature() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream inputStream = IOUtils.toInputStream(new byte[16]);
        assertThrows(CompressorException.class, () -> {
            factory.createCompressorInputStream(inputStream);
        });
    }

    @Test
    public void testCreateGzipCompressorInputStream() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream inputStream = IOUtils.toInputStream("Gzip compressed data\n");
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream("gzip", inputStream);
        assertSame(GzipCompressorInputStream.class, compressorInputStream.getClass());
        compressorInputStream.close();
    }

    @Test
    public void testCreateBzip2CompressorInputStream() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream inputStream = IOUtils.toInputStream(BZip2CompressorInputStream.BZIP2_MAGIC);
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream("bzip2", inputStream);
        assertSame(BZip2CompressorInputStream.class, compressorInputStream.getClass());
        compressorInputStream.close();
    }

    @Test
    public void testCreateDeflateCompressorInputStream() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream inputStream = IOUtils.toInputStream(DeflateCompressorInputStream.ZLIB_MAGIC);
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream("deflate", inputStream);
        assertSame(DeflateCompressorInputStream.class, compressorInputStream.getClass());
        compressorInputStream.close();
    }

    @Test
    public void testCreateXZCompressorInputStream() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream inputStream = IOUtils.toInputStream(new byte[16]); // XZ_MAGIC is 16 bytes long
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream("xz", inputStream);
        assertSame(XZCompressorInputStream.class, compressorInputStream.getClass());
        compressorInputStream.close();
    }

    @Test
    public void testCreateLZMACompressorInputStream() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream inputStream = IOUtils.toInputStream(LZMAUtils.LZMA_MAGIC);
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream("lzma", inputStream);
        assertSame(LZMACompressorInputStream.class, compressorInputStream.getClass());
        compressorInputStream.close();
    }

    @Test
    public void testCreatePack200CompressorInputStream() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream inputStream = IOUtils.toInputStream(new byte[8]); // PACK0_MAGIC is 8 bytes long
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream("pack200", inputStream);
        assertSame(Pack200CompressorInputStream.class, compressorInputStream.getClass());
        compressorInputStream.close();
    }

    @Test
    public void testCreateSnappyRawCompressorInputStream() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream inputStream = IOUtils.toInputStream(new byte[12]); // SNAPPY_IDENTIFIER is 12 bytes long
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream("snappy-raw", inputStream);
        assertSame(SnappyCompressorInputStream.class, compressorInputStream.getClass());
        compressorInputStream.close();
    }

    @Test
    public void testCreateSnappyFramedCompressorInputStream() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream inputStream = IOUtils.toInputStream(new byte[10]); // SNAPPY_FRAMED_IDENTIFIER is 10 bytes long
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream("snappy-framed", inputStream);
        assertSame(FramedSnappyCompressorInputStream.class, compressorInputStream.getClass());
        compressorInputStream.close();
    }

    @Test
    public void testCreateZCompressorInputStream() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream inputStream = IOUtils.toInputStream(ZCompressorInputStream.UNCOMPRESSED_Z_MAGIC);
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream("z", inputStream);
        assertSame(ZCompressorInputStream.class, compressorInputStream.getClass());
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStream_nullOutputStream() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createCompressorOutputStream(null, null);
        });
    }

    @Test
    public void testCreateGzipCompressorOutputStream() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream outputStream = IOUtils.toOutputStream(1024);
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("gzip", outputStream);
        assertSame(GzipCompressorOutputStream.class, compressorOutputStream.getClass());
        compressorOutputStream.finish();
        compressorOutputStream.close();
    }

    @Test
    public void testCreateBzip2CompressorOutputStream() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream outputStream = IOUtils.toOutputStream(1024);
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("bzip2", outputStream);
        assertSame(BZip2CompressorOutputStream.class, compressorOutputStream.getClass());
        compressorOutputStream.finish();
        compressorOutputStream.close();
    }

    @Test
    public void testCreateDeflateCompressorOutputStream() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream outputStream = IOUtils.toOutputStream(1024);
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("deflate", outputStream);
        assertSame(DeflateCompressorOutputStream.class, compressorOutputStream.getClass());
        compressorOutputStream.finish();
        compressorOutputStream.close();
    }

    @Test
    public void testCreateXZCompressorOutputStream() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream outputStream = IOUtils.toOutputStream(1024);
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("xz", outputStream);
        assertSame(XZCompressorOutputStream.class, compressorOutputStream.getClass());
        compressorOutputStream.finish();
        compressorOutputStream.close();
    }

    @Test
    public void testCreateLZMACompressorOutputStream() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream outputStream = IOUtils.toOutputStream(1024);
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("lzma", outputStream);
        assertSame(LZMACompressorOutputStream.class, compressorOutputStream.getClass());
        compressorOutputStream.finish();
        compressorOutputStream.close();
    }

    @Test
    public void testCreatePack200CompressorOutputStream() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream outputStream = IOUtils.toOutputStream(1024);
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("pack200", outputStream);
        assertSame(Pack200CompressorOutputStream.class, compressorOutputStream.getClass());
        compressorOutputStream.finish();
        compressorOutputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStream_compressionNameIsNull() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream outputStream = IOUtils.toOutputStream(1024);
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createCompressorOutputStream(null, outputStream);
        });
    }

    @Test
    public void testCreateCompressorOutputStream_compressionNameEmpty() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream outputStream = IOUtils.toOutputStream(1024);
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createCompressorOutputStream("", outputStream);
        });
    }

    @Test
    public void testCreateCompressorOutputStream_unsupportedCompressionName() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream outputStream = IOUtils.toOutputStream(1024);
        assertThrows(CompressorException.class, () -> {
            factory.createCompressorOutputStream("unknown", outputStream);
        });
    }

    @Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertFalse(factory.getDecompressConcatenated());

        CompressorStreamFactory factoryWithConcatenated = new CompressorStreamFactory(true);
        assertTrue(factoryWithConcatenated.getDecompressConcatenated());
    }
}