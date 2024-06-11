import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.junit.Test;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead_NormalInput() throws IOException {
        byte[] compressedData = new byte[]{120, -100, 101, 0, 0, -62, -65};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        
        assertEquals(120, deflateInputStream.read());
        assertEquals(-100, deflateInputStream.read());
        assertEquals(101, deflateInputStream.read());
        
        deflateInputStream.close();
    }

    @Test
    public void testRead_EmptyInput() throws IOException {
        byte[] compressedData = new byte[0];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        
        assertEquals(-1, deflateInputStream.read());
        
        deflateInputStream.close();
    }

    @Test
    public void testRead_LargeInput() throws IOException {
        byte[] compressedData = new byte[100000];
        for (int i = 0; i < compressedData.length; i++) {
            compressedData[i] = (byte) (i % 256);
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        
        for (int i = 0; i < compressedData.length; i++) {
            assertEquals(i % 256, deflateInputStream.read());
        }
        
        deflateInputStream.close();
    }

    @Test
    public void testRead_AvailableData() throws IOException {
        byte[] compressedData = new byte[]{120, -100, 101, 0, 0, -62, -65};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        
        byte[] buffer = new byte[3];
        int bytesRead = deflateInputStream.read(buffer);
        
        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[]{120, -100, 101}, buffer);
        
        deflateInputStream.close();
    }

    @Test
    public void testSkip_NormalInput() throws IOException {
        byte[] compressedData = new byte[]{120, -100, 101, 0, 0, -62, -65};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        
        long skippedBytes = deflateInputStream.skip(3);
        
        assertEquals(3, skippedBytes);
        assertEquals(101, deflateInputStream.read());
        
        deflateInputStream.close();
    }

    @Test
    public void testSkip_NegativeInput() throws IOException {
        byte[] compressedData = new byte[]{120, -100, 101, 0, 0, -62, -65};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        
        long skippedBytes = deflateInputStream.skip(-1);
        
        assertEquals(0, skippedBytes);
        assertEquals(120, deflateInputStream.read());
        
        deflateInputStream.close();
    }

    @Test
    public void testAvailable_NormalInput() throws IOException {
        byte[] compressedData = new byte[]{120, -100, 101, 0, 0, -62, -65};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        
        int availableBytes = deflateInputStream.available();
        
        assertEquals(7, availableBytes);
        
        deflateInputStream.close();
    }

    @Test
    public void testClose() throws IOException {
        byte[] compressedData = new byte[]{120, -100, 101, 0, 0, -62, -65};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        
        deflateInputStream.close();
        
        // Verify that the input stream is closed
        try {
            deflateInputStream.read();
            fail("IOException should be thrown");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testCheckSignature_ValidSignature() {
        byte[] signature = new byte[]{120, -100, 3};
        
        boolean isValidSignature = DeflateCompressorInputStream.checkSignature(signature, 3);
        
        assertTrue(isValidSignature);
    }

    @Test
    public void testCheckSignature_InvalidSignature() {
        byte[] signature = new byte[]{120, -100, 3};
        
        boolean isValidSignature = DeflateCompressorInputStream.checkSignature(signature, 2);
        
        assertFalse(isValidSignature);
    }
}