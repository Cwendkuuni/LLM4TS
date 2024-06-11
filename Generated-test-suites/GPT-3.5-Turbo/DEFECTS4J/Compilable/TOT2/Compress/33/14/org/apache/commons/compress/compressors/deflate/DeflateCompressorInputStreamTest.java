package org.apache.commons.compress.compressors.deflate;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testReadSingleByte() throws IOException {
        byte[] data = {0x48};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream decompressor = new DeflateCompressorInputStream(inputStream);
        
        assertEquals('H', decompressor.read());
    }

    @Test
    public void testReadBuffer() throws IOException {
        byte[] data = {0x48, 0x65, 0x6C, 0x6C, 0x6F};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream decompressor = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[5];
        
        assertEquals(5, decompressor.read(buffer, 0, 5));
        assertArrayEquals(data, buffer);
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = {0x48, 0x65, 0x6C, 0x6C, 0x6F};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream decompressor = new DeflateCompressorInputStream(inputStream);
        
        assertEquals(3, decompressor.skip(3));
        assertEquals('l', decompressor.read());
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] data = {0x48, 0x65, 0x6C, 0x6C, 0x6F};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream decompressor = new DeflateCompressorInputStream(inputStream);
        
        assertEquals(5, decompressor.available());
    }

    @Test
    public void testClose() throws IOException {
        byte[] data = {0x48, 0x65, 0x6C, 0x6C, 0x6F};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream decompressor = new DeflateCompressorInputStream(inputStream);
        
        decompressor.close();
        assertTrue("Input stream should be closed", inputStream.read() == -1);
    }

    @Test
    public void testDeflateParametersConstructor() throws IOException {
        byte[] data = {0x48, 0x65, 0x6C, 0x6C, 0x6F};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream decompressor = new DeflateCompressorInputStream(inputStream, new DeflateParameters());
        
		byte[] buffer = new byte[5];
        assertEquals(5, decompressor.read(buffer, 0, 5));
        assertArrayEquals(data, buffer);
    }

    // Edge case test for invalid input data
    @Test(expected = IOException.class)
    public void testInvalidInputDataStream() throws IOException {
        byte[] data = {0x48, 0x65, 0x6C, 0x6C, 0x6F};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream decompressor = new DeflateCompressorInputStream(inputStream);

        // Read more bytes than available in the stream
        byte[] buffer = new byte[10];
        decompressor.read(buffer, 0, 10);
    }
}