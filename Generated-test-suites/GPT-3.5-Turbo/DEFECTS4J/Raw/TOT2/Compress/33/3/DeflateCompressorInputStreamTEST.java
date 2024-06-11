import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testReadSingleByte() throws IOException {
        byte[] testData = { 0x48, 0x65, 0x6c, 0x6c, 0x6f }; // "Hello"
        ByteArrayInputStream inStream = new ByteArrayInputStream(testData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inStream);
        
        assertEquals('H', deflateInputStream.read());
    }
    
    @Test
    public void testReadByteArray() throws IOException {
        byte[] testData = { 0x48, 0x65, 0x6c, 0x6c, 0x6f }; // "Hello"
        ByteArrayInputStream inStream = new ByteArrayInputStream(testData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inStream);
        
        byte[] buffer = new byte[5];
        int bytesRead = deflateInputStream.read(buffer, 0, 5);
        
        assertArrayEquals(testData, buffer);
        assertEquals(5, bytesRead);
    }
    
    @Test
    public void testSkipBytes() throws IOException {
        byte[] testData = { 0x48, 0x65, 0x6c, 0x6c, 0x6f }; // "Hello"
        ByteArrayInputStream inStream = new ByteArrayInputStream(testData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inStream);
        
        long skipped = deflateInputStream.skip(2);
        
        assertEquals(2, skipped);
    }
    
    @Test
    public void testAvailableBytes() throws IOException {
        byte[] testData = { 0x48, 0x65, 0x6c, 0x6c, 0x6f }; // "Hello"
        ByteArrayInputStream inStream = new ByteArrayInputStream(testData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inStream);
        
        int available = deflateInputStream.available();
        
        assertEquals(5, available);
    }
    
    @Test
    public void testCloseStream() throws IOException {
        byte[] testData = { 0x48, 0x65, 0x6c, 0x6c, 0x6f }; // "Hello"
        ByteArrayInputStream inStream = new ByteArrayInputStream(testData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inStream);
        
        deflateInputStream.close();
        
        assertTrue(true); // If no exception is thrown, consider the test passed
    }
    
    @Test
    public void testDeflateHeader() {
        // Test if the signature matches what is expected for a zlib/deflated file with the zlib header
        byte[] signature = { 0x78, (byte) 0x9C };
        int length = 2;
        
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[0]));
        assertTrue(deflateInputStream.checkForZlibHeader(signature, length));
    }
}