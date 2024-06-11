package org.apache.commons.compress.compressors;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorFactory;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.utils.IOUtils;

public class CompressorStreamFactoryTest {

    @Test
    public void testCreateCompressorInputStream_Gzip() throws IOException {
        final InputStream input = mock(InputStream.class);
        when(input.read()).thenReturn(1);
        final CompressorInputStream output = new CompressorStreamFactory()
                .createCompressorInputStream("gzip", input);

        final byte[] expectedFirstByte = new byte[]{(byte) 0x1F, (byte) 0x8B, (byte) 0x08};
        assertEquals(expectedFirstByte[0], output.read());
        assertEquals(expectedFirstByte[1], output.read());
        assertEquals(expectedFirstByte[2], output.read());

        assertSame(input, output.getSourceStream());
    }

    @Test
    public void testCreateCompressorInputStream_Bzip2() throws IOException {
        final InputStream input = mock(InputStream.class);
        when(input.read()).thenReturn(1);
        final CompressorInputStream output = new CompressorStreamFactory()
                .createCompressorInputStream("bzip2", input);

        final byte[] expectedFirstFourBytes = new byte[]{
                (byte) 0x42, (byte) 0x5A, (byte) 0x68, (byte) 0x64,
                (byte) 0x67, (byte) 0x65, (byte) 0x20, (byte) 0x42,
                (byte) 0x5A, (byte) 0x68, (byte) 0x64, (byte) 0x67,
                (byte) 0x65, (byte) 0x20, (byte) 0x43, (byte) 0x75,
                (byte) 0x74, (byte) 0x65, (byte) 0x72, (byte) 0x6E,
                (byte) 0x6F, (byte) 0x74, (byte) 0x68, (byte) 0x65,
                (byte) 0x61, (byte) 0x74, (byte) 0x65, (byte) 0x31,
                (byte) 0x31, (byte) 0x31, (byte) 0x31, (byte) 0x31};
        byte[] actualBytes = new byte[expectedFirstFourBytes.length];
        for (int i = 0; i < expectedFirstFourBytes.length; i++) {
            actualBytes[i] = output.read();
        }
        assertArrayEquals(expectedFirstFourBytes, actualBytes);

        assertSame(input, output.getSourceStream());
    }

    @Test
    public void testCreateCompressorInputStream_Xz() throws IOException {
        final InputStream input = mock(InputStream.class);
        when(input.read()).thenReturn(1);
        final CompressorInputStream output = new CompressorStreamFactory()
                .createCompressorInputStream("xz", input);

        final byte[] expectedFirstFiveBytes = new byte[]{
                (byte) 0xDD, (byte) 0x00, (byte) 0x1A, (byte) 0xBD, (byte) 0x2B};
        assertEquals(expectedFirstFiveBytes[0], output.read());
        assertEquals(expectedFirstFiveBytes[1], output.read());
        assertEquals(expectedFirstFiveBytes[2], output.read());
        assertEquals(expectedFirstFiveBytes[3], output.read());
        assertEquals(expectedFirstFiveBytes[4], output.read());

        assertSame(input, output.getSourceStream());
    }

    @Test
    public void testCreateCompressorInputStream_UnsupportedFormat()
            throws IOException {
        final InputStream input = mock(InputStream.class);
        final CompressorInputStream output = new CompressorStreamFactory()
                .createCompressorInputStream("foo", input);

        assertNull(output);
    }

    @Test
    public void testCreateCompressorOutputStream_Gzip() throws IOException {
        final OutputStream output = mock(OutputStream.class);
        final CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream("gzip", output);

        assertSame(output, compressorOutputStream.getStream());
    }

    @Test
    public void testCreateCompressorOutputStream_Bzip2() throws IOException {
        final OutputStream output = mock(OutputStream.class);
        final CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream("bzip2", output);

        assertSame(output, compressorOutputStream.getStream());
    }

    @Test
    public void testCreateCompressorOutputStream_Xz() throws IOException {
        final OutputStream output = mock(OutputStream.class);
        final CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream("xz", output);

        assertSame(output, compressorOutputStream.getStream());
    }

    @Test
    public void testCreateCompressorOutputStream_UnsupportedFormat()
            throws IOException {
        final OutputStream output = mock(OutputStream.class);
        final CompressorOutputStream compressorOutputStream = new CompressorStreamFactory()
                .createCompressorOutputStream("foo", output);

        assertNull(compressorOutputStream);
    }

    @Test
    public void testGetDecompressConcatenated() {
        final CompressorStreamFactory factory = new CompressorStreamFactory();
        assertFalse(factory.getDecompressConcatenated());

        final CompressorStreamFactory factoryWithConcatenated = new CompressorStreamFactory(true);
        assertTrue(factoryWithConcatenated.getDecompressConcatenated());
    }
}