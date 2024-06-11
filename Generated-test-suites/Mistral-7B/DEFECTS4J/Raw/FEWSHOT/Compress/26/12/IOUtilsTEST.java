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
        InputStream input = mock(InputStream.class);
        OutputStream output = mock(OutputStream.class);
        when(input.read()).thenReturn(42).thenReturn(-1);
        when(output.write(org.apache.commons.utils.IOUtils.toByteArray(new ByteArrayInputStream(new byte[] { 1, 2, 3, 4 })))[0]).thenReturn(4);

        long result = IOUtils.copy(input, output);
        assertEquals(4L, result);
    }

    @Test
    public void testCopy_nullOutput() {
        InputStream input = mock(InputStream.class);
        OutputStream output = null;

        try {
            IOUtils.copy(input, output);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testCopy_nullInputStream() {
        InputStream input = null;
        OutputStream output = mock(OutputStream.class);

        try {
            IOUtils.copy(input, output);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = mock(InputStream.class);
        when(input.skip(1)).thenReturn(1);
        when(input.skip(3)).thenReturn(3);
        when(input.skip(4)).thenReturn(0);

        long result = IOUtils.skip(input, 8);
        assertEquals(5L, result);
    }

    @Test
    public void testSkip_nullInputStream() {
        InputStream input = null;

        try {
            IOUtils.skip(input, 0);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = mock(InputStream.class);
        byte[] buffer = new byte[] { 1, 2, 3, 4 };

        when(input.read(buffer)).thenReturn(4).thenReturn(-1);

        int result = IOUtils.readFully(input, buffer);
        assertArrayEquals(buffer, IOUtils.toByteArray(input));
        assertEquals(4, result);
    }

    @Test
    public void testReadFully_nullBuffer() {
        InputStream input = mock(InputStream.class);

        try {
            IOUtils.readFully(input, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testReadFully_nullInputStream() {
        InputStream input = null;

        try {
            IOUtils.readFully(input, new byte[0]);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4 });

        byte[] result = IOUtils.toByteArray(input);
        assertArrayEquals(new byte[] { 1, 2, 3, 4 }, result);
    }

    @Test
    public void testToByteArray_nullInputStream() {
        InputStream input = null;

        try {
            IOUtils.toByteArray(input);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testCloseQuietly() throws IOException {
        OutputStream output = mock(OutputStream.class);
        Closeable closeable = (Closeable) output;

        IOUtils.closeQuietly(closeable);
        verify(closeable).close();
    }

    @Test
    public void testCloseQuietly_nullCloseable() {
        Closeable closeable = null;

        IOUtils.closeQuietly(closeable);
        // no verification needed
    }
}