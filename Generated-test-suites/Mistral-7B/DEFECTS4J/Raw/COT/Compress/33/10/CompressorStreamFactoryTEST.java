package org.apache.commons.compress.compressors;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test cases for CompressorStreamFactory class.
 */
public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream() throws Exception {
        // Test with GZIP CompressorInputStream
        InputStream input = new ByteArrayInputStream(new byte[10]);
        CompressorInputStream gzipInput = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.GZIP, input);
        Assert.assertNotNull(gzipInput);
        gzipInput.close();

        // Test with BZIP2 CompressorInputStream
        input = new ByteArrayInputStream(new byte[10]);
        CompressorInputStream bzip2Input = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.BZIP2, input);
        Assert.assertNotNull(bzip2Input);
        bzip2Input.close();

        // Test with XZ CompressorInputStream
        input = new ByteArrayInputStream(new byte[10]);
        CompressorInputStream xzInput = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.XZ, input);
        Assert.assertNotNull(xzInput);
        xzInput.close();

        // Test with invalid compressor name
        InputStream invalidInput = new ByteArrayInputStream(new byte[10]);
        try {
            CompressorInputStream invalidInputStream = new CompressorStreamFactory().createCompressorInputStream("InvalidCompressor", invalidInput);
            Assert.fail();
        } catch (CompressorException e) {
            Assert.assertEquals("Compressor: InvalidCompressor not found.", e.getMessage());
        }
        invalidInput.close();
    }

    @Test
    public void testCreateCompressorOutputStream() throws Exception {
        // Test with GZIP CompressorOutputStream
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream gzipOutput = new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.GZIP, output);
        Assert.assertNotNull(gzipOutput);
        gzipOutput.finish();
        gzipOutput.close();

        // Test with BZIP2 CompressorOutputStream
        output = new ByteArrayOutputStream();
        CompressorOutputStream bzip2Output = new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.BZIP2, output);
        Assert.assertNotNull(bzip2Output);
        bzip2Output.finish();
        bzip2Output.close();

        // Test with XZ CompressorOutputStream
        output = new ByteArrayOutputStream();
        CompressorOutputStream xzOutput = new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.XZ, output);
        Assert.assertNotNull(xzOutput);
        xzOutput.finish();
        xzOutput.close();

        // Test with invalid compressor name
        OutputStream invalidOutput = new ByteArrayOutputStream();
        try {
            CompressorOutputStream invalidOutputStream = new CompressorStreamFactory().createCompressorOutputStream("InvalidCompressor", invalidOutput);
            Assert.fail();
        } catch (CompressorException e) {
            Assert.assertEquals("Compressor: InvalidCompressor not found.", e.getMessage());
        }
        invalidOutput.close();
    }

    @Test
    public void testSetDecompressConcatenated() {
        // Test setting decompressConcatenated to true
        CompressorStreamFactory factory = new CompressorStreamFactory(true);
        boolean decompressConcatenated = factory.getDecompressConcatenated();
        Assert.assertTrue(decompressConcatenated);

        // Test setting decompressConcatenated to false
        factory = new CompressorStreamFactory();
        decompressConcatenated = factory.getDecompressConcatenated();
        Assert.assertFalse(decompressConcatenated);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetDecompressConcatenatedWithSetConstructor() {
        // Test setting decompressConcatenated when constructor is used
        CompressorStreamFactory factory = new CompressorStreamFactory(true);
        factory.setDecompressConcatenated(false);
    }
}