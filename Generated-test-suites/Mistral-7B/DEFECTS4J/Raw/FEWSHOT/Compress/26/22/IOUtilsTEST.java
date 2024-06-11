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
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class IOUtilsTest {

    private InputStream input;
    private OutputStream output;
    private Closeable closeable;

    @Before
    public void setup() {
        input = mock(InputStream.class);
        output = mock(OutputStream.class);
        closeable = mock(Closeable.class);
    }

    @Test
    public void testCopy() throws IOException {
        final byte[] expected = new byte[] { 1, 2, 3 };
        when(input.read(expected)).thenReturn(expected.length);

        IOUtils.copy(input, output);

        ArgumentCaptor<byte[]> byteCaptor = ArgumentCaptor.forClass(byte[].class);
        verifyCopy(input, output, expected.length, expected);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        final byte[] expected = new byte[] { 1, 2, 3 };
        when(input.read(ArgumentCaptor.forClass(byte[].class))).thenReturn(expected.length);

        IOUtils.copy(input, output, 2048);

        ArgumentCaptor<byte[]> byteCaptor = ArgumentCaptor.forClass(byte[].class);
        ArgumentCaptor<Integer> intCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(output).write(byteCaptor.capture(), intCaptor.capture());
        assertEquals(expected.length, intCaptor.getValue().intValue());
        assertArrayEquals(expected, byteCaptor.getValue());
    }

    @Test
    public void testSkip() throws IOException {
        final long numToSkip = 5;

        InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        long skipped = IOUtils.skip(input, numToSkip);
        assertEquals(numToSkip - (input.available() + skipped), input.available());

        input = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        when(input.skip(ArgumentCaptor.forClass(Long.class))).thenReturn(0);
        long skipped2 = IOUtils.skip(input, numToSkip);
        assertEquals(0, skipped2);
    }

    @Test
    public void testReadFully() throws IOException {
        final byte[] expected = new byte[] { 1, 2, 3 };

        InputStream input = new ByteArrayInputStream(expected);
        byte[] output = new byte[expected.length];
        IOUtils.readFully(input, output);
        assertArrayEquals(expected, output);
    }

    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        final byte[] expected = new byte[] { 1, 2, 3 };

        InputStream input = new ByteArrayInputStream(expected);
        byte[] output = new byte[expected.length + 1];
        int read = IOUtils.readFully(input, output, 1, 3);
        assertArrayEquals(Arrays.copyOfRange(expected, 1, 3), Arrays.copyOfRange(output, 1, 3));
        assertEquals(2, read);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyWithNegativeOffset() throws IOException {
        final byte[] expected = new byte[] { 1, 2, 3 };
        IOUtils.readFully(mock(InputStream.class), new byte[] { 0 }, -1, 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyWithNegativeLength() throws IOException {
        final byte[] expected = new byte[] { 1, 2, 3 };
        IOUtils.readFully(mock(InputStream.class), new byte[] { 0 }, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyWithOffsetAndNegativeLength() throws IOException {
        final byte[] expected = new byte[] { 1, 2, 3 };
        IOUtils.readFully(mock(InputStream.class), new byte[] { 0 }, 0, expected.length + 1);
    }

    @Test
    public void testToByteArray() throws IOException {
        final byte[] input = new byte[] { 1, 2, 3 };
        byte[] expected = IOUtils.toByteArray(new ByteArrayInputStream(input));
        assertArrayEquals(input, expected);
    }

    @Test
    public void testCloseQuietly() throws IOException {
        IOUtils.closeQuietly(output);
        verify(output).close();
    }

    @Test
    public void testCloseQuietlyNull() {
        IOUtils.closeQuietly(null);
        // No verification needed
    }

    private void verifyCopy(InputStream input, OutputStream output, int expectedLength, byte[] expected)
            throws IOException {
        ArgumentCaptor<byte[]> byteCaptor = ArgumentCaptor.forClass(byte[].class);
        ArgumentCaptor<Integer> intCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(output).write(byteCaptor.capture(), intCaptor.capture());
        assertEquals(expectedLength, intCaptor.getValue().intValue());
        assertArrayEquals(expected, byteCaptor.getValue());
        verify(input).read(byteCaptor.capture());
        assertArrayEquals(expected, byteCaptor.getValue());
    }
}