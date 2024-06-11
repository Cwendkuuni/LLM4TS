package org.apache.commons.compress.compressors.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;

import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorFactory;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.DefaultByteArrayOutputStream;
import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Test case for {@link CompressorStreamFactory}.
 */
public class CompressorStreamFactoryTest {

    /**
     * Test that the factory can create a GZIP compressor input stream from a GZIP stream.
     */
    @org.junit.Test
    public void testCreateGzipCompressorInputStream() throws IOException {
        InputStream input = new ByteArrayInputStream(GzipSample.getData());
        CompressorInputStream compressedInput = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.GZIP, input);
        assertEquals("Expected a GZIP compressor input stream.",
                GzipCompressorInputStream.class, compressedInput.getClass());
    }

    /**
     * Test that the factory can create a BZIP2 compressor input stream from a BZIP2 stream.
     */
    @org.junit.Test
    public void testCreateBzip2CompressorInputStream() throws IOException {
        InputStream input = new ByteArrayInputStream(Bzip2Sample.getData());
        CompressorInputStream compressedInput = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.BZIP2, input);
        assertEquals("Expected a BZIP2 compressor input stream.",
                BZip2CompressorInputStream.class, compressedInput.getClass());
    }

    /**
     * Test that the factory can create a deflate compressor input stream from a deflate stream.
     */
    @org.junit.Test
    public void testCreateDeflateCompressorInputStream() throws IOException {
        InputStream input = new ByteArrayInputStream(DeflateSample.getData());
        CompressorInputStream compressedInput = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.DEFLATE, input);
        assertEquals("Expected a deflate compressor input stream.",
                DeflateCompressorInputStream.class, compressedInput.getClass());
    }

    /**
     * Test that the factory returns null if the compressor name is not known.
     */
    @org.junit.Test
    public void testUnknownCompressor() {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        assertNull("Expected null.",
                new CompressorStreamFactory().createCompressorInputStream("unknown", input));
    }

    /**
     * Test that the factory throws an exception if the input stream is null.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNullInputStream() {
        new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.GZIP, null);
    }

    /**
     * Test that the factory throws an exception if the output stream is null.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNullOutputStream() {
        new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.GZIP, null);
    }

    /**
     * Test that the factory creates a GZIP compressor output stream.
     */
    @org.junit.Test
    public void testCreateGzipCompressorOutputStream() throws IOException, DataFormatException {
        ByteArrayOutputStream out = new DefaultByteArrayOutputStream();
        CompressorOutputStream compressorOutput = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.GZIP, out);
        InputStream input = new ByteArrayInputStream(SampleData.DATA);
        IOUtils.copy(input, compressorOutput);
        compressorOutput.finish();
        assertEquals("Expected a GZIP compressed byte array.",
                GzipSample.getData(), out.toByteArray());
    }

    /**
     * Test that the factory creates a BZIP2 compressor output stream.
     */
    @org.junit.Test
    public void testCreateBzip2CompressorOutputStream() throws IOException, DataFormatException {
        ByteArrayOutputStream out = new DefaultByteArrayOutputStream();
        CompressorOutputStream compressorOutput = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.BZIP2, out);
        InputStream input = new ByteArrayInputStream(SampleData.DATA);
        IOUtils.copy(input, compressorOutput);
        compressorOutput.finish();
        assertEquals("Expected a BZIP2 compressed byte array.",
                Bzip2Sample.getData(), out.toByteArray());
    }

    /**
     * Test that the factory creates a deflate compressor output stream.
     */
    @org.junit.Test
    public void testCreateDeflateCompressorOutputStream() throws IOException, DataFormatException {
        ByteArrayOutputStream out = new DefaultByteArrayOutputStream();
        CompressorOutputStream compressorOutput = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.DEFLATE, out);
        InputStream input = new ByteArrayInputStream(SampleData.DATA);
        IOUtils.copy(input, compressorOutput);
        compressorOutput.finish();
        assertEquals("Expected a deflate compressed byte array.",
                DeflateSample.getData(), out.toByteArray());
    }

    /**
     * Test that the factory throws an exception if the compressor name is null.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testNullCompressorName() {
        new CompressorStreamFactory().createCompressorOutputStream(null, new ByteArrayOutputStream());
    }

    /**
     * Test that the factory throws an exception if the compressor name is empty.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testEmptyCompressorName() {
        new CompressorStreamFactory().createCompressorOutputStream("", new ByteArrayOutputStream());
    }

    /**
     * Test that the factory throws an exception if the compressor name is not one of the supported names.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testUnsupportedCompressorName() {
        new CompressorStreamFactory().createCompressorOutputStream("unsupported", new ByteArrayOutputStream());
    }

    /**
     * Test that the factory throws an exception if the compressor name is unknown and the deprecated setDecompressConcatenated method is not used.
     */
    @org.junit.Test(expected = CompressorException.class)
    public void testUnknownCompressorWithDeprecatedMethodNotUsed() {
        new CompressorStreamFactory().createCompressorInputStream("unknown", new ByteArrayInputStream(new byte[0]));
    }

    /**
     * Test that the factory sets the decompressConcatenated flag to true if setDecompressConcatenated is used and the compressor name is gzip.
     */
    @org.junit.Test
    public void testSetDecompressConcatenatedForGzip() {
        boolean originalDecompressConcatenated = new CompressorStreamFactory().getDecompressConcatenated();
        new CompressorStreamFactory(true).createCompressorInputStream(CompressorStreamFactory.GZIP, new ByteArrayInputStream(new byte[0]));
        assertTrue("Expected decompressConcatenated to be set to true.", new CompressorStreamFactory().getDecompressConcatenated());
        new CompressorStreamFactory(originalDecompressConcatenated).createCompressorInputStream(CompressorStreamFactory.GZIP, new ByteArrayInputStream(new byte[0]));
        assertFalse("Expected decompressConcatenated to be reset to the original value.", new CompressorStreamFactory().getDecompressConcatenated());
    }

    /**
     * Test that the factory sets the decompressConcatenated flag to true if setDecompressConcatenated is used and the compressor name is bzip2.
     */
    @org.junit.Test
    public void testSetDecompressConcatenatedForBzip2() {
        boolean originalDecompressConcatenated = new CompressorStreamFactory().getDecompressConcatenated();
        new CompressorStreamFactory(true).createCompressorInputStream(CompressorStreamFactory.BZIP2, new ByteArrayInputStream(new byte[0]));
        assertTrue("Expected decompressConcatenated to be set to true.", new CompressorStreamFactory().getDecompressConcatenated());
        new CompressorStreamFactory(originalDecompressConcatenated).createCompressorInputStream(CompressorStreamFactory.BZIP2, new ByteArrayInputStream(new byte[0]));
        assertFalse("Expected decompressConcatenated to be reset to the original value.", new CompressorStreamFactory().getDecompressConcatenated());
    }

    /**
     * Test that the factory does not set the decompressConcatenated flag to true if setDecompressConcatenated is used and the compressor name is not gzip or bzip2.
     */
    @org.junit.Test
    public void testSetDecompressConcatenatedForOtherCompressor() {
        boolean originalDecompressConcatenated = new CompressorStreamFactory().getDecompressConcatenated();
        new CompressorStreamFactory(true).createCompressorInputStream(CompressorStreamFactory.DEFLATE, new ByteArrayInputStream(new byte[0]));
        assertFalse("Expected decompressConcatenated to be false.", new CompressorStreamFactory().getDecompressConcatenated());
        new CompressorStreamFactory(originalDecompressConcatenated).createCompressorInputStream(CompressorStreamFactory.DEFLATE, new ByteArrayInputStream(new byte[0]));
        assertFalse("Expected decompressConcatenated to be reset to the original value.", new CompressorStreamFactory().getDecompressConcatenated());
    }

    /**
     * Test that the factory throws an exception if setDecompressConcatenated is used with an unsupported compressor name.
     */
    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetDecompressConcatenatedForUnsupportedCompressor() {
        new CompressorStreamFactory(true).createCompressorInputStream(CompressorStreamFactory.PACK200, new ByteArrayInputStream(new byte[0]));
    }

    /**
     * Test that the factory sets the decompressConcatenated flag to false when the constructor is called without the deprecated setDecompressConcatenated method.
     */
    @org.junit.Test
    public void testDefaultDecompressConcatenatedValue() {
        assertFalse("Expected decompressConcatenated to be false.", new CompressorStreamFactory().getDecompressConcatenated());
    }
}