import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class BaseNCodecInputStreamTest {

    @Test
    public void testReadSingleByte() throws IOException {
        byte[] data = {65, 66, 67, 68, 69}; // ASCII codes for A, B, C, D, E
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        BaseNCodec baseNCodec = new BaseNCodec(true);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, true);
        
        // Read first byte
        int result = inputStream.read();
        assertEquals(65, result); // Expecting ASCII code for A
        
        // Read second byte
        result = inputStream.read();
        assertEquals(66, result); // Expecting ASCII code for B
        
        // Move stream pointer back to the beginning
        inputStream.reset();
        
        // Read first byte again
        result = inputStream.read();
        assertEquals(65, result); // Expecting ASCII code for A
        
        // Close the input stream
        inputStream.close();
    }
    
    @Test
    public void testReadMultipleBytes() throws IOException {
        byte[] data = {65, 66, 67, 68, 69}; // ASCII codes for A, B, C, D, E
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        BaseNCodec baseNCodec = new BaseNCodec(true);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, true);
        byte[] buffer = new byte[5];
        
        // Read all bytes in one read operation
        int bytesRead = inputStream.read(buffer);
        assertEquals(5, bytesRead); // Expecting all bytes to be read
        assertArrayEquals(data, buffer); // Expecting the same data
        
        // Close the input stream
        inputStream.close();
    }
    
    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] data = {65, 66, 67, 68, 69}; // ASCII codes for A, B, C, D, E
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        BaseNCodec baseNCodec = new BaseNCodec(true);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, true);
        byte[] buffer = new byte[5];
        
        // Read only 3 bytes with offset and length
        int bytesRead = inputStream.read(buffer, 1, 3);
        assertEquals(3, bytesRead); // Expecting 3 bytes to be read
        assertEquals(0, buffer[0]); // Expecting 0 at index 0
        assertEquals(65, buffer[1]); // Expecting ASCII code for A at index 1
        assertEquals(66, buffer[2]); // Expecting ASCII code for B at index 2
        assertEquals(67, buffer[3]); // Expecting ASCII code for C at index 3
        
        // Close the input stream
        inputStream.close();
    }
    
    @Test(expected = NullPointerException.class)
    public void testReadWithNullBuffer() throws IOException {
        byte[] data = {65, 66, 67, 68, 69}; // ASCII codes for A, B, C, D, E
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        BaseNCodec baseNCodec = new BaseNCodec(true);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, true);
        byte[] buffer = null;
        
        // Attempt to read with null buffer
        inputStream.read(buffer, 0, 5);
        
        // Close the input stream
        inputStream.close();
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        byte[] data = {65, 66, 67, 68, 69}; // ASCII codes for A, B, C, D, E
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        BaseNCodec baseNCodec = new BaseNCodec(true);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, true);
        byte[] buffer = new byte[5];
        
        // Attempt to read with invalid offset
        inputStream.read(buffer, -1, 5);
        
        // Close the input stream
        inputStream.close();
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws IOException {
        byte[] data = {65, 66, 67, 68, 69}; // ASCII codes for A, B, C, D, E
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        BaseNCodec baseNCodec = new BaseNCodec(true);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, true);
        byte[] buffer = new byte[5];
        
        // Attempt to read with invalid length
        inputStream.read(buffer, 0, 6);
        
        // Close the input stream
        inputStream.close();
    }

    @Test
    public void testMarkSupported() {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        BaseNCodec baseNCodec = new BaseNCodec(true);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, true);
        
        // Check if mark is supported
        boolean result = inputStream.markSupported();
        assertFalse(result);
        
        // Close the input stream
        inputStream.close();
    }
    
    @Test
    public void testReadEOF() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        BaseNCodec baseNCodec = new BaseNCodec(true);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, true);
        
        // Attempt to read from empty input stream
        int result = inputStream.read();
        assertEquals(-1, result); // Expecting -1 for EOF
        
        // Close the input stream
        inputStream.close();
    }
    
    @Test
    public void testReadResults() throws IOException {
        byte[] data = {65, 66, 67, 68, 69}; // ASCII codes for A, B, C, D, E
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        BaseNCodec baseNCodec = new BaseNCodec(true);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, true);
        byte[] buffer = new byte[5];
        
        // Read results until all data is read
        int totalBytesRead = 0;
        int bytesRead;
        while ((bytesRead = inputStream.readResults(buffer, totalBytesRead, 5 - totalBytesRead)) > 0) {
            totalBytesRead += bytesRead;
        }
        
        assertEquals(5, totalBytesRead); // Expecting all bytes to be read
        assertArrayEquals(data, buffer); // Expecting the same data
        
        // Close the input stream
        inputStream.close();
    }
    
}