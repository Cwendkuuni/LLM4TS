import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

@RunWith(JUnit4.class)
public class BitInputStreamTest {
    
    private BitInputStream bitInputStream;
    
    @Before
    public void setUp() {
        // Create a sample byte array to provide as input stream
        byte[] sampleBytes = {0x08, 0x1A, (byte) 0xF3, (byte) 0xC7};
        ByteArrayInputStream sampleInputStream = new ByteArrayInputStream(sampleBytes);
        
        bitInputStream = new BitInputStream(sampleInputStream, ByteOrder.BIG_ENDIAN);
    }
    
    @Test
    public void testClearBitCache() {
        bitInputStream.clearBitCache();
        long result = bitInputStream.readBits(8);
        assertEquals(-1, result);
    }
    
    @Test
    public void testReadBitsEndOfStream() {
        long result = bitInputStream.readBits(64);
        assertEquals(-1, result);
    }
    
    @Test
    public void testReadBitsWithNegativeCount() {
        try {
            bitInputStream.readBits(-1);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // Test passed
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }
    
    @Test
    public void testReadBitsWithInvalidCount() {
        try {
            long result = bitInputStream.readBits(64);
            assertEquals(-1, result);
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException occurred");
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }
    
    @Test
    public void testReadBitsLittleEndian() {
        BitInputStream bitInputStreamLittleEndian = new BitInputStream(new ByteArrayInputStream(new byte[]{(byte) 0b11011000}), ByteOrder.LITTLE_ENDIAN);
        try {
            long result1 = bitInputStreamLittleEndian.readBits(3);
            assertEquals(6, result1);
            long result2 = bitInputStreamLittleEndian.readBits(3);
            assertEquals(3, result2);
            long result3 = bitInputStreamLittleEndian.readBits(2);
            assertEquals(1, result3);
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }
    
    @Test
    public void testReadBitsBigEndian() {
        BitInputStream bitInputStreamBigEndian = new BitInputStream(new ByteArrayInputStream(new byte[]{(byte) 0b11011000}), ByteOrder.BIG_ENDIAN);
        try {
            long result1 = bitInputStreamBigEndian.readBits(3);
            assertEquals(3, result1);
            long result2 = bitInputStreamBigEndian.readBits(3);
            assertEquals(6, result2);
            long result3 = bitInputStreamBigEndian.readBits(2);
            assertEquals(1, result3);
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }
}