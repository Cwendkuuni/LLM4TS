package org.apache.commons.compress.compressors.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorFactory;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Test for CompressorStreamFactory
 */
public class CompressorStreamFactoryTest {

    /**
     * Test method for {@link CompressorStreamFactory#CompressorStreamFactory()}
     */
    @org.junit.Test
    public void testCompressorStreamFactory() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertNotNull(factory);
    }

    /**
     * Test method for {@link CompressorStreamFactory#CompressorStreamFactory(boolean)}
     */
    @org.junit.Test
    public void testCompressorStreamFactoryBoolean() {
        CompressorStreamFactory factory = new CompressorStreamFactory(true);
        assertTrue(factory.getDecompressConcatenated());

        factory = new CompressorStreamFactory(false);
        assertFalse(factory.getDecompressConcatenated());
    }

    /**
     * Test method for {@link CompressorStreamFactory#createCompressorInputStream(java.lang.String, java.io.InputStream)}
     */
    @org.junit.Test
    public void testCreateCompressorInputStreamStringInputStream() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        CompressorInputStream compressorInputStream = null;

        compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream("bzip2", input);
        assertNotNull(compressorInputStream);
        assertEquals(BZip2CompressorInputStream.class, compressorInputStream.getClass());

        compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream("gzip", input);
        assertNull(compressorInputStream);

        compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream("unknown", input);
        assertNull(compressorInputStream);

        compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream("deflate", input);
        assertNotNull(compressorInputStream);
        assertEquals(DeflateCompressorInputStream.class, compressorInputStream.getClass());

        compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(null, input);
        assertNull(compressorInputStream);

        compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(
                "", input); // empty string
        assertNull(compressorInputStream);
    }

    /**
     * Test method for {@link CompressorStreamFactory#createCompressorInputStream(java.io.InputStream)}
     */
    @org.junit.Test
    public void testCreateCompressorInputStreamInputStream() throws IOException {
        String name = "";
        InputStream input = new ByteArrayInputStream(new byte[0]);
        CompressorInputStream compressorInputStream = null;

        compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(input);
        assertNull(compressorInputStream);

        input = new ByteArrayInputStream(new byte[BZip2CompressorInputStream.SIGNATURE_LENGTH]);
        IOUtils.write(BZip2CompressorInputStream.SIGNATURE, input, 0,
                BZip2CompressorInputStream.SIGNATURE_LENGTH);
        compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(input);
        assertNotNull(compressorInputStream);
        assertEquals(BZip2CompressorInputStream.class, compressorInputStream.getClass());

        input.reset();
        name = "gzip";
        compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(input);
        assertNull(compressorInputStream);

        name = "unknown";
        compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(input);
        assertNull(compressorInputStream);

        name = "deflate";
        compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(input);
        assertNotNull(compressorInputStream);
        assertEquals(DeflateCompressorInputStream.class, compressorInputStream.getClass());

        name = "";
        compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(input);
        assertNull(compressorInputStream);

        name = null;
        compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(input);
        assertNull(compressorInputStream);
    }

    /**
     * Test method for {@link CompressorStreamFactory#createCompressorOutputStream(java.lang.String, java.io.OutputStream)}
     */
    @org.junit.Test
    public void testCreateCompressorOutputStreamStringOutputStream() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = null;

        compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream("bzip2", output);
        assertNotNull(compressorOutputStream);
        assertEquals(BZip2CompressorOutputStream.class, compressorOutputStream.getClass());

        compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream("gzip", output);
        assertNull(compressorOutputStream);

        compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream("unknown", output);
        assertNull(compressorOutputStream);

        compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream("deflate", output);
        assertNotNull(compressorOutputStream);
        assertEquals(DeflateCompressorOutputStream.class, compressorOutputStream.getClass());

        compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(null, output);
        assertNull(compressorOutputStream);

        compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream("", output); // empty string
        assertNull(compressorOutputStream);
    }

    /**
     * Test method for {@link CompressorStreamFactory#createCompressorOutputStream(java.io.OutputStream)}
     */
    @org.junit.Test
    public void testCreateCompressorOutputStreamOutputStream() throws IOException {
        String name = "";
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = null;

        compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(output);
        assertNull(compressorOutputStream);

        compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(output, BZip2CompressorOutputStream.DEFAULT_BUFFER_SIZE);
        assertNotNull(compressorOutputStream);
        assertEquals(BZip2CompressorOutputStream.class, compressorOutputStream.getClass());

        name = "gzip";
        compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(output);
        assertNull(compressorOutputStream);

        name = "unknown";
        compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(output);
        assertNull(compressorOutputStream);

        name = "deflate";
        compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(output);
        assertNotNull(compressorOutputStream);
        assertEquals(DeflateCompressorOutputStream.class, compressorOutputStream.getClass());

        name = "";
        compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(output);
        assertNull(compressorOutputStream);

        name = null;
        compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(output);
        assertNull(compressorOutputStream);
    }

    /**
     * Test method for {@link CompressorStreamFactory#createCompressorOutputStream(java.lang.String, java.io.FileOutputStream)}
     */
    @org.junit.Test
    public void testCreateCompressorOutputStreamStringFileOutputStream() throws IOException {
        String name = "";
        OutputStream output = new FileOutputStream("testfile.bz2");
        CompressorOutputStream compressorOutputStream = null;

        compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(name, output);
        assertNull(compressorOutputStream);

        name = "bzip2";
        compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(name, output);
        assertNotNull(compressorOutputStream);
        assertEquals(BZip2CompressorOutputStream.class, compressorOutputStream.getClass());

        name = "gzip";
        compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(name, output);
        assertNull(compressorOutputStream);

        name = "unknown";
        compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(name, output);
        assertNull(compressorOutputStream);

        name = "deflate";
        compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(name, output);
        assertNotNull(compressorOutputStream);
        assertEquals(DeflateCompressorOutputStream.class, compressorOutputStream.getClass());

        name = "";
        compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(name, output);
        assertNull(compressorOutputStream);

        name = null;
        compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(name, output);
        assertNull(compressorOutputStream);

        // cleanup
        output.close();
        IOUtils.deleteFile("testfile.bz2");
    }

    /**
     * Test method for {@link CompressorStreamFactory#createCompressorOutputStream(java.io.FileOutputStream)}
     */
    @org.junit.Test
    public void testCreateCompressorOutputStreamFileOutputStream() throws IOException {
        OutputStream output = new FileOutputStream("testfile.bz2");
        CompressorOutputStream compressorOutputStream = null;

        compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(output);
        assertNull(compressorOutputStream);

        compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(output, BZip2CompressorOutputStream.DEFAULT_BUFFER_SIZE);
        assertNotNull(compressorOutputStream);
        assertEquals(BZip2CompressorOutputStream.class, compressorOutputStream.getClass());

        compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(output, DeflateCompressorOutputStream.DEFAULT_BUFFER_SIZE);
        assertNotNull(compressorOutputStream);
        assertEquals(DeflateCompressorOutputStream.class, compressorOutputStream.getClass());

        compressorOutputStream.close();
        output.close();
        IOUtils.deleteFile("testfile.bz2");
    }

    /**
     * Test method for {@link CompressorStreamFactory#createCompressorOutputStream(java.lang.String)}
     */
    @org.junit.Test
    public void testCreateCompressorOutputStreamString() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = null;

        compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream("bzip2");
        assertNull(compressorOutputStream);

        compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream("bzip2", output);
        assertNotNull(compressorOutputStream);
        assertEquals(BZip2CompressorOutputStream.class, compressorOutputStream.getClass());

        compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream("gzip", output);
        assertNull(compressorOutputStream);

        compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream("unknown", output);
        assertNull(compressorOutputStream);

        compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream("deflate", output);
        assertNotNull(compressorOutputStream);
        assertEquals(DeflateCompressorOutputStream.class, compressorOutputStream.getClass());
    }

    /**
     * Test method for {@link CompressorStreamFactory#createCompressorOutputStream()}
     */
    @org.junit.Test
    public void testCreateCompressorOutputStream() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = null;

        compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream();
        assertNull(compressorOutputStream);

        compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(BZip2CompressorOutputStream.DEFAULT_BUFFER_SIZE, output);
        assertNotNull(compressorOutputStream);
        assertEquals(BZip2CompressorOutputStream.class, compressorOutputStream.getClass());

        compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream(DeflateCompressorOutputStream.DEFAULT_BUFFER_SIZE, output);
        assertNotNull(compressorOutputStream);
        assertEquals(DeflateCompressorOutputStream.class, compressorOutputStream.getClass());
    }
}