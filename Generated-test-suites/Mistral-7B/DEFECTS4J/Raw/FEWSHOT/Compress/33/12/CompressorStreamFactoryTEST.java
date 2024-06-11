package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.*;
import org.junit.Test;

public class CompressorStreamFactoryTest {

    private CompressorStreamFactory factory;

    @Test
    public void testCreateCompressorInputStream_GZIP() throws Exception {
        InputStream testStream = mock(InputStream.class);
        when(testStream.markSupported()).thenReturn(true);
        GzipCompressorInputStream gzipInputStream = new GzipCompressorInputStream(testStream, true);
        InputStream result = factory.createCompressorInputStream("GZIP", testStream);
        assertNotNull(result);
        assert(result instanceof GzipCompressorInputStream);
        assertEquals(gzipInputStream, result);
    }

    @Test
    public void testCreateCompressorInputStream_BZIP2() throws Exception {
        InputStream testStream = mock(InputStream.class);
        when(testStream.markSupported()).thenReturn(true);
        BZip2CompressorInputStream bzip2InputStream = new BZip2CompressorInputStream(testStream, true);
        InputStream result = factory.createCompressorInputStream("BZIP2", testStream);
        assertNotNull(result);
        assert(result instanceof BZip2CompressorInputStream);
        assertEquals(bzip2InputStream, result);
    }

    @Test
    public void testCreateCompressorInputStream_XZ() throws Exception {
        InputStream testStream = mock(InputStream.class);
        when(testStream.markSupported()).thenReturn(true);
        XZCompressorInputStream xzInputStream = new XZCompressorInputStream(testStream, true);
        InputStream result = factory.createCompressorInputStream("XZ", testStream);
        assertNotNull(result);
        assert(result instanceof XZCompressorInputStream);
        assertEquals(xzInputStream, result);
    }

    @Test
    public void testCreateCompressorInputStream_LZMA() throws Exception {
        InputStream testStream = mock(InputStream.class);
        when(testStream.markSupported()).thenReturn(true);
        LZMACompressorInputStream lzmaInputStream = new LZMACompressorInputStream(testStream, null);
        InputStream result = factory.createCompressorInputStream("LZMA", testStream);
        assertNotNull(result);
        assert(result instanceof LZMACompressorInputStream);
        assertEquals(lzmaInputStream, result);
    }

    @Test
    public void testCreateCompressorInputStream_PACK200() throws Exception {
        InputStream testStream = mock(InputStream.class);
        when(testStream.markSupported()).thenReturn(true);
        Pack200CompressorInputStream pack200InputStream = new Pack200CompressorInputStream(testStream);
        InputStream result = factory.createCompressorInputStream("PACK200", testStream);
        assertNotNull(result);
        assert(result instanceof Pack200CompressorInputStream);
        assertEquals(pack200InputStream, result);
    }

    @Test
    public void testCreateCompressorInputStream_Unknown() throws Exception {
        InputStream testStream = mock(InputStream.class);
        when(testStream.markSupported()).thenReturn(true);
        InputStream result = factory.createCompressorInputStream("UNKNOWN", testStream);
        assertNull(result);
    }

    @Test
    public void testCreateCompressorOutputStream_GZIP() throws Exception {
        OutputStream testStream = mock(OutputStream.class);
        when(testStream.markSupported()).thenReturn(true);
        GzipCompressorOutputStream gzipOutputStream = new GzipCompressorOutputStream(testStream);
        OutputStream result = factory.createCompressorOutputStream("GZIP", testStream);
        assertNotNull(result);
        assert(result instanceof GzipCompressorOutputStream);
        assertEquals(gzipOutputStream, result);
    }

    @Test
    public void testCreateCompressorOutputStream_BZIP2() throws Exception {
        OutputStream testStream = mock(OutputStream.class);
        when(testStream.markSupported()).thenReturn(true);
        BZip2CompressorOutputStream bzip2OutputStream = new BZip2CompressorOutputStream(testStream);
        OutputStream result = factory.createCompressorOutputStream("BZIP2", testStream);
        assertNotNull(result);
        assert(result instanceof BZip2CompressorOutputStream);
        assertEquals(bzip2OutputStream, result);
    }

    @Test
    public void testCreateCompressorOutputStream_XZ() throws Exception {
        OutputStream testStream = mock(OutputStream.class);
        when(testStream.markSupported()).thenReturn(true);
        XZCompressorOutputStream xzOutputStream = new XZCompressorOutputStream(testStream);
        OutputStream result = factory.createCompressorOutputStream("XZ", testStream);
        assertNotNull(result);
        assert(result instanceof XZCompressorOutputStream);
        assertEquals(xzOutputStream, result);
    }

    @Test
    public void testCreateCompressorOutputStream_PACK200() throws Exception {
        OutputStream testStream = mock(OutputStream.class);
        when(testStream.markSupported()).thenReturn(true);
        Pack200CompressorOutputStream pack200OutputStream = new Pack200CompressorOutputStream(testStream);
        OutputStream result = factory.createCompressorOutputStream("PACK200", testStream);
        assertNotNull(result);
        assert(result instanceof Pack200CompressorOutputStream);
        assertEquals(pack200OutputStream, result);
    }

    @Test
    public void testCreateCompressorOutputStream_DEFLATE() throws Exception {
        OutputStream testStream = mock(OutputStream.class);
        when(testStream.markSupported()).thenReturn(true);
        DeflateCompressorOutputStream deflateOutputStream = new DeflateCompressorOutputStream(testStream, 9);
        OutputStream result = factory.createCompressorOutputStream("DEFLATE", testStream);
        assertNotNull(result);
        assert(result instanceof DeflateCompressorOutputStream);
        assertEquals(deflateOutputStream, result);
    }

    @Test
    public void testCreateCompressorOutputStream_Unknown() throws Exception {
        OutputStream testStream = mock(OutputStream.class);
        OutputStream result = factory.createCompressorOutputStream("UNKNOWN", testStream);
        assertNull(result);
    }

    @Test
    public void testGetDecompressConcatenated() {
        factory = new CompressorStreamFactory(true);
        assertTrue(factory.getDecompressConcatenated());
        
        factory = new CompressorStreamFactory();
        assertFalse(factory.getDecompressConcatenated());
    }
}