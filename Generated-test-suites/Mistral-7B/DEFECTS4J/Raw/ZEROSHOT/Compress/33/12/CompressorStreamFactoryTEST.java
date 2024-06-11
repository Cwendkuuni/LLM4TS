package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link CompressorStreamFactory} class
 */
public class CompressorStreamFactoryTest {

    private CompressorStreamFactory factory;

    @Before
    public void setUp() {
        factory = new CompressorStreamFactory();
    }

    @Test
    public void shouldCreateBZip2CompressorInputStreamFromFile() throws IOException {
        final byte[] bzip2Signature = new byte[12];
        IOUtils.write(BZip2CompressorInputStream.BZIP2_SIGNATURE, 0, bzip2Signature, 0, bzip2Signature.length);

        final InputStream inputStream = new ByteArrayInputStream(bzip2Signature);
        final CompressorInputStream compressorInputStream = factory.createCompressorInputStream("bzip2", inputStream);
        assertInstanceOf(BZip2CompressorInputStream.class, compressorInputStream);
    }

    @Test
    public void shouldCreateBZip2CompressorInputStreamFromInputStream() throws IOException {
        final byte[] bzip2Signature = new byte[12];
        IOUtils.write(BZip2CompressorInputStream.BZIP2_SIGNATURE, 0, bzip2Signature, 0, bzip2Signature.length);

        final InputStream inputStream = new ByteArrayInputStream(bzip2Signature);
        final OutputStream outputStream = new ByteArrayOutputStream();
        final CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("bzip2", outputStream);
        compressorOutputStream.write(BZip2CompressorInputStream.BZIP2_SIGNATURE, 0, BZip2CompressorInputStream.BZIP2_SIGNATURE.length);
        compressorOutputStream.close();

        final InputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        final CompressorInputStream compressorInputStream = factory.createCompressorInputStream("bzip2", testInputStream);
        assertInstanceOf(BZip2CompressorInputStream.class, compressorInputStream);
    }

    @Test(expected = CompressorException.class)
    public void shouldThrowCompressorExceptionWhenUnknownCompressorIsDetected() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[12]);
        factory.createCompressorInputStream("unknown", inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenInputStreamIsNull() {
        factory.createCompressorInputStream("bzip2", null);
    }

    @Test(expected = CompressorException.class)
    public void shouldThrowCompressorExceptionWhenUnsupportedCompressorIsDetected() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(GzipCompressorInputStream.GZIP_SIGNATURE);
        factory.createCompressorInputStream("xz", inputStream);
    }

    @Test
    public void shouldCreateBZip2CompressorOutputStreamFromFile() throws IOException {
        final InputStream inputStream = new FileInputStream("inputFile.bz2");
        final OutputStream outputStream = new ByteArrayOutputStream();
        factory.createCompressorOutputStream("bzip2", outputStream, inputStream);
        inputStream.close();

        final InputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        final CompressorInputStream compressorInputStream = factory.createCompressorInputStream("bzip2", testInputStream);
        assertInstanceOf(BZip2CompressorInputStream.class, compressorInputStream);
    }

    @Test
    public void shouldCreateBZip2CompressorOutputStreamFromInputStream() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[1]);
        final OutputStream outputStream = new ByteArrayOutputStream();
        factory.createCompressorOutputStream("bzip2", outputStream, inputStream);
        inputStream.close();

        final InputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        final CompressorInputStream compressorInputStream = factory.createCompressorInputStream("bzip2", testInputStream);
        assertInstanceOf(BZip2CompressorInputStream.class, compressorInputStream);
    }

    @Test
    public void shouldCreateGzipCompressorOutputStreamFromFile() throws IOException {
        final InputStream inputStream = new FileInputStream("inputFile.gz");
        final OutputStream outputStream = new ByteArrayOutputStream();
        factory.createCompressorOutputStream("gzip", outputStream, inputStream);
        inputStream.close();

        final InputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        final CompressorInputStream compressorInputStream = factory.createCompressorInputStream("gzip", testInputStream);
        assertInstanceOf(GzipCompressorInputStream.class, compressorInputStream);
    }

    @Test
    public void shouldCreateGzipCompressorOutputStreamFromInputStream() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[1]);
        final OutputStream outputStream = new ByteArrayOutputStream();
        factory.createCompressorOutputStream("gzip", outputStream, inputStream);
        inputStream.close();

        final InputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        final CompressorInputStream compressorInputStream = factory.createCompressorInputStream("gzip", testInputStream);
        assertInstanceOf(GzipCompressorInputStream.class, compressorInputStream);
    }

    @Test(expected = CompressorException.class)
    public void shouldThrowCompressorExceptionWhenInputstreamIsNotMarkSupported() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[1]);
        factory.createCompressorInputStream("bzip2", inputStream);
    }

    @Test
    public void shouldCreateXZCompressorOutputStreamFromFile() throws IOException {
        final InputStream inputStream = new FileInputStream("inputFile.xz");
        final OutputStream outputStream = new ByteArrayOutputStream();
        factory.createCompressorOutputStream("xz", outputStream, inputStream);
        inputStream.close();

        final InputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        final CompressorInputStream compressorInputStream = factory.createCompressorInputStream("xz", testInputStream);
        assertInstanceOf(XZCompressorInputStream.class, compressorInputStream);
    }

    @Test
    public void shouldCreateXZCompressorOutputStreamFromInputStream() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[1]);
        final OutputStream outputStream = new ByteArrayOutputStream();
        factory.createCompressorOutputStream("xz", outputStream, inputStream);
        inputStream.close();

        final InputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        final CompressorInputStream compressorInputStream = factory.createCompressorInputStream("xz", testInputStream);
        assertInstanceOf(XZCompressorInputStream.class, compressorInputStream);
    }

    @Test(expected = CompressorException.class)
    public void shouldThrowCompressorExceptionWhenFailedToDetectCompressor() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final InputStream inputStream = new ByteArrayInputStream(new byte[1]);
        factory.createCompressorOutputStream("unknown", outputStream, inputStream);
        inputStream.close();
    }

    @Test
    public void shouldCreateDeflateCompressorOutputStreamFromFile() throws IOException {
        final InputStream inputStream = new FileInputStream("inputFile.zip");
        final OutputStream outputStream = new ByteArrayOutputStream();
        factory.createCompressorOutputStream("deflate", outputStream, inputStream);
        inputStream.close();

        final InputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        final CompressorInputStream compressorInputStream = factory.createCompressorInputStream("deflate", testInputStream);
        assertInstanceOf(DeflateCompressorInputStream.class, compressorInputStream);
    }

    @Test
    public void shouldCreateDeflateCompressorOutputStreamFromInputStream() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[1]);
        final OutputStream outputStream = new ByteArrayOutputStream();
        factory.createCompressorOutputStream("deflate", outputStream, inputStream);
        inputStream.close();

        final InputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        final CompressorInputStream compressorInputStream = factory.createCompressorInputStream("deflate", testInputStream);
        assertInstanceOf(DeflateCompressorInputStream.class, compressorInputStream);
    }

    @Test
    public void shouldGetDecompressConcatenatedProperty() {
        assertTrue(factory.getDecompressConcatenated());
    }

    @Test
    public void shouldSetDecompressConcatenatedProperty() {
        factory.setDecompressConcatenated(false);
        assertFalse(factory.getDecompressConcatenated());
    }
}