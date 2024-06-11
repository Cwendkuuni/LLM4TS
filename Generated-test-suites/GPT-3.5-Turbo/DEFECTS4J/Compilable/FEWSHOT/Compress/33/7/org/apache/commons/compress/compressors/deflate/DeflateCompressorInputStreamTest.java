/* Test cases for the DeflateCompressorInputStream class */
package org.apache.commons.compress.compressors.deflate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class DeflateCompressorInputStreamTest {
    private DeflateCompressorInputStream inputStream;

    @Before
    public void setUp() {
        byte[] data = new byte[] { 120, -100, 79, 49, 0, 45, 5, 34, 43, 26 };
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        inputStream = new DeflateCompressorInputStream(byteArrayInputStream);
    }

    @Test
    public void testRead_shouldReturnValueInRange() throws IOException {
        assertEquals(120, inputStream.read());
        assertEquals(-100, inputStream.read());
    }

    @Test
    public void testRead_shouldReturnMinusOneAtEndOfStream() throws IOException {
        for (int i = 0; i < 10; i++) {
            inputStream.read();
        }
        assertEquals(-1, inputStream.read());
    }

    @Test
    public void testRead_shouldReadBytesIntoByteArray() throws IOException {
        byte[] buffer = new byte[5];
        inputStream.read(buffer, 0, 5);
        byte[] expected = new byte[] { 120, -100, 79, 49, 0 };
        for (int i = 0; i < 5; i++) {
            assertEquals(expected[i], buffer[i]);
        }
    }

    @Test
    public void testSkip_shouldSkipBytes() throws IOException {
        long skipped = inputStream.skip(3);
        assertEquals(3, skipped);
        int result = inputStream.read();
        assertEquals(49, result);
    }

    @Test
    public void testAvailable_shouldReturnNumberOfBytesAvailable() throws IOException {
        assertEquals(10, inputStream.available());
        inputStream.read();
        assertEquals(9, inputStream.available());
    }

    @Test
    public void testClose_shouldCloseInputStream() throws IOException {
        inputStream.close();
        assertNull(inputStream.read());
    }
}