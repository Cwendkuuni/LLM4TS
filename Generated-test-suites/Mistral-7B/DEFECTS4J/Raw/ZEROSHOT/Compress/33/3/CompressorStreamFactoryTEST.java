package org.apache.commons.compress.compressors.test;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.test.Utils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

/**
 * Test for CompressorStreamFactory.
 */
public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStreamGzip() throws IOException {
        final String name = CompressorStreamFactory.GZIP;
        final InputStream input = new ByteArrayInputStream(Utils.GZIP_SIGNATURE.getBytes());
        CompressorInputStream compressorInputStream = CompressorStreamFactory.newInstance().createCompressorInputStream(name, input);
        Assert.assertEquals(GzipCompressorInputStream.class, compressorInputStream.getClass());
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStreamBzip2() throws IOException {
        final String name = CompressorStreamFactory.BZIP2;
        final InputStream input = new ByteArrayInputStream(Utils.BZIP2_SIGNATURE.getBytes());
        CompressorInputStream compressorInputStream = CompressorStreamFactory.newInstance().createCompressorInputStream(name, input);
        Assert.assertEquals(BZip2CompressorInputStream.class, compressorInputStream.getClass());
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStreamXz() throws IOException {
        final String name = CompressorStreamFactory.XZ;
        final InputStream input = new ByteArrayInputStream(Utils.XZ_SIGNATURE.getBytes());
        CompressorInputStream compressorInputStream = CompressorStreamFactory.newInstance().createCompressorInputStream(name, input);
        Assert.assertEquals(XZCompressorInputStream.class, compressorInputStream.getClass());
        compressorInputStream.close();
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStreamUnknown() throws IOException {
        final String name = "Unknown";
        final InputStream input = new ByteArrayInputStream(new byte[10]);
        CompressorStreamFactory compressorStreamFactory = CompressorStreamFactory.newInstance();
        compressorStreamFactory.createCompressorInputStream(name, input);
    }

    @Test
    public void testCreateCompressorOutputStreamGzip() throws IOException {
        final String name = CompressorStreamFactory.GZIP;
        final OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = CompressorStreamFactory.newInstance().createCompressorOutputStream(name, output);
        compressorOutputStream.write(Utils.PLAIN_DATA.getBytes());
        compressorOutputStream.finish();
        compressorOutputStream.close();
        byte[] result = ((ByteArrayOutputStream) output).toByteArray();
        Assert.assertFalse(Utils.isPlainData(result));
        Assert.assertTrue(Utils.isGzipData(result));
    }

    @Test
    public void testCreateCompressorOutputStreamBzip2() throws IOException {
        final String name = CompressorStreamFactory.BZIP2;
        final OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = CompressorStreamFactory.newInstance().createCompressorOutputStream(name, output);
        compressorOutputStream.write(Utils.PLAIN_DATA.getBytes());
        compressorOutputStream.finish();
        compressorOutputStream.close();
        byte[] result = ((ByteArrayOutputStream) output).toByteArray();
        Assert.assertFalse(Utils.isPlainData(result));
        Assert.assertTrue(Utils.isBzip2Data(result));
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStreamUnknown() throws IOException {
        final String name = "Unknown";
        final OutputStream output = new ByteArrayOutputStream();
        CompressorStreamFactory compressorStreamFactory = CompressorStreamFactory.newInstance();
        compressorStreamFactory.createCompressorOutputStream(name, output);
    }

    @Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory compressorStreamFactory = CompressorStreamFactory.newInstance(true);
        Assert.assertTrue(compressorStreamFactory.getDecompressConcatenated());
        compressorStreamFactory = CompressorStreamFactory.newInstance(false);
        Assert.assertFalse(compressorStreamFactory.getDecompressConcatenated());
    }

    // Factory method test
    static CompressorStreamFactory newInstance() {
        return new CompressorStreamFactory();
    }

    static CompressorStreamFactory newInstance(boolean decompressConcatenated) {
        return new CompressorStreamFactory(decompressConcatenated);
    }
}