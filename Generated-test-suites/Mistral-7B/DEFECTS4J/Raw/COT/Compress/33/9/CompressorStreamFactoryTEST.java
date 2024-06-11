package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorFactory;
import org.apache.commons.compress.compressors.gzip.GzipCompressorFactory;
import org.apache.commons.compress.compressors.xz.XZCompressorFactory;
import org.apache.commons.compress.compressors.CompressorFactory;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

/**
 * Test cases for {@link CompressorStreamFactory}.
 */
public class CompressorStreamFactoryTest {
    /**
     * Test method for {@link CompressorStreamFactory#CompressorStreamFactory()}.
     */
    @org.junit.Test
    public void testConstructor() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertNull(factory.getDecompressConcatenated());
    }

    /**
     * Test method for {@link CompressorStreamFactory#CompressorStreamFactory(boolean)}.
     */
    @org.junit.Test
    public void testConstructorWithDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory(true);
        assertTrue(factory.getDecompressConcatenated());
    }

    /**
     * Test method for {@link CompressorStreamFactory#createCompressorInputStream(InputStream)}
     * when the input stream contains a valid BZIP2 signature.
     */
    @org.junit.Test
    public void testCreateCompressorInputStreamWithBZIP2() throws IOException {
        final InputStream input = new ByteArrayInputStream(BZip2CompressorInputStream.SIGNATURE);
        final CompressorInputStream result = new CompressorStreamFactory()
                .createCompressorInputStream(BZip2CompressorFactory.ID, input);
        assertSame(BZip2CompressorInputStream.class, result.getClass());
    }

    /**
     * Test method for {@link CompressorStreamFactory#createCompressorInputStream(InputStream)}
     * when the input stream contains a valid GZIP signature.
     */
    @org.junit.Test
    public void testCreateCompressorInputStreamWithGZIP() throws IOException {
        final InputStream input = new ByteArrayInputStream(GzipCompressorInputStream.SIGNATURE);
        final CompressorInputStream result = new CompressorStreamFactory()
                .createCompressorInputStream(GzipCompressorFactory.ID, input);
        assertSame(GzipCompressorInputStream.class, result.getClass());
    }

    /**
     * Test method for {@link CompressorStreamFactory#createCompressorInputStream(InputStream)}
     * when the input stream contains a valid XZ signature.
     */
    @org.junit.Test
    public void testCreateCompressorInputStreamWithXZ() throws IOException {
        final InputStream input = new ByteArrayInputStream(XZCompressorInputStream.SIGNATURE);
        final CompressorInputStream result = new CompressorStreamFactory()
                .createCompressorInputStream(XZCompressorFactory.ID, input);
        assertSame(XZCompressorInputStream.class, result.getClass());
    }

    /**
     * Test method for
     * {@link CompressorStreamFactory#createCompressorInputStream(String, InputStream)}
     * when the compressor name is not known.
     */
    @org.junit.Test
    public void testCreateCompressorInputStreamWithUnknownCompressor() {
        final InputStream input = new ByteArrayInputStream(BZip2CompressorInputStream.SIGNATURE);
        assertThrows(CompressorException.class, () -> {
            new CompressorStreamFactory().createCompressorInputStream("UNKNOWN", input);
        });
    }

    /**
     * Test method for {@link CompressorStreamFactory#createCompressorOutputStream(String, OutputStream)}.
     * when the compressor name is null.
     */
    @org.junit.Test
    public void testCreateCompressorOutputStreamWithNullCompressorName() {
        final OutputStream output = new ByteArrayOutputStream();
        assertThrows(NullPointerException.class, () -> {
            new CompressorStreamFactory().createCompressorOutputStream(null, output);
        });
    }

    /**
     * Test method for {@link CompressorStreamFactory#createCompressorOutputStream(String, OutputStream)}
     * when the compressor name is not known.
     */
    @org.junit.Test
    public void testCreateCompressorOutputStreamWithUnknownCompressor() {
        final OutputStream output = new ByteArrayOutputStream();
        assertThrows(CompressorException.class, () -> {
            new CompressorStreamFactory().createCompressorOutputStream("UNKNOWN", output);
        });
    }

    /**
     * Test method for {@link CompressorStreamFactory#createCompressorOutputStream(String, OutputStream)}
     * when the output stream is null.
     */
    @org.junit.Test
    public void testCreateCompressorOutputStreamWithNullOutputStream() {
        final String compressorName = DeflateCompressorFactory.ID;
        assertThrows(NullPointerException.class, () -> {
            new CompressorStreamFactory().createCompressorOutputStream(compressorName, null);
        });
    }

    /**
     * Test method for {@link CompressorStreamFactory#createCompressorOutputStream(String, OutputStream)}
     * when the compressor name is GZIP and decompressConcatenated is false.
     */
    @org.junit.Test
    public void testCreateCompressorOutputStreamWithGZIP() throws IOException {
        final OutputStream output = new ByteArrayOutputStream();
        final CompressorOutputStream result = new CompressorStreamFactory()
                .createCompressorOutputStream(GzipCompressorFactory.ID, output);
        assertSame(GzipCompressorOutputStream.class, result.getClass());
    }

    /**
     * Test method for {@link CompressorStreamFactory#createCompressorOutputStream(String, OutputStream)}
     * when the compressor name is BZIP2 and decompressConcatenated is false.
     */
    @org.junit.Test
    public void testCreateCompressorOutputStreamWithBZIP2() throws IOException {
        final OutputStream output = new ByteArrayOutputStream();
        final CompressorOutputStream result = new CompressorStreamFactory()
                .createCompressorOutputStream(BZip2CompressorFactory.ID, output);
        assertSame(BZip2CompressorOutputStream.class, result.getClass());
    }

    /**
     * Test method for {@link CompressorStreamFactory#createCompressorOutputStream(String, OutputStream)}
     * when the compressor name is XZ and decompressConcatenated is false.
     */
    @org.junit.Test
    public void testCreateCompressorOutputStreamWithXZ() throws IOException {
        final OutputStream output = new ByteArrayOutputStream();
        final CompressorOutputStream result = new CompressorStreamFactory()
                .createCompressorOutputStream(XZCompressorFactory.ID, output);
        assertSame(XZCompressorOutputStream.class, result.getClass());
    }

    /**
     * Test method for {@link CompressorStreamFactory#createCompressorOutputStream(String, OutputStream)}
     * when the compressor name is DEFLATE and decompressConcatenated is true.
     */
    @org.junit.Test
    public void testCreateCompressorOutputStreamWithDEFLATE() throws IOException {
        final OutputStream output = new ByteArrayOutputStream();
        final CompressorOutputStream result = new CompressorStreamFactory(true)
                .createCompressorOutputStream(DeflateCompressorFactory.ID, output);
        assertSame(DeflateCompressorOutputStream.class, result.getClass());
    }

    /**
     * Test method for {@link CompressorStreamFactory#createCompressorOutputStream(String, OutputStream)}
     * when the compressor name is DEFLATE and decompressConcatenated is false.
     */
    @org.junit.Test
    public void testCreateCompressorOutputStreamWithDEFLATE_False() throws IOException {
        final OutputStream output = new ByteArrayOutputStream();
        final CompressorOutputStream result = new CompressorStreamFactory()
                .createCompressorOutputStream(DeflateCompressorFactory.ID, output);
        assertFalse(result.isDecompressConcatenated());
    }
}