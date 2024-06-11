
package org.apache.commons.compress.compressors.deflate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.InflaterInputStream;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.junit.Before;
import org.junit.Test;

public class DeflateCompressorInputStreamTest {

    private InputStream inputStream;
    private DeflateParameters parameters;
    private InflaterInputStream inflaterInputStream;
    private DeflateCompressorInputStream deflateCompressorInputStream;

    @Before
    public void setup() {
        inputStream = mock(InputStream.class);
        parameters = mock(DeflateParameters.class);
        inflaterInputStream = mock(InflaterInputStream.class);
        deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, parameters);

        // Mock behavior of the InflaterInputStream constructor with parameters
        // to return the mock object
        mock(InflaterInputStream.class).when(inputStream)
                .thenReturn(inflaterInputStream);
    }

    @Test
    public void testConstructor() throws IOException {
        deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        verify(inputStream).mark(6);
        verify(parameters).withZlibHeader();
    }

    @Test
    public void testRead() throws IOException {
        int expected = 1;
        mock(InflaterInputStream.class).when(inflaterInputStream).read();
        int actual = deflateCompressorInputStream.read();
        assertEquals(expected, actual);
        verify(inflaterInputStream).read();
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] buffer = new byte[10];
        int expected = 5;
        mock(InflaterInputStream.class).when(inflaterInputStream).read(buffer,
                0, 10).thenReturn(expected);
        int actual = deflateCompressorInputStream.read(buffer, 0, 10);
        assertEquals(expected, actual);
        verify(inflaterInputStream).read(buffer, 0, 10);
    }

    @Test
    public void testSkip() throws IOException {
        long expected = 5L;
        mock(InflaterInputStream.class).when(inflaterInputStream).skip(5L)
                .thenReturn(expected);
        long actual = deflateCompressorInputStream.skip(5L);
        assertEquals(expected, actual);
        verify(inflaterInputStream).skip(5L);
    }

    @Test
    public void testAvailable() throws IOException {
        int expected = 10;
        mock(InflaterInputStream.class).when(inflaterInputStream).available()
                .thenReturn(expected);
        int actual = deflateCompressorInputStream.available();
        assertEquals(expected, actual);
        verify(inflaterInputStream).available();
    }

    @Test
    public void testClose() throws IOException {
        deflateCompressorInputStream.close();
        verify(inflaterInputStream).close();
    }
}

