
package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    public void testReadBits() throws IOException {
        byte[] inputData = { 0x01 };
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputData);
        BitInputStream bitInputStream = new BitInputStream(byteArrayInputStream, ByteOrder.BIG_ENDIAN);

        long result = bitInputStream.readBits(4);
        assertEquals(0x0, result);

        result = bitInputStream.readBits(4);
        assertEquals(0x1, result);

        result = bitInputStream.readBits(8);
        assertEquals(-1, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithInvalidCount() throws IOException {
        bitInputStream.readBits(64);
    }

    @Test
    public void testClearBitCache() throws IOException {
        bitInputStream.clearBitCache();

        verifyZeroInteractions(mockInputStream);
    }
}
