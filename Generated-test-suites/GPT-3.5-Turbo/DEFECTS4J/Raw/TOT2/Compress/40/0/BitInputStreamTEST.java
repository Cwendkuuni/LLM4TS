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
        byte[] byteArray = {0x12, 0x34, 0x56, 0x78, 0x9A};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
    }

    @Test
    public void testClearBitCache() {
        bitInputStream.readBits(16);
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.readBits(1));
    }

    @Test
    public void testReadBitsPositive() throws IOException {
        assertEquals(0x1234, bitInputStream.readBits(16));
        assertEquals(0x5678, bitInputStream.readBits(16));
    }

    @Test
    public void testReadBitsEndOfStream() throws IOException {
        assertEquals(-1, bitInputStream.readBits(40));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsNegativeCount() throws IOException {
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsExceedMaxCount() throws IOException {
        bitInputStream.readBits(64);
    }

    @Test
    public void testReadBitsEdgeCase() throws IOException {
        assertEquals(0x12, bitInputStream.readBits(4));
        assertEquals(0x34, bitInputStream.readBits(7));
        assertEquals(0x56, bitInputStream.readBits(7));
    }

}