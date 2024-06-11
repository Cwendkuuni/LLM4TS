package org.apache.commons.compress.compressors.deflate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.junit.Before;
import org.junit.Test;

public class DeflateCompressorInputStreamTest {
    
    private static final byte[] DATA = "Hello, World!".getBytes();
    private static final byte[] COMPRESSED_DATA;

    static {
        byte[] compressed = new byte[100];
        Deflater deflater = new Deflater();
        deflater.setInput(DATA);
        deflater.finish();
        int compressedLength = deflater.deflate(compressed);
        COMPRESSED_DATA = new byte[compressedLength];
        System.arraycopy(compressed, 0, COMPRESSED_DATA, 0, compressedLength);
        deflater.end();
    }
    
    private DeflateCompressorInputStream stream;
    
    @Before
    public void setUp() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(COMPRESSED_DATA);
        stream = new DeflateCompressorInputStream(inputStream);
    }
    
    @Test
    public void testRead() throws IOException {
        int value = stream.read();
        assertEquals(DATA[0], (byte) value);
    }
    
    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = stream.read(buffer);
        assertEquals(10, bytesRead);
        for (int i = 0; i < 10; i++) {
            assertEquals(DATA[i], buffer[i]);
        }
    }
    
    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = stream.read(buffer, 3, 5);
        assertEquals(5, bytesRead);
        for (int i = 0; i < 5; i++) {
            assertEquals(DATA[i], buffer[i + 3]);
        }
    }
    
    @Test
    public void testSkip() throws IOException {
        long bytesSkipped = stream.skip(5);
        assertEquals(5, bytesSkipped);
        int value = stream.read();
        assertEquals(DATA[5], (byte) value);
    }
    
    @Test
    public void testAvailable() throws IOException {
        int available = stream.available();
        assertTrue(available > 0);
    }
    
    @Test
    public void testClose() throws IOException {
        stream.close();
        // Verify the stream is closed by trying to read from it
        int value = stream.read();
        assertEquals(-1, value);
    }
    
    @Test
    public void testDeflateParametersWithZlibHeader() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(COMPRESSED_DATA);
        DeflateParameters parameters = new DeflateParameters();
        parameters.setWithZlibHeader(true);
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(inputStream, parameters);
        int value = stream.read();
        assertEquals(DATA[0], (byte) value);
    }
    
    @Test
    public void testDeflateParametersWithoutZlibHeader() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(COMPRESSED_DATA);
        DeflateParameters parameters = new DeflateParameters();
        parameters.setWithZlibHeader(false);
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(inputStream, parameters);
        int value = stream.read();
        assertEquals(DATA[0], (byte) value);
    }
    
    @Test
    public void testCheckSignatureWithValidSignature() throws IOException {
        byte[] signature = { 0x78, (byte) 0x9c };
        boolean result = DeflateCompressorInputStream.checkSignature(signature, signature.length);
        assertTrue(result);
    }
    
    @Test
    public void testCheckSignatureWithInvalidSignature() throws IOException {
        byte[] signature = { 0x78, 0x9d };
        boolean result = DeflateCompressorInputStream.checkSignature(signature, signature.length);
        assertFalse(result);
    }
    
    @Test
    public void testCompressorType() {
        String type = stream.getCompressor().getCompressorStreamType();
        assertNotNull(type);
    }
    
    @Test
    public void testCompatibilityWithDeflaterOutputStream() throws IOException {
        byte[] original = "Hello, World!".getBytes();
        byte[] compressed = new byte[original.length];
        
        Deflater deflater = new Deflater(Deflater.DEFAULT_COMPRESSION, true);
        deflater.setInput(original);
        deflater.finish();
        int compressedLength = deflater.deflate(compressed);
        deflater.end();
        
        ByteArrayInputStream inputStream = new ByteArrayInputStream(compressed, 0, compressedLength);
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(inputStream);
        
        byte[] uncompressed = new byte[original.length];
        int uncompressedLength = stream.read(uncompressed);
        
        assertEquals(original.length, uncompressedLength);
        for (int i = 0; i < uncompressedLength; i++) {
            assertEquals(original[i], uncompressed[i]);
        }
        
        stream.close();
    }
}