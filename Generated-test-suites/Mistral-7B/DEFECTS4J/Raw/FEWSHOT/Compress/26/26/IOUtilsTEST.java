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

import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for IOUtils class.
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
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        IOUtils.copy(inputStream, outputStream);
        byte[] expected = new byte[] { 0x01, 0x02, 0x03 };
        byte[] actual = outputStream.toByteArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testCopy_LargeBuffer() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(
                new byte[IOUtils.COPY_BUF_SIZE * 2]);
        IOUtils.copy(inputStream, outputStream);
        byte[] expected = new byte[IOUtils.COPY_BUF_SIZE * 2];
        byte[] actual = outputStream.toByteArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testCopy_SmallBuffer() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01 });
        IOUtils.copy(inputStream, outputStream, 2);
        assertEquals(0, outputStream.size());
    }

    @Test
    public void testSkip() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        when(inputStream.skip(3)).thenReturn(3L);
        long skipped = IOUtils.skip(inputStream, 7L);
        assertEquals(3L, skipped);

        when(inputStream.skip(3)).thenReturn(0L);
        when(inputStream.read()).thenReturn(-1);
        long skipped2 = IOUtils.skip(inputStream, 7L);
        assertEquals(3L, skipped2);
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        byte[] expected = new byte[] { 0x01, 0x02, 0x03 };
        byte[] actual = new byte[3];
        IOUtils.readFully(inputStream, actual);
        assertArrayEquals(expected, actual);

        InputStream emptyInputStream = mock(InputStream.class);
        IOUtils.readFully(emptyInputStream, new byte[3]);
        assertEquals(0, emptyInputStream.read());
    }

    @Test
    public void testReadFully_StreamEnd() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        byte[] expected = new byte[] { 0x01, 0x02 };
        byte[] actual = new byte[3];
        IOUtils.readFully(inputStream, actual, 0, 2);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        byte[] expected = new byte[] { 0x01, 0x02, 0x03 };
        byte[] actual = IOUtils.toByteArray(inputStream);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testToByteArray_NullInputStream() throws IOException {
        assertNull(IOUtils.toByteArray(null));
    }

    @Test
    public void testCloseQuietly() throws IOException {
        Closeable closeable = mock(Closeable.class);
        IOUtils.closeQuietly(closeable);
        verify(closeable).close();
    }
}