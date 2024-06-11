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
    public void testCopy() throws Exception {
        InputStream input = mock(InputStream.class);
        OutputStream output = mock(OutputStream.class);
        when(input.read()).thenReturn(47);
        when(input.read()).thenReturn(24);
        when(input.read()).thenReturn(-1);
        when(output.write(new byte[0])).thenReturn(47);
        when(output.write(new byte[0])).thenReturn(24);

        long l = IOUtils.copy(input, output);
        assertEquals(61L, l);

        byte[] expected = new byte[] { 47, 24 };
        byte[] actual = new byte[2];
        IOUtils.readFully(new ByteArrayInputStream(IOUtils.toByteArray(new ByteArrayOutputStream().toByteArray())), actual, 0, 2);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testCopyWithBufferSize() throws Exception {
        InputStream input = mock(InputStream.class);
        OutputStream output = mock(OutputStream.class);
        when(input.read()).thenReturn(47);
        when(input.read()).thenReturn(24);
        when(input.read()).thenReturn(-1);
        when(output.write(new byte[0])).thenReturn(47);
        when(output.write(new byte[0])).thenReturn(24);

        long l = IOUtils.copy(input, output, 7);
        assertEquals(61L, l);

        byte[] expected = new byte[] { 47, 24 };
        byte[] actual = new byte[2];
        IOUtils.readFully(new ByteArrayInputStream(IOUtils.toByteArray(new ByteArrayOutputStream().toByteArray())), actual, 0, 2);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSkip() throws Exception {
        InputStream input = mock(InputStream.class);
        when(input.skip(1)).thenReturn(1);
        when(input.skip(4)).thenReturn(4);
        when(input.skip(10)).thenReturn(10);
        when(input.skip(12)).thenReturn(12);

        long numToSkip = 17;
        long skipped = IOUtils.skip(input, numToSkip);
        assertEquals(12L, skipped);

        numToSkip = 17;
        skipped = IOUtils.skip(input, numToSkip);
        assertEquals(12L, skipped);
    }

    @Test
    public void testReadFully() throws Exception {
        InputStream input = new ByteArrayInputStream("Hello World!".getBytes());
        byte[] buffer = new byte[12];

        int bytesRead = IOUtils.readFully(input, buffer);
        assertEquals(11, bytesRead);
        assertArrayEquals("Hello World!".substring(0, 11).getBytes(), buffer, 0, 11);

        bytesRead = IOUtils.readFully(input, buffer, 1, 10);
        assertEquals(9, bytesRead);
        assertArrayEquals("World!".getBytes(), buffer, 1, 9);

        bytesRead = IOUtils.readFully(input, buffer, 11, 3);
        assertEquals(3, bytesRead);
        assertArrayEquals("d!".getBytes(), buffer, 11, 3);

        bytesRead = IOUtils.readFully(input, buffer, 12, 1);
        assertEquals(0, bytesRead);
    }

    @Test
    public void testToByteArray() throws Exception {
        InputStream input = new ByteArrayInputStream("Hello World!".getBytes());
        byte[] expected = "Hello World!".getBytes();

        byte[] actual = IOUtils.toByteArray(input);
        assertArrayEquals(expected, actual);

        actual = IOUtils.toByteArray(new ByteArrayInputStream(new byte[0]));
        assertArrayEquals(new byte[0], actual);
    }

    @Test
    public void testCloseQuietly() {
        Closeable closeable = mock(Closeable.class);

        IOUtils.closeQuietly(closeable);
    }
}