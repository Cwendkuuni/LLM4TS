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

import org.junit.Test;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        final InputStream input = mock(InputStream.class);
        final OutputStream output = mock(OutputStream.class);

        when(input.read()).thenReturn(42).thenReturn(-1);
        when(output.write(anyByte())).thenAnswer(invocation -> invocation.getArgument(0)[0]);

        final byte[] expected = new byte[] { 0, 1, 2, 3 };
        final byte[] buffer = new byte[4];

        IOUtils.copy(input, output);
        IOUtils.copy(input, output, 2);

        byte[] result = new byte[4];
        System.arraycopy(expected, 0, result, 0, expected.length);
        assertArrayEquals(result, buffer);
    }

    @Test
    public void testSkip() throws IOException {
        final InputStream input = mock(InputStream.class);
        when(input.skip(anyLong())).thenReturn(5L).thenReturn(-1L);

        assertEquals(10L, IOUtils.skip(input, 10L));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyThrowsIndexOutOfBoundsException() throws IOException {
        final InputStream input = mock(InputStream.class);
        when(input.read()).thenAnswer(invocation -> ((byte[]) invocation.getArgument(1))[0]);
        final byte[] buffer = new byte[1];

        IOUtils.readFully(input, buffer, 0, 2);
    }

    @Test
    public void testReadFully() throws IOException {
        final InputStream input = mock(InputStream.class);
        final byte[] buffer = new byte[] { 0, 1, 2, 3, 4 };

        when(input.read()).thenAnswer(invocation -> ((byte[]) invocation.getArgument(1))[invocation.getArgument(2)]);
        when(input.read(anyByteArray(), anyInt(), anyInt())).thenReturn(5);

        assertEquals(5, IOUtils.readFully(input, buffer));
    }

    @Test
    public void testToByteArray() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[] { 0, 1, 2, 3 });

        final byte[] expected = new byte[] { 0, 1, 2, 3 };
        final byte[] result = IOUtils.toByteArray(input);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testCloseQuietly() {
        final Closeable closeable = mock(Closeable.class);

        IOUtils.closeQuietly(closeable);
    }
}