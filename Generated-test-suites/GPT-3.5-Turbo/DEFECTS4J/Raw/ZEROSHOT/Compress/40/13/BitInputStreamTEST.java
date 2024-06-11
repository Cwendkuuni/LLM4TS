import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    @Test
    public void testReadBits() {
        byte[] bytes = {0b11111111, 0b00000000};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);

        try {
            Assert.assertEquals(255L, bitInputStream.readBits(8));
            Assert.assertEquals(0L, bitInputStream.readBits(8));
            Assert.assertEquals(-1L, bitInputStream.readBits(8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testClearBitCache() {
        byte[] bytes = {0b11111111, 0b00000000};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);

        try {
            bitInputStream.readBits(8);
            bitInputStream.clearBitCache();
            Assert.assertEquals(255L, bitInputStream.readBits(8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadBitsOutOfRange() {
        byte[] bytes = {0b11111111, 0b00000000};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);

        try {
            bitInputStream.readBits(64);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("count must not be negative or greater than 63", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadBitsEndOfStream() {
        byte[] bytes = {0b11111111};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);

        try {
            Assert.assertEquals(-1L, bitInputStream.readBits(16));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}