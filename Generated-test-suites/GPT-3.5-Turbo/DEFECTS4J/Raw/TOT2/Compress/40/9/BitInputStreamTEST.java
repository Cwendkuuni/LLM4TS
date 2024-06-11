import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {
  
    private BitInputStream bitInputStream;
  
    @Before
    public void setUp() {
        byte[] bytes = {0b01010101, 0b10101010, 0b11110000};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        bitInputStream = new BitInputStream(byteArrayInputStream, ByteOrder.BIG_ENDIAN);
    }
  
    @Test
    public void testClearBitCache() {
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.readBits(3));
    }

    @Test
    public void testReadBits_PositiveCount() {
        assertEquals(0b01010101L, bitInputStream.readBits(8));
        assertEquals(-1, bitInputStream.readBits(8));
    }

    @Test
    public void testReadBits_NegativeCount() {
        try {
            bitInputStream.readBits(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("count must not be negative or greater than 63", e.getMessage());
        } catch (IOException e) {
            fail("Expected IllegalArgumentException");
        }
    }

    @Test
    public void testReadBits_MaximumCount() {
        assertEquals(0b01010101L, bitInputStream.readBits(8));
        assertEquals(0b10101010L, bitInputStream.readBits(8));
        assertEquals(0b11110000L, bitInputStream.readBits(7));
        assertEquals(-1, bitInputStream.readBits(1));
    }
  
    @Test
    public void testReadBits_EndOfStream() {
        try {
            bitInputStream.readBits(24);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }
  
    @Test
    public void testConstructor() {
        assertNotNull(bitInputStream);
    }
  
    @Test
    public void testClose() {
        try {
            bitInputStream.close();
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }
}