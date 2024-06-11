import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test class for BitInputStream.
 */
public class BitInputStreamTest {

    private InputStream inputStream;

    @Before
    public void setUp() {
        byte[] testData = new byte[]{(byte) 0b11111111, (byte) 0b01111111};
        inputStream = new ByteArrayInputStream(testData);
    }

    @After
    public void tearDown() throws IOException {
        if (inputStream != null) {
            inputStream.close();
        }
    }

    @Test
    public void testBitInputStreamCreation() {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        assertEquals(ByteOrder.LITTLE_ENDIAN, byteOrder);
        assertEquals(inputStream, bitInputStream.in);
    }

    @Test
    public void testReadBitsWithValidCount() throws IOException {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        // read the first byte
        long bitsRead = bitInputStream.readBits(8);
        assertEquals(-1, bitsRead);

        // read 1 bit
        bitsRead = bitInputStream.readBits(1);
        assertEquals(0b10000000 & inputStream.read(), bitsRead);

        // read 6 bits
        bitsRead = bitInputStream.readBits(6);
        assertEquals(0b111111 & inputStream.read(), bitsRead << 2);

        // read 3 bits
        bitsRead = bitInputStream.readBits(3);
        assertEquals(0b111 & inputStream.read(), bitsRead);

        // read 5 bits
        bitsRead = bitInputStream.readBits(5);
        assertEquals(0b11111 & inputStream.read(), bitsRead << 3);

        // read remaining 3 bits from the second byte
        bitsRead = bitInputStream.readBits(3);
        assertEquals(0b111 & inputStream.read(), bitsRead);

        // try to read more bits than available
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(10));
    }

    @Test
    public void testReadBitsWithInvalidCount() throws IOException {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        // try to read a negative number of bits
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(-5));

        // try to read more bits than available in the first byte
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(9));

        // try to read more bits than available in the second byte
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(4));
    }

    @Test
    public void testClearBitCache() throws IOException {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);

        // read the first byte and 6 bits
        bitInputStream.readBits(8);
        long bitsRead = bitInputStream.readBits(6);
        assertEquals(0b111111 & inputStream.read(), bitsRead << 2);

        // clear the cache
        bitInputStream.clearBitCache();

        // read the second byte and 3 bits
        bitsRead = bitInputStream.readBits(3);
        assertEquals(0b111 & inputStream.read(), bitsRead);

        // the cache should be empty now
        assertEquals(0, bitInputStream.bitsCachedSize);
        assertEquals(0, bitInputStream.bitsCached);
    }
}