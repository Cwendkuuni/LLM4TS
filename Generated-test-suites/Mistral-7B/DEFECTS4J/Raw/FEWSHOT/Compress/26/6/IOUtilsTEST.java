package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        final InputStream input = mock(InputStream.class);
        final OutputStream output = mock(OutputStream.class);

        when(input.read(new byte[8024])).thenReturn(1024);
        when(output.write(new byte[8024])).thenReturn(1024);

        final long actual = IOUtils.copy(input, output);
        assertEquals(2048L, actual);
    }

    @Test
    public void testCopyWithBuffersize() throws IOException {
        final InputStream input = mock(InputStream.class);
        final OutputStream output = mock(OutputStream.class);

        when(input.read(new byte[32768])).thenReturn(32768);
        when(output.write(new byte[32768])).thenReturn(32768);

        final long actual = IOUtils.copy(input, output, 32768);
        assertEquals(65536L, actual);
    }

    @Test
    public void testSkip() throws IOException {
        final InputStream input = mock(InputStream.class);
        final long numToSkip = 123456L;

        when(input.skip(numToSkip)).thenReturn(123456L);

        final long actual = IOUtils.skip(input, numToSkip);
        assertEquals(numToSkip, actual);
    }

    @Test
    public void testSkipWithFallback() throws IOException {
        final InputStream input = mock(InputStream.class);
        final long numToSkip = 123456L;

        when(input.skip(numToSkip)).thenReturn(0);

        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final byte[] buffer = new byte[4096];

        when(input.read(buffer)).thenReturn(-1);

        IOUtils.skip(input, numToSkip);

        assertArrayEquals(new byte[0], output.toByteArray());
    }

    @Test
    public void testReadFully() throws IOException {
        final InputStream input = mock(InputStream.class);
        final byte[] expected = new byte[] { 0x1, 0x2, 0x3, 0x4, 0x5 };
        final byte[] buffer = new byte[5];

        when(input.read(buffer)).thenReturn(5);
        when(input.read(buffer)).thenReturn(-1);

        final int actual = IOUtils.readFully(input, buffer);
        assertArrayEquals(expected, buffer);
        assertEquals(5, actual);
    }

    @Test
    public void testReadFullyWithLessBytesAvailable() throws IOException {
        final InputStream input = mock(InputStream.class);
        final byte[] expected = new byte[] { 0x1, 0x2, 0x3, 0x4 };
        final byte[] buffer = new byte[5];

        when(input.read(buffer)).thenReturn(4);
        when(input.read(buffer)).thenReturn(-1);

        final int actual = IOUtils.readFully(input, buffer);
        assertArrayEquals(expected, buffer);
        assertEquals(4, actual);
    }

    @Test
    public void testToByteArray() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });

        final byte[] expected = new byte[] { 0x01, 0x02, 0x03 };
        final byte[] actual = IOUtils.toByteArray(input);

        assertNotNull(actual);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testCloseQuietly() {
        final InputStream input = mock(InputStream.class);
        final OutputStream output = mock(OutputStream.class);

        IOUtils.closeQuietly(input);
        IOUtils.closeQuietly(output);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyWithNegativeByteLength() throws IOException {
        final InputStream input = mock(InputStream.class);
        final byte[] buffer = new byte[5];

        IOUtils.readFully(input, buffer, -1, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyWithNegativeOffset() throws IOException {
        final InputStream input = mock(InputStream.class);
        final byte[] buffer = new byte[5];

        IOUtils.readFully(input, buffer, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyWithNegativeLengthAndOffset() throws IOException {
        final InputStream input = mock(InputStream.class);
        final byte[] buffer = new byte[5];

        IOUtils.readFully(input, buffer, -1, -1);
    }

    @Test(expected = NullPointerException.class)
    public void testCopyWithNullOutputStream() throws IOException {
        final InputStream input = mock(InputStream.class);

        IOUtils.copy(input, null);
    }

    @Test(expected = NullPointerException.class)
    public void testSkipWithNullInputStream() throws IOException {
        final long numToSkip = 0L;

        IOUtils.skip(null, numToSkip);
    }

    @Test(expected = NullPointerException.class)
    public void testReadFullyWithNullInputStream() throws IOException {
        final byte[] buffer = new byte[5];

        IOUtils.readFully(null, buffer);
    }

    @Test(expected = NullPointerException.class)
    public void testToByteArrayWithNullInputStream() throws IOException {
        final InputStream input = null;

        IOUtils.toByteArray(input);
    }
}