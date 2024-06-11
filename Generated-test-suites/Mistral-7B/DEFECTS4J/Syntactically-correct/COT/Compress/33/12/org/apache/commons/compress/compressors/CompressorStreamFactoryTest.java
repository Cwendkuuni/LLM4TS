package org.apache.commons.compress.compressors;

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
import java.util.Arrays;

/**
 * CompressorStreamFactoryTest
 */
public class CompressorStreamFactoryTest {

    private static final String TEST_DATA = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";

    @Test
    public void testCreateCompressorInputStream_DetectsCorrectFormat() throws Exception {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                     .createCompressorOutputStream(CompressorStreamFactory.BZIP2, out);
             InputStream in = new ByteArrayInputStream(out.toByteArray());
             CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(in)) {

            byte[] expectedSignature = BZip2CompressorInputStream.BZIP2_SIGNATURE;
            byte[] actualSignature = new byte[expectedSignature.length];
            IOUtils.readFully(in, actualSignature);

            Assert.assertArrayEquals("Unexpected signature", expectedSignature, actualSignature);
        }
    }

    @Test
    public void testCreateCompressorInputStream_UnsupportedFormat() throws Exception {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                     .createCompressorOutputStream(CompressorStreamFactory.INVALID_FORMAT, out);
             InputStream in = new ByteArrayInputStream(out.toByteArray())) {

            try {
                new CompressorStreamFactory().createCompressorInputStream(in);
                Assert.fail("Expected CompressorException");
            } catch (CompressorException e) {
                // Expected
            }
        }
    }

    @Test
    public void testCreateCompressorOutputStream_BZIP2() throws Exception {
        try (ByteArrayOutputStream inputStreamByteArray = new ByteArrayOutputStream();
             InputStream inputStream = new ByteArrayInputStream(TEST_DATA.getBytes());
             CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                     .createCompressorOutputStream(CompressorStreamFactory.BZIP2, new ByteArrayOutputStream())) {

            IOUtils.copy(inputStream, compressorOutputStream);
            compressorOutputStream.finish();
            compressorOutputStream.close();

            byte[] outputBytes = ((ByteArrayOutputStream) compressorOutputStream).toByteArray();
            CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                    .createCompressorInputStream(new ByteArrayInputStream(outputBytes));

            byte[] expected = TEST_DATA.getBytes();
            byte[] actual = new byte[expected.length];
            IOUtils.readFully(compressorInputStream, actual);

            Assert.assertArrayEquals("Unexpected data", expected, actual);
        }
    }

    @Test
    public void testCreateCompressorOutputStream_DEFLATE() throws Exception {
        try (ByteArrayOutputStream inputStreamByteArray = new ByteArrayOutputStream();
             InputStream inputStream = new ByteArrayInputStream(TEST_DATA.getBytes());
             CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                     .createCompressorOutputStream(CompressorStreamFactory.DEFLATE, new ByteArrayOutputStream())) {

            IOUtils.copy(inputStream, compressorOutputStream);
            compressorOutputStream.finish();
            compressorOutputStream.close();

            byte[] outputBytes = ((ByteArrayOutputStream) compressorOutputStream).toByteArray();
            CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                    .createCompressorInputStream(new ByteArrayInputStream(outputBytes));

            byte[] expected = TEST_DATA.getBytes();
            byte[] actual = new byte[expected.length];
            IOUtils.readFully(compressorInputStream, actual);

            Assert.assertArrayEquals("Unexpected data", expected, actual);
        }
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStream_InvalidFormat() throws Exception {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                     .createCompressorOutputStream(CompressorStreamFactory.INVALID_FORMAT, out);) {

            // Input Stream should not be created
            Assert.fail("Expected CompressorException");
        }
    }

    @Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        Assert.assertFalse("Unexpected value", factory.getDecompressConcatenated());

        CompressorStreamFactory factoryWithConcatenated = new CompressorStreamFactory(true);
        Assert.assertTrue("Unexpected value", factoryWithConcatenated.getDecompressConcatenated());
    }
}