import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    @Test
    public void testConstructor() {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;

        BitInputStream bitInputStream = new BitInputStream(in, byteOrder);

        assertNotNull(bitInputStream);
        assertEquals(in, bitInputStream.in);
        assertEquals(byteOrder, bitInputStream.byteOrder);
    }

    @Test
    public void testClose() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;

        BitInputStream bitInputStream = new BitInputStream(in, byteOrder);
        bitInputStream.close();
    }

    @Test
    public void testClearBitCache() {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;

        BitInputStream bitInputStream = new BitInputStream(in, byteOrder);

        bitInputStream.bitsCached = 123L;
        bitInputStream.bitsCachedSize = 7;

        bitInputStream.clearBitCache();

        assertEquals(0L, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

    @Test
    public void testReadBits() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[]{(byte) 0x80, (byte) 0x1F});
        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(in, byteOrder);

        long result = bitInputStream.readBits(20);

        assertEquals(0x8001FL, result);
    }

    @Test
    public void testReadBitsWithEndOfStream() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[]{(byte) 0x80, (byte) 0x1F});
        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(in, byteOrder);

        long result = bitInputStream.readBits(50);

        assertEquals(-1L, result);
    }
}