package org.apache.commons.compress.compressors.deflate;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Inflater;

public class DeflateCompressorInputStreamTest {

    private DeflateCompressorInputStream deflateInputStream;
    private InputStream mockInputStream;

    @Before
    public void setup() {
        mockInputStream = mock(InputStream.class);
        deflateInputStream = new DeflateCompressorInputStream(mockInputStream);
    }

    @Test
    public void testRead() throws IOException {
        when(mockInputStream.read()).thenReturn(65);
        int result = deflateInputStream.read();
        assert result == 65;
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] buf = new byte[10];
        when(mockInputStream.read(buf, 0, 10)).thenReturn(5);
        int result = deflateInputStream.read(buf, 0, 10);
        assert result == 5;
    }

    @Test
    public void testSkip() throws IOException {
        when(mockInputStream.skip(5)).thenReturn(3L);
        long result = deflateInputStream.skip(5);
        assert result == 3L;
    }

    @Test
    public void testAvailable() throws IOException {
        when(mockInputStream.available()).thenReturn(10);
        int result = deflateInputStream.available();
        assert result == 10;
    }

    @Test
    public void testClose() throws IOException {
        deflateInputStream.close();
    }
}