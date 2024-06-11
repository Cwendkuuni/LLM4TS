
import org.apache.commons.compress.utils.BitInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import static org.junit.Assert.*;
public class BitInputStreamTest {
    private BitInputStream bitInputStream;
    @Before
    public void setUp() {
        byte[] testData = {0b11001010, 0b01100101, 0b10101010};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
    }
    @After
    public void tearDown() throws IOException {
        bitInputStream.close();
    }
    @Test
    public void testReadBits() throws IOException {
        assertEquals(0b11001010, bitInputStream.readBits(8));
        assertEquals(0b01100101, bitInputStream.readBits(8));
        assertEquals(0b10101010, bitInputStream.readBits(8));
    }
    @Test
    public void testReadBitsWithInvalidCount() throws IOException {
        try {
            bitInputStream.readBits(-1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("count must not be negative or greater than 63", e.getMessage());
        }
        try {
            bitInputStream.readBits(64);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("count must not be negative or greater than 63", e.getMessage());
        }
    }
    @Test
    public void testReadBitsWhenEndOfStream() throws IOException {
        BitInputStream emptyStream = new BitInputStream(new ByteArrayInputStream(new byte[0]), ByteOrder.BIG_ENDIAN);
        assertEquals(-1, emptyStream.readBits(8));
    }
    @Test
    public void testClearBitCache() throws IOException {
        assertEquals(0b11001010, bitInputStream.readBits(8));
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.readBits(8));
    }
    @Test
    public void testClose() throws IOException {
        bitInputStream.close();
        try {
            bitInputStream.readBits(8);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Stream closed", e.getMessage());
        }
    }
}