
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.compress.utils.BitInputStream;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {
    private BitInputStream bitInputStream;

    @Before
    public void setUp() {
        byte[] data = {0b11110000, 0b10101010}; // sample data
        bitInputStream = new BitInputStream(new ByteArrayInputStream(data), ByteOrder.LITTLE_ENDIAN);
    }

    @Test
    public void testReadBits_positiveCount() throws IOException {
        long result1 = bitInputStream.readBits(2); // expects 0b00
        assertEquals(0b00, result1);

        long result2 = bitInputStream.readBits(4); // expects 0b1100
        assertEquals(0b1100, result2);

        long result3 = bitInputStream.readBits(8); // expects 0b10101010
        assertEquals(0b10101010, result3);

        long result4 = bitInputStream.readBits(1); // expects 0b0
        assertEquals(0b0, result4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_negativeCount() throws IOException {
        bitInputStream.readBits(-1);
        fail("Expected an IllegalArgumentException to be thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_largeCount() throws IOException {
        bitInputStream.readBits(64);
        fail("Expected an IllegalArgumentException to be thrown");
    }

    @Test
    public void testReadBits_endOfStream() throws IOException {
        long result1 = bitInputStream.readBits(16); // expects 0b1111000010101010
        assertEquals(0b1111000010101010, result1);

        long result2 = bitInputStream.readBits(8); // expects -1
        assertEquals(-1, result2);

        long result3 = bitInputStream.readBits(1); // expects -1
        assertEquals(-1, result3);
    }
}
