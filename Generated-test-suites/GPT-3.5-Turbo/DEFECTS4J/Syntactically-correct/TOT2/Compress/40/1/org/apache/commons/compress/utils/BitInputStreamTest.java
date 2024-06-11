import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    @Test
    public void testClearBitCache() {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] {(byte)0b11010010, (byte)0b11101001});
        BitInputStream bitInputStream = new BitInputStream(bais, ByteOrder.BIG_ENDIAN);

        bitInputStream.readBits(4);
        bitInputStream.clearBitCache();

        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

    @Test
    public void testReadBits_Success_BigEndian() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] {(byte)0b11010010, (byte)0b11101001});
        BitInputStream bitInputStream = new BitInputStream(bais, ByteOrder.BIG_ENDIAN);

        long result = bitInputStream.readBits(9);

        assertEquals(0b110100101, result);
    }

    @Test
    public void testReadBits_Success_LittleEndian() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] {(byte)0b11010010, (byte)0b11101001});
        BitInputStream bitInputStream = new BitInputStream(bais, ByteOrder.LITTLE_ENDIAN);

        long result = bitInputStream.readBits(9);

        assertEquals(0b100101101, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_NegativeCount() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] {(byte)0b11010010, (byte)0b11101001});
        BitInputStream bitInputStream = new BitInputStream(bais, ByteOrder.BIG_ENDIAN);

        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_CountGreaterThanMax() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] {(byte)0b11010010, (byte)0b11101001});
        BitInputStream bitInputStream = new BitInputStream(bais, ByteOrder.BIG_ENDIAN);

        bitInputStream.readBits(64);
    }

    @Test
    public void testReadBits_EndOfStream() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] {(byte)0b11010010, (byte)0b11101001});
        BitInputStream bitInputStream = spy(new BitInputStream(bais, ByteOrder.BIG_ENDIAN));

        doReturn(-1).when(bitInputStream).read();

        long result = bitInputStream.readBits(9);

        assertEquals(-1, result);
    }
}