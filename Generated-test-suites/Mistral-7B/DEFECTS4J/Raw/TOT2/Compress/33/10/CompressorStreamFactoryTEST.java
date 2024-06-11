package org.apache.commons.compress.compressors;

import org.apache.commons.compress.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.lzma.LZMACompressorInputStream;
import org.apache.commons.compress.compressors.lzma.LZMACompressorOutputStream;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorInputStream;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorOutputStream;
import org.apache.commons.compress.compressors.snappy.FramedSnappyCompressorInputStream;
import org.apache.commons.compress.compressors.snappy.SnappyCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test cases for {@link CompressorStreamFactory} class.
 */
public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream_Gzip() throws IOException, CompressorException {
        // Given
        InputStream input = new ByteArrayInputStream(GzipCompressorInputStream.GZIP_SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.GZIP, input);

        // When
        int read = compressorInputStream.read();

        // Then
        Assert.assertEquals(-1, read); // EOF
        Assert.assertEquals(CompressorStreamFactory.GZIP, compressorInputStream.getName());
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStream_Bzip2() throws IOException, CompressorException {
        // Given
        InputStream input = new ByteArrayInputStream(BZip2CompressorInputStream.BZIP2_SIGNATURE);
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.BZIP2, input);

        // When
        int read = compressorInputStream.read();

        // Then
        Assert.assertEquals(-1, read); // EOF
        Assert.assertEquals(CompressorStreamFactory.BZIP2, compressorInputStream.getName());
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStream_XZ() throws IOException, CompressorException {
        // Given
        InputStream input = new ByteArrayInputStream(XZUtils.getXZ_SIGNATURE());
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.XZ, input);

        // When
        int read = compressorInputStream.read();

        // Then
        Assert.assertEquals(-1, read); // EOF
        Assert.assertEquals(CompressorStreamFactory.XZ, compressorInputStream.getName());
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStream_Unknown() throws IOException, CompressorException {
        // Given
        InputStream input = new ByteArrayInputStream("Unknown".getBytes());
        CompressorStreamFactory compressorStreamFactory = new CompressorStreamFactory();

        // When & Then
        try {
            CompressorInputStream compressorInputStream = compressorStreamFactory.createCompressorInputStream("Unknown", input);
            Assert.fail();
        } catch (CompressorException e) {
            Assert.assertEquals("Compressor: Unknown not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateCompressorInputStream_NullInputStream() {
        // Given
        InputStream input = null;
        CompressorStreamFactory compressorStreamFactory = new CompressorStreamFactory();

        // When & Then
        try {
            compressorStreamFactory.createCompressorInputStream(CompressorStreamFactory.BZIP2, input);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Stream must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateCompressorOutputStream_Gzip() throws IOException, CompressorException {
        // Given
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.GZIP, output);

        // When
        byte[] data = "Hello, World!".getBytes();
        compressorOutputStream.write(data);
        compressorOutputStream.finish();

        // Then
        byte[] expected = GzipCompressorOutputStream.compress(data);
        Assert.assertArrayEquals(expected, output.toByteArray());
        Assert.assertEquals(CompressorStreamFactory.GZIP, compressorOutputStream.getName());
        compressorOutputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStream_Bzip2() throws IOException, CompressorException {
        // Given
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.BZIP2, output);

        // When
        byte[] data = "Hello, World!".getBytes();
        compressorOutputStream.write(data);
        compressorOutputStream.finish();

        // Then
        byte[] expected = BZip2CompressorOutputStream.compress(data);
        Assert.assertArrayEquals(expected, output.toByteArray());
        Assert.assertEquals(CompressorStreamFactory.BZIP2, compressorOutputStream.getName());
        compressorOutputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStream_Xz() throws IOException, CompressorException {
        // Given
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.XZ, output);

        // When
        byte[] data = "Hello, World!".getBytes();
        compressorOutputStream.write(data);
        compressorOutputStream.finish();

        // Then
        byte[] expected = XZCompressorOutputStream.compress(data);
        Assert.assertArrayEquals(expected, output.toByteArray());
        Assert.assertEquals(CompressorStreamFactory.XZ, compressorOutputStream.getName());
        compressorOutputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStream_Unknown() throws IOException, CompressorException {
        // Given
        OutputStream output = new ByteArrayOutputStream();
        CompressorStreamFactory compressorStreamFactory = new CompressorStreamFactory();

        // When & Then
        try {
            compressorStreamFactory.createCompressorOutputStream("Unknown", output);
            Assert.fail();
        } catch (CompressorException e) {
            Assert.assertEquals("Compressor: Unknown not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateCompressorOutputStream_NullOutputStream() {
        // Given
        OutputStream output = null;
        CompressorStreamFactory compressorStreamFactory = new CompressorStreamFactory();

        // When & Then
        try {
            compressorStreamFactory.createCompressorOutputStream(CompressorStreamFactory.BZIP2, output);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Stream must not be null.", e.getMessage());
        }
    }
}