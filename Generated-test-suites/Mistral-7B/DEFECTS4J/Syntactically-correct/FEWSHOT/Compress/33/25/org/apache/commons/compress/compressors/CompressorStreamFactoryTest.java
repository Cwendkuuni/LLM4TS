package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZUtils;
import org.apache.commons.compress.compressors.CompressorFactory;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Test cases for CompressorStreamFactory.
 */
public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorFactory() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertNull(factory.getDecompressConcatenated());

        factory = new CompressorStreamFactory(false);
        assertFalse(factory.getDecompressConcatenated());

        factory = new CompressorStreamFactory(true);
        assertTrue(factory.getDecompressConcatenated());
    }

    @Test
    public void testCreateBZip2CompressorInputStream() throws IOException {
        InputStream testStream = new ByteArrayInputStream(
                BZip2CompressorInputStream.BZIP2_MAGIC.getBytes());
        CompressorFactory factory = new CompressorStreamFactory();
        CompressorInputStream in = factory.createCompressorInputStream("bzip2", testStream);
        assertSame(BZip2CompressorInputStream.class, in.getClass());
    }

    @Test(expected = CompressorException.class)
    public void testCreateUnsupportedCompressorInputStream() throws IOException {
        InputStream testStream = new ByteArrayInputStream("test".getBytes());
        CompressorFactory factory = new CompressorStreamFactory();
        factory.createCompressorInputStream("unknown", testStream);
    }

    @Test
    public void testCreateGZipCompressorInputStream() throws IOException {
        InputStream testStream = new ByteArrayInputStream(GzipCompressorInputStream.GZIP_MAGIC.getBytes());
        CompressorFactory factory = new CompressorStreamFactory();
        CompressorInputStream in = factory.createCompressorInputStream("gzip", testStream);
        assertSame(GzipCompressorInputStream.class, in.getClass());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetDecompressConcatenatedAfterConstruction() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.setDecompressConcatenated(true);
    }

    @Test
    public void testCreateXZCompressorInputStream() throws IOException {
        InputStream testStream = new ByteArrayInputStream(XZ_MAGIC.getBytes());
        CompressorFactory factory = new CompressorStreamFactory();
        CompressorInputStream in = factory.createCompressorInputStream("xz", testStream);
        assertSame(XZCompressorInputStream.class, in.getClass());
    }

    @Test(expected = CompressorException.class)
    public void testCreateUnsupportedCompressorOutputStream() {
        OutputStream testStream = new ByteArrayOutputStream();
        String compressorName = "unknown";
        CompressorFactory factory = new CompressorStreamFactory();
        factory.createCompressorOutputStream(compressorName, testStream);
    }

    @Test
    public void testCreateDeflateCompressorOutputStream() {
        OutputStream testStream = new ByteArrayOutputStream();
        CompressorFactory factory = new CompressorStreamFactory();
        CompressorOutputStream out = factory.createCompressorOutputStream("deflate", testStream);
        assertSame(DeflateCompressorOutputStream.class, out.getClass());
    }

    @Test
    public void testCreateBZip2CompressorOutputStream() {
        OutputStream testStream = new ByteArrayOutputStream();
        CompressorFactory factory = new CompressorStreamFactory();
        CompressorOutputStream out = factory.createCompressorOutputStream("bzip2", testStream);
        assertSame(BZip2CompressorOutputStream.class, out.getClass());
    }

    @Test
    public void testCreateGZipCompressorOutputStream() {
        OutputStream testStream = new ByteArrayOutputStream();
        CompressorFactory factory = new CompressorStreamFactory();
        CompressorOutputStream out = factory.createCompressorOutputStream("gzip", testStream);
        assertSame(GzipCompressorOutputStream.class, out.getClass());
    }

    @Test
    public void testCreateXZCompressorOutputStream() {
        OutputStream testStream = new ByteArrayOutputStream();
        CompressorFactory factory = new CompressorStreamFactory();
        CompressorOutputStream out = factory.createCompressorOutputStream("xz", testStream);
        assertSame(XZCompressorOutputStream.class, out.getClass());
    }

    @Test
    public void testCreatePack200CompressorOutputStream() {
        OutputStream testStream = new ByteArrayOutputStream();
        CompressorFactory factory = new CompressorStreamFactory();
        CompressorOutputStream out = factory.createCompressorOutputStream("pack200", testStream);
        assertSame(Pack200CompressorOutputStream.class, out.getClass());
    }

    @Test
    public void testCreateSnappyRawCompressorOutputStream() {
        OutputStream testStream = new ByteArrayOutputStream();
        CompressorFactory factory = new CompressorStreamFactory();
        CompressorOutputStream out = factory.createCompressorOutputStream("snappy-raw", testStream);
        assertSame(SnappyCompressorOutputStream.class, out.getClass());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateSnappyFramedCompressorOutputStream() {
        OutputStream testStream = new ByteArrayOutputStream();
        CompressorFactory factory = new CompressorStreamFactory();
        factory.createCompressorOutputStream("snappy-framed", testStream);
    }

    @Test
    public void testCreateZCompressorOutputStream() {
        OutputStream testStream = new ByteArrayOutputStream();
        CompressorFactory factory = new CompressorStreamFactory();
        CompressorOutputStream out = factory.createCompressorOutputStream("z", testStream);
        assertSame(ZCompressorOutputStream.class, out.getClass());
    }

    private static final byte[] XZ_MAGIC = XZUtils.XZ_MAGIC;
}