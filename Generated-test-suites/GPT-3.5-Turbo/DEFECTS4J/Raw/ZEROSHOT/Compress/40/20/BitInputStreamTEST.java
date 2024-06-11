import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    @Test
    public void testReadBitsWithPositiveCount() throws IOException {
        byte[] data = {0x01, (byte)0xFF, 0x00};
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.BIG_ENDIAN);

        long result = bitInputStream.readBits(8);

        assertEquals(0x01, result);
    }

    @Test
    public void testReadBitsWithZeroCount() throws IOException {
        byte[] data = {0x01, (byte)0xFF, 0x00};
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.BIG_ENDIAN);

        long result = bitInputStream.readBits(0);

        assertEquals(0, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithNegativeCount() throws IOException {
        byte[] data = {0x01, (byte)0xFF, 0x00};
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.BIG_ENDIAN);

        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithCountGreaterThanMaximumCacheSize() throws IOException {
        byte[] data = {0x01, (byte)0xFF, 0x00};
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.BIG_ENDIAN);

        bitInputStream.readBits(64);
    }

    @Test
    public void testReadBitsWithEndOfStream() throws IOException {
        byte[] data = new byte[0];
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.BIG_ENDIAN);

        long result = bitInputStream.readBits(8);

        assertEquals(-1, result);
    }

    @Test
    public void testClearBitCache() throws IOException {
        byte[] data = {0x01, (byte)0xFF, 0x00};
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.BIG_ENDIAN);

        bitInputStream.readBits(8);
        bitInputStream.clearBitCache();

        long result = bitInputStream.readBits(8);

        assertEquals(0x01, result);
    }
}