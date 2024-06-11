package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.XZCompressorInputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.junit.Test;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream_BZIP2() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream input = new ByteArrayInputStream(new byte[10]);
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(CompressorStreamFactory.BZIP2, input);
        assertNotNull(compressorInputStream);
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStream_GZIP() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream input = new ByteArrayInputStream(new byte[10]);
        try {
            factory.createCompressorInputStream(CompressorStreamFactory.GZIP, input);
            assertFalse(true); // GZIP not supported as an input stream type
        } catch (CompressorException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorInputStream_Pack200() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream input = new ByteArrayInputStream(new byte[10]);
        try {
            factory.createCompressorInputStream(CompressorStreamFactory.PACK200, input);
            assertFalse(true); // Pack200 not supported as an input stream type
        } catch (CompressorException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorInputStream_XZ() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream input = new ByteArrayInputStream(new byte[10]);
        CompressorInputStream compressorInputStream = null;
        try {
            compressorInputStream = factory.createCompressorInputStream(CompressorStreamFactory.XZ, input);
            assertNotNull(compressorInputStream);
            compressorInputStream.close();
        } catch (CompressorException e) {
            // Expected
        } finally {
            if (compressorInputStream != null) {
                try {
                    compressorInputStream.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }

    @Test
    public void testCreateCompressorInputStream_LZMA() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream input = new ByteArrayInputStream(new byte[10]);
        try {
            factory.createCompressorInputStream(CompressorStreamFactory.LZMA, input);
            assertFalse(true); // LZMA not supported as an input stream type
        } catch (CompressorException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorInputStream_SnappyFramed() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream input = new ByteArrayInputStream(new byte[10]);
        try {
            factory.createCompressorInputStream(CompressorStreamFactory.SNAPPY_FRAMED, input);
            assertFalse(true); // SnappyFramed not supported as an input stream type
        } catch (CompressorException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorInputStream_SnappyRaw() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream input = new ByteArrayInputStream(new byte[10]);
        try {
            factory.createCompressorInputStream(CompressorStreamFactory.SNAPPY_RAW, input);
            assertFalse(true); // SnappyRaw not supported as an input stream type
        } catch (CompressorException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorInputStream_Z() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream input = new ByteArrayInputStream(new byte[10]);
        try {
            factory.createCompressorInputStream(CompressorStreamFactory.Z, input);
            assertFalse(true); // Z not supported as an input stream type
        } catch (CompressorException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorInputStream_Deflate() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        InputStream input = new ByteArrayInputStream(new byte[10]);
        CompressorInputStream compressorInputStream = factory.createCompressorInputStream(CompressorStreamFactory.DEFLATE, input);
        assertNotNull(compressorInputStream);
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStream_BZIP2() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream output = new ByteArrayOutputStream();
        try {
            factory.createCompressorOutputStream(CompressorStreamFactory.BZIP2, output);
            assertFalse(true); // BZIP2 not supported as an output stream type
        } catch (CompressorException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorOutputStream_GZIP() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.GZIP, output);
        assertNotNull(compressorOutputStream);
        compressorOutputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStream_Pack200() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream output = new ByteArrayOutputStream();
        try {
            factory.createCompressorOutputStream(CompressorStreamFactory.PACK200, output);
            assertFalse(true); // Pack200 not supported as an output stream type
        } catch (CompressorException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorOutputStream_XZ() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream output = new ByteArrayOutputStream();
        try {
            factory.createCompressorOutputStream(CompressorStreamFactory.XZ, output);
            assertFalse(true); // XZ not supported as an output stream type
        } catch (CompressorException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorOutputStream_LZMA() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream output = new ByteArrayOutputStream();
        try {
            factory.createCompressorOutputStream(CompressorStreamFactory.LZMA, output);
            assertFalse(true); // LZMA not supported as an output stream type
        } catch (CompressorException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorOutputStream_SnappyFramed() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream output = new ByteArrayOutputStream();
        try {
            factory.createCompressorOutputStream(CompressorStreamFactory.SNAPPY_FRAMED, output);
            assertFalse(true); // SnappyFramed not supported as an output stream type
        } catch (CompressorException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorOutputStream_SnappyRaw() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream output = new ByteArrayOutputStream();
        try {
            factory.createCompressorOutputStream(CompressorStreamFactory.SNAPPY_RAW, output);
            assertFalse(true); // SnappyRaw not supported as an output stream type
        } catch (CompressorException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorOutputStream_Z() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream output = new ByteArrayOutputStream();
        try {
            factory.createCompressorOutputStream(CompressorStreamFactory.Z, output);
            assertFalse(true); // Z not supported as an output stream type
        } catch (CompressorException e) {
            // Expected
        }
    }

    @Test
    public void testCreateCompressorOutputStream_Deflate() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        OutputStream output = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = factory.createCompressorOutputStream(CompressorStreamFactory.DEFLATE, output);
        assertNotNull(compressorOutputStream);
        compressorOutputStream.close();
    }

    @Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertNull(factory.getDecompressConcatenated());
    }

    @Test
    public void testGetDecompressConcatenated_true() {
        CompressorStreamFactory factory = new CompressorStreamFactory(true);
        assertTrue(factory.getDecompressConcatenated());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetDecompressConcatenated_false() {
        CompressorStreamFactory factory = new CompressorStreamFactory(true);
        factory.setDecompressConcatenated(false);
    }
}