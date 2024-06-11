package org.apache.commons.compress.compressors.deflate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Inflater;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.junit.Test;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testDeflateCompressorInputStream() {
        InputStream inputStream = mock(InputStream.class);
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        assertEquals(inputStream, compressorInputStream.getDelegateStream());
    }

    @Test
    public void testRead() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        Inflater inflater = mock(Inflater.class);
        InflaterInputStream inflaterInputStream = mock(InflaterInputStream.class);
        when(inflater.create()).thenReturn(inflater);
        when(inflaterInputStream.getInputStream()).thenReturn(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inflaterInputStream);

        byte[] data = new byte[1];
        when(inputStream.read(data)).thenReturn(1);

        int read = compressorInputStream.read();
        assertEquals(data[0], read);

        byte[] buffer = new byte[1024];
        int readBytes = compressorInputStream.read(buffer);
        assertEquals(-1, readBytes);

        compressorInputStream.close();
    }

    @Test
    public void testReadWithZlibHeader() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{-31, 139, 8, 0, 126});
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());

        byte[] data = new byte[1];
        int read = compressorInputStream.read();
        assertEquals(data[0], read);

        byte[] buffer = new byte[1024];
        int readBytes = compressorInputStream.read(buffer);
        assertEquals(-1, readBytes);

        compressorInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testReadWithNull() throws IOException {
        InputStream inputStream = null;
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        compressorInputStream.read();
    }

    @Test
    public void testSkip() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);

        long skipped = compressorInputStream.skip(1L);
        assertEquals(1L, skipped);

        skipped = compressorInputStream.skip(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, skipped);
    }

    @Test
    public void testAvailable() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);

        int available = compressorInputStream.available();
        assertEquals(0, available);

        byte[] buffer = new byte[1024];
        int readBytes = inputStream.read(buffer);
        when(inputStream.available()).thenReturn(readBytes);

        available = compressorInputStream.available();
        assertEquals(readBytes, available);
    }

    @Test
    public void testClose() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);

        compressorInputStream.close();
        verify(inputStream).close();
    }
}