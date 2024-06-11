import org.junit.Test;
import static org.junit.Assert.*;

public class BitInputStreamTest {

    @Test
    public void testConstructor() {
        InputStream in = new ByteArrayInputStream(new byte[10]);
        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(in, byteOrder);

        assertNotNull(bitInputStream);
        assertEquals(in, bitInputStream.in);
        assertEquals(byteOrder, bitInputStream.byteOrder);
    }
}