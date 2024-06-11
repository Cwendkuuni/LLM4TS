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
        when(input.available()).thenReturn(1024);

        long copied = IOUtils.copy(input, output);
        assertEquals(1024, copied);

        byte[] buffer = new byte[1024];
        InputStream inputWithData = new ByteArrayInputStream(buffer);
        OutputStream outputFile = new ByteArrayOutputStream();
        IOUtils.copy(inputWithData, outputFile);
        byte[] expected = inputWithData.readAllBytes();
        assertArrayEquals(expected, outputFile.toByteArray());
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream input = mock(InputStream.class);
        OutputStream output = mock(OutputStream.class);
        when(input.available()).thenReturn(8192);

        long copied = IOUtils.copy(input, output, 2048);
        assertEquals(6144, copied); // 8192 - 2048

        byte[] buffer = new byte[8192];
        InputStream inputWithData = new ByteArrayInputStream(buffer);
        OutputStream outputFile = new ByteArrayOutputStream();
        IOUtils.copy(inputWithData, outputFile, 2048);
        byte[] expected = new byte[6144];
        System.arraycopy(inputWithData.readAllBytes(), 0, expected, 0, 6144);
        assertArrayEquals(expected, outputFile.toByteArray());
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = mock(InputStream.class);
        when(input.skip(3))
                .thenReturn(1L)
                .thenReturn(1L)
                .thenReturn(0L);

        long skipped = IOUtils.skip(input, 6L);
        assertEquals(4L, skipped);
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = mock(InputStream.class);
        byte[] buffer = new byte[5];
        when(input.read(buffer)).thenReturn(5);

        int read = IOUtils.readFully(input, buffer, 0, buffer.length);
        assertEquals(5, read);
    }

    @Test
    public void testReadFullyWithLessData() throws IOException {
        InputStream input = mock(InputStream.class);
        byte[] buffer = new byte[5];
        when(input.read(buffer)).thenReturn(3);

        int read = IOUtils.readFully(input, buffer, 0, buffer.length);
        assertEquals(3, read);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyWithInvalidArgs() throws IOException {
        InputStream input = mock(InputStream.class);
        byte[] buffer = new byte[5];
        IOUtils.readFully(input, buffer, -1, buffer.length);
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("This is a test".getBytes());
        byte[] expected = "This is a test".getBytes();
        byte[] result = IOUtils.toByteArray(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testCloseQuietlyNull() {
        Closeable c = null;
        IOUtils.closeQuietly(c);
    }

    @Test
    public void testCloseQuietly() {
        Closeable c = mock(Closeable.class);
        when(c.close()).thenThrow(new IOException("Test Error"));

        IOUtils.closeQuietly(c);
    }
}