
package org.apache.commons.compress.compressors.deflate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.junit.Test;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testConstructorWithDefaultParameters() {
        // Create a sample byte array with compressed data
        byte[] compressedData = {120, -100, 75, 6, 0, 0, 0, 0};
        
        InputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        
        // Verify the internal InflaterInputStream
        InputStream internalInputStream = deflateInputStream.getIn();
        assertTrue(internalInputStream instanceof InflaterInputStream);
        
        // Verify the internal Inflater
        Inflater inflater = ((InflaterInputStream) internalInputStream).getInflater();
        assertTrue(inflater instanceof Inflater);
        
        // Verify the configured withZlibHeader parameter
        assertEquals(true, inflater.getAdler());
    }
    
    @Test
    public void testConstructorWithCustomParameters() {
        // Create a sample byte array with compressed data
        byte[] compressedData = {120, -100, 75, 6, 0, 0, 0, 0};
        
        InputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateParameters parameters = new DeflateParameters();
        parameters.setWithZlibHeader(false);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream, parameters);
        
        // Verify the internal InflaterInputStream
        InputStream internalInputStream = deflateInputStream.getIn();
        assertTrue(internalInputStream instanceof InflaterInputStream);
        
        // Verify the internal Inflater
        Inflater inflater = ((InflaterInputStream) internalInputStream).getInflater();
        assertTrue(inflater instanceof Inflater);
        
        // Verify the configured withZlibHeader parameter
        assertEquals(false, inflater.getAdler());
    }
    
    @Test
    public void testRead() throws IOException {
        // Create a sample byte array with compressed data
        byte[] compressedData = {120, -100, 75, 6, 0, 0, 0, 0};
        
        InputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        
        int result = deflateInputStream.read();
        assertEquals(120, result);
    }
    
    @Test
    public void testReadWithByteArray() throws IOException {
        // Create a sample byte array with compressed data
        byte[] compressedData = {120, -100, 75, 6, 0, 0, 0, 0};
        
        InputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        
        byte[] buffer = new byte[4];
        int result = deflateInputStream.read(buffer, 0, 4);
        
        assertEquals(4, result);
        for (int i = 0; i < 4; i++) {
            assertEquals(compressedData[i], buffer[i]);
        }
    }
    
    @Test
    public void testSkip() throws IOException {
        // Create a sample byte array with compressed data
        byte[] compressedData = {120, -100, 75, 6, 0, 0, 0, 0};
        
        InputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        
        long result = deflateInputStream.skip(3);
        assertEquals(3, result);
    }
    
    @Test
    public void testAvailable() throws IOException {
        // Create a sample byte array with compressed data
        byte[] compressedData = {120, -100, 75, 6, 0, 0, 0, 0};
        
        InputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        
        int result = deflateInputStream.available();
        assertTrue(result >= 0);
    }
    
    @Test
    public void testClose() throws IOException {
        // Create a sample byte array with compressed data
        byte[] compressedData = {120, -100, 75, 6, 0, 0, 0, 0};
        
        InputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        
        deflateInputStream.close();
        // Verify if the input stream is closed
        assertTrue(deflateInputStream.getIn().isClosed());
    }
}
