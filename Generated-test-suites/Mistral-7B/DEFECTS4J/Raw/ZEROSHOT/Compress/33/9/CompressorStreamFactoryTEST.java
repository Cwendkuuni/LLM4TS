package org.apache.commons.compress.compressors;

import static org.junit.Assert.*;

import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.CompressorStreamFactory.BZIP2;
import org.apache.commons.compress.compressors.CompressorStreamFactory.DEFLATE;
import org.apache.commons.compress.compressors.CompressorStreamFactory.GZIP;
import org.apache.commons.compress.compressors.CompressorStreamFactory.XZ;
import org.apache.commons.compress.utils.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * CompressorStreamFactoryTest class for testing all methods in CompressorStreamFactory class.
 */
public class CompressorStreamFactoryTest {

    private static final int TEST_DATA_SIZE = 1024;
    private static final byte[] TEST_DATA = new byte[TEST_DATA_SIZE];

    static {
        IOUtils.fill(new ByteArrayInputStream(TEST_DATA), TEST_DATA_SIZE);
    }

    /**
     * Test createCompressorInputStream() method with valid compressor name.
     */
    @org.junit.Test
    public void testCreateCompressorInputStream_ValidCompressorName() throws IOException {
        InputStream input = IOUtils.toInputStream(TEST_DATA);
        try (OutputStream output = new ByteArrayOutputStream(TEST_DATA_SIZE)) {
            CompressorStreamFactory factory = new CompressorStreamFactory();
            CompressorInputStream is = factory.createCompressorInputStream(DEFLATE, input);
            IOUtils.copy(is, output);
            byte[] outputData = output.toByteArray();
            assertTrue("Output data should not be empty.", outputData.length > 0);
            assertFalse("Output data should be compressed.",
                    IOUtils.contentEquals(input, new ByteArrayInputStream(outputData)));
            is.close();
        }
    }

    /**
     * Test createCompressorInputStream() method with invalid compressor name.
     */
    @org.junit.Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_InvalidCompressorName() throws IOException {
        InputStream input = IOUtils.toInputStream(TEST_DATA);
        CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.createCompressorInputStream("INVALID_COMPRESSOR", input);
    }

    /**
     * Test createCompressorOutputStream() method with valid compressor name.
     */
    @org.junit.Test
    public void testCreateCompressorOutputStream_ValidCompressorName() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        ByteArrayOutputStream output = new ByteArrayOutputStream(TEST_DATA_SIZE);
        CompressorOutputStream os = factory.createCompressorOutputStream(DEFLATE, output);
        InputStream input = new ByteArrayInputStream(TEST_DATA);
        IOUtils.copy(input, os);
        os.finish();
        os.close();
        byte[] outputData = output.toByteArray();
        assertTrue("Output data should not be empty.", outputData.length > 0);
        assertTrue("Output data should be compressed.",
                !IOUtils.contentEquals(input, new ByteArrayInputStream(outputData)));
    }

    /**
     * Test createCompressorOutputStream() method with invalid compressor name.
     */
    @org.junit.Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStream_InvalidCompressorName() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        ByteArrayOutputStream output = new ByteArrayOutputStream(TEST_DATA_SIZE);
        factory.createCompressorOutputStream("INVALID_COMPRESSOR", output);
    }

    /**
     * Test setDecompressConcatenated() method.
     */
    @org.junit.Test
    public void testSetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertTrue("Default value should be false.", factory.getDecompressConcatenated() == false);
        factory = new CompressorStreamFactory(true);
        assertTrue("Set value should be true.", factory.getDecompressConcatenated() == true);
    }
}