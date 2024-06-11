
package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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
    public void testReadOneByte() throws IOException {
        int expectedValue = 65;

        when(mockInputStream.read()).thenReturn(expectedValue);

        int actualValue = inputStream.read();

        assertEquals(expectedValue, actualValue);
        verify(mockChecksum).update(expectedValue);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] expectedBytes = { 65, 66, 67 };

        when(mockInputStream.read(Mockito.any(byte[].class))).thenReturn(expectedBytes.length);
        when(mockInputStream.read(Mockito.any(byte[].class), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(expectedBytes.length);

        byte[] actualBytes = new byte[expectedBytes.length];
        int actualLength = inputStream.read(actualBytes);

        assertEquals(expectedBytes.length, actualLength);
        assertEquals(expectedBytes, actualBytes);
        verify(mockChecksum).update(expectedBytes, 0, expectedBytes.length);
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        byte[] expectedBytes = { 65, 66, 67 };
        int offset = 1;
        int length = 2;

        when(mockInputStream.read(Mockito.any(byte[].class), Mockito.eq(offset), Mockito.eq(length)))
                .thenReturn(length);

        byte[] actualBytes = new byte[length];
        int actualLength = inputStream.read(actualBytes, offset, length);

        assertEquals(length, actualLength);
        assertEquals(expectedBytes[offset], actualBytes[0]);
        assertEquals(expectedBytes[offset + 1], actualBytes[1]);
        verify(mockChecksum).update(expectedBytes, offset, length);
    }

    @Test
    public void testSkip() throws IOException {
        long expectedValue = 1;

        when(mockInputStream.read()).thenReturn(-1);

        long actualValue = inputStream.skip(expectedValue);

        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetValue() {
        long expectedValue = 123456789;

        when(mockChecksum.getValue()).thenReturn(expectedValue);

        long actualValue = inputStream.getValue();

        assertEquals(expectedValue, actualValue);
    }

}
