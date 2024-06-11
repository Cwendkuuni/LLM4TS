import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Test class for BitInputStream
 */
public class BitInputStreamTest {

    private InputStream inputStream;
    private BitInputStream bitInputStream;

    @Before
    public void setUp() throws IOException {
        byte[] bytes = new byte[]{(byte) 0b11111111, (byte) 0b01111111, (byte) 0b10111111};
        inputStream = new ByteArrayInputStream(bytes);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
    }

    @After
    public void tearDown() throws IOException {
        bitInputStream.close();
        inputStream.close();
    }

    @Test
    public void testReadBits_byte() throws IOException {
        // test reading 1 bit
        long bit = bitInputStream.readBits(1);
        assertEquals(1L, bit);

        // test reading multiple bits from single byte
        long bits = bitInputStream.readBits(3);
        assertEquals(bits & 7L, 5L); // the 3 least significant bits

        // test reading one bit more than available
        try {
            long bit2 = bitInputStream.readBits(2);
            fail("Expected an IOException");
        } catch (IOException e) {
            assertEquals("No more bits available", e.getMessage());
        }
    }

    @Test
    public void testReadBits_bytes() throws IOException {
        // test reading multiple bytes and merging them
        long bits = bitInputStream.readBits(12);
        assertEquals(bits, (1L << 11) | (1L << 2) | 0b11111111); // 11111111 0011 1000

        // test reading multiple bytes and merging them in reverse order (big-endian)
        bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        bits = bitInputStream.readBits(12);
        assertEquals(bits, (1L << 7) | (1L << 3) | 0b01111111); // 01111111 0011 1111
    }

    @Test
    public void testClearBitCache() throws IOException {
        // test clearing cache and reading again
        assertEquals(1L, bitInputStream.readBits(1));
        bitInputStream.clearBitCache();
        assertEquals(-1L, bitInputStream.readBits(1)); // end of stream reached
        assertEquals(1L, bitInputStream.readBits(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_negativeCount() throws IOException {
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_countGreaterThanMaximum() throws IOException {
        bitInputStream.readBits(64);
    }

    @Test(expected = EOFException.class)
    public void testEndOfStream() throws IOException {
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        // end of stream reached
        assertEquals(-1L, bitInputStream.readBits(1));
    }
}