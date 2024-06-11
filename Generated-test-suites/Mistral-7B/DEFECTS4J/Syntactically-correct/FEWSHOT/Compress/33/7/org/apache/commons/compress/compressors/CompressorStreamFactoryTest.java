package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZUtils;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorInputStream;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorFactory;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

/**
 * Unit tests for CompressorStreamFactory class
 */
public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream_GZIP() throws Exception {
        CompressorFactory factory = new CompressorStreamFactory();
        InputStream is = new ByteArrayInputStream("Gzip signature".getBytes());
        CompressorInputStream inputStream = factory.createCompressorInputStream("gzip", is);
        assertNotNull(inputStream);
        assertNull(inputStream.getInputStream());
        IOUtils.closeQuietly(inputStream);
    }

    @Test
    public void testCreateCompressorInputStream_BZIP2() throws Exception {
        CompressorFactory factory = new CompressorStreamFactory();
        InputStream is = new ByteArrayInputStream("Bzip2 signature".getBytes());
        CompressorInputStream inputStream = factory.createCompressorInputStream("bzip2", is);
        assertNotNull(inputStream);
        assertNull(inputStream.getInputStream());
        IOUtils.closeQuietly(inputStream);
    }

    @Test
    public void testCreateCompressorInputStream_XZ() throws Exception {
        CompressorFactory factory = new CompressorStreamFactory();
        InputStream is = new ByteArrayInputStream("Xz signature".getBytes());
        CompressorInputStream inputStream = factory.createCompressorInputStream("xz", is);
        assertNotNull(inputStream);
        assertNull(inputStream.getInputStream());
        IOUtils.closeQuietly(inputStream);
    }

    @Test
    public void testCreateCompressorInputStream_PACK200() throws Exception {
        CompressorFactory factory = new CompressorStreamFactory();
        InputStream is = new ByteArrayInputStream("Pack200 signature".getBytes());
        CompressorInputStream inputStream = factory.createCompressorInputStream("pack200", is);
        assertNotNull(inputStream);
        assertNull(inputStream.getInputStream());
        IOUtils.closeQuietly(inputStream);
    }

    @Test
    public void testCreateCompressorInputStream_DEFLATE() throws Exception {
        CompressorFactory factory = new CompressorStreamFactory();
        InputStream is = new ByteArrayInputStream("Deflate signature".getBytes());
        CompressorInputStream inputStream = factory.createCompressorInputStream("deflate", is);
        assertNotNull(inputStream);
        assertNull(inputStream.getInputStream());
        IOUtils.closeQuietly(inputStream);
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_InvalidName() throws Exception {
        CompressorFactory factory = new CompressorStreamFactory();
        InputStream is = new ByteArrayInputStream("Invalid signature".getBytes());
        factory.createCompressorInputStream("invalid", is);
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStream_NullInputStream() throws Exception {
        CompressorFactory factory = new CompressorStreamFactory();
        factory.createCompressorInputStream("gzip", null);
    }

    @Test
    public void testCreateCompressorOutputStream_GZIP() throws Exception {
        CompressorFactory factory = new CompressorStreamFactory();
        OutputStream os = new ByteArrayOutputStream();
        CompressorOutputStream outputStream = factory.createCompressorOutputStream("gzip", os);
        assertNotNull(outputStream);
        IOUtils.closeQuietly(outputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_BZIP2() throws Exception {
        CompressorFactory factory = new CompressorStreamFactory();
        OutputStream os = new ByteArrayOutputStream();
        CompressorOutputStream outputStream = factory.createCompressorOutputStream("bzip2", os);
        assertNotNull(outputStream);
        IOUtils.closeQuietly(outputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_XZ() throws Exception {
        CompressorFactory factory = new CompressorStreamFactory();
        OutputStream os = new ByteArrayOutputStream();
        CompressorOutputStream outputStream = factory.createCompressorOutputStream("xz", os);
        assertNotNull(outputStream);
        IOUtils.closeQuietly(outputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_PACK200() throws Exception {
        CompressorFactory factory = new CompressorStreamFactory();
        OutputStream os = new ByteArrayOutputStream();
        CompressorOutputStream outputStream = factory.createCompressorOutputStream("pack200", os);
        assertNotNull(outputStream);
        IOUtils.closeQuietly(outputStream);
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStream_InvalidName() throws Exception {
        CompressorFactory factory = new CompressorStreamFactory();
        OutputStream os = new ByteArrayOutputStream();
        factory.createCompressorOutputStream("invalid", os);
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStream_NullOutputStream() throws Exception {
        CompressorFactory factory = new CompressorStreamFactory();
        factory.createCompressorOutputStream("gzip", null);
    }

    @Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertTrue(factory.getDecompressConcatenated() == false);
        CompressorStreamFactory factory2 = new CompressorStreamFactory(true);
        assertTrue(factory2.getDecompressConcatenated() == true);
    }
}