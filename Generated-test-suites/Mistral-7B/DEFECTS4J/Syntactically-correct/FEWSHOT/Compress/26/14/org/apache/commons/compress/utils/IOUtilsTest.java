package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
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

        when(input.read()).thenReturn(-1);

        long result = IOUtils.copy(input, output);
        assertEquals(0, result);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = new ByteArrayInputStream("test".getBytes());
        OutputStream out2 = new ByteArrayOutputStream();

        IOUtils.copy(in, out2);
        byte[] expectedOutput = "test".getBytes();
        byte[] actualOutput = out2.toByteArray();
        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream input = mock(InputStream.class);
        OutputStream output = mock(OutputStream.class);

        when(input.read(new byte[IOUtils.COPY_BUF_SIZE])).thenReturn(IOUtils.COPY_BUF_SIZE);

        long result = IOUtils.copy(input, output);
        assertEquals(IOUtils.COPY_BUF_SIZE, result);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = new ByteArrayInputStream("test".getBytes());
        OutputStream out2 = new ByteArrayOutputStream();

        IOUtils.copy(in, out2, IOUtils.COPY_BUF_SIZE);
        byte[] expectedOutput = "test".getBytes();
        byte[] actualOutput = out2.toByteArray();
        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = mock(InputStream.class);
        long expectedBytesSkipped = 20;

        when(input.skip(expectedBytesSkipped)).thenReturn(expectedBytesSkipped);

        long bytesSkipped = IOUtils.skip(input, expectedBytesSkipped);
        assertEquals(expectedBytesSkipped, bytesSkipped);

        InputStream input2 = mock(InputStream.class);
        long expectedBytesToSkip = 25;

        when(input2.skip(0)).thenReturn(0);
        when(input2.read()).thenReturn(-1);

        long bytesSkipped2 = IOUtils.skip(input2, expectedBytesToSkip);
        assertEquals(expectedBytesToSkip - IOUtils.SKIP_BUF_SIZE, bytesSkipped2);
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = mock(InputStream.class);
        byte[] expectedOutput = new byte[] { 1, 2, 3, 4 };
        byte[] output = new byte[expectedOutput.length];

        when(input.read(output)).thenReturn(expectedOutput.length);
        when(input.read(output, 0, output.length)).thenReturn(expectedOutput.length);

        int bytesRead = IOUtils.readFully(input, output);
        assertEquals(expectedOutput.length, bytesRead);
        assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        byte[] expectedOutput = "test".getBytes();
        byte[] output = IOUtils.toByteArray(input);

        assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testCloseQuietly() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        OutputStream outputWithClose = new ForwardingOutputStream(output) {
            @Override
            public void close() throws IOException {
                throw new IOException("Test close failure");
            }
        };

        IOUtils.closeQuietly(outputWithClose);
    }
}

class ForwardingOutputStream extends OutputStream {
    private OutputStream delegate;

    ForwardingOutputStream(OutputStream delegate) {
        this.delegate = delegate;
    }

    @Override
    public void write(int b) throws IOException {
        delegate.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        delegate.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        delegate.flush();
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }
}