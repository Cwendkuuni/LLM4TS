import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    private BitInputStream bitInputStream;

    @Before
    public void setUp() {
        byte[] byteArray = new byte[]{0b00110101, 0b01101001, 0b10010011, 0b01000100, 0b10101010};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
    }

    @Test
    public void testReadBits_typicalCase() throws IOException {
        long bits = bitInputStream.readBits(16);
        assertEquals(0b0110100100110110, bits);
    }

    @Test
    public void testReadBits_edgeCase() throws IOException {
        long bits = bitInputStream.readBits(32);
        assertEquals(0b00110101011010011001010001000100, bits);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_invalidCount() throws IOException {
        bitInputStream.readBits(-1);
    }

    @Test
    public void testReadBits_endOfStream() throws IOException {
        bitInputStream.readBits(40);
        long bits = bitInputStream.readBits(8);
        assertEquals(-1, bits);
    }

    @Test
    public void testClearBitCache() throws IOException {
        bitInputStream.clearBitCache();

        long bits = bitInputStream.readBits(8);
        assertEquals(0b00110101, bits);
    }

    @Test
    public void testClose() throws IOException {
        bitInputStream.close();

        long bits = bitInputStream.readBits(8);
        assertEquals(-1, bits);
    }
}