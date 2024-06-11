import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BitInputStreamTest {
    
    private BitInputStream bitInputStream;
    private ByteArrayInputStream byteArrayInputStream;

    @Before
    public void setUp() {
        byteArrayInputStream = new ByteArrayInputStream(new byte[] {0b11001010, 0b00111000});
        bitInputStream = new BitInputStream(byteArrayInputStream, ByteOrder.BIG_ENDIAN);
    }

    @After
    public void tearDown() throws IOException {
        bitInputStream.close();
    }

    @Test
    public void testReadBits_positiveCount() throws IOException {
        long result = bitInputStream.readBits(4);
        assertEquals(0b1100, result);
        result = bitInputStream.readBits(8);
        assertEquals(0b10100011, result);
    }

    @Test
    public void testReadBits_negativeCount() throws IOException {
        try {
            bitInputStream.readBits(-1);
        } catch (IllegalArgumentException e) {
            assertEquals("count must not be negative or greater than 63", e.getMessage());
        }
    }

    @Test
    public void testReadBits_largerCount() throws IOException {
        try {
            bitInputStream.readBits(64);
        } catch (IllegalArgumentException e) {
            assertEquals("count must not be negative or greater than 63", e.getMessage());
        }
    }

    @Test
    public void testReadBits_endOfStream() throws IOException {
        long result1 = bitInputStream.readBits(4);
        long result2 = bitInputStream.readBits(8);
        long result3 = bitInputStream.readBits(8);
        long result4 = bitInputStream.readBits(8);
        assertEquals(0b1100, result1);
        assertEquals(0b10100011, result2);
        assertEquals(-1, result3);
        assertEquals(-1, result4);
    }

    @Test
    public void testClearBitCache() throws IOException {
        bitInputStream.readBits(4);
        bitInputStream.clearBitCache();
        long result = bitInputStream.readBits(4);
        assertEquals(0b1100, result);
    }

    @Test
    public void testReadBits_littleEndian() throws IOException {
        bitInputStream = new BitInputStream(byteArrayInputStream, ByteOrder.LITTLE_ENDIAN);
        long result = bitInputStream.readBits(4);
        assertEquals(0b1010, result);
        result = bitInputStream.readBits(8);
        assertEquals(0b11001010, result);
    }
}