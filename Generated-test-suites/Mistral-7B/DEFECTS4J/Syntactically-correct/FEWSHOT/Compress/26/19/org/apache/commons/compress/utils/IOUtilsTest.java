package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        InputStream input = mock(InputStream.class);
        OutputStream output = mock(OutputStream.class);
        when(input.read()).thenReturn('A').thenReturn('B').thenReturn('C').thenReturn(-1);

        long result = IOUtils.copy(input, output);
        assertEquals(3, result);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream input = mock(InputStream.class);
        OutputStream output = mock(OutputStream.class);
        when(input.read()).thenReturn('A').thenReturn('B').thenReturn('C').thenReturn(-1);

        long result = IOUtils.copy(input, output, 3);
        assertEquals(1, result);
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = mock(InputStream.class);
        when(input.skip(3)).thenReturn(3);
        when(input.skip(2)).thenReturn(2);
        long result = IOUtils.skip(input, 8);
        assertEquals(5, result);
    }

    @Test
    public void testSkipLessBytesThanRequested() throws IOException {
        InputStream input = mock(InputStream.class);
        when(input.skip(3)).thenReturn(3);
        when(input.skip(2)).thenThrow(new EOFException());
        long result = IOUtils.skip(input, 8);
        assertEquals(3, result);
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = mock(InputStream.class);
        byte[] expected = new byte[]{'A', 'B', 'C', 'D'};
        when(input.read(expected)).thenReturn(4);

        int result = IOUtils.readFully(input, expected);
        assertArrayEquals(expected, expected);
        assertEquals(4, result);
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("abcdefg".getBytes());
        byte[] expected = new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g'};
        byte[] result = IOUtils.toByteArray(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testCloseQuietlyNullCloseable() {
        IOUtils.closeQuietly(null);
    }

    @Test
    public void testCloseQuietlyIOException() {
        Closeable closeable = mock(Closeable.class);
        when(closeable.close()).thenThrow(new IOException());

        IOUtils.closeQuietly(closeable);
    }

    @Test
    public void testCloseQuietlyExpectedIOException() {
        Closeable closeable = mock(Closeable.class);
        when(closeable.close()).thenThrow(new IOException());

        assertThrows(IOException.class, () -> IOUtils.closeQuietly(closeable));
    }
}