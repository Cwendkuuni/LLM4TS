package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorInputStream;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;
import org.apache.commons.compress.test.Utils;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStreamBzip2() throws IOException {
        final InputStream input = new ByteArrayInputStream(Utils.getBzip2TestData());
        final CompressorInputStream compressorInputStream =
                new CompressorStreamFactory().createCompressorInputStream("bzip2", input);
        assertInstanceOf(BZip2CompressorInputStream.class, compressorInputStream);
    }

    @Test
    public void testCreateCompressorInputStreamGzip() throws IOException {
        final InputStream input = new ByteArrayInputStream(Utils.getGzipTestData());
        final CompressorInputStream compressorInputStream =
                new CompressorStreamFactory().createCompressorInputStream("gzip", input);
        assertInstanceOf(GzipCompressorInputStream.class, compressorInputStream);
    }

    @Test
    public void testCreateCompressorInputStreamXz() throws IOException {
        final InputStream input = new ByteArrayInputStream(Utils.getXzTestData());
        final CompressorInputStream compressorInputStream =
                new CompressorStreamFactory().createCompressorInputStream("xz", input);
        assertInstanceOf(XZCompressorInputStream.class, compressorInputStream);
    }

    @Test
    public void testCreateCompressorInputStreamPack200() throws IOException {
        final InputStream input = new ByteArrayInputStream(Utils.getPack200TestData());
        final CompressorInputStream compressorInputStream =
                new CompressorStreamFactory().createCompressorInputStream("pack200", input);
        assertInstanceOf(Pack200CompressorInputStream.class, compressorInputStream);
    }

    @Test
    public void testCreateCompressorInputStreamDeflate() throws IOException {
        final InputStream input = new ByteArrayInputStream(Utils.getDeflateTestData());
        final CompressorInputStream compressorInputStream =
                new CompressorStreamFactory().createCompressorInputStream("deflate", input);
        assertInstanceOf(DeflateCompressorInputStream.class, compressorInputStream);
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStreamUnknownFormat() throws IOException {
        final InputStream input = new ByteArrayInputStream(Utils.getBzip2TestData());
        new CompressorStreamFactory().createCompressorInputStream("unknown", input);
    }

    @Test
    public void testCreateCompressorInputStreamNullInput() {
        final CompressorStreamFactory factory = new CompressorStreamFactory();
        try {
            factory.createCompressorInputStream("bzip2", null);
            assert false;
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateCompressorOutputStreamBzip2() throws IOException {
        final OutputStream output = new ByteArrayOutputStream();
        final CompressorOutputStream compressorOutputStream =
                new CompressorStreamFactory().createCompressorOutputStream("bzip2", output);
        assertInstanceOf(BZip2CompressorOutputStream.class, compressorOutputStream);
    }

    @Test
    public void testCreateCompressorOutputStreamGzip() throws IOException {
        final OutputStream output = new ByteArrayOutputStream();
        final CompressorOutputStream compressorOutputStream =
                new CompressorStreamFactory().createCompressorOutputStream("gzip", output);
        assertInstanceOf(GzipCompressorOutputStream.class, compressorOutputStream);
    }

    @Test
    public void testCreateCompressorOutputStreamXz() throws IOException {
        final OutputStream output = new ByteArrayOutputStream();
        final CompressorOutputStream compressorOutputStream =
                new CompressorStreamFactory().createCompressorOutputStream("xz", output);
        assertInstanceOf(XZCompressorOutputStream.class, compressorOutputStream);
    }

    @Test
    public void testCreateCompressorOutputStreamPack200() throws IOException {
        final OutputStream output = new ByteArrayOutputStream();
        final CompressorOutputStream compressorOutputStream =
                new CompressorStreamFactory().createCompressorOutputStream("pack200", output);
        assertInstanceOf(Pack200CompressorOutputStream.class, compressorOutputStream);
    }

    @Test
    public void testCreateCompressorOutputStreamDeflate() throws IOException {
        final OutputStream output = new ByteArrayOutputStream();
        final CompressorOutputStream compressorOutputStream =
                new CompressorStreamFactory().createCompressorOutputStream("deflate", output);
        assertInstanceOf(DeflateCompressorOutputStream.class, compressorOutputStream);
    }

    @Test
    public void testGetDecompressConcatenatedFalse() {
        final CompressorStreamFactory factory = new CompressorStreamFactory();
        assertFalse(factory.getDecompressConcatenated());
    }

    @Test
    public void testGetDecompressConcatenatedTrue() {
        final CompressorStreamFactory factory = new CompressorStreamFactory(true);
        assertTrue(factory.getDecompressConcatenated());
    }
}