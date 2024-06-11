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

import org.junit.Before;
import org.junit.Test;

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
        final byte[] inputArray = new byte[] { 1, 2, 3 };
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        when(input.read()).thenReturn(inputArray[0]);
        when(input.read()).thenReturn(inputArray[1]);
        when(input.read()).thenReturn(inputArray[2]);
        when(input.read()).thenReturn(-1);

        IOUtils.copy(input, outputStream);
        assertArrayEquals(inputArray, outputStream.toByteArray());
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        final byte[] inputArray = new byte[] { 1, 2, 3 };
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        when(input.read()).thenReturn(inputArray[0]);
        when(input.read()).thenReturn(inputArray[1]);
        when(input.read()).thenReturn(inputArray[2]);
        when(input.read()).thenReturn(-1);

        IOUtils.copy(input, outputStream, 5);
        assertArrayEquals(inputArray, outputStream.toByteArray());
    }

    @Test
    public void testCopyWithNullInput() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> IOUtils.copy(null, output));
    }

    @Test
    public void testCopyWithNullOutput() throws IOException {
        assertThrows(IllegalArgumentException.class, () -> IOUtils.copy(input, null));
    }

    @Test
    public void testSkip() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });

        assertEquals(12, IOUtils.skip(input, 12L));
        assertEquals(1, input.read());
        assertEquals(2, input.read());
        assertEquals(3, input.read());
        assertEquals(4, input.read());
        assertEquals(5, input.read());
        assertEquals(-1, input.read());
    }

    @Test
    public void testSkipWithNegativeNumber() throws IOException {
        final InputStream input = mock(InputStream.class);
        when(input.skip(Long.MAX_VALUE)).thenReturn(Long.MAX_VALUE);
        assertThrows(IllegalArgumentException.class, () -> IOUtils.skip(input, -1L));
    }

    @Test
    public void testSkipWithNullInput() throws IOException {
        assertThrows(NullPointerException.class, () -> IOUtils.skip(null, 1L));
    }

    @Test
    public void testReadFully() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });
        final byte[] output = new byte[5];

        IOUtils.readFully(input, output);
        assertArrayEquals(new byte[] { 1, 2, 3, 4, 5 }, output);

        final InputStream emptyInput = new ByteArrayInputStream(new byte[0]);
        final byte[] emptyOutput = new byte[0];
        IOUtils.readFully(emptyInput, emptyOutput);
        assertArrayEquals(new byte[0], emptyOutput);
    }

    @Test
    public void testReadFullyWithNegativeNumber() throws IOException {
        final InputStream input = mock(InputStream.class);
        when(input.read()).thenAnswer(invocationOnMock -> -1);
        assertThrows(IndexOutOfBoundsException.class, () -> IOUtils.readFully(input, new byte[1]));
    }

    @Test
    public void testReadFullyWithNullOutput() throws IOException {
        assertThrows(NullPointerException.class, () -> IOUtils.readFully(input, null));
    }

    @Test
    public void testToByteArray() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        final byte[] expected = new byte[] { 1, 2, 3 };

        assertArrayEquals(expected, IOUtils.toByteArray(input));
    }

    @Test
    public void testToByteArrayWithNullInput() throws IOException {
        assertNull(IOUtils.toByteArray(null));
    }

    @Test
    public void testCloseQuietly() {
        final Closeable closeable = mock(Closeable.class);

        IOUtils.closeQuietly(closeable);
    }
}