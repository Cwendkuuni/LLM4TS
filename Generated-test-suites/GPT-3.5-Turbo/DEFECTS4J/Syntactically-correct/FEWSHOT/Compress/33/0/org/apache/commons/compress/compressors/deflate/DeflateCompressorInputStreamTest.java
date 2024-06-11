
package org.apache.commons.compress.compressors.deflate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead_singleByte() throws IOException {
        byte[] data = new byte[] { 0x48, 0x65, 0x6C, 0x6C, 0x6F };
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream);
        
        assertEquals('H', deflateStream.read());
        assertEquals('e', deflateStream.read());
        assertEquals('l', deflateStream.read());
        assertEquals('l', deflateStream.read());
        assertEquals('o', deflateStream.read());
        assertEquals(-1, deflateStream.read());
        
        deflateStream.close();
    }

    @Test
    public void testRead_byteArray() throws IOException {
        byte[] data = new byte[] { 0x48, 0x65, 0x6C, 0x6C, 0x6F };
        InputStream inputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream);
        
        byte[] buffer = new byte[5];
        int bytesRead = deflateStream.read(buffer, 0, 5);
        
        assertEquals(5, bytesRead);
        assertEquals('H', buffer[0]);
        assertEquals('e', buffer[1]);
        assertEquals('l', buffer[2]);
        assertEquals('l', buffer[3]);
        assertEquals('o', buffer[4]);
        
        deflateStream.close();
    }
    
    // Additional test cases can be added to test other methods in the class

}
