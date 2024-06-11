import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

import static org.junit.Assert.*;

public class BitInputStreamTest {

    private BitInputStream bitInputStream;

    @Before
    public void setup() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05});
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
    }

    @Test
    public void testReadBits_SingleByte_LittleEndian() throws IOException {
        assertEquals(0, bitInputStream.readBits(0));
        assertEquals(0, bitInputStream.readBits(1));
        assertEquals(1, bitInputStream.readBits(1));
        assertEquals(3, bitInputStream.readBits(2));
        assertEquals(14, bitInputStream.readBits(4));
        assertEquals(1057, bitInputStream.readBits(10));
        assertEquals(514, bitInputStream.readBits(9));
        assertEquals(0, bitInputStream.readBits(10));
        assertEquals(0, bitInputStream.readBits(9));
        assertEquals(-1, bitInputStream.readBits(1));
    }

    @Test
    public void testReadBits_SingleByte_BigEndian() throws IOException {
        bitInputStream = new BitInputStream(new ByteArrayInputStream(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05}), ByteOrder.BIG_ENDIAN);
        assertEquals(0, bitInputStream.readBits(0));
        assertEquals(0, bitInputStream.readBits(1));
        assertEquals(0, bitInputStream.readBits(2));
        assertEquals(0, bitInputStream.readBits(3));
        assertEquals(0, bitInputStream.readBits(4));
        assertEquals(17, bitInputStream.readBits(5));
        assertEquals(272, bitInputStream.readBits(9));
        assertEquals(136, bitInputStream.readBits(8));
        assertEquals(2, bitInputStream.readBits(1));
        assertEquals(0, bitInputStream.readBits(1));
        assertEquals(-1, bitInputStream.readBits(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_InvalidCount_Negative() throws IOException {
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_InvalidCount_ExceedMaximum() throws IOException {
        bitInputStream.readBits(64);
    }

    @Test
    public void testClearBitCache() {
        bitInputStream.readBits(10);
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.readBits(1));
        assertEquals(0, bitInputStream.readBits(2));
        assertEquals(0, bitInputStream.readBits(3));
    }

    @Test
    public void testClose() throws IOException {
        bitInputStream.close();
    }
}