import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    @Test
    public void testReadBits() throws Exception {
        byte[] testData = {0x12, 0x34, 0x56};
        InputStream inputStream = new ByteArrayInputStream(testData);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        long result = bitInputStream.readBits(8);
        Assert.assertEquals(0x12, result);

        result = bitInputStream.readBits(8);
        Assert.assertEquals(0x34, result);

        result = bitInputStream.readBits(8);
        Assert.assertEquals(0x56, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsInvalidCount() throws Exception {
        byte[] testData = {0x12, 0x34, 0x56};
        InputStream inputStream = new ByteArrayInputStream(testData);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        bitInputStream.readBits(-1);
    }

    @Test
    public void testReadBitsEndOfStream() throws Exception {
        byte[] testData = {0x12, 0x34, 0x56};
        InputStream inputStream = new ByteArrayInputStream(testData);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        bitInputStream.readBits(24);
        long result = bitInputStream.readBits(8);
        Assert.assertEquals(-1, result);
    }

    @Test
    public void testClearBitCache() throws Exception {
        byte[] testData = {0x12, 0x34, 0x56};
        InputStream inputStream = new ByteArrayInputStream(testData);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        bitInputStream.readBits(16);
        bitInputStream.clearBitCache();

        long result = bitInputStream.readBits(8);
        Assert.assertEquals(0x12, result);
    }

    @Test
    public void testClose() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        bitInputStream.close();
    }
}