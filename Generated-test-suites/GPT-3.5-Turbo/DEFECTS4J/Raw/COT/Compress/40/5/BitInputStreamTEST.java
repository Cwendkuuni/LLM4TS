
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    private BitInputStream bitInputStream;

    @Before
    public void setUp() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0b10101010, 0b01010101});
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
    }

    @Test
    public void testClose() throws IOException {
        bitInputStream.close();
        // Check if the input stream is closed
        assertTrue(bitInputStream.in.read() == -1);
    }

    @Test
    public void testClearBitCache() throws IOException {
        // Read some bits to populate the cache
        bitInputStream.readBits(4);
        // Clear the cache
        bitInputStream.clearBitCache();

        // Try reading bits again and check if the cache is empty
        assertEquals(0, bitInputStream.readBits(4));
    }

    @Test
    public void testReadBits() throws IOException {
        // Read first 4 bits
        assertEquals(0b1010, bitInputStream.readBits(4));
        // Read next 4 bits
        assertEquals(0b1010, bitInputStream.readBits(4));
        // Read next 8 bits
        assertEquals(0b01010101, bitInputStream.readBits(8));
        // Read next 8 bits, should return -1 as end of stream is reached
        assertEquals(-1, bitInputStream.readBits(8));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithNegativeCount() throws IOException {
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithGreaterCount() throws IOException {
        bitInputStream.readBits(64);
    }
}
