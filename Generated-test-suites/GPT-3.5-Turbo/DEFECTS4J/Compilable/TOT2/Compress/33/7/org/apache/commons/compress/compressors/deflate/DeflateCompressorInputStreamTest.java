package org.apache.commons.compress.compressors.deflate;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = {120, -100, -60, 65, 66};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(byteArrayInputStream);
        
        assertEquals(120, deflateCompressorInputStream.read());
        assertEquals(-100, deflateCompressorInputStream.read());
        assertEquals(-60, deflateCompressorInputStream.read());
        assertEquals(65, deflateCompressorInputStream.read());
        assertEquals(66, deflateCompressorInputStream.read());
    }
    
    @Test
    public void testReadByteArray() throws IOException {
        byte[] data = {120, -100, -60, 65, 66};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(byteArrayInputStream);
        byte[] buf = new byte[5];
        
        assertEquals(5, deflateCompressorInputStream.read(buf, 0, 5));
        assertArrayEquals(data, buf);
    }
    
    @Test
    public void testSkip() throws IOException {
        byte[] data = {120, -100, -60, 65, 66};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(byteArrayInputStream);
        
        assertEquals(3, deflateCompressorInputStream.skip(3));
        assertEquals(65, deflateCompressorInputStream.read());
    }
    
    @Test
    public void testAvailable() throws IOException {
        byte[] data = {120, -100, -60, 65, 66};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(byteArrayInputStream);
        
        assertEquals(5, deflateCompressorInputStream.available());
    }
    
    @Test
    public void testClose() throws IOException {
        byte[] data = {120, -100, -60, 65, 66};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(byteArrayInputStream);
        
        deflateCompressorInputStream.close();
        assertTrue(byteArrayInputStream.read() == -1);
    }
    
    @Test
    public void testDeflateParametersConstructor() throws IOException {
        byte[] data = {120, -100, -60, 65, 66};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(byteArrayInputStream);
        
        assertNotNull(deflateCompressorInputStream);
    }
}