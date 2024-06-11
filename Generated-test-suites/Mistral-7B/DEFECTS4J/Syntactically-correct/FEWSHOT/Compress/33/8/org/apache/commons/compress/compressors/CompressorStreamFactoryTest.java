package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test cases for CompressorStreamFactory class
 */
public class CompressorStreamFactoryTest {

    @Test
    public void testFactoryInstance() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertNotNull(factory);
    }

    @Test
    public void testCreateCompressorInputStreamFromSignature_UnknownFormat() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        try {
            factory.createCompressorInputStream(inputStream);
            assertFalse(true);
        } catch (CompressorException e) {
            assertEquals("No Compressor found for the stream signature.", e.getMessage());
        }
    }

    @Test
    public void testCreateCompressorInputStreamFromName_GZIP() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream("gzip", inputStream);
        assertTrue(compressorInputStream instanceof GzipCompressorInputStream);
    }

    @Test
    public void testCreateCompressorInputStreamFromName_BZIP2() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream("bzip2", inputStream);
        assertTrue(compressorInputStream instanceof BZip2CompressorInputStream);
    }

    @Test
    public void testCreateCompressorInputStreamFromName_XZ() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream("xz", inputStream);
        assertTrue(compressorInputStream instanceof XZCompressorInputStream);
    }

    @Test
    public void testCreateCompressorInputStreamFromName_UnknownFormat() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        try {
            factory.createCompressorInputStream("unknown", inputStream);
            assertFalse(true);
        } catch (CompressorException e) {
            assertEquals("Compressor: unknown not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateCompressorOutputStreamFromName_GZIP() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("gzip", outputStream);
        assertTrue(compressorOutputStream instanceof GzipCompressorOutputStream);
    }

    @Test
    public void testCreateCompressorOutputStreamFromName_BZIP2() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("bzip2", outputStream);
        assertTrue(compressorOutputStream instanceof BZip2CompressorOutputStream);
    }

    @Test
    public void testCreateCompressorOutputStreamFromName_XZ() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("xz", outputStream);
        assertTrue(compressorOutputStream instanceof XZCompressorOutputStream);
    }

    @Test
    public void testCreateCompressorOutputStreamFromName_DEFLATE() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("deflate", outputStream);
        assertTrue(compressorOutputStream instanceof DeflateCompressorOutputStream);
    }

    @Test
    public void testCreateCompressorOutputStreamFromName_UnknownFormat() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        try {
            factory.createCompressorOutputStream("unknown", outputStream);
            assertFalse(true);
        } catch (CompressorException e) {
            assertEquals("Compressor: unknown not found.", e.getMessage());
        }
    }

    @Test
    public void testDecompressConcatenatedProperty() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertFalse(factory.getDecompressConcatenated());

        CompressorStreamFactory factoryWithDecompressConcatenated = new CompressorStreamFactory(true);
        assertTrue(factoryWithDecompressConcatenated.getDecompressConcatenated());
    }

    @Test
    public void testCreateCompressorInputStreamFromSignature_GZIP() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(GzipCompressorInputStream.SIGNATURE);
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(inputStream);
        assertTrue(compressorInputStream instanceof GzipCompressorInputStream);
    }

    @Test
    public void testCreateCompressorInputStreamFromSignature_BZIP2() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(BZip2CompressorInputStream.SIGNATURE);
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(inputStream);
        assertTrue(compressorInputStream instanceof BZip2CompressorInputStream);
    }

    @Test
    public void testCreateCompressorInputStreamFromSignature_XZ() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(XZCompressorInputStream.SIGNATURE);
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(inputStream);
        assertTrue(compressorInputStream instanceof XZCompressorInputStream);
    }

    @Test
    public void testCreateCompressorInputStreamFromSignature_UnknownFormat() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        try {
            factory.createCompressorInputStream(inputStream);
            assertFalse(true);
        } catch (CompressorException e) {
            assertEquals("No Compressor found for the stream signature.", e.getMessage());
        }
    }

    @Test
    public void testCreateCompressorOutputStreamFromSignature_GZIP() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(outputStream, "gzip");
        assertTrue(compressorOutputStream instanceof GzipCompressorOutputStream);
    }

    @Test
    public void testCreateCompressorOutputStreamFromSignature_BZIP2() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(outputStream, "bzip2");
        assertTrue(compressorOutputStream instanceof BZip2CompressorOutputStream);
    }

    @Test
    public void testCreateCompressorOutputStreamFromSignature_XZ() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(outputStream, "xz");
        assertTrue(compressorOutputStream instanceof XZCompressorOutputStream);
    }

    @Test
    public void testCreateCompressorOutputStreamFromSignature_UnknownFormat() throws IOException {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        try {
            factory.createCompressorOutputStream(outputStream, "unknown");
            assertFalse(true);
        } catch (CompressorException e) {
            assertEquals("Compressor: unknown not found.", e.getMessage());
        }
    }
}