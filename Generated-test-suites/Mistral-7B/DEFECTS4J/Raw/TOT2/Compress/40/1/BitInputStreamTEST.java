// BitInputStreamTest.java (Part 1)
import org.apache.commons.compress.utils.BitInputStream;
import org.junit.Test;
import static org.junit.Assert.*;

public class BitInputStreamTest {

    @Test
    public void testReadBits_ValidArguments_ReturnsExpectedValue() throws Exception {
        BitInputStream bitInputStream = new BitInputStream(System.in, ByteOrder.LITTLE_ENDIAN);
        final int count = 5;
        long expected = 15L; // 1111 in binary, 5 bits
        long actual = bitInputStream.readBits(count);
        assertEquals(expected, actual);
    }

    // Next expert will add edge cases and exception handling tests.
}