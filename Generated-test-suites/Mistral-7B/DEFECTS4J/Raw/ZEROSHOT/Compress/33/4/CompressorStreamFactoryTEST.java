package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertEquals;
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
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

/**
 * Unit tests for CompressorStreamFactory
 */
public class CompressorStreamFactoryTest {

    /**
     * Test createCompressorInputStream method with valid arguments.
     */
    @org.junit.Test
    public void testCreateCompressorInputStream() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        CompressorStreamFactory factory = new CompressorStreamFactory();

        // Test GZIP compressor
        CompressorInputStream gzipInput = factory.createCompressorInputStream(CompressorStreamFactory.GZIP, input);
        assertTrue(gzipInput instanceof GzipCompressorInputStream);
        gzipInput.close();

        // Test BZIP2 compressor
        CompressorInputStream bzip2Input = factory.createCompressorInputStream(CompressorStreamFactory.BZIP2, input);
        assertTrue(bzip2Input instanceof BZip2CompressorInputStream);
        bzip2Input.close();

        // Test XZ compressor
        CompressorInputStream xzInput = factory.createCompressorInputStream(CompressorStreamFactory.XZ, input);
        assertTrue(xzInput instanceof XZCompressorInputStream);
        xzInput.close();

        // Test DEFLATE compressor
        CompressorInputStream deflateInput = factory.createCompressorInputStream(CompressorStreamFactory.DEFLATE, input);
        assertTrue(deflateInput instanceof DeflateCompressorInputStream);
        deflateInput.close();

        // Test unsupported compressor
        InputStream unsupportedInput = factory.createCompressorInputStream("SNAPPY_RAW", input);
        assertNull(unsupportedInput);

        // Test null input
        InputStream nullInput = null;
        CompressorInputStream nullInputResult = factory.createCompressorInputStream("GZIP", nullInput);
        assertNull(nullInputResult);
    }

    /**
     * Test createCompressorOutputStream method with valid arguments.
     */
    @org.junit.Test
    public void testCreateCompressorOutputStream() throws Exception {
        OutputStream output = new ByteArrayOutputStream();
        CompressorStreamFactory factory = new CompressorStreamFactory();

        // Test GZIP compressor
        CompressorOutputStream gzipOutput = factory.createCompressorOutputStream(CompressorStreamFactory.GZIP, output);
        assertTrue(gzipOutput instanceof GzipCompressorOutputStream);
        gzipOutput.finish();
        gzipOutput.close();

        // Test BZIP2 compressor
        CompressorOutputStream bzip2Output = factory.createCompressorOutputStream(CompressorStreamFactory.BZIP2, output);
        assertTrue(bzip2Output instanceof BZip2CompressorOutputStream);
        bzip2Output.finish();
        bzip2Output.close();

        // Test XZ compressor
        CompressorOutputStream xzOutput = factory.createCompressorOutputStream(CompressorStreamFactory.XZ, output);
        assertTrue(xzOutput instanceof XZCompressorOutputStream);
        xzOutput.finish();
        xzOutput.close();

        // Test DEFLATE compressor
        CompressorOutputStream deflateOutput = factory.createCompressorOutputStream(CompressorStreamFactory.DEFLATE, output);
        assertTrue(deflateOutput instanceof DeflateCompressorOutputStream);
        deflateOutput.finish();
        deflateOutput.close();

        // Test unsupported compressor
        OutputStream unsupportedOutput = new ByteArrayOutputStream();
        CompressorOutputStream unsupportedOutputResult = factory.createCompressorOutputStream("SNAPPY_FRAMED", unsupportedOutput);
        assertNull(unsupportedOutputResult);

        // Test null output
        OutputStream nullOutput = null;
        CompressorOutputStream nullOutputResult = factory.createCompressorOutputStream("GZIP", nullOutput);
        assertNull(nullOutputResult);
    }

    /**
     * Test createCompressorInputStream method with invalid arguments.
     */
    @org.junit.Test(expected = CompressorException.class)
    public void testCreateCompressorInputStreamWithInvalidArguments() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        CompressorStreamFactory factory = new CompressorStreamFactory();

        // Test unknown compressor
        factory.createCompressorInputStream("SNAPPY_RAW", input);
    }

    /**
     * Test createCompressorOutputStream method with invalid arguments.
     */
    @org.junit.Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStreamWithInvalidArguments() throws Exception {
        OutputStream output = new ByteArrayOutputStream();
        CompressorStreamFactory factory = new CompressorStreamFactory();

        // Test unknown compressor
        factory.createCompressorOutputStream("SNAPPY_RAW", output);
    }

    /**
     * Test getDecompressConcatenated method with default constructor.
     */
    @org.junit.Test
    public void testGetDecompressConcatenatedDefaultConstructor() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertFalse(factory.getDecompressConcatenated());
    }

    /**
     * Test getDecompressConcatenated method with constructor with decompressConcatenated set to true.
     */
    @org.junit.Test
    public void testGetDecompressConcatenatedConstructorWithDecompressConcatenatedTrue() {
        CompressorStreamFactory factory = new CompressorStreamFactory(true);
        assertTrue(factory.getDecompressConcatenated());
    }

    /**
     * Test getDecompressConcatenated method with constructor with decompressConcatenated set to false.
     */
    @org.junit.Test
    public void testGetDecompressConcatenatedConstructorWithDecompressConcatenatedFalse() {
        CompressorStreamFactory factory = new CompressorStreamFactory(false);
        assertFalse(factory.getDecompressConcatenated());
    }
}