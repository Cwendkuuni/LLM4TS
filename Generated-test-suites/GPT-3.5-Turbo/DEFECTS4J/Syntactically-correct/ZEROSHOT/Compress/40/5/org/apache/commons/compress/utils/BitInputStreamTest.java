package org.apache.commons.compress.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BitInputStreamTest {

    @Mock
    private InputStream mockInputStream;

    private BitInputStream bitInputStream;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        bitInputStream = new BitInputStream(mockInputStream, ByteOrder.BIG_ENDIAN);
    }

    @After
    public void tearDown() throws IOException {
        bitInputStream.close();
    }

    @Test
    public void testReadBits_Success() throws IOException {
        when(mockInputStream.read()).thenReturn(1).thenReturn(2);

        long result = bitInputStream.readBits(16);
        assertEquals(258, result);
    }

    @Test
    public void testReadBits_CountLessThanZero_ThrowsException() throws IOException {
        try {
            bitInputStream.readBits(-1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("count must not be negative or greater than 63", e.getMessage());
        }
    }

    @Test
    public void testReadBits_CountGreaterThanMaxCacheSize_ThrowsException() throws IOException {
        try {
            bitInputStream.readBits(64);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("count must not be negative or greater than 63", e.getMessage());
        }
    }

    @Test
    public void testReadBits_EndOfStream_ReturnsMinusOne() throws IOException {
        long result = bitInputStream.readBits(16);
        assertEquals(-1, result);
    }

    @Test
    public void testClearBitCache() {
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

}