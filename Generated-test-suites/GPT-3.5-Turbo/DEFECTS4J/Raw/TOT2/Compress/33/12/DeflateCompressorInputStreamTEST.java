package org.apache.commons.compress.compressors.deflate;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = { 0x78, 0x9C, 0x00 }; // Deflate compressed data
        DeflateCompressorInputStream input = new DeflateCompressorInputStream(new ByteArrayInputStream(data));
        assertEquals(0x00, input.read()); // Check if the first byte is read correctly
        input.close();
    }

    @Test
    public void testReadBuffer() throws IOException {
        byte[] data = { 0x78, 0x9C, 0x00 }; // Deflate compressed data
        DeflateCompressorInputStream input = new DeflateCompressorInputStream(new ByteArrayInputStream(data));
        byte[] buffer = new byte[3];
        int numBytes = input.read(buffer, 0, 3);
        assertArrayEquals(data, buffer); // Check if the entire buffer is read correctly
        input.close();
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] data = { 0x78, 0x9C, 0x00 }; // Deflate compressed data
        DeflateCompressorInputStream input = new DeflateCompressorInputStream(new ByteArrayInputStream(data));
        assertEquals(3, input.available()); // Check if available method returns correct value
        input.close();
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = { 0x78, 0x9C, 0x00 }; // Deflate compressed data
        DeflateCompressorInputStream input = new DeflateCompressorInputStream(new ByteArrayInputStream(data));
        long bytesSkipped = input.skip(2);
        assertEquals(2, bytesSkipped); // Check if skip method returns correct value
        input.close();
    }

    @Test
    public void testClose() throws IOException {
        byte[] data = { 0x78, 0x9C, 0x00 }; // Deflate compressed data
        DeflateCompressorInputStream input = new DeflateCompressorInputStream(new ByteArrayInputStream(data));
        input.close();
        assertTrue(true); // Check if close method executes without exceptions
    }

}