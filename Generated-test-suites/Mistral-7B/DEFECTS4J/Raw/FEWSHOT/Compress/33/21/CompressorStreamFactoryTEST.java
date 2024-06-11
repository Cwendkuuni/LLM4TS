package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertInstanceOf;
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
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream_Gzip() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x1f, (byte) 0x8b, (byte) 0x08, 0});
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream("gzip", input);
        assertInstanceOf(GzipCompressorInputStream.class, compressorInputStream);
    }

    @Test
    public void testCreateCompressorInputStream_Bzip2() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[]{0x42, 0x5A, 0x68, 0, (byte) 0x43, 0x61, 0x72, 0x61, 0x63, 0x68, 0x65, 0x72, 0x73, 0x69, 0x73, 0x74});
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream("bzip2", input);
        assertInstanceOf(BZip2CompressorInputStream.class, compressorInputStream);
    }

    @Test
    public void testCreateCompressorInputStream_Xz() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[]{0x78, 0x58, 0x4D, 0x4C, 0x65, 0x78, 0x54, 0x68, 0x65, 0x78, 0x2E, 0x63, 0x61, 0x72, 0x73, 0x73, 0x61, 0x73, 0x74, 0x65, 0x72, 0x31, 0x32, 0x31, 0x33, 0x32, 0x31});
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream("xz", input);
        assertInstanceOf(XZCompressorInputStream.class, compressorInputStream);
    }

    @Test
    public void testCreateCompressorInputStream_InvalidName() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        try {
            new CompressorStreamFactory().createCompressorInputStream("invalid", input);
            assert false;
        } catch (CompressorException e) {
            assertNull(e.getCause());
            assertEquals("Compressor: invalid not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateCompressorInputStream_InvalidStream() throws Exception {
        InputStream input = null;
        try {
            new CompressorStreamFactory().createCompressorInputStream("gzip", input);
            assert false;
        } catch (IllegalArgumentException e) {
            assertNull(e.getCause());
            assertEquals("Stream must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateCompressorInputStream_NullInput() throws Exception {
        InputStream input = null;
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream("gzip", input);
        assertNull(compressorInputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_Gzip() throws Exception {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream("gzip", output);
        assertInstanceOf(GzipCompressorOutputStream.class, compressorOutputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_Bzip2() throws Exception {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream("bzip2", output);
        assertInstanceOf(BZip2CompressorOutputStream.class, compressorOutputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_Xz() throws Exception {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream("xz", output);
        assertInstanceOf(XZCompressorOutputStream.class, compressorOutputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_InvalidName() throws Exception {
        OutputStream output = new ByteArrayOutputStream();
        try {
            new CompressorStreamFactory().createCompressorOutputStream("invalid", output);
            assert false;
        } catch (CompressorException e) {
            assertNull(e.getCause());
            assertEquals("Compressor: invalid not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateCompressorOutputStream_InvalidStream() throws Exception {
        OutputStream output = null;
        try {
            new CompressorStreamFactory().createCompressorOutputStream("gzip", output);
            assert false;
        } catch (IllegalArgumentException e) {
            assertNull(e.getCause());
            assertEquals("Stream must not be null.", e.getMessage());
        }
    }

    @Test
    public void testCreateCompressorOutputStream_NullOutput() throws Exception {
        OutputStream output = null;
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream("gzip", output);
        assertNull(compressorOutputStream);
    }

    @Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertFalse(factory.getDecompressConcatenated());

        CompressorStreamFactory factory2 = new CompressorStreamFactory(true);
        assertTrue(factory2.getDecompressConcatenated());
    }
}