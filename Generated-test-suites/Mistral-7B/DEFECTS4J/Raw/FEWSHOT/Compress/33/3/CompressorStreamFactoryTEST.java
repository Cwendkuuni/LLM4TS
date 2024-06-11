package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.XZCompressorInputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorFactory;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.XZCompressorOutputStream;
import org.junit.Test;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateBZip2CompressorInputStream() {
        final InputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.BZIP2, inputStream);
        assertNotNull(compressorInputStream);
        assertNull(compressorInputStream.getDelegate());
        assertNull(compressorInputStream.getInputStream());
        BZip2CompressorInputStream bzip2CompressorInputStream = (BZip2CompressorInputStream) compressorInputStream;
        assertNotNull(bzip2CompressorInputStream.getInputStream());
        bzip2CompressorInputStream.close();
        inputStream.close();
    }

    @Test
    public void testCreateGzipCompressorInputStream() {
        final InputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.GZIP, inputStream);
        assertNotNull(compressorInputStream);
        assertNull(compressorInputStream.getDelegate());
        assertNull(compressorInputStream.getInputStream());
        GzipCompressorInputStream gzipCompressorInputStream = (GzipCompressorInputStream) compressorInputStream;
        assertNotNull(gzipCompressorInputStream.getInputStream());
        gzipCompressorInputStream.close();
        inputStream.close();
    }

    @Test
    public void testCreateXZCompressorInputStream() {
        final InputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(CompressorStreamFactory.XZ, inputStream);
        assertNotNull(compressorInputStream);
        assertNull(compressorInputStream.getDelegate());
        assertNull(compressorInputStream.getInputStream());
        XZCompressorInputStream xzCompressorInputStream = (XZCompressorInputStream) compressorInputStream;
        assertNotNull(xzCompressorInputStream.getInputStream());
        xzCompressorInputStream.close();
        inputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStream() {
        final OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.BZIP2, outputStream);
        assertNotNull(compressorOutputStream);
        assertNull(compressorOutputStream.getDelegate());
        assertNull(compressorOutputStream.getOutputStream());
        compressorOutputStream.finish();
        compressorOutputStream.close();
        assertNull(outputStream.toString());
        assertNull(outputStream.toByteArray());

        outputStream = new ByteArrayOutputStream();
        compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.GZIP, outputStream);
        assertNotNull(compressorOutputStream);
        assertNull(compressorOutputStream.getDelegate());
        assertNull(compressorOutputStream.getOutputStream());
        compressorOutputStream.finish();
        compressorOutputStream.close();
        assertNull(outputStream.toString());
        assertNull(outputStream.toByteArray());

        outputStream = new ByteArrayOutputStream();
        compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.XZ, outputStream);
        assertNotNull(compressorOutputStream);
        assertNull(compressorOutputStream.getDelegate());
        assertNull(compressorOutputStream.getOutputStream());
        compressorOutputStream.finish();
        compressorOutputStream.close();
        assertNull(outputStream.toString());
        assertNull(outputStream.toByteArray());

        outputStream = new ByteArrayOutputStream();
        compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(CompressorStreamFactory.DEFLATE, outputStream);
        assertNotNull(compressorOutputStream);
        assertNull(compressorOutputStream.getDelegate());
        assertNull(compressorOutputStream.getOutputStream());
        compressorOutputStream.finish();
        compressorOutputStream.close();
        assertNull(outputStream.toString());
        assertNotNull(outputStream.toByteArray());
    }

    @Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory compressorStreamFactory = new CompressorStreamFactory();
        assertFalse(compressorStreamFactory.getDecompressConcatenated());

        compressorStreamFactory = new CompressorStreamFactory(true);
        assertTrue(compressorStreamFactory.getDecompressConcatenated());
    }
}