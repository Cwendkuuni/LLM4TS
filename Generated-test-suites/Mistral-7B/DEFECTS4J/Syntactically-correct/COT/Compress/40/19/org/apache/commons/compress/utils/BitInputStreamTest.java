import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class BitInputStreamTest {

    @Test
    public void testBitInputStreamConstructor() {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        Assert.assertNotNull(bitInputStream);
    }

    @Test
    public void testCloseMethod() throws IOException {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        bitInputStream.close();
        Assert.assertThrows(IOException.class, () -> bitInputStream.readBits(1));
    }

    @Test
    public void testClearBitCacheMethod() throws IOException {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        long value = bitInputStream.readBits(10);
        bitInputStream.clearBitCache();
        Assert.assertEquals(0, bitInputStream.readBits(1));
    }

    @Test
    public void testReadBitsMethodWithValidCountAndByteOrderLittleEndian() throws IOException {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        long expectedValue = 0b11111111;
        long value = bitInputStream.readBits(7);
        Assert.assertEquals(expectedValue, value);
    }

    @Test
    public void testReadBitsMethodWithValidCountAndByteOrderBigEndian() throws IOException {
        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        long expectedValue = 0b00111111;
        long value = bitInputStream.readBits(7);
        Assert.assertEquals(expectedValue, value);
    }

    @Test
    public void testReadBitsMethodWithInvalidCount() throws IOException {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        Assert.assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(-1));
        Assert.assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(64));
    }

    @Test
    public void testReadBitsMethodWithEndOfStream() throws IOException {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        long value = bitInputStream.readBits(5);
        bitInputStream.close();
        Assert.assertEquals(-1, value);
    }
}