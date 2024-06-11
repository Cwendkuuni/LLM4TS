package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Inflater;

public class ZipArchiveInputStreamTest {
    private ZipArchiveInputStream zipArchiveInputStream;

    @Before
    public void setup() {
        InputStream mockedInputStream = mock(InputStream.class);
        zipArchiveInputStream = new ZipArchiveInputStream(mockedInputStream);
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        assertNull(zipArchiveInputStream.getNextZipEntry());
    }

    @Test
    public void testGetNextEntry() throws IOException {
        assertNull(zipArchiveInputStream.getNextEntry());
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[10];

        // Mock InputStream
        InputStream mockedInputStream = mock(InputStream.class);
        zipArchiveInputStream = new ZipArchiveInputStream(mockedInputStream);
        when(mockedInputStream.read(buffer, 0, buffer.length)).thenReturn(5);

        int result = zipArchiveInputStream.read(buffer, 0, buffer.length);

        assertEquals(5, result);
    }

    @Test(expected = IOException.class)
    public void testReadWhenStreamIsClosed() throws IOException {
        byte[] buffer = new byte[10];

        zipArchiveInputStream.close();

        zipArchiveInputStream.read(buffer, 0, buffer.length);
    }

    @Test
    public void testClose() throws IOException {
        InputStream mockedInputStream = mock(InputStream.class);
        zipArchiveInputStream = new ZipArchiveInputStream(mockedInputStream);

        zipArchiveInputStream.close();

        verify(mockedInputStream, times(1)).close();
    }

    @Test
    public void testSkip() throws IOException {
        long value = 10;
        long expected = 10;

        long result = zipArchiveInputStream.skip(value);

        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkipWithNegativeValue() throws IOException {
        long value = -10;

        zipArchiveInputStream.skip(value);
    }

    @Test
    public void testMatches() {
        byte[] signature1 = {0x50, 0x4B, 0x03, 0x04};
        byte[] signature2 = {0x50, 0x4B, 0x03};
        byte[] signature3 = {0x50, 0x4B, 0x03, 0x04, 0x05};

        boolean result1 = ZipArchiveInputStream.matches(signature1, signature1.length);
        boolean result2 = ZipArchiveInputStream.matches(signature2, signature2.length);
        boolean result3 = ZipArchiveInputStream.matches(signature3, signature3.length);

        assertEquals(true, result1);
        assertEquals(false, result2);
        assertEquals(false, result3);
    }

    @Test
    public void testCloseEntry() throws IOException {
        InputStream mockedInputStream = mock(InputStream.class);
        zipArchiveInputStream = new ZipArchiveInputStream(mockedInputStream);

        zipArchiveInputStream.closeEntry();

        verify(mockedInputStream, times(1)).read(any(byte[].class));
    }

    @Test(expected = IOException.class)
    public void testCloseEntryWhenStreamIsClosed() throws IOException {
        zipArchiveInputStream.close();
        zipArchiveInputStream.closeEntry();
    }

    @Test
    public void testFill() throws IOException {
        InputStream mockedInputStream = mock(InputStream.class);
        zipArchiveInputStream = new ZipArchiveInputStream(mockedInputStream);

        zipArchiveInputStream.fill();

        verify(mockedInputStream, times(1)).read(any(byte[].class));
    }

    @Test(expected = IOException.class)
    public void testFillWhenStreamIsClosed() throws IOException {
        zipArchiveInputStream.close();
        zipArchiveInputStream.fill();
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] b = new byte[10];

        InputStream mockedInputStream = mock(InputStream.class);
        zipArchiveInputStream = new ZipArchiveInputStream(mockedInputStream);

        zipArchiveInputStream.readFully(b);

        verify(mockedInputStream, times(1)).read(eq(b), anyInt(), anyInt());
    }
}