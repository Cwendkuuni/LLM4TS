package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class IOUtilsTest {

    private InputStream input;
    private OutputStream output;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        input = mock(InputStream.class);
        output = mock(OutputStream.class);
    }

    @Test
    public void testCopy() throws IOException {
        when(input.available()).thenReturn(10);
        when(input.read(org.apache.commons.compress.utils.IOUtils.EMPTY_STRING_ARRAY)).thenReturn(5);
        when(output.write(org.apache.commons.compress.utils.IOUtils.EMPTY_STRING_ARRAY)).thenReturn(5);

        long result = IOUtils.copy(input, output);
        assertEquals(10, result);
    }

    @Test
    public void testCopyWithBuffersize() throws IOException {
        when(input.available()).thenReturn(10);
        when(input.read(org.apache.commons.compress.utils.IOUtils.EMPTY_STRING_ARRAY)).thenReturn(5);
        when(output.write(org.apache.commons.compress.utils.IOUtils.EMPTY_STRING_ARRAY)).thenReturn(5);

        long result = IOUtils.copy(input, output, 3);
        assertEquals(10, result);
    }

    @Test
    public void testCopyWithIOException() throws IOException {
        when(input.available()).thenThrow(IOException.class);

        try {
            IOUtils.copy(input, output);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello World".getBytes());
        when(input.skip(10)).thenReturn(5);
        when(input.skip(5)).thenReturn(0);

        long result = IOUtils.skip(input, 15);
        assertEquals(10, result);
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello World".getBytes());
        byte[] expected = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        byte[] result = IOUtils.toByteArray(input);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testReadFullyWithIOException() throws IOException {
        InputStream input = mock(InputStream.class);
        when(input.read()).thenThrow(IOException.class);

        try {
            IOUtils.readFully(input, new byte[1]);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello World".getBytes());
        byte[] result = IOUtils.toByteArray(input);
        byte[] expected = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};

        assertArrayEquals(expected, result);
    }

    @Test
    public void testToByteArrayWithIOException() throws IOException {
        InputStream input = mock(InputStream.class);
        when(input.read()).thenThrow(IOException.class);

        try {
            IOUtils.toByteArray(input);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testCloseQuietlyNullInput() {
        IOUtils.closeQuietly(null);
    }

    @Test
    public void testCloseQuietlyInputStream() throws IOException {
        InputStream input = mock(InputStream.class);
        when(input.close()).thenThrow(IOException.class);

        IOUtils.closeQuietly(input);
    }

    @Test
    public void testCloseQuietlyOutputStream() throws IOException {
        OutputStream output = mock(OutputStream.class);
        when(output.close()).thenThrow(IOException.class);

        IOUtils.closeQuietly(output);
    }
}