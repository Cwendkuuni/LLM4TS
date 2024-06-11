package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Test class for CompressorStreamFactory.
 */
public class CompressorStreamFactoryTest {

    /**
     * Test method for {@link CompressorStreamFactory#createCompressorInputStream(InputStream)}.
     *
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testCreateCompressorInputStream() throws Exception {
        // Test GZIPcompressorInputStream
        InputStream inputStream = createMockInputStreamWithSignature(GzipCompressorInputStream.SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(GZip2CompressorInputStream.GZIP, inputStream);
        assertEquals(GzipCompressorInputStream.class, compressorInputStream.getClass());
        verify(inputStream).mark(GzipCompressorInputStream.SIGNATURE.length);

        // Test BZIP2compressorInputStream
        inputStream = createMockInputStreamWithSignature(BZip2CompressorInputStream.SIGNATURE);
        compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(BZip2CompressorInputStream.BZIP2, inputStream);
        assertEquals(BZip2CompressorInputStream.class, compressorInputStream.getClass());
        verify(inputStream).mark(BZip2CompressorInputStream.SIGNATURE.length);

        // Test DeflatecompressorInputStream
        inputStream = createMockInputStreamWithSignature(DeflateCompressorInputStream.SIGNATURE);
        compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(DeflateCompressorInputStream.DEFLATE, inputStream);
        assertEquals(DeflateCompressorInputStream.class, compressorInputStream.getClass());
        verify(inputStream).mark(DeflateCompressorInputStream.SIGNATURE.length);

        // Test unknown compressor
        inputStream = createMockInputStreamWithSignature(new byte[0]);
        try {
            new CompressorStreamFactory()
                    .createCompressorInputStream("Unknown", inputStream);
            fail();
        } catch (CompressorException e) {
            // expected
        }
    }

    /**
     * Test method for {@link CompressorStreamFactory#createCompressorOutputStream(OutputStream)}.
     *
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testCreateCompressorOutputStream() throws Exception {
        // Test GZIPcompressorOutputStream
        OutputStream outputStream = createMockOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(GzipCompressorOutputStream.GZIP, outputStream);
        assertEquals(GzipCompressorOutputStream.class, compressorOutputStream.getClass());

        // Test BZIP2compressorOutputStream
        outputStream = createMockOutputStream();
        compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(BZip2CompressorOutputStream.BZIP2, outputStream);
        assertEquals(BZip2CompressorOutputStream.class, compressorOutputStream.getClass());

        // Test DeflatecompressorOutputStream
        outputStream = createMockOutputStream();
        compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(DeflateCompressorOutputStream.DEFLATE, outputStream);
        assertEquals(DeflateCompressorOutputStream.class, compressorOutputStream.getClass());

        // Test unknown compressor
        outputStream = createMockOutputStream();
        try {
            new CompressorStreamFactory()
                    .createCompressorOutputStream("Unknown", outputStream);
            fail();
        } catch (CompressorException e) {
            // expected
        }
    }

    /**
     * Test method for {@link CompressorStreamFactory#createCompressorInputStream(String, InputStream)}.
     *
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testCreateCompressorInputStreamFromName() throws Exception {
        // Test GZIPcompressorInputStream
        InputStream inputStream = createMockInputStreamWithSignature(GzipCompressorInputStream.SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(GzipCompressorInputStream.GZIP, inputStream);
        assertEquals(GzipCompressorInputStream.class, compressorInputStream.getClass());

        // Test BZIP2compressorInputStream
        inputStream = createMockInputStreamWithSignature(BZip2CompressorInputStream.SIGNATURE);
        compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(BZip2CompressorInputStream.BZIP2, inputStream);
        assertEquals(BZip2CompressorInputStream.class, compressorInputStream.getClass());

        // Test DeflatecompressorInputStream
        inputStream = createMockInputStreamWithSignature(DeflateCompressorInputStream.SIGNATURE);
        compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(DeflateCompressorInputStream.DEFLATE, inputStream);
        assertEquals(DeflateCompressorInputStream.class, compressorInputStream.getClass());

        // Test unknown compressor
        inputStream = createMockInputStreamWithSignature(new byte[0]);
        try {
            new CompressorStreamFactory()
                    .createCompressorInputStream("Unknown", inputStream);
            fail();
        } catch (CompressorException e) {
            // expected
        }
    }

    /**
     * Test method for {@link CompressorStreamFactory#createCompressorOutputStream(String, OutputStream)}.
     *
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testCreateCompressorOutputStreamFromName() throws Exception {
        // Test GZIPcompressorOutputStream
        OutputStream outputStream = createMockOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(GzipCompressorOutputStream.GZIP, outputStream);
        assertEquals(GzipCompressorOutputStream.class, compressorOutputStream.getClass());

        // Test BZIP2compressorOutputStream
        outputStream = createMockOutputStream();
        compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(BZip2CompressorOutputStream.BZIP2, outputStream);
        assertEquals(BZip2CompressorOutputStream.class, compressorOutputStream.getClass());

        // Test DeflatecompressorOutputStream
        outputStream = createMockOutputStream();
        compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(DeflateCompressorOutputStream.DEFLATE, outputStream);
        assertEquals(DeflateCompressorOutputStream.class, compressorOutputStream.getClass());

        // Test unknown compressor
        outputStream = createMockOutputStream();
        try {
            new CompressorStreamFactory()
                    .createCompressorOutputStream("Unknown", outputStream);
            fail();
        } catch (CompressorException e) {
            // expected
        }

        // Test create compressorOutputStream with decompressConcatenated = true for gzip
        outputStream = createMockOutputStream();
        compressorOutputStream = new CompressorStreamFactory(true)
                .createCompressorOutputStream(GzipCompressorOutputStream.GZIP, outputStream);
        assertEquals(GzipCompressorOutputStream.class, compressorOutputStream.getClass());
        verify(outputStream).mark(GzipCompressorOutputStream.SIGNATURE.length);

        // Test create compressorOutputStream with decompressConcatenated = true for bzip2
        outputStream = createMockOutputStream();
        compressorOutputStream = new CompressorStreamFactory(true)
                .createCompressorOutputStream(BZip2CompressorOutputStream.BZIP2, outputStream);
        assertEquals(BZip2CompressorOutputStream.class, compressorOutputStream.getClass());
        verify(outputStream).mark(BZip2CompressorOutputStream.SIGNATURE.length);

        // Test create compressorOutputStream with decompressConcatenated = true for deflate
        outputStream = createMockOutputStream();
        compressorOutputStream = new CompressorStreamFactory(true)
                .createCompressorOutputStream(DeflateCompressorOutputStream.DEFLATE, outputStream);
        assertEquals(DeflateCompressorOutputStream.class, compressorOutputStream.getClass());
        verify(outputStream).mark(DeflateCompressorOutputStream.SIGNATURE.length);
    }

    /**
     * Test method for {@link CompressorStreamFactory#getDecompressConcatenated()}.
     *
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testGetDecompressConcatenated() throws Exception {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertFalse(factory.getDecompressConcatenated());

        CompressorStreamFactory factoryWithDecompressConcatenated = new CompressorStreamFactory(true);
        assertTrue(factoryWithDecompressConcatenated.getDecompressConcatenated());
    }

    /**
     * Create a mock InputStream with a given signature.
     *
     * @param signature the signature of the compressor
     * @return a mocked InputStream
     */
    private InputStream createMockInputStreamWithSignature(byte[] signature) {
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.markSupported()).thenReturn(true);
        when(inputStream.read(any(byte[].class), anyInt(), anyInt()))
                .thenAnswer(i -> IOUtils.readFully(inputStream, signature.length));
        try {
            when(inputStream.mark(signature.length)).thenReturn(signature.length);
        } catch (IOException e) {
            // ignore
        }
        return inputStream;
    }

    /**
     * Create a mock OutputStream.
     *
     * @return a mocked OutputStream
     */
    private OutputStream createMockOutputStream() {
        OutputStream outputStream = mock(OutputStream.class);
        return outputStream;
    }
}