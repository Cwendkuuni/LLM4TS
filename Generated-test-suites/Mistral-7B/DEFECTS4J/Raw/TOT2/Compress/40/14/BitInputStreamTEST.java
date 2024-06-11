import org.apache.commons.compress.utils.BitInputStream;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BitInputStreamTest {
    @Test
    public void testReadBits() throws Exception {
        BitInputStream bitInputStream = new BitInputStream(System.in, ByteOrder.LITTLE_ENDIAN);
        long result = bitInputStream.readBits(5);
        long expected = 0b101101; // arbitrary values for testing
        assertEquals(expected, result);
        bitInputStream.close();
    }
}