
import org.apache.commons.compress.utils.BitInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

import static org.junit.Assert.assertEquals;

public class BitInputStreamTest {

    private BitInputStream bitInputStream;

    private ByteArrayInputStream byteArrayInputStream;


    @Before
    public void setUp() {
        byte[] testData = {0b10101010, 0b01010101};
        byteArrayInputStream = new ByteArrayInputStream(testData);
        bitInputStream = new BitInputStream(byteArrayInputStream, ByteOrder.BIG_ENDIAN);
    }

    @After
    public void tearDown() throws IOException {
        bitInputStream.close();
        byteArrayInputStream.close();
    }

    @Test
    public void testReadBits_LittleEndian() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(byteArrayInputStream, ByteOrder.LITTLE_ENDIAN);
        long result = bitInputStream.readBits(8);
        assertEquals(0b10101010, result);
    }

    @Test
    public void testReadBits_BigEndian() throws IOException {
        long result = bitInputStream.readBits(8);
        assertEquals(0b10101010, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_NegativeCount() throws IOException {
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_InvalidCount() throws IOException {
        bitInputStream.readBits(64);
    }

    @Test
    public void testReadBits_EndOfStream() throws IOException {
        bitInputStream.readBits(16);
        long result = bitInputStream.readBits(8);
        assertEquals(-1, result);
    }

    @Test
    public void testClearBitCache() {
        bitInputStream.clearBitCache();
    }

}
