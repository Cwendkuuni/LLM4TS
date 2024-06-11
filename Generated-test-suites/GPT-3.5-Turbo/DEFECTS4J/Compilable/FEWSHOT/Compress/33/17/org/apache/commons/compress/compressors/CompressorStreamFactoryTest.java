package org.apache.commons.compress.compressors;

import org.junit.Test;

import static org.junit.Assert.*;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream() throws Exception {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertNotNull(factory.createCompressorInputStream("bzip2", null));
        assertNotNull(factory.createCompressorInputStream("gz", null));
        assertNotNull(factory.createCompressorInputStream("pack200", null));
        assertNotNull(factory.createCompressorInputStream("xz", null));
        assertNotNull(factory.createCompressorInputStream("lzma", null));
        assertNotNull(factory.createCompressorInputStream("snappy-framed", null));
        assertNotNull(factory.createCompressorInputStream("snappy-raw", null));
        assertNotNull(factory.createCompressorInputStream("z", null));
        assertNotNull(factory.createCompressorInputStream("deflate", null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorInputStreamWithNullArguments() throws Exception {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.createCompressorInputStream(null, null);
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorInputStreamWithInvalidCompressorName() throws Exception {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.createCompressorInputStream("invalidCompressor", null);
    }

    @Test
    public void testCreateCompressorOutputStream() throws Exception {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        assertNotNull(factory.createCompressorOutputStream("gz", null));
        assertNotNull(factory.createCompressorOutputStream("bzip2", null));
        assertNotNull(factory.createCompressorOutputStream("xz", null));
        assertNotNull(factory.createCompressorOutputStream("pack200", null));
        assertNotNull(factory.createCompressorOutputStream("deflate", null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCompressorOutputStreamWithNullArguments() throws Exception {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.createCompressorOutputStream(null, null);
    }

    @Test(expected = CompressorException.class)
    public void testCreateCompressorOutputStreamWithInvalidCompressorName() throws Exception {
        CompressorStreamFactory factory = new CompressorStreamFactory();
        factory.createCompressorOutputStream("invalidCompressor", null);
    }
}