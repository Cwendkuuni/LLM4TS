package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;

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
 * CompressorStreamFactoryTest
 */
public class CompressorStreamFactoryTest {

    @org.junit.Test
    public void testCreateCompressorInputStreamBZip2() throws IOException, CompressorException {
        byte[] inputData = "Test data for BZip2 compressor.".getBytes();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CompressorOutputStream cos = new CompressorStreamFactory()
                     .createCompressorOutputStream(CompressorStreamFactory.BZIP2, out);) {
            IOUtils.write(inputData, cos);
            cos.finish();
            cos.close();

            try (ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
                 CompressorInputStream cis = new CompressorStreamFactory()
                         .createCompressorInputStream(CompressorStreamFactory.BZIP2, in)) {
                byte[] expected = "Test data for BZip2 compressor.".getBytes();
                byte[] actual = new byte[expected.length];
                int numRead = IOUtils.readFully(cis, actual);
                assertEquals("Data read from BZip2 decompressed stream is incorrect.",
                        expected, actual, 0);
            }
        }
    }

    @org.junit.Test
    public void testCreateCompressorInputStreamGZip() throws IOException, CompressorException {
        byte[] inputData = "Test data for GZip compressor.".getBytes();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CompressorOutputStream cos = new CompressorStreamFactory()
                     .createCompressorOutputStream(CompressorStreamFactory.GZIP, out);) {
            IOUtils.write(inputData, cos);
            cos.finish();
            cos.close();

            try (ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
                 CompressorInputStream cis = new CompressorStreamFactory()
                         .createCompressorInputStream(CompressorStreamFactory.GZIP, in)) {
                byte[] expected = "Test data for GZip compressor.".getBytes();
                byte[] actual = new byte[expected.length];
                int numRead = IOUtils.readFully(cis, actual);
                assertEquals("Data read from GZip decompressed stream is incorrect.",
                        expected, actual, 0);
            }
        }
    }

    @org.junit.Test
    public void testCreateCompressorInputStreamBZip2WithInvalidSignature()
            throws IOException, CompressorException {
        byte[] inputData = "Test data for BZip2 compressor.".getBytes();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             InputStream is = new ByteArrayInputStream(new byte[]{"I"});
             CompressorInputStream cis = new CompressorStreamFactory()
                     .createCompressorInputStream(CompressorStreamFactory.BZIP2, is)) {
            // Read input stream to make sure the signature is not read
            int numRead = IOUtils.readFully(is, new byte[1]);
            assertEquals("Invalid BZip2 signature.", 1, numRead);

            try {
                new BZip2CompressorInputStream(is, false);
                fail("Expected CompressorException.");
            } catch (CompressorException e) {
                // Expected
            }
        }
    }

    @org.junit.Test
    public void testCreateCompressorInputStreamWithInvalidName()
            throws IOException, CompressorException {
        try (ByteArrayInputStream is = new ByteArrayInputStream("Test".getBytes())) {
            try {
                new CompressorStreamFactory().createCompressorInputStream("INVALID", is);
                fail("Expected CompressorException.");
            } catch (CompressorException e) {
                // Expected
            }
        }
    }

    @org.junit.Test
    public void testCreateCompressorOutputStreamBZip2() throws IOException {
        byte[] inputData = "Test data for BZip2 compressor.".getBytes();

        try (ByteArrayInputStream in = new ByteArrayInputStream(inputData);
             ByteArrayOutputStream out = new ByteArrayOutputStream();
             CompressorOutputStream cos = new CompressorStreamFactory()
                     .createCompressorOutputStream(CompressorStreamFactory.BZIP2, out);) {
            IOUtils.copy(in, cos);
            cos.finish();
            cos.close();

            try (ByteArrayInputStream in2 = new ByteArrayInputStream(out.toByteArray());
                 BZip2CompressorInputStream cis = new BZip2CompressorInputStream(in2, false)) {
                byte[] expected = "Test data for BZip2 compressor.".getBytes();
                byte[] actual = new byte[expected.length];
                int numRead = IOUtils.readFully(cis, actual);
                assertEquals("Data read from BZip2 compressed stream is incorrect.",
                        expected, actual, 0);
            }
        }
    }

    @org.junit.Test
    public void testCreateCompressorOutputStreamGZip() throws IOException {
        byte[] inputData = "Test data for GZip compressor.".getBytes();

        try (ByteArrayInputStream in = new ByteArrayInputStream(inputData);
             ByteArrayOutputStream out = new ByteArrayOutputStream();
             CompressorOutputStream cos = new CompressorStreamFactory()
                     .createCompressorOutputStream(CompressorStreamFactory.GZIP, out);) {
            IOUtils.copy(in, cos);
            cos.finish();
            cos.close();

            try (ByteArrayInputStream in2 = new ByteArrayInputStream(out.toByteArray());
                 GzipCompressorInputStream cis = new GzipCompressorInputStream(in2, false)) {
                byte[] expected = "Test data for GZip compressor.".getBytes();
                byte[] actual = new byte[expected.length];
                int numRead = IOUtils.readFully(cis, actual);
                assertEquals("Data read from GZip compressed stream is incorrect.",
                        expected, actual, 0);
            }
        }
    }

    @org.junit.Test
    public void testCreateCompressorOutputStreamBZip2WithInvalidSignature()
            throws IOException {
        byte[] inputData = "Test data for BZip2 compressor.".getBytes();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             OutputStream os = out::write) {
            CompressorOutputStream cos = new CompressorStreamFactory()
                    .createCompressorOutputStream(CompressorStreamFactory.BZIP2, os);

            try {
                cos.finish();
                fail("Expected IOException.");
            } catch (IOException e) {
                // Expected
            }

            try {
                cos.close();
                fail("Expected IOException.");
            } catch (IOException e) {
                // Expected
            }

            try {
                IOUtils.write(inputData, cos);
                fail("Expected IOException.");
            } catch (IOException e) {
                // Expected
            }
        }
    }

    @org.junit.Test
    public void testCreateCompressorOutputStreamWithInvalidName()
            throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             OutputStream os = out::write) {
            try {
                new CompressorStreamFactory().createCompressorOutputStream("INVALID", os);
                fail("Expected CompressorException.");
            } catch (CompressorException e) {
                // Expected
            }
        }
    }

    @org.junit.Test
    public void testCreateCompressorOutputStreamWithNullOutputStream()
            throws IOException, CompressorException {
        try {
            new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.BZIP2, null);
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertEquals("DecompressConcatenated is false by default.", false, factory.getDecompressConcatenated());

        CompressorStreamFactory factoryConcatenated = new CompressorStreamFactory(true);
        assertEquals("DecompressConcatenated is true when set.", true, factoryConcatenated.getDecompressConcatenated());
    }
}