package org.apache.commons.compress.compressors.deflate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = {0x78, 0x9C, 0x01, 0x00};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream in = new DeflateCompressorInputStream(inputStream);

        int result = in.read();
        assertEquals(1, result);
        
        in.close();
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] data = {0x78, 0x9C, 0x01, 0x00};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream in = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[2];

        int result = in.read(buffer, 0, 2);
        assertEquals(2, result);
        assertEquals(0x78, buffer[0]);
        assertEquals(0x9C, buffer[1]);
        
        in.close();
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = {0x78, 0x9C, 0x01, 0x00};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream in = new DeflateCompressorInputStream(inputStream);

        long skipped = in.skip(2);
        assertEquals(2, skipped);
        
        in.close();
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] data = {0x78, 0x9C, 0x01, 0x00};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream in = new DeflateCompressorInputStream(inputStream);

        int available = in.available();
        assertEquals(4, available);
        
        in.close();
    }

    @Test
    public void testClose() throws IOException {
        byte[] data = {0x78, 0x9C, 0x01, 0x00};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream in = new DeflateCompressorInputStream(inputStream);

        in.close();
        assertFalse(in.markSupported());
        
        byte[] buffer = new byte[2];
        int result = in.read(buffer, 0, 2);
        assertEquals(-1, result);
    }

    @Test
    public void testIsZlibWithZlibHeader() {
        byte[] data = {0x78, 0x9C, 0x01, 0x00};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream in = new DeflateCompressorInputStream(inputStream);

        assertTrue(in.isZlibWithZlibHeader());
        
        in.close();
    }

    @Test
    public void testIsZlibWithZlibHeaderFalse() {
        byte[] data = {0x78, 0x01, 0x00};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream in = new DeflateCompressorInputStream(inputStream);

        assertFalse(in.isZlibWithZlibHeader());
        
        in.close();
    }
}