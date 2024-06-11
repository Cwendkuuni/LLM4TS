package org.apache.commons.compress.compressors.deflate;
/*
* Test cases for DeflateCompressorInputStream class
*/

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] input = {1, 2, 3, 4, 5};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        assertEquals(1, deflateInputStream.read());
        assertEquals(2, deflateInputStream.read());
        assertEquals(3, deflateInputStream.read());
        assertEquals(4, deflateInputStream.read());
        assertEquals(5, deflateInputStream.read());
        assertEquals(-1, deflateInputStream.read());
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] input = {1, 2, 3, 4, 5};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[5];

        int bytesRead = deflateInputStream.read(buffer, 0, 5);

        assertEquals(5, bytesRead);
        assertArrayEquals(input, buffer);
    }

    @Test
    public void testSkip() throws IOException {
        byte[] input = {1, 2, 3, 4, 5};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        long skippedBytes = deflateInputStream.skip(3);

        assertEquals(3, skippedBytes);
        assertEquals(4, deflateInputStream.read());
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] input = {1, 2, 3, 4, 5};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        assertEquals(5, deflateInputStream.available());
        deflateInputStream.read();
        assertEquals(4, deflateInputStream.available());
    }

    @Test
    public void testClose() throws IOException {
        byte[] input = {1, 2, 3, 4, 5};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);

        deflateInputStream.close();

        try {
            deflateInputStream.read();
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void testZlibHeader() {
        fail("Not implemented yet");
    }
}