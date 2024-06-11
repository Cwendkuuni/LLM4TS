package org.apache.commons.compress.compressors;

import static org.apache.commons.compress.compressors.CompressorStreamFactory.BZIP2;
import static org.apache.commons.compress.compressors.CompressorStreamFactory.DEFLATE;
import static org.apache.commons.compress.compressors.CompressorStreamFactory.GZIP;
import static org.apache.commons.compress.compressors.CompressorStreamFactory.PACK200;
import static org.apache.commons.compress.compressors.CompressorStreamFactory.XZ;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.lzma.LZMACompressorInputStream;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorInputStream;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStreamBZIP2() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"\u0042\u005A\u0042\u0068"});
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(BZIP2, input);
        assertEquals(BZip2CompressorInputStream.class, compressorInputStream.getClass());
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStreamUnsupportedFormat() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"\u0042\u005A\u0042\u0068"});
        new CompressorStreamFactory().createCompressorInputStream("UNSUPPORTED", input);
    }

    @Test
    public void testCreateCompressorInputStreamGZip() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"\u0047\u004E\u0054\u0065\u0078"});
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(GZIP, input);
        assertEquals(GzipCompressorInputStream.class, compressorInputStream.getClass());
    }

    @Test
    public void testCreateCompressorInputStreamPACK200() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"\u004D\u0041\u004E\u004B\u0045\u0054\u005F\u0053\u0054\u0052\u002E"});
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(PACK200, input);
        assertEquals(Pack200CompressorInputStream.class, compressorInputStream.getClass());
    }

    @Test
    public void testCreateCompressorInputStreamXZ() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"\u006B\u0078\u0058\u004C\u0069\u006E\u0065"});
        CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(XZ, input);
        if (XZUtils.isXZCompressionAvailable()) {
            assertEquals(XZCompressorInputStream.class, compressorInputStream.getClass());
        } else {
            fail("XZ compression is not available");
        }
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStreamUnknownFormat() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"\u0042\u005A\u0042\u0068"});
        new CompressorStreamFactory()
                .createCompressorInputStream("UNKNOWN", input);
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStreamNullInput() throws IOException {
        new CompressorStreamFactory()
                .createCompressorInputStream(BZIP2, null);
    }

    @Test
    public void testCreateCompressorOutputStreamBZIP2() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(BZIP2, output);
        assertEquals(BZip2CompressorOutputStream.class, compressorOutputStream.getClass());
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStreamUnsupportedFormat() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        new CompressorStreamFactory()
                .createCompressorOutputStream("UNSUPPORTED", output);
    }

    @Test
    public void testCreateCompressorOutputStreamGZip() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(GZIP, output);
        assertEquals(GzipCompressorOutputStream.class, compressorOutputStream.getClass());
    }

    @Test
    public void testCreateCompressorOutputStreamPACK200() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(PACK200, output);
        assertEquals(Pack200CompressorOutputStream.class, compressorOutputStream.getClass());
    }

    @Test
    public void testCreateCompressorOutputStreamXZ() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(XZ, output);
        if (XZUtils.isXZCompressionAvailable()) {
            assertEquals(XZCompressorOutputStream.class, compressorOutputStream.getClass());
        } else {
            fail("XZ compression is not available");
        }
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStreamUnknownFormat() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        new CompressorStreamFactory()
                .createCompressorOutputStream("UNKNOWN", output);
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStreamNullOutput() throws IOException {
        new CompressorStreamFactory()
                .createCompressorOutputStream(BZIP2, null);
    }

    @Test
    public void testGetDecompressConcatenatedFalse() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertFalse(factory.getDecompressConcatenated());
    }

    @Test
    public void testGetDecompressConcatenatedTrue() {
        CompressorStreamFactory factory = new CompressorStreamFactory(true);
        assertTrue(factory.getDecompressConcatenated());
    }
}