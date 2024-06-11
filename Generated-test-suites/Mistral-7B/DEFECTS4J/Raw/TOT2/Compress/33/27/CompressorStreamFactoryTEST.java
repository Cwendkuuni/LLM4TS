package org.apache.commons.compress.compressors.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Test case for {@link CompressorStreamFactory} class
 */
public class CompressorStreamFactoryTest {

    /**
     * Test creating compressor input streams from known compressor names.
     * @throws IOException
     * @throws CompressorException
     */
    @org.junit.Test
    public void testCreateCompressorInputStreamFromKnownNames()
            throws IOException, CompressorException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        // Test Gzip
        ByteArrayOutputStream gzipOutput = new ByteArrayOutputStream();
        GzipCompressorOutputStream gzipCompressorOutputStream = factory
                .createCompressorOutputStream(CompressorStreamFactory.GZIP, gzipOutput);
        gzipCompressorOutputStream.write("Test data".getBytes());
        gzipCompressorOutputStream.close();

        ByteArrayInputStream gzipInput = new ByteArrayInputStream(gzipOutput.toByteArray());
        CompressorInputStream gzipCompressorInputStream = factory
                .createCompressorInputStream(CompressorStreamFactory.GZIP, gzipInput);
        assertEquals("Test data", IOUtils.toString(gzipCompressorInputStream));
        gzipCompressorInputStream.close();

        // Test BZIP2
        ByteArrayOutputStream bzip2Output = new ByteArrayOutputStream();
        BZip2CompressorOutputStream bzip2CompressorOutputStream = factory
                .createCompressorOutputStream(CompressorStreamFactory.BZIP2, bzip2Output);
        bzip2CompressorOutputStream.write("Test data".getBytes());
        bzip2CompressorOutputStream.close();

        ByteArrayInputStream bzip2Input = new ByteArrayInputStream(bzip2Output.toByteArray());
        CompressorInputStream bzip2CompressorInputStream = factory
                .createCompressorInputStream(CompressorStreamFactory.BZIP2, bzip2Input);
        assertEquals("Test data", IOUtils.toString(bzip2CompressorInputStream));
        bzip2CompressorInputStream.close();

        // Test Deflate
        ByteArrayOutputStream deflateOutput = new ByteArrayOutputStream();
        DeflateCompressorOutputStream deflateCompressorOutputStream = factory
                .createCompressorOutputStream(CompressorStreamFactory.DEFLATE, deflateOutput);
        deflateCompressorOutputStream.write("Test data".getBytes());
        deflateCompressorOutputStream.close();

        ByteArrayInputStream deflateInput = new ByteArrayInputStream(deflateOutput.toByteArray());
        CompressorInputStream deflateCompressorInputStream = factory
                .createCompressorInputStream(CompressorStreamFactory.DEFLATE, deflateInput);
        assertEquals("Test data", IOUtils.toString(deflateCompressorInputStream));
        deflateCompressorInputStream.close();
    }

    /**
     * Test creating compressor input streams from unknown compressor names.
     * @throws IOException
     * @throws CompressorException
     */
    @org.junit.Test(expected = CompressorException.class)
    public void testCreateCompressorInputStreamFromUnknownName()
            throws IOException, CompressorException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.createCompressorInputStream("UnknownCompressor", new ByteArrayInputStream(new byte[0]));
    }

    /**
     * Test creating compressor output streams from known compressor names.
     * @throws IOException
     * @throws DataFormatException
     * @throws CompressorException
     */
    @org.junit.Test
    public void testCreateCompressorOutputStreamFromKnownNames()
            throws IOException, DataFormatException, CompressorException {
        CompressorStreamFactory factory = new CompressorStreamFactory();

        // Test Gzip
        ByteArrayOutputStream gzipOutput = new ByteArrayOutputStream();
        CompressorOutputStream gzipCompressorOutputStream = factory
                .createCompressorOutputStream(CompressorStreamFactory.GZIP, gzipOutput);
        gzipCompressorOutputStream.write("Test data".getBytes());
        gzipCompressorOutputStream.finish();
        gzipCompressorOutputStream.close();

        ByteArrayInputStream gzipInput = new ByteArrayInputStream(gzipOutput.toByteArray());
        CompressorInputStream gzipCompressorInputStream = factory
                .createCompressorInputStream(CompressorStreamFactory.GZIP, gzipInput);
        assertEquals("Test data", IOUtils.toString(gzipCompressorInputStream));
        gzipCompressorInputStream.close();

        // Test BZIP2
        ByteArrayOutputStream bzip2Output = new ByteArrayOutputStream();
        CompressorOutputStream bzip2CompressorOutputStream = factory
                .createCompressorOutputStream(CompressorStreamFactory.BZIP2, bzip2Output);
        bzip2CompressorOutputStream.write("Test data".getBytes());
        bzip2CompressorOutputStream.finish();
        bzip2CompressorOutputStream.close();

        ByteArrayInputStream bzip2Input = new ByteArrayInputStream(bzip2Output.toByteArray());
        CompressorInputStream bzip2CompressorInputStream = factory
                .createCompressorInputStream(CompressorStreamFactory.BZIP2, bzip2Input);
        assertEquals("Test data", IOUtils.toString(bzip2CompressorInputStream));
        bzip2CompressorInputStream.close();

        // Test Deflate
        ByteArrayOutputStream deflateOutput = new ByteArrayOutputStream();
        CompressorOutputStream deflateCompressorOutputStream = factory
                .createCompressorOutputStream(CompressorStreamFactory.DEFLATE, deflateOutput);
        deflateCompressorOutputStream.write("Test data".getBytes());
        deflateCompressorOutputStream.finish();
        deflateCompressorOutputStream.close();

        ByteArrayInputStream deflateInput = new ByteArrayInputStream(deflateOutput.toByteArray());
        CompressorInputStream deflateCompressorInputStream = factory
                .createCompressorInputStream(CompressorStreamFactory.DEFLATE, deflateInput);
        assertEquals("Test data", IOUtils.toString(deflateCompressorInputStream));
        deflateCompressorInputStream.close();
    }

    /**
     * Test creating compressor output streams from unknown compressor names.
     * @throws IOException
     * @throws CompressorException
     */
    @org.junit.Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStreamFromUnknownName()
            throws IOException, CompressorException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.createCompressorOutputStream("UnknownCompressor", new ByteArrayOutputStream());
    }

    /**
     * Test setDecompressConcatenated with valid value.
     */
    @org.junit.Test
    public void testSetDecompressConcatenatedWithValidValue() {
        CompressorStreamFactory factory = new CompressorStreamFactory(true);
        assertTrue(factory.getDecompressConcatenated());
    }

    /**
     * Test setDecompressConcatenated with null value.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSetDecompressConcatenatedWithNullValue() {
        new CompressorStreamFactory(null);
    }

    /**
     * Test setDecompressConcatenated with invalid boolean value.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSetDecompressConcatenatedWithInvalidBooleanValue() {
        new CompressorStreamFactory((Object) false);
    }
}