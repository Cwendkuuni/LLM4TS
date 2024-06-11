package org.apache.commons.compress.compressors.deflate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.junit.Test;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[10];
        when(inputStream.read(buffer)).thenReturn(5);

        int read = compressorInputStream.read();
        int expected = buffer[0] & 0xFF;
        assertEquals(expected, read);

        read = compressorInputStream.read();
        expected = buffer[1] & 0xFF;
        assertEquals(expected, read);

        read = compressorInputStream.read();
        expected = buffer[2] & 0xFF;
        assertEquals(expected, read);

        read = compressorInputStream.read();
        assertEquals(-1, read);
    }

    @Test
    public void testReadWithByteArray() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] expectedData = new byte[] {1, 2, 3, 4};
        byte[] buffer = new byte[10];
        when(inputStream.read(buffer)).thenReturn(expectedData.length);
        when(inputStream.read(buffer)).thenReturn(expectedData);

        int len = compressorInputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(expectedData, buffer, 0, expectedData.length);

        len = compressorInputStream.read(buffer, 5, 5);
        assertEquals(-1, len);
    }

    @Test
    public void testSkip() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        long expectedSkipBytes = 10;

        long skipped = compressorInputStream.skip(expectedSkipBytes);
        assertEquals(expectedSkipBytes, skipped);
    }

    @Test
    public void testAvailable() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[10];
        when(inputStream.available()).thenReturn(5);

        int available = compressorInputStream.available();
        assertEquals(5, available);

        when(inputStream.available()).thenReturn(10);
        available = compressorInputStream.available();
        assertEquals(10, available);
    }

    @Test
    public void testClose() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);

        compressorInputStream.close();

        verify(inputStream).close();
    }

    @Test
    public void testCreateWithInputStream() throws IOException {
        InputStream inputStream = mock(InputStream.class);

        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);

        assertNotNull(compressorInputStream);
    }

    @Test
    public void testCreateWithInputStreamAndDeflateParameters() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        DeflateParameters deflateParameters = new DeflateParameters();

        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream, deflateParameters);

        assertNotNull(compressorInputStream);
    }
}