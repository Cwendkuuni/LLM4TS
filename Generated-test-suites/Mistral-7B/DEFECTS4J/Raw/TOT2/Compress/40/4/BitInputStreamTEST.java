import org.apache.commons.compress.utils.BitInputStream;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BitInputStreamTest {
    @Test
    public void testReadBits() throws Exception {
        BitInputStream testBitInputStream = new BitInputStream(System.in, ByteOrder.LITTLE_ENDIAN);
        long result = testBitInputStream.readBits(5);
        long expected = 0b101011; // Arbitrary value
        assertEquals(expected, result);
        testBitInputStream.close();
    }
}