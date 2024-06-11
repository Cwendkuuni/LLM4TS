package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.longThat;
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
        InputStream input = createMockInputStream(new byte[]{1, 2, 3});
        OutputStream output = createMockOutputStream();

        long copiedBytes = IOUtils.copy(input, output);
        assertEquals(3, copiedBytes);

        byte[] result = ((ByteArrayOutputStream) output).toByteArray();
        assertArrayEquals(new byte[]{1, 2, 3}, result);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream input = createMockInputStream(new byte[]{1, 2, 3, 4, 5});
        OutputStream output = createMockOutputStream();

        long copiedBytes = IOUtils.copy(input, output, 2);
        assertEquals(5, copiedBytes);

        byte[] result = ((ByteArrayOutputStream) output).toByteArray();
        assertArrayEquals(new byte[]{1, 1, 2, 2, 3, 3, 4, 4, 5, 5}, result);
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = createMockInputStream(new byte[]{1, 2, 3, 4, 5});

        long skippedBytes = IOUtils.skip(input, 5L);
        assertEquals(5, skippedBytes);

        long remainingBytesToSkip = 2L;
        long skippedBytes2 = IOUtils.skip(input, remainingBytesToSkip);
        assertEquals(2, skippedBytes2);

        byte[] result = input.readAllBytes();
        byte[] expectedResult = new byte[]{3, 4, 5};
        assertArrayEquals(expectedResult, result);
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = createMockInputStream(new byte[]{1, 2, 3, 4, 5});
        byte[] buffer = new byte[5];

        int readBytes = IOUtils.readFully(input, buffer);
        assertEquals(5, readBytes);

        byte[] result = new byte[5];
        System.arraycopy(buffer, 0, result, 0, 5);
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, result);
    }

    @Test
    public void testReadFullyWithNegativeOffsetAndLength() {
        InputStream input = mock(InputStream.class);

        assertThrows(IndexOutOfBoundsException.class, () -> {
            IOUtils.readFully(input, new byte[0], 1, 1);
        });

        assertThrows(IndexOutOfBoundsException.class, () -> {
            IOUtils.readFully(input, new byte[1], -1, 1);
        });

        assertThrows(IndexOutOfBoundsException.class, () -> {
            IOUtils.readFully(input, new byte[1], 1, -1);
        });
    }

    @Test
    public void testReadFullyWithPartialRead() throws IOException {
        InputStream input = createMockInputStream(new byte[]{1, 2, 3, 4, 5});

        byte[] buffer = new byte[3];
        int readBytes = IOUtils.readFully(input, buffer, 0, buffer.length);
        assertEquals(3, readBytes);

        byte[] result = new byte[5];
        System.arraycopy(buffer, 0, result, 0, 3);
        assertArrayEquals(new byte[]{1, 2, 3}, result);
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = createMockInputStream(new byte[]{1, 2, 3, 4, 5});

        byte[] result = IOUtils.toByteArray(input);
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, result);
    }

    @Test
    public void testCloseQuietly() {
        InputStream input = mock(Closeable.class);
        OutputStream output = mock(Closeable.class);

        IOUtils.copy(input, output);
        IOUtils.closeQuietly(input);
        IOUtils.closeQuietly(output);

        verify(input, times(1)).close();
        verify(output, times(1)).close();
    }

    private InputStream createMockInputStream(byte[] bytes) {
        InputStream input = mock(InputStream.class);
        when(input.readAllBytes()).thenReturn(bytes);
        when(input.available()).then(longThat(l -> l <= bytes.length));
        return input;
    }

    private OutputStream createMockOutputStream() {
        OutputStream output = mock(OutputStream.class);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        when(output).then(output1 -> byteArrayOutputStream);
        return output;
    }
}