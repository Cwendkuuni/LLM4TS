package org.apache.commons.compress.compressors.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.XZCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Tests for {@link CompressorStreamFactory}.
 */
public class CompressorStreamFactoryTest {

    /**
     * Tests creating a compressor input stream from a known file signature.
     * @throws IOException if reading from the test files fails
     */
    @Test
    public void testCreateCompressorInputStreamFromKnownSignature() throws IOException {
        byte[] signature = BZip2CompressorInputStream.signature;
        InputStream is = new ByteArrayInputStream(signature);
        CompressorInputStream in = new CompressorStreamFactory()
                .createCompressorInputStream("bzip2", is);
        assertEquals(BZip2CompressorInputStream.class, in.getClass());
        in.close();
    }

    /**
     * Tests creating a compressor input stream from a known compressor name.
     * @throws IOException if reading from the test files fails
     */
    @Test
    public void testCreateCompressorInputStreamFromKnownName() throws IOException {
        InputStream is = new FileInputStream("testfiles/test.bz2");
        CompressorInputStream in = new CompressorStreamFactory()
                .createCompressorInputStream("bzip2", is);
        assertEquals(BZip2CompressorInputStream.class, in.getClass());
        in.close();
    }

    /**
     * Tests creating a compressor input stream from an unknown compressor.
     * @throws IOException if reading from the test files fails
     */
    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStreamFromUnknownName() throws IOException {
        InputStream is = new FileInputStream("testfiles/test.bz2");
        new CompressorStreamFactory()
                .createCompressorInputStream("unknown", is);
    }

    /**
     * Tests creating a compressor output stream from a known compressor name.
     * @throws IOException if writing to the output stream fails
     */
    @Test
    public void testCreateCompressorOutputStreamFromKnownName() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        CompressorOutputStream out = new CompressorStreamFactory()
                .createCompressorOutputStream("bzip2", os);
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        CompressorInputStream in = new CompressorStreamFactory()
                .createCompressorInputStream("bzip2", is);
        assertEquals(BZip2CompressorInputStream.class, in.getClass());
        in.close();
        os.close();
    }

    /**
     * Tests creating a compressor output stream from a known file signature.
     * @throws IOException if writing to the output stream fails
     */
    @Test
    public void testCreateCompressorOutputStreamFromKnownSignature() throws IOException {
        byte[] signature = GzipCompressorInputStream.signature;
        OutputStream os = new ByteArrayOutputStream();
        CompressorOutputStream out = new CompressorStreamFactory()
                .createCompressorOutputStream("gzip", os);
        ByteArrayInputStream is = new ByteArrayInputStream(signature);
        CompressorInputStream in = new CompressorStreamFactory()
                .createCompressorInputStream("gzip", is);
        assertEquals(GzipCompressorInputStream.class, in.getClass());
        in.close();
        os.close();
    }

    /**
     * Tests creating a compressor output stream from an unknown compressor.
     * @throws IOException if writing to the output stream fails
     */
    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStreamFromUnknownName() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        new CompressorStreamFactory().createCompressorOutputStream("unknown", os);
    }

    /**
     * Tests creating a compressor input stream from an empty input stream.
     * @throws IOException if creating the empty input stream fails
     */
    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStreamFromEmptyInput() throws IOException {
        InputStream is = new ByteArrayInputStream(new byte[0]);
        new CompressorStreamFactory().createCompressorInputStream("bzip2", is);
    }

    /**
     * Tests creating a compressor output stream from a null output stream.
     * @throws IOException if creating the null output stream fails
     */
    @Test(expected = NullPointerException.class)
    public void testCreateCompressorOutputStreamFromNullOutput() throws IOException {
        OutputStream os = null;
        new CompressorStreamFactory().createCompressorOutputStream("bzip2", os);
    }

    /**
     * Tests creating a compressor input stream from a null input stream.
     * @throws IOException if creating the null input stream fails
     */
    @Test(expected = NullPointerException.class)
    public void testCreateCompressorInputStreamFromNullInput() throws IOException {
        InputStream is = null;
        new CompressorStreamFactory().createCompressorInputStream("bzip2", is);
    }

    /**
     * Tests creating a compressor input stream from a stream with a wrong signature.
     * @throws IOException if reading from the test files fails
     */
    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStreamFromWrongSignature() throws IOException {
        byte[] signature = new byte[10];
        InputStream is = new ByteArrayInputStream(signature);
        new CompressorStreamFactory()
                .createCompressorInputStream("bzip2", is);
    }

    /**
     * Tests creating a compressor output stream from a stream with a wrong signature.
     * @throws IOException if writing to the output stream fails
     */
    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStreamFromWrongSignature() throws IOException {
        byte[] signature = new byte[10];
        OutputStream os = new ByteArrayOutputStream();
        new CompressorStreamFactory()
                .createCompressorOutputStream("bzip2", os);
    }

    /**
     * Tests creating a compressor input stream when decompressing concatenated streams is enabled.
     * @throws IOException if reading from the test files fails
     */
    @Test
    public void testCreateCompressorInputStreamWithConcatenatedStreams() throws IOException {
        InputStream is = new FileInputStream("testfiles/test.gz");
        CompressorInputStream in = new CompressorStreamFactory(true)
                .createCompressorInputStream("gzip", is);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copy(in, baos);
        ByteArrayInputStream is2 = new ByteArrayInputStream(baos.toByteArray());
        CompressorInputStream in2 = new CompressorStreamFactory()
                .createCompressorInputStream("gzip", is2);
        assertEquals(in.getClass(), in2.getClass());
        in2.close();
        is2.close();
        in.close();
        is.close();
    }

    /**
     * Tests creating a compressor output stream when decompressing concatenated streams is enabled.
     * @throws IOException if writing to the output stream fails
     */
    @Test
    public void testCreateCompressorOutputStreamWithConcatenatedStreams() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        CompressorOutputStream out = new CompressorStreamFactory(true)
                .createCompressorOutputStream("gzip", os);
        ByteArrayInputStream is = new ByteArrayInputStream(new byte[0]);
        CompressorInputStream in = new CompressorStreamFactory()
                .createCompressorInputStream("gzip", is);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copy(in, baos);
        InputStream is2 = new ByteArrayInputStream(baos.toByteArray());
        CompressorInputStream in2 = new CompressorStreamFactory()
                .createCompressorInputStream("gzip", is2);
        IOUtils.copy(in2, os);
        ByteArrayInputStream is3 = new ByteArrayInputStream(os.toByteArray());
        CompressorInputStream in3 = new CompressorStreamFactory()
                .createCompressorInputStream("gzip", is3);
        assertEquals(out.getClass(), in3.getClass());
        in3.close();
        is3.close();
        in2.close();
        is2.close();
        in.close();
        is.close();
    }
}