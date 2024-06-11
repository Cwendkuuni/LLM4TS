package org.apache.commons.compress.utils;

import org.junit.Test;
import java.io.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        InputStream input = mock(InputStream.class);
        OutputStream output = mock(OutputStream.class);

        IOUtils.copy(input, output);

        verify(input, atLeastOnce()).read(any(byte[].class));
        verify(output, atLeastOnce()).write(any(byte[].class), anyInt(), anyInt());
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream input = mock(InputStream.class);
        OutputStream output = mock(OutputStream.class);
        int bufferSize = 1024;

        IOUtils.copy(input, output, bufferSize);

        verify(input, atLeastOnce()).read(any(byte[].class));
        verify(output, atLeastOnce()).write(any(byte[].class), anyInt(), anyInt());
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = mock(InputStream.class);
        long numToSkip = 10;

        IOUtils.skip(input, numToSkip);

        verify(input, atLeastOnce()).skip(anyLong());
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = mock(InputStream.class);
        byte[] buffer = new byte[10];

        IOUtils.readFully(input, buffer);

        verify(input, atLeastOnce()).read(any(byte[].class), anyInt(), anyInt());
    }

    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        InputStream input = mock(InputStream.class);
        byte[] buffer = new byte[10];
        int offset = 0;
        int len = 5;

        IOUtils.readFully(input, buffer, offset, len);

        verify(input, atLeastOnce()).read(any(byte[].class), anyInt(), anyInt());
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = mock(InputStream.class);

        IOUtils.toByteArray(input);

        verify(input, atLeastOnce()).read(any(byte[].class));
    }

    @Test
    public void testCloseQuietlyWithNonNullCloseable() {
        Closeable closeable = mock(Closeable.class);

        IOUtils.closeQuietly(closeable);

        verify(closeable, atLeastOnce()).close();
    }

    @Test
    public void testCloseQuietlyWithNullCloseable() {
        Closeable closeable = null;

        IOUtils.closeQuietly(closeable);

        // No exception should be thrown
    }
}