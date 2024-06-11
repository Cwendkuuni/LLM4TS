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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        InputStream input = mock(InputStream.class);
        OutputStream output = mock(OutputStream.class);
        when(input.read()).thenReturn(42);
        when(output.write(new byte[] { 0, 1, 2, 3 })).thenReturn(4);

        long actual = IOUtils.copy(input, output);
        assertEquals(9L, actual);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream input = mock(InputStream.class);
        OutputStream output = mock(OutputStream.class);
        when(input.read(new byte[1024])).thenReturn(1024);
        when(output.write(new byte[1024])).thenReturn(1024);

        long actual = IOUtils.copy(input, output, 1024);
        assertEquals(1024L, actual);
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = mock(InputStream.class);
        when(input.skip(10)).thenReturn(5);
        when(input.skip(5)).thenReturn(0);

        long actual = IOUtils.skip(input, 15);
        assertEquals(10L, actual);
    }

    @Test
    public void testSkipFallsBackToRead() throws IOException {
        InputStream input = mock(InputStream.class);
        when(input.skip(10)).thenReturn(0);
        when(input.read()).thenReturn(1);

        long actual = IOUtils.skip(input, 10);
        assertEquals(1L, actual);
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream("abcdefg".getBytes());
        byte[] expected = new byte[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g' };
        byte[] actual = new byte[7];

        IOUtils.readFully(input, actual);
        assertArrayEquals(expected, actual);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyWithInvalidOffsetOrLength() throws IOException {
        InputStream input = mock(InputStream.class);
        byte[] b = new byte[10];

        IOUtils.readFully(input, b, -1, 1);
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("abcdefg".getBytes());
        byte[] expected = new byte[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g' };
        byte[] actual = IOUtils.toByteArray(input);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testCloseQuietly() {
        Closeable closeable = mock(Closeable.class);

        IOUtils.closeQuietly(closeable);
    }

    @Test
    public void testCloseQuietlyThrowsIOException() {
        Closeable closeable = mock(Closeable.class);
        when(closeable.close()).thenThrow(new IOException("Error closing stream"));

        assertThrows(IOException.class, () -> IOUtils.closeQuietly(closeable));
    }
}