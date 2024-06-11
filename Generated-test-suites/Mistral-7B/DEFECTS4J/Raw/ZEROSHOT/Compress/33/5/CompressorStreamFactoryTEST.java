package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Test class for CompressorStreamFactory
 */
public class CompressorStreamFactoryTest {

    @org.junit.Test
    public void testCreateCompressorInputStream_GZIP() throws IOException {
        InputStream input = new ByteArrayInputStream("test data".getBytes());
        CompressorStreamFactory factory = new CompressorStreamFactory();
        CompressorInputStream compressed = factory.createCompressorInputStream("gzip", input);
        assertNotNull(compressed);

        GzipCompressorInputStream gzipExpected = new GzipCompressorInputStream(input);
        assertEquals(gzipExpected.getClass(), compressed.getClass());
    }

    @org.junit.Test
    public void testCreateCompressorInputStream_BZIP2() throws IOException {
        InputStream input = new ByteArrayInputStream("test data".getBytes());
        CompressorStreamFactory factory = new CompressorStreamFactory();
        CompressorInputStream compressed = factory.createCompressorInputStream("bzip2", input);
        assertNotNull(compressed);

        BZip2CompressorInputStream bzip2Expected = new BZip2CompressorInputStream(input);
        assertEquals(bzip2Expected.getClass(), compressed.getClass());
    }

    @org.junit.Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_UnknownFormat() throws IOException {
        InputStream input = new ByteArrayInputStream("test data".getBytes());
        CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.createCompressorInputStream("unknown", input);
    }

    @org.junit.Test
    public void testCreateCompressorOutputStream_GZIP() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        CompressorStreamFactory factory = new CompressorStreamFactory();
        CompressorOutputStream compressor = factory.createCompressorOutputStream("gzip", output);
        assertNotNull(compressor);

        GzipCompressorOutputStream gzipExpected = new GzipCompressorOutputStream(output);
        assertEquals(gzipExpected.getClass(), compressor.getClass());
    }

    @org.junit.Test
    public void testCreateCompressorOutputStream_BZIP2() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        CompressorStreamFactory factory = new CompressorStreamFactory();
        CompressorOutputStream compressor = factory.createCompressorOutputStream("bzip2", output);
        assertNotNull(compressor);

        BZip2CompressorOutputStream bzip2Expected = new BZip2CompressorOutputStream(output);
        assertEquals(bzip2Expected.getClass(), compressor.getClass());
    }

    @org.junit.Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStream_UnknownFormat() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.createCompressorOutputStream("unknown", output);
    }

    @org.junit.Test
    public void testCreateCompressorInputStream_BZIP2_Detect() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream("BZ\u0077\u0077IP\u0021".getBytes());
        CompressorStreamFactory factory = new CompressorStreamFactory();
        CompressorInputStream compressed = factory.createCompressorInputStream(input);

        BZip2CompressorInputStream bzip2Expected = new BZip2CompressorInputStream(input);
        assertEquals(bzip2Expected.getClass(), compressed.getClass());
    }

    @org.junit.Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory(false);
        assertEquals(false, factory.getDecompressConcatenated());

        factory = new CompressorStreamFactory(true);
        assertEquals(true, factory.getDecompressConcatenated());
    }
}