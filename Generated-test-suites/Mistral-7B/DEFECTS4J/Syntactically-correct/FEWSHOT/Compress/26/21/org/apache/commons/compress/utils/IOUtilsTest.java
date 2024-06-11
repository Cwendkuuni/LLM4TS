package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Test cases for IOUtils class
 * @author <a href="mailto:jbonhoff@apache.org">Johannes Bonhoff</a>
 */
public class IOUtilsTest {

    private InputStream input;
    private OutputStream output;

    @Before
    public void setUp() {
        input = mock(InputStream.class);
        output = mock(OutputStream.class);
    }

    @Test
    public void testCopy() throws IOException {
        final byte[] buffer = new byte[] { 1, 2, 3, 4 };
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        when(input.read()).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                int index = ((Integer) invocation.getArguments()[0]).intValue();
                return buffer[index] < 0 ? -1 : buffer[index];
            }
        });

        IOUtils.copy(input, output);
        byte[] result = output.toByteArray();

        assertArrayEquals(buffer, result);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        final byte[] buffer = new byte[] { 1, 2, 3, 4 };
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        when(input.read(any(byte[].class))).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                byte[] bytes = (byte[]) invocation.getArguments()[0];
                int read = Math.min(buffer.length, bytes.length);
                System.arraycopy(buffer, 0, bytes, 0, read);
                return read;
            }
        });

        IOUtils.copy(input, output, IOUtils.COPY_BUF_SIZE);
        byte[] result = output.toByteArray();

        assertArrayEquals(buffer, result);
    }

    @Test
    public void testSkip() throws IOException {
        final long numToSkip = 10;

        when(input.skip(anyLong())).thenAnswer(new Answer<Long>() {
            @Override
            public Long answer(InvocationOnMock invocation) throws Throwable {
                long skipped = ((Long) invocation.getArguments()[0]).longValue();
                long available = numToSkip - skipped;
                if (available <= 0) {
                    return 0L;
                }
                return available;
            }
        });

        long skipped = IOUtils.skip(input, numToSkip);

        assertEquals(numToSkip, skipped);
    }

    @Test
    public void testReadFully() throws IOException {
        final byte[] buffer = new byte[] { 1, 2, 3, 4 };
        final int len = buffer.length;
        final int offset = 0;

        when(input.read(any(byte[].class))).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                byte[] bytes = (byte[]) invocation.getArguments()[0];
                int read = Math.min(len, bytes.length);
                System.arraycopy(buffer, offset, bytes, 0, read);
                return read;
            }
        });

        int actual = IOUtils.readFully(input, new byte[len], offset, len);
        assertArrayEquals(buffer, new byte[len]);
        assertEquals(len, actual);
    }

    @Test
    public void testReadFullyWithBuffer() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4 });
        final byte[] buffer = new byte[IOUtils.COPY_BUF_SIZE];
        final int len = 3;

        int actual = IOUtils.readFully(input, buffer, 0, len);
        assertArrayEquals(new byte[] { 1, 2, 3 }, buffer, 0, len);
        assertEquals(len, actual);
    }

    @Test
    public void testReadFullyWithEndOfStream() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[0]);
        final byte[] buffer = new byte[10];

        int actual = IOUtils.readFully(input, buffer, 0, buffer.length);
        assertEquals(0, actual);
    }

    @Test
    public void testToByteArray() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4 });
        final byte[] expected = new byte[] { 1, 2, 3, 4 };

        byte[] actual = IOUtils.toByteArray(input);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testCloseQuietly() {
        Closeable closeable = mock(Closeable.class);

        IOUtils.closeQuietly(closeable);
        verify(closeable).close();
    }

    @Test
    public void testCloseQuietlyNull() {
        IOUtils.closeQuietly(null);
        // no exception expected
    }
}