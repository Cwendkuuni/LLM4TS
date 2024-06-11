package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Checksum;

public class ChecksumCalculatingInputStreamTest {

    @Mock
    private Checksum checksum;

    @Mock
    private InputStream underlyingStream;

    private ChecksumCalculatingInputStream checksumInputStream;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        checksumInputStream = new ChecksumCalculatingInputStream(checksum, underlyingStream);
    }

    @Test
    public void testRead_shouldUpdateChecksumAndReturnValueFromUnderlyingStream() throws IOException {
        when(underlyingStream.read()).thenReturn(65);
        int ret = checksumInputStream.read();
        assertEquals(65, ret);
        verify(checksum).update(65);
    }

    @Test
    public void testRead_withByteArray_shouldUpdateChecksumAndReturnValueFromUnderlyingStream() throws IOException {
        byte[] byteArray = new byte[]{65, 66};
        when(underlyingStream.read(byteArray)).thenReturn(2);
        int ret = checksumInputStream.read(byteArray);
        assertEquals(2, ret);
        verify(checksum).update(byteArray, 0, 2);
    }

    @Test
    public void testRead_withOffsetAndLength_shouldUpdateChecksumAndReturnValueFromUnderlyingStream() throws IOException {
        byte[] byteArray = new byte[]{65, 66};
        when(underlyingStream.read(byteArray, 0, 2)).thenReturn(2);
        int ret = checksumInputStream.read(byteArray, 0, 2);
        assertEquals(2, ret);
        verify(checksum).update(byteArray, 0, 2);
    }

    @Test
    public void testSkip_shouldReturnZero() throws IOException {
        when(underlyingStream.read()).thenReturn(-1);
        long ret = checksumInputStream.skip(1);
        assertEquals(0, ret);
    }

    @Test
    public void testGetValue_shouldReturnChecksumValue() {
        when(checksum.getValue()).thenReturn(12345L);
        long ret = checksumInputStream.getValue();
        assertEquals(12345L, ret);
    }
}