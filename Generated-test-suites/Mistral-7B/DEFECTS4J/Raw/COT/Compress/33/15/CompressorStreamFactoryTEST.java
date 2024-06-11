package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Unit tests for {@link CompressorStreamFactory} class
 */
public class CompressorStreamFactoryTest {

    @org.junit.Test
    public void testCreateCompressorInputStream_AutoDetection() throws IOException {
        byte[] gzipSignature = Arrays.copyOfRange(GzipCompressorInputStream.signature, 0, 10);
        byte[] bzip2Signature = Arrays.copyOfRange(BZip2CompressorInputStream.signature, 0, 10);

        try (ByteArrayInputStream testInputStream = new ByteArrayInputStream(gzipSignature)) {
            CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(testInputStream);
            assertNotNull(compressorInputStream);
            assertInstanceOf(GzipCompressorInputStream.class, compressorInputStream);
        }

        try (ByteArrayInputStream testInputStream = new ByteArrayInputStream(bzip2Signature)) {
            CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(testInputStream);
            assertNotNull(compressorInputStream);
            assertInstanceOf(BZip2CompressorInputStream.class, compressorInputStream);
        }

        try (ByteArrayInputStream testInputStream = new ByteArrayInputStream(new byte[12])) {
            CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(testInputStream);
            assertNull(compressorInputStream);
        }
    }

    @org.junit.Test
    public void testCreateCompressorInputStream_ByName() throws CompressorException, IOException {
        try (ByteArrayInputStream testInputStream = new ByteArrayInputStream(GzipCompressorInputStream.signature)) {
            CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream("GZIP", testInputStream);
            assertNotNull(compressorInputStream);
            assertInstanceOf(GzipCompressorInputStream.class, compressorInputStream);
        }

        try (ByteArrayInputStream testInputStream = new ByteArrayInputStream(BZip2CompressorInputStream.signature)) {
            CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream("BZIP2", testInputStream);
            assertNotNull(compressorInputStream);
            assertInstanceOf(BZip2CompressorInputStream.class, compressorInputStream);
        }

        try (ByteArrayInputStream testInputStream = new ByteArrayInputStream(new byte[12])) {
            assertThrows(CompressorException.class, () -> new CompressorStreamFactory().createCompressorInputStream("UNKNOWN", testInputStream));
        }
    }

    @org.junit.Test
    public void testCreateCompressorOutputStream_ByName() throws IOException {
        try (ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream()) {
            CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream("GZIP", testOutputStream);
            assertNotNull(compressorOutputStream);
            assertInstanceOf(GzipCompressorOutputStream.class, compressorOutputStream);
        }

        try (ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream()) {
            CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream("BZIP2", testOutputStream);
            assertNotNull(compressorOutputStream);
            assertInstanceOf(BZip2CompressorOutputStream.class, compressorOutputStream);
        }

        try (ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream()) {
            assertThrows(CompressorException.class, () -> new CompressorStreamFactory().createCompressorOutputStream("UNKNOWN", testOutputStream));
        }
    }

    @org.junit.Test
    public void testDecompressConcatenated() throws IOException, CompressorException {
        try (ByteArrayInputStream testInputStream = new ByteArrayInputStream(GzipCompressorInputStream.signature);
             ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream()) {
            CompressorStreamFactory compressorStreamFactory = new CompressorStreamFactory(true);
            CompressorOutputStream compressorOutputStream = compressorStreamFactory.createCompressorOutputStream("GZIP", testOutputStream);

            CompressorInputStream compressorInputStream = compressorStreamFactory.createCompressorInputStream("GZIP", testInputStream);
            IOUtils.copy(compressorInputStream, compressorOutputStream);
            compressorOutputStream.finish();

            CompressorInputStream decompressorInputStream = compressorStreamFactory.createCompressorInputStream("GZIP", new ByteArrayInputStream(testOutputStream.toByteArray()));
            IOUtils.copy(decompressorInputStream, new ByteArrayOutputStream());
        }

        try (ByteArrayInputStream testInputStream = new ByteArrayInputStream(GzipCompressorInputStream.signature);
             ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream()) {
            CompressorStreamFactory compressorStreamFactory = new CompressorStreamFactory();
            CompressorOutputStream compressorOutputStream = compressorStreamFactory.createCompressorOutputStream("GZIP", testOutputStream);

            CompressorInputStream compressorInputStream = compressorStreamFactory.createCompressorInputStream("GZIP", testInputStream);
            IOUtils.copy(compressorInputStream, compressorOutputStream);
            compressorOutputStream.finish();

            CompressorInputStream decompressorInputStream = compressorStreamFactory.createCompressorInputStream("GZIP", new ByteArrayInputStream(testOutputStream.toByteArray()));
            IOUtils.copy(decompressorInputStream, new ByteArrayOutputStream(5));
            assertFalse(decompressorInputStream.hasNextByte());
        }
    }

    @org.junit.Test
    public void testSetDecompressConcatenated() throws CompressorException {
        CompressorStreamFactory compressorStreamFactory = new CompressorStreamFactory();
        assertFalse(compressorStreamFactory.getDecompressConcatenated());

        compressorStreamFactory.setDecompressConcatenated(true);
        assertTrue(compressorStreamFactory.getDecompressConcatenated());
    }
}