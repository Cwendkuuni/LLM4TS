package org.apache.commons.compress.compressors.deflate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Inflater;

import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.junit.Before;
import org.junit.Test;

public class DeflateCompressorInputStreamTest {

    private InputStream mockInputStream;
    private DeflateCompressorInputStream deflateInputStream;

    @Before
    public void setUp() {
        mockInputStream = mock(InputStream.class);
        deflateInputStream = new DeflateCompressorInputStream(mockInputStream);
    }

    @Test
    public void testRead() throws IOException {
        when(mockInputStream.read()).thenReturn(1);
        assertEquals(1, deflateInputStream.read());
        verify(mockInputStream, times(1)).read();

        when(mockInputStream.read()).thenReturn(-1);
        assertEquals(-1, deflateInputStream.read());
        verify(mockInputStream, times(2)).read();

        verifyNoMoreInteractions(mockInputStream);
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] buffer = new byte[10];

        when(mockInputStream.read(buffer, 0, 10)).thenReturn(5);
        assertEquals(5, deflateInputStream.read(buffer, 0, 10));
        verify(mockInputStream, times(1)).read(buffer, 0, 10);

        when(mockInputStream.read(buffer, 0, 10)).thenReturn(-1);
        assertEquals(-1, deflateInputStream.read(buffer, 0, 10));
        verify(mockInputStream, times(2)).read(buffer, 0, 10);

        verifyNoMoreInteractions(mockInputStream);
    }

    @Test
    public void testSkip() throws IOException {
        long n = 10;

        when(mockInputStream.skip(n)).thenReturn(5L);
        assertEquals(5L, deflateInputStream.skip(n));
        verify(mockInputStream, times(1)).skip(n);

        verifyNoMoreInteractions(mockInputStream);
    }

    @Test
    public void testAvailable() throws IOException {
        int available = 10;

        when(mockInputStream.available()).thenReturn(available);
        assertEquals(available, deflateInputStream.available());
        verify(mockInputStream, times(1)).available();

        verifyNoMoreInteractions(mockInputStream);
    }

    @Test
    public void testClose() throws IOException {
        deflateInputStream.close();
        verify(mockInputStream, times(1)).close();

        verifyNoMoreInteractions(mockInputStream);
    }
}