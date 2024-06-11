package org.apache.commons.compress.compressors.deflate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Inflater;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class DeflateCompressorInputStreamTest {

    private DeflateCompressorInputStream deflateInputStream;
    private InputStream mockInputStream;
    private DeflateParameters mockParameters;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        mockInputStream = mock(InputStream.class);
        mockParameters = mock(DeflateParameters.class);
        deflateInputStream = new DeflateCompressorInputStream(mockInputStream, mockParameters);
    }

    @Test
    public void testConstructorWithInputStream() throws IOException {
        deflateInputStream = new DeflateCompressorInputStream(mockInputStream);
        assertNotNull(deflateInputStream);
    }

    @Test
    public void testRead() throws IOException {
        int expected = 65;
        when(mockInputStream.read()).thenReturn(expected);
        assertEquals(expected, deflateInputStream.read());
    }

    @Test
    public void testReadWithByteArray() throws IOException {
        byte[] byteArray = new byte[10];
        int expected = 5;
        when(mockInputStream.read(byteArray, 0, byteArray.length)).thenReturn(expected);
        assertEquals(expected, deflateInputStream.read(byteArray, 0, byteArray.length));
    }

    @Test
    public void testSkip() throws IOException {
        long expected = 10;
        when(mockInputStream.skip(expected)).thenReturn(expected);
        assertEquals(expected, deflateInputStream.skip(expected));
    }

    @Test
    public void testAvailable() throws IOException {
        int expected = 10;
        when(mockInputStream.available()).thenReturn(expected);
        assertEquals(expected, deflateInputStream.available());
    }

    @Test
    public void testClose() throws IOException {
        deflateInputStream.close();
    }

}