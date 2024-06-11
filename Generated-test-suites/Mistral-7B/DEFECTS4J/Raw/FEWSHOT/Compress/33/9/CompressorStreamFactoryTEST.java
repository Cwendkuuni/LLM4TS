package org.apache.commons.compress.compressors;

import static org.junit.Assert.*;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream_Gzip() throws IOException {
        InputStream input = new ByteArrayInputStream(GzipCompressorInputStream.GZIP_SIGNATURE);
        CompressorInputStream compressorInputStream = CompressorStreamFactory.newInstance().createCompressorInputStream("gz", input);
        assertEquals("gzip", compressorInputStream.getName());
        assertNotNull(compressorInputStream.getFormat());
        assertTrue(compressorInputStream instanceof GzipCompressorInputStream);
    }

    @Test
    public void testCreateCompressorInputStream_Bzip2() throws IOException {
        InputStream input = new ByteArrayInputStream(BZip2CompressorInputStream.BZIP2_SIGNATURE);
        CompressorInputStream compressorInputStream = CompressorStreamFactory.newInstance().createCompressorInputStream("bzip2", input);
        assertEquals("bzip2", compressorInputStream.getName());
        assertNotNull(compressorInputStream.getFormat());
        assertTrue(compressorInputStream instanceof BZip2CompressorInputStream);
    }

    @Test
    public void testCreateCompressorInputStream_Xz() throws IOException {
        InputStream input = new ByteArrayInputStream(XZUtils.XZ_SIGNATURE);
        CompressorInputStream compressorInputStream = CompressorStreamFactory.newInstance().createCompressorInputStream("xz", input);
        assertEquals("xz", compressorInputStream.getName());
        assertNotNull(compressorInputStream.getFormat());
        assertTrue(compressorInputStream instanceof XZCompressorInputStream);
    }

    @Test
    public void testCreateCompressorInputStream_Lzma() throws IOException {
        InputStream input = new ByteArrayInputStream(LZMAUtils.LZMA_SIGNATURE);
        CompressorInputStream compressorInputStream = CompressorStreamFactory.newInstance().createCompressorInputStream("lzma", input);
        assertEquals("lzma", compressorInputStream.getName());
        assertNotNull(compressorInputStream.getFormat());
        assertTrue(compressorInputStream instanceof LZMACompressorInputStream);
    }

    @Test
    public void testCreateCompressorInputStream_Unknown() throws IOException {
        InputStream input = new ByteArrayInputStream(" unknown".getBytes());
        try {
            CompressorInputStream compressorInputStream = CompressorStreamFactory.newInstance().createCompressorInputStream("unknown", input);
            fail("Expected CompressorException");
        } catch (CompressorException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorInputStream_NullInput() {
        try {
            CompressorInputStream compressorInputStream = CompressorStreamFactory.newInstance().createCompressorInputStream("gzip", null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorOutputStream_Gzip() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = CompressorStreamFactory.newInstance().createCompressorOutputStream("gz", output);
        assertEquals("gzip", compressorOutputStream.getName());
        assertNotNull(compressorOutputStream.getFormat());
        assertTrue(compressorOutputStream instanceof GzipCompressorOutputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_Bzip2() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = CompressorStreamFactory.newInstance().createCompressorOutputStream("bzip2", output);
        assertEquals("bzip2", compressorOutputStream.getName());
        assertNotNull(compressorOutputStream.getFormat());
        assertTrue(compressorOutputStream instanceof BZip2CompressorOutputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_Xz() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = CompressorStreamFactory.newInstance().createCompressorOutputStream("xz", output);
        assertEquals("xz", compressorOutputStream.getName());
        assertNotNull(compressorOutputStream.getFormat());
        assertTrue(compressorOutputStream instanceof XZCompressorOutputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_Lzma() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = CompressorStreamFactory.newInstance().createCompressorOutputStream("lzma", output);
        assertEquals("lzma", compressorOutputStream.getName());
        assertNotNull(compressorOutputStream.getFormat());
        assertTrue(compressorOutputStream instanceof LZMACompressorOutputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_Pack200() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = CompressorStreamFactory.newInstance().createCompressorOutputStream("pack200", output);
        assertEquals("pack200", compressorOutputStream.getName());
        assertNotNull(compressorOutputStream.getFormat());
        assertTrue(compressorOutputStream instanceof Pack200CompressorOutputStream);
    }

    @Test
    public void testCreateCompressorOutputStream_Deflate() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = CompressorStreamFactory.newInstance().createCompressorOutputStream("deflate", output);
        assertEquals("deflate", compressorOutputStream.getName());
        assertNotNull(compressorOutputStream.getFormat());
        assertTrue(compressorOutputStream instanceof DeflateCompressorOutputStream);
    }

    @Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory factory = CompressorStreamFactory.newInstance();
        assertFalse(factory.getDecompressConcatenated());

        CompressorStreamFactory factoryConcatenated = new CompressorStreamFactory(true);
        assertTrue(factoryConcatenated.getDecompressConcatenated());
    }
}