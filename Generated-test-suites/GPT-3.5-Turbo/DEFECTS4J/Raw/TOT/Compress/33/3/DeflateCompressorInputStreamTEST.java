import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;

public class DeflateCompressorInputStreamTest {
    
    private static final byte[] BYTES = "Hello, World!".getBytes();
    private DeflateCompressorInputStream stream;
    
    @Before
    public void setUp() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(compress(BYTES));
        stream = new DeflateCompressorInputStream(in);
    }
    
    @Test
    public void testRead() throws IOException {
        int b;
        int i = 0;
        while ((b = stream.read()) != -1) {
            assertEquals(BYTES[i], (byte) b);
            i++;
        }
        assertEquals(BYTES.length, i);
        
        stream.close();
    }
    
    @Test
    public void testRead_withBuffer() throws IOException {
        byte[] buffer = new byte[5];
        int bytesRead = stream.read(buffer, 0, buffer.length);
        assertEquals(buffer.length, bytesRead);
        assertArrayEquals(subArray(BYTES, 0, buffer.length), buffer);
        
        bytesRead = stream.read(buffer, 0, buffer.length);
        assertEquals(BYTES.length % buffer.length, bytesRead);
        assertArrayEquals(subArray(BYTES, buffer.length, BYTES.length), subArray(buffer, 0, bytesRead));
        
        bytesRead = stream.read(buffer, 0, buffer.length);
        assertEquals(-1, bytesRead);
        
        stream.close();
    }
    
    @Test
    public void testSkip() throws IOException {
        long bytesSkipped = stream.skip(5);
        assertEquals(5, bytesSkipped);
        
        byte[] buffer = new byte[3];
        int bytesRead = stream.read(buffer, 0, buffer.length);
        assertEquals(buffer.length, bytesRead);
        assertArrayEquals(subArray(BYTES, 5, 8), buffer);
        
        stream.close();
    }
    
    @Test
    public void testAvailable() throws IOException {
        int available1 = stream.available();
        assertEquals(BYTES.length, available1);
        
        byte[] buffer = new byte[5];
        stream.read(buffer, 0, buffer.length);
        
        int available2 = stream.available();
        assertEquals(BYTES.length - buffer.length, available2);
        
        stream.close();
    }
    
    @Test
    public void testClose() throws IOException {
        stream.close();
        assertEquals(-1, stream.read());
        
        try {
            stream.available();
            fail("Should have thrown IOException");
        } catch (IOException e) {
            // expected
        }
        
        try {
            stream.skip(5);
            fail("Should have thrown IOException");
        } catch (IOException e) {
            // expected
        }
    }
    
    @Test
    public void testWithZlibHeader() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(compress(BYTES));
        DeflateParameters parameters = new DeflateParameters();
        
        parameters.withZlibHeader(true);
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(in, parameters);
        assertTrue(stream.isZlibHeaderPresent());
        stream.close();
        
        parameters.withZlibHeader(false);
        stream = new DeflateCompressorInputStream(in, parameters);
        assertFalse(stream.isZlibHeaderPresent());
        stream.close();
    }
    
    @Test
    public void testInvalidSignature() throws IOException {
        byte[] invalidSignature = { 0x00, 0x00, 0x00, 0x00 };
        ByteArrayInputStream in = new ByteArrayInputStream(compress(BYTES));
        
        assertFalse(DeflateCompressorInputStream.isZlibHeader(invalidSignature, invalidSignature.length));
        
        assertFalse(DeflateCompressorInputStream.isZlibHeader(invalidSignature, 0));
        
        assertFalse(DeflateCompressorInputStream.isZlibHeader(invalidSignature, -1));
        
        assertTrue(DeflateCompressorInputStream.isZlibHeader(compress(BYTES), BYTES.length));
        
        assertFalse(DeflateCompressorInputStream.isZlibHeader(new byte[0], 0));
        
        assertFalse(DeflateCompressorInputStream.isZlibHeader(new byte[0], -1));
        
        in.close();
    }
    
    @Test
    public void testRead_afterClosingInputStream() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(compress(BYTES));
        DeflateCompressorInputStream stream = new DeflateCompressorInputStream(in);
        stream.close();
        
        try {
            stream.read();
            fail("Should have thrown IOException");
        } catch (IOException e) {
            // expected
        }
    }
    
    // Helper method to compress input bytes using DeflaterOutputStream
    private byte[] compress(byte[] inputBytes) throws IOException {
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(out);
        deflaterOutputStream.write(inputBytes);
        deflaterOutputStream.close();
        return out.toByteArray();
    }
    
    // Helper method to extract subarray
    private byte[] subArray(byte[] array, int startIndex, int endIndex) {
        byte[] subArray = new byte[endIndex - startIndex];
        System.arraycopy(array, startIndex, subArray, 0, subArray.length);
        return subArray;
    }
}