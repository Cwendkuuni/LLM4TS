import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {
    
    private ByteArrayInputStream input;
    private BitInputStream bitInputStream;
    
    @Before
    public void setUp() {
        byte[] testData = new byte[]{0x12, 0x34, 0x56, 0x78};
        input = new ByteArrayInputStream(testData);
        bitInputStream = new BitInputStream(input, ByteOrder.BIG_ENDIAN);
    }

    @After
    public void tearDown() throws IOException {
        bitInputStream.close();
        input.close();
    }

    @Test
    public void testReadBits_typicalCase() throws IOException {
        long result = bitInputStream.readBits(16);
        assertEquals(0x1234, result);
        
        result = bitInputStream.readBits(16);
        assertEquals(0x5678, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_negativeCount() throws IOException {
        bitInputStream.readBits(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_countGreaterThanMax() throws IOException {
        bitInputStream.readBits(64);
    }
    
    @Test(expected = IOException.class)
    public void testReadBits_endOfStream() throws IOException {
        // Consume all bytes in the input stream
        bitInputStream.readBits(64);
        
        // Try to read more bits
        bitInputStream.readBits(1);
    }
    
    @Test
    public void testClearBitCache() {
        bitInputStream.clearBitCache();
        
        // Verify that the bit cache is empty
        assertEquals(0, bitInputStream.readBits(1));
    }
    
    @Test
    public void testClose() {
        // Verify that the input stream is closed after calling close method
        try {
             bitInputStream.close();
             assertTrue(input.available() == 0);
        } catch (IOException e) {
             fail("IOException occurred.");
        }
    }
}