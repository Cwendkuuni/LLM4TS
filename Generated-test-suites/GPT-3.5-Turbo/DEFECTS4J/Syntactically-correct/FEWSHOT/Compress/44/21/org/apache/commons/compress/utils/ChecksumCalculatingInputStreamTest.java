
package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Checksum;

import org.junit.Before;
import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    private ChecksumCalculatingInputStream inputStream;
    private Checksum mockChecksum;
    private InputStream mockInputStream;

    @Before
    public void setUp() {
        mockChecksum = mock(Checksum.class);
        mockInputStream = mock(InputStream.class);
        inputStream = new ChecksumCalculatingInputStream(mockChecksum, mockInputStream);
    }

    @Test
    public void testRead_whenDataIsAvailable_updatesChecksumAndReturnsData() throws IOException {
        when(mockInputStream.read()).thenReturn((int) 'a');

        int result = inputStream.read();

        verify(mockInputStream).read();
        verify(mockChecksum).update('a');
        assertEquals('a', result);
    }

    @Test
    public void testRead_whenEndOfStream_returnNegativeValue() throws IOException {
        when(mockInputStream.read()).thenReturn(-1);

        int result = inputStream.read();

        verify(mockInputStream).read();
        assertEquals(-1, result);
    }

    @Test
    public void testRead_withByteArray_updatesChecksumAndReturnsBytesRead() throws IOException {
        byte[] bytes = new byte[5];
        when(mockInputStream.read(bytes)).thenReturn(3);

        int result = inputStream.read(bytes);

        verify(mockInputStream).read(bytes);
        verify(mockChecksum).update(bytes, 0, 3);
        assertEquals(3, result);
    }

    @Test
    public void testRead_withOffsetAndLength_updatesChecksumAndReturnsBytesRead() throws IOException {
        byte[] bytes = new byte[5];
        when(mockInputStream.read(bytes, 1, 3)).thenReturn(2);

        int result = inputStream.read(bytes, 1, 3);

        verify(mockInputStream).read(bytes, 1, 3);
        verify(mockChecksum).update(bytes, 1, 2);
        assertEquals(2, result);
    }

    @Test
    public void testSkip_returnsNumberOfSkippedBytes() throws IOException {
        when(mockInputStream.read()).thenReturn(-1);

        long result = inputStream.skip(10);

        verify(mockInputStream).read();
        assertEquals(0, result);
    }

    @Test
    public void testGetValue_returnsChecksumValue() {
        when(mockChecksum.getValue()).thenReturn(12345L);

        long result = inputStream.getValue();

        verify(mockChecksum).getValue();
        assertEquals(12345L, result);
    }
}
