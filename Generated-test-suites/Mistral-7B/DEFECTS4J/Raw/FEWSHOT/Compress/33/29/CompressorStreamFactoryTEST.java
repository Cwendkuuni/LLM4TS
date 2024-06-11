package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test cases for CompressorStreamFactory
 */
public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStreamAutoDetect() throws IOException, CompressorException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x42, (byte) 0x5A, (byte) 0x68, (byte) 0x64, (byte) 0x67, (byte) 0x65, (byte) 0x20, (byte) 0x41, (byte) 0x53, (byte) 0x53, (byte) 0x45, (byte) 0x54, (byte) 0x20, (byte) 0x42, (byte) 0x59, (byte) 0x53, (byte) 0x44, (byte) 0x35, (byte) 0x32, (byte) 0x34, (byte) 0x31, (byte) 0x36, (byte) 0x31});
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream(inputStream);
        assertSame(GzipCompressorInputStream.class, compressorInputStream.getClass());
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorInputStreamByName() throws IOException, CompressorException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x0, 0x0, 0x1A, 0xB5, 0x0, 0x0, 0x0, 0x0});
        CompressorInputStream compressorInputStream = new CompressorStreamFactory().createCompressorInputStream("DEFLATE", inputStream);
        assertSame(DeflateCompressorInputStream.class, compressorInputStream.getClass());
        compressorInputStream.close();
    }

    @Test
    public void testCreateCompressorOutputStreamByName() throws IOException, CompressorException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CompressorOutputStream compressorOutputStream = new CompressorStreamFactory().createCompressorOutputStream("GZIP", outputStream);
        compressorOutputStream.write("Test".getBytes());
        compressorOutputStream.finish();
        compressorOutputStream.close();
        byte[] outputBytes = ((ByteArrayOutputStream) outputStream).toByteArray();
        String expectedOutput = "H4sIAAAAAAAAGwAAHwBwAAAA+QAAADxwAAAAEAEABhAQACgQAjAQACg==";
        assertArrayEquals(expectedOutput.getBytes(), outputBytes);
    }

    @Test
    public void testCreateCompressorOutputStreamByNameInvalidName() {
        OutputStream outputStream = new ByteArrayOutputStream();
        assertThrows(CompressorException.class, () -> new CompressorStreamFactory().createCompressorOutputStream("INVALID", outputStream));
    }

    @Test
    public void testCreateCompressorOutputStreamByNameUnsupported() {
        OutputStream outputStream = new ByteArrayOutputStream();
        assertThrows(UnsupportedOperationException.class, () -> new CompressorStreamFactory().createCompressorOutputStream("XZ", outputStream));
    }
}