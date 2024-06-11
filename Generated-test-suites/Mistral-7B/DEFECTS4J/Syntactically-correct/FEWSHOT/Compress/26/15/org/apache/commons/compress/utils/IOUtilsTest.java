package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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

public class IOUtilsTest {

    private InputStream input;
    private OutputStream output;

    @Before
    public void setup() {
        input = mock(InputStream.class);
        output = mock(OutputStream.class);
    }

    @Test
    public void testCopy() throws IOException {
        when(input.read()).thenReturn(47);
        when(input.read()).thenReturn(13);
        when(input.read()).thenReturn(-1);

        byte[] expected = new byte[] { 47, 13 };
        byte[] buffer = new byte[2];

        long result = IOUtils.copy(input, output);
        assertEquals(2, result);

        byte[] actual = new byte[2];
        System.arraycopy(IOUtils.toByteArray(output), 0, actual, 0, 2);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        when(input.read()).thenReturn(47);
        when(input.read()).thenReturn(13);
        when(input.read()).thenReturn(-1);

        byte[] expected = new byte[] { 47, 13 };
        byte[] buffer = new byte[5];

        long result = IOUtils.copy(input, output);
        assertEquals(2, result);

        byte[] actual = new byte[2];
        System.arraycopy(IOUtils.toByteArray(output), 0, actual, 0, 2);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSkip() throws IOException {
        when(input.skip(0)).thenReturn(0L);
        when(input.skip(1)).thenReturn(1L);
        when(input.skip(1)).thenReturn(0L);
        when(input.skip(2)).thenReturn(2L);

        long result = IOUtils.skip(input, 4L);
        assertEquals(3L, result);
    }

    @Test
    public void testSkipWithNegativeArgument() throws IOException {
        when(input.skip(-1)).thenReturn(0L);
        when(input.skip(0)).thenReturn(0L);

        long result = IOUtils.skip(input, -1L);
        assertEquals(0L, result);
    }

    @Test
    public void testReadFully() throws IOException {
        when(input.read()).thenReturn(47);
        when(input.read()).thenReturn(13);
        when(input.read()).thenReturn(-1);

        byte[] expected = new byte[] { 47, 13 };
        byte[] buffer = new byte[2];

        int result = IOUtils.readFully(input, buffer);
        assertEquals(2, result);

        byte[] actual = new byte[2];
        System.arraycopy(buffer, 0, actual, 0, 2);
        assertArrayEquals(expected, actual);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyWithInvalidOffset() throws IOException {
        IOUtils.readFully(input, new byte[] { 0 }, -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyWithInvalidLength() throws IOException {
        IOUtils.readFully(input, new byte[] { 0 }, 0, -1);
    }

    @Test
    public void testToByteArray() throws IOException {
        when(input.read()).thenReturn(47);
        when(input.read()).thenReturn(13);
        when(input.read()).thenReturn(-1);

        byte[] expected = new byte[] { 47, 13 };

        byte[] result = IOUtils.toByteArray(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testCloseQuietly() {
        Closeable closeable = mock(Closeable.class);

        IOUtils.closeQuietly(closeable);
    }
}