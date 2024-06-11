package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.Pack200CompressorInputStream;
import org.apache.commons.compress.compressors.XZCompressorInputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.junit.Test;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStreamGzip() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.markSupported()).thenReturn(true);
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(GZipCompressorInputStream.class.getName(), inputStream);
        assertInstanceOf(GzipCompressorInputStream.class, compressorInputStream);
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStreamBzip2() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.markSupported()).thenReturn(true);
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(BZip2CompressorInputStream.class.getName(), inputStream);
        assertInstanceOf(BZip2CompressorInputStream.class, compressorInputStream);
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStreamXZ() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.markSupported()).thenReturn(true);
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(XZCompressorInputStream.class.getName(), inputStream);
        assertInstanceOf(XZCompressorInputStream.class, compressorInputStream);
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStreamPack200() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.markSupported()).thenReturn(true);
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(Pack200CompressorInputStream.class.getName(), inputStream);
        assertInstanceOf(Pack200CompressorInputStream.class, compressorInputStream);
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStreamUnsupported() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.markSupported()).thenReturn(true);
        OutputStream outputStream = new ByteArrayOutputStream();
        IOException exception = assertThrows(CompressorException.class, () -> new CompressorStreamFactory().createCompressorInputStream("unsupported", inputStream));
        assertEquals("Compressor: unsupported not found.", exception.getMessage());
    }

    @Test
    public void testCreateCompressorOutputStreamGzip() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        OutputStream outputStream = mock(OutputStream.class);
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(GZipCompressorOutputStream.class.getName(), outputStream);
        assertInstanceOf(GzipCompressorOutputStream.class, compressorOutputStream);
        compressorOutputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStreamBzip2() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        OutputStream outputStream = mock(OutputStream.class);
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(BZip2CompressorOutputStream.class.getName(), outputStream);
        assertInstanceOf(BZip2CompressorOutputStream.class, compressorOutputStream);
        compressorOutputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStreamXZ() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        OutputStream outputStream = mock(OutputStream.class);
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(XZCompressorOutputStream.class.getName(), outputStream);
        assertInstanceOf(XZCompressorOutputStream.class, compressorOutputStream);
        compressorOutputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStreamDeflate() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        OutputStream outputStream = mock(OutputStream.class);
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream(DeflateCompressorOutputStream.class.getName(), outputStream);
        assertInstanceOf(DeflateCompressorOutputStream.class, compressorOutputStream);
        compressorOutputStream.close();
    }

    @Test
    public void testCreateCompressorStreamsWithWrongType() {
        InputStream inputStream = mock(InputStream.class);
        OutputStream outputStream = mock(OutputStream.class);
        IOException exception = assertThrows(CompressorException.class, () -> new CompressorStreamFactory().createCompressorOutputStream("XZ", inputStream));
        assertEquals("Compressor: XZ not found.", exception.getMessage());
    }

    @Test
    public void testCreateCompressorStreamsWithNullName() {
        InputStream inputStream = mock(InputStream.class);
        OutputStream outputStream = mock(OutputStream.class);
        IOException exception = assertThrows(CompressorException.class, () -> new CompressorStreamFactory().createCompressorOutputStream(null, outputStream));
        assertEquals("Compressor name must not be null.", exception.getMessage());
    }

    @Test
    public void testCreateCompressorStreamsWithNullStream() {
        String name = "GZip";
        IOException exception = assertThrows(CompressorException.class, () -> new CompressorStreamFactory().createCompressorOutputStream(name, null));
        assertEquals("Stream must not be null.", exception.getMessage());
    }

    @Test
    public void testGetDecompressConcatenated() {
        CompressorStreamFactory factory = new CompressorStreamFactory(true);
        assertTrue(factory.getDecompressConcatenated());
        factory = new CompressorStreamFactory();
        assertFalse(factory.getDecompressConcatenated());
    }
}