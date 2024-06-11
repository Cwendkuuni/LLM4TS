import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

public class BaseNCodecInputStreamTest {
    
    private BaseNCodecInputStream baseNCodecInputStream;
    
    @Before
    public void setUp() {
        // Create an instance of BaseNCodec to use for testing
        BaseNCodec baseNCodec = new BaseNCodec();
        // Create an instance of InputStream to use for testing
        InputStream inputStream = new ByteArrayInputStream("hello".getBytes());
        // Create an instance of BaseNCodecInputStream with doEncode set to true for testing read() method
        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);
    }
    
    @Test
    public void testReadWithValidData() throws IOException {
        int result = baseNCodecInputStream.read();
        assertEquals(104, result);
    }
    
    @Test
    public void testReadWithEmptyInputStream() throws IOException {
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream emptyBaseNCodecInputStream = new BaseNCodecInputStream(emptyInputStream, baseNCodec, true);
        
        int result = emptyBaseNCodecInputStream.read();
        assertEquals(-1, result);
    }
    
    @Test
    public void testReadWithMultipleBytes() throws IOException {
        byte[] buffer = new byte[5];
        int result = baseNCodecInputStream.read(buffer, 0, 5);
        
        assertEquals(5, result);
        assertArrayEquals(new byte[]{104, 101, 108, 108, 111}, buffer);
    }
    
    @Test(expected = NullPointerException.class)
    public void testReadWithNullBuffer() throws IOException {
        baseNCodecInputStream.read(null, 0, 5);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeOffset() throws IOException {
        byte[] buffer = new byte[5];
        baseNCodecInputStream.read(buffer, -1, 5);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        byte[] buffer = new byte[5];
        baseNCodecInputStream.read(buffer, 6, 5);
    }
    
    @Test
    public void testMarkSupported() {
        assertFalse(baseNCodecInputStream.markSupported());
    }
    
    @Test
    public void testAvailable() throws IOException {
        int result = baseNCodecInputStream.available();
        assertEquals(1, result);
    }
}