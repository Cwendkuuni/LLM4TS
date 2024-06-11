package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
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
        InputStream input = mock(InputStream.class);
        OutputStream output = mock(OutputStream.class);

        byte[] src = new byte[] { 0x01, 0x02, 0x03 };
        byte[] ref = new byte[] { 0x01, 0x02, 0x03 };

        when(input.read(IOUtils.COPY_BUF_SIZE)).thenReturn(src);
        when(output.write(IOUtils.COPY_BUF_SIZE, (byte[]) anyArray(), 0)).thenAnswer(i -> i[0].length);

        long count = IOUtils.copy(input, output);
        assertEquals(src.length, count);

        byte[] target = new byte[src.length];
        IOUtils.copy(input, target, 0, target.length);
        assertArrayEquals(ref, target);

        IOUtils.closeQuietly(input);
        IOUtils.closeQuietly(output);
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = mock(InputStream.class);

        long numToSkip = 12345;

        when(input.skip(numToSkip)).thenReturn(numToSkip);
        long skipped = IOUtils.skip(input, numToSkip);
        assertEquals(numToSkip, skipped);

        when(input.skip(numToSkip)).thenReturn(0);
        skipped = IOUtils.skip(input, numToSkip);
        assertEquals(numToSkip - IOUtils.skip(input, numToSkip), skipped);

        IOUtils.closeQuietly(input);
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        byte[] buffer = new byte[3];

        int read = IOUtils.readFully(input, buffer);
        assertEquals(3, read);
        assertArrayEquals(new byte[] { 0x01, 0x02, 0x03 }, buffer);

        IOUtils.closeQuietly(input);
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = mock(InputStream.class);
        OutputStream output = new ByteArrayOutputStream();

        byte[] src = new byte[] { 0x01, 0x02, 0x03 };

        when(input.read(IOUtils.COPY_BUF_SIZE)).thenReturn(src);
        when(output.write(IOUtils.COPY_BUF_SIZE, (byte[]) anyArray(), 0)).thenAnswer(i -> i[0].length);

        byte[] target = IOUtils.toByteArray(input);
        assertArrayEquals(src, target);

        IOUtils.closeQuietly(input);
        IOUtils.closeQuietly(output);
    }

    @Test
    public void testCloseQuietly() {
        Closeable closeable = mock(Closeable.class);

        IOUtils.closeQuietly(closeable);
    }
}