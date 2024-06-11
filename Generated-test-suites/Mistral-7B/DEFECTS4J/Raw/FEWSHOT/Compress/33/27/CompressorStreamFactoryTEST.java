package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZUtils;
import org.apache.commons.compress.test.CompressorTestUtils;
import org.junit.Test;

/**
 * Test cases for the CompressorStreamFactory class.
 */
public class CompressorStreamFactoryTest {

    @Test
    public void testFactoryCreation() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertNull(factory.getDecompressConcatenated());

        factory = new CompressorStreamFactory(false);
        assertFalse(factory.getDecompressConcatenated());

        factory = new CompressorStreamFactory(true);
        assertTrue(factory.getDecompressConcatenated());
    }

    @Test
    public void testCreateBZip2CompressorInputStream() throws Exception {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream input = new ByteArrayInputStream(CompressorTestUtils.BZIP2_TEST_DATA);
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream("bzip2", input);
        assertSame(BZip2CompressorInputStream.class, compressorInputStream.getClass());
    }

    @Test(expected = CompressorException.class)
    public void testCreateUnknownCompressorInputStream() throws Exception {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream input = new ByteArrayInputStream(new byte[0]);
        factory.createCompressorInputStream("unknown", input);
    }

    @Test
    public void testCreateGZipCompressorInputStream() throws Exception {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream input = new ByteArrayInputStream(CompressorTestUtils.GZIP_TEST_DATA);
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream("gzip", input);
        assertSame(GzipCompressorInputStream.class, compressorInputStream.getClass());
    }

    @Test(expected = CompressorException.class)
    public void testCreatePack200CompressorInputStream() throws Exception {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream input = new ByteArrayInputStream(CompressorTestUtils.PACK200_TEST_DATA);
        factory.createCompressorInputStream("pack200", input);
    }

    @Test
    public void testCreateXZCompressorInputStream() throws Exception {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream input = new ByteArrayInputStream(CompressorTestUtils.XZ_TEST_DATA);
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream("xz", input);
        assertSame(XZCompressorInputStream.class, compressorInputStream.getClass());
    }

    @Test(expected = CompressorException.class)
    public void testCreateXZCompressorInputStreamWithoutLZMA() throws Exception {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream input = new ByteArrayInputStream(CompressorTestUtils.XZ_TEST_DATA);
        if (!XZUtils.isLZMACompressionAvailable()) {
            throw new AssertionError("LZMA decoding is not available");
        }
        factory.setDecompressConcatenated(false);
        factory.createCompressorInputStream("xz", input);
    }

    @Test(expected = CompressorException.class)
    public void testCreateLZMACompressorInputStream() throws Exception {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream input = new ByteArrayInputStream(CompressorTestUtils.LZMA_TEST_DATA);
        factory.createCompressorInputStream("lzma", input);
    }

    @Test
    public void testCreateDeflateCompressorInputStream() throws Exception {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream input = new ByteArrayInputStream(CompressorTestUtils.DEFLATE_TEST_DATA);
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream("deflate", input);
        assertSame(DeflateCompressorInputStream.class, compressorInputStream.getClass());
    }

    @Test
    public void testCreateBZip2CompressorOutputStream() throws Exception {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream output = new ByteArrayOutputStream(16);
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("bzip2", output);
        compressorOutputStream.write("Test".getBytes());
        compressorOutputStream.finish();
        byte[] expected = CompressorTestUtils.BZIP2_TEST_OUTPUT;
        byte[] result = ((ByteArrayOutputStream) output).toByteArray();
        assertArrayEquals(expected, result);
    }

    @Test(expected = CompressorException.class)
    public void testCreateUnknownCompressorOutputStream() throws Exception {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream output = new ByteArrayOutputStream(16);
        factory.createCompressorOutputStream("unknown", output);
    }

    @Test
    public void testCreateGZipCompressorOutputStream() throws Exception {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream output = new ByteArrayOutputStream(16);
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("gzip", output);
        compressorOutputStream.write("Test".getBytes());
        compressorOutputStream.finish();
        byte[] expected = CompressorTestUtils.GZIP_TEST_OUTPUT;
        byte[] result = ((ByteArrayOutputStream) output).toByteArray();
        assertArrayEquals(expected, result);
    }

    @Test(expected = CompressorException.class)
    public void testCreatePack200CompressorOutputStream() throws Exception {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream output = new ByteArrayOutputStream(16);
        factory.createCompressorOutputStream("pack200", output);
    }

    @Test
    public void testCreateXZCompressorOutputStream() throws Exception {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream output = new ByteArrayOutputStream(16);
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("xz", output);
        compressorOutputStream.write("Test".getBytes());
        compressorOutputStream.finish();
        byte[] expected = CompressorTestUtils.XZ_TEST_OUTPUT;
        byte[] result = ((ByteArrayOutputStream) output).toByteArray();
        assertArrayEquals(expected, result);
    }

    @Test(expected = CompressorException.class)
    public void testCreateXZCompressorOutputStreamWithoutLZMA() throws Exception {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream output = new ByteArrayOutputStream(16);
        if (!XZUtils.isLZMACompressionAvailable()) {
            throw new AssertionError("LZMA encoding is not available");
        }
        factory.setDecompressConcatenated(false);
        factory.createCompressorOutputStream("xz", output);
    }

    @Test
    public void testCreateLZMACompressorOutputStream() throws Exception {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream output = new ByteArrayOutputStream(16);
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("lzma", output);
        compressorOutputStream.write("Test".getBytes());
        compressorOutputStream.finish();
        byte[] expected = CompressorTestUtils.LZMA_TEST_OUTPUT;
        byte[] result = ((ByteArrayOutputStream) output).toByteArray();
        assertArrayEquals(expected, result);
    }

    @Test
    public void testCreateDeflateCompressorOutputStream() throws Exception {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream output = new ByteArrayOutputStream(16);
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream("deflate", output);
        compressorOutputStream.write("Test".getBytes());
        compressorOutputStream.finish();
        byte[] expected = CompressorTestUtils.DEFLATE_TEST_OUTPUT;
        byte[] result = ((ByteArrayOutputStream) output).toByteArray();
        assertArrayEquals(expected, result);
    }
}