package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
        final InputStream input = mock(InputStream.class);
        final OutputStream output = mock(OutputStream.class);

        IOUtils.copy(input, output);

        verify(input).read(IOUtils.COPY_BUF_SIZE);
        verify(output).write(IOUtils.COPY_BUF_SIZE);
    }

    @Test
    public void testCopyWithCustomBufferSize() throws IOException {
        final InputStream input = mock(InputStream.class);
        final OutputStream output = mock(OutputStream.class);

        IOUtils.copy(input, output, 1024);

        verify(input).read(1024);
        verify(output).write(1024);
    }

    @Test
    public void testSkip() throws IOException {
        final InputStream input = mock(InputStream.class);

        IOUtils.skip(input, 5);
        IOUtils.skip(input, 10);
        IOUtils.skip(input, 15);

        verify(input).skip(5);
        verify(input).skip(5);
        verify(input).skip(5);
    }

    @Test
    public void testSkipFallsBackToRead() throws IOException {
        final InputStream input = mock(InputStream.class);

        IOUtils.skip(input, 5);
        IOUtils.skip(input, 5);
        IOUtils.skip(input, 3);

        verify(input).read(3);
        verify(input).read(1);
        verify(input).read(1);
    }

    @Test
    public void testReadFully() throws IOException {
        final InputStream input = new ByteArrayInputStream("test".getBytes());
        final byte[] expected = new byte[5];

        IOUtils.readFully(input, expected);

        assertArrayEquals(new byte[]{"t", 'e', 's', 't', ' '}, expected);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyNegativeIndex() {
        final InputStream input = mock(InputStream.class);
        final byte[] buffer = new byte[5];

        IOUtils.readFully(input, buffer, 5, -5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyOutOfBounds() {
        final InputStream input = mock(InputStream.class);
        final byte[] buffer = new byte[5];

        IOUtils.readFully(input, buffer, 5, 10);
    }

    @Test
    public void testToByteArray() throws IOException {
        final InputStream input = new ByteArrayInputStream("test".getBytes());
        final byte[] expected = new byte[5];

        byte[] result = IOUtils.toByteArray(input);

        assertArrayEquals(new byte[]{'t', 'e', 's', 't', ' '}, result);
    }

    @Test
    public void testCloseQuietly() {
        final Closeable closeable = mock(Closeable.class);

        IOUtils.closeQuietly(closeable);

        verify(closeable).close();
    }

    @Test
    public void testCloseQuietlyNull() {
        IOUtils.closeQuietly(null);
    }
}