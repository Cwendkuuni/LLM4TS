package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.Pack200CompressorInputStream;
import org.apache.commons.compress.compressors.XZCompressorInputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.junit.Test;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream_Gzip() {
        InputStream inputStream = new ByteArrayInputStream("Gzip signature".getBytes());
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream("gzip", inputStream);
        assertInstanceOf(GzipCompressorInputStream.class, compressorInputStream);
    }

    @Test
    public void testCreateCompressorInputStream_Bzip2() {
        InputStream inputStream = new ByteArrayInputStream("Bzip2 signature".getBytes());
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream("bzip2", inputStream);
        assertInstanceOf(BZip2CompressorInputStream.class, compressorInputStream);
    }

    @Test
    public void testCreateCompressorInputStream_XZ() {
        InputStream inputStream = new ByteArrayInputStream("XZ signature".getBytes());
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream("xz", inputStream);
        assertInstanceOf(XZCompressorInputStream.class, compressorInputStream);
    }

    @Test
    public void testCreateCompressorInputStream_Pack200() {
        InputStream inputStream = new ByteArrayInputStream("Pack200 signature".getBytes());
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream("pack200", inputStream);
        assertInstanceOf(Pack200CompressorInputStream.class, compressorInputStream);
    }

    @Test
    public void testCreateCompressorInputStream_Unknown() {
        InputStream inputStream = new ByteArrayInputStream("Unknown signature".getBytes());
        assertThrows(CompressorException.class, () -> new CompressorStreamFactory().createCompressorInputStream("unknown", inputStream));
    }

    @Test
    public void testCreateCompressorInputStream_NullInput() {
        assertThrows(IllegalArgumentException.class, () -> new CompressorStreamFactory().createCompressorInputStream("gzip", null));
    }

    @Test
    public void testCreateCompressorOutputStream_Gzip() {
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream("gzip", outputStream);
        assertInstanceOf(GzipCompressorOutputStream.class, compressorOutputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_Bzip2() {
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream("bzip2", outputStream);
        assertInstanceOf(BZip2CompressorOutputStream.class, compressorOutputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_XZ() {
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream("xz", outputStream);
        assertInstanceOf(XZCompressorOutputStream.class, compressorOutputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_Pack200() {
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream("pack200", outputStream);
        assertInstanceOf(Pack200CompressorOutputStream.class, compressorOutputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_Deflate() {
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream("deflate", outputStream);
        assertInstanceOf(DeflateCompressorOutputStream.class, compressorOutputStream);
    }

    @Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory compressorStreamFactory = new CompressorStreamFactory();
        assertFalse(compressorStreamFactory.getDecompressConcatenated());

        CompressorStreamFactory compressorStreamFactoryConcatenated = new CompressorStreamFactory(true);
        assertTrue(compressorStreamFactoryConcatenated.getDecompressConcatenated());
    }
}