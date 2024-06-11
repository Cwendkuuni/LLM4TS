
package org.apache.commons.compress.compressors.deflate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = {0x78, 0x9C, (byte) 0xED, (byte) 0xC1, 0x11, 0x00, (byte) 0x00, 0x00};
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream decompressor = new DeflateCompressorInputStream(inputStream);
        
        assertEquals(0x78, decompressor.read());
        assertEquals(0x9C, decompressor.read());
        assertEquals((byte) 0xED, decompressor.read());
        assertEquals((byte) 0xC1, decompressor.read());
        assertEquals(0x11, decompressor.read());
        assertEquals(0x00, decompressor.read());
        assertEquals(0x00, decompressor.read());
        assertEquals(0x00, decompressor.read());

        assertEquals(-1, decompressor.read());
        
        decompressor.close();
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] data = {0x78, 0x9C, (byte) 0xED, (byte) 0xC1, 0x11, 0x00, (byte) 0x00, 0x00};
        byte[] buffer = new byte[8];
        
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream decompressor = new DeflateCompressorInputStream(inputStream);
        
        int bytesRead = decompressor.read(buffer, 0, buffer.length);
        
        assertEquals(8, bytesRead);
        assertEquals(0x78, buffer[0]);
        assertEquals(0x9C, buffer[1]);
        assertEquals((byte) 0xED, buffer[2]);
        assertEquals((byte) 0xC1, buffer[3]);
        assertEquals(0x11, buffer[4]);
        assertEquals(0x00, buffer[5]);
        assertEquals(0x00, buffer[6]);
        assertEquals(0x00, buffer[7]);

        bytesRead = decompressor.read(buffer, 0, buffer.length);
        assertEquals(-1, bytesRead);

        decompressor.close();
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = {0x78, 0x9C, (byte) 0xED, (byte) 0xC1, 0x11, 0x00, (byte) 0x00, 0x00};
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream decompressor = new DeflateCompressorInputStream(inputStream);

        long bytesSkipped = decompressor.skip(4);
        
        assertEquals(4, bytesSkipped);
        
        int bytesRead = decompressor.read();
        assertEquals(0x11, bytesRead);
        
        decompressor.close();
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] data = {0x78, 0x9C, (byte) 0xED, (byte) 0xC1, 0x11, 0x00, (byte) 0x00, 0x00};
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream decompressor = new DeflateCompressorInputStream(inputStream);
        
        int available = decompressor.available();
        assertEquals(8, available);
        
        decompressor.read(new byte[4], 0, 4);
        
        available = decompressor.available();
        assertEquals(4, available);
        
        decompressor.close();
    }

    @Test
    public void testClose() throws IOException {
        byte[] data = {0x78, 0x9C, (byte) 0xED, (byte) 0xC1, 0x11, 0x00, (byte) 0x00, 0x00};
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream decompressor = new DeflateCompressorInputStream(inputStream);
        
        decompressor.close();
        
        assertEquals(-1, decompressor.read());
    }
    
    @Test
    public void testParameterizedConstructorWithZlibHeader() throws IOException {
        byte[] data = {0x78, 0x9C, (byte) 0xED, (byte) 0xC1, 0x11, 0x00, (byte) 0x00, 0x00};
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateParameters parameters = new DeflateParameters().withZlibHeader();
        DeflateCompressorInputStream decompressor = new DeflateCompressorInputStream(inputStream, parameters);
        
        assertEquals(0x78, decompressor.read());
        assertEquals(0x9C, decompressor.read());
        assertEquals((byte) 0xED, decompressor.read());
        assertEquals((byte) 0xC1, decompressor.read());
        assertEquals(0x11, decompressor.read());
        assertEquals(0x00, decompressor.read());
        assertEquals(0x00, decompressor.read());
        assertEquals(0x00, decompressor.read());

        assertEquals(-1, decompressor.read());
        
        decompressor.close();
    }

    @Test
    public void testParameterizedConstructorWithoutZlibHeader() throws IOException {
        byte[] data = {0x78, 0x9C, (byte) 0xED, (byte) 0xC1, 0x11, 0x00, (byte) 0x00, 0x00};
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateParameters parameters = new DeflateParameters().withoutZlibHeader();
        DeflateCompressorInputStream decompressor = new DeflateCompressorInputStream(inputStream, parameters);
        
        int first = decompressor.read();
        
        // The first byte should not be equal to the first byte of the original data (0x78)
        assertTrue(first != 0x78);
        
        decompressor.close();
    }
    
    @Test
    public void testCheckSignatureWithZlibHeader() {
        byte[] signature = {0x78, 0x9C};
        DeflateParameters parameters = new DeflateParameters().withZlibHeader();

        boolean result = DeflateCompressorInputStream.checkSignature(signature, signature.length, parameters);

        assertTrue(result);
    }

    @Test
    public void testCheckSignatureWithoutZlibHeader() {
        byte[] signature = {0x78, 0x9C};
        DeflateParameters parameters = new DeflateParameters().withoutZlibHeader();

        boolean result = DeflateCompressorInputStream.checkSignature(signature, signature.length, parameters);

        assertFalse(result);
    }
}
