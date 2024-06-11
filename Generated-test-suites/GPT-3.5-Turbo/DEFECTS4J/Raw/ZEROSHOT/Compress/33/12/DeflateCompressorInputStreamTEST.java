import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import static org.junit.Assert.assertEquals;

public class DeflateCompressorInputStreamTest {
    
    private DeflateCompressorInputStream inflater;
    private byte[] testData = { 0x78, 0x9C, (byte) 0xCB, 0x4C, 0x55, 0x28, 0xC9, (byte) 0x2C, 0x2A, 0xD4, (byte) 0xCB, 0x4C, 0x55, 0x28, (byte) 0xC9, 0x2C, 0x2A, 0x15, 0x00, (byte) 0x8E, (byte) 0xE4, (byte) 0xDF, 0x1B, 0x5E, (byte) 0xB0, 0x03, (byte) 0xA0, 0x16, 0x00, 0x00, 0x00 };
    
    @Before
    public void setUp() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(testData);
        inflater = new DeflateCompressorInputStream(inputStream);
    }
    
    @After
    public void tearDown() throws IOException {
        inflater.close();
    }
    
    @Test
    public void testRead() throws IOException {
        int expected = 0x78;
        int actual = inflater.read();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] buffer = new byte[5];
        int expectedCount = 5;
        int actualCount = inflater.read(buffer, 0, 5);
        assertEquals(expectedCount, actualCount);
        
        byte[] expectedBytes = { 0x9C, (byte) 0xCB, 0x4C, 0x55, 0x28 };
        byte[] actualBytes = new byte[5];
        System.arraycopy(buffer, 0, actualBytes, 0, 5);
        assertArrayEquals(expectedBytes, actualBytes);
    }
    
    @Test
    public void testSkip() throws IOException {
        long expected = 5;
        long actual = inflater.skip(5);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAvailable() throws IOException {
        int expected = 10;
        int actual = inflater.available();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testClose() throws IOException {
        inflater.close();
        // No assertion needed, close() should not throw an exception
    }
}