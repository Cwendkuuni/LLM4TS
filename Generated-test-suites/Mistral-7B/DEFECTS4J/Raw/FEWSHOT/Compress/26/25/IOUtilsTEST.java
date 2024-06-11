package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
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

        when(input.read()).thenReturn(47).thenReturn(11).thenReturn(-1);
        when(output.write(anyByte())).thenAnswer(invocation -> invocation.getArgument(0));

        long result = IOUtils.copy(input, output);
        assertEquals(48, result);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream input = mock(InputStream.class);
        OutputStream output = mock(OutputStream.class);

        when(input.read(any(byte[].class))).thenAnswer(invocation -> {
            byte[] buffer = invocation.getArgument(0);
            for (int i = 0; i < buffer.length; i++) {
                buffer[i] = (byte) (i + 1);
            }
            return buffer.length;
        });

        when(output.write(anyByte())).thenAnswer(invocation -> invocation.getArgument(0));

        byte[] expectedResult = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
        byte[] result = new byte[IOUtils.COPY_BUF_SIZE + 15];

        IOUtils.copy(input, output, IOUtils.COPY_BUF_SIZE);
        assertArrayEquals(expectedResult, result);
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = mock(InputStream.class);

        when(input.skip(anyLong())).thenReturn(4L).thenReturn(7L).thenReturn(5L).thenReturn(0L);

        long result = IOUtils.skip(input, 13L);
        assertEquals(2L, result);
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello World!".getBytes());
        byte[] result = new byte[11];

        IOUtils.readFully(input, result);

        assertArrayEquals(new byte[] { 'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd' }, result);
    }

    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello World!".getBytes());
        byte[] result = new byte[11];

        IOUtils.readFully(input, result, 2, 9);

        assertArrayEquals(new byte[] { 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd' }, result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyWithInvalidArguments() throws IOException {
        InputStream input = mock(InputStream.class);
        byte[] buffer = new byte[10];

        IOUtils.readFully(input, buffer, -1, 5);
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello World!".getBytes());

        byte[] result = IOUtils.toByteArray(input);

        assertArrayEquals(new byte[] { 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33 }, result);
    }

    @Test
    public void testCloseQuietly() {
        Closeable closeable = mock(Closeable.class);

        IOUtils.closeQuietly(closeable);

        // No exception thrown
    }
}